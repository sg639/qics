<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<%
String SEQ_Q100 = request.getParameter("SEQ_Q100") == null ? "" : request.getParameter("SEQ_Q100");
String SEQ_T300 = request.getParameter("SEQ_T300") == null ? "" : request.getParameter("SEQ_T300");
String WEB_DATA_YN = request.getParameter("WEB_DATA_YN") == null ? "" : request.getParameter("WEB_DATA_YN");
String IN_LINE = request.getParameter("IN_LINE") == null ? "" : request.getParameter("IN_LINE");
String FORM_TYPE = request.getParameter("FORM_TYPE") == null ? "" : request.getParameter("FORM_TYPE");
String E_STATUS = request.getParameter("E_STATUS") == null ? "" : request.getParameter("E_STATUS");
String STATUS_CODE = request.getParameter("STATUS_CODE") == null ? "" : request.getParameter("STATUS_CODE");
String FRM_DAT_ID = request.getParameter("FRM_DAT_ID") == null ? "" : request.getParameter("FRM_DAT_ID");
String ERP_UPLOAD_YN = request.getParameter("ERP_UPLOAD_YN") == null ? "" : request.getParameter("ERP_UPLOAD_YN");


String STATUS_NM = "";
String STATUS_CSS = "btn-default";
if("R".equals(STATUS_CODE)) {
	STATUS_NM = "출력대기";
	STATUS_CSS = "btn-default";
} else if("P".equals(STATUS_CODE)) {
	STATUS_NM = "검사대기";
	STATUS_CSS = "btn-default";
} else if("S".equals(STATUS_CODE)) {
	if("E".equals(ERP_UPLOAD_YN)) {
		STATUS_NM = "ERP 전송실패";
		STATUS_CSS = "btn-danger";
	} else {
		STATUS_NM = "검사중";
		STATUS_CSS = "btn-info";
	}
	
} else if("C".equals(STATUS_CODE)) {
	STATUS_NM = "ERP 전송완료";
	STATUS_CSS = "btn-success";
} else {
}

%>

