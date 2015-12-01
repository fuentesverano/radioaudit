<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html lang="es">

<head>
<title> Radioaudit.com</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="Login">
<meta name="author" content="Fabricio Fuentes">
<link rel="icon" href="../../favicon.ico">

<!-- Bootstrap core CSS -->
<link href="/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<!-- Custom styles for this template -->
<link href="/resources/css/signin.css" rel="stylesheet">
<link href="/resources/css/page-header.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<!-- <script src="/resources/bootstrap/js/ie-emulation-modes-warning.js"></script> -->

<script src="/resources/js/general-functions.js" type="text/javascript"></script>
<script src="/resources/js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>

<script type="text/javascript">
	
</script>
</head>

<body>
	<jsp:include page="header.jsp" flush="true" />

	<div class="container">

		<form id="loginFormController" class="form-signin" method="POST"
			action="login/process-login">
			<h2 class="form-signin-heading">Please sign in</h2>
			<span class="input-group-addon"><i
				class="glyphicon glyphicon-user"></i></span> <label for="inputEmail"
				class="sr-only">Email</label>
			<%-- 			<spring:bind path="authenticationModel.email"> --%>
			<input id="inputUsername" type="text" name="username"
				class="form-control" placeholder="Username" required autofocus>
			<%-- 			</spring:bind> --%>
			<span class="input-group-addon"><i
				class="glyphicon glyphicon-lock"></i></span> <label for="inputPassword"
				class="sr-only">Password</label>
			<%-- 			<spring:bind path="authenticationModel.password"> --%>
			<input type="password" name="password" id="inputPassword"
				class="form-control" placeholder="Password" required>
			<%-- 			</spring:bind> --%>
			<div class="checkbox">
				<label><input type="checkbox" name="remember_me_checkbox"
					value="remember-me">Remember me</label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
			<div class="image-container">
				<img alt="" src="/resources/img/radio-philips-01.jpg"
					style="height: 25%; width: 100%">
			</div>
		</form>
	</div>
</body>
</html>