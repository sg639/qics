/*
 * ȭ    ��    ��  	: CommonParamDAC.java
 * 
 * ȭ  ��  ��  ��	: 
 *
 * �� �� �� �� ��	: �� ����Ʈ����� �ŵ���ȭ���ػ����ֽ�ȸ�� �����Դϴ�.
 *            	          �ŵ���ȭ���ػ����ֽ�ȸ���� �㰡���� ���� �� ������ �� �����ϴ�.
 * 			  Copyright 2006 by Shindongah Fire & Marine insurance Co., Ltd. AllRights Reserved. 
 * 
 * ��Ű�� ���� 	: 1.0
 * 
 * ��   ��    ��  	: ������
 * 
 * ��  ��  �� �� 	: 2004. 10. 5.���� 10:50:4
 * 
 * @version		: $Revision: 1.1.1.1 $
 * 
 */

package com.optidpp.podservice.db.dac;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class CommonParamDAC implements Serializable{
    
    private String		argType;  
    private String		argString;  
    private int			argInt;  
    private double		argDouble;  
    private Timestamp	argTimestamp;  
    private BigDecimal	argBigDecimal;  
    private int			argLocation;  

 	public CommonParamDAC() {
 	}

 	public CommonParamDAC(String argType, String argValue, int location) throws Exception{

 		try{
 	    
	 	    this.setArgType(argType);
	 		
	 		if (argType.equals("STR")){
	 		    this.setArgString(argValue);
	 		}
	 		if (argType.equals("INT")){
	 		    this.setArgInt(argValue);
	 		}
	 		if (argType.equals("DBL")){
	 		    this.setArgDouble(argValue);
	 		}
	 		if (argType.equals("TSP")){
	 		    this.setArgTimestamp(argValue);
	 		}
	 		if (argType.equals("BDC")){
	 		    this.setArgBigDecimal(argValue);
	 		}
	 		
	 		this.setArgLocation(location);

 		}catch(Exception ex){
 		   throw new Exception(ex.getMessage());
 		}
 		
 	}
    
    public void setArgType(String argType){ 
        this.argType		= argType; 
    }
    
    public String getArgType(){ 
        return argType; 
    }
    
    public void setArgString (String argString){ 
        this.argString	= argString; 
    }
    
    public String getArgString(){ 
        return argString; 
    }
    
    public void setArgInt(String argInt){ 
        this.argInt		= Integer.parseInt(argInt); 
    }    
    
    public int getArgInt(){ 
        return argInt; 
    }
    
    public void setArgDouble(String argDouble){ 
        this.argDouble	= Double.parseDouble(argDouble); 
    }
    
    public double getArgDouble(){ 
        return argDouble; 
    }
    
    public void setArgTimestamp(String argTimestamp){ 
        this.argTimestamp = Timestamp.valueOf(argTimestamp);
    }

    public Timestamp getArgTimestamp(){ 
        return argTimestamp; 
    }
    
    public void setArgBigDecimal(String argBigDecimal){ 
        this.argBigDecimal= new BigDecimal(argBigDecimal); 
    }
    
    public BigDecimal getArgBigDecimal(){ 
        return argBigDecimal; 
    }
    
    public void setArgLocation(int argLocation){ 
        this.argLocation		= argLocation; 
    }
    
    public int getArgLocation(){ 
        return argLocation; 
    }
}