<!DOCTYPE html>
<html id="wrapSub">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
  <title>HYUNDAI BNG STEEL :: 보정화면</title>
 
 <%@ include file="/WEB-INF/jsp/common/include/jqueryScript.jsp"%>
 <style type="text/css">
 .btn-wrapper {padding:15px 0px 0px 0px;}
 </style>
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
var CchkUserMap = new HashMap();
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
		$('#my-button3').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		parent.userDivide(e);
	});	
	$('#my-button7').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		 
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
	/*
	$('#my-button4').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		dataSave();
	});
	*/
	
	$('#my-button2').bind('click', function(e) {
		//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
		if($("#E_STATUS").val() =="C"){
			parent.userReportBgInfo(e);
		 }else{
			 alert("작성중입니다.");
		 }
	});	

	if($("#WEB_DATA_YN").val() =="Y"){
		if($("#FRM_DAT_ID").val() !=""){
			$('#my-button1').show();
			$('#my-button1').text("직접입력건");
			
			$('#orgBg').text("원본보기");	
			$('#orgBg').show();
			
			
			$('#viewUser').hide("");
			$('#printBtn').show();
		} else {
			$('#my-button1').show();
			$('#my-button1').text("직접입력건");
			
			$('#orgBg').hide();
			$('#viewUser').text("보정데이터보기 (직접입력건)");	
			$('#printBtn').hide();
		}
		
		
	}else{
		$('#my-button1').show();
		$('#my-button1').bind('click', function(e) {
			//defaultFn( '/View.do?cmd=orginViewPopup',e); //긴급작업
			parent.userViewInfo(e);
		});	
	
		$('#orgBg').bind('click', function(e) {
			getPSImage();
			
			$('#printBtn').show();
		})
		
		$('#viewUser').bind('click', function(e) {
			setLayOut();
			$('#printBtn').hide();
		})
		
		$('#printBtn').show();
	}
	if($("#FORM_TYPE").val() =="M"){
		 var reportNm="";
		 if($("#E_STATUS").val() =="R" || $("#E_STATUS").val() =="S" || $("#E_STATUS").val() =="C"){
			
			 if($("#E_STATUS").val() =="R"){
				 reportNm ='품질이상보고서 인쇄요청상태';
			 }else if ($("#E_STATUS").val() =="S"){
				 reportNm ='품질이상보고서 작성대기중';
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
	
	var result =ajaxCall("/Code.do?cmd=codeList", $("#frameForm").serialize(), false);
	setCodeMap(result.DATA);

	try {
		//alert(top.document.getElementById("extraViewerTitle"));
		//top.document.getElementById("extraViewerTitle").innerHTML = "품질검사결과표 상세보기 ( "+ top.document.getElementById("POC_NO01").value + "-0000 )";
	} catch (e) {
		// TODO: handle exception
	}
});

function setCodeMap(codeDataInfo){
	for(var i = 0; i<codeDataInfo.length; i++){ 
	          
		if(codeDataInfo[i].CODE_GUBUN =="UFS_GRADE"){
			CppMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
			ClvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
			//가능등급은 D1, E1만 입력 CalvMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
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
			CdefCdMap.put(codeDataInfo[i].CODE,codeDataInfo[i].ATTRIBUTE1);
		}
		
	}
    //가능등급은 D1, E1만 입력
	CalvMap.put("D1","D1");
	CalvMap.put("E1","E1");
	
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
	
	//alert('WEB_DATA_YN : ' + $("#WEB_DATA_YN").val());
	//alert('FRM_DAT_ID : ' + $("#FRM_DAT_ID").val());
	//var result1 = ajaxCall("/View.do?cmd=viewBGInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색
	//setUserValue(result1.DATA);
	
	
	
	if($("#WEB_DATA_YN").val() =="Y"){
		if($("#FRM_DAT_ID").val() !=""){
			if($("#SEQ_T300").val() < 1){
				getPSImage();
			} else {
				setLayOut();
			}			
		} else {
			setLayOut();
		}
	}else{
		if($("#SEQ_T300").val() < 1){
			getPSImage();
		} else {
			setLayOut();
		}		
	}
	
	/*
	if($("#WEB_DATA_YN").val() =="Y"){
		if($("#FRM_DAT_ID").val() !=""){
			getPSImage();
		} else {
			setLayOut();
		}
	}else{
		getPSImage();
	}
	*/
	
	//setLayOut();
}

function getPSImage(){
	$("#bgImage").attr('src','');
	$("#viewer").html('');
	$("#orgBg").removeClass('btn pull-left').addClass('btn btn-success  pull-left');
	$("#viewUser").removeClass('btn btn-success  pull-left').addClass('btn pull-left');
	var result = ajaxCall("/Other.do?cmd=otherBGInfo", $("#frameForm").serialize(), false);
	setPS(result.DATA);
}
function setPS(viewDataInfo){
	for(var i = 0; i<viewDataInfo.length; i++){ 
		var  url = "/ImagePS.do?BG_PATH="+viewDataInfo[i].P_BG_PATH.split("\\").join("\\\\")+"&P_BG="+viewDataInfo[i].P_BG;
		psImageName=viewDataInfo[i].P_BG;
		psImagePath=viewDataInfo[i].P_BG_PATH.split("\\").join("\\\\");

		$("#bgImage").attr('src',url);
		$("#bgImage").css({"opacity":"1.0","filter":"alpha(opacity=100)"});
	}
	parent.hideGuidTxt();
}
function setLayOut(){
	$("#viewer").html('');
	//getBGImage();
	getPSImage();
	groupMemberMap= new HashMap();
	userValueMap= new HashMap();
	memberValueMap= new HashMap();
	cMCount=0;
	cMspCount = 0;
	cRollStop=0;
	$("#orgBg").removeClass('btn btn-success  pull-left').addClass('btn pull-left');
	$("#viewUser").removeClass('btn pull-left').addClass('btn btn-success  pull-left');
	var result = ajaxCall("/View.do?cmd=viewValueInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색
	setUserValue(result.DATA);
}
/////////////////////////////////////////////////////////////////////////////
function getBGImage(){
	$("#bgImage").attr('src','');
	$("#viewer").html('');
	var result = ajaxCall("/Other.do?cmd=otherBGInfo", $("#frameForm").serialize(), false);
	setBG(result.DATA);
}

function setBG(viewDataInfo){
	
	for(var i = 0; i<viewDataInfo.length; i++){ 
		var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].BG_NAME;
		 
		$("#bgImage").attr('src',url);
		$("#bgImage").css({"opacity":"0.7","filter":"alpha(opacity=70)"});
	}
	
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
							 if(userValueMap.containsKey("C_THMAX")){
								 userValue = userValueMap.get("C_THMAX");
								 var tempValue = userValue.split(".");
								 if(tempValue.length >0){
									 maxValue1 = tempValue[0];
									 maxValue2 = tempValue[1];
								 }
								 
							 }		
							 
							 if(viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" ){
								 if(userValueMap.containsKey("C_THMAX01_POD")){
									 maxValue1 = userValueMap.get("C_THMAX01_POD")
								 }else{
									 maxValue1 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+maxValue1+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }else if(viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD"){
								 if(userValueMap.containsKey("C_THMAX02_POD")){
									 maxValue2 = userValueMap.get("C_THMAX02_POD")
								 }else{
									 maxValue2 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+maxValue2+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }
							 
							 
						 }else if(viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD"	 || viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
							 var minValue1="";
							 var minValue2="";
							 if(userValueMap.containsKey("C_THMIN")){
								 userValue = userValueMap.get("C_THMIN");
								 var tempValue = userValue.split(".");
								 if(tempValue.length >0){
									 minValue1 = tempValue[0];
									 minValue2 = tempValue[1];
								 }
							 }							 
							 if(viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD" ){
								 if(userValueMap.containsKey("C_THMIN01_POD")){
									 minValue1 = userValueMap.get("C_THMIN01_POD")
								 }else{
									 minValue1 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+minValue1+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }else if(viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
								 if(userValueMap.containsKey("C_THMIN02_POD")){
									 minValue2 = userValueMap.get("C_THMIN02_POD")
								 }else{
									 minValue2 = viewDataInfo[i].FIELD_POD;
								 }
								 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"  value="'+minValue2+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
							 }
						 }else{
							 var tempValue = viewDataInfo[i].FIELD_POD;
							 
							 if(viewDataInfo[i].FIELD_NAME == "C_POC_NO01_POD"){
								 
								 if(userValueMap.containsKey("C_POC_NO01_POD")){
									 tempValue = userValueMap.get("C_POC_NO01_POD")
								 }
							 }
 							if(viewDataInfo[i].FIELD_NAME == "C_POC_NO02_POD"){
								 
								 if(userValueMap.containsKey("C_POC_NO02_POD")){
									 tempValue = userValueMap.get("C_POC_NO02_POD")
								 }
							 }
 							if(viewDataInfo[i].FIELD_NAME == "C_POC_NO03_POD"){
								 
								 if(userValueMap.containsKey("C_POC_NO03_POD")){
									 tempValue = userValueMap.get("C_POC_NO03_POD")
								 }
							 }
							
							 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="form-control h'+viewDataInfo[i].FIELD_HEIGHT+'px"   value="'+tempValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
						 }
				    	 
				     }else{
				    	 /*****************************************
				    	 * 임성현 수정부 : 보정화면에서 직접 입력한 description 내역을 같이 조회 하기위해
				    	 *           원본이미지를 바탕으로 두고 각 데이터를 바인딩 할때 규격정보가 중접 되 보여지는
				    	 *           현상이 발생 하여 규격정보를 바인딩하지 않고 바탕이미지에 있는 내역을 출력하도록 변경
				    	 *****************************************/
				    	 /*
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
				    			 document.frameForm.TARGET_THICKNESS.value=viewDataInfo[i].FIELD_POD;
				    		 }
				    		 if(viewDataInfo[i].FIELD_NAME =="C_STD02"){
				    			 document.frameForm.LAST_WIDTH.value=viewDataInfo[i].FIELD_POD;
				    		 }
				    		 if(viewDataInfo[i].FIELD_NAME.indexOf("C_APP") > -1 || viewDataInfo[i].FIELD_NAME =="C_SEQ" ){
				    			 dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"   style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD+'</span>';
				    		 }else if(viewDataInfo[i].FIELD_NAME =="C_CHK_DT"){
				    			dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;font-size:12pt;letter-spacing:-0.02em;" />';
				    		 }else if(viewDataInfo[i].FIELD_NAME =="C_STD01" || viewDataInfo[i].FIELD_NAME =="C_STD02" || viewDataInfo[i].FIELD_NAME =="C_STP"){
				    			dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onchange="javascript:goChangeKEYField(\''+viewDataInfo[i].FIELD_NAME+'\');"/>';
				    		 }else{
					    		dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"   style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;top: 25%;">'+viewDataInfo[i].FIELD_POD+'</span>';
				    		 }
				    		
				    	 }*/
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
									tempStyle="text-danger";
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
						//dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs active"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
						dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs active"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].ATTRIBUTE1+'</button>';
					}else{
						//dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
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

	//parent.goOtherLine();
		parent.hideGuidTxt();
	changeCmst();
	
	var chNo= $("#C_CHK_NO").val();
	var userNm = CchkUserMap.get(chNo);
	 $("#C_CHKER").val(userNm);
	 
	 $("#frameForm input[type=text]").css({"ime-mode":"disabled","text-transform":"uppercase","background-color":"#FFFFFF"});
	 $("#frameForm input[type=text]").attr("readonly",true);
	 //$("#C_CHKER").css("ime-mode","active");
}

function changeCss(id){
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
	
	//var result = ajaxCall("/Save.do?cmd=dataSave", $("#frameForm").serialize(), false);  //작업내역 상세검색
	//alert(result.Message);
}
function goChageData(id){
	var value = $("#"+id).val();
	var dataMin = $("#C_THMIN01_POD").val()+"."+$("#C_THMIN02_POD").val();
	var dataMax = $("#C_THMAX01_POD").val()+"."+$("#C_THMAX02_POD").val();
	if(isNaN(value)){
		 
	}else{
	//	alert(id+" : " +parseFloat(value)+" : " +parseFloat(dataMin)+" : " +parseFloat(dataMax));
		if(parseFloat(dataMin) > parseFloat(value) ){
			$("#"+id).addClass("text-danger");
		}else if (parseFloat(dataMax) < parseFloat(value)){
			$("#"+id).addClass("text-danger");
		}else{
			$("#"+id).removeClass("text-danger");
		}
	}
}
function changeCmst(){
	var dataMin = $("#C_THMIN01_POD").val()+"."+$("#C_THMIN02_POD").val();
	var dataMax = $("#C_THMAX01_POD").val()+"."+$("#C_THMAX02_POD").val();
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
                                                                                                                                                                                     
function checkValue(value){
	var tmpStr = "";
	var isCheck=false;
	if(value.length < 2){
		
	}else{
		tmpStr=value.substr(1,1).toUpperCase();
		if(tmpStr == "A" || tmpStr == "B" || tmpStr == "C" || tmpStr == "D" || tmpStr == "K"){
			isCheck = true;
		}
	}
	 
 	return isCheck;
}

function goErrorCheck(id){
 
	var value = $("#"+id).val();
 
	var isCheck = false;
	if(id == "C_PP"){
		if(CppMap.containsKey(value)){
			isCheck = true;
		}
	}else if(id == "C_LV"){
		if(ClvMap.containsKey(value)){
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
			isCheck = true;
		}
	}else if(id.indexOf("C_SH") > -1){
		if(CshMap.containsKey(value)){
			
			isCheck = true;
		}
	}else if(id.indexOf("C_RH") > -1){
		if(CrhMap.containsKey(value)){
			isCheck = true;
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
	 
	if(isCheck){
		$("#"+id).removeClass("text-danger");
	}else{
		$("#"+id).addClass("text-danger");
	}
}
function deleteData(){
	//parent.deleteInfo($("#SEQ_Q100").val(),$("#SEQ_T300").val(),$("#IN_LINE").val());
}

function ImagePrint(){
	
	try{
		top.window.opener.top.goPrintByPopUp(document.frameForm.SEQ_T300.value);
		alert("인쇄요청을 하였습니다.");
	} catch(e) {
		var url = "/QICSImageDownload.do?SEQ_T300="+document.frameForm.SEQ_T300.value;
		top.window.frames["mainPenWork"].location.href=url;
	}
	/*
	var url = "/QICSImageDownload.do?SEQ_T300="+document.frameForm.SEQ_T300.value;
	top.window.frames["mainPenWork"].location.href=url;
	*/
	
}
</script>
<body>
        <form  id="frameForm" name="frameForm" class="form-horizontal">
                	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100"  value="<%=SEQ_Q100%>" />
                	<input type="hidden" id="SEQ_T300" name="SEQ_T300"  value="<%=SEQ_T300%>" />
                	<input type="hidden" id="IN_LINE" name="IN_LINE"  value="<%=IN_LINE%>" />
                	<input type="hidden" id="WEB_DATA_YN" name="WEB_DATA_YN"  value="<%=WEB_DATA_YN%>" />
                	<input type="hidden" id="FORM_TYPE" name="FORM_TYPE"  value="<%=FORM_TYPE%>" />
                	<input type="hidden" id="E_STATUS" name="E_STATUS"  value="<%=E_STATUS%>" />
                	<input type="hidden" id="FRM_DAT_ID" name="FRM_DAT_ID"  value="<%=FRM_DAT_ID%>" />
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
			        <input type="hidden" id="C_BEST_VALUE" name="C_BEST_VALUE"  />      
			        <input type="hidden" id="C_HOOP_VALUE" name="C_HOOP_VALUE"  />        
			    <input type="hidden" id="PAGE_ORDER" name="PAGE_ORDER" value="1"   />
			    
        <div class="wrapPaper">
			<div class="revision-wrapper">
            	<div class="img-shadow paper">
            		<img id="bgImage" src="" alt="image" style="width:991px;height:700px;opacity:0.8;​filter:alpha(opacity=80);">
            	</div>
            	<div id='subImage'></div>
            	<div id="viewer" class="data-area" style="width:991px; height:700px;"></div>
			</div>

            <div class="btn-wrapper" style="display:none;">
            	<div class="row">
                   	<div class="col-xs-3">
                   		<!--  
               			<button id="my-button1" type="button" class="btn btn-success pull-left m-r-xs"><i class="fa fa-sticky-note-o"></i> 원본보기</button>
               			<button id="my-button2" type="button" class="btn btn-danger pull-left m-r-xs" ><i class="fa fa-exclamation-triangle"></i><span id = "reportNm"></span></button>
                        <button id="my-button7" type="button" class="btn btn-danger pull-left m-r-xs" >품질이상보고서 생성</button>
                        <button id="my-button3" type="button" class="btn btn-default pull-left">분할양식추가</button>
               			-->
               			<button id="orgBg" name="orgBg"  type="button" class="btn btn-success  pull-left disabled">원본보기</button>
                		<button id="viewUser" name="viewUser" type="button" class="btn pull-left disabled">보정데이터 보기</button>               			
            		</div>
            		<div class="col-xs-4">
               			<button id="my-button2" type="button" class="btn btn-danger pull-left m-r-xs" ><i class="fa fa-exclamation-triangle"></i><span id = "reportNm"></span></button>               			
            		</div>
            		<div class="col-xs-5">
               			<!--  
               			<button  id="my-button4" type="button" class="btn btn-primary pull-right m-r-xs"><i class="fa fa-edit"></i> 내용저장</button>
               			<button type="button" class="btn btn-default pull-right m-r-xs">ERP전송</button>
               			<button type="button" class="btn btn-default pull-right m-r-xs" onclick="javascipt:deleteData();">삭제</button>
               			-->
               			<button id="statusText" type="button" class="btn <%=STATUS_CSS %> pull-right m-r-xs disabled"><%= STATUS_NM%></button>
               			<button id="printBtn" type="button" class="btn btn-primary pull-right m-r-xs disabled" style="display: block;" onclick="javascript:ImagePrint();"><i class="fa fa-print"></i> 인쇄하기</button>
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