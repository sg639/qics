<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
.ui-popup-overlay {position:fixed;top:0;left:0;width:100%;height:100%;background:#aaaaaa;filter:alpha(opacity=50);opacity:0.5}
.order_create_area {position:absolute;left:35%;top:30%;width:360px;height:180px;border:3px solid #c0c0c0;background:#FFF url(/common/images/popup/bg_creating.gif) 50% 35% no-repeat; padding:30px 20px 10px;}
.order_create_area .txt{display:block;padding-top:100px;text-align:center;color:#666;font-size:18px;line-height:26px;font-weight:bold}
</style>
  <title>품질검사정보수집시스템(QICS)-자료보기 :: ${param.POC_NO} :: ${param.ORIGIN_TYPE} </title>

<%@ include file="/WEB-INF/jsp/common/include/jqueryScript_linkservice.jsp"%>

<script type="text/javascript">
var penWorkData=null;
var iLineMap=new HashMap();
var iFactMap=new HashMap();
var iOperMap=new HashMap();
var fCodeMap= new HashMap();
var fTypeMap = new HashMap();
var slideIndex = 0;
var default_line="";
var first_line = "";
var resetLineFlag = false;
$(document).ready(function() {	
	if(screen.height>1024) {
			
	}
	window.resizeTo(1100, screen.height);
	
	if('${LINE}'!='') {
		default_line = '${LINE}';
		$("#IN_LINE").val(default_line);
	} 
	
	window.frames["basePenWork"].location.href="about:blank";
	
	 var result = ajaxCall("/Process.do?cmd=processList", $("#mySheetForm").serialize(), false); //공통코드셋팅
	 initPage(result);
	 //createSelect(result);
		 
	//작업내역상세검색팝업
	$('#workListSearchBtn').bind('click', function(e) {
		defaultFn( '/Work.do?cmd=workPopup',e);
	});	
	
		window.focus();
});
//공통정보조히 생성
function initPage(result){
    var str="";
    var mStr="";
    var fStr="";
	if(result.DATA.length > 0 ){
		iLineMap=new HashMap();
		iFactMap=new HashMap();
		iOperMap=new HashMap();
		fCodeMap= new HashMap();
		fTypeMap = new HashMap();
		for(var i = 0; i<result.DATA.length; i++){
			
			iLineMap.put(result.DATA[i].CODE,result.DATA[i].CODE);
			iFactMap.put(result.DATA[i].CODE,result.DATA[i].ATTRIBUTE2);
			iOperMap.put(result.DATA[i].CODE,result.DATA[i].ATTRIBUTE1);
		//	alert("result.DATA["+i+"].CODE = "+result.DATA[i].CODE+" result.DATA["+i+"].FORM_CODE = "+result.DATA[i].FORM_CODE);
			fCodeMap.put(result.DATA[i].CODE,result.DATA[i].FORM_CODE);
			fTypeMap.put(result.DATA[i].CODE,result.DATA[i].FORM_TYPE);

		}
	}else{
		//str = "<option value=''>없음</option>";
	}

	if( otherList() ) {
		$("#lodingBG").show().fadeIn('fast');
		$("#lodingTxtDiv").show().fadeIn('fast');
		
		if(resetLineFlag) {	
			$("#IN_LINE").val(first_line);
			
			changeLine(first_line,'','','','');	
		} else {
			changeLine(default_line,'','','','');	
		}
	} else {
		$("#sheet_list").html("");
		//var help_txt ='<div><h3 id="sheet_nav_1" class="slide-ON">'+$("#IN_LINE").val()+' 공정에서 '+$("#POC_NO").val()+' 에 대한 검사결과가 없습니다.<br>POC번호 or 공정을 확인해주세요</h3></div>';
		var help_txt ='<div><h3 id="sheet_nav_1" class="slide-ON"> 조회된 품질검사결과표가 없습니다. 조회조건을 다시 확인해주세요</h3></div>';

		$("#sheet_list").html(help_txt);
		
		window.frames["basePenWork"].location.href="about:blank";
		
		$("#lodingBG").hide();
		$("#lodingTxtDiv").hide();
	}
	
}
//팝업공통모듈
function defaultFn(url,e){
	 if(!e) e = window.event;
     if (e.preventDefault){
         // Firefox, Chrome
         e.preventDefault();
     }else{
         // Internet Explorer
         e.returnValue = false;
     }
	  //e.preventDefault();

	    $('#content').html('');
		$('#pop_up_banner').bPopup({
			modalClose :false,
			content:'ajax', //'ajax', 'iframe' or 'image'
			contentContainer:'#content',
			follow: [false, false], //x, y
			position: [(($(document).width()/2)-540<0)?0:($(document).width()/2)-540, 20] ,//x, y
	        loadUrl: url//Uses jQuery.load()
		});
}
//종료
function closePopUp(closeId){
	$('#content').html('');
	$('#'+closeId).bPopup().close();
}


//타공정조회
function otherList(){
	first_line = "";
	
	var result = ajaxCall("/OtherLine.do", $("#mySheetForm").serialize(), false); //타공정 조회
	return createOther(result);
}

function createOther(result){
	var rtn = false;
	resetLineFlag = true;
	var otherListStr="";
	$('#otherList').html('');
	if(result && result.DATA && result.DATA.length > 0 ){
		for(var i = 0; i<result.DATA.length; i++){
			
			if(default_line=="" && i==0) {
				default_line =result.DATA[i].LINE_CODE;
				$('#IN_LINE').val(default_line); 
			} 

			if(i==0) {
				first_line = result.DATA[i].LINE_CODE;
			}

			
			if( result.DATA[i].LINE_CODE==$('#IN_LINE').val() ) { //일단은 같은 라인도 링크활성화.
				resetLineFlag = false;
				if( result.DATA[i].E_STATUS=="" ) {
	            	//str += '<span class="btn btn-outline btn-default disabled" onclick="javascript:otherLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');">'+result.DATA[i].LINE_CODE+'</span>';
	            	otherListStr += '<span id="IN_LINE_'+result.DATA[i].LINE_CODE+'" class="btn btn-outline btn-primary active" onclick="javascript:changeLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');">'+result.DATA[i].LINE_CODE+'</span>';
	            } else {
	            	otherListStr += '<span id="IN_LINE_'+result.DATA[i].LINE_CODE+'" class="btn btn-outline btn-danger active" onclick="javascript:changeLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');"><i class="fa fa-warning"></i> '+result.DATA[i].LINE_CODE+'</span>';
	            }
			} else {
				if( result.DATA[i].E_STATUS=="" ) {
	            	//str += '<span class="btn btn-outline btn-default disabled" onclick="javascript:otherLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');">'+result.DATA[i].LINE_CODE+'</span>';
	            	otherListStr += '<span id="IN_LINE_'+result.DATA[i].LINE_CODE+'" class="btn btn-outline btn-primary" onclick="javascript:changeLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');">'+result.DATA[i].LINE_CODE+'</span>';
	            } else {
	            	otherListStr += '<span id="IN_LINE_'+result.DATA[i].LINE_CODE+'" class="btn btn-outline btn-danger" onclick="javascript:changeLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');"><i class="fa fa-warning"></i> '+result.DATA[i].LINE_CODE+'</span>';
	            }
			}
            
		}
		$('#otherList').html(otherListStr);
		rtn = true;
	} else {
		resetLineFlag = false;
		rtn = false;
	}
	
	return rtn;
}
function changeLine(in_line,poc_no01,m_cnt,f_cnt,r_cnt) {
	$("#lodingBG").show().fadeIn('fast');
	$("#lodingTxtDiv").show().fadeIn('fast');
	
	try {
		$('.slider-nav').slick("unslick");
	} catch (e) {
		// TODO: handle exception
	}
	
	$("#IN_LINE_"+$("#IN_LINE").val()).removeClass('active');
	$("#IN_LINE_"+in_line).addClass('active');
	
	slideIndex=0;
	$("#sheet_list").html("");
	$("#sheet_list").html('<div><h3 id="sheet_nav_1" class="slide-ON">요청된 자료를 조회중입니다<br>잠시만 기다려주세요</h3></div>');
	
	callPenWorkList(in_line);
}

