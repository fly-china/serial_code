package com.lpf.common.page;

import java.util.List;

@SuppressWarnings("rawtypes")
public class DataTableData {
		private long iTotalRecords;
		private long iTotalDisplayRecords;
		private String sEcho;
		private List aData;

		public DataTableData() {
		}

		public DataTableData(long iTotalRecords, long iTotalDisplayRecords, String sEcho,List aData) {
			this.iTotalRecords = iTotalRecords;
			this.iTotalDisplayRecords = iTotalDisplayRecords;
			this.sEcho = sEcho;
			this.aData = aData;
		}
		
		public long getiTotalRecords() {
			return iTotalRecords;
		}

		public void setiTotalRecords(long iTotalRecords) {
			this.iTotalRecords = iTotalRecords;
		}

		public long getiTotalDisplayRecords() {
			return iTotalDisplayRecords;
		}

		public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
			this.iTotalDisplayRecords = iTotalDisplayRecords;
		}

		public String getsEcho() {
			return sEcho;
		}

		public void setsEcho(String sEcho) {
			this.sEcho = sEcho;
		}

		public List getaData() {
			return aData;
		}

		public void setaData(List aData) {
			this.aData = aData;
		}

//		public String[][] getAaData() {
//			return aaData;
//		}
//
//		public void setAaData(String[][] aaData) {
//			this.aaData = aaData;
//		}


}
