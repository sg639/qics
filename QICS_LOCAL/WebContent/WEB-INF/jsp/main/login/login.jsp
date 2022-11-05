<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<%
String PARAM_SEQ_T300 = request.getAttribute("SEQ_T300") == null ? "" : request.getAttribute("SEQ_T300").toString() ;
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
  <title>HYUNDAI BNG STEEL</title>
  <%@ include file="/WEB-INF/jsp/common/include/jqueryScript.jsp"%>
   <script src="/common/js/inspinia.js"></script>
<style type="text/css">

</style>
</head>
<script type="text/javascript">
var iLineMap=new HashMap();
var iFactMap=new HashMap();
var iOperMap=new HashMap();
var fCodeMap= new HashMap();
var fTypeMap = new HashMap();
var pocData = new HashMap();
var penWorkData=null;
var tempSeq = '<%=PARAM_SEQ_T300%>';

function test(delay) {
	alert(delay);
	 
}

$(document).ready(function() {
	
	$('.penList').niceScroll();
	//작업추가팝업
	$('#my-button1').bind('click', function(e) {
		checkButton();
			var key = $("#IN_LINE").val();
			if(iFactMap.containsKey(key)){
				$("#IN_FACT").val(iFactMap.get(key));
			}
			if(iOperMap.containsKey(key)){
				$("#IN_ORDER").val(iOperMap.get(key));
			}
		//	alert($("#LINE_CODE").val()+" : "+$("#LINE_TYPE").val()+" : " +$("#LINE_GUBUN").val());
		if(	$('#lineTitle').text()=='공정 선택'){
			alert("작업공정을 먼저 선택해주세요");
		}else{
			var resultMn = dateCheck();
			if(resultMn != ""){
				if(confirm(resultMn)){
					defaultFn( '/Add.do?cmd=addPopup',e); //작업추가
				}
			}else{
				defaultFn( '/Add.do?cmd=addPopup',e); //작업추가
			}
			
		}
	});
	//긴급작업팝업
	$('#my-button2').bind('click', function(e) {
		checkButton();
		if(	$('#lineTitle').text()=='공정 선택'){
			alert("작업공정을 먼저 선택해주세요");
		}else{
			//callCS(441);
			defaultFn( '/Urgency.do?cmd=urgencyPopup',e);
		}
		
	});	
	//작업내역상세검색팝업
	$('#my-button3').bind('click', function(e) {
		checkButton();
		if(	$('#lineTitle').text()=='공정 선택'){
			alert("작업공정을 먼저 선택해주세요");
		}else{
			defaultFn( '/Work.do?cmd=workPopup',e); 
		}
		//
	});	
	
	$(".close").click(function() {
		closePopUp('pop_up_banner');
	});
	$('#WORK_DATE').datepicker({
		 changeMonth: true,
		 dateFormat:'yy/mm/dd',
	     maxDate : 0
	 
	});	
	
	window.frames["mainPenWork"].location.href="/Guide.do";
	 var result = ajaxCall("/Process.do?cmd=processList", $("#mySheetForm").serialize(), false); //작업공정 I/F 조회
	 createSelect(result);
	 
	if(tempSeq == ""){
		 var nowDate = new Date();
		 var nowYear = nowDate.getFullYear();
		 var nowMonth = nowDate.getMonth()+1;
		 var nowDay = nowDate.getDate();

		 var checkDate = new Date(nowYear, nowMonth-1, nowDay, 7, 0, 0, 0);
 
		 if(nowDate.getTime() > checkDate.getTime()){
			 if(nowMonth <10) {nowMonth="0"+nowMonth;}
			 if(nowDay <10) {nowDay="0"+nowDay;}
			 var thisDate = nowYear +"/"+nowMonth+"/"+nowDay;
			 $('#WORK_DATE').val(thisDate);
		 }else{
			 nowDate.setDate(nowDate.getDate()-1);
			 nowDay = nowDate.getDate();
			 var  x = new Date(nowYear, nowMonth-1, nowDay);
				// alert(x.getFullYear()+" : "+ (x.getMonth()+1)+" : "+ x.getDate()+" : "+ x.getHours());
			 $( "#WORK_DATE" ).datepicker().datepicker( "setDate", x );
		 }
		 //alert(nowYear+" : "+ nowMonth+" : "+ nowDay+" : "+ nowHours);
		
	}

	
    $("#mainPenWork").load(function () {
    	var iframeLocation = this.contentWindow.location.href;
        if ( iframeLocation.indexOf("/Guide.do")>-1 
        		|| iframeLocation.indexOf("/FilePS.do")>-1 ) { //Guide면 초기화./FilePS.do
            //alert("the iframe has changed.");
        } else {
        	
        }
    });
    
  //긴급작업팝업
	$('#logoBtn').bind('click', function(e) {
		//defaultFn( '/Guide.do',e);
	});
  
	$('#sendMsg').on("click",function () {
        var strMsg = $('#txtMsg').val();
        
        cAPI.fn_print_f(strMsg);
    });
  
    $(window).focus();
});


function callClientPrintM(param){
	alert("web : "+param);
	cAPI.fn_print_m(param);
}

function callClientPrintF(param){
	alert("web : "+param);
	cAPI.fn_print_f(param);
}