//전자펜 작업목록을 조회
function callPenWorkList(in_line){
	$('#IN_LINE').val(in_line);
	
	var result = ajaxCall("/OtherLineSheet.do", $("#mySheetForm").serialize(), false);
	createPenWork(result);
}

//전자펜 작업 갯수
function createPenWork(result){
	if(result && result.DATA && result.DATA.length > 0 ){
		penWorkData =result.DATA; 
		
		//alert(result.DATA.length);
		var sheet_list = "";
		var init_slideIndex = "";
		var init_IN_LINE = "";
		var init_SEQ_Q100 = "";
		var init_SEQ_T300 = "";
		var init_POC_NO = "";
		
		var selectedPOCNO = "";
		var selectedWD = "";
		
		var errIcon = "";
		var satusIcon = "";
		
		for(var i = 0; i<result.DATA.length; i++){
			if(result.DATA[i].E_STATUS!="") {
				errIcon = '<i class="fa fa-warning" style="color:#FF0000;"></i> ';
			} else {
				errIcon = '';
			}// 품질이상보고서가 등록된 경우..! 표시.
			
			if("R"==result.DATA[i].STATUS_CODE) { //출력대기
           	 satusIcon ='<img src="/common/img/btn_p_1.png" style="position:absolute; width:62px;height:20px;left:20px;top:30px;border:none !important;">&nbsp;&nbsp;&nbsp;';
         	} else if ("P"==result.DATA[i].STATUS_CODE) { //검사대기
         		satusIcon ='<img src="/common/img/btn_p_3.png" style="position:absolute; width:62px;height:20px;left:20px;top:30px;border:none !important;">&nbsp;&nbsp;&nbsp;';
         		
         	} else if ("S"==result.DATA[i].STATUS_CODE) { //검사중
         		satusIcon ='<img src="/common/img/btn_p_4.png" style="position:absolute; width:62px;height:20px;left:20px;top:30px;border:none !important;">&nbsp;&nbsp;&nbsp;';
         		
         	} else if ("C"==result.DATA[i].STATUS_CODE) { //전송완료
         		satusIcon = '<img src="/common/img/btn_p_5.png" style="position:absolute; width:62px;height:20px;left:20px;top:30px;border:none !important;">&nbsp;&nbsp;&nbsp;';
         		
         	} else if ("ERP 전송실패"==result.DATA[i].STATUS_CODE) {
         		satusIcon ='<img src="/common/img/btn_p_6.png" style="position:absolute; width:62px;height:20px;left:20px;top:30px;border:none !important;">&nbsp;&nbsp;&nbsp;';
         	} else {
         		satusIcon =''+result.DATA[i].STATUS_CODE+'';
         		
         	}		              
					 
           satusIcon = "";              
					              
					     
           
			slideIndex++;
			
			if(i==0) {
				//sheet_list += '<div><h3 id="sheet_nav_'+slideIndex+'" class="slide-ON" onclick="javascript:changeSheet(\''+slideIndex+'\',\''+result.DATA[i].IN_LINE+'\',\''+result.DATA[i].SEQ_Q100+'\',\''+result.DATA[i].SEQ_T300+'\');">'+slideIndex+'. '+result.DATA[i].IN_LINE+' '+result.DATA[i].FORM_NAME+'<br>'+errIcon+'( '+result.DATA[i].WORK_DATE+' )</h3></div>\n';
				sheet_list += '<div><h3 id="sheet_nav_'+slideIndex+'" class="slide-ON" onclick="javascript:changeSheet(\''+slideIndex+'\',\''+result.DATA[i].IN_LINE+'\',\''+result.DATA[i].SEQ_Q100+'\',\''+result.DATA[i].SEQ_T300+'\');">'+slideIndex+'. '+result.DATA[i].IN_LINE+', '+result.DATA[i].WORK_DATE+'&nbsp;'+errIcon+'<br>'+satusIcon+'( '+result.DATA[i].POC_NO02+' )</h3></div>\n';
				//changeSheet(slideIndex,result.DATA[i].IN_LINE,result.DATA[i].SEQ_Q100,result.DATA[i].SEQ_T300);
				init_slideIndex = ""+slideIndex;
				init_IN_LINE = result.DATA[i].IN_LINE;
				init_SEQ_Q100 = result.DATA[i].SEQ_Q100;
				init_SEQ_T300 = result.DATA[i].SEQ_T300;
				init_POC_NO = result.DATA[i].POC_NO;
			} else {
				//sheet_list += '<div><h3 id="sheet_nav_'+slideIndex+'" onclick="javascript:changeSheet(\''+slideIndex+'\',\''+result.DATA[i].IN_LINE+'\',\''+result.DATA[i].SEQ_Q100+'\',\''+result.DATA[i].SEQ_T300+'\');">'+slideIndex+'. '+result.DATA[i].IN_LINE+' '+result.DATA[i].FORM_NAME+'<br>'+errIcon+'( '+result.DATA[i].WORK_DATE+' )</h3></div>\n';
				sheet_list += '<div><h3 id="sheet_nav_'+slideIndex+'" onclick="javascript:changeSheet(\''+slideIndex+'\',\''+result.DATA[i].IN_LINE+'\',\''+result.DATA[i].SEQ_Q100+'\',\''+result.DATA[i].SEQ_T300+'\');">'+slideIndex+'. '+result.DATA[i].IN_LINE+', '+result.DATA[i].WORK_DATE+'&nbsp;'+errIcon+'<br>'+satusIcon+'( '+result.DATA[i].POC_NO02+' )</h3></div>\n';
			}
			//alert("::"+result.DATA[i].POC_NO+"::"+$('#POC_NO').val()+"::");
			if(result.DATA[i].POC_NO==$('#POC_NO').val() && selectedPOCNO=="") {
				init_slideIndex = ""+slideIndex;
				init_IN_LINE = result.DATA[i].IN_LINE;
				init_SEQ_Q100 = result.DATA[i].SEQ_Q100;
				init_SEQ_T300 = result.DATA[i].SEQ_T300;
				
				selectedPOCNO=result.DATA[i].POC_NO;
			}
			
			
			if(result.DATA[i].POC_NO==$('#POC_NO').val() && result.DATA[i].WORK_DATE==$('#WD').val() && selectedWD=="") {
				init_slideIndex = ""+slideIndex;
				init_IN_LINE = result.DATA[i].IN_LINE;
				init_SEQ_Q100 = result.DATA[i].SEQ_Q100;
				init_SEQ_T300 = result.DATA[i].SEQ_T300;
				
				selectedPOCNO=result.DATA[i].POC_NO;
				selectedWD=result.DATA[i].WORK_DATE;
			}
		}
		
		$("#sheet_list").html(sheet_list);
		
		//changeSheet(init_slideIndex,init_IN_LINE,init_SEQ_Q100,init_SEQ_T300);
		$(".slide-ON").removeClass("slide-ON");
	 	$("#sheet_nav_"+init_slideIndex).addClass("slide-ON");
	 
		changeSheetDetail(init_SEQ_Q100);
		
		if(slideIndex>30) {

			$('.slider-nav').slick({
				  slidesToShow: 4,
				  slidesToScroll: 4,
				  //asNavFor: '.slider-for',
				  dots: false,
				  centerMode: false,
				  focusOnSelect: true,
				  swipe : false,
				  draggable : false,
				  infinite : false
			});
		} else if(slideIndex>6) {

			$('.slider-nav').slick({
				  slidesToShow: 4,
				  slidesToScroll: 1,
				  //asNavFor: '.slider-for',
				  dots: false,
				  centerMode: false,
				  focusOnSelect: true,
				  swipe : false,
				  draggable : false,
				  infinite : false
			});
		} else if(slideIndex>3) {

			$('.slider-nav').slick({
				  slidesToShow: 4,
				  slidesToScroll: 1,
				  //asNavFor: '.slider-for',
				  dots: false,
				  centerMode: false,
				  focusOnSelect: true,
				  swipe : false,
				  draggable : false,
				  infinite : false
			});
		} else {
			$('.slider-nav').slick({
				  slidesToShow: 4,//slideIndex,
				  slidesToScroll: 1,
				  //asNavFor: '.slider-for',
				  dots: false,
				  centerMode: false,
				  focusOnSelect: true,
				  swipe : false,
				  draggable : false,
				  infinite : false
			});		
		}
		
		if(init_slideIndex>4) {
			$('.slider-nav').slick("slickGoTo", init_slideIndex-1, true);	
		}
		
		//$('.slider-nav').slick("slickSetOption", null, null, true);
	}else{
		$("#sheet_list").html("");
		//var help_txt ='<div><h3 id="sheet_nav_1" class="slide-ON">'+$("#IN_LINE").val()+' 공정에서 '+$("#POC_NO").val()+' 에 대한 검사결과가 없습니다.<br>POC번호 or 공정을 확인해주세요</h3></div>';
		var help_txt ='<div><h3 id="sheet_nav_1" class="slide-ON"> 조회된 품질검사결과표가 없습니다. 조회조건을 다시 확인해주세요</h3></div>';

		$("#sheet_list").html(help_txt);
		
		window.frames["basePenWork"].location.href="about:blank";
	}
	
	$("#lodingBG").hide();
	$("#lodingTxtDiv").hide();
}

