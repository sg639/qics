<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 4 -품질이상보고서 인쇄</title>

<script type="text/javascript">

	$(function() {


	});
	$(document).ready(function() {		
		document.popUpForm.POC_NO.value = document.mySheetForm.REPORT_POC.value;
		document.popUpForm.POC_NO01.value = document.mySheetForm.REPORT_POC01.value;
		document.popUpForm.POC_NO02.value = document.mySheetForm.REPORT_POC02.value;
		document.popUpForm.POC_NO03.value = document.mySheetForm.REPORT_POC03.value;
		document.popUpForm.SEQ_T300.value = document.mySheetForm.SEQ_T300.value;
		document.popUpForm.IN_LINE.value = document.mySheetForm.REPORT_LINE.value;
		document.popUpForm.SEQ_Q100.value = document.mySheetForm.SEQ_Q100.value;
		document.popUpForm.REPORT_DATE.value = document.mySheetForm.WORK_DATE.value;
		//alert($("#popUpForm").serialize());
		var result1 = ajaxCall("/Report.do?cmd=viewBGReport", $("#popUpForm").serialize(), false);
		setUserBG(result1.DATA);
	});
	function setUserBG(viewDataInfo){
		for(var i = 0; i<viewDataInfo.length; i++){ 
			var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].BG_NAME;
			$("#bgImage").attr('src',url);
		}
	}
	function goPrint5(){
		$("#print").hide();
		progressStart("양식생성에 필요한 정보를 수집중입니다.<br/>잠시만 기다려 주세요",function() {
			var result = ajaxCall("/Report.do?cmd=reportPrint", $("#popUpForm").serialize(), false);
			//alert(JSON.stringify(result));
			progressStop();
			var delayTime =0;
			var penWorkData =result.DATA;
			if(penWorkData.RESULT =="Y"){
					callHostUI("psprint", penWorkData.SEQ_T300);
				progressStart(penWorkData.FORM_NAME+'('+$("#POC_NO").val()+')을 생성중입니다.<br/>잠시만 기다려 주세요', 
						function() {
							progressStop();
							createSampleFormFinish(penWorkData); 
						}, delayTime
					);
			}
		});
		
	}
	function createSampleFormFinish(penWorkData){
		if(penWorkData.RESULT =="Y"){
			alert("양식생성을 성공했습니다.");
			changeMain(document.popUpForm.IN_LINE.value,document.popUpForm.REPORT_DATE.value,document.popUpForm.SEQ_Q100.value);

		}else{
			alert("양식생성에 실패했습니다.");
		}
		closePopUp('pop_up_banner');
	}	
</script>

</head>
<body class="hold-transition skin-blue layout-top-nav">
<div class="wrapper">
<form  id="popUpForm" name="popUpForm" class="form-horizontal">
	<input type="hidden" id="FORM_CODE" name="FORM_CODE"  value="FORM_QICS_E"/>
	<input type="hidden" id="FORM_TYPE" name="FORM_TYPE"  value="R"/>
	<input type="hidden" id="SEQ_T300" name="SEQ_T300" />
	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100" />
	<input type="hidden" id="POC_NO01" name="POC_NO01" />
	<input type="hidden" id="POC_NO02" name="POC_NO02" />
	<input type="hidden" id="POC_NO03" name="POC_NO03" />
	<input type="hidden" id="REPORT_DATE" name="REPORT_DATE" />
	<input type="hidden" id="IN_LINE" name="IN_LINE" />

<!-- Popup Area -->
<div class="modal-dialog modal-sm">
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>품질이상보고서 양식생성</label>
      </h3>
      <span></span>
    </div>
    <!-- ./modal-header -->
    
    <div class="modal-body">
    	<div class="modal-paper">
        
        	<div class="row">
                <div class="col-sm-12">
                  <div class="ibox m-b-xs">
                  
                    <div class="ibox-content"> 
                   		<div class="row">
                			<div class="col-lg-12">
           						<div class="paper-sm">
           							<img   id="bgImage"  alt="image">
           						</div>
                			</div>
            			</div>           
           				<div class="m-t-lg">
                    		<h3 class="text-navy">품질이상보고서</h3>
                        	<hr>
                        </div>
                        <div class="row">
                        	<div class="col-sm-12">
            					<div class="form-group m-b-none">
            						<label class="control-label">POC No.</label>
            						<input type="text" id="POC_NO" name="POC_NO"  class="form-control" >
                        		</div>
                     		</div>
                    	</div>
                    </div>
                    <div class="ibox-footer"> 
    					<div class="row">
							<div class="col-xs-12">
								<button type="button" id="print"  class="btn pull-right" onclick="javascript:goPrint5();"><i class="fa fa-print"></i> 인쇄요청</button>
							</div>
						</div> 
                    </div>
                    
                  </div>
                  <!-- ./ibox -->
            	</div>
            	<!-- ./col -->
            </div>
            <!-- ./row -->
    	
		</div>        
    </div>
    <!-- ./modal-body-->
    
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->
</form>
</div>

</body>
</html>