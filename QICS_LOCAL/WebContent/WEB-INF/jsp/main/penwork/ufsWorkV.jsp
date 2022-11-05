<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<%
String SEQ_Q100 = request.getParameter("SEQ_Q100") == null ? "" : request.getParameter("SEQ_Q100");
String SEQ_T300 = request.getParameter("SEQ_T300") == null ? "" : request.getParameter("SEQ_T300");
String WEB_DATA_YN = request.getParameter("WEB_DATA_YN") == null ? "" : request.getParameter("WEB_DATA_YN");
String IN_LINE = request.getParameter("IN_LINE") == null ? "" : request.getParameter("IN_LINE");
String FORM_TYPE = request.getParameter("FORM_TYPE") == null ? "" : request.getParameter("FORM_TYPE");
String E_STATUS = request.getParameter("E_STATUS") == null ? "" : request.getParameter("E_STATUS");
String FORM_CODE = request.getParameter("FORM_CODE") == null ? "" : request.getParameter("FORM_CODE");
%>

<!DOCTYPE html>
<html id="wrapSub">
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
  <title>HYUNDAI BNG STEEL :: UFS 이전데이터 화면</title>
 
 <%@ include file="/WEB-INF/jsp/common/include/jqueryScript.jsp"%>
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
	var result1 = ajaxCall("/View.do?cmd=viewBGInfo", $("#frameForm").serialize(), false);  //작업내역 상세검색
	setBG(result1.DATA);
	
	parent.goOtherLine();
});

function setBG(viewDataInfo){

	for(var i = 0; i<viewDataInfo.length; i++){ 
		var  url = "/ImagePS.do?BG_PATH="+viewDataInfo[i].P_BG_PATH.split("\\").join("\\\\")+"&P_BG="+viewDataInfo[i].P_BG;
		$("#bgImage").attr('src',url);
		  
	}
	
	
}

</script>
<body>
        <form  id="frameForm" name="frameForm" class="form-horizontal">
                	<input type="hidden" id="SEQ_Q100" name="SEQ_Q100"  value="<%= SEQ_Q100%>" />
                	<input type="hidden" id="SEQ_T300" name="SEQ_T300"  value="<%= SEQ_T300%>" />
                	<input type="hidden" id="IN_LINE" name="IN_LINE"  value="<%= IN_LINE%>" />
                	<input type="hidden" id="WEB_DATA_YN" name="WEB_DATA_YN"  value="<%= WEB_DATA_YN%>" />
                	<input type="hidden" id="FORM_TYPE" name="FORM_TYPE"  value="<%= FORM_TYPE%>" />
                	<input type="hidden" id="E_STATUS" name="E_STATUS"  value="<%= E_STATUS%>" />
                	<input type="hidden" id="FORM_CODE" name="FORM_CODE"  value="<%= FORM_CODE%>" />
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
 
        <div class="wrapPaper">
			<div class="revision-wrapper">
            	<div class="img-shadow paper">
            		<img id="bgImage" src="" alt="image">
            	</div>
            	<div id='subImage' ></div>
            	<div id="viewer" class="data-area"></div>
			</div>

            <div class="btn-wrapper">
            	<div class="row">
		        	<div class="col-xs-6">
		           	<!-- 		<button id="del_btn"  name="del_btn"  type="button" class="btn pull-left">삭제</button> -->
		           	</div>
		        	<div class="col-xs-6">
		           	<!-- 	<button type="button" class="btn pull-right" onclick="javascript:closePopUp('pop_up_banner');">닫기</button> -->
		           		<button type="button" class="btn btn-primary pull-right m-r-xs"><i class="fa fa-print"></i> 인쇄하기</button>
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