                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 <%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%
	String code = request.getParameter("code");
	String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ISU System</title>
<script src="${ctx}/common/js/jquery/1.8.3/jquery.min.js"></script>
<link rel="stylesheet" href="/common/theme1/css/style.css" />
<script type="text/javascript">
$(function(){
	var code = "${code}";
	if(parent.opener){
		if(code == "905"){
			self.close();
			redirect("/Login.do");
		}else{
			$("#title").html(code);
			var contents = "";
			contents +="<br/>Code      : ${requestScope['javax.servlet.error.status_code']}";
			contents +="<br/>Exception_type : ${requestScope['javax.servlet.error.exception_type']}";
			contents +="<br/>Message : ${requestScope['javax.servlet.error.message']}";
			contents +="<br/>Exception : ${requestScope['javax.servlet.error.exception']}";
			contents +="<br/>Request_uri : ${requestScope['javax.servlet.error.request_uri']}";
			
			contents +="??????????????? ??????????????????.";
			$("#contents").html(contents);
			$("#error_main").show();
			redirect("/Main.do");
		}
	}else{
		if(code == "905"){
			redirect("/Login.do");
		}else if(code == "906"){
			redirect("/Main.do");	
		}else{
			$("#title").html(code);
			var contents = "";
			//contents +="<br/>Message : ${message}";
			//contents +="<br/>Request_uri : ${requestScope['javax.servlet.error.request_uri']}";
			contents +="<br/>Code      : ${requestScope['javax.servlet.error.status_code']}";
			contents +="<br/>Exception_type : ${requestScope['javax.servlet.error.exception_type']}";
			contents +="<br/>Message : ${requestScope['javax.servlet.error.message']}";
			contents +="<br/>Exception : ${requestScope['javax.servlet.error.exception']}";
			contents +="<br/>Request_uri : ${requestScope['javax.servlet.error.request_uri']}";
			
			contents +="??????????????? ??????????????????.";
			$("#contents").html(contents);
			$("#error_main").show();
		}
	}
	
    var result = "";
    
    $.each(jQuery.browser, function(i, val) {
        result += i + ":" + val + "\n";
    });	
   
    //result = result +"</br>"+ document.referrer;
    
    

	// ???????????? ?????? ?????????
	$("#logout").click(function(){
		var url = "/logoutUser.do";    
		$(location).attr('href',url);
	});
    
	$("#userInfo").html(result);
	
});


function redirect(url) {
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){
        var referLink = document.createElement('a');
        referLink.href = url;
        document.body.appendChild(referLink);
        referLink.click();
    } else {
		$(location).attr('href',"/logoutUser.do");
        //location.href = url;
    }
}

</script>
<body>
<div id="error_main" class="error_main" style="display:none">
	<div class="body">
		<div id="header" class="header">Information <span></span></div>
		<div id="title"  class="title"></div>
		<div id="contents"  class="contents"></div>
		<div id="userInfo"  class="userInfo"></div>
		
		<!-- 
		<div>
			IE7 ????????? ??????????????? ???????????? ?????? ??? ????????? IE 8 ????????? ???????????????.<br />
			??????????????? ??????????????? ????????????  "<a href="#" id="logout">????????????</a>" ??? ???????????? ????????? ????????? ????????????.<br />
			????????? ????????? ?????? <div id="contents"  class="contents"></div>
		</div>
		 -->
		<div class="bottom">
			<b><!--  ????????? ???????????????--></b><br />
<!--  			02 - 2030 -  0387	-->
		</div>
	</div>
</div>
</body>
</html>