<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="page-header">
	<table class="table-header">
		<thead>
			<tr>
				<th style="width: 6%;"><c:if test="${ not empty radioCode}">
						<a href="/home.html"> <img
							src="/resources/img/button/back.png" title="Volver" alt="Volver"
							width="60" height="60">
						</a>
					</c:if></th>
				<th style="width: 25%"><c:if test="${ not empty radioCode}">
						<h4>
							<img class="cover"
								style="border: 3px solid #a1a1a1; border-radius: 50px;"
								src="/resources/img/radio/${radioCode}.gif" title="${radioName}"
								alt="${radioName}" width="60" height="60"><b> ${radioName}</b>
						</h4>
					</c:if></th>
				<th style="width: 5%;"><img class="radioAuditLogo"
					style="width: 80px; height: 80px;" alt="Radio Audit"
					src="/resources/img/radioaudit.jpg">
				<th style="width: 50%;"><h1>
						<i><b>Radioaudit.com.uy</b></i>
					</h1></th>
				<th style="width: 15%;"><img class="userInfo"
					style="width: 60px; height: 60px;" alt="Radio Audit"
					src="/resources/img/user.png"> ffuentes</th>
			</tr>
		</thead>
	</table>
</div>
