<%---------------------------------------- 화면 라이브러리 ----------------------------------------%>
<%@ taglib uri="http://www.springframework.org/tags" 		prefix="spring" %>
<%-- <%@ taglib uri="http://www.springframework.org/tags/form" 	prefix="form" %> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 			prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" 			prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" 	prefix="fn" %>
<%-- <%@ taglib uri="http://www.anyframejava.org/tags" 			prefix="anyframe" %> --%>

<%---------------------------------------- 화면 공통 변수 ----------------------------------------%>
<c:set var="sessionScope"	scope="session" />
<c:set var="packageName"	value="com.hr"/>
<c:set var="ctx" 			value="${pageContext.request.contextPath}"/>
<c:set var="localeResource" value="${ctx}/common/" />
<c:set var="contextRequestUrl">${pageContext.request.requestURL}</c:set>
<c:set var="baseURL" 		value="${fn:replace(contextRequestUrl, pageContext.request.requestURI, pageContext.request.contextPath)}" />
<%----------------------------------- 1a 개발확인용으로만  -----------------------------------%>
<c:set var="hostIp" 		value="${pageContext.request.remoteHost}"/>

<c:set var="hrm"		value="/hrfile/${sessionScope.ssnEnterCd}/hrm/" />
<c:set var="hri"		value="/hrfile/${sessionScope.ssnEnterCd}/hri/" />
<c:set var="cpn"		value="/hrfile/${sessionScope.ssnEnterCd}/cpn/" />
<c:set var="mail"		value="/hrfile/${sessionScope.ssnEnterCd}/mail/" />

<%-- <c:set var="datePattern"><fmt:message key="date.format"></fmt:message></c:set> --%>
<%-- <c:set var="imagesResource" value="${sessionScope.userSession.fsDomain}/resource/images" /> --%>

<%---------------------------------------- 권한 설정 ----------------------------------------%>

<c:set var="authPg"			value="${param.authPg}" />

<%---------------------------------------- 팝업 설정[parent,top] ----------------------------------------%>
<c:set var="popUpStatus"  value="parent" />

<%---------------------------------------- 다국어 언어 설정 ----------------------------------------%>

<fmt:setLocale value="${language}" scope="session"/>

<%-------------------------------------- IBSheet 공통 필드 설정 ----------------------------------------%>

<c:set var="dataReadCnt"  value="22" />
<c:set var="sNoTy"  value="Seq" 	/><c:set var="sNoHdn"  value="0" /><c:set var="sNoWdt"  value="45" />

<c:choose>
	<c:when test="${authPg == 'A'}">
		<c:set var="textCss"	value="text" />
		<c:set var="dateCss"  	value="date2" />
		<c:set var="readonly" 	value="" />
		<c:set var="disabled" 	value="" />		
		<c:set var="editable" 	value="1" />
	
		<c:set var="sDelTy" value="DelCheck"/><c:set var="sDelHdn" value="0" /><c:set var="sDelWdt" value="45" />
		<c:set var="sRstTy" value="Result" 	/><c:set var="sRstHdn" value="1" /><c:set var="sRstWdt" value="0" />
		<c:set var="sSttTy" value="Status" 	/><c:set var="sSttHdn" value="0" /><c:set var="sSttWdt" value="45" />
	</c:when>
	<c:otherwise>
		<c:set var="textCss" 	value="text transparent" />
		<c:set var="dateCss" 	value="text transparent" />
		<c:set var="readonly" 	value="readonly" />
		<c:set var="disabled" 	value="disabled" />		
		<c:set var="editable" 	value="0" />

		<c:set var="sDelTy" value="DelCheck"/><c:set var="sDelHdn" value="1" /><c:set var="sDelWdt" value="0" />
		<c:set var="sRstTy" value="Result" 	/><c:set var="sRstHdn" value="1" /><c:set var="sRstWdt" value="0" />
		<c:set var="sSttTy" value="Status" 	/><c:set var="sSttHdn" value="1" /><c:set var="sSttWdt" value="0" />
	</c:otherwise>
</c:choose>			



<%---------------------------------------- Jquery Tab  ----------------------------------------%>

<c:set var="maxTabCnt"  value="6" 	/>

<%---------------------------------------- JSTL Date  ----------------------------------------%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="curSysYyyyMMdd"><fmt:formatDate value="${now}" pattern="yyyyMMdd" /></c:set> 
<c:set var="curSysYear">	<fmt:formatDate value="${now}" pattern="yyyy" /></c:set> 
<c:set var="curSysMon">		<fmt:formatDate value="${now}" pattern="MM" /></c:set> 
<c:set var="curSysDay">		<fmt:formatDate value="${now}" pattern="dd" /></c:set>
<c:set var="curSysYyyyMMddHyphen"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd" /></c:set>
<c:set var="curSysYyyyMMHyphen"><fmt:formatDate value="${now}" pattern="yyyy-MM" /></c:set>