//전자펜 작업목록을 조회
function callPenWorkList(){
	$('#otherList').html('');
	var result = ajaxCall("/PenWork.do?cmd=penWorkList", $("#mySheetForm").serialize(), false);
	  createPenWork(result);
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
		        //position: ['auto', 'auto'] ,//x, y
		        loadUrl: url//Uses jQuery.load()
			});
}
//작업공정 select 생성
function createSelect(result){
    var str="<option value=''>공정 선택 </option>";
    var mStr ="";
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
			if(result.DATA[i].FORM_TYPE == "M"){
				mStr = mStr+'<li><a href="javascript:chageSearch(\''+result.DATA[i].CODE+'\')">'+result.DATA[i].CODE+'</a></li>'
			}else{
				fStr = fStr+'<li><a href="javascript:chageSearch(\''+result.DATA[i].CODE+'\')">'+result.DATA[i].CODE+'</a></li>'
			}
		}
	}else{
		//str = "<option value=''>없음</option>";
	}
	var fullStr=mStr+'<li class="divider"></li>'+fStr;
	$("#lineList").html(fullStr);  
	 
	if(tempSeq == ""){
		callPenWorkList();
		//callCS('441');
	}else{
		var result2 = ajaxCall("/Work.do?cmd=workInfo","SEQ_T300="+tempSeq, false); //작업공정 I/F 조회
		//alert(JSON.stringify(result2.DATA))
		changeMain2(result2.DATA.IN_LINE,result2.DATA.WORK_DATE,result2.DATA.SEQ_Q100);
	}
	
}
function callCS(seq){
	var result2 = ajaxCall("/Work.do?cmd=workInfo","SEQ_T300="+seq, false); //작업공정 I/F 조회
	//alert(JSON.stringify(result2.DATA))
	if(result2 && result2.DATA ){
		changeMain2(result2.DATA.IN_LINE,result2.DATA.WORK_DATE,result2.DATA.SEQ_Q100);
	}else{
		alert("삭제된 검사양식이거나 등록되지 않은 검사양식입니다.["+seq+"]");
	}
	
}
function callWinForm(seq){
	seq = seq.split("\'").join("");
	var result2 = ajaxCall("/Work.do?cmd=workInfo","SEQ_T300="+seq, false); //작업공정 I/F 조회
	//alert(JSON.stringify(result2.DATA))
	if(result2 && result2.DATA ){
		changeMain2(result2.DATA.IN_LINE,result2.DATA.WORK_DATE,result2.DATA.SEQ_Q100);
	}else{
		alert("삭제된 검사양식이거나 등록되지 않은 검사양식입니다.["+seq+"]");
	}
	
}
//전자펜 작업 갯수
function createPenWork(result){
	$("#penWorkCount").html("");
	$("#penWorkList").html("");
	penWorkData =result.DATA; 
	if(result.DATA.length > 0 ){
		$("#penWorkCount").html("총 "+result.DATA.length+" 건");
		pocData = new HashMap();
		for(var i = 0; i<result.DATA.length; i++){
			pocData.put(result.DATA[i].POC_NO,result.DATA[i].POC_NO);
		    createLay(result.DATA[i]);
		}
	}else{
		$("#penWorkCount").html("총  0 건");
		createEmptyLay();
		//callCS('441');
	}
	$(window).focus();
}
function createEmptyLay(){
	var stringDiv = '<li>';
	stringDiv +='	<div class="col-lg-12">';
	
	stringDiv +='	<div class="widget white-bg p-m text-center">';
	stringDiv +=' <div class="m-b-md">';
	if(	$('#lineTitle').text()=='공정 선택'){
		stringDiv +='<i class="fa fa-pencil-square-o fa-4x"></i><h5 class="m-sm">작업공정을 선택하세요</h5>';
	}else{
		stringDiv +='<i class="fa fa-exclamation-triangle fa-4x"></i><h5 class="m-sm">조회된 값이 없습니다.</h5>';
	}

	stringDiv +='  </div>';
	stringDiv +='</div>';
	
	stringDiv +='</div>';
	$("#penWorkList").append(stringDiv);
	
}
//인쇄요청
function checkPocNo(poc){
	//alert("poc = "+poc);
	var isPocExist = false;
	if(pocData.containsKey(poc)){
		isPocExist = true;
	}
	return isPocExist
}
function createPod(li_idx){
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
	var psFileYn="";
	var frmDatId="";
	var formType="";
	var mrgWipEntityName="";
	var extraViewPOCNO = "";
	var seqTemp = "";
	for(var i = 0; i<penWorkData.length; i++){
		
		if(penWorkData[i].SEQ_Q100 == li_idx){

			$( "#l_"+penWorkData[i].SEQ_Q100 ).addClass('active');
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
			psFileYn = penWorkData[i].PS_FILE_YN;
			formCode = penWorkData[i].FORM_CODE;
			frmDatId= penWorkData[i].FRM_DAT_ID;
			formType=  penWorkData[i].FORM_TYPE;
			seqTempList=  penWorkData[i].SEQ_TEMP_LIST;
			mrgWipEntityName =penWorkData[i].MRG_WIP_ENTITY_NAME;
			$("#SEQ_T300").val(seqT300);
			seqTemp = penWorkData[i].SEQ_TEMP;
			//$("#WORK_DATE").val(penWorkData[i].WORK_DATE);
			if(formCode != "" && formCode != null){
				
			}else{
				if(fCodeMap.containsKey(inLine)){
					formCode = fCodeMap.get(inLine);
					 
				}
			}
			if(formType ==""){
				formType = fTypeMap.get(inLine);
			}
			
			extraViewPOCNO = penWorkData[i].POC_NO01 + "-" + penWorkData[i].POC_NO02;
		}else{
			$( "#l_"+penWorkData[i].SEQ_Q100 ).removeClass('active');
		}
	}
	$("#SEQ_Q100").val(li_idx);


	var url="";
	if(statusCode =="R"){
		if(formType=="M"){
			url = "/PenWork.do?cmd=penWorkM&SEQ_Q100="+li_idx+"&FORM_CODE="+formCode+"&POC_NO01="+pocNo1+"&POC_NO02="+pocNo2+"&POC_NO03="+pocNo3+"&WORK_DATE="+$('#WORK_DATE').val()+"&IN_LINE="+inLine+"&SEQ_T300="+seqT300+"&FORM_TYPE="+formType+"&SEQ_TEMP="+seqTemp;
		}else{
			url= "/PenWork.do?cmd=penWorkF&SEQ_Q100="+li_idx+"&FORM_CODE="+formCode+"&POC_NO01="+pocNo1+"&POC_NO02="+pocNo2+"&POC_NO03="+pocNo3+"&WORK_DATE="+$('#WORK_DATE').val()+"&IN_LINE="+inLine+"&SEQ_T300="+seqT300+"&FORM_TYPE="+formType+"&SEQ_TEMP="+seqTemp;
			//url= "/PenWork.do?cmd=penWorkV&SEQ_Q100="+li_idx+"&FORM_CODE="+formCode+"&POC_NO1="+pocNo1+"&POC_NO2="+pocNo2+"&POC_NO3="+pocNo3;
		}
	}else if(statusCode =="S" || statusCode =="C"){
		if(statusCode =="C" && frmDatId !=""){
			alert("web : "+seqTempList);
			cAPI.fn_print_s(seqTempList);//url= "/PenWork.do?cmd=ufsWorkV&SEQ_Q100="+li_idx+"&SEQ_T300="+seqT300+"&WEB_DATA_YN="+webDataYn+"&FORM_TYPE="+formType+"&IN_LINE="+inLine+"&E_STATUS="+eStatus+"&FORM_CODE="+formCode+"&V_POC_NO="+pocNo1;
		}else{
			alert("web : "+seqTempList);
			cAPI.fn_print_c(seqTempList);//url= "/PenWork.do?cmd=penWorkV&SEQ_Q100="+li_idx+"&SEQ_T300="+seqT300+"&WEB_DATA_YN="+webDataYn+"&FORM_TYPE="+formType+"&IN_LINE="+inLine+"&E_STATUS="+eStatus+"&FORM_CODE="+formCode+"&STATUS_CODE="+statusCode+"&MRG_WIP_ENTITY_NAME="+mrgWipEntityName+"&V_POC_NO="+pocNo1;
		}
		
	}else if(statusCode =="P"){
		if(psFileYn =="Y"){
			alert("▣ 검사대기중인 항목입니다  ▣ \n\n  - 검사양식 출력후 전자펜입력을 기다리는 중입니다.   \n\n  - 검사양식 작성후 전자펜을 꽂으시면 해당내용이 보여집니다.\n\n\n※ 양식이 출력되지않은 경우 좌측하단 프린트 아이콘을   \n\n     이용해 검사양식을 다시 출력하시기바랍니다.\n");
			$(window).focus();
		}else{
			alert("▣ 검사양식을 생성중인 항목입니다  ▣ \n\n  - 검사양식이 만들어지는 중입니다.   \n\n  - 상황에 따라 양식생성 및 출력에 1분이상의 시간이 소요될 수 있습니다.\n\n\n※ 오랜시간동안 양식생성중인 상태로 표시되는 경우   \n\n     F5키를 이용해 화면을 새로고침 해보시기 바랍니다.\n");
			$(window).focus();
		}
		
	}else{
		//alert("검사대기중입니다.");
	}
	if(url==""){
		
	}else{
		$("#formName").html("");
		if(formName == null || formName==""){
			formName="검사결과표 생성";
		}
		$("#formName").html(inLine +" " +formName);
		
		$("#EXTRAVIEW_POC").val(extraViewPOCNO);
		window.frames["mainPenWork"].location.href=url;
		
	}
	$(window).focus();
//	window.mainPenWork.location.href=url;
}

