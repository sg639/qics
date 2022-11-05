package com.optidpp.zrfc;

import java.util.ArrayList;

import com.optidpp.zrfc.common.ConfigValue;

public class MessengerVO {

	private boolean isError = false;
	private String errorCode;
	private String errorMessage;
	
	private String strSENDER;		
	private String strTARGET;
	private String strSUB;
	private String strCONT;
	private String strCONT_eng;
	private String strSYSTYPE;
	private String strURL;
	
	private ArrayList<String> strTARGETList;
	
	public MessengerVO() {
		this.strTARGETList = new ArrayList<String>();
		this.strSYSTYPE="sign";
		
	}

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

	public String getStrSENDER() {
		return strSENDER;
	}

	public void setStrSENDER(String strSENDER) {
		this.strSENDER = strSENDER;
	}

	public String getStrTARGET() {
		return strTARGET;
	}

	public void setStrTARGET(String strTARGET) {
		this.strTARGET = strTARGET;
	}

	public String getStrSUB() {
		return strSUB;
	}

	public void setStrSUB(String strSUB) {
		this.strSUB = strSUB;
	}

	public String getStrCONT() {
		return strCONT;
	}

	public void setStrCONT(String strCONT) {
		this.strCONT = strCONT;
	}

	public String getStrCONT_eng() {
		return strCONT_eng;
	}

	public void setStrCONT_eng(String strCONT_eng) {
		this.strCONT_eng = strCONT_eng;
	}

	public String getStrSYSTYPE() {
		return strSYSTYPE;
	}

	public void setStrSYSTYPE(String strSYSTYPE) {
		this.strSYSTYPE = strSYSTYPE;
	}

	public String getStrURL() {
		return strURL;
	}

	public void setStrURL(String strURL) {
		this.strURL = strURL;
	}

	public ArrayList<String> getStrTARGETList() {
		return strTARGETList;
	}

	public void setStrTARGETList(ArrayList<String> strTARGETList) {
		this.strTARGETList = strTARGETList;
	}
}
