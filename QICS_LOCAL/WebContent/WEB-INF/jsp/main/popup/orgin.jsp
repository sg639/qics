<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/include/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/include/meta.jsp"%>
<title>작업추가</title>
<base target="_self" />
<script type="text/javascript">

	$(function() {


	});
	$(document).ready(function() {				
		var result = ajaxCall("/Orgin.do?cmd=orginInfo", $("#popUpForm").serialize(), false);
		alert(JSON.stringify(result));
		 
	});
	function orginPrint(){
		var result = ajaxCall("/Orgin.do?cmd=orginPrint", $("#popUpForm").serialize(), false);
		alert(JSON.stringify(result));
	}
</script>

</head>
<body>
<div style="width: 991px; height: 760px;background: white;">
	<div>
	<ul>
		<li>원본보기</li>
		<li class="close"></li>
	</ul>
	</div>
	
	<div>
		<form id="popUpForm" name="popUpForm">
                <input type="hidden" id="ZJISA" name="ZJISA" value=""/>
                <input type="hidden" id="localId" name="localId"  value="Orgin"/>
                  <div class="sheet_search outer">
                    <div>

                    </div>
                </div>
		</form>
		<div>
				<div>
				<ul>
					<li id="txt" class="txt">원본보기</li>
				</ul>
				</div>
			</div>
			<img alt="" src="/common/images/FORM_ASIANA_DEMO_R01_1_200.png"  style="width: 490px; height: 693px;">
	        <div>
	            <ul>
	                <li>
						<a href="javascript:orginPrint();" class="gray large">인쇄하기</a><a href="javascript:closePopUp('pop_up_banner');" class="gray large">닫기</a>
	                </li>
	            </ul>
	        </div>				
		
	</div>
</div>

</body>
</html>