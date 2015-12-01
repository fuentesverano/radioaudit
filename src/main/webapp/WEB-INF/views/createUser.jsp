<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<title>Create User</title>
<script src="js/general-functions.js" type="text/javascript"></script>
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>

<script type="text/javascript">
	$(document).ready(function() {
		document.getElementById("usernameInput").focus();
	});

	function _validate_createUser() {
		// validar inputs
		document.getElementById("loginFormController").submit();
	}
</script>
</head>

<body>

	<jsp:include page="header.jsp" flush="true" />
	<div class="bodyContent"
		style="text-align: center; padding: 10px; margin: 5%; padding: 3%; margin-left: 30%; margin-right: 30%; border-color: gray; border-style: solid; border-radius: 25px;">
		<div class="container" style="padding-bottom: 20px;">
			<span class="title"
				style="font-style: oblique; font-size: 25; font-family: sans-serif;">Ingrese
				nuevo usuario: </span>
		</div>
		<div class="tableContent"
			style="padding-left: 25%; padding-right: 25%;">
			<form id="createUserController" method="POST"
				action="createUser/create.html">
				<table>
					<tr>
						<td><span class="title"
							style="font-style: inherit; font-size: 20; font-family: -webkit-body;">Usuario:
						</span></td>
						<td><form:input id="usernameInput"
								path="createUserModel.username" /></td>
					</tr>
					<tr>
						<td><span class="title"
							style="font-style: inherit; font-size: 20; font-family: -webkit-body;">Contraseña:
						</span></td>
						<td><form:password id="passwordInput"
								path="createUserModel.password" /></td>
					</tr>
					<tr>
						<td><span class="title"
							style="font-style: inherit; font-size: 20; font-family: -webkit-body;">Confirmar
								contraseña: </span></td>
						<td><form:password id="confirmPasswordInput"
								path="createUserModel.confirmPassword" /></td>
					</tr>
					<tr>
						<td><span class="title"
							style="font-style: inherit; font-size: 20; font-family: -webkit-body;">Nombre:
						</span></td>
						<td><form:input id="firstnameInput"
								path="createUserModel.firstname" /></td>
					</tr>
					<tr>
						<td><span class="title"
							style="font-style: inherit; font-size: 20; font-family: -webkit-body;">Apellido:
						</span></td>
						<td><form:input id="lastnameInput"
								path="createUserModel.lastname" /></td>
					</tr>
					<tr>
						<td><span class="title"
							style="font-style: inherit; font-size: 20; font-family: -webkit-body;">Email:
						</span></td>
						<td><form:input id="emailInput" path="createUserModel.email" />
						</td>
					</tr>
					<tr>
						<td></td>
						<td>

							<button id="loginButton" value="submit"
								onclick="_validate_createUser()"
								style="border-radius: 5; width: 70; font-family: sans-serif; background-color: darkgray;">Ingresar</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="clearBox"></div>
	</div>
</body>

</html>