//전자펜 작업리스트 생성	
function createLay(tempData){
	var stringDiv = '<li id=l_'+tempData.SEQ_Q100+'>';
	
	/* 카운트도 같이 수정해야해서..이건 아닌듯..
	if ("ERP 전송완료"==tempData.STATUS_NM) {
		stringDiv += '<li id=l_'+tempData.SEQ_Q100+' style="display:none;">';
	} else {
		stringDiv += '<li id=l_'+tempData.SEQ_Q100+'>';
	}
	*/
	
	stringDiv +='<a href="javascript:createPod(\''+tempData.SEQ_Q100+'\')">';
	stringDiv +='	<table class="table-sm table-bordered" style="table-layout:fixed;">';
	
	stringDiv +='  <col width="80">';
	stringDiv +='  <col width="150">';
	
	stringDiv +=' <thead>';
	stringDiv +='  <tr>';
	//stringDiv +='     <th colspan="4" class="text-center">';
	stringDiv +='     <th colspan="2" class="text-center">';
	stringDiv +='      	'+tempData.POC_NO+'';
	stringDiv +='     </th>';
	stringDiv +=' </tr>';
	stringDiv +='  </thead>';
	stringDiv +=' <tbody>';
	stringDiv  +='  <tr>';
	//stringDiv +='     <td>'+tempData.IN_FACT+'</td>';
	//stringDiv +='     <td>'+tempData.IN_ORDER+'</td>';
	if(tempData.MRG_STEEL_TYPE !="" && tempData.MRG_STEEL_TYPE !=null){
		stringDiv +='     <td >'+tempData.MRG_STEEL_TYPE+'</td>';
	}else{
		stringDiv +='     <td >&nbsp;</td>';
	}
	
	if(tempData.MRG_R_SUPPLIER !="" && tempData.MRG_R_SUPPLIER !=null){
		stringDiv +='     <td style="word-break:break-all;">'+tempData.MRG_R_SUPPLIER+'</td>';
	}else{
		stringDiv +='     <td style="word-break:break-all;">&nbsp;</td>';
	}
	stringDiv +='  </tr>';
	stringDiv +='  <tr>';
	//stringDiv +='     <td colspan="3">'+tempData.MRG_R_SUPPLY_THICKNESS+' X '+tempData.MRG_R_WIDTH+'</td>';
	//stringDiv +='     <td>'+tempData.MRG_R_SUPPLY_THICKNESS+' X '+tempData.MRG_R_WIDTH+'</td>';
	//stringDiv +='     <td style="word-break:break-all;">'+tempData.STATUS_NM+'</td>';

	if("출력대기"==tempData.STATUS_NM) {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;"><img src="/common/img/btn_p_1.png"></td>';
			
	} else if ("양식생성중"==tempData.STATUS_NM) {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;"><img src="/common/img/btn_p_2.png"></td>';
		
	} else if ("검사대기"==tempData.STATUS_NM) {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;"><img src="/common/img/btn_p_3.png"></td>';
		
	} else if ("검사중"==tempData.STATUS_NM) {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;"><img src="/common/img/btn_p_4.png"></td>';
		
	} else if ("ERP 전송완료"==tempData.STATUS_NM) {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;"><img src="/common/img/btn_p_5.png"></td>';
		
	} else if ("ERP 전송실패"==tempData.STATUS_NM) {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;"><img src="/common/img/btn_p_6.png"></td>';
		
	} else {
		stringDiv +='     <td id="LIST_STATUS_NM_'+tempData.SEQ_Q100+'" style="word-break:break-all;">'+tempData.STATUS_NM+'</td>';
		
	}
	
	
	
	if(tempData.MRG_R_SUPPLY_THICKNESS != "" && tempData.MRG_R_WIDTH !=""){
		stringDiv +='     <td>'+tempData.MRG_R_SUPPLY_THICKNESS+' X '+tempData.MRG_R_WIDTH+'</td>';
	}else{
		stringDiv +='     <td>'+tempData.MRG_R_SUPPLY_THICKNESS+' X '+tempData.MRG_R_WIDTH+'</td>';
	}
	
	stringDiv +=' </tr>';
	stringDiv +='  </tbody>';
	stringDiv +='</table>';
	stringDiv +='  </a>';
	stringDiv +='</li>';
	$("#penWorkList").append(stringDiv);
	
	$("#formName").html("품질검사정보수집시스템(QICS)");
	
	$(window).focus();
}

function callT100(seqQ100,seqT300,formType,inLine,workDate) { //PS파일이 생성되고 다운로드가 완료되면. 전용브라우져에서 호출됨.
	//alert(" seqQ100="+seqQ100 +",seqT300="+seqT300+",formType="+formType+",inLine="+inLine+",workDate="+workDate);
	//seqQ100 = 1101 seqT300 =810 formType =M inLine =AP2 workDate =2016/03/08
	try {
		var refreshData = false;
		for(var i = 0; i<penWorkData.length; i++){
			
			if(penWorkData[i].SEQ_Q100 == seqQ100){
				//전자펜 작업 갯수
				refreshData = true;
				
				break;
			}
		}
		//alert("::"+refreshData+"::");
		if(refreshData) {
			penWorkData = null;
		
			var result = ajaxCall("/PenWork.do?cmd=penWorkList", $("#mySheetForm").serialize(), false);
			penWorkData =result.DATA;
			
			//alert("::"+$("#LIST_STATUS_NM_"+seqQ100).html()+"::");
			if(formType!="R") {
				$("#LIST_STATUS_NM_"+seqQ100).html('<img src="/common/img/btn_p_3.png">');
			}
			
		}
	} catch (e) {
		alert(e+"\n\n※ 양식생성정보가 수신되었으나, 화면을 새로고침하지 못했습니다.\n\n F5키를 눌러 화면을 새로고침하신뒤 사용하시길 권장합니다.");
	}
	$(window).focus();
}

