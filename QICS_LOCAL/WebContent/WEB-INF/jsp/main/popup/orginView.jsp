<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 3 - 원본보기 및 출력</title>
<script type="text/javascript">

	$(function() {


	});
	$(document).ready(function() {			
		document.popUpForm.SEQ_Q100.value = document.mySheetForm.SEQ_Q100.value;
		document.popUpForm.SEQ_T300.value = document.mySheetForm.SEQ_T300.value;
		var result1 = ajaxCall("/View.do?cmd=viewUserBGInfo", $("#popUpForm").serialize(), false);  //작업내역 상세검색
		setUserBG(result1.DATA);
		 
	});
	function setUserBG(viewDataInfo){
		for(var i = 0; i<viewDataInfo.length; i++){ 
			if(i==0){
				$("#POC_NO").html('');
				$("#POC_NO").html('원본 보기 ( ' + viewDataInfo[i].POC_NO + ' )');
			}
				$("#bgImage").attr('src',url);
			var  url = "/ImagePS.do?BG_PATH="+viewDataInfo[i].P_BG_PATH.split("\\").join("\\\\")+"&P_BG="+viewDataInfo[i].P_BG;
			$("#bgImage").attr('src',url);
			
		}
	}
	function csImagePrint(){
		//callHostUI("imgprint", document.popUpForm.SEQ_T300.value);
		
		//var url = "/QICSImageDownload.do?SEQ_T300="+document.popUpForm.SEQ_T300.value;
		//callHostUI("defaultprint", seqT300);
		//window.frames["mainPenWork"].location.href=url;
 
			try {
				callHostUI("imgprint", document.popUpForm.SEQ_T300.value);
			} catch (e) {
					alert(e+"\n\n▣ QICS 전용브라우져를 사용해주시기 바랍니다.");
			}
	 
		alert("인쇄요청을 하였습니다.");
	}
</script>

</head>
<body class="hold-transition skin-blue layout-top-nav">
<form  id="popUpForm" name="popUpForm" class="form-horizontal">
	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100" />
	<input type="hidden" id="SEQ_T300" name="SEQ_T300" />
</form>
<div class="wrapper">

<!-- Popup Area -->
<div class="modal-dialog modal-xl">
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"  onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label id="POC_NO">원본 보기</label>
        <small class="pull-right m-r-lg" style="display: none;">
        	<span ></span> 
        </small>
      </h3>
      <span></span>
    </div>
    <!-- ./modal-header -->
    
    <div class="modal-body">

		<div class="revision-wrapper">
           	<div   class="img-shadow paper" style="" >
           		  <img id ="bgImage"  alt="image"> 
           	</div>
		</div>
              
    </div>
    <div class="modal-footer">
    	<div class="row">
        	<div class="col-xs-12">
           		<button type="button" class="btn pull-right" onclick="javascript:closePopUp('pop_up_banner');"> 닫기</button>
           		<button type="button" class="btn btn-primary pull-right m-r-xs" onclick="javascript:csImagePrint();"><i class="fa fa-print"></i> 인쇄하기</button>
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