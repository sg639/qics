package com.optidpp.zrfc;

import java.io.Serializable;
import java.util.ArrayList;


public class InParameter implements Serializable{
	private String RfcFnName;
	private String Name;
	private String ExportTableName;
	private String ImportTableName;
	private String inputType;
	private ArrayList inputData;
	
	public String getRfcFnName() {
		return RfcFnName;
	}
	public void setRfcFnName(String rfcFnName) {
		RfcFnName = rfcFnName;
	}
	
	public String getImportTableName() {
		return ImportTableName;
	}
	public void setImportTableName(String importTableName) {
		ImportTableName = importTableName;
	}
	public void setName(String name){
		this.Name = name;
	}
	public void setExportTableName(String exportTableName){
		this.ExportTableName = exportTableName;
	}
		
	public void setInputType(String type){
		this.inputType = type;
	}
	
	public void setInputData(ArrayList data){
		this.inputData = data;
	}
	
	public String getName(){
		return this.Name;
	}

	public String getExportTableName(){
		return this.ExportTableName;
	}

	
	
	public String getInputType(){
		return this.inputType;
	}
	
	public ArrayList getInputData(){
		return this.inputData;
	}
}
