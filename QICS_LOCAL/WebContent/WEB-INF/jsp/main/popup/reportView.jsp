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
		
		/*판정 체크 박스 생성*/
		var textJudgCheckbox = "";
		textJudgCheckbox += "<input type=\"checkbox\" id=\"CHK_JUDG_A\" style=\"position: absolute; top:-172px; left:110px\" /> ";
		textJudgCheckbox += "<input type=\"checkbox\" id=\"CHK_JUDG_B\" style=\"position: absolute; top:-172px; left:285px\" /> ";
		textJudgCheckbox += "<input type=\"checkbox\" id=\"CHK_JUDG_C\" style=\"position: absolute; top:-141px; left:150px\" /> ";
		textJudgCheckbox += "<input type=\"checkbox\" id=\"CHK_JUDG_D\" style=\"position: absolute; top:-172px; left:396px\" /> ";
		textJudgCheckbox += "<input type=\"checkbox\" id=\"CHK_JUDG_E\" style=\"position: absolute; top:-141px; left:270px\" /> ";
		textJudgCheckbox += "<input type=\"checkbox\" id=\"CHK_JUDG_F\" style=\"position: absolute; top:-141px; left:370px\" /> ";
		
		$("#judgArea").append(textJudgCheckbox);
		
		var judgResult = ajaxCall("/View.do?cmd=viewJudgInfo", $("#popUpForm").serialize(), false);  //판정 내용 추가
		setJudgValue(judgResult.DATA);
		
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
	/*판정 데이터*/
	function setJudgValue(judgResultInfo){
		
		var judgVal = judgResultInfo[0].USER_LAST_VALUE.split(",");
		var innerText = "";
		
		for(var i = 0; i<judgVal.length; i++){
			$("#CHK_JUDG_"+judgVal[i]).prop("checked", true);
		}
		
	}
	
	function deleteReport(){
		var result = ajaxCall("/Report.do?cmd=reportDelete", $("#popUpForm").serialize(), false);
		alert(result.Message);
		workList(document.popUpForm.SEQ_Q100.value);
		closePopUp('pop_up_banner');
		
		
	}
	function csImagePrint(){
 
			try {
				callHostUI("imgprint", document.popUpForm.SEQ_T300.value);
			} catch (e) {
				alert(e+"\n\n▣ QICS 전용브라우져를 사용해주시기 바랍니다.");
			}
 
		alert("인쇄요청을 하였습니다.");
	}
	function saveJudg(){
		var arr = ['A', 'B', 'C', 'D', 'E', 'F'];
		var str = "";
		
		for(var i=0; i<arr.length; i++){
			if($("input:checkbox[id='CHK_JUDG_" + arr[i] + "']").is(":checked")){
				if(str != ""){
					str += ","
				}
				str += arr[i]
			}
		}
		
		//판정데이터 A,B,C 형태로 C_JUDG에 저장
		$("#C_JUDG").val(str);
		var result = ajaxCall("/Save.do?cmd=saveJudg", $("#popUpForm").serialize(), false);  //판정데이터 저장
		alert(result.Message);	
		
	}
	
</script>

</head>
<body class="hold-transition skin-blue layout-top-nav">
<form  id="popUpForm" name="popUpForm" class="form-horizontal">
	<input type="hidden" id="FORM_CODE" name="FORM_CODE"  value="FORM_QICS_E"/>
	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100" />
	<input type="hidden" id="SEQ_T300" name="SEQ_T300" />
	<input type="hidden" id="VIEWONLY" name="VIEWONLY" />
	
	<input type="hidden" id="C_JUDG" name="C_JUDG" />
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
           	<div class="img-shadow paper" >
           		<img id="bgImage" alt="image">
           		<div id="judgArea" style="position: absolute;"></div>
           	</div>
		</div>
              
    </div>
    <div class="modal-footer">
    	<div class="row">
        	<div class="col-xs-6">
           		<button id="del_btn"  name="del_btn"  type="button" class="btn pull-left"  onclick="javascript:deleteReport();">삭제</button>
           	</div>
        	<div class="col-xs-6">
        		
           		<button type="button" class="btn pull-right" onclick="javascript:closePopUp('pop_up_banner');">닫기</button>
           		<button type="button" class="btn btn-primary pull-right m-r-xs" onclick="javascript:csImagePrint();"><i class="fa fa-print"></i> 인쇄하기</button>
           		<button type="button" id="my-button4"  class="btn btn-primary pull-right m-r-xs" onclick="javascript:saveJudg();"><i class="fa fa-edit"></i> 내용저장</button>
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