function changeSheet(p_slideIndex,p_IN_LINE,p_SEQ_Q100,p_SEQ_T300) {
	$("#lodingBG").show().fadeIn('fast');
	$("#lodingTxtDiv").show().fadeIn('fast');
	
	 $(".slide-ON").removeClass("slide-ON");
	 $("#sheet_nav_"+p_slideIndex).addClass("slide-ON");
	 
	 changeSheetDetail(p_SEQ_Q100);
}
//인쇄요청
function changeSheetDetail(p_SEQ_Q100){
	var li_idx = p_SEQ_Q100;
	
	var podNo1="";
	var podNo2="";
	var podNo3="";
	var inLine="";
	var statusCode ="";
	var formCode=""
	var formName="";
	var seqT300="";
	var webDataYn="";
	var eStatus="";
	var STATUS_NM = "";
	var erpUploadYN = "";
	var frmDatId="";
	
	//$("#formName").html("");
	for(var i = 0; i<penWorkData.length; i++){
		
		if(penWorkData[i].SEQ_Q100 == li_idx){

			//$( "#l_"+penWorkData[i].SEQ_Q100 ).addClass('active');
			pocNo1 = penWorkData[i].POC_NO01;
			$("#POC_NO01").val(pocNo1);
			$("#OTHER_LINE").val(penWorkData[i].IN_LINE);
			pocNo2 = penWorkData[i].POC_NO02;
			pocNo3 = penWorkData[i].POC_NO03;
			statusCode =  penWorkData[i].STATUS_CODE;
			inLine =penWorkData[i].IN_LINE;
			formName = penWorkData[i].FORM_NAME;
			seqT300 = penWorkData[i].SEQ_T300;
			webDataYn =penWorkData[i].WEB_DATA_YN;
			eStatus =penWorkData[i].E_STATUS;
			frmDatId= penWorkData[i].FRM_DAT_ID;
			erpUploadYN= penWorkData[i].ERP_UPLOAD_YN;
			$("#SEQ_T300").val(seqT300);
			if(formName == null || formName==""){
				formName="검사결과표 생성";
			}
			//$("#formName").html(inLine +" " +formName);
			if(fCodeMap.containsKey(inLine)){
				formCode = fCodeMap.get(inLine);
				 
			}
			
		}else{
			//$( "#l_"+penWorkData[i].SEQ_Q100 ).removeClass('active');
		}
	}
	$("#SEQ_Q100").val(li_idx);


	var url="";
	if(statusCode =="R"){ //출력대기
		window.frames["basePenWork"].location.href="/Guide.do";
	}else if(statusCode =="S" || statusCode =="C"){ //점검중 or 완료
		url= "/SheetDetailViewNew.do?FRM_DAT_ID="+frmDatId+"&SEQ_Q100="+li_idx+"&SEQ_T300="+seqT300+"&WEB_DATA_YN="+webDataYn+"&FORM_TYPE="+fTypeMap.get(inLine)+"&IN_LINE="+inLine+"&E_STATUS="+eStatus+"&STATUS_CODE="+statusCode+"&ERP_UPLOAD_YN="+erpUploadYN;
	}else{ // P 검사대기중.
		//alert("검사대기중입니다.");
		window.frames["basePenWork"].location.href="/Guide.do";
	}
	if(url==""){
		window.frames["basePenWork"].location.href="/Guide.do";
	}else{
		window.frames["basePenWork"].location.href=url;
	}
	
//	window.basePenWork.location.href=url;
}

