<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<%
String SEQ_Q100 = request.getParameter("SEQ_Q100") == null ? "" : request.getParameter("SEQ_Q100");
String SEQ_T300 = request.getParameter("SEQ_T300") == null ? "" : request.getParameter("SEQ_T300");
String WEB_DATA_YN = request.getParameter("WEB_DATA_YN") == null ? "" : request.getParameter("WEB_DATA_YN");
String IN_LINE = request.getParameter("IN_LINE") == null ? "" : request.getParameter("IN_LINE");
String FORM_TYPE = request.getParameter("FORM_TYPE") == null ? "" : request.getParameter("FORM_TYPE");
String E_STATUS = request.getParameter("E_STATUS") == null ? "" : request.getParameter("E_STATUS");
String FORM_CODE = request.getParameter("FORM_CODE") == null ? "" : request.getParameter("FORM_CODE");
String STATUS_CODE = request.getParameter("STATUS_CODE") == null ? "" : request.getParameter("STATUS_CODE");
String MRG_WIP_ENTITY_NAME = request.getParameter("MRG_WIP_ENTITY_NAME") == null ? "" : request.getParameter("MRG_WIP_ENTITY_NAME");

%>

<!DOCTYPE html>
<html id="wrapSub">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
  <title>HYUNDAI BNG STEEL :: 보정화면</title>
 
 <%@ include file="/WEB-INF/jsp/common/include/jqueryScript.jsp"%>
</head>

<script type="text/javascript">
var groupMemberMap= new HashMap();
var userValueMap= new HashMap();
var memberValueMap= new HashMap();
var psImageName="";
var psImagePath="";
var isSave=false;
var CskinMap= new HashMap();
var CppMap= new HashMap();
var ClvMap= new HashMap();
var CalvMap= new HashMap();
var CasMap= new HashMap();
var CshMap= new HashMap();
var CstMap= new HashMap();
var CrhMap= new HashMap();
var CrtMap= new HashMap();
var CdefCdMap= new HashMap();
var CchkNoMap= new HashMap();
var orgInfoMap= new HashMap();
var ufsResourceMap= new HashMap();
var ngCodeMap= new HashMap();
var buttonMap= new HashMap();
var CcoatMap= new HashMap();
var CchkUserMap = new HashMap();
var CstpMap = new HashMap();
var cMCount=0;
var cMspCount = 0;
var cRollStopCount=0;
var cCoilStCount=0;
var cCoilEdCount=0;
var cNCount=0;
var cHCount=0;
var cOp1Count=0;
var cOp2Count=0;

$(document).ready(function() {

	$('#my-button1').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		parent.userViewInfo(e);
	});	
	$('#my-button2').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		if($("#E_STATUS").val() =="C"){
			parent.userReportBgInfo(e);
		 }else{
			 alert("작성중입니다.");
		 }
	});	
	$('#my-button3').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		parent.userDivide(e);
	});	
	$('#my-button7').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		$("#C_POC_NO01_POD").val($("#C_POC_NO01_POD").val().toUpperCase());
		$("#C_POC_NO02_POD").val($("#C_POC_NO02_POD").val().toUpperCase());
		$("#C_POC_NO03_POD").val($("#C_POC_NO03_POD").val().toUpperCase());
		
				var poc ="";
				var poc1 = $("#C_POC_NO01_POD").val();
				var poc2 = $("#C_POC_NO02_POD").val();
				var poc3 = $("#C_POC_NO03_POD").val();
			 	poc = poc1+"-"+poc2+"-"+poc3;
			    var tempPoc = poc.split("-");
			    var poc_no="";
			    for(var i = 0; i<tempPoc.length; i++){
					if(tempPoc[i] == ""){
						break;
					}else{
						if(i==0){
							poc_no = tempPoc[i];
						}else{
							poc_no = poc_no+"-"+ tempPoc[i];
						}
						
					}
				}
			  
			parent.document.mySheetForm.REPORT_POC.value=poc_no;
			parent.document.mySheetForm.REPORT_POC01.value=poc1;
			parent.document.mySheetForm.REPORT_POC02.value=poc2;
			parent.document.mySheetForm.REPORT_POC03.value=poc3;
			parent.document.mySheetForm.REPORT_LINE.value=document.frameForm.IN_LINE.value
			parent.reportBgInfo(e);
		
	
	});	
	$('#my-button4').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		dataSave();
	});	
	$('#my-button5').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		alert("샘플 채취하셨습니까?");
		var isSend= $("#my-button5").hasClass("disabled");
		 
		if(!isSend){
			sendErp();
		}
		
		
	});	

	if($("#WEB_DATA_YN").val() =="Y"){
		$('#my-button1').hide();
	}else{
		$('#my-button1').show();
	}
	if($("#FORM_TYPE").val() =="M"){
		 var reportNm="";
		 if($("#E_STATUS").val() =="R" || $("#E_STATUS").val() =="S" || $("#E_STATUS").val() =="C"){
			
			 if($("#E_STATUS").val() =="R"){
				 reportNm ='품질이상보고서 보기';
			 }else if ($("#E_STATUS").val() =="S"){
				 reportNm ='품질이상보고서 보기';
			 }else if($("#E_STATUS").val() =="C"){
				 reportNm ='품질이상보고서 보기';
			 }else{
			 }
			 $("#reportNm").html(reportNm) ;
			 $("#my-button2").show();
			 $("#my-button7").hide();
		}else{
			$("#my-button2").hide();
			$("#my-button7").show();
		}
		$('#my-button3').hide();
	}else{
		$("#my-button2").hide();
		$("#my-button7").hide();
		$('#my-button3').show();
	}
	if($("#STATUS_CODE").val() =="C"){
		
		$("#my-button4").hide();
		$("#my-button5").hide();
		$('#my-button6').hide();
		$("#my-button7").hide();
		if($("#FORM_TYPE").val() =="M"){
			 var reportNm="";
			 if($("#E_STATUS").val() =="R" || $("#E_STATUS").val() =="S" || $("#E_STATUS").val() =="C"){
				
				 if($("#E_STATUS").val() =="R"){
					 reportNm ='품질이상보고서 보기';
				 }else if ($("#E_STATUS").val() =="S"){
					 reportNm ='품질이상보고서 보기';
				 }else if($("#E_STATUS").val() =="C"){
					 reportNm ='품질이상보고서 보기';
				 }else{
				 }
				 $("#reportNm").html(reportNm) ;
				 $("#my-button2").show();
				 $("#my-button7").hide();
			}else{
				$("#my-button2").hide();
				$("#my-button7").show();
			}
			$('#my-button3').hide();
		}
	}

	var result =ajaxCall("/Code.do?cmd=codeList", $("#frameForm").serialize(), false);
	setCodeMap(result.DATA);


	
});
function setCodeMap(codeDataInfo){
	for(var i = 0; i<codeDataInfo.length; i++){ 
	                                                                                       
		if(codeDataInfo[i].CODE_GUBUN =="UFS_GRADE"){
			
			if($("#FORM_TYPE").val() == "M" && codeDataInfo[i].ATTRIBUTE1=='중간검사'){
				ClvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].CODE);
			}else if ($("#FORM_TYPE").val() == "F" && codeDataInfo[i].ATTRIBUTE1=='최종검사'){
				ClvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].CODE);
			}
			
			//가능등급은 D1, E1만 입력
			 CalvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].CODE);
		}
		if(codeDataInfo[i].CODE_GUBUN =="INSPECTOR"){
			CchkNoMap.put(codeDataInfo[i].CODE,codeDataInfo[i].CODE);
			CchkUserMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
		}
		if(codeDataInfo[i].CODE_GUBUN =="SURFACE"){
			CskinMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
		}
		if(codeDataInfo[i].CODE_GUBUN =="ORG_INFO"){
			 orgInfoMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
		}
		if(codeDataInfo[i].CODE_GUBUN =="UFS_RESOURCE"){
			 ufsResourceMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE2);
		}
		
		if(codeDataInfo[i].CODE_GUBUN =="STEEL"){
			CstpMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
		}
		if(codeDataInfo[i].CODE_GUBUN =="COATING"){
			CcoatMap.put(codeDataInfo[i].CODE,codeDataInfo[i].CODE);
		}
		
	}
	if($("#FORM_TYPE").val() == "M"){
		CppMap.put("D1","D1");
		CppMap.put("E1","E1");
	}

	//가능등급은 D1, E1만 입력
