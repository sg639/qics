package com.optidpp.zrfc;

import java.util.HashMap;

public class ExportParameterDTO {

	private boolean isError = false;
	private String errorCode;
	private String errorMessage;
	
	//private HashMap<String, OutParameter> exportParameterData = new HashMap<String, OutParameter>();
	private HashMap<String, RFCCommonCollect> exportMapData = new HashMap<String, RFCCommonCollect>();
	
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/*
	public HashMap<String, OutParameter> getExportParameterData() {
		return exportParameterData;
	}
	public void setExportParameterData(HashMap<String, OutParameter> exportParameterData) {
		this.exportParameterData = exportParameterData;
	}
	*/
	public HashMap<String, RFCCommonCollect> getExportMapData() {
		return exportMapData;
	}
	public void setExportMapData(HashMap<String, RFCCommonCollect> exportMapData) {
		this.exportMapData = exportMapData;
	}
		
	
}
