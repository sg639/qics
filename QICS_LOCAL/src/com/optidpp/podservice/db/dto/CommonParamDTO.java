package com.optidpp.podservice.db.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CommonParamDTO extends AbstractDataTransObj implements Serializable{
    
    public ArrayList 	typeList;
    public ArrayList 	valueList;
    public ArrayList 	preStatIdxList;
    
    public ArrayList    inParamList;
    public ArrayList    inParamIdxList;
    
    public ArrayList 	trackList;
    public ArrayList 	trackPositionList;
    
    public String		insertFile;
    public String 		updateFile;
    public String		deleteFile;
    
    public boolean		sqlAppend = false;
    
    public CommonParamDTO(){
        init();
    }
    
    public void setPath(String directory, String file)  throws Exception{

        try{
        	init();
	        this.directory = directory;
	        this.file      = file;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
    public CommonParamDTO(String[] status){
        init();
    }
    
    public void setParam(String paramType, String paramValue, int preStatIdx) throws Exception{

        try{
	        paramType = paramType.toUpperCase();
	        setType(preStatIdx, paramType);
	        setValue(preStatIdx, paramValue);
	        setPreStatIdx(preStatIdx, String.valueOf(preStatIdx));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
 	}
    
    public void setComboParam(String paramType, String paramValue, int preStatIdx) throws Exception{

        try{
	        paramType = paramType.toUpperCase().trim();
	        setType(preStatIdx, paramType);
	        if(paramValue.toUpperCase().equals("ALL") || 
               paramValue.toUpperCase().equals("NULL") ||
               paramValue.toUpperCase().equals(""))
	        {
	            paramValue = "%%";
	        }
	        setValue(preStatIdx, paramValue);
	        setPreStatIdx(preStatIdx, String.valueOf(preStatIdx));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
 	}

    public void setInParam(String paramType, String paramValue, int preStatIdx) throws Exception{

        try{
	        paramType = paramType.toUpperCase().trim();
	        //setType(preStatIdx, paramType);
	        setInParam(preStatIdx, paramValue);
	        setInParamIdx(preStatIdx, String.valueOf(preStatIdx));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
 	}

    
    public void setTable(String track, int position) throws Exception {
        
        try{
	        setTrack(position, track);
	        setTrackPosition(position, String.valueOf(position));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
    public void setTable(String track, int position, boolean sqlAppend) throws Exception{
        
        try{
	        this.sqlAppend = sqlAppend;
	        setTrack(position, track);
	        setTrackPosition(position, String.valueOf(position));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
    
    public void setParamList(String paramType, String paramValue, int preStatIdx) throws Exception{

        try{
	        paramType = paramType.toUpperCase();
	        setType(preStatIdx, paramType);
	        setValue(preStatIdx, paramValue);
	        setPreStatIdx(preStatIdx, String.valueOf(preStatIdx));
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
	 		
 	}
    
    public int getTotalSize(){
        return preStatIdxList.size()+inParamIdxList.size();
    }
    
    public int getStatSize(){
    	return preStatIdxList.size();
    }
    
    public int getInParamSize(){
    	return inParamIdxList.size();
    }
    
    public boolean getSqlAppend(){
        return sqlAppend;
    }
    
    public int getTractCount(){
        return trackList.size();
    }

    public void setDirectory(String directory){
        this.directory = directory;
    }
    
    public String getDirectory(){
        return directory;
    }
    
    public void setFile(String file){
        this.file = file;
    }
    
    public String getFile(){
        return file;
    }
    
    public void setInParam(int idx, String value){
        if(idx > inParamList.size()){
        	inParamList.add(value);
        }else{
        	inParamList.set(idx-1,value);
        }
    }
    
    public String getInParam(int index){
        return inParamList.get(index).toString();
    }
    
    public void setInParamIdx(int idx, String value){
        if(idx > inParamIdxList.size()){
        	inParamIdxList.add(value);
        }else{
        	inParamIdxList.set(idx-1,value);
        }
    }
    
    public String getInParamIdx(int index){
        return inParamIdxList.get(index).toString();
    }
    
    public void setType(int idx, String value){
        if(idx > typeList.size()){
            typeList.add(value);
        }else{
            typeList.set(idx-1,value);
        }
    }
    
    public String getType(int index){
        return typeList.get(index).toString();
    }
   
    public void setValue(int idx, String value){
        if(idx > valueList.size()){
            valueList.add(value);
        }else{
            valueList.set(idx-1,value);
        }
    }
    
    public String getValue(int index){
        return valueList.get(index).toString();
    }
    
    public void setPreStatIdx(int idx, String value){
        if(idx > preStatIdxList.size()){
            preStatIdxList.add(value);
        }else{
            preStatIdxList.set(idx-1,value);
        }
    }
    
    public int getPreStatIdx(int index){
        return Integer.parseInt(preStatIdxList.get(index).toString());
    }    

    public void setTrack(int idx, String value){
        if(idx > trackList.size()){
            trackList.add(value);
        }else{
            trackList.set(idx-1,value);
        }
    }
    
    public String getTrack(int index){
        return trackList.get(index).toString();
    }    
    
    public void setTrackPosition(int idx, String value){
        if(idx > trackPositionList.size()){
            trackPositionList.add(value);
        }else{
            trackPositionList.set(idx-1,value);
        }
    }
    
    public String getTrackPosition(int index){
        return trackPositionList.get(index).toString();
    }    

    public void setParamType(String paramType){ 
        this.paramType		= paramType; 
    }
    
    public String getParamType(){
        return paramType; 
    }
    
    public void setParamString (String paramString){ 
        this.paramString	= paramString; 
    }
    
    public String getParamString(){ 
        return paramString; 
    }
    
    public void setParamInt(String paramInt){ 
        this.paramInt		= Integer.parseInt(paramInt); 
    }    
    
    public int getParamInt(){ 
        return paramInt; 
    }
    
    public void setParamDouble(String paramDouble){ 
        this.paramDouble	= Double.parseDouble(paramDouble); 
    }
    
    public double getParamDouble(){ 
        return paramDouble; 
    }
    
    public void setParamTimestamp(String paramTimestamp){ 
        this.paramTimestamp = Timestamp.valueOf(paramTimestamp);
    }

    public Timestamp getParamTimestamp(){ 
        return paramTimestamp; 
    }
    
    public void setParamBigDecimal(String paramBigDecimal){ 
        this.paramBigDecimal= new BigDecimal(paramBigDecimal); 
    }
    
    public BigDecimal getParamBigDecimal(){ 
        return paramBigDecimal; 
    }
    
    public void setParamLocation(int paramLocation){ 
        this.paramLocation		= paramLocation; 
    }
    
    public int getParamLocation(){ 
        return paramLocation; 
    }
    
    public void setInsertFile(String directory, String file){ 
        this.insertFile		= directory+file; 
    }
    
    public String getInsertFile(){ 
        return insertFile; 
    }
    
    public void setUpdateFile(String directory, String file){ 
        this.updateFile		= directory+file; 
    }
    
    public String getUpdateFile(){ 
        return updateFile; 
    }
    
    public void setDeleteFile(String directory, String file){ 
        this.deleteFile		= directory+file; 
    }
    
    public String getDeleteFile(){ 
        return deleteFile; 
    }
    
    public void init(){
 
        typeList       		= null;
        valueList      		= null;
        preStatIdxList 		= null;
        trackList      		= null;
        trackPositionList 	= null;
        inParamList      	= null;
        inParamIdxList 		= null;   	
    	
        typeList       		= new ArrayList();
        valueList      		= new ArrayList();
        preStatIdxList 		= new ArrayList();
        trackList      		= new ArrayList();
        trackPositionList 	= new ArrayList();
        inParamList      	= new ArrayList();
        inParamIdxList 		= new ArrayList();
    }
}