/*	CalvMap.put("ZZ","ZZ");
	CalvMap.put("QQ","QQ");
	CalvMap.put("RR","RR");
 
	ClvMap.put("ZZ","ZZ");
	ClvMap.put("QQ","QQ");
	ClvMap.put("RR","RR");*/
 
	 
	var inFact = ufsResourceMap.get($("#IN_LINE").val()); //BGC라는 정보를 가져온다.
 
	var orgInfo = orgInfoMap.keys();
 
	var ngValue="";
	for(var i=0; i<orgInfo.length; i++){
		if(inFact == orgInfoMap.get(orgInfo[i])){
			ngValue = orgInfo[i];
		}
	}
	$("#ORGANIZATION_ID").val(ngValue);
	for(var i = 0; i<codeDataInfo.length; i++){ 
		if(codeDataInfo[i].CODE_GUBUN =="NGCODE"){
			if(ngValue == codeDataInfo[i].ATTRIBUTE2)
			 ngCodeMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE2);
		}
	}
	var ngCode =ngCodeMap.keys();
	for(var i=0; i<ngCode.length; i++){
		if(ngValue == ngCodeMap.get(ngCode[i])){
			CshMap.put(ngCode[i],ngCode[i]);
			CrhMap.put(ngCode[i],ngCode[i]);
			CdefCdMap.put(ngCode[i],ngCode[i]);
		}
		
	}
	var result1 = ajaxCall("/View.do?cmd=viewBGInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색

	setBG(result1.DATA);
	parent.hideGuidTxt();
}
function setBG(viewDataInfo){

	for(var i = 0; i<viewDataInfo.length; i++){ 
		var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_PATH="+viewDataInfo[i].BG_PATH+"&BG_NAME="+viewDataInfo[i].BG_NAME;
		 psImageName=viewDataInfo[i].P_BG;
		 psImagePath=viewDataInfo[i].P_BG_PATH.split("\\").join("\\\\");
		  
		$("#bgImage").attr('src',url);
	}
	var result = ajaxCall("/View.do?cmd=viewValueInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색
	setUserValue(result.DATA);
	parent.hideGuidTxt();
}
function setUserValue(viewDataInfo){
	for(var i = 0; i<viewDataInfo.length; i++){
		if(viewDataInfo[i].FIELD_NAME == "C_MAIN_PRC"){
			document.frameForm.IN_LINE.value =viewDataInfo[i].USER_INPUT_VALUE;
		}
		userValueMap.put(viewDataInfo[i].FIELD_NAME,viewDataInfo[i].USER_INPUT_VALUE);
	}
	var result = ajaxCall("/View.do?cmd=viewPadInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색
	getGroupMember(result.DATA);
}
function getGroupMember(viewDataInfo){
	for(var i = 0; i<viewDataInfo.length; i++){ 
		if(viewDataInfo[i].FIELD_FIELDVALUE !=null && viewDataInfo[i].FIELD_FIELDVALUE !=""){
			memberValueMap.put(viewDataInfo[i].FIELD_NAME,viewDataInfo[i].FIELD_FIELDVALUE);
		}
		if(viewDataInfo[i].FIELD_GROUPTYPE =="radio" ){
			var str ='';
			if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
				str ='<input type="hidden" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValueMap.get(viewDataInfo[i].FIELD_NAME)+'" />';
			}else{
				str ='<input type="hidden" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" />';
			}
			$("#frameForm").append(str)
			 var groupMember = viewDataInfo[i].FIELD_GROUPMEMBER.split(";");
			 for(var j =0; j < groupMember.length; j++){
				 groupMemberMap.put(groupMember[j],viewDataInfo[i].FIELD_GROUPMEMBER);
			 }
		 }
	}
	createInfoDiv(viewDataInfo);
}
function createInfoDiv(viewDataInfo){
	var dataDiv ="";//작성된 필드 정보 입력
	var tempOrder=0;
	var fullDiv="";
	var subDiv=""
	for(var i = 0; i<viewDataInfo.length; i++){ 
		 if(i == 0){
			 tempOrder=viewDataInfo[i].PAGE_ORDER;
			 $("#FORM_SEQ").val(viewDataInfo[i].FORM_SEQ);
			 $("#PAGE_ORDER").val(viewDataInfo[i].PAGE_ORDER);
	        fullDiv='<div id="result_'+(tempOrder)+'" style="display: block;">'
		 }
		 if(tempOrder ==viewDataInfo[i].PAGE_ORDER ){
			 var userValue ="";
			 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
				 userValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
			 }			 
			 if(viewDataInfo[i].FIELD_DATATYPE == "image"){
				 subDiv="";
				 if( viewDataInfo[i].FIELD_NAME == "C_SPEC"){
					 
					if(psImageName != "" && psImagePath  != ""){
						subDiv = "<div id='pg_"+viewDataInfo[i].FIELD_NAME+"' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:"+viewDataInfo[i].FIELD_WIDTH+"px;height:"+(parseInt(viewDataInfo[i].FIELD_HEIGHT))+"px; left:"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px; top:"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;  background-image:url(/ImagePS.do?BG_PATH="+psImagePath+"&P_BG="+psImageName+") ; background-repeat: no-repeat;background-position:-"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px -"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;' >"	;
						$("#subImage").append(subDiv);
					}
					
				 }
				 if( viewDataInfo[i].FIELD_NAME == "C_IMG_RM"){
					 
						if(psImageName != "" && psImagePath  != ""){
							subDiv = "<div id='pg_"+viewDataInfo[i].FIELD_NAME+"' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:"+viewDataInfo[i].FIELD_WIDTH+"px;height:"+(parseInt(viewDataInfo[i].FIELD_HEIGHT))+"px; left:"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px; top:"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;  background-image:url(/ImagePS.do?BG_PATH="+psImagePath+"&P_BG="+psImageName+") ; background-repeat: no-repeat;background-position:-"+(parseInt(viewDataInfo[i].FIELD_LEFT))+"px -"+(parseInt(viewDataInfo[i].FIELD_TOP))+"px;' >"	;
							$("#subImage").append(subDiv);
						}
						
					 }
			 }
			 if(viewDataInfo[i].FIELD_DATATYPE == "text" || (viewDataInfo[i].FIELD_DATATYPE == "group" && viewDataInfo[i].FIELD_GROUPTYPE == "text")){
				 if(viewDataInfo[i].FIELD_DYNAMIC=="true"){
					 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT))+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
				    
					 //C_POC_NO1 의 값이 존재 한다면 C_POC_NO1로 없으면 C_POC_NO1_POD로
					 if(viewDataInfo[i].FIELD_NAME == "C_POC_NO01_POD" || viewDataInfo[i].FIELD_NAME == "C_POC_NO02_POD" || viewDataInfo[i].FIELD_NAME == "C_POC_NO03_POD"
				    	 || viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" || viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD" || viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD"
				    		 || viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
						 
						 if(viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" || viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD"){
							 var maxValue1="";
							 var maxValue2="";
							 var lastValue1="";
							 var lastValue2="";
							 var podValue =  viewDataInfo[i].FIELD_POD;
							 if(podValue != "" && podValue != null){
								 
							 }
							 
							 if(userValueMap.containsKey("C_THMAX")){
								 userValue = userValueMap.get("C_THMAX");
								 var tempValue = userValue.split(".");
								 if(tempValue.length >0){
									 lastValue1 = tempValue[0];
									 lastValue2 = tempValue[1];
								 }
								 
							 }		
							 if(viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" ){
								 if(userValueMap.containsKey("C_THMAX01_POD")){
									 maxValue1 = userValueMap.get("C_THMAX01_POD")
								 }else{
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 maxValue1 = viewDataInfo[i].FIELD_POD;
									 }else{
										 maxValue1 = lastValue1;
									 }
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+maxValue1+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }else if(viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD"){
								 if(userValueMap.containsKey("C_THMAX02_POD")){
									 maxValue2 = userValueMap.get("C_THMAX02_POD")
								 }else{
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 maxValue2 = viewDataInfo[i].FIELD_POD;
									 }else{
										 maxValue2 = lastValue2;
									 }
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+maxValue2+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }
							 
						 }else if(viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD"	 || viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
							 var minValue1="";
							 var minValue2="";
							 var lastValue1="";
							 var lastValue2="";
							 if(userValueMap.containsKey("C_THMIN")){
								 userValue = userValueMap.get("C_THMIN");
								 var tempValue = userValue.split(".");
								 if(tempValue.length >0){
									 lastValue1 = tempValue[0];
									 lastValue2 = tempValue[1];
								 }
							 }							 
							 if(viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD" ){
								 if(userValueMap.containsKey("C_THMIN01_POD")){
									 minValue1 = userValueMap.get("C_THMIN01_POD")
								 }else{
									 
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 minValue1 = viewDataInfo[i].FIELD_POD;
									 }else{
										 minValue1 = lastValue1;
									 }
									 
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+minValue1+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }else if(viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
								 if(userValueMap.containsKey("C_THMIN02_POD")){
									 minValue2 = userValueMap.get("C_THMIN02_POD")
								 }else{
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 minValue2 = viewDataInfo[i].FIELD_POD;
									 }else{
										 minValue2 = lastValue2;
									 }
									 
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+minValue2+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }
						 }else{
							 var tempValue = "";
							 
							 if(viewDataInfo[i].FIELD_NAME == "C_POC_NO01_POD"){
								 
								 if(userValueMap.containsKey("C_POC_NO01_POD")){
									 tempValue = userValueMap.get("C_POC_NO01_POD")
								 }else{
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 tempValue = viewDataInfo[i].FIELD_POD;
									 }else{
										 if(userValueMap.containsKey("C_POC_NO01")){
											 tempValue = userValueMap.get("C_POC_NO01")
										 }
									 }
								 }
							 }
 							if(viewDataInfo[i].FIELD_NAME == "C_POC_NO02_POD"){
								 
								 if(userValueMap.containsKey("C_POC_NO02_POD")){
									 tempValue = userValueMap.get("C_POC_NO02_POD")
								 }else{
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 tempValue = viewDataInfo[i].FIELD_POD;
									 }else{
										 if(userValueMap.containsKey("C_POC_NO02")){
											 tempValue = userValueMap.get("C_POC_NO02")
										 }
									 }
								 }
							 }
 							if(viewDataInfo[i].FIELD_NAME == "C_POC_NO03_POD"){
								 
								 if(userValueMap.containsKey("C_POC_NO03_POD")){
									 tempValue = userValueMap.get("C_POC_NO03_POD")
								 }else{
									 if(viewDataInfo[i].FIELD_POD != "" && viewDataInfo[i].FIELD_POD != null  ){
										 tempValue = viewDataInfo[i].FIELD_POD;
									 }else{
										 if(userValueMap.containsKey("C_POC_NO03")){
											 tempValue = userValueMap.get("C_POC_NO03")
										 }
									 }
								 }
							 }
							
							 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"   value="'+tempValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
						 }
				    	 
				     }else{
				    	 if(viewDataInfo[i].FIELD_NAME == "C_FRM_DPT" || viewDataInfo[i].FIELD_NAME == "C_FRM_PRN" || viewDataInfo[i].FIELD_NAME == "C_FRM_ID" || viewDataInfo[i].FIELD_NAME == "C_FRM_NO"){
				    		 
				    	 }else if(viewDataInfo[i].FIELD_NAME == "C_STAMP01_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP02_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP03_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP04_POD"){
				    		if(viewDataInfo[i].FIELD_POD == "" || viewDataInfo[i].FIELD_POD == null){
				    			
				    		}else{
				    			if($("#WEB_DATA_YN").val() == 'Y'){
				    				dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn-inform" style="position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD.split("\n").join("<br>")+'</span>';
				    			}else{
				    				
				    			}
				    			
				    		}
				    		 
				    	 }else{
				    		 if(viewDataInfo[i].FIELD_NAME =="C_CHK_DT"){
				    			 document.frameForm.WORK_DATE.value=viewDataInfo[i].FIELD_POD;
				    		 }
				    		 if(viewDataInfo[i].FIELD_NAME =="C_STD01"){
				    			// document.frameForm.TARGET_THICKNESS.value=viewDataInfo[i].FIELD_POD;
				    		 }
				    		 if(viewDataInfo[i].FIELD_NAME =="C_STD02"){
				    			// document.frameForm.LAST_WIDTH.value=viewDataInfo[i].FIELD_POD;
				    		 }
				    		 if(viewDataInfo[i].FIELD_NAME.indexOf("C_APP") > -1 || viewDataInfo[i].FIELD_NAME =="C_SEQ" ){
				    			 dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"   style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD+'</span>';
				    		 }else if(viewDataInfo[i].FIELD_NAME =="C_CHK_DT"){
				    			dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;font-size:13pt;" />';
				    		 }else if(viewDataInfo[i].FIELD_NAME =="C_STD01" || viewDataInfo[i].FIELD_NAME =="C_STD02" || viewDataInfo[i].FIELD_NAME =="C_STP"){
				    			dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onchange="javascript:goChangeKEYField(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
				    		 }else{
					    		dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"   style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;top: 25%;">'+viewDataInfo[i].FIELD_POD+'</span>';
				    		 }
				    		
				    	 }
				     }
					 //dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" disabled="disabled" value="'+viewDataInfo[i].FIELD_POD+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
				     
					 dataDiv +='</div>';
				 }else{
					 //var tempName = viewDataInfo[i].FIELD_NAME;
					 var tempValue ="";
					 var tempStyle="";
					 var tempScript=""
						 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT))+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
					 if( viewDataInfo[i].FIELD_NAME.indexOf("C_MST") > -1){
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onchange="javascript:goChageData(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_OP1") > -1){
						 cOp1Count++;
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onchange="javascript:goChageData(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_OP2") > -1){
						 cOp2Count++;
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onchange="javascript:goChageData(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_PP"){
						 
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CppMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									if($("#FORM_TYPE").val() == "M"){
										tempStyle="text-danger";
									}
								}
							}
						 }
						 
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_LV"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(ClvMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_COAT"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CcoatMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_ALV"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CalvMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_SKIN"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							  
							 if(CskinMap.containsKey(tempValue)){
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if(viewDataInfo[i].FIELD_NAME == "C_CHK_NO"){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 
							 if(CchkNoMap.containsKey(tempValue)){
								 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_SH") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							// alert(tempValue +" : " +CshMap.containsKey(tempValue))
							 if(CshMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_RH") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CrhMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_DEF_CD") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(CdefCdMap.containsKey(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_ST") > -1){
						 if(viewDataInfo[i].FIELD_NAME.indexOf("C_STP") > -1){
							 
						 }else{
							 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
								 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
								 if(checkValue(tempValue)){
									 	 
								}else{
									if(tempValue != "" && tempValue !=null){
										tempStyle="text-danger";
									}
								}
							 }
						 }
						
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_RT") > -1){
						 if(userValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 tempValue = userValueMap.get(viewDataInfo[i].FIELD_NAME);
							 if(checkValue(tempValue)){
								 	 
							}else{
								if(tempValue != "" && tempValue !=null){
									tempStyle="text-danger";
								}
								  
							}
						 }
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px '+tempStyle+'" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px; " onchange="javascript:goErrorCheck(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_MSP") > -1){
						 cMspCount ++;
						 tempScript ='onchange="javascript:checkPreCmspValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" '+tempScript+'/>';
					 }else if( viewDataInfo[i].FIELD_NAME.indexOf("C_MSW") > -1){
						// cMspCount ++;
						 tempScript ='onchange="javascript:changeCmsw();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" '+tempScript+'/>';
					 }else if(viewDataInfo[i].FIELD_NAME.indexOf("C_ROLL_STOP") > -1){
						 cRollStopCount++;
						 tempScript ='onchange="javascript:checkPreCrollStopValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					 }else if(viewDataInfo[i].FIELD_NAME.substring(0,3) == "C_M"){
						 cMCount++;
						 tempScript ='onchange="javascript:checkPreCmValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					}else if(viewDataInfo[i].FIELD_NAME.substring(0,3) == "C_N"){
						 cNCount++;
						 tempScript ='onchange="javascript:checkPreCnValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					}else if(viewDataInfo[i].FIELD_NAME.substring(0,3) == "C_H"){
						 cHCount++;
						 tempScript ='onchange="javascript:checkPreChValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					}else if(viewDataInfo[i].FIELD_NAME.indexOf("C_COIL_ST") > -1){
						 cCoilStCount++;
						 tempScript ='onchange="javascript:checkPreCoilingValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					}else if(viewDataInfo[i].FIELD_NAME.indexOf("C_COIL_ED") > -1){
						 cCoilEdCount++;
						 tempScript ='onchange="javascript:checkPreCoilingValue();"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					}else if(viewDataInfo[i].FIELD_NAME.indexOf("C_DEF_ED") > -1 || viewDataInfo[i].FIELD_NAME.indexOf("C_DEF_ST") > -1 ){
						 tempScript ='onchange="javascript:checkDefStEdValue(\''+ viewDataInfo[i].FIELD_NAME.replace("C_DEF_ST","").replace("C_DEF_ED","") +'\');"';
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"  '+tempScript+'/>';
					}else{
						 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px" value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					 }
					 
					 dataDiv +='</div>';
				 }
			 }else{
				 if(groupMemberMap.containsKey(viewDataInfo[i].FIELD_NAME)){
					 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT) )+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
					 var tempStr = viewDataInfo[i].FIELD_NAME.substring(0,viewDataInfo[i].FIELD_NAME.lastIndexOf("_"));
					 buttonMap.put(tempStr,tempStr);
					 var tempVal = '';
					 var memVal = '';
					 
					 if(userValueMap.containsKey(tempStr)){
						 tempVal = userValueMap.get(tempStr);
					 }
					 if(memberValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
						 memVal = memberValueMap.get(viewDataInfo[i].FIELD_NAME);
					 }
					// alert(viewDataInfo[i].FIELD_NAME+" : "+tempVal +memVal);
					if(tempVal == memVal && memVal !=''){
						dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs active"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
					}else{
						dataDiv +='<button disabled type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
					}
					 dataDiv +='</div>'
				 }
			 }
			
		 }
			
		 if(i == viewDataInfo.length-1){
	     	fullDiv=fullDiv+dataDiv+"</div>"
	     }
	}
	$("#viewer").append(fullDiv);
	
	parent.hideGuidTxt();
	$("#frameForm input[type=text]").css({"ime-mode":"disabled","text-transform":"uppercase","background-color":"#FFFFFF"});
	$("#frameForm input[type=text]").attr("readonly",true);
	$("#frameForm button[type=button]").css({"ime-mode":"disabled"});
	
	var buttonKeys = buttonMap.keys();
	var key="";
	for(var i=0; i<buttonKeys.length; i++){
		key = buttonMap.get(buttonKeys[i]);
	//	var tempStr = '<input type="hidden" id="'+key+'" name="'+key+'"  />';      
		//alert(tempStr);
		$("<input></input>",{id:key,name:key,type:"hidden"}).appendTo(document.frameForm);
	}
	parent.goOtherLine();
	checkPreCmValue();
	checkPreCrollStopValue();
	checkPreCmspValue();
	changeCmsw();
	changeCmst();
	if($("#FORM_CODE").val() =="FORM_QICS_C"){
		changeCop1();
	}
	if($("#FORM_CODE").val() =="FORM_QICS_D"){
		changeCop1();
		changeCop2();
	}
	
	checkDefStEdValue("init");
	checkPreCoilingValue();
	
	var chNo= $("#C_CHK_NO").val();
	var userNm = CchkUserMap.get(chNo);
	 $("#C_CHKER").val(userNm);
	 
	 var chChkDt= $(" C_CHK_DT").val();
		$('#C_CHK_DT').datepicker({
			 showOn: "focus",
			 changeMonth: true,
			 dateFormat:'yy/mm/dd'
			 
		});	
		
	 //key event 처리..text-transform: uppercase;
	 //$("#frameForm input[type=text]:not(C_CHKER)").css({"ime-mode":"disabled","text-transform":"uppercase"});
	 //$("#C_CHKER").css("ime-mode","active");
	 $("#frameForm input[type=text]").css({"ime-mode":"disabled","text-transform":"uppercase","background-color":"#FFFFFF"});
	 $("#frameForm input[type=text]").attr("readonly",true);
	 
	 /*
	 $("#frameForm input[type=text]").keydown(function(event){
		 	event = event || window.event;
			var keyID = (event.which) ? event.which : event.keyCode;
//alert(this.id + "::"+ keyID);
			// 48~57:숫자, 96~105:숫자패드, 8:backspace, 9:tab, 46:Del, 37:<-, 39:->, 190:. 110: 숫자키패드. shift:16, 186: :; 65~90:A~Z 97~122:a~z
			// 일자
			// 숫자+영문 C_POC_NO01_POD C_POC_NO02_POD C_POC_NO03_POD C_PP C_LV C_ALV C_SKIN C_STP_TOP 
			// 이름 C_CHKER
			//alert((/C_DEF_[ST|ED|PT]/).test(this.id) );
			if(this.id=="C_CHKER" ) { // 작성자..
				return;
			} else if((/C_CHK_[ST|ED]/).test(this.id) ) { // 시간
				if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105)
						|| keyID == 9 || keyID == 8 || keyID == 16 || keyID == 46 || keyID == 37 || keyID == 39 || keyID == 186 ) { 
					return;
				 } else {
					return false;
				 }
			} else if((/C_ST[0-1][0-9]/).test(this.id) || (/C_RT[0-1][0-9]/).test(this.id) ) { // 등급 1자리. B C D K
				if ( (keyID >= 66 && keyID <= 68) || (keyID >= 98 && keyID <= 100) || keyID == 75 || keyID == 107
						|| keyID == 9 || keyID == 8 || keyID == 16 || keyID == 46 || keyID == 37 || keyID == 39  ) { 
					return;
				 } else {
					return false;
				 }
			} else if((/C_STD01/).test(this.id) || (/C_MST/).test(this.id) || (/C_TH_[TOP|END]/).test(this.id) || (/C_SPEED/).test(this.id)
					|| (/C_OP/).test(this.id) || (/C_WMIN/).test(this.id)  || (/C_WMAX/).test(this.id) ) { // 두께, 숫자와 쩜.
				if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105)
						|| keyID == 9 || keyID == 8 || keyID == 16 || keyID == 46 || keyID == 37 || keyID == 39 || keyID == 190 || keyID == 110 ) { 
					return;
				 } else {
					return false;
				 }
			} else if( (/C_MEAMC/).test(this.id) || (/C_STD02/).test(this.id) || (/C_THMIN/).test(this.id)  || (/C_THMAX/).test(this.id) 
					|| (/C_MSP/).test(this.id) || (/C_MSW/).test(this.id) 
					|| (/C_M[0-1][0-9]/).test(this.id) || (/C_ROLL_STOP/).test(this.id) 
					|| (/C_COIL_/).test(this.id) || (/C_N[0-1][0-9]/).test(this.id) 
					|| (/C_H[0-1][0-9]/).test(this.id) || (/C_DEF_[ST|ED|PT]/).test(this.id) 
					|| (/C_LEN/).test(this.id) || (/C_KG/).test(this.id) 
					|| (/C_CHK_NO/).test(this.id) || (/C_SEQNO/).test(this.id) 					
					|| (/C_1SES_DR/).test(this.id)|| (/C_TP_[TOP|END]/).test(this.id)					
				) { // 숫자만.
				if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105)
						|| keyID == 9 || keyID == 8 || keyID == 16 || keyID == 46 || keyID == 37 || keyID == 39  ) { 
					return;
				 } else {
					return false;
				 }

			} else { //영문숫자
				if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || (keyID >= 65 && keyID <= 90) || (keyID >= 97 && keyID <= 122) 
						|| keyID == 9 || keyID == 8 || keyID == 16 || keyID == 46 || keyID == 37 || keyID == 39) { 
					return;
				 } else {
					return false;
				 }				
			}
		 	
	});*/
	 
	 /* 키업은...포커스가 맨뒤로 막..가는..문제가 있음.
	 $("#frameForm input[type=text]").blur(function(event){
		 	event = event || window.event;
			var keyID = (event.which) ? event.which : event.keyCode;
			this.value = this.value.toUpperCase(); 	
	});
	 */
	 parent.hideGuidTxt();

}
function changeCss(id){
	$('#'+id).val( $.trim($('#'+id).val().toUpperCase())  );
	
	
	 var isClick = $('#'+id).hasClass("btn btn-option btn-xxs active");
	 //alert(isClick);
	// $( '#'+id ).toggleClass( "active" );
	
	var tempStr = id.substring(0,id.lastIndexOf("_"));

	var strTemp = groupMemberMap.get(id).split(";");
	
	 for(var i =0; i < strTemp.length; i++){
	 
		 if(strTemp[i] == id){
			 if(isClick){
				 $('#'+strTemp[i]).removeClass('btn btn-option btn-xxs active').addClass('btn btn-option btn-xxs');
			 }else{
				if(memberValueMap.containsKey(id)){
					var memVal = memberValueMap.get(id);
					
					$("#"+tempStr).val(memVal);
				}
				 $('#'+strTemp[i]).removeClass('btn btn-option btn-xxs').addClass('btn btn-option btn-xxs  active');
			 }
			
		 }else{
			 $('#'+strTemp[i]).removeClass('btn btn-option btn-xxs active').addClass('btn btn-option btn-xxs');
		 }
	 }
	 var isCheck=false;
	 for(var i =0; i < strTemp.length; i++){
		 isCheck = $('#'+strTemp[i]).hasClass("btn btn-option btn-xxs active");
		 if(isCheck){
			 break;
		 }
	 }
	 if(!isCheck){
		 $("#"+tempStr).val('');
	 }

	// if(isClick){
	//	 $('#'+id).removeClass('btn btn-option btn-xxs active').addClass('btn btn-option btn-xxs');
//	 }else{
//		 $('#'+id).removeClass('btn btn-option btn-xxs').addClass('btn btn-option btn-xxs  active');
//	 }
	
}
function backGroudImage(w,h,l,t,nm){
	
	if($("#WEB_DATA_YN").val() == 'Y'){
		
	}else{
		var str ="<div id='subBg' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:177px;height:173px; left:455px; top:495px;  background-image:url(/ImagePS.do?BG_PATH="+a+"&P_BG="+b+") ; background-repeat: no-repeat;background-position:-455px -495px;' >"	;
		$("#subImage").html(str);
	}
	
}
function dataSave(){
	
//	checkDATA();
	
	return;
	
	checkDATA();
	var result = ajaxCall("/Save.do?cmd=dataSave", $("#frameForm").serialize(), false);  //작업내역 상세검색
	alert(result.Message);
	isSave=true;
	if(isSave){
		$("#my-button5").removeClass("disabled");
	}
}