//작업내역 원본보기
function userViewInfo(e){
	defaultFn( '/View.do?cmd=orginViewPopup',e); //View
}
//품질이상결과보고서
function userReportBgInfo(e){
	defaultFn( '/Report.do?cmd=reportViewBGPopupExtra',e); 
}
function changePOCNO() {
	$("#I_POC_NO").val($("#I_POC_NO").val().toUpperCase());
	var pocNO = $("#I_POC_NO").val();
	
	var pocTextLength = pocNO.length;
	
	if(pocTextLength<9) {
		alert("POC번호는 최소 9자리까지 입력하셔야 합니다.");
		$("#I_POC_NO").focus();
		return;
	} else if(pocTextLength>=9) {
		var oLine = $("#IN_LINE").val();
		default_line=oLine;
		
		$("#IN_LINE").val(oLine);
		$("#OTHER_LINE").val(oLine);
		$("#lineTitle").val(oLine);
		$("#SEQ_Q100").val("");
		$("#SEQ_T300").val("");
		$("#REPORT_POC").val("");
		$("#REPORT_POC01").val("");
		$("#REPORT_POC02").val("");
		$("#REPORT_POC03").val("");
		
		if(pocTextLength==9 ) {
			$("#I_POC_NO").val(pocNO+"-0000");
			$("#POC_NO").val(pocNO+"-0000");
			$("#POC_NO01").val(pocNO);
		} else if(pocTextLength>9 && pocNO.indexOf("-")>8 ) {
			var poc_split = pocNO.split("-");
			$("#POC_NO").val(pocNO);
			$("#POC_NO01").val(poc_split[0]);
		} else {
			$("#POC_NO").val(pocNO);
			$("#POC_NO01").val(pocNO);
		}
	} else {
		$("#POC_NO").val(pocNO);
		$("#POC_NO01").val(pocNO);
	}
	
	//
	$("#sheet_list").html("");
	$("#sheet_list").html('<div><h3 id="sheet_nav_1" class="slide-ON">요청된 자료를 조회중입니다<br>잠시만 기다려주세요</h3></div>');
	
	window.frames["basePenWork"].location.href="about:blank";
	
	if( otherList() ) {
		$("#lodingBG").show().fadeIn('fast');
		$("#lodingTxtDiv").show().fadeIn('fast');
		
		if(resetLineFlag) {	
			$("#IN_LINE").val(first_line);
			
			changeLine(first_line,'','','','');	
		} else {
			changeLine(default_line,'','','','');	
		}
	} else {
		$("#sheet_list").html("");
		//var help_txt ='<div><h3 id="sheet_nav_1" class="slide-ON">'+$("#IN_LINE").val()+' 공정에서 '+$("#POC_NO").val()+' 에 대한 검사결과가 없습니다.<br>POC번호 or 공정을 확인해주세요</h3></div>';
		
		var help_txt ='<div><h3 id="sheet_nav_1" class="slide-ON"> 조회된 품질검사결과표가 없습니다. 조회조건을 다시 확인해주세요</h3></div>';
		
		$("#sheet_list").html(help_txt);
		
		$("#lodingBG").hide();
		$("#lodingTxtDiv").hide();
	}
	
	$("#lodingBG").hide();
	$("#lodingTxtDiv").hide();
}

