<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">
<head profile="http://gmpg.org/xfn/11">
<title>Image Based Captcha</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="/imagecaptcha/resources/css/default.css" rel="stylesheet"
	type="text/css" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js"></script>
<link href="/imagecaptcha/resources/css/captcha.css" rel="stylesheet"
	type="text/css" />
<meta name="viewport"
	content="width=device-width, initial-scale:1.0, minimum-scale:1.0, maximum-cale:1.0" />
</head>
<body>

	<div class="main">
		<div id="headline">Get Started: Sign Up</div>
		<div class="default">
			<form id="signup_form" action="/imagecaptcha/signup/" method="post">
				<p>Please register to use the Captcha API.</p>
				<p>Once you are successfully signed up, you will be emailed with
					public/private keys.</p>
				<p>
					<label for="id_username">Username:</label> <input id="id_username"
						type="text" name="username" maxlength="255" />
				</p>

				<c:if test="${response.usernameError}">
					<p>
						<span style="color: red; position: relative; left: 90px;">Username
							is required.</span>
					</p>
				</c:if>

				<p>
					<label for="pass1">Password:</label> <input id="pass1"
						type="password" name="password" maxlength="255" />
				</p>
				<p>
					<label for="pass2">Confirm Password:</label> <input id="pass2"
						type="password" onkeyup="confirmPass(); return false;"
						name="password" maxlength="255" /> <span id="confirmMessage"
						class="confirmMessage"></span>
				</p>

				<p>
					<label for="id_email">Email:</label> <input id="id_email"
						type="text" name="email" maxlength="255" />
				</p>
				<c:if test="${response.emailIdError}">
					<p>
						<span style="color: red; position: relative; left: 90px;">Email
							Id is required.</span>
					</p>
				</c:if>

				<p>
					<label for="id_website">Company Name:</label> <input
						id="id_website" type="text" name="website" maxlength="255" />
				</p>
				<c:if test="${response.captchaError}">
					<p>
						<span style="color: red; position: relative; left: 90px;">Sorry,
							the Picatcha you filled out was incorrect.</span>
					</p>
				</c:if>
				<p>
					<label for="pixcaptcha">Captcha:</label><span name="pixcaptcha">
						<script type="text/javascript"
							src="/imagecaptcha/resources/js/captcha.js"></script>
						<link rel="stylesheet" type="text/css"
							href="/imagecaptcha/resources/js/captcha.css" /> <script>
								Picatcha.API_SERVER = 'http://192.168.1.5:8080';
								Picatcha.PUBLIC_KEY = 'test';
								Picatcha.NOISE_TYPE = '0';
								Picatcha.NOISE_LEVEL = '5';
								Picatcha.FORMAT = '2';
								Picatcha.IMG_SIZE = '75';
								PicatchaOptions = {
									'lang' : 'en',
									langOverride : '1'
								}
								window.onload = function() {
									Picatcha.create('picatcha');
								}
							</script>
						<div id="picatcha"></div>
					</span>
				</p>

				<input type="hidden" name="action" value="submit"> <input
					type="submit" id="signupSubmit" class="btn primary" value="Submit"
					style="margin-left: 165px" />
		</div>

		</form>
	</div>
	</div>

	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							var default_text = {
								'id_username' : 'Example: Sampada',
								'id_email' : 'Example: sampada@captcha.com',
								'id_website' : 'Example: www.captcha.com',
							};
							$.each(default_text, function(k, v) {
								var elm = $('#' + k);
								if (elm.val() == '')
									elm.val(v);
								elm.live('focus', function() {
									var v = default_text[this.id];
									if (v && this.value == v)
										this.value = '';
								})
							});

							// add in the validate 
							$('#id_username').blur(function() {
								checkIdAvailability();
							});
							$('#id_username')
									.after(
											$('<div id="usernameMessage" style="margin-left:150px; display:none;" />'));

							$('#id_email').blur(function() {
								echeck();
							});
							$('#id_email')
									.after(
											$('<div id="emailMessage" style="margin-left:150px; display:none;" />'));

							$('#pass1').blur(function() {
								checkPass();
							});

							$('#pass1')
									.after(
											$('<div id="passMessage" style="margin-left:150px; display:none;" />'));

							$("#signup_form").submit(
									function(event) {
										// disable the submit button to prevent repeated clicks
										$('#signupSubmit').attr("disabled",
												"disabled");
										//console.log('no credit card info');
										var form$ = $("#signup_form");
										form$.get(0).submit();
										// prevent the form from submitting with the default action
										return false;
									});
						});

		function checkIdAvailability() {
			var username = $('#id_username').val();
			var mesg = $('#usernameMessage');
			if (username != 'Example: Sampada' && username != '') {
				$.ajax({
					url : "/imagecaptcha/check_username/",
					dataType : 'json',
					data : {
						'username' : username
					},
					success : function(data) {
						//mesg.css('display','block');
						if (data.code == 'invalid-username') {
							mesg.css('color', '#FF6666').html(
									'Invalid username, Please Choose Another')
									.slideDown();
						} else if (data.taken) {
							mesg.css('color', '#FF6666').html(
									'Username Taken, Please Choose Another')
									.slideDown();
						} else {
							//mesg.css('color','green').html('Username Available');
							mesg.css('display', 'none').slideUp();
							mesg.parent().css('background-color', 'initial');
						}
					}
				});
			} else {
				mesg.css('display', 'none');
			}
		}

		function checkPass() {
			var ck_password = /^[A-Za-z0-9!@#$%^&*()_]{6,20}$/;
			var pass1 = document.getElementById('pass1');
			var message = $('#passMessage');
			if (!ck_password.test(pass1.value)) {
				message.css('color', '#FF6666').html(
						'Please enter a valid Password.').slideDown();
			} else {
				message.css('display', 'none').slideUp();
			}
		}

		function confirmPass() {
			//Store the password field objects into variables ...
			var pass1 = document.getElementById('pass1');
			var pass2 = document.getElementById('pass2');
			//Store the Confimation Message Object ...
			var message = document.getElementById('confirmMessage');
			//Set the colors we will be using ...
			var goodColor = "#66cc66";
			var badColor = "#ff6666";
			//Compare the values in the password field
			//and the confirmation field
			if (pass1.value == pass2.value) {
				//The passwords match.
				//Set the color to the good color and inform
				//the user that they have entered the correct password
				pass2.style.backgroundColor = goodColor;
				message.style.color = goodColor;
				message.innerHTML = "Passwords Match!";
			} else {
				//The passwords do not match.
				//Set the color to the bad color and
				//notify the user.
				pass2.style.backgroundColor = badColor;
				message.style.color = badColor;
				message.innerHTML = "Passwords Do Not Match!";
			}
		}

		function echeck() {
			var emailID = document.getElementById('id_email');
			var message = $('#emailMessage');
			var str = emailID.value;
			var at = "@";
			var dot = ".";
			var lat = str.indexOf(at);
			var lstr = str.length;
			var ldot = str.indexOf(dot);
			var error = 'success';
			if (str.indexOf(at) == -1) {
				error = 'error';
			}
			if (str.indexOf(at) == -1 || str.indexOf(at) == 0
					|| str.indexOf(at) == lstr) {
				error = 'error';
			}
			if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0
					|| str.indexOf(dot) == lstr) {
				error = 'error';
			}
			if (str.indexOf(at, (lat + 1)) != -1) {
				error = 'error';
			}
			if (str.substring(lat - 1, lat) == dot
					|| str.substring(lat + 1, lat + 2) == dot) {
				error = 'error';
			}
			if (str.indexOf(dot, (lat + 2)) == -1) {
				error = 'error';
			}
			if (str.indexOf(" ") != -1) {
				error = 'error';
			}
			if (error == 'error') {
				message.css('color', '#FF6666').html('Invalid E-mail ID')
						.slideDown();
			} else {
				message.css('display', 'none').slideUp();
			}
		}
	</script>
</body>
</html>