function goChangeKEYField(id){
	$('#'+id).val( $.trim($('#'+id).val().toUpperCase())  );
	
	if(id =="C_STD01" ) { //|| viewDataInfo[i].FIELD_NAME =="C_STD02" || viewDataInfo[i].FIELD_NAME =="C_STP"){
	//	$("#TARGET_THICKNESS").val( $("#C_STD01").val() );
		changeCmst();
		changeCop1();
		changeCop2();
		if($("#C_STD01").val()=="") {
			alert("규격 두께는 반드시 입력하셔야 합니다.");
			if(!$("#C_STD01").hasClass("text-danger")) {
				$("#C_STD01").addClass("text-danger");
			}
			$("#C_STD01").focus();
		} else {
			if($("#C_STD01").hasClass("text-danger")) {
				$("#C_STD01").removeClass("text-danger");
			}
		}
	}
	
	if(id =="C_STD02" ) { //|| viewDataInfo[i].FIELD_NAME =="C_STD02" || viewDataInfo[i].FIELD_NAME =="C_STP"){
		//$("#LAST_WIDTH").val( $("#C_STD02").val() );
		changeCmsw();
		if($("#C_STD02").val()=="") {
			alert("규격 폭은 반드시 입력하셔야 합니다.");
			if(!$("#C_STD02").hasClass("text-danger")) {
				$("#C_STD02").addClass("text-danger");
			}
			$("#C_STD02").focus();
		} else {
			if($("#C_STD02").hasClass("text-danger")) {
				$("#C_STD02").removeClass("text-danger");
			}
		}
	}
	
	if(id =="C_STP" ) { //|| viewDataInfo[i].FIELD_NAME =="C_STD02" || viewDataInfo[i].FIELD_NAME =="C_STP"){
		//$("#TARGET_THICKNESS").val( $("#C_STD01").val() );
		
		if($("#C_STP").val()=="") {
			alert("강종은 반드시 입력하셔야 합니다.");
			if(!$("#C_STP").hasClass("text-danger")) {
				$("#C_STP").addClass("text-danger");
			}
			$("#C_STP").focus();
		} else {
			var id = "C_STP";
			goErrorCheck(id);
			 
		}
	}
}