//작업공정 select 생성
function createSelect(result){
	//var str="<option value=''>전체 </option>";
	var str="<option value=''>전체 </option>";
	if(result.DATA.length > 0 ){
		for(var i = 0; i<result.DATA.length; i++){
			if('${LINE}'=='') {
			}
			str =str+"<option value='"+result.DATA[i].CODE+"'>"+result.DATA[i].CODE+" </option>";
		}
	}else{
		str = "<option value=''>없음</option>";
	}

	$("#ON_LINE").append(str);  
	var inLine = document.mySheetForm.IN_LINE.value;
	$("#ON_LINE").val(inLine);
	 
	
}

function hideGuidTxt() {
	try {
		$("#lodingBG").hide();
		$("#lodingTxtDiv").hide();	
	} catch (e) {
		// TODO: handle exception
	}
	
}
</script>
</head>

<body>
<div id="wrapper" class="top-navigation">
<form id="mySheetForm" name="mySheetForm" >
<input type="hidden" id="lineTitle" name="lineTitle" value="${LINE}" />
	 	<input type="hidden" id="IN_FACT" name="IN_FACT"  />
		<input type="hidden" id="IN_ORDER" name="IN_ORDER" />
		<input type="hidden" id="IN_LINE" name="IN_LINE" value="${LINE}" />
		<input type="hidden" id="SEQ_Q100" name="SEQ_Q100" />
		<input type="hidden" id="SEQ_T300" name="SEQ_T300" />
		<input type="hidden" id="REPORT_POC" name="REPORT_POC" />
		<input type="hidden" id="REPORT_POC01" name="REPORT_POC01" />
		<input type="hidden" id="REPORT_POC02" name="REPORT_POC02" />
		<input type="hidden" id="REPORT_POC03" name="REPORT_POC03" />
		<input type="hidden" id="REPORT_LINE" name="REPORT_LINE" />
		<input type="hidden" id="OTHER_LINE" name="OTHER_LINE" value="${LINE}" />
		<input type="hidden" id="POC_NO" name="POC_NO" value="${POC_NO}" />
		<input type="hidden" id="POC_NO01" name="POC_NO01" value="${POC_NO01}" />
		<input type="hidden" id="WD" name="WD" value="${WD}" />
		<input type="hidden" id="VIEWONLY" name="VIEWONLY" value="Y" />
