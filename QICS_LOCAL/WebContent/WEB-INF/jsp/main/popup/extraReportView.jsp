<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 4 - 품질이상보고서 보기 및 출력</title>

<script type="text/javascript">

	$(document).ready(function() {		
		document.popUpForm.SEQ_Q100.value = document.mySheetForm.SEQ_Q100.value;
		 
	 
		try{
			if(document.mySheetForm.VIEWONLY.value=='Y') {
				$("#del_btn").hide();
			}
		}catch(e){
			//alert(e);
			
		}
		
		var result1 = ajaxCall("/Report.do?cmd=viewResultBGReport", $("#popUpForm").serialize(), false);
		setUserBG(result1.DATA);
	});
	function setUserBG(viewDataInfo){
		for(var i = 0; i<viewDataInfo.length; i++){ 
			document.popUpForm.SEQ_T300.value=viewDataInfo[i].SEQ_T300;
			var  url = "/ImagePS.do?BG_PATH="+viewDataInfo[i].P_BG_PATH.split("\\").join("\\\\")+"&P_BG="+viewDataInfo[i].P_BG;
			$("#bgImage").attr('src',url);
			//$("#RP_POC_NO").html("품질이상보고서 ( "+viewDataInfo[i].POC_NO+" )");
			$("#RP_POC_NO").html("품질이상보고서 보기");
			
		}
	}
	function deleteReport(){

		
	}
	function popupCSImagePrint(){
		try{
			top.window.opener.top.goPrintByPopUp(document.popUpForm.SEQ_T300.value);
			alert("인쇄요청을 하였습니다.");
		} catch(e) {
			var url = "/QICSImageDownload.do?SEQ_T300="+document.popUpForm.SEQ_T300.value;
			top.window.frames["mainPenWork"].location.href=url;
		}
	}
</script>

</head>
<body class="hold-transition skin-blue layout-top-nav">
<form  id="popUpForm" name="popUpForm" class="form-horizontal">
	<input type="hidden" id="FORM_CODE" name="FORM_CODE"  value="FORM_QICS_E"/>
	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100" />
	<input type="hidden" id="SEQ_T300" name="SEQ_T300" />
	<input type="hidden" id="VIEWONLY" name="VIEWONLY" />
</form>
<div class="wrapper">

<!-- Popup Area -->
<div class="modal-dialog modal-xl">
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label><span id="RP_POC_NO"></span> </label>
      </h3>
      <span></span>
    </div>
    <!-- ./modal-header -->
    
    <div class="modal-body">

		<div class="revision-wrapper">
           	<div class="img-shadow paper" style="">
           		<img id="bgImage" alt="image">
           	</div>
		</div>
              
    </div>
    <div class="modal-footer">
    	<div class="row">
        	<div class="col-xs-6">
        	<!-- 
           		<button id="del_btn"  name="del_btn"  type="button" class="btn pull-left"  onclick="javascript:deleteReport();">삭제</button>
           		 -->
           	</div>
        	<div class="col-xs-6">
           		<button type="button" class="btn pull-right" onclick="javascript:closePopUp('pop_up_banner');">닫기</button>
           		<button type="button" class="btn btn-primary pull-right m-r-xs" 	onclick="javascript:popupCSImagePrint();"><i class="fa fa-print"></i> 인쇄하기</button>
           	</div>
        </div> 
    </div>
    <!-- ./modal-body-->
    
  </div>
  <!-- ./modal-content -->
</div>
<!-- ./modal-dialog -->

</div>

</body>
</html>