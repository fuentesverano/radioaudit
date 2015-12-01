<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<title>Log - ${radioLogModel.name}</title>
<script src="js/jquery-1.9.1.js" type="text/javascript"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="js/jquery-sortable.js"></script>
<script src="js/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">

<style>
.logTable {
	overflow: auto;
	margin: 0px;
	padding: 0px;
	width: 100%;
	border: 1px solid #ffffff;
	-moz-border-radius-bottomleft: 10px;
	-webkit-border-bottom-left-radius: 10px;
	border-bottom-left-radius: 10px;
	-moz-border-radius-bottomright: 10px;
	-webkit-border-bottom-right-radius: 10px;
	border-bottom-right-radius: 10px;
	-moz-border-radius-topright: 10px;
	-webkit-border-top-right-radius: 10px;
	border-top-right-radius: 10px;
	-moz-border-radius-topleft: 10px;
	-webkit-border-top-left-radius: 10px;
	border-top-left-radius: 10px;
}

.logTable table {
	border-collapse: collapse;
	border-spacing: 0;
	width: 100%;
	height: 100%;
	margin: 0px;
	padding: 0px;
	border-collapse: collapse;
}

.logTable tr:last-child td:last-child {
	-moz-border-radius-bottomright: 10px;
	-webkit-border-bottom-right-radius: 10px;
	border-bottom-right-radius: 10px;
}

.logTable table tr:first-child td:first-child {
	-moz-border-radius-topleft: 10px;
	-webkit-border-top-left-radius: 10px;
	border-top-left-radius: 10px;
}

.logTable table tr:first-child td:last-child {
	-moz-border-radius-topright: 10px;
	-webkit-border-top-right-radius: 10px;
	border-top-right-radius: 10px;
}

.logTable tr:last-child td:first-child {
	-moz-border-radius-bottomleft: 10px;
	-webkit-border-bottom-left-radius: 10px;
	border-bottom-left-radius: 10px;
}

.logTable tr:hover td {
	background-color: #cccccc;
}

.logTable td {
	vertical-align: middle;
	background-color: #cccccc;
	border: 1px solid #ffffff;
	border-width: 0px 1px 1px 0px;
	text-align: center;
	padding: 10px;
	font-size: 12px;
	font-family: Arial;
	font-weight: normal;
	color: #000000;
}

.logTable tr:last-child td {
	border-width: 0px 1px 0px 0px;
}

.logTable tr td:last-child {
	border-width: 0px 0px 1px 0px;
}

.logTable tr:last-child td:last-child {
	border-width: 0px 0px 0px 0px;
}

.logTable tr:first-child td {
	background: -o-linear-gradient(bottom, #191919 5%, #b2b2b2 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #191919
		), color-stop(1, #b2b2b2));
	background: -moz-linear-gradient(center top, #191919 5%, #b2b2b2 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#191919",
		endColorstr="#b2b2b2");
	background: -o-linear-gradient(top, #191919, b2b2b2);
	background-color: #191919;
	border: 0px solid #ffffff;
	text-align: center;
	border-width: 0px 0px 1px 1px;
	font-size: 15px;
	font-family: Arial;
	font-weight: bold;
	color: #ffffff;
}

.logTable tr:first-child:hover td {
	background: -o-linear-gradient(bottom, #191919 5%, #b2b2b2 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #191919
		), color-stop(1, #b2b2b2));
	background: -moz-linear-gradient(center top, #191919 5%, #b2b2b2 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#191919",
		endColorstr="#b2b2b2");
	background: -o-linear-gradient(top, #191919, b2b2b2);
	background-color: #191919;
}

.logTable tr:first-child td:first-child {
	border-width: 0px 0px 1px 0px;
}

.logTable tr:first-child td:last-child {
	border-width: 0px 0px 1px 1px;
}

#sortable1,#sortable2 {
	list-style-type: none;
	margin: 0;
	padding: 0 0 2.5em;
	float: left;
	margin-right: 10px;
}

#sortable1 li,#sortable2 li {
	margin: 0 5px 5px 5px;
	padding: 5px;
	font-size: 1.2em;
	width: auto;
}
</style>

</head>

<body>
	<jsp:include page="header.jsp" flush="true" />

	<div class="bodyContent"
		style="margin-left: 1%; margin-right: 1%; padding: 1%;">
		<div class="radioInfo" style="height: 20%; width: 50%;">
			<table>
				<tbody>
					<tr>
						<td style="width: 20%;"><a
							href="/radioAudit/showRadioInfo.html?radioCode=${radioLogModel.code}">
								<img class="cover" src="img/button/back.png"
								title="${userRadio.name}" alt="${userRadio.name}" width="70"
								height="70">
						</a></td>
						<td style="width: 20%;"><img class="cover"
							src="img/radio/${radioLogModel.code}.gif"
							title="${radioLogModel.name}" alt="${radioLogModel.name}"
							width="100" height="100" style="float: right;"></td>
						<td style="width: 40%; padding-top: 2%;">
							<h3 style="font-family: monospace; font-size: 23px;">
								<c:out value="${radioLogModel.name}" />
							</h3>
						</td>
						<td style="width: 10%;"><a href="${radioLogModel.website}"
							TARGET="_new"
							style="font: cursive; font-size: 15px; color: blue;"><img
								class="cover" src="img/website/website_1.jpg" title="Sitio Web"
								alt="Sitio Web" width="50" height="50"></a></td>
						<td style="width: 10%;"><c:choose>
								<c:when test="${radioLogModel.format == 'MPEG'}">
									<img class="cover" src="img/format/mp3.png" width="40"
										height="40">
								</c:when>
								<c:otherwise>
									<img class="cover" src="img/format/aac.png" width="40"
										height="40">
								</c:otherwise>
							</c:choose></td>
						<td style="width: 10%;"><c:choose>
								<c:when test="${radioLogModel.active}">
									<img class="cover" src="img/state/green_ball.jpg"
										title="Radio Activa" alt="Radio Activa" width="45" height="45">
								</c:when>
								<c:otherwise>
									<img class="cover" src="img/state/red_ball.png"
										title="Radio Inactiva" alt="Radio Inactiva" width="45"
										height="55">
								</c:otherwise>
							</c:choose></td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="clearBox"></div>

		<div class="logTable"
			style="height: 50%; width: 80%; padding-left: 10%">
			<c:choose>
				<c:when test="${fn:length(radioLogModel.coincidences) > 0}">
					<table>
						<tbody>
							<tr>
								<td>Nombre</td>
								<td>Fecha</td>
								<td>Porcentaje</td>
								<td>Similitud</td>
							</tr>
							<c:forEach items="${radioLogModel.coincidences}"
								var="coincidence" varStatus="varStatus">
								<tr>
									<td>${coincidence.name}</td>
									<td><fmt:formatDate value="${coincidence.date}"
											pattern="dd/MM/yyyy HH:mm:ss" /></td>
									<td>${coincidence.matchPercent}</td>
									<td>${coincidence.fingerprintSimilarity}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<span class="title"
						style="font-size: 25; font-family: monospace; font-weight: bold;">No
						hay coincidencias</span>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>