function goChageData(id){  
	$('#'+id).val( $.trim($('#'+id).val().toUpperCase())  );
/*	
	if($("#TARGET_THICKNESS").val()=="") {
		 
			alert("규격 두께를 먼저 입력하셔야 허용범위 체크가 가능합니다.");
			 
			if(!$("#C_STD01").hasClass("text-danger")) {
				$("#C_STD01").addClass("text-danger");
			}
			$("#C_STD01").focus();
			return;
	}
	*/
	var value = $("#"+id).val();
	//Number( (parseFloat(data.MRMAC)-parseFloat(data.DESIC)).toFixed(2) )
 
	var dataMin = Number( ( parseFloat($("#C_STD01").val()*0.7 )).toFixed(2) );
	var dataMax = Number( ( parseFloat($("#C_STD01").val()*1.3) ).toFixed(2) );
	 
	
	if(isNaN(value) && isNaN(dataMin)  && isNaN(dataMax) ){
		 
	}else{
	//	alert(id+" : " +parseFloat(value)+" : " +parseFloat(dataMin)+" : " +parseFloat(dataMax));
		if(dataMin> (parseFloat(value))){
			$("#"+id).addClass("text-danger");
		}else if (dataMax < parseFloat(value)){
			$("#"+id).addClass("text-danger");
		}else{
			$("#"+id).removeClass("text-danger");
		}
	}
}
function goChageData2(id){  
	$('#'+id).val( $.trim($('#'+id).val().toUpperCase())  );
/*	
	if($("#LAST_WIDTH").val()=="") {
			 alert($("#LAST_WIDTH").val());
			alert("규격 폭를 먼저 입력하셔야 허용범위 체크가 가능합니다.");
			if(!$("#C_STD02").hasClass("text-danger")) {
				$("#C_STD02").addClass("text-danger");
			}
			$("#C_STD02").focus();
			 
			return;
	}
*/	
	var value = $("#"+id).val();
	//Number( (parseFloat(data.MRMAC)-parseFloat(data.DESIC)).toFixed(2) )
	var dataMin = Number( ( parseFloat($("#C_STD02").val())*0.7 ).toFixed(2) );
	var dataMax = Number( ( parseFloat($("#C_STD02").val())*1.3 ).toFixed(2) );
	
	
	if(isNaN(value) && isNaN(dataMin)  && isNaN(dataMax) ){
		 
	}else{
	//	alert(id+" : " +parseFloat(value)+" : " +parseFloat(dataMin)+" : " +parseFloat(dataMax));
		if(dataMin> (parseFloat(value))){
			$("#"+id).addClass("text-danger");
		}else if (dataMax < parseFloat(value)){
			$("#"+id).addClass("text-danger");
		}else{
			$("#"+id).removeClass("text-danger");
		}
	}
}
function changeCmst(){
	var dataMin = $("#C_STD01").val();
	var dataMax = $("#C_STD01").val();
	if(!isNaN(dataMin) && !isNaN(dataMax) ){
		for(var i=1;i<=18;i++) {  
		    var id="";
			var value='';
			if(i<10){
				id="C_MST0"+i;
				
			}else{
				id="C_MST"+i;
			}
			value = $("#"+id).val();
			if(value !="" && value !=null){
				goChageData(id);
			}
			
		}
	}
}
function changeCmsw(){
	var dataMin = $("#C_STD02").val();
	var dataMax = $("#C_STD02").val();
	if(!isNaN(dataMin) && !isNaN(dataMax) ){
		for(var i=1;i<=cMspCount;i++) {  
		    var id="";
			var value='';
			if(i<10){
				id="C_MSW0"+i;
				
			}else{
				id="C_MSW"+i;
			}
			value = $("#"+id).val();
			if(value !="" && value !=null){
				goChageData2(id);
			}
			
		}
	}
}
function changeCop1(){
	var dataMin = $("#C_STD01").val();
	var dataMax = $("#C_STD01").val();
	if(!isNaN(dataMin) && !isNaN(dataMax) ){
		for(var i=1;i<=cOp1Count;i++) {  
		    var id="";
			var value='';
			if(i<10){
				id="C_OP1"+i;
				
			}else{
				id="C_OP1"+i;
			}
			value = $("#"+id).val();
			if(value !="" && value !=null){
				goChageData(id);
			}
			
		}
	}
}

function changeCop2(){
	var dataMin = $("#C_STD01").val();
	var dataMax = $("#C_STD01").val();
	if(!isNaN(dataMin) && !isNaN(dataMax) ){
		for(var i=1;i<=cOp2Count;i++) {  
		    var id="";
			var value='';
			if(i<10){
				id="C_OP2"+i;
				
			}else{
				id="C_OP2"+i;
			}
			value = $("#"+id).val();
			if(value !="" && value !=null){
				goChageData(id);
			}
			
		}
	}
}
function checkValue(value){
	var isCheck=false;
	if(value == "B" || value == "C" || value == "D" || value == "K" || value == ""){
		isCheck = true;
	}
 	return isCheck;
}

