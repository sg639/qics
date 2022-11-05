package com.optidpp.podservice.db.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public abstract class AbstractDataTransObj implements Serializable {
    
    public String		directory;
    public String 		file;
    
    public String 		paramType;  
    public String 		paramString;  
    public int 			paramInt;  
    public double		paramDouble;  
    public Timestamp	paramTimestamp;  
    public BigDecimal	paramBigDecimal;  
    public int			paramLocation;  
    
    public abstract void setDirectory(String directory);
    
    public abstract String getDirectory();
    
    public abstract void setFile(String file);
    
    public abstract String getFile();
    
    public abstract void setParamType(String paramType);
    
    public abstract String getParamType();
    
    public abstract void setParamString (String paramString);
    
    public abstract String getParamString();
    
    public abstract void setParamInt(String paramInt);
    
    public abstract int getParamInt();
    
    public abstract void setParamDouble(String paramDouble);
    
    public abstract double getParamDouble();
    
    public abstract void setParamTimestamp(String paramTimestamp);

    public abstract Timestamp getParamTimestamp();
    
    public abstract void setParamBigDecimal(String paramBigDecimal);
    
    public abstract BigDecimal getParamBigDecimal();
    
    public abstract void setParamLocation(int paramLocation);
    
    public abstract int getParamLocation();
    
}
