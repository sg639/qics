<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 2 - 타공정 작업내역 보기(팝업)</title>
<base target="_self" />

<script type="text/javascript">
var groupMemberMap= new HashMap();
var userValueMap= new HashMap();
var memberValueMap= new HashMap();
	$(function() {


	});
	$(document).ready(function() {		
		document.popUpForm.IN_LINE.value = document.mySheetForm.OTHER_LINE.value;
		document.popUpForm.POC_NO01.value  = document.mySheetForm.POC_NO01.value;
		var result = ajaxCall("/Other.do?cmd=otherInfo", $("#popUpForm").serialize(), false);
	//	alert(JSON.stringify(result));
		createSelectBox(result);
	});
	//작업 select box 생성
	function createSelectBox(result){
		var str ='';
		for(var i = 0; i<result.DATA.length; i++){
			if(i==0){
				document.popUpForm.SEQ_Q100.value = result.DATA[i].SEQ_Q100;
				document.popUpForm.SEQ_T300.value  = result.DATA[i].SEQ_T300;
				document.popUpForm.FORM_SEQ.value  = result.DATA[i].FORM_SEQ;
			}
			str =str+"<option value='"+result.DATA[i].SEQ_Q100+'_'+result.DATA[i].SEQ_T300+'_'+result.DATA[i].FORM_SEQ+"'>"+result.DATA[i].POC_NO+"&frasl;"+result.DATA[i].IN_LINE+"&frasl;"+result.DATA[i].WORK_DATE+" </option>";
		}
		$("#otherSelect").append(str);
		getBGImage();
	}

	function getBGImage(){
		$("#bgImage").attr('src','');
		$("#viewer").html('');
		$("#orgBg").removeClass('btn pull-left').addClass('btn btn-success  pull-left');
		$("#viewUser").removeClass('btn btn-success  pull-left').addClass('btn pull-left');
		var result = ajaxCall("/Other.do?cmd=otherBGInfo", $("#popUpForm").serialize(), false);
		setBG(result.DATA);
	}
	function setBG(viewDataInfo){
		
		for(var i = 0; i<viewDataInfo.length; i++){ 
			if($("WEB_DATA_YN").val()=="Y"){
				var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].BG_NAME;
				$("#bgImage").attr('src',url);
			}else{
				var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].P_BG;
				$("#bgImage").attr('src',url);
			}
		}
		
	}
	function setLayOut(){
		$("#orgBg").removeClass('btn btn-success  pull-left').addClass('btn pull-left');
		$("#viewUser").removeClass('btn pull-left').addClass('btn btn-success  pull-left');
		var result = ajaxCall("/View.do?cmd=viewValueInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
		setUserValue(result.DATA);
	}
	function setUserValue(viewDataInfo){
		for(var i = 0; i<viewDataInfo.length; i++){
			userValueMap.put(viewDataInfo[i].FIELD_NAME,viewDataInfo[i].USER_INPUT_VALUE);
		}
		var result = ajaxCall("/View.do?cmd=viewPadInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
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
				$("#popUpForm").append(str)
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
				 if(viewDataInfo[i].FIELD_DATATYPE == "text" || (viewDataInfo[i].FIELD_DATATYPE == "group" && viewDataInfo[i].FIELD_GROUPTYPE == "text")){
					 if(viewDataInfo[i].FIELD_DYNAMIC=="true"){
						 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT))+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
					    
						 //C_POC_NO1 의 값이 존재 한다면 C_POC_NO1로 없으면 C_POC_NO1_POD로
						 if(viewDataInfo[i].FIELD_NAME == "C_POC_NO01_POD" || viewDataInfo[i].FIELD_NAME == "C_POC_NO02_POD" || viewDataInfo[i].FIELD_NAME == "C_POC_NO03_POD"
					    	 || viewDataInfo[i].FIELD_NAME == "C_THMAX01_POD" || viewDataInfo[i].FIELD_NAME == "C_THMAX02_POD" || viewDataInfo[i].FIELD_NAME == "C_THMIN01_POD"
					    		 || viewDataInfo[i].FIELD_NAME == "C_THMIN02_POD"){
					    	 dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+viewDataInfo[i].FIELD_POD+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					     }else{
					    	 if(viewDataInfo[i].FIELD_NAME == "C_FRM_DPT" || viewDataInfo[i].FIELD_NAME == "C_FRM_PRN" || viewDataInfo[i].FIELD_NAME == "C_FRM_ID" || viewDataInfo[i].FIELD_NAME == "C_FRM_NO" || viewDataInfo[i].FIELD_NAME == " C_SEQ"){
					    		 
					    	 }else if(viewDataInfo[i].FIELD_NAME == "C_STAMP01_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP02_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP03_POD" || viewDataInfo[i].FIELD_NAME == "C_STAMP04_POD"){
					    		if(viewDataInfo[i].FIELD_POD == "" || viewDataInfo[i].FIELD_POD == null){
					    		}else{
					    			dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn-inform" style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD.split("\n").join("<br>")+'</span>';
					    		}
					    		 
					    	 }else{
					    		 dataDiv +='<span id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" style="'+viewDataInfo[i].FIELD_STYLE+';position:absolute;top:7px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">'+viewDataInfo[i].FIELD_POD+'</span>';
					    	 }
					     }
						 //dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" disabled="disabled" value="'+viewDataInfo[i].FIELD_POD+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
					     
						 dataDiv +='</div>';
					 }else{
						
						 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT))+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
					     dataDiv +='<input type="text" id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'"  value="'+userValue+'" style="height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;"/>';
						 dataDiv +='</div>';
					 }

				 }else{
					 if(groupMemberMap.containsKey(viewDataInfo[i].FIELD_NAME)){
						 dataDiv +='<div id="div_'+viewDataInfo[i].FIELD_NAME+'" style="position:absolute;left:'+(parseInt(viewDataInfo[i].FIELD_LEFT) )+'px;top:'+(parseInt(viewDataInfo[i].FIELD_TOP))+'px;height:'+viewDataInfo[i].FIELD_HEIGHT+'px;width:'+viewDataInfo[i].FIELD_WIDTH+'px;">';
						 var tempStr = viewDataInfo[i].FIELD_NAME.substring(0,viewDataInfo[i].FIELD_NAME.lastIndexOf("_"));
						 
						 var tempVal = '';
						 var memVal = '';
						 if(userValueMap.containsKey(tempStr)){
							 tempVal = userValueMap.get(tempStr);
						 }
						 if(memberValueMap.containsKey(viewDataInfo[i].FIELD_NAME)){
							 memVal = memberValueMap.get(viewDataInfo[i].FIELD_NAME);
						 }
						if(tempVal == memVal && memVal !=''){
							dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs active"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
						}else{
							dataDiv +='<button type="button"  id="'+viewDataInfo[i].FIELD_NAME+'" name="'+viewDataInfo[i].FIELD_NAME+'" class="btn btn-option btn-xxs"  style="width:'+viewDataInfo[i].FIELD_WIDTH+'px;" onclick="javascript:changeCss(\''+viewDataInfo[i].FIELD_NAME+'\')";>'+viewDataInfo[i].ATTRIBUTE1+'</button>';
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
		//backGroudImage();
	}
	function backGroudImage(){
		if($("#WEB_DATA_YN").val() == 'Y'){
			
		}else{
			var str ="<div id='subBg' style='border:2px solid #c5c5c5;display:block; position:absolute  ;width:177px;height:173px; left:455px; top:495px;  background-image:url(/common/img/20160128_135437_947[1]_THUMBNAIL.png) ; background-repeat: no-repeat;background-position:-455px -495px;' >"	;
			$("#subImage").html(str);
		}
		
	}
	//image원본(결과)
	
	//결과 layout
	
	//image배경();
	function dataChange(){
		var tempStr = $("#otherSelect").val().split("_");
		$("#bgImage").attr('src','');
		$("#viewer").html('');
		groupMemberMap= new HashMap();
		userValueMap= new HashMap();
		memberValueMap= new HashMap();
		document.popUpForm.SEQ_Q100.value = tempStr[0];
		document.popUpForm.SEQ_T300.value  =tempStr[1];
		document.popUpForm.FORM_SEQ.value  = tempStr[2];
		getBGImage();
		
	}
</script>

</head>
<body>
<div class="wrapper">
<form id="popUpForm" name="popUpForm" class="form-horizontal">
   	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100"  />
   	<input type="hidden" id="SEQ_T300" name="SEQ_T300"   />
   	<input type="hidden" id="FORM_SEQ" name="FORM_SEQ" />
   	<input type="hidden" id="IN_LINE" name="IN_LINE" />
   	<input type="hidden" id="POC_NO01" name="POC_NO01" />
   	<input type="hidden" id="FORM_SEQ" name="FORM_SEQ"  />
    <input type="hidden" id="PAGE_ORDER" name="PAGE_ORDER"   />
    <input type="hidden" id="WEB_DATA_YN" name="WEB_DATA_YN"  />    
</form>
<!-- Popup Area -->
<div class="modal-dialog modal-xl">
  <div class="modal-content"> 
   
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>타공정 작업내역 보기</label>
        <div class="form-group pull-right m-b-none">
            <div class="col-sm-12">
            <select  id="otherSelect"  class="form-control input-sm m-r" onchange="javascript:dataChange();">
            </select> 
        	</div>
        </div>
      </h3>
      <span></span>
    </div>
    <!-- ./modal-header -->
    
    <div class="modal-body">
		<div class="modal-paper">
           	<div class="img-shadow paper">
           		<img id="bgImage" src="" alt="image">
           	</div>
           	<div id='subImage' ></div>
           	<div id="viewer" class="data-area"></div>
		</div>
    </div>
    <!-- ./modal-body -->  
      
    <div class="modal-footer">
    	<div class="row">
            <div class="col-xs-7">
           		<button id="orgBg" name="orgBg"  type="button" class="btn btn-success  pull-left" onclick="javascript:getBGImage();">원본양식</button>
                <button id="viewUser" name="viewUser" type="button" class="btn pull-left" onclick="javascript:setLayOut();">보정데이터 보기</button>
           	</div>
           	<div class="col-xs-5">
           		<button type="button" class="btn pull-right" onclick="javascript:closePopUp('pop_up_banner');">닫기</button>
           		<button type="button" class="btn btn-primary pull-right m-r-xs"><i class="fa fa-print"></i> 인쇄하기</button>
           	</div>
		</div>    
    </div>
    <!-- ./modal-footer -->
    
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->

</div>

</body>
</html>