function goErrorCheck(id){
	$('#'+id).val( $.trim($('#'+id).val().toUpperCase())  );
	
	var value = $("#"+id).val();
 
	var isCheck = false;
	if(id == "C_PP"){
		if(CppMap.containsKey(value)){
			isCheck = true;
		}
		if($("#FORM_TYPE").val() == "F"){
			isCheck = true;
		}
	}else if(id == "C_LV"){
		if(ClvMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id == "C_STP"){
		if(CstpMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id == "C_COAT"){
		if(CcoatMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id == "C_ALV"){
		if(CalvMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id == "C_SKIN"){
		if(CskinMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id == "C_CHK_NO"){
		if(CchkNoMap.containsKey(value)){
			 var userNm = CchkUserMap.get(value);
			 $("#C_CHKER").val(userNm);
			isCheck = true;
		}
	}else if(id.indexOf("C_SH") > -1){
		if(CshMap.containsKey(value)){
			
				isCheck = true;
		}else{
			if(value == ""){
				isCheck = true;
			}
		}
	}else if(id.indexOf("C_RH") > -1){
		if(CrhMap.containsKey(value)){
			isCheck = true;
		}else{
			if(value == ""){
				isCheck = true;
			}
		}
	}else if(id.indexOf("C_DEF_CD") > -1){
		if(CdefCdMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id.indexOf("C_RT") > -1){
		isCheck = checkValue(value);
	}else if(id.indexOf("C_ST") > -1){
		isCheck = checkValue(value);
	}
	//	alert(id+" : " +parseFloat(value)+" : " +parseFloat(dataMin)+" : " +parseFloat(dataMax));
	 cssChange(isCheck,id);
	/*if(isCheck){
		$("#"+id).removeClass("text-danger");
	}else{
		$("#"+id).addClass("text-danger");
	}*/
}
function cssChange(isCheck,id){
	if(isCheck){
		$("#"+id).removeClass("text-danger");
	}else{
		$("#"+id).addClass("text-danger");
	}
}
function deleteData(){
	parent.deleteInfo($("#SEQ_Q100").val(),$("#SEQ_T300").val(),$("#IN_LINE").val());
}
function errorData(){
//	checkDATA();
//checkAll();
}
function checkDATA(){
	
    var defaultArray = new Array();
    var cmInfo = new Object();
    var tempValue=0;
	for(var i=1; i<=cMCount; i++){
		var tempId = i < 10 ? "0"+i : i;
		var tempCm = $("#C_M"+tempId).val();
		var tempCsh = $("#C_SH"+tempId).val();
		var tempCst =$("#C_ST"+tempId).val();
		var tempCrh =$("#C_RH"+tempId).val();
		var tempCrt = $("#C_RT"+tempId).val();
		
		if((tempCm != "" &&  tempCm != null) && (tempCsh != "" && tempCsh != null ) && (tempCst != "" && tempCst != null)  && (tempCrh != "" && tempCrh != null) && (tempCrt != "" && tempCrt !=null) ){
			//alert(tempCm +" : "+tempCsh+" : "+tempCst+" : "+tempCrh+" : "+tempCrt);
			cmInfo = new Object();
			cmInfo.Meter = parseInt(tempCm) - tempValue;
			cmInfo.Csh = tempCsh;
			cmInfo.Cst= tempCst;
			cmInfo.Crh = tempCrh;
			cmInfo.Crt = tempCrt;
			tempValue = tempCm;
			defaultArray.push(cmInfo);
		}
	}
 
	var tempShMap= new HashMap();
	var tempStMap=new HashMap();
	var tempRhMap= new HashMap();
	var tempRtMap= new HashMap();
	var tempString="";
	for(var i=0; i<defaultArray.length; i++){
		tempString="";
		if(tempShMap.containsKey(defaultArray[i].Csh)){
			tempString=  tempStMap.get(defaultArray[i].Csh);
			var tempMeter = tempShMap.get(defaultArray[i].Csh);
			var valueMeter = parseInt(tempMeter) +parseInt(defaultArray[i].Meter);
			tempShMap.put(defaultArray[i].Csh,valueMeter);
			tempString = tempString+defaultArray[i].Cst+"_"+defaultArray[i].Meter+",";
			tempStMap.put(defaultArray[i].Csh,tempString);
		}else{
			tempString= tempString+defaultArray[i].Cst+"_"+defaultArray[i].Meter+",";
			tempShMap.put(defaultArray[i].Csh,defaultArray[i].Meter);
			tempStMap.put(defaultArray[i].Csh,tempString);
		}
		tempString="";
		if(tempRhMap.containsKey(defaultArray[i].Crh)){
			tempString=  tempRtMap.get(defaultArray[i].Crh);
			var tempMeter = tempRhMap.get(defaultArray[i].Crh);
			var valueMeter = parseInt(tempMeter) +parseInt(defaultArray[i].Meter);
			tempRhMap.put(defaultArray[i].Crh,valueMeter);
			tempString = tempString+defaultArray[i].Crt+"_"+defaultArray[i].Meter+",";
			tempRtMap.put(defaultArray[i].Crh,tempString);
		}else{
			tempRhMap.put(defaultArray[i].Crh,defaultArray[i].Meter);
			tempString = tempString+defaultArray[i].Crt+"_"+defaultArray[i].Meter+",";
			tempRtMap.put(defaultArray[i].Crh,tempString);
		}
	}
	
 	var errCodeAllMap = new HashMap();
	var tempCode =tempShMap.keys();
	var tempMax =0;
	var tempKey ="";
	var tempObject = new HashMap();
	var codeArray = new Array();
	
	for(var i=0; i<tempCode.length; i++){
		
		var isValue = tempShMap.get(tempCode[i]);
		if(isValue == 0){
			
		}else{
			var codeObject = new Object();
			var tempArray = tempStMap.get(tempCode[i]);
			codeObject = changeArray(tempArray,"S");
			//codeArray.push(codeObject);
			if(errCodeAllMap.containsKey(tempCode[i])){
				var tempObject = errCodeAllMap.get(tempCode[i]);
				tempObject.SB = parseInt(tempObject.SB) + parseInt(codeObject.SB);
				tempObject.SC = parseInt(tempObject.SC) + parseInt(codeObject.SC);
				tempObject.SD = parseInt(tempObject.SD) +parseInt( codeObject.SD);
				tempObject.SK = parseInt(tempObject.SK) + parseInt(codeObject.SK);
				errCodeAllMap.put(tempCode[i],tempObject);
			}else{
				errCodeAllMap.put(tempCode[i],codeObject);
			}
			//alert("표면의 대표 에러코드는 " +tempCode[i] +" 최대값은 "+tempShMap.get(tempCode[i]) + " 부분값은 "+JSON.stringify(codeArray));
		}
		
	
	}
	tempCode =tempRhMap.keys();
	tempMax =0;
	tempKey ="";
	tempValueMap = new HashMap();
	for(var i=0; i<tempCode.length; i++){
		var isValue = tempRhMap.get(tempCode[i]);
		if(isValue == 0){
			
		}else{
			var codeObject = new Object();
			var tempArray = tempRtMap.get(tempCode[i]);
			codeObject = changeArray(tempArray,'R');
			//codeArray.push(codeObject);
			if(errCodeAllMap.containsKey(tempCode[i])){
				var tempObject = errCodeAllMap.get(tempCode[i]);
				tempObject.RB = parseInt(tempObject.RB) + parseInt(codeObject.RB);
				tempObject.RC = parseInt(tempObject.RC) + parseInt(codeObject.RC);
				tempObject.RD = parseInt(tempObject.RD) +parseInt( codeObject.RD);
				tempObject.RK = parseInt(tempObject.RK) + parseInt(codeObject.RK);
				errCodeAllMap.put(tempCode[i],tempObject);
			}else{
				errCodeAllMap.put(tempCode[i],codeObject);
			}
			//alert("배면의 대표 에러코드는 " +tempCode[i] +" 최대값은 "+tempRhMap.get(tempCode[i]) + " 부분값은 "+JSON.stringify(codeArray));
		}
		
	
	}
	//alert(JSON.stringify(errCodeAllMap));
	var errCodeKey = errCodeAllMap.keys();

	var tempSB=0;
	var tempSC=0;
	var tempSD=0;
	var tempSK=0;
	

	var tempRB=0;
	var tempRC=0;
	var tempRD=0;
	var tempRK=0;
	var htmlStrHead="";
	var htmlStrTD ="";
	var htmlStrFoot="</TABLE>";
	var jsonDetail1Param = new Array();
	var jsonDetail2Param = new Array();
    var maxKey="";
    var maxValue=0;
	htmlStrHead = "<TABLE>"  + "<TR><TD> 코드 </TD><TD>표면B</TD><TD>표면C</TD><TD>표면D</TD><TD>표면K</TD><TD>배면B</TD><TD>배면C</TD><TD>배면D</TD><TD>배면K</TD><TD>결합누계</TD></TR>"
	for(var i=0; i<errCodeKey.length; i++){
		var paramObject = new Object();
		 var codeInfo = errCodeAllMap.get(errCodeKey[i]);
		 var total =parseInt(codeInfo.SB)+parseInt(codeInfo.SC)+parseInt(codeInfo.SD)+parseInt(codeInfo.SK)
						+ parseInt(codeInfo.RB)
						+ parseInt(codeInfo.RC)
						+ parseInt(codeInfo.RD)
						+	parseInt(codeInfo.RK);
		 
		 var dk_MAX = parseInt(codeInfo.SD)+parseInt(codeInfo.SK)+ parseInt(codeInfo.RD)+parseInt(codeInfo.RK);
		 
		 if(dk_MAX  >  maxValue){
			 maxValue=dk_MAX;
			 maxKey=errCodeKey[i];
		 }
		 var alertStr = errCodeKey[i]+" 표면 B :" +codeInfo.SB+" 표면 C :" +codeInfo.SC+" 표면 D:" +codeInfo.SD+" 표면 K:" +codeInfo.SK
		 					 +" 배면 B :" +codeInfo.RB+" 배면 C : " +codeInfo.RC+" 배면 D : " +codeInfo.RD+" 배면 K : " +codeInfo.RK +"\n 결함 누계 : "+total;
	//	alert(alertStr);
		paramObject.code=errCodeKey[i];
		paramObject.SB=codeInfo.SB;
		paramObject.SC=codeInfo.SC;
		paramObject.SD=codeInfo.SD;
		paramObject.SK=codeInfo.SK;
		paramObject.RB=codeInfo.RB;
		paramObject.RC=codeInfo.RC;
		paramObject.RD=codeInfo.RD;
		paramObject.RK=codeInfo.RK;
		paramObject.SUM = total;
		jsonDetail1Param.push(paramObject);
		htmlStrTD = htmlStrTD + "<TR><TD>"+errCodeKey[i]+" </TD>"+"<TD>"+codeInfo.SB+" </TD>"+"<TD>"+codeInfo.SC+" </TD>"+"<TD>"+codeInfo.SD+" </TD>"+"<TD>"+codeInfo.SK+" </TD>"
		                    +"<TD>"+codeInfo.RB+" </TD>"+"<TD>"+codeInfo.RC+" </TD>"+"<TD>"+codeInfo.RD+" </TD>"+"<TD>"+codeInfo.RK+" </TD>"+"<TD>"+total+" </TD></TR>";
	//	alert(htmlStrTD);

		tempSB = parseInt(tempSB)+ parseInt(codeInfo.SB);
		tempSC = parseInt(tempSC)+ parseInt(codeInfo.SC);
		tempSD = parseInt(tempSD)+ parseInt(codeInfo.SD);
		tempSK = parseInt(tempSK)+ parseInt(codeInfo.SK);
		tempRB = parseInt(tempRB)+ parseInt(codeInfo.RB);
		tempRC = parseInt(tempRC)+ parseInt(codeInfo.RC);
		tempRD = parseInt(tempRD)+ parseInt(codeInfo.RD);
		tempRK = parseInt(tempRK)+ parseInt(codeInfo.RK);
		
	}
	var sTotal  = tempSB+tempSC+tempSD+tempSK;
	var rTotal  = tempRB+tempRC+tempRD+tempRK;
	htmlStrHead="<TABLE>";
    htmlStrTD +="<TR><TD>&nbsp;</TD><TD>&nbsp;</TD><TD>A</TD><TD>B</TD><TD>C</TD><TD>D</TD><TD>K</TD><TD>계</TD></TR>";
    htmlStrTD +="<TR><TD>표면</TD><TD>M</TD>><TD>"+tempSB+"</TD><TD>"+tempSC+"</TD><TD>"+tempSD+"</TD><TD>"+tempSK+"</TD><TD>"+sTotal+"</TD></TR>";
    htmlStrTD +="<TR><TD>표면</TD><TD>%</TD><TD>" + Number( ( parseFloat(tempSB/sTotal)*100 ).toFixed(2) ) +"</TD><TD>" + Number( ( parseFloat(tempSC/sTotal)*100 ).toFixed(2) ) +"</TD><TD>" + Number( ( parseFloat(tempSD/sTotal)*100 ).toFixed(2) ) +"</TD><TD>" + Number( ( parseFloat(tempSK/sTotal)*100 ).toFixed(2) ) +"</TD><TD>" +Number( ( parseFloat(sTotal/sTotal)*100 ).toFixed(2) )+"</TD></TR>";
    htmlStrTD +="<TR><TD>배면</TD><TD>M</TD><TD>"+tempRB+"</TD><TD>"+tempRC+"</TD><TD>"+tempRD+"</TD><TD>"+tempRK+"</TD><TD>"+rTotal+"</TD></TR>";
    htmlStrTD +="<TR><TD>배면</TD><TD>%</TD><TD>" + Number( ( parseFloat(tempRB/rTotal)*100 ).toFixed(2) ) +"</TD><TD>" + Number( ( parseFloat(tempRC/rTotal)*100 ).toFixed(2) ) +"</TD><TD>" + Number( ( parseFloat(tempRD/rTotal)*100 ).toFixed(2) ) +"</TD><TD>" + Number( ( parseFloat(tempRK/rTotal)*100 ).toFixed(2) ) +"</TD><TD> " +Number( ( parseFloat(rTotal/rTotal)*100 ).toFixed(2) )+"</TD></TR>";
    htmlStrFoot="</TABLE>"
    var paramObject = new Object();
    paramObject.SB=tempSB;
    paramObject.SC=tempSC;
    paramObject.SD=tempSD;
    paramObject.SK=tempSK;
    paramObject.ST=sTotal;
    paramObject.SBP=Number( ( parseFloat(tempSB/sTotal)*100 ).toFixed(2) );
    paramObject.SCP=Number( ( parseFloat(tempSC/sTotal)*100 ).toFixed(2) );
    paramObject.SDP=Number( ( parseFloat(tempSD/sTotal)*100 ).toFixed(2) );
    paramObject.SKP=Number( ( parseFloat(tempSK/sTotal)*100 ).toFixed(2) );
    paramObject.STP=Number( ( parseFloat(sTotal/sTotal)*100 ).toFixed(2) );
    paramObject.RB=tempRB;
    paramObject.RC=tempRC;
    paramObject.RD=tempRD;
    paramObject.RK=tempRK;
    paramObject.RT=rTotal;
	paramObject.RBP=Number( ( parseFloat(tempRB/rTotal)*100 ).toFixed(2) );
    paramObject.RCP=Number( ( parseFloat(tempRC/rTotal)*100 ).toFixed(2) );
    paramObject.RDP=Number( ( parseFloat(tempRD/rTotal)*100 ).toFixed(2) );
    paramObject.RKP=Number( ( parseFloat(tempRK/rTotal)*100 ).toFixed(2) );
	paramObject.RTP=Number( ( parseFloat(rTotal/rTotal)*100 ).toFixed(2) );
    
    jsonDetail2Param.push(paramObject);
	//alert("표면 A : " + tempSA +"표면 B : " + tempSB +" 표면 C : " + tempSC +" 표면 D : " + tempSD +" 표면 K : " + tempSK +" 계 : " +sTotal);
	//alert("배면 A : " + tempRA +"배면 B : " + tempRB +" 배면 C : " + tempRC +" 배면 D : " + tempRD +" 배면 K : " + tempRK +" 계 : " +rTotal);
	
	//alert("표면 A : " + (parseFloat(tempSA/sTotal)*100).toFixed(2) +" 표면 B : " + Number( ( parseFloat(tempSB/sTotal)*100 ).toFixed(2) ) +" 표면 C : " + Number( ( parseFloat(tempSC/sTotal)*100 ).toFixed(2) ) +" 표면 D : " + Number( ( parseFloat(tempSD/sTotal)*100 ).toFixed(2) ) +" 표면 K : " + Number( ( parseFloat(tempSK/sTotal)*100 ).toFixed(2) ) +" 계 : " +Number( ( parseFloat(sTotal/sTotal)*100 ).toFixed(2) ));
	//alert("배면 A : " + (parseFloat(tempRA/rTotal)*100).toFixed(2) +" 배면 B : " + Number( ( parseFloat(tempRB/rTotal)*100 ).toFixed(2) ) +" 배면 C : " + Number( ( parseFloat(tempRC/rTotal)*100 ).toFixed(2) ) +" 배면 D : " + Number( ( parseFloat(tempRD/rTotal)*100 ).toFixed(2) ) +" 배면 K : " + Number( ( parseFloat(tempRK/rTotal)*100 ).toFixed(2) ) +" 계 : " +Number( ( parseFloat(rTotal/rTotal)*100 ).toFixed(2) ));
	//alert("배면 A : " + tempRA +"배면 B : " + tempRB +"배면 C : " + tempRC +"배면 D : " + tempRD +"배면 K : " + tempRK +" 계 : " +rTotal);
	//alert(JSON.stringify(tempStMap));
	//alert(JSON.stringify(tempShMap));
	//alert(JSON.stringify(tempRtMap));
	var totalInfo = new Object();
	totalInfo.codeType = jsonDetail1Param;
	totalInfo.codeSum = jsonDetail2Param;
	var jsonDetail3Param = new Array();
	var cBest = $("#C_BEST").val();
	if(cBest != ""){
		if(cBest =="B") {
			for(var j=0; j<2;j++){
				for(var i =1; i <(cCoilStCount+1); i++){
					var tempId = i < 10 ? "0"+i : i;
					var tempMstart = $("#C_COIL_ST"+tempId).val();
					var tempMend= $("#C_COIL_ED"+tempId).val();
					if(tempMstart != "" && tempMend !=""){
						  var paramObject = new Object();
						  if(j==0){
							  cBest ='R';
						  }else{
							  cBest ='S';
						  }
						  paramObject.COLLECT_TYPE = cBest;
						  paramObject.M_START = tempMstart;
						  paramObject.M_END = tempMend;
						  jsonDetail3Param.push(paramObject);
					}
					
				}
			}
		}else if (cBest =="R") { // B R S
			for(var i =1; i <(cCoilStCount+1); i++){
				var tempId = i < 10 ? "0"+i : i;
				var tempMstart = $("#C_COIL_ST"+tempId).val();
				var tempMend= $("#C_COIL_ED"+tempId).val();
				if(tempMstart != "" && tempMend !=""){
					  var paramObject = new Object();
					  paramObject.COLLECT_TYPE = cBest;
					  paramObject.M_START = tempMstart;
					  paramObject.M_END = tempMend;
					  jsonDetail3Param.push(paramObject);
				}
				
			}
		}else if (cBest =="S"){
			for(var i =1; i <5; i++){
				var tempId = i < 10 ? "0"+i : i;
				var tempMstart = $("#C_COIL_ST"+tempId).val();
				var tempMend= $("#C_COIL_ED"+tempId).val();
				if(tempMstart != "" && tempMend !=""){
					  var paramObject = new Object();
					  paramObject.COLLECT_TYPE = cBest;
					  paramObject.M_START = tempMstart;
					  paramObject.M_END = tempMend;
					  jsonDetail3Param.push(paramObject);
				}
				
			}
		}
		
	}
	

	totalInfo.collingSE = jsonDetail3Param;
	if($("#FORM_CODE").val()=="FORM_QICS_C" || $("#FORM_CODE").val()=="FORM_QICS_D"){
		if($("#C_BEST").val() == "R"){
			$("#C_BEST_VALUE").val("IN");
		}else if($("#C_BEST").val() == "S" || $("#C_BEST").val() == "B"){
			$("#C_BEST_VALUE").val("OUT");
		}else{
			
		}
	}else {
		if($("#C_BEST").val() == "B"){
			$("#C_BEST_VALUE").val("양면");
		}else if($("#C_BEST").val() == "R"){
			$("#C_BEST_VALUE").val("배면");
		}else if($("#C_BEST").val() == "S"){
			$("#C_BEST_VALUE").val("표면");
		}else{
			
		}
	}
	if($("#C_HOOP").val() == "Y"){
	
		$("#C_HOOP_VALUE").val("정상")
	}else if($("#C_HOOP").val() == "N"){
		$("#C_HOOP_VALUE").val("비정상")
	}else{
		
	}
	if($("#C_COAT").val() == ""){
		
		$("#C_COAT_VALUE").val("NO")
	}else{
		$("#C_COAT_VALUE").val($("#C_COAT").val());
	} 
	$("#C_POC_NO01_POD").val($("#C_POC_NO01_POD").val().toUpperCase());
	$("#C_POC_NO02_POD").val($("#C_POC_NO02_POD").val().toUpperCase());
	$("#C_POC_NO03_POD").val($("#C_POC_NO03_POD").val().toUpperCase());
	
	var poc ="";
	var poc1 = $("#C_POC_NO01_POD").val();
	var poc2 = $("#C_POC_NO02_POD").val();
	var poc3 = $("#C_POC_NO03_POD").val();
 	poc = poc1+"-"+poc2+"-"+poc3;
    var tempPoc = poc.split("-");
    var poc_no="";
    for(var i = 0; i<tempPoc.length; i++){
		if(tempPoc[i] == ""){
			break;
		}else{
			if(i==0){
				poc_no = tempPoc[i];
			}else{
				poc_no = poc_no+"-"+ tempPoc[i];
			}
			
		}
	}
    document.frameForm.ORDER_DELIVERY.value = $("#C_DEST").html();
	document.frameForm.POC_NO.value=poc_no;
	var thmax01 = $("#C_THMAX01_POD").val();
	var thmax02 = $("#C_THMAX02_POD").val();
	var thmin01 = $("#C_THMIN01_POD").val();
	var thmin02 = $("#C_THMIN02_POD").val();
	var thmax ="";
	var thmin  ="";
	var thrange="";
	if(thmax01 != "" && thmax02 != "" ){
		thmax = thmax01+"."+thmax02;
	}else if(thmax01 == "" && thmax02 != "" ){
		thmax = "0."+thmax02;
	}else if(thmax01 != "" && thmax02 == ""){
		thmax = thmax01+".00";
	}else{
		thmax = "";
	}
	if(thmin01 != "" && thmin02 != "" ){
		thmin = thmin01+"."+thmin02;
	}else if(thmin01 == "" && thmin02 != "" ){
		thmin =  "0."+thmin02;
	}else if(thmin01 != "" && thmin02 == ""){
		thmin = thmin01 +".00";
	}else{
		thmin = "";
	}
	if(thmax != "" && thmin != "" ){
		thrange = thmin+"~"+thmax;
	}else if(thmax == "" && thmin != "" ){
		thrange = thmin+"~";
	}else if(thmax != "" && thmin == "" ){
		thrange ="~"+thmax;
	}else{
		thrange = "";
	}
	
	document.frameForm.TICKNESS_RANGE.value=thrange;
	document.frameForm.TARGET_THICKNESS.value=$("#C_STD01").val();
	document.frameForm.LAST_WIDTH.value=$("#C_STD02").val();
	//alert($("#TICKNESS_RANGE").val());
	//alert(document.frameForm.TARGET_THICKNESS.value +" : "+ 	document.frameForm.LAST_WIDTH.value);
	$("#ERROR_CODE").val(JSON.stringify(totalInfo));
	$("#NG_CODE").val(maxKey);;
	$("#NG_QUANTITY").val(maxValue);
	

}
function changeArray(dataArray,type){
	var result="";
	var arrayKey ="";
	var arrayValue="";
	var tempArray="";
	var tempValue=0;
	var tempSplit="";
	var tempValueMap = new HashMap();
	dataArray = dataArray.substring(0,dataArray.length-1);
	if(dataArray.indexOf(",") <1){
		tempSplit = dataArray.split("_");
		arrayKey =tempSplit[0];
		arrayValue =tempSplit[1];
		if(tempValueMap.containsKey(arrayKey)){
			tempValue = tempValue+parseInt(tempValueMap.get(arrayKey));
			tempValueMap.put(arrayKey,arrayValue);
		}else{
			tempValueMap.put(arrayKey,arrayValue);
		}
		
	}else{
		tempArray = dataArray.split(",");
		for(var i=0; i<tempArray.length; i++){
			tempSplit =tempArray[i].split("_");
			arrayKey =tempSplit[0];
			arrayValue =tempSplit[1];
			
			if(tempValueMap.containsKey(arrayKey)){
				 
				tempValue = parseInt(arrayValue)+parseInt(tempValueMap.get(arrayKey));
				tempValueMap.put(arrayKey,tempValue);
			}else{
				tempValueMap.put(arrayKey,arrayValue);
			}
		}
	}
	var codeInfo = new Object();
	codeInfo.SB="0";
	codeInfo.SC="0";
	codeInfo.SD="0";
	codeInfo.SK="0";
	codeInfo.RB="0";
	codeInfo.RC="0";
	codeInfo.RD="0";
	codeInfo.RK="0";
	
	var tempCode =tempValueMap.keys();
	for(var i=0; i<tempCode.length; i++){
		if(type == "S"){
			if(tempCode[i] == "B"){
				codeInfo.SB=tempValueMap.get(tempCode[i]);
			}else if(tempCode[i] == "C"){
				codeInfo.SC=tempValueMap.get(tempCode[i]);
			}else if(tempCode[i] == "D"){
				codeInfo.SD=tempValueMap.get(tempCode[i]);
			}else if(tempCode[i] == "K"){
				codeInfo.SK=tempValueMap.get(tempCode[i]);
			}
		}else if (type == "R"){
			if(tempCode[i] == "B"){
				codeInfo.RB=tempValueMap.get(tempCode[i]);
			}else if(tempCode[i] == "C"){
				codeInfo.RC=tempValueMap.get(tempCode[i]);
			}else if(tempCode[i] == "D"){
				codeInfo.RD=tempValueMap.get(tempCode[i]);
			}else if(tempCode[i] == "K"){
				codeInfo.RK=tempValueMap.get(tempCode[i]);
			}
		}

	}
	
	return codeInfo;
}
function cMdata(){
	//var textArr = $("input[name^='C_M**']");//menu를 포함하는$( "input[name^='news']" ).val( "news here!" );
    var defaultArray = new Array();
    var cmInfo = new Object();
    var tempValue=0;
	for(var i=1; i<=cMCount; i++){
		var tempId = i < 10 ? "0"+i : i;
		var tempCm = $("#C_M"+tempId).val();
		var tempCsh = $("#C_SH"+tempId).val();
		var tempCst =$("#C_ST"+tempId).val();
		var tempCrh =$("#C_RH"+tempId).val();
		var tempCrt = $("#C_RT"+tempId).val();
		
		if((tempCm != "" &&  tempCm != null) && (tempCsh != "" && tempCsh != null ) && (tempCst != "" && tempCst != null)  && (tempCrh != "" && tempCrh != null) && (tempCrt != "" && tempCrt !=null) ){
			//alert(tempCm +" : "+tempCsh+" : "+tempCst+" : "+tempCrh+" : "+tempCrt);
			cmInfo = new Object();
			cmInfo.Meter = parseInt(tempCm) - tempValue;
			cmInfo.Csh = tempCsh;
			cmInfo.Cst= tempCst;
			cmInfo.Crh = tempCrh;
			cmInfo.Crt = tempCrt;
			tempValue = tempCm;
			defaultArray.push(cmInfo);
			//표면
			
			//배면
		}
	}
	 
	var tempStMap= new HashMap();
	var tempRtMap= new HashMap();
	for(var i=0; i<defaultArray.length; i++){
		if(tempStMap.containsKey(defaultArray[i].Cst)){
			var tempMeter = tempStMap.get(defaultArray[i].Cst);
			var valueMeter = parseInt(tempMeter) +parseInt(defaultArray[i].Meter);
			tempStMap.put(defaultArray[i].Cst,valueMeter);
		}else{
			tempStMap.put(defaultArray[i].Cst,defaultArray[i].Meter);
		}
		if(tempRtMap.containsKey(defaultArray[i].Crt)){
			var tempMeter = tempRtMap.get(defaultArray[i].Crt);
			var valueMeter = parseInt(tempMeter) +parseInt(defaultArray[i].Meter);
			tempRtMap.put(defaultArray[i].Crt,valueMeter);
		}else{
			tempRtMap.put(defaultArray[i].Crt,defaultArray[i].Meter);
		}
	}
	//alert(JSON.stringify(hm));
	//alert(JSON.stringify(tempStMap));
	//alert(JSON.stringify(tempRtMap));
	var tempCode =tempStMap.keys();
	var tempMax = 0;
	var tempKey="";
	for(var i=0; i<tempCode.length; i++){
		 if(parseInt(tempMax) < tempStMap.get(tempCode[i])){
			 tempMax = tempStMap.get(tempCode[i]);
			 tempKey = tempCode[i];
		 }
	}
	//alert(tempKey +" : " +tempMax );
	var errorCode="";
	for(var i=0; i<defaultArray.length; i++){
		if(tempKey ==defaultArray[i].Cst){
			errorCode =defaultArray[i].Csh;
			break;
		}
	}
	alert("표면의 "+tempKey +" : " +tempMax+" : " +errorCode);
	$("#NG_CODE").val(errorCode);
	tempCode =tempRtMap.keys();
	tempKey="";
	tempMax=0;
	errorCode="";
	for(var i=0; i<tempCode.length; i++){
		 if(parseInt(tempMax) < tempRtMap.get(tempCode[i])){
			 tempMax = tempRtMap.get(tempCode[i]);
			 tempKey = tempCode[i];
		 }
	}
	for(var i=0; i<defaultArray.length; i++){
		if(tempKey ==defaultArray[i].Crt){
			errorCode =defaultArray[i].Crh;
			break;
		}
	}
	alert("배면의 "+tempKey +" : " +tempMax+" : " +errorCode);
	//checkPreValue();
	checkAll();
}
function checkPreCmValue(){

	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cMCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_M"+tempId).val();
		if(tempValue !=""){
			 if(parseInt(tempMax) <= parseInt(tempValue)){
				 tempMax = tempValue;
				 isCheck = true;
			 }
			 cssChange(isCheck,"C_M"+tempId);
			 isCheck = false;
		}else{
			 isCheck = true;
			cssChange(isCheck,"C_M"+tempId);
			 isCheck = false;
		}
		
	}
}
function checkPreCnValue(){
	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cNCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_N"+tempId).val();
		if(tempValue !=""){
			 if(parseInt(tempMax) <= parseInt(tempValue)){
				 tempMax = tempValue;
				 isCheck = true;
			 }
			 cssChange(isCheck,"C_N"+tempId);
			 isCheck = false;
		}else{
			 isCheck = true;
				cssChange(isCheck,"C_N"+tempId);
				 isCheck = false;
			}
		
	}
}
function checkPreChValue(){
	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cHCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_H"+tempId).val();
		if(tempValue !=""){
			 if(parseInt(tempMax) <= parseInt(tempValue)){
				 tempMax = tempValue;
				 isCheck = true;
			 }
			 cssChange(isCheck,"C_H"+tempId);
			 isCheck = false;
		}else{
			 isCheck = true;
				cssChange(isCheck,"C_H"+tempId);
				 isCheck = false;
			}
		
	}
}
function checkPreCmspValue(){
	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cMspCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_MSP"+tempId).val();
		if(tempValue !=""){
			 if(parseInt(tempMax) <= parseInt(tempValue)){
				 tempMax = tempValue;
				 isCheck = true;
			 }
			 cssChange(isCheck,"C_MSP"+tempId);
			 isCheck = false;
		}else{
			 isCheck = true;
				cssChange(isCheck,"C_MSP"+tempId);
				 isCheck = false;
			}
	}
}
function checkPreCmspValue(){
	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cMspCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_MSP"+tempId).val();
		if(tempValue !=""){
			 if(parseInt(tempMax) <= parseInt(tempValue)){
				 tempMax = tempValue;
				 isCheck = true;
			 }
			 cssChange(isCheck,"C_MSP"+tempId);
			 isCheck = false;
		}else{
			 isCheck = true;
				cssChange(isCheck,"C_MSP"+tempId);
				 isCheck = false;
			}
	}
}
function checkPreCrollStopValue(){
	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cRollStopCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_ROLL_STOP"+tempId).val();
		if(tempValue !=""){
			 if(parseInt(tempMax) <= parseInt(tempValue)){
				 tempMax = tempValue;
				 isCheck = true;
			 }
			 cssChange(isCheck,"C_ROLL_STOP"+tempId);
			 isCheck = false;
		}else{
			 isCheck = true;
				cssChange(isCheck,"C_ROLL_STOP"+tempId);
				 isCheck = false;
			}
	}
}

