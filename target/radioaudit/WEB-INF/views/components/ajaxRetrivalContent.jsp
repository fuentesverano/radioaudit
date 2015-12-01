<%@page import="java.util.UUID"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ page contentType="text/html; charset=utf-8"%>

<c:set var="component_id">
	<%= "ID_" + UUID.randomUUID().toString() %>
</c:set>
<c:set var="controller">
	<tiles:getAsString name='controller'/>
</c:set>
<c:set var="params">
	<tiles:getAsString name='params'/>
</c:set>
<c:set var="errorLoadingMessage">
	<tiles:getAsString name='errorLoadingMessage'/>
</c:set>
<c:set var="callback">
	<tiles:getAsString name='callback' ignore="true"/>
</c:set>


<script type="text/javascript">
	$(document).ready(function(){
		gen_postLoadingPage('${controller}', ${params}, function(data){
			$("#${component_id}").replaceWith(data);

// 			if ($("form",$("#${component_id}")).length > 0){
// 				$("form",$("#${component_id}")).LeValidate({
// 					notifyBox : "le-notification-orange",
// 					errorMessages : versionedStaticResource_errorMessages
// 				});
// 			}
			<c:if test="${ not empty callback}">
				${callback}();
			</c:if>
		},function(){
			$("#${component_id} img").remove();
			$("#${component_id}_errorContainer").show();
		});
	});
	
</script>

<div id="${component_id}">
	<img alt="Loading" src="${pageContext.request.contextPath}/img/microfono.gif" />
	<div id="${component_id}_errorContainer" class="ui-widget warning hide">
		<div class="ui-state-error ui-corner-all messageComponentDiv messageComponent_ERROR" style="padding: 10px;">
			<table style="width: 100%">
				<tr>
					<td style="width: 5px; vertical-align: middle;"><span class="ui-icon ui-icon-alert"></span></td>
					<td style="width: 95%; color: #cd0a0a;"><spring:message code="${errorLoadingMessage}" htmlEscape="true" /></td>
				</tr>
			</table>
		</div>
	</div>
	
</div>

