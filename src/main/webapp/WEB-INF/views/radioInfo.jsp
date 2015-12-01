<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html lang="es">

<head>
<title> Radioaudit.com</title>
<link href="/resources/img/favicon.ico" rel="icon" type="image/x-icon" />

<script src="/resources/js/jquery-1.9.1.js" type="text/javascript"></script>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="/resources/js/jquery-sortable.js"></script>
<script src="/resources/js/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">
<!-- Bootstrap core CSS -->
<link href="/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="/resources/css/page-header.css" rel="stylesheet">
</head>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<style>
#sortable1, #sortable2 {
	list-style-type: none;
	margin: 0;
	padding: 0 0 2.5em;
	float: left;
	margin-right: 10px;
}

#sortable1 li, #sortable2 li {
	margin: 0 5px 5px 5px;
	padding: 5px;
	font-size: 1.2em;
	width: auto;
}
</style>

<script>
	$(function() {
		$("#fromDatepicker").datepicker();
	});
	$(function() {
		$("#toDatepicker").datepicker();
	});

	$(function() {
		$("#tabs").tabs();
	});

	$(function() {
		$("#sortable1, #sortable2").sortable({
			connectWith : ".connectedSortable"
		}).disableSelection();
	});

	function filterResults(radioCode) {
		var date = $("#fromDatepicker").datepicker({
			dateFormat : 'dd-mm-yy'
		}).val();
		//var toDate = $("#toDatepicker").datepicker({ dateFormat: 'dd-mm-yy' }).val();
		window.location.replace("/showRadioInfo.html?radioCode=" + radioCode
				+ "&date=" + date);
	}

	function showLog(radioCode) {
		$.get("/showLog", {
			radioCode : radioCode
		}, function(data) {
			window.location.reload(false);
		});
	}

	function updateRadioCommercials(radioCode) {

		var userCommercialsList = document.getElementById("sortable1")
				.getElementsByTagName("li");
		var radioCommercialsList = document.getElementById("sortable2")
				.getElementsByTagName("li");

		var userCommercialsNames = [];
		for (var i = 0; i < userCommercialsList.length; i++) {
			userCommercialsNames.push(userCommercialsList[i].innerHTML);
		}

		var radioCommercialsNames = [];
		for (var i = 0; i < radioCommercialsList.length; i++) {
			radioCommercialsNames.push(radioCommercialsList[i].innerHTML);
		}

		var updateRadio = {
			radioCode : radioCode,
			userCommercials : userCommercialsNames,
			radioCommercials : radioCommercialsNames
		};

		console.log(updateRadio);

		$.ajax({
			type : "POST",
			url : "updateRadioCommercials",
			data : JSON.stringify(updateRadio),
			contentType : "application/json",
			success : function(data) {
				window.location.reload(false);
			}
		});
	}

	$(function() {
		$("#dialog").dialog({
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 1000
			},
			hide : {
				effect : "explode",
				duration : 1000
			}
		});

		$("#opener").click(function() {
			$("#dialog").dialog("open");
		});
	});
</script>

</head>