function checkDefStEdValueByRow(row) {
	var stValue = $("#C_DEF_ST" + row).val();
	var edValue = $("#C_DEF_ED" + row).val();
	var isCheck = false;
	//alert(stValue+"::"+edValue);
	if(  stValue!="" && edValue==""  ) {
		isCheck = false;
		cssChange(isCheck,"C_DEF_ED"+row);
	} else if(  stValue=="" && edValue!=""  ) {
		isCheck = false;
		cssChange(isCheck,"C_DEF_ST"+row);
	} else if(  parseInt(stValue) > parseInt(edValue)  ) {
		isCheck = false;
		cssChange(isCheck,"C_DEF_ED"+row);
	} else if ( stValue!="" && edValue!=""  &&  (  parseInt(stValue) <= parseInt(edValue)  )  ){
		isCheck = true;
		cssChange(isCheck,"C_DEF_ST"+row);
		cssChange(isCheck,"C_DEF_ED"+row);
	} else if(  stValue=="" && edValue==""  ) {
		isCheck = true;
		cssChange(isCheck,"C_DEF_ST"+row);
		cssChange(isCheck,"C_DEF_ED"+row);
	}
}

function checkDefStEdValue(row) {
	if(row=="init") {
		for(var i=1;i<=5;i++) {
		 	checkDefStEdValueByRow("0"+row);
		}
	} else {
		checkDefStEdValueByRow(row);
	}
	
}

