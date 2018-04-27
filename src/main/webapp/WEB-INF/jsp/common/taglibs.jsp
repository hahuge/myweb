<%@ page import="com.hahuge.myweb.poi.helper.Conf" %>
<%@ page import="com.hahuge.myweb.poi.helper.Constants" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="SERVER" value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}" />
<c:set var="basepath" value="${SERVER}${pageContext.request.contextPath}" />
<!DOCTYPE html>
