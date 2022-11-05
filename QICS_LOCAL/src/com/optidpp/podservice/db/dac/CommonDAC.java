package com.optidpp.podservice.db.dac;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class CommonDAC extends DataAccessCmd implements Serializable{
    public CommonDAC() throws Exception{
        super();
    }
    
    public CommonDAC(String sql) throws SQLException{
        super(sql);
    }
  
    public void setString(String param, int loc)throws SQLException{
        ((PreparedStatement)getStatement()).setString(loc, param);
    }

     public void setInteger(int param, int loc)throws SQLException{
        ((PreparedStatement)getStatement()).setInt(loc, param);
    }

     public void setDouble(double param, int loc)throws SQLException{
        ((PreparedStatement)getStatement()).setDouble(loc, param);
    }

     public void setTimestamp(Timestamp param, int loc)throws SQLException{
        ((PreparedStatement)getStatement()).setTimestamp(loc, param);
    }

     public void setBigDecimal(BigDecimal param, int loc)throws SQLException{
        ((PreparedStatement)getStatement()).setBigDecimal(loc, param);
    }    
}