</form>
  <div id="page-wrapper" class="gray-bg" style="overflow-y: hidden;">
    <div class="row border-bottom">
        <nav class="navbar navbar-static-top white-bg" role="navigation">            
        	<div class="navbar-header">
            	<!--<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>-->
                <div class="navbar-search">  
                	<form role="form" class="form-inline">
                		<div class="form-group">
                        	<label class="control-label">POC NO</label>
                			<input id="I_POC_NO" type="text" class="form-control width240px" style="font-size: 20px;font-weight: bold;ime-mode:disabled;text-transform:uppercase;" value="${POC_NO}" placeholder="POC번호를 입력하세요.">
                        </div>
                        <!--  
                    	<div class="form-group">
		               	<select id="ON_LINE" name="ON_LINE"  class="form-control input-sm" style="height: 34px;line-height: 34px;"></select> 
		                <input id="I_POC_NO" type="text" class="form-control width240px" style="font-size: 20px;font-weight: bold;" value="C15449903-000" placeholder="POC번호를 입력하세요.">
                        </div>
                         -->
                        <button type="button" class="btn btn-primary" onClick="javascript:changePOCNO();"><i class="fa fa-search"></i> 검색</button>
                        <!-- <button type="button" id="workListSearchBtn" class="btn btn-default">작업내역 상세검색</button> -->
                    </form>
                </div>
        	</div>
            <ul class="nav navbar-top-links navbar-right">
                <!-- previous procss -->
                <li id="otherList" class="process">
                <!-- 
                	<span class="btn btn-outline btn-primary">BA1</span>
                    <span class="btn btn-outline btn-danger"><i class="fa fa-warning"></i> BA2</span>
                	<span class="btn btn-outline btn-primary">BA3</span>
                    <span class="btn btn-outline btn-default disabled">UBA</span>
                	<span class="btn btn-outline btn-primary">AP2</span>
                	<span class="btn btn-outline btn-danger"><i class="fa fa-warning"></i> RC</span>
                    <span class="btn btn-outline btn-default disabled">SL</span>
                	<span class="btn btn-outline btn-default disabled">TL</span>
                    <span class="btn btn-outline btn-default disabled">DG</span>
                	<span class="btn btn-outline btn-default disabled">UTL</span>
                 -->
                </li>
            </ul>
        </nav>
    </div>

    <div class="row wrapper page-heading" style="padding-bottom:0;">            	
        <!-- slide : START -->            
		<section id="features" class="blue">
            <div class="content">
				<div id="sheet_list" class="slider slider-nav">
				<!-- 
					<div>
                    	<h3 id="sheet_nav_1" class="slide-ON" onclick="javascript:changeSheet(1,222,'BA1');">BA1 M/E COIL 검사결과표<br>(2016.12.31)</h3>
                  	</div>
					<div>
                    	<h3 id="sheet_nav_2" onclick="javascript:changeSheet(2,333,'BA1');">BA2 M/E COIL 검사결과표<br>(2016.12.31)</h3>
                  	</div>
                  	<div>
                    	<h3 id="sheet_nav_3" onclick="javascript:changeSheet(3,444,'BA1');">BA3 M/E COIL 검사결과표<br>(2016.12.31)</h3>
                  	</div>
                  	<div>
                    	<h3 id="sheet_nav_4" onclick="javascript:changeSheet(4,555,'BA1');">BA4 M/E COIL 검사결과표<br>(2016.12.31)</h3>
                  	</div>
                  	 -->
				</div>
				<div>
                	
                    <!-- slide 1 -->
					<div>
        				<!-- 양식지 영역:S -->            
        				<div class="wrapPaper">
            				<iframe src="about:blank" id="basePenWork" name="basePenWork" class="cont-iframes" frameborder="0" style="width: 990px;height: 780px;" ></iframe>     
        				</div>
        				<!-- 양식지 영역:E --> 
                    </div>
                    
				</div>
            </div>
		</section>
        <!-- slide : END -->    
      
        </div>
    <div id='pop_up_banner' class="modal-dialog modal-lg" style="display:none;">
		<div id ="content" ></div>
	</div>   
    <div class="wrapper wrapper-content"  style="display:none;">
		<div class="layout-content">
			<iframe id="mainPenWork" name="mainPenWork" class="cont-iframes" frameborder="0" ></iframe>                
    	</div>
  	</div>
    </div>
    <!--./page-wrapper -->

  </div>

<div id="lodingBG" class="ui-popup-overlay" style="display: none;"></div>
<div id="lodingTxtDiv" class="order_create_area" style="display: none;"><div id="lodingTxt" class="txt">QICS 품질검사실적을 가져오는 중입니다.<br/>잠시만 기다려 주세요</div></div>
</body>
</html>