<body>

	<div id="container" class="container" style="height: auto; width: 100%">

		<c:set var="radioCode" value="${radioModel.code}" scope="request" />
		<c:set var="radioName" value="${radioModel.name}" scope="request" />
		<c:set var="username" value="ffuentes" scope="request" />
		<jsp:include page="header.jsp" flush="true">
			<jsp:param name="radioCode" value="${radioCode}" />
			<jsp:param name="radioName" value="${radioName}" />
			<jsp:param name="username" value="${username}" />
		</jsp:include>

		<div id="table-results" class="table-responsive" style="height: 30%;">
			<table class="table">
				<thead style="background-color: darkgray;">
					<tr>
						<th>Jingle</th>
						<th>Frecuencia Diaria</th>
						<th>Hora Inicio</th>
						<th>Hora Fin</th>
						<th>Detalle</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${radioModel.jingleResults}" var="jingleResult"
						varStatus="varStatus">
						<tr>
							<td>${jingleResult.name}</td>
							<td>${jingleResult.frecuency}</td>
							<td><fmt:formatDate
									value="${jingleResult.firstCoincidenceDate}" pattern="HH:mm:ss" />
							</td>
							<td><fmt:formatDate
									value="${jingleResult.lastCoincidenceDate}" pattern="HH:mm:ss" /></td>
							<td><button id="opener">
									<img style="width: 20px; height: 20px;"
										src="/resources/img/ico_lupa.png">
								</button></td>
						</tr>

					</c:forEach>
				</tbody>
			</table>
		</div>

		<div style="width: 25%; height: 5%; padding: 10px">
			<table class="table">
				<thead style="background-color: darkseagreen;">
					<tr>
						<th>Día: <input type="text" id="fromDatepicker" size="10"></th>
						<th><button type="button"
								style="border: 0; background: forestgreen"
								onclick="javascript:filterResults('${radioModel.code}')">
								Filtrar</button></th>
					</tr>
				</thead>
			</table>
		</div>

		<div class="userCommercials" style="height: 45%; width: 100%;">
			<table>
				<tbody>
					<tr>
						<td align="center" style="width: 45%; height: 10%"><h3
								style="font-family: monospace; /* font: -webkit-control; */ font-size: 18;">
								Mis Jingles - (${fn:length(radioModel.deactivedJingles)}) - <img
									src="/resources/img/silence.png" width="40px" height="40px">
							</h3></td>
						<td align="center" style="width: 10%; height: 5%"></td>
						<td align="center" style="width: 45%; height: 5%"><h3
								style="font-family: monospace; /* font: -webkit-control; */ font-size: 18;">
								Jingles Activos - (${fn:length(radioModel.activeJingles)}) - <img
									src="/resources/img/listen.png" width="40" height="40">
							</h3></td>
					</tr>
					<tr>
						<td align="center" style="width: 45%; height: 80%;">
							<ul id="sortable1" class="connectedSortable"
								style="float: none; text-align: -webkit-center; height: 100%; overflow: auto">
								<c:forEach items="${radioModel.deactivedJingles}"
									var="userCommercial" varStatus="varStatus">
									<li class="ui-state-default" value="${userCommercial.title}"
										style="background: salmon; border-color: darkred; color: brown;">${userCommercial.title}</li>
								</c:forEach>
							</ul>
						</td>
						<td align="center" style="width: 10%; height: 80%;">
							<div>
								<button type="button" style="border: 0; background: transparent">
									<img title="Guardar Cambios"
										onclick="javascript:updateRadioCommercials('${radioModel.code}')"
										src="
							/resources/img/button/refresh.png" width="100"
										height="100">
								</button>
							</div>
							<div style="padding-top: 10%">
								<h3>
									<b> Subir Jingle (.wav) </b>
								</h3>
								<form method="POST" enctype="multipart/form-data"
									action="upload.html?radioCode=${radioModel.code}">
									<div style="padding-top: 5px; float: left;">
										<input type="file" name="file" size="40">
									</div>
									<div style="float: right;">
										<button type="submit"
											style="border: 0; background: transparent">
											<img src="/resources/img/button/upload_1.png" width="30"
												height="30">
										</button>
									</div>
								</form>
							</div>
						</td>
						<td align="center" style="width: 45%; height: 80%;">
							<ul id="sortable2" class="connectedSortable"
								style="float: none; text-align: -webkit-center; height: 100%; overflow: auto">
								<c:forEach items="${radioModel.activeJingles}"
									var="radioCommercial" varStatus="varStatus">
									<li class="ui-state-highlight" value="${radioCommercial.title}"
										style="background: lightgreen; border-color: darkgreen; color: forestgreen;">${radioCommercial.title}</li>
								</c:forEach>
							</ul>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div id="dialog" title="Basic dialog">
		<p>Not implemented yet!</p>
	</div>
</body>
</html>