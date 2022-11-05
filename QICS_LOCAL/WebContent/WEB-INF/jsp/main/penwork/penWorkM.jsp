<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<%
String SEQ_Q100 = request.getParameter("SEQ_Q100") == null ? "" : request.getParameter("SEQ_Q100");
String SEQ_TEMP = request.getParameter("SEQ_TEMP") == null ? "" : request.getParameter("SEQ_TEMP");
String FORM_CODE = request.getParameter("FORM_CODE") == null ? "" : request.getParameter("FORM_CODE");
String POC_NO01 = request.getParameter("POC_NO01") == null ? "" : request.getParameter("POC_NO01");
String POC_NO02 = request.getParameter("POC_NO02") == null ? "" : request.getParameter("POC_NO02");
String POC_NO03 = request.getParameter("POC_NO03") == null ? "" : request.getParameter("POC_NO03");
String WORK_DATE = request.getParameter("WORK_DATE") == null ? "" : request.getParameter("WORK_DATE");
String IN_LINE = request.getParameter("IN_LINE") == null ? "" : request.getParameter("IN_LINE");
String FORM_TYPE = request.getParameter("FORM_TYPE") == null ? "" : request.getParameter("FORM_TYPE");
%>
<!DOCTYPE html>
<html id="wrapSub">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
  <title>HYUNDAI BNG STEEL :: 서브화면2 - 양식인쇄(중간검사 공정용)M</title>
  <%@ include file="/WEB-INF/jsp/common/include/jqueryScript.jsp"%>

