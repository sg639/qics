package com.optidpp.zrfc;

import java.util.ArrayList;


/**
 * 
 * @author 
 */
public abstract class Connector {
	/**
	 * init
	 *
	 */
	public abstract void init();
	
	/**
	 * RFC Connect
	 *
	 */
	public abstract void connect();
	
	/**
	 * process
	 * @param rfcName
	 */
	public abstract ArrayList process(String rfcName, ArrayList inParameterList);
	
	/**
	 * process
	 * @param name
	 */
	public abstract ArrayList process(String name);
	
	/**
	 * get Export, Table data
	 * @return
	 */
	public ArrayList getOutputData(){
		ArrayList data = null;
		try{
			data = getDataArray();
		}catch(Exception e){}
		
		return data;
	}
	
	public void setInputData(ArrayList inParameterList){
		if(inParameterList != null){
			for(int i = 0; i < inParameterList.size(); i++){
				InParameter data = (InParameter)inParameterList.get(i);
				String strName = data.getName();
				String strType = data.getInputType();
				
				if(strType.equals("FIELD")){
					setFieldData(strName, data.getInputData());
				}else if(strType.equals("STRUCTURE")){
					setStructureData(strName, data.getInputData());
				}else if(strType.equals("TABLE")){
					setTableData(strName, data.getInputData());
				}else if(strType.equals("FIELDIN")){
					setFieldInData(strName, data.getInputData());
				}else if(strType.equals("FIELDOUT")){
					setFieldOutData(strName, data.getInputData());
				}
			}
		}
	}
	
	public abstract void setFieldData(String name, ArrayList data);
	
	public abstract void setStructureData(String name, ArrayList data);
	
	public abstract void setTableData(String name, ArrayList data);
	
	public abstract void setFieldInData(String name, ArrayList data);
	
	public abstract void setFieldOutData(String name, ArrayList data);
	
	public abstract ArrayList getDataArray();
	/**
	 * RFC Close
	 *
	 */
	public abstract boolean close();
}