function checkPreCoilingValue() {
	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=(cCoilStCount); i++){
		for(var j=1; j<=2; j++){
			tempId = i < 10 ? "0"+i : i;
			
			if(j==1) {
				tempValue = $("#C_COIL_ST"+tempId).val();
				if(tempValue !=""){
					 if(parseInt(tempMax) <= parseInt(tempValue)){
						 tempMax = tempValue;
						 isCheck = true;
					 }
					 cssChange(isCheck,"C_COIL_ST"+tempId);
					 isCheck = false;
				}else{
					 isCheck = true;
					 cssChange(isCheck,"C_COIL_ST"+tempId);
					 isCheck = false;
				}
			} else if(j==2){
				tempValue = $("#C_COIL_ED"+tempId).val();
				if(tempValue !=""){
					 if(parseInt(tempMax) <= parseInt(tempValue)){
						 tempMax = tempValue;
						 isCheck = true;
					 }
					 cssChange(isCheck,"C_COIL_ED"+tempId);
					 isCheck = false;
				}else{
					 isCheck = true;
					 cssChange(isCheck,"C_COIL_ED"+tempId);
					 isCheck = false;
				}
			}
			
			
		}
	}
	
	
	
}

function checkPreValue(){

	var tempMax = 0;
	var tempId="";
	var tempValue="";
	var isCheck = false;
	for(var i=1; i<=cMCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_M"+tempId).val();
		 if(parseInt(tempMax) <= parseInt(tempValue)){
			 tempMax = tempValue;
			 isCheck = true;
		 }
		 cssChange(isCheck,"C_M"+tempId);
		 isCheck = false;
	}
	tempMax = 0;
	tempId="";
	tempValue="";
	isCheck = false;
	for(var i=1; i<=cMspCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_MSP"+tempId).val();
		 if(parseInt(tempMax) <= parseInt(tempValue)){
			 tempMax = tempValue;
			 isCheck = true;
		 }
		 cssChange(isCheck,"C_MSP"+tempId);
		 isCheck = false;
	}
	tempMax = 0;
	tempId="";
	tempValue="";
	isCheck = false;
	for(var i=1; i<=cRollStopCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_ROLL_STOP"+tempId).val();
		 if(parseInt(tempMax) <= parseInt(tempValue)){
			 tempMax = tempValue;
			 isCheck = true;
		 }
		 cssChange(isCheck,"C_ROLL_STOP"+tempId);
		 isCheck = false;
	}
/*	tempMax = 0;
	tempId="";
	var firstValue="";
	var lastValue="";
	isCheck = false;
	for(var i=1; i<=cCoilStCount; i++){      //  ------cCoilStCount cCoilEd
		tempId = i < 10 ? "0"+i : i;
	     firstValue = $("#C_COIL_ST"+tempId).val();
	     lastValue = $("#C_COIL_ED"+tempId).val();
		 if(parseInt(firstValue) <= parseInt(lastValue)){
			 isCheck = true;
		 }
		 cssChange(isCheck,"C_COIL_ST"+tempId);
		 isCheck = false;
		 
	}*/
	tempMax = 0;
	tempId="";
	tempValue="";
	isCheck = false;
	for(var i=1; i<=cNCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_N"+tempId).val();
		 if(parseInt(tempMax) <= parseInt(tempValue)){
			 tempMax = tempValue;
			 isCheck = true;
		 }
		 cssChange(isCheck,"C_N"+tempId);
		 isCheck = false;
	}
	tempMax = 0;
	tempId="";
	tempValue="";
	isCheck = false;
	for(var i=1; i<=cHCount; i++){
		tempId = i < 10 ? "0"+i : i;
		tempValue = $("#C_H"+tempId).val();
		 if(parseInt(tempMax) <= parseInt(tempValue)){
			 tempMax = tempValue;
			 isCheck = true;
		 }
		 cssChange(isCheck,"C_H"+tempId);
		 isCheck = false;
	}
}
function checkAll(){
	var isError =false;
	var isId="";
	var msg = "";
	$("input[type='text']").each(function() {
    	isError = $("#"+this.id).hasClass("text-danger");
    	if($('#'+this.id).val() != "" ){
    		if(this.id == "C_POC_NO01_POD" || this.id == "C_POC_NO02_POD" || this.id =="C_POC_NO03_POD" 
    				|| this.id=="C_THMIN01_POD" || this.id=="C_THMIN02_POD" || this.id=="C_THMAX01_POD"  || this.id=="C_THMAX02_POD"){
    		}else{
    			if(isError){
    				msg ="입력값을 확인 하시기바랍니다.";
    	    		isId = this.id;
    	    		return false; 
    	    	}
    		}
	    	
    	}
	});
	//측정치 확인
	//C_MSP01.5
	var tempId="";
	//var cmspValue="";
//	var isCheck = false;

    //측청치 항목 체크
	for(var i=1; i<=cMspCount; i++){
		tempId = i < 10 ? "0"+i : i;
		var cmspValue = $("#C_MSP"+tempId).val(); //위치
		var cmstValue = $("#C_MST"+tempId).val(); //두께
		var cmswValue = $("#C_MSW"+tempId).val(); //폭
		if(cmspValue != ""){
			if(cmstValue ==""){
				msg = "두께 값을 입력해주시기 바랍니다.";
				isId = "C_MST"+tempId;
				isError=true;
				break;
			}else if(cmswValue ==""){
				msg = "폭을 입력해주시기 바랍니다.";
				isId = "C_MSW"+tempId;
				isError=true;
				break;
			}
		}
	}
	//소구간 확인
	//C_M01.8
 	
	//검사결과 확인
	//C_ROLL_STOP01.ㅐ
	if($("#C_CHK_DT").val() ==""){
		isId = "C_CHK_DT";
		isError =true;
		msg ="날짜를 선택해주세요"
	}else{
		document.frameForm.WORK_DATE.value =$("#C_CHK_DT").val();
	}
	if($("#C_SKIN").val() ==""){
		isId = "C_SKIN";
		isError =true;
		msg ="작업표면은 필수입력항목입니다. "
	}
	if($("#C_LV").val() ==""){
		isId = "C_LV";
		isError =true;
		msg ="등급은 필수입력항목입니다. "
	}
	
	if($("#FORM_TYPE").val() =="F"){
		
		if($("#C_LV").val() !=""){
			
			var cLv = $("#C_LV").val();
			
			if(cLv =="T" || cLv =="H1" || cLv =="D2" || cLv =="D3" || cLv =="E2" || cLv =="E3" || cLv =="S" || cLv =="XX"
				|| cLv =="RR" || cLv =="QQ" || cLv =="ZZ"
			){
				if(cLv =="T" || cLv =="H1"){
					 if($("#C_MDCODE").val()==""){
							isId = "C_MDCODE";
							isError =true;
							msg ="주결함코드는 필수입력항목입니다. "
						}
					 if($("#C_MDCODE").val()!=""){
						 
						 if($("#C_HOOP").val() ==""){
								msg ="H1/T급상태는 필수입력항목입니다. "
								alert(msg);
								return false;
							}
					 }
				}else{
				 	if($("#C_MDCODE").val()==""){
						isId = "C_MDCODE";
						isError =true;
						msg ="주결함코드는 필수입력항목입니다. "
					}
				}
				
			}
			
		}
	}
	
	
	if(isError){
		$('#'+isId).focus();
		alert(msg);
		return false;
	}else{
		//alert("저장가능합니다.");
		return true;
	}
}
function sendErp(){ //데이터 확인 오류 및 수정
	// isCheck = $('#'+strTemp[i]).hasClass("btn btn-option btn-xxs active");
	//$('').find
	var isOk = checkAll();
    if(isOk){
    	var result = ajaxCall("/Erp.do?cmd=dataErp", $("#frameForm").serialize(), false);  //작업내역 상세검색
    	var status = result.DATA.Status;
    	var message = result.DATA.Message;
    	if(status =="T"){
    		 alert(message.split(",").join("\n"));
    		 var messageSplit = message.split(",");
    			var isCheck =false;
    			var id ="";
    			for(var i = 0; i<messageSplit.length; i++){
    			//	alert(messageSplit[i]);
    				if(messageSplit[i].indexOf("등록되지 않은 검사자") > -1){
    					id ="C_CHK_NO";
    					cssChange(isCheck,id);
    				}
    				if(messageSplit[i].indexOf("ERP실적 미존재 POC") > -1){
    					id = "C_POC_NO01_POD";
    				//	cssChange(isCheck,id);
    					id = "C_POC_NO02_POD";
    				//	cssChange(isCheck,id);
    					id = "C_POC_NO03_POD";
    				//	cssChange(isCheck,id);
    					break;
    				} 
    				if(messageSplit[i].indexOf("강종이 미존재") > -1){
    					id = "C_STP";
    					cssChange(isCheck,id);
    				}
    				if(messageSplit[i].indexOf("POC 두께 허용범위") > -1){
    					id = "C_THMIN01_POD";
    				//	cssChange(isCheck,id);
    					id = "C_THMIN02_POD";
    				//	cssChange(isCheck,id);
    					id = "C_THMAX01_POD";
    				//	cssChange(isCheck,id);
    					id = "C_THMAX02_POD";
    				//	cssChange(isCheck,id);
    					
    				}
    				if(messageSplit[i].indexOf("POC 폭") > -1){
    					id = "C_STD02";
    					cssChange(isCheck,id);
    				}
    				
    			}
    		isSave =false;
    		$("#my-button5").addClass("disabled");
    		parent.changeNm($("#SEQ_Q100").val());
    	}else if(status =="C"){
    		message = result.DATA.Message;
    		alert(message);
    		parent.erpEnd($("#SEQ_Q100").val(),$("#SEQ_T300").val(),$("#IN_LINE").val());
    	}else if(status =="E"){
    		message = result.DATA.Message;
    		alert(message);
    		var idx = $("#SEQ_Q100").val();
    		parent.changeNm($("#SEQ_Q100").val());
    	}else if(status =="W"){
    		message = result.DATA.Message;
    		alert(message);
    		isSave =false;
    		$("#my-button5").addClass("disabled");
    		parent.changeNm($("#SEQ_Q100").val());
    	}else{
    		
    	}
    }

   
}

