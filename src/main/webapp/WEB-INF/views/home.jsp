<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html lang="es">
<head>
<title> Radioaudit.com</title>
<link href="/resources/img/favicon.ico" rel="icon" type="image/x-icon" />

<link href="/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link href="/resources/css/home.css" rel="stylesheet">
<script src="/resources/js/general-functions.js" type="text/javascript"></script>
<script src="/resources/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<link href="/resources/css/page-header.css" rel="stylesheet">
</head>

<script src="/resources/js/jquery-1.9.1.js" type="text/javascript"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="/resources/js/jquery-1.9.1.js"></script>
<script src="/resources/js/jquery-ui.js"></script>

<script>
	
</script>

</head>

<body>

	<div id="container" class="container">

		<c:set var="username" value="ffuentes" scope="request" />
		<jsp:include page="header.jsp" flush="true">
			<jsp:param name="username" value="${username}" />
		</jsp:include>

		<div id="activeRadios">
			<div>
				<h2>
					<i><b>Radios activas - </b></i> <img
						src="/resources/img/listen.png" width="50" height="50">
				</h2>
			</div>
			<ul id="sortable">
				<c:forEach items="${userRadios}" var="userRadio"
					varStatus="varStatus">
					<li style="width: 100px; height: 100px;"><a
						href="/showRadioInfo.html?radioCode=${userRadio.code}"> <img
							class="cover" src="/resources/img/radio/${userRadio.code}.gif"
							title="${userRadio.name}" alt="${userRadio.name}" width="90"
							height="90">
					</a></li>
				</c:forEach>
			</ul>
		</div>

		<div id="deactivateRadios">
			<h2>
				<i><b>Todas las radios - </b></i> <img
					src="/resources/img/silence.png" width="50" height="50">
			</h2>
			<ul id="sortable">
				<c:forEach items="${homeModel.userRadios}" var="userRadio"
					varStatus="varStatus">
					<li style="width: 70px; height: 70px;"><a
						href="/showRadioInfo.html?radioCode=${userRadio.code}"> <img
							class="cover" src="/resources/img/radio/${userRadio.code}.gif"
							title="${userRadio.name}" alt="${userRadio.name}" width="60"
							height="60">
					</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
</html>