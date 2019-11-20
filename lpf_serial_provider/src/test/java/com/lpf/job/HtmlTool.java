package com.lpf.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ishare on 17-6-15.
 * 去掉html标签
 */
public class HtmlTool {

    public static String tag(String str){
        String regEx_html="<[^>]+>";
        Pattern pattern=Pattern.compile(regEx_html);
        Matcher matcher=pattern.matcher(str);
        str=matcher.replaceAll("");
        return str.replaceAll("&nbsp"," ");
    }
}
