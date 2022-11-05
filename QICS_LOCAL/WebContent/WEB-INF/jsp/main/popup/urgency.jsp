<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html id="wrapPop">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>HYUNDAI BNG STEEL :: 팝업화면 7 - 긴급작업</title>
<base target="_self" />

<script type="text/javascript">
 

	$(document).ready(function() {	
		$("#POC_NO").css({"ime-mode":"disabled","text-transform":"uppercase"});
		
		
		/*
		$("#POC_NO").keydown(function(event){
		 	event = event || window.event;
			var keyID = (event.which) ? event.which : event.keyCode;
			alert(this.id + "::"+ keyID);
			// 48~57:숫자, 96~105:숫자패드, 8:backspace, 9:tab, 46:Del, 37:<-, 39:->, 190:. 110: 숫자키패드. shift:16, 186: :; 65~90:A~Z 97~122:a~z - : 189,45
			// 일자
			// 숫자+영문 C_POC_NO01_POD C_POC_NO02_POD C_POC_NO03_POD C_PP C_LV C_ALV C_SKIN C_STP_TOP 
			// 이름 C_CHKER
			//alert((/C_DEF_[ST|ED|PT]/).test(this.id) );
			
				if ( (keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105) || (keyID >= 65 && keyID <= 90) || (keyID >= 97 && keyID <= 122) 
						|| keyID == 8 || keyID == 16 || keyID == 46 || keyID == 37 || keyID == 39 || keyID == 189 || keyID == 45) { 
					return;
				 } else {
					return false;
				 }				
			
		 	
	});*/
		document.popUpForm.IN_LINE.value = document.mySheetForm.IN_LINE.value;
		document.popUpForm.WORK_DATE.value = document.mySheetForm.WORK_DATE.value;
		$("#lineNm").html(document.popUpForm.IN_LINE.value+" 검사결과표"); 
		var result = ajaxCall("/Process.do?cmd=processList", $("#popUpForm").serialize(), false); //작업공정 I/F 조회
		 createSelect(result);
	});
	function createSelect(result){
	  var str="<option value=''>전체 </option>";
	  var mStr ="";
	  var fStr="";
	  if(result.DATA.length > 0 ){
			for(var i = 0; i<result.DATA.length; i++){
				if(document.popUpForm.IN_LINE.value == result.DATA[i].CODE){
					document.popUpForm.IN_FACT.value =result.DATA[i].ATTRIBUTE2;
					document.popUpForm.IN_ORDER.value = result.DATA[i].ATTRIBUTE1;
					document.popUpForm.FORM_CODE.value = result.DATA[i].FORM_CODE;
					document.popUpForm.FORM_TYPE.value = result.DATA[i].FORM_TYPE;
				}
			}
	  }
	  var result1 = ajaxCall("/Report.do?cmd=viewBGReport", $("#popUpForm").serialize(), false);
		setUserBG(result1.DATA);
	//  $("#finalList").html(fStr);  
	}
	function setUserBG(viewDataInfo){
		for(var i = 0; i<viewDataInfo.length; i++){ 
			var  url = "/ImageBG.do?FORM_ID="+viewDataInfo[i].FORM_ID+"&BG_NAME="+viewDataInfo[i].BG_NAME;
			$("#bgImage").attr('src',url);
		}
	}
	function urgencyAdd(){
		document.popUpForm.POC_NO.value = $.trim( document.popUpForm.POC_NO.value.toUpperCase() ) ;
		
		if(document.popUpForm.POC_NO.value != null && document.popUpForm.POC_NO.value != ""  ){
			
			
			var tempStr = document.popUpForm.POC_NO.value.split("-");
			if(tempStr.length>=2) {
				for(var i=0; i<tempStr.length; i++){
					var tempValue=tempStr[i];
					
					if(i == 0 && ( tempValue.length<9 || tempValue.length>9 ) ){
						alert("POC No 번호를 확인해주세요.\n\n - POC No 첫9자리가 올바르지 않습니다.");
						return;
					}else if(i == 1 && ( tempValue.length<4 || tempValue.length>4 ) ){
						alert("POC No 번호를 확인해주세요.\n\n - POC No 두번째 4자리가 올바르지 않습니다.");
						return;
					}else if(i == 2 && ( tempValue.length<3 || tempValue.length>3 ) ){
						alert("POC No 번호를 확인해주세요.\n\n - POC No 세번째 3자리가 올바르지 않습니다.");
						return;
					}else if(i >= 3 ){
						alert("POC No 번호를 확인해주세요.\n\n - POC No는 9자리-4자리 이거나 9자리-4자리-3자리 형태이어야 합니다.");
						return;
					}
	
				
				}
			} else {
				alert("POC No 번호를 확인해주세요.\n\n - POC No는 9자리-4자리 이거나 9자리-4자리-3자리 형태이어야 합니다.");
				return;
			}
			
			
			for(var i=0; i<tempStr.length; i++){
				var tempValue=tempStr[i];
				if(tempValue != null  && tempValue != "" ){
					if(i == 0){
						document.popUpForm.POC_NO01.value =tempValue;
					}else if(i == 1){
						document.popUpForm.POC_NO02.value =tempValue;
					}else if(i == 2){
						document.popUpForm.POC_NO03.value =tempValue;
					}
				}
			}
			var result = ajaxCall("/Urgency.do?cmd=saveUrgency", $("#popUpForm").serialize(), false);
			if(result.Message.indexOf("저장") > -1){
				alert(result.Message);
			}else{
				var message = "QICS작업목록 추가 결과는 아래와 같습니다.\n해당 POC번호를 다시 확인바랍니다.\n\n 실패 : ※ 이미 등록된 POC번호 입니다.\n POC_NO : "+ result.Message;
				alert(message);
			}
			
			changeMain(document.popUpForm.IN_LINE.value,document.popUpForm.WORK_DATE.value,result.SEQ_Q100)
		}else{
			alert("POC NO는 필수 입력값입니다.")
		}
		
	}
	 
	 
	 
</script>

</head>
<body>
<div class="wrapper">
<form  id="popUpForm" name="popUpForm" class="form-horizontal">
	<input type="hidden" id="IN_LINE"  name="IN_LINE" />
	<input type="hidden" id="IN_FACT"  name="IN_FACT" />
	<input type="hidden" id="IN_ORDER"  name="IN_ORDER" />
	<input type="hidden" id="WORK_DATE"  name="WORK_DATE" />
	<input type="hidden" id="FORM_CODE" name="FORM_CODE" />
	<input type="hidden" id="FORM_TYPE" name="FORM_TYPE" />
	<input type="hidden" id="POC_NO01" name="POC_NO01" />
	<input type="hidden" id="POC_NO02" name="POC_NO02" />
	<input type="hidden" id="POC_NO03" name="POC_NO03" />
	<input type="hidden" id="URGENCY_YN" name="URGENCY_YN"  value="Y"/>

<!-- Popup Area -->
<div class="modal-dialog modal-sm">
  <div class="modal-content">  
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" onclick="javascript:closePopUp('pop_up_banner');">×</button> 
      <h3 class="modal-title">
        <label>긴급작업</label>
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
                    		<h3 id="lineNm"  name="lineNm"  class="text-navy"></h3>
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
								<!--  <button type="button" id="print"  class="btn pull-right" onclick="javascript:goPrint();"><i class="fa fa-print"></i> 인쇄요청</button>-->
								<button id="my-button1" type="button" class="btn btn-info btn-sm pull-right" onclick="javascript:urgencyAdd();"><i class="fa fa-plus"></i> 작업추가</button>
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