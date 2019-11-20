package com.lpf.common.util;


import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * SFTP 工具类
 */
public class SftpUtils {
	private ChannelSftp channel;
	private final Logger logger = LoggerFactory.getLogger(SftpUtils.class);
	private String host;
	private String username;
	private String password;
	private int port = 26;
	private ChannelSftp sftp = null;
	private Session sshSession = null;

	public SftpUtils() {
	}

	public SftpUtils(String host, String username, String password, int port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public SftpUtils(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	/**
	 * connect server via sftp
	 */
	public void connect() {
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			//System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			//System.out.println("Session connected.");
			//System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			//System.out.println("Connected to " + host + ".");
		} catch (Exception e) {
			logger.error("SFTP连接时发生异常",e);
		}
	}

	/**
	 * 关闭资源
	 */
	public void disconnect() {
		if (this.sftp != null) {
			if (this.sftp.isConnected()) {
				this.sftp.disconnect();
				//System.out.println("sftp is closed already");
			}
		}

		if (this.sshSession != null) {
			if (this.sshSession.isConnected()) {
				this.sshSession.disconnect();
				//System.out.println("sshSession is closed already");
			}

		}

	}
	
	public InputStream downFile(String remotePath,String remoteFile){
		try {
			sftp.cd(remotePath);
			return sftp.get(remoteFile);
		} catch (SftpException e) {
			logger.error("文件下载失败或文件不存在！",e.getMessage());
		}
		  return null;
	}

	/**
	 * 批量下载文件
	 * 
	 * @param remotPath
	 *            远程下载目录(以路径符号结束)
	 * @param localPath
	 *            本地保存目录(以路径符号结束)
	 * @param fileFormat
	 *            下载文件格式(以特定字符开头,为空不做检验)
	 * @param del
	 *            下载后是否删除sftp文件
	 * @return
	 */
	public boolean batchDownLoadFile(String remotPath, String localPath,
			String fileFormat, boolean del) {
		try {
			connect();
			Vector v = listFiles(remotPath);
			if (v.size() > 0) {

				Iterator it = v.iterator();
				while (it.hasNext()) {
					LsEntry entry = (LsEntry) it.next();
					String filename = entry.getFilename();
					SftpATTRS attrs = entry.getAttrs();
					if (!attrs.isDir()) {
						if (fileFormat != null && !"".equals(fileFormat.trim())) {
							if (filename.startsWith(fileFormat)) {
								if (this.downloadFile(remotPath, filename,
										localPath, filename)
										&& del) {
									deleteSFTP(remotPath, filename);
								}
							}
						} else {
							if (this.downloadFile(remotPath, filename,
									localPath, filename)
									&& del) {
								deleteSFTP(remotPath, filename);
							}
						}
					}
				}
			}
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
		return false;
	}

	/**
	 * 下载单个文件
	 * 
	 * @param remoteFileName
	 *            下载文件名
	 * @param localPath
	 *            本地保存目录(以路径符号结束)
	 * @param localFileName
	 *            保存文件名
	 * @return
	 */
	public boolean downloadFile(String remotePath, String remoteFileName,
			String localPath, String localFileName) {
		try {
			sftp.cd(remotePath);
			File file = new File(localPath + localFileName);
			mkdirs(localPath + localFileName);
			sftp.get(remoteFileName, new FileOutputStream(file));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		}

		return false;
	}

    /**
     * 判断文件是否存在
     *<p>Description:</p>
     * @Title: isFileExist
     * @return
     * @throws SftpException
     * @author suxx
     */
    public boolean isFileExist (String filePath,String fileName) throws SftpException {
        boolean isExitFlag = false;

        if(!isDirExist(filePath))  return isExitFlag;

        String fileFullName = null;

        char lastPathChar = filePath.charAt(filePath.length()-1);
        if((File.separatorChar != lastPathChar) && ('/' != lastPathChar))
            fileFullName = filePath + "/" + fileName;
        else
            fileFullName = filePath+fileName;

        try{
            InputStream inputStream = sftp.get(fileFullName);
            if(inputStream != null)
                return true;
            else
                return false;
        }catch (SftpException ex){
            if(ex.id == ChannelSftp.SSH_FX_NO_SUCH_FILE)
                return false;
            else
                throw ex;
        }
    }

    /**
	 *
	 * 上传
	 *
	 */

	 public void uploadFile(String remotePath, String fileName,InputStream input) throws IOException,Exception {
	        try {
	        	sftp.cd(remotePath);
	            sftp.put(input, fileName);
	        } catch (Exception e) {
	            logger.error("文件上传异常！", e);
	            e.printStackTrace();
	            throw new Exception("文件上传异常");
	        } finally {
	        	if(input!=null){
					try{
						input.close();
					}catch(Exception e){
					}
				}
	            disconnect();
	        }
	    }



	/**
	 * 上传单个文件
	 *
	 * @param remotePath
	 *            远程保存目录
	 * @param remoteFileName
	 *            保存文件名
	 * @param localPath
	 *            本地上传目录(以路径符号结束)
	 * @param localFileName
	 *            上传的文件名
	 * @return
	 */
	public boolean uploadFile(String remotePath, String remoteFileName,
			String localPath, String localFileName) {
		FileInputStream in = null;
		try {
			createDir(remotePath);
			File file = new File(localPath + localFileName);
			in = new FileInputStream(file);
			sftp.put(in, remoteFileName);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

    /**
     * 上传单个文件
     *
     * @param remotePath
     *            远程保存目录
     * @param remoteFileName
     *            保存文件名
     * @param fileInput
     *            上传的文件
     * @return
     */
    public boolean uploadFile(String remotePath, String remoteFileName,File fileInput) {
        FileInputStream in = null;
        try {
            createDir(remotePath);
            in = new FileInputStream(fileInput);
            sftp.put(in, remoteFileName);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

	/**
	 * 批量上传文件
	 *
	 * @param remotePath
	 *            远程保存目录
	 * @param localPath
	 *            本地上传目录(以路径符号结束)
	 * @param del
	 *            上传后是否删除本地文件
	 * @return
	 */
	public boolean bacthUploadFile(String remotePath, String localPath,
			boolean del) {
		try {
			connect();
			File file = new File(localPath);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()
						&& files[i].getName().indexOf("bak") == -1) {
					if (this.uploadFile(remotePath, files[i].getName(),
							localPath, files[i].getName())
							&& del) {
						deleteFile(localPath + files[i].getName());

					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
		return false;

	}

	/**
	 * 删除本地文件
	 *
	 * @param filePath
	 * @return
	 */
	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}

		if (!file.isFile()) {
			return false;
		}

		return file.delete();
	}

	/**
	 * 创建目录
	 *
	 * @param createpath
	 * @return
	 */
	public boolean createDir(String createpath) {
		try {
			if (isDirExist(createpath)) {
				this.sftp.cd(createpath);
				return true;
			}
			String pathArry[] = createpath.split("/");
			StringBuffer filePath = new StringBuffer("/");
			for (String path : pathArry) {
				if (path.equals("")) {
					continue;
				}
				filePath.append(path + "/");
				if (isDirExist(filePath.toString())) {
					sftp.cd(filePath.toString());
				} else {
					// 建立目录
					sftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
					sftp.cd(filePath.toString());
				}

			}
			this.sftp.cd(createpath);
			return true;
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断目录是否存在
	 *
	 * @param directory
	 * @return
	 */
	public boolean isDirExist(String directory) {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = sftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}

	/**
	 * 删除stfp文件
	 *
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 */
	public void deleteSFTP(String directory, String deleteFile) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果目录不存在就创建目录
	 *
	 * @param path
	 */
	public void mkdirs(String path) {
		File f = new File(path);

		String fs = f.getParent();

		f = new File(fs);

		if (!f.exists()) {
			f.mkdirs();
		}
	}

	/**
	 * 列出目录下的文件
	 *
	 * @param directory
	 *            要列出的目录
	 * @return
	 * @throws SftpException
	 */
	public Vector listFiles(String directory) throws SftpException {
		return sftp.ls(directory);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ChannelSftp getSftp() {
		return sftp;
	}

	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}

	public static void main(String[] args) {
		diaoyong();
	}

	private static void diaoyong() {
		SftpUtils ftp = new SftpUtils("192.168.201.40", "sftp_root", "sftp_root123",26);
		String localPath = "C:\\";
		String remotePath = "/billfiles/22315875/";
		ftp.connect();

//		ftp.uploadFile(remotePath, "test22.txt", localPath, "recon1.txt");
//		ftp.bacthUploadFile(remotePath,localPath,true);
	    ftp.downloadFile(remotePath, "recon.txt", localPath, "test222.txt");
//		ftp.batchDownLoadFile(remotePath, localPath, null, true);

		ftp.disconnect();
		System.exit(0);
	}

	//
	public  InputStream downloadFileToStream(String remotePath, String remoteFileName) {
		InputStream in = null;	
		try {
				sftp.cd(remotePath);
				in = sftp.get( remoteFileName);
			} catch (SftpException e) {
				e.printStackTrace();
			}
		return in;
	}
	//
	public boolean uploadFile(String localPath,InputStream in  ) {
		try {
			File file = new File(localPath );
			if(!file.exists()){
				file.createNewFile();
			}
			sftp.put(in, localPath);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

}