//종료
function closePopUp(closeId){
	$('#content').html('');
	$('#'+closeId).bPopup().close();
	$(window).focus();
}
function  changePenWorkF(li_idx,formCode,pocNo1,pocNo2,pocNo3,inLine,seqT300){

	//url= "/PenWork.do?cmd=penWorkF&SEQ_Q100="+li_idx+"&FORM_CODE="+formCode+"&POC_NO01="+pocNo1+"&POC_NO02="+pocNo2+"&POC_NO03="+pocNo3+"&WORK_DATE="+$('#WORK_DATE').val()+"&IN_LINE="+inLine+"&SEQ_T300="+seqT300+"&FORM_TYPE=F";
	//window.frames["mainPenWork"].location.href=url;
}
function changeNm(seqQ100){
	$("#LIST_STATUS_NM_"+seqQ100).html("");
	$("#LIST_STATUS_NM_"+seqQ100).html('<img src="/common/img/btn_p_6.png">');
}

function check_Enter(){
    /**
     *엔터키 입력시 조회
     */
	if (event.keyCode==13) alert("엔터키");
}
//작업내역 상세보기
function userViewInfo(e){
	defaultFn( '/View.do?cmd=orginViewPopup',e); //View
}
//품질이상결과보고서
function userReportBgInfo(e){
	defaultFn( '/Report.do?cmd=reportViewBGPopup',e); 
}
//분할양식추가
function userDivide(e){
	defaultFn( '/Divide.do?cmd=dividePopup',e); 
}
//품질이상결과생성
function reportBgInfo(e){
	defaultFn( '/Report.do?cmd=reportBGPopup',e); 
}
//저장후 조회
function workList(li_idx){
	callPenWorkList();
	createPod(li_idx);
}

function viewPrintList(){
	if(	$('#lineTitle').text()=='공정 선택'){
		alert("작업공정을 먼저 선택해주세요");
	}else{
		$("#my-button3").hide();
		$("#my-button4").hide();
		$("#penWorkListInfo").hide();
		$("#penWorkCountInfo").hide();
		$("#printListInfo").show();
		callPrintList();
	}
	
}

function viewWorkList(){
	$("#my-button3").show();
	$("#my-button4").show();
	$("#penWorkListInfo").show();
	$("#penWorkCountInfo").show();
	$("#printListInfo").hide();
}
//인쇄목록 조회
function callPrintList(){
	var result = ajaxCall("/Work.do?cmd=printList", $("#mySheetForm").serialize(), false);
	createPrintWork(result.DATA);
}
function createPrintWork(printListInfo){
	$('#printList').html("");
	//alert(printListInfo.length );
	if(printListInfo.length > 0){
	//progressStart("조회중입니다.",function() {
		for(var i = 0; i<printListInfo.length; i++){
			var stringDiv = '<tr>';
			stringDiv +='<td>';
			stringDiv +='	<span>'+printListInfo[i].POC_NO+'</span><br/>';
			stringDiv += printListInfo[i].IN_LINE +"  "+printListInfo[i].FORM_NAME;
			stringDiv +='</td>';
			if("Y"==printListInfo[i].PS_FILE_YN) {
				stringDiv +='<td><button type="button" class="btn btn-success btn-mini" onclick="javascript:goPrint(\''+printListInfo[i].SEQ_T300+'\')"><i class="ion ion-android-print"></i></button></td>';
					
			} else {
				stringDiv +='<td>&nbsp;</td>';
				
			}
			//stringDiv +='<td><button type="button" class="btn btn-success btn-mini" onclick="javascript:goPrint2(\''+printListInfo[i].FORM_ID+'\',\''+printListInfo[i].PS_FILE_NAME+'\')"><i class="ion ion-android-print"></i></button></td>';
			stringDiv +='<td><button type="button" class="btn btn-danger btn-mini" onclick="javascript:goDelete('+printListInfo[i].SEQ_Q100+','+printListInfo[i].SEQ_T300+',\''+printListInfo[i].FORM_NAME+'\')"><i class="ion ion-close"></i></button></td>';
			stringDiv +='</tr>';
			$('#printList').append(stringDiv);
		}
	//	progressStop();
		
	//});
	}
}
function goPrint(seqT300){
	//alert(formId + " : "+psName);
	//var url = "/FilePS.do?FORM_ID="+formId+"&PS_NAME="+psName;
	try {
		callHostUI("defaultprint", seqT300);
	} catch (e) {
		alert(e+"\n\n▣ QICS 전용브라우져를 사용해주시기 바랍니다.");
	}
	alert("인쇄요청을 하였습니다.");
	
}
function goPrintByPopUp(seqT300){
	//alert(formId + " : "+psName);
	//var url = "/FilePS.do?FORM_ID="+formId+"&PS_NAME="+psName;
	try {
		callHostUI("imgprint", seqT300);
	} catch (e) {
		alert(e+"\n\n▣ QICS 전용브라우져를 사용해주시기 바랍니다.");
	}
	//alert("인쇄요청을 하였습니다.");
}
function goPrint2(formId,psName){
	//alert(formId + " : "+psName);
	var url = "/FilePS.do?FORM_ID="+formId+"&PS_NAME="+psName;
	//callHostUI("defaultprint", seqT300);
	window.frames["mainPenWork"].location.href=url;
	
}
function callHostUI(cmd, value)
{
	try {
		window.external.PopulateWindow(cmd, value);
	} catch (e) {
		alert(e+"\n\n▣ QICS 전용브라우져를 사용해주시기 바랍니다.");
	}
}
function goDelete(seqQ100, seqT300, formname){

	if(confirm("생성된 검사양식을 삭제하시겠습니까?")){
		var result = ajaxCall("/Delete.do?cmd=dataDelete", "SEQ_Q100="+seqQ100+"&SEQ_T300="+seqT300, false);
		callPrintList();
		
		var refreshData = false;
		try {
			for(var i = 0; i<penWorkData.length; i++){
				
				if(penWorkData[i].SEQ_Q100 == seqQ100){
					//전자펜 작업 갯수
					refreshData = true;
					
					break;
				}
			}
			if(refreshData) {
					penWorkData = null;
				
					var result = ajaxCall("/PenWork.do?cmd=penWorkList", $("#mySheetForm").serialize(), false);
					penWorkData =result.DATA;
					
					if(formname.indexOf("품질이상")>-1) {
						
					} else {
						if( $("#LIST_STATUS_NM_"+seqQ100).length > 0 ) { //객체가 존재하면...
							$("#LIST_STATUS_NM_"+seqQ100).html('<img src="/common/img/btn_p_1.png">');
						}	
					}
						
			}
		} catch (e) {
			alert(e+"\n\n※ 검사양식을 삭제하였으나, 화면을 새로고침하지 못했습니다.\n\n F5키를 눌러 화면을 새로고침하신뒤 사용하시길 권장합니다.");
		}
	}
 
}
function deleteInfo(seqQ100, seqT300,inLine){

	if(confirm("삭제하시겠습니까?")){
		var result = ajaxCall("/Delete.do?cmd=dataDelete", "SEQ_Q100="+seqQ100+"&SEQ_T300="+seqT300, false);
		//window.frames["mainPenWork"].location.href="/Guide.do";
		chageSearch(inLine);
	}
 
}
function clearInfo(seqQ100){

	if(confirm("QICS작업목록에서 삭제 하시겠습니까?")){
		var result = ajaxCall("/Delete.do?cmd=clearPenWork", "SEQ_Q100="+seqQ100, false);
		window.frames["mainPenWork"].location.href="/Guide.do";
		$("#formName").html("품질검사정보수집시스템(QICS)");
		callPenWorkList();
	}
 
}

