package com.optidpp.podservice.db;

public class DBErrorMsg {

    public static String getErrMsg(String errorMsg)
    {
        String errmessage = "";
        errorMsg	= errorMsg.trim();
        if(errorMsg.indexOf("00001") != -1)
            errmessage = "데이타가 중복됩니다.";
        else if(errorMsg.indexOf("ORA-01009") != -1)
            errmessage = "필수 매개 변수가 없습니다.";
        else if(errorMsg.indexOf("00936") != -1)
            errmessage = "작성한 SQL문 문법 오류 입니다.";
        else if(errorMsg.indexOf("00936") != -1)
            errmessage = "작성한 SQL문 문법 오류 입니다.";
        else if(errorMsg.indexOf("00904") != -1)
            errmessage = "작성한 열명이 부적합 합니다.";
        else if(errorMsg.indexOf("00917") != -1)
            errmessage = "쉽표가 누락되었습니다.";
        else if(errorMsg.indexOf("00957") != -1)
            errmessage = "작성한 열명이 중복되었습니다.";
        else if(errorMsg.indexOf("00936") != -1)
            errmessage = "작성한 SQL문 문법 오류 입니다.";
        else if(errorMsg.indexOf("01401") != -1)
            errmessage = "작성한 값이 저장 Recode Size보다 큽니다.";
        else if(errorMsg.indexOf("01722") != -1)
            errmessage = "수치가 부적합 합니다.";
        else if(errorMsg.indexOf("01704") != -1)
            errmessage = "입력한 문자열이 너무 큽니다.";
        else
            errmessage = "저장확인 바랍니다.";

        return errmessage;
    }
}