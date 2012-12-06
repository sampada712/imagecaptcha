<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Captcha Response</title>
<link href="/imagecaptcha/resources/css/default.css" rel="stylesheet"
	type="text/css" />
</head>
<body>
	<c:choose>
		<c:when test="${result}">
			<div class="main">
				<div class="default demo_result">
					<h1>Congratulations!</h1>
					<h3>You have done it!</h3>
					<p>
						<a href="/imagecaptcha"><h4>Back to demo Page</h4></a>
					</p>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="main">
				<div class="default demo_result">
					<h1>OOPS!</h1>
					<h3>You got it wrong. But no worries, its easy. Give it
						another shot!</h3>
					<p>
						<a href="/imagecaptcha"><h4>Back to demo Page</h4></a>
					</p>
				</div>
			</div>

		</c:otherwise>
	</c:choose>
</body>
</html>