function erpEnd(seqQ100, seqT300,inLine){
	//window.frames["mainPenWork"].location.href="/Guide.do";
	chageSearch(inLine);
}



//타공정팝업
function otherLine(inLine,pocNo01){
	//var e = window.event;
	//defaultFn( '/Other.do?cmd=otherPopup',e); 
}

function openExtraView(line,pocno01,wd) { 
	if(""!=$("#EXTRAVIEW_POC").val()){
		$("#EXTRAVIEW_LINE").val(line);
		$("#EXTRAVIEW_WD").val(wd);
		//$("#EXTRAVIEW_POC").val(pocno01); --좌측목록선택시 셋팅..
		
		var result = ajaxCall("/Other.do?cmd=extraViewerKey", $("#mySheetForm").serialize(), false);
		if(result&&result.RESULT&&result.RESULT.LINKURL) {
			var extraViewerURL = "/Other.do?cmd=extraViewer&p="+result.RESULT.LINKURL;
			
		    var W = 1100;        //screen.availWidth; 
		    var H = screen.availHeight;

		    var features = "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,width=" + W + ",height=" + H + ",left=0,top=0"; 
		    var ExtraViewer = window.open(extraViewerURL,(""+line+""+pocno01),features); 

		    $("#EXTRAVIEW_LINE").val("");
		    $("#EXTRAVIEW_WD").val("");
			//$("#EXTRAVIEW_POC").val("");
		} else {
			alert("타공정조회화면 권한 오류");	
		}	
	} else {
		
	}
}

function goOtherLine(){
	var result = ajaxCall("/OtherLine.do", $("#mySheetForm").serialize(), false); //타공정 조회
	createOther(result);
}
function createOther(result){
	var otherListStr="";
	$('#otherList').html('');
	if(result && result.DATA && result.DATA.length > 0 ){
		for(var i = 0; i<result.DATA.length; i++){			
				if( result.DATA[i].E_STATUS=="" ) {
	            	//str += '<span class="btn btn-outline btn-default disabled" onclick="javascript:otherLine(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\','+result.DATA[i].M_CNT+','+result.DATA[i].F_CNT+','+result.DATA[i].R_CNT+');">'+result.DATA[i].LINE_CODE+'</span>';
	            	otherListStr += '<span id="IN_LINE_'+result.DATA[i].LINE_CODE+'" class="btn btn-outline btn-primary" onclick="javascript:openExtraView(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\',\'\');">'+result.DATA[i].LINE_CODE+'</span>';
	            } else {
	            	otherListStr += '<span id="IN_LINE_'+result.DATA[i].LINE_CODE+'" class="btn btn-outline btn-danger" onclick="javascript:openExtraView(\''+result.DATA[i].LINE_CODE+'\',\''+result.DATA[i].POC_NO01+'\',\'\');"><i class="fa fa-warning"></i> '+result.DATA[i].LINE_CODE+'</span>';
	            }
		}
		$('#otherList').html(otherListStr);
	} else {

	}
}
function chageSearch(inLine){
	//alert($('#lineTitle').text());
	$("#formName").html("품질검사정보수집시스템(QICS)");
	$('#lineTitle').text(inLine);
	window.frames["mainPenWork"].location.href="/Guide.do";
	$("#IN_LINE").val(inLine);
	callPenWorkList();
	
	$('#otherList').html('');
}
function dateCheck(){
	 var nowDate = new Date();
	 var nowYear = nowDate.getFullYear();
	 var nowMonth = nowDate.getMonth()+1;
	 var nowDay = nowDate.getDate();
	 var currDay = 1*24 * 60 * 60 * 1000;// 
	 var checkDate = new Date(nowYear, nowMonth-1, nowDay, 7, 0, 0, 0);
	 var thisDate ="";
	 var message="";
	 if(nowDate.getTime() > checkDate.getTime()){
		 if(nowMonth <10) {nowMonth="0"+nowMonth;}
		 if(nowDay <10) {nowDay="0"+nowDay;}
		  thisDate = nowYear +"/"+nowMonth+"/"+nowDay;
		 if(thisDate !=  $('#WORK_DATE').val()){
			 //alert("선택한 날짜는 과거일자입니다. \n\n[현재일자 : "+thisDate+" ]     \n\n업무에 참고하시기 바랍니다.     ");
			 message="선택한 날짜는 과거일자입니다. \n\n[현재일자 : "+thisDate+" ]     \n\n업무에 참고하시기 바랍니다.     ";
		 }
	 }else{
		 var settingDate = new Date();
		 var settingYear = "";
		 var settingMonth = "";
		 var settingDay = "";
		 settingDate.setTime(nowDate.getTime()-currDay);
		 settingYear = settingDate.getFullYear();
		 settingMonth = (settingDate.getMonth()+1) ? "0"+ (settingDate.getMonth()+1):(settingDate.getMonth()+1);
		 settingDay = settingDate.getDate() <10 ? "0" + settingDate.getDate(): settingDate.getDate();
		 thisDate = settingYear +"/"+settingMonth+"/"+settingDay;
		
		 if(thisDate !=  $('#WORK_DATE').val()){
			 message = "선택한 날짜는 과거일자입니다. \n\n[현재일자 : "+thisDate+" ]      \n\n업무에 참고하시기 바랍니다.     ";
			// alert("선택한 날짜는 과거일자입니다. \n\n[현재일자 : "+thisDate+" ]      \n\n업무에 참고하시기 바랍니다.     ");
		 }
		 
	 }
	 return message
}
function chageDate(){
	 
	var inLine = $('#lineTitle').text();
	window.frames["mainPenWork"].location.href="/Guide.do";
	$("#IN_LINE").val(inLine);
	callPenWorkList();
}
function changeMain(inLine,workDate,li_idx){
	$('#lineTitle').text(inLine);
	$("#IN_LINE").val(inLine);
	$("#WORK_DATE").val(workDate);
	 closePopUp('pop_up_banner');
	 callPenWorkList();
	 createPod(li_idx);
}
function changeMain2(inLine,workDate,li_idx){
	$('#lineTitle').text(inLine);
	$("#IN_LINE").val(inLine);
	$("#WORK_DATE").val(workDate);
	 callPenWorkList();
	 createPod(li_idx);
}
function checkButton(){
	
	if($("#printListInfo").css("display") == "block"){
		$("#my-button3").show();
		$("#my-button4").show();
		$("#penWorkListInfo").show();
		$("#penWorkCountInfo").show();
		$("#printListInfo").hide();
	}

}

function iframe_autoresize(arg) {
    arg.height = eval(arg.name+".document.body.scrollHeight");
}

</script>
<body>
<div id="wrapper">
	<form id="mySheetForm" name="mySheetForm" >
	 	<input type="hidden" id="IN_FACT" name="IN_FACT"  />
		<input type="hidden" id="IN_ORDER" name="IN_ORDER" />
		<input type="hidden" id="IN_LINE" name="IN_LINE" />
		<input type="hidden" id="SEQ_Q100" name="SEQ_Q100" />
		<input type="hidden" id="SEQ_T300" name="SEQ_T300" />
		<input type="hidden" id="REPORT_POC" name="REPORT_POC" />
		<input type="hidden" id="REPORT_POC01" name="REPORT_POC01" />
		<input type="hidden" id="REPORT_POC02" name="REPORT_POC02" />
		<input type="hidden" id="REPORT_POC03" name="REPORT_POC03" />
		<input type="hidden" id="REPORT_LINE" name="REPORT_LINE" />
		<input type="hidden" id="OTHER_LINE" name="OTHER_LINE" />
		<input type="hidden" id="POC_NO01" name="POC_NO01" />
		<input type="hidden" id="EXTRAVIEW_LINE" name="EXTRAVIEW_LINE" />
		<input type="hidden" id="EXTRAVIEW_POC" name="EXTRAVIEW_POC" />
		<input type="hidden" id="EXTRAVIEW_WD" name="EXTRAVIEW_WD" />
	<nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header"> 
                	<span><img id="logoBtn" src="/common/img/logo_steel.png" alt="HYUNDAI BNG STEEL" /></span>
                </li>
                
                <!-- LeftMenu : 작업공정 -->
<!-- 
				<input type="text" id="txtMsg" class="form-control"/></label>
    			<button class="btn btn-primary" id="sendMsg">팝업메시지</button>
 -->
    
                <li class="header lm-title1"></li>
                <li class="m-b">
                	<div class="lm-process">
                      	<div class="form-group m-b-sm">
                      	
                      	<div class="input-group-btn"> 
                           		<button id="lineTitle" tabindex="-1" data-toggle="dropdown" class="btn btn-white dropdown-toggle combo-xl" type="button" >공정 선택</button> 
                                   <button data-toggle="dropdown" class="btn btn-white dropdown-toggle btn-combo" type="button" ><span class="caret"></span></button> 
                           		<ul id="lineList" class="dropdown-menu dropdown-xl" style="width:191px; font-size:16px"> 
                           			 
                           		</ul> 
                           	</div>
                           		                         	
                      	</div>
                         
                  	  	<div class="input-group m-b-sm">
  				       		<!--<input class="span2" size="16" type="text" >-->   
  							<input type="text" id="WORK_DATE" name="WORK_DATE"  readonly="readonly"  class="form-control width150px" style="padding-left: 10px;" onchange="javascript:chageDate();">
  							 
						</div>
                        <div class="row">
              				<div class="col-xs-12">
                  				<button id="my-button2" type="button" class="btn btn-info btn-sm pull-right">긴급작업</button>
                  				<button id="my-button1" type="button" class="btn btn-success btn-sm pull-left"><i class="fa fa-plus"></i> 작업추가</button>
              		  		</div>
            		  	</div>
                    </div>
                </li>
   
                <!-- LeftMenu : 전자펜 작업목록 -->
                <li id="penWorkCountInfo" class="header lm-title2">
                		<span id="penWorkCount" class="pull-right"></span>
                </li> 
                <li id="penWorkListInfo">
                	<div id="penwork_area" class="penList ">
                		<ul id="penWorkList" class="nav">
                	</ul>
                	</div>
                </li>
                <li>
                <div  id="printListInfo" style="display: none">
                    <div id="print_area" class="penList print-Area">
                	<ul class="nav">
                    	<li>
                    		<table class="table table-print">
                            <colgroup>
                            	<col width="70%">
                                <col width="15%">
                                <col width="15%">
                            </colgroup>
                            <thead>
                            <tr>
                                <th class="text-left">
                                	QICS 검사양식 출력
                                </th>
                                <th class="text-center">&nbsp;</th>
                                <th class="text-center">&nbsp;</th>
                            </tr>
                            </thead>
                            <tbody id="printList">
                            
                            </tbody>
                        	</table>
                    	</li>
                	</ul>
                	</div>
                	<div style="position:absolute; left:240px; bottom:0">
                    	<button type="button" class="btn btn-primary btn-print" onClick="javascript:viewWorkList();"><i class="ion ion-android-print"></i></button>
                    </div>
                    
                </div>
                </li>
            </ul>
        </div>
        
        <!-- nav-footer -->
        <div class="nav-footer">
        	<div class="row">
           		<div class="col-xs-4">
            		<button type="button" id="my-button4" class="btn btn-primary btn-print pull-left"  onclick="javascript:viewPrintList();"><i class="ion ion-android-print"></i></button>
           		</div>
           		<div class="col-xs-8">
             		<button type="button" id="my-button3" class="btn btn-default pull-right"><i class="fa fa-search"></i> 작업내역 상세검색</button>
           		</div>
        	</div>
        </div>
        <!-- ./nav-footer -->
  </nav>
</form>
  <div id="page-wrapper" class="gray-bg">
    <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
        	<div class="navbar-header">
            	<!--<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>-->
        	</div>
            <ul class="nav navbar-top-links navbar-right">
                <!-- previous procss -->
                <li id="otherList" class="process">
                	 <!-- <span class="btn btn-outline btn-primary" onClick="javascript:otherLine('BA1');">BA1</span>
                    <span class="btn btn-outline btn-danger" onClick="javascript:otherLine('BA2');"><i class="fa fa-warning"></i> BA2</span>
                	<span class="btn btn-outline btn-primary" onClick="javascript:otherLine('BA3');">BA3</span>
                    <span class="btn btn-outline btn-default disabled" onClick="javascript:otherLine('UBA');">UBA</span>
                	<span class="btn btn-outline btn-primary" onClick="javascript:otherLine('AP2');">AP2</span>
                	<span class="btn btn-outline btn-danger" onClick="javascript:otherLine('RC');"><i class="fa fa-warning"></i> RC</span>
                    <span class="btn btn-outline btn-default disabled" onClick="javascript:otherLine('SL');">SL</span>
                	<span class="btn btn-outline btn-default disabled" onClick="javascript:otherLine('TL');">TL</span>
                    <span class="btn btn-outline btn-default disabled" onClick="javascript:otherLine('DG');">DG</span>
                	<span class="btn btn-outline btn-default disabled" onClick="javascript:otherLine('UTL');">UTL</span> -->
                </li>
            </ul>
        </nav>
    </div>
    <!-- SubTitle -->
    <div class="row wrapper border-bottom white-bg page-heading">
    	<div class="col-lg-10">
       		<h2><span id="formName">품질검사정보수집시스템(QICS)</span></h2>
       	</div>
       	<div class="col-lg-2">
       	</div>
    </div>
    <!-- ./SubTitle -->
 	<div id='pop_up_banner' class="modal-dialog modal-lg">
		<div id ="content" ></div>
	</div>   
    <div class="wrapper wrapper-content">
		<div class="layout-content">
			<iframe id="mainPenWork" name="mainPenWork" src="about:blank" class="cont-iframes" scrolling="no" frameborder="0"   ></iframe>                
    	</div>
  	</div>
  	<!--./page-wrapper -->
</div>
</div>
</body>
</html>