<script type="text/javascript">

	$(function() {
		

	});
	$(document).ready(function() {		
		 //$('.i-checks').iCheck({
       //      checkboxClass: 'icheckbox_square-green',
        //     radioClass: 'iradio_square-green',
       //  });
		var result = ajaxCall("/PenWork.do?cmd=penWorkInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색
		//alert(JSON.stringify(result.DATA));
		spanData(result.DATA);
		
	
	});
	function stampData(result){
		var str='<div class="form-group">';
		if(result.DATA.length > 0 ){
			var point = 0;
			for(var i = 0; i<result.DATA.length; i++){
				if(result.DATA[i].ATTRIBUTE2 == "중간검사"){
					if((point+1)%3 == 0   ){
						str = str +'<div class="col-sm-4">';
						str = str +'<div class=""><label> <input type="checkbox"  id="chk"  name="chk"  value="'+result.DATA[i].CODE+'" >  <span>'+result.DATA[i].CODE+'</span> </label></div>';
						str = str +'</div>';
						str = str+'</div>';
						str=  str+'<div class="form-group">';
					}else{
						str = str +'<div class="col-sm-4">';
						str = str +'<div class=""><label> <input type="checkbox"  id="chk"  name="chk"  value="'+result.DATA[i].CODE+'" >  <span>'+result.DATA[i].CODE+'</span> </label></div>';
						str = str +'</div>';
					}
					point++;
				}
				if((i+1)==result.DATA.length){
					str = str +'<div class="col-sm-4">';
					str = str +'&nbsp;';
					str = str +'</div>';
					str = str +'</div>';
				}
			}
			$("#stampList").append(str);
			$("input[type=checkbox]").click(function() {
				var checkCnt = 0;
				$('#chk:checked').each(function() { 
					checkCnt++;
			  	 });
				
				if(checkCnt>4) {
					alert("알림정보는 최대 4개 까지만 선택 가능합니다.");
					$(this).attr("checked", false);
				}
				
				 var count=0;
				 var checkData ="";
				 var checkValue = $(this).val();
				$('#chk:checked').each(function() { 
					
					count++;
					if(count == 5){
						alert("알림정보는 최대 4개 까지만 선택 가능합니다.");
						$(this).attr("checked", false);
					}else{
						checkData =checkData+$(this).val()+",";
						
					}
			      
			   });
				$("#CHECK_DATA").val(checkData);
				//alert($("#CHECK_DATA").val());
			});
		}
		parent.goOtherLine();

	}
	function spanData(tempData){
		$("#POC_NO").html('');
		$("#SPAN_ORDER").html('');
		$("#SPAN_LINE").html('');
		$("#MRG_R_SUPPLY_THICKNESS").html('');
		$("#MRG_R_WIDTH").html('');
		$("#PARTIAL_WEIGHT").html('');
		$("#MRG_STEEL_TYPE").html('');
		$("#MRG_R_SUPPLIER").html('');
		$("#STATUS_NM").html('');
		
		$("#POC_NO").html(tempData.POC_NO);
		$("#SPAN_ORDER").html(tempData.IN_ORDER);
		$("#SPAN_LINE").html(tempData.IN_LINE);
		$("#MRG_R_SUPPLY_THICKNESS").html(tempData.MRG_R_SUPPLY_THICKNESS);
		$("#MRG_R_WIDTH").html(tempData.MRG_R_WIDTH);
		$("#PARTIAL_WEIGHT").html(tempData.PARTIAL_WEIGHT);
		$("#MRG_STEEL_TYPE").html(tempData.MRG_STEEL_TYPE);
		$("#MRG_R_SUPPLIER").html(tempData.MRG_R_SUPPLIER);
		$("#STATUS_NM").html(tempData.STATUS_NM);
		createStamp();
	}
	function createStamp(){
		$("#stampList").html('');
		var result =  ajaxCall("/Process.do?cmd=qicsStampList", $("#frameForm").serialize(), false);  //작업내역 상세검색
		stampData(result);
	}
	function createImport(){
		$("#print").hide();
	 	$("#direct").hide();
	 	$("#delete").hide();
		progressStart("양식생성에 필요한 정보를 수집중입니다.<br/>잠시만 기다려 주세요",function() {
			var result = ajaxCall("/PenWork.do?cmd=penWorkMiddlePrint", $("#frameForm").serialize(), false);
			progressStop();
			var delayTime =0;
			var penWorkData =result.DATA;
			if(penWorkData.RESULT =="Y"){
				callHostUI("psprint", penWorkData.SEQ_T300);
				progressStart(penWorkData.FORM_NAME+'('+$("#POC_NO").html()+')을 생성중입니다.<br/>잠시만 기다려 주세요', 
						function() {
							progressStop();
							createSampleFormFinish(penWorkData); 
						}, delayTime
					);
			}
		});
		 
		
		//sleep(10000);
		//printCall(result);
	}
	
	function createImport2(){
		$("#print").hide();
	 	$("#direct").hide();
	 	$("#delete").hide();
	 	
	 	var result = ajaxCall("/PenWork.do?cmd=penWorkMiddlePrint2", $("#frameForm").serialize(), false);
	 	
	 	//parent.callClientPrintM($("#SEQ_Q100").val());
	 	parent.callClientPrintM($("#SEQ_TEMP").val());
	}
	
	function printCall(result){
		var penWorkData =result.DATA;
		alert(JSON.stringify(result));
		if(penWorkData.RESULT =="Y"){
		/*	 var idx = $("#SEQ_Q100").val();
			//parent.workList(idx);
		//	var psName = penWorkData.PS_NAME;
			var psName = "work_x64.zip";
			var formId = penWorkData.FORM_ID;
			//var url = "/FilePS.do";
			var url = "/FilePS.do?FORM_ID="+formId+"&PS_NAME="+psName;
			window.frames["download"].location.href=url;
			//alert(url);*/
			
		}else{
			alert(result.Message);
		}
	}
	function createSampleFormFinish(penWorkData){
		if(penWorkData.RESULT =="Y"){
			alert("인쇄요청을 하였습니다.");
			 parent.callPenWorkList();
		}else{
			alert("양식생성에 실패했습니다.");
		}
	}	
	//function printCall(result){
//		var penWorkData =result.DATA;
		
//		if(penWorkData.RESULT =="Y"){
//			var psName = penWorkData.PS_NAME;
//			var formId = penWorkData.FORM_ID;
//			var url = "http://203.231.40.97/FilePS.do?FORM_ID="+formId+"&PS_NAME="+psName;
			//window.frames["download"].location.href=url;
//			callHostUI("psprint",url);
//		}else{
//			alert(result.Message);
//		}
//	}
	function callHostUI(cmd, value)
	{
		try {
			window.external.PopulateWindow(cmd, value);
		} catch (e) {
			alert(e+"\n\n▣ QICS 전용브라우져를 사용해주시기 바랍니다.");
		}
	}
	
	function createWeb(){
		$("#print").hide();
	 	$("#direct").hide();
	 	$("#delete").hide();
		var result = ajaxCall("/PenWork.do?cmd=penWorkMiddleDirect", $("#frameForm").serialize(), false);
		if(result.RESULT =="Y"){
			var idx = $("#SEQ_Q100").val();
			parent.workList(idx);
		}
	}

	function loading_st(){
		var ct_left = (parseInt(window.screen.width)-450)/2;
		var ct_top = (parseInt(window.screen.height))/3;
		layer_str = "<div id='loading_layer' style='position:absolute; background-color:; font-size:12px; left:"+ct_left+"px; top:"+ct_top+"px; width:400px; height:; padding:50px; text-align:center; vertical-align:middle; z-index:1000; font-weight: bold;'>로딩중입니다.</div>"
		document.write(layer_str);
		}
	function loading_ed(){
	 
		var ta =document.getElementById('loading_layer');
		ta.style.display='none';
		}
	function sleep(delay) {
 
		var start = new Date().getTime();
		while (new Date().getTime() < start + delay);
	}
	function checkBoxCount(){
 
		var count=0;
		$("input[name=box]:checked").each(function() {
			count++;
			var test = $(this).val();

		//	alert(test);

		});
	}
	function setCheckBox(){
		
		var splitCode = $("#splitCode").val().split(",");

		for (var idx in splitCode) {

			$("input[name=box][value=" + splitCode[idx] + "]").attr("checked", true);

		}

	}
	function syncCode(){
		
		progressStart("알림항목 동기화중입니다.<br/>잠시만 기다려 주세요",function() {
		var result = ajaxCall("/Code.do?cmd=codeSync", $("#frameForm").serialize(), false);
		if(result.RESULT=="Y"){
			progressStop();
			createStamp();
		}else{
			progressStop();
		}
		alert(result.Message);
		});
	}
	function clearData(){
		parent.clearInfo($("#SEQ_Q100").val());
	}
	function changeType(){
		parent.changePenWorkF($("#SEQ_Q100").val(),"FORM_QICS_C",$("#POC_NO01").val(),$("#POC_NO02").val(),$("#POC_NO03").val(),$("#IN_LINE").val(),"");
	}
</script>

</head>
<body>
        <div class="wrapSub">
            <div class="row">
                <div class="col-lg-12">
                  <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>작업기본정보</h5>
                    </div>
                    <div class="ibox-content">
                        <table class="table table-bordered green">
                            <thead>
                            <tr>
                                <th colspan="4"><div class="th-text">POC NO</div>
                                	<span id ="POC_NO"></span>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><div class="td-text">공정</div>
                                	<span id ="SPAN_ORDER"></span>
                                </td>
                                <td><div class="td-text">라인</div>
                                	<span id ="SPAN_LINE"></span>
                                </td>
                                <td><div class="td-text">강종</div>
                                	<span id ="MRG_STEEL_TYPE"></span>
                                </td>
                                <td><div class="td-text">원재사</div>
                                    <span id ="MRG_R_SUPPLIER"></span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3"><div class="td-text">소재 (두께 x 폭)</div>
                                	<span id ="MRG_R_SUPPLY_THICKNESS"></span> X <span id ="MRG_R_WIDTH"></span> 
                                </td>
                                <td><div class="td-text">상태</div>
                                	<span id ="STATUS_NM"></span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                  </div>
                  <!-- ./ibox -->
            	</div>
                <!-- ./col -->
            </div>
            <!-- ./row -->
            
            <div class="row">
                <div class="col-sm-12">
                  <div class="ibox float-e-margins">
                    <div class="ibox-title">
                       	<h5>알림정보</h5>
                        <div class="ibox-tools pull-right">
                        <!-- 
            				<button type="button" class="btn btn-default btn-sm" onclick="javascript:syncCode();"><i class="fa fa-cloud-download"></i> 알림항목 동기화</button>
                         -->
          				</div>
                    </div>    
                    <div class="ibox-content">
       				 <form  id="frameForm" name="frameForm" class="form-horizontal">
            		     <input type="hidden" id="SEQ_Q100" name="SEQ_Q100"  value="<%= SEQ_Q100%>" />
            		     <input type="hidden" id="SEQ_TEMP" name="SEQ_TEMP"  value="<%= SEQ_TEMP%>" />
			        	<input type="hidden" id="FORM_CODE" name="FORM_CODE"  value="<%= FORM_CODE%>" />
			        	<input type="hidden" id="POC_NO01" name="POC_NO01"  value="<%= POC_NO01%>" />
			        	<input type="hidden" id="POC_NO02" name="POC_NO02"  value="<%= POC_NO02%>" />
			        	<input type="hidden" id="POC_NO03" name="POC_NO03"  value="<%= POC_NO03%>" />
						<input type="hidden" id="WORK_DATE" name="WORK_DATE"  value="<%= WORK_DATE%>" />
						<input type="hidden" id="IN_LINE" name="IN_LINE"   value="<%= IN_LINE%>" />
						<input type="hidden" id="FORM_TYPE" name="FORM_TYPE"   value="<%= FORM_TYPE%>" />
						<input type="hidden" id="CHECK_DATA" name="CHECK_DATA"   />
                    	<div id="stampList">
                    	</div>
                    	<!--  <div class="form-group">
            				<div class="col-sm-4">
            					<div class=""><label> <input type="checkbox"  id="chk"  name="chk"  value="현대&#xA;자동차" >  <span>현대자동차</span> </label></div>
            				</div>
            				<div class="col-sm-4">
            					<div class="i-checks"><label> <input type="checkbox"  id="chk"  name="chk"  value="강종&#xA;변경"> <span>강종변경</span> </label></div>
            				</div>
            				<div class="col-sm-4">
            					<div class="i-checks"><label> <input type="checkbox"  id="chk"   name="chk"  value="두께&#xA;변경" >  <span>두께변경</span> </label></div>
            				</div>
            			</div>
                    	<div class="form-group">
            				<div class="col-sm-4">
            					<div class="i-checks"><label> <input type="checkbox"  id="chk"  name="chk"  value="SPM&#xA;작업" >  <span>SPM 작업</span> </label></div>
            				</div>
            				<div class="col-sm-4">
            					<div class="i-checks"><label> <input type="checkbox"  id="chk"  name="chk"  value="SPM&#xA;미작업" > <span>SPM 미작업</span> </label></div>
            				</div>
            				<div class="col-sm-4">
            					&nbsp;
            				</div>
            			</div>-->
                    </form>    
                    </div>    
                    <!-- ./ibox-content -->
                    
                    <div class="ibox-footer"> 
            			<div class="row">
            			    <div class="col-xs-7">
		               			<button id="delete"  type="button" class="btn btn-default pull-left m-r-xs" onclick="javascipt:clearData();">삭제</button>
              				</div>
              				<div class="col-xs-5">
                  				<button id="print" type="button" class="btn btn-primary pull-right" onclick="javascript:createImport2();"><i class="fa fa-print"></i> 인쇄요청</button>
                  				<!-- 
                  				<button id="direct" type="button" class="btn pull-right m-r-xs" onclick="javascript:createWeb();"><i class="fa fa-edit"></i> 직접입력</button>
                  				 -->
              				</div>
            			</div>
                    </div>
                    
                  </div>
                  <!-- ./ibox -->
            	</div>
                <!-- ./col -->
            </div>
            <!-- ./row -->
            			<iframe id="download"   name="download"   frameborder="0" ></iframe>               
		</div>

</body>
</html>