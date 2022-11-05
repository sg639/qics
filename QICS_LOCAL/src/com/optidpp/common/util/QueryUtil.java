package com.optidpp.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import org.anyframe.query.QueryServiceException;
import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;
/**
 * Common Query Util
 * 
 * @author ParkMoohun
 */
public class QueryUtil{
	@Inject
	@Named("Dao")
    	private Dao dao;
	/**
	 * @param query
	 * @param paramMap
	 * @return String
	 * @throws QueryServiceException
	 */
	public synchronized static String queryForceHandle(String query, Map paramMap) throws QueryServiceException{
	    int c		= 0;
	    String value 	= "";
	    String key	= "";
	    	
	    Set<String> sessionSet = paramMap.keySet();
	    Iterator<String> iterator = sessionSet.iterator();
	    while (iterator.hasNext()) {
    		key = iterator.next();
    		value = String.valueOf( paramMap.get(key) );
    		c = findStr(query,key);
    		if(c > 0){
    		    query = changeStr(c,query,key,value);
    		}
	    }
	    return query;
	}
	
	public synchronized static int findStr(String tst, String fst){
	    int c = 0;
	    if( tst.indexOf( "{{"+fst+"}}" ) > 0)c=1;
	    if( tst.indexOf( ":"+fst+"" ) > 0)c+=10;
	    return c;
	}
	
	public synchronized static String changeStr(int pat,String tst, String fst, String cst){
	    String delimiter[] = new String[12];
	    delimiter[1] = "{{"+fst+"}}";
	    delimiter[10] = ":"+fst;
	    if(pat == 1){
		tst = tst.replaceAll(delimiter[pat], "'"+cst+"'");
	    }else if(pat == 10){
		tst = tst.replaceAll(delimiter[pat], "'"+cst+"'");
	    }
	    return tst;
	}
}