</script>
<body>
        <form  id="frameForm" name="frameForm" class="form-horizontal">
                	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100"  value="<%= SEQ_Q100%>" />
                	<input type="hidden" id="SEQ_T300" name="SEQ_T300"  value="<%= SEQ_T300%>" />
                	<input type="hidden" id="IN_LINE" name="IN_LINE"  value="<%= IN_LINE%>" />
                	<input type="hidden" id="WEB_DATA_YN" name="WEB_DATA_YN"  value="<%= WEB_DATA_YN%>" />
                	<input type="hidden" id="FORM_TYPE" name="FORM_TYPE"  value="<%= FORM_TYPE%>" />
                	<input type="hidden" id="E_STATUS" name="E_STATUS"  value="<%= E_STATUS%>" />
                	<input type="hidden" id="FORM_CODE" name="FORM_CODE"  value="<%= FORM_CODE%>" />
                	<input type="hidden" id="STATUS_CODE" name="STATUS_CODE"  value="<%= STATUS_CODE%>" />
                	<input type="hidden" id="FORM_SEQ" name="FORM_SEQ"  />
                	<input type="hidden" id="PAGE_ORDER" name="PAGE_ORDER"   />
                	<input type="hidden" id="WORK_DATE" name="WORK_DATE"  />      
			        <input type="hidden" id="LAST_WIDTH" name="LAST_WIDTH"  />      
			        <input type="hidden" id="TARGET_THICKNESS" name="TARGET_THICKNESS"  />      
			        <input type="hidden" id="NG_CODE" name="NG_CODE"  />      
			        <input type="hidden" id="NG_QUANTITY" name="NG_QUANTITY"  />      
			        <input type="hidden" id="POC_NO" name="POC_NO"  />      
			        <input type="hidden" id="ERROR_CODE" name="ERROR_CODE"  />      
			        <input type="hidden" id="ORGANIZATION_ID" name="ORGANIZATION_ID"  />      
			        <input type="hidden" id="ORDER_DELIVERY" name="ORDER_DELIVERY"  />      
			        <input type="hidden" id="C_BEST_VALUE" name="C_BEST_VALUE"  />      
			        <input type="hidden" id="C_HOOP_VALUE" name="C_HOOP_VALUE"  />      
			        <input type="hidden" id="C_COAT_VALUE" name="C_COAT_VALUE"  />      
			        <input type="hidden" id="TICKNESS_RANGE" name="TICKNESS_RANGE"  />      
			        <input type="hidden" id="MRG_WIP_ENTITY_NAME" name="MRG_WIP_ENTITY_NAME"  value="<%= MRG_WIP_ENTITY_NAME%>"  />      
 
        <div class="wrapPaper">
			<div class="revision-wrapper">
            	<div class="img-shadow paper">
            		<img id="bgImage" src="" alt="image">
            	</div>
            	<div id='subImage' ></div>
            	<div id="viewer" class="data-area"></div>
			</div>

            <div class="btn-wrapper" style="display:none;">
            	<div class="row">
                   	<div class="col-xs-7">
               			<button id="my-button1" type="button" class="btn btn-success pull-left m-r-xs"><i class="fa fa-sticky-note-o"></i> 원본보기</button>
               			<button id="my-button2" type="button" class="btn btn-danger pull-left m-r-xs" ><i class="fa fa-exclamation-triangle"></i><span id = "reportNm"></span></button>
                        <button id="my-button7" type="button" class="btn btn-danger pull-left m-r-xs" >품질이상보고서 생성</button>
                        <button id="my-button3" type="button" class="btn btn-default pull-left">분할양식추가</button>
            		</div>
            		<div class="col-xs-5">
               			<button  id="my-button4" type="button" class="btn btn-primary pull-right m-r-xs"><i class="fa fa-edit"></i> 내용저장</button>
               			<button id="my-button5" type="button"  class="btn btn-default pull-right m-r-xs disabled" >ERP전송</button>
               			<!--  <button type="button" class="btn btn-default pull-right m-r-xs" onclick="javascipt:cMdata();">ERPTEST</button>-->
               			<button   id="my-button6"  type="button" class="btn btn-default pull-right m-r-xs" onclick="javascipt:deleteData();">삭제</button>
            		</div>
            	</div>     
			</div>         
        </div>
 	<div id='pop_up_banner'   class="modal-dialog modal-lg" style="width:1031px">
		<div id ="content" ></div>
	</div>           
</form>		
</body>
</html>