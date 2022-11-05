package com.optidpp.zrfc;

import java.io.Serializable;
import java.util.ArrayList;



public class OutParameter implements Serializable{
	private String RfcFnName;
	private String Name;
	private String outputType;
	private ArrayList outputData;
	
	public String getRfcFnName() {
		return RfcFnName;
	}
	public void setRfcFnName(String rfcFnName) {
		RfcFnName = rfcFnName;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getOutputType() {
		return outputType;
	}
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}
	public ArrayList getOutputData() {
		return outputData;
	}
	public void setOutputData(ArrayList outputData) {
		this.outputData = outputData;
	}

}
