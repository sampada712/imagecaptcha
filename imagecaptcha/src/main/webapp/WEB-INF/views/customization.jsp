<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">
<head profile="http://gmpg.org/xfn/11">
<title>CAPTCHA: Customizations</title>
<link rel="shortcut icon" href="/static/favicon.ico" type="image/x-icon" />

<c:choose>
	<c:when test="${mobile}">
		<link href="/imagecaptcha/resources/css/default-mobile.css"
			rel="stylesheet" type="text/css" />
		<!-- TODO: add logic for iPad -->
		<meta name="viewport"
			content="width=device-width, initial-scale:1.0, minimum-scale:1.0, maximum-cale:1.0" />
	</c:when>
	<c:otherwise>
		<link href="/imagecaptcha/resources/css/default.css" rel="stylesheet"
			type="text/css" />
	</c:otherwise>
</c:choose>
<c:if test="${ipad}">
	<meta name="viewport"
		content="width=device-width, initial-scale:1.0, minimum-scale:1.0, maximum-cale:1.0" />
</c:if>
<link href="/imagecaptcha/resources/css/captcha.css" rel="stylesheet"
	type="text/css" />

<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js"></script>

</head>
<body>
	<div class="main">

		<script type="text/javascript"
			src="/imagecaptcha/resources/js/scripts.js"></script>
		<link rel="stylesheet"
			href="/imagecaptcha/resources/css/jquery.ui.all.css">
		<style>
#custom_table  table,th,td { /*    border:1px solid #ccc;
    border-width:1px 1px 1px 0px;
    border-style:solid;
    border-color:#ccc;
    border-collapse:collapse;*/
	/*    margin-top:5px;*/
	height: auto;
}

.item {
	font-weight: bold;
	width: 20%;
	padding: 5px 0px;
}

.subInfo {
	font-size: 14px;
	font-weight: normal;
}

.subsubInfo {
	font-size: 10px;
}

.miniHeader {
	font-size: 10px;
}

.highlight {
	background-color: #efefef;
}

#custom_table .bold {
	font-weight: bold;
}

#picatcha .picatcha_question {
	text-align: left;
}

#custom_table input[type="checkbox"] {
	padding: 6px;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 11px;
	vertical-align: top;
}

#custom_table .radio_label {
	margin-right: 20px;
}

#custom_table #slider {
	width: 150px;
	height: 8px;
	margin: 15px auto 30px auto;
}

#custom_table .demo { /*    margin-left:150px;*/
	
}

#custom_table a.ui-slider-handle {
	height: 15px;
	width: 10px;
}

/*.halfContent {
	width: 49%;
	vertical-align: middle;
}*/
.left {
	text-align: left;
	border-left: 1px;
}

/*
.leftIndent {
	position: relative;
	left: 15px;
}*/
#amount {
	background-color: #EEE;
}

.customSubHeader {
	border-bottom: 2px solid #333;
	text-align: left;
	margin: 5px;
	padding-left: 5px;
	width: 90%;
	font-weight: bold;
}

.farbtastic {
	margin: auto;
}

tr {
	vertical-align: top;
}

#color {
	margin-top: 20px;
}
</style>

		<script type="text/javascript">
			//Collect all the user inputs, set the appropriate variables in Picatcha and tell it to refresh!

			function liveRefresh() {
				//checked elements
				var checked_elements = $('input:checked');
				//console.log(checked_elements.length);

				//console.log("iterating through elements:");
				for ( var i = 0; i < checked_elements.length; i++) {
					//console.log([i,checked_elements[i].name]);
					if (checked_elements[i].name == 'radio1') {
						Picatcha.FORMAT = checked_elements[i].value;
						//console.log(Picatcha.FORMAT);
					} else if (checked_elements[i].name == 'cb2') {
						//PicatchaOptions['langOverride'] = checked_elements[i].checked==1;
					} else if (checked_elements[i].name == 'cb1') {
						//Picatcha.LINK = checked_elements[i].value;
					}
				}

				Picatcha.FORMAT = $('#f').find(':selected')[0].value;
				Picatcha.IMG_SIZE = $('#is').find(':selected')[0].value;
				Picatcha.NOISE_TYPE = $('#nt').find(':selected')[0].value;
				/*PicatchaOptions={
				    'lang':$('#picatchaCustomLanguage').find(':selected')[0].value,
				    //'langOverride':'0'
				}*/
				PicatchaOptions['lang'] = $('#picatchaCustomLanguage').find(
						':selected')[0].value;
				//PicatchaOptions['langOverride'] = $('#cb2')[0].checked;
				Picatcha.refresh();
			}
		</script>
		<div id="headline">Captcha Customizations</div>
		<div class="default customization">
			<span style="margin-bottom: 5px;"></span>
			<form id="custom_form" action="/interfaces/custom/" method="post"
				accept-charset="utf-8" class="centered">
				<div style='display: none'>
					<input type='hidden' name='csrfmiddlewaretoken'
						value='JlCoIaAkKNs1aORQLuw83zXVeIkY3skM' />
				</div>
				<div style="width: 100%">

					<script type="text/javascript"
						src="/imagecaptcha/resources/js/captcha.js"></script>
					<link rel="stylesheet" type="text/css"
						href="/imagecaptcha/resources/css/captcha.css" />
					<script>
						Picatcha.API_SERVER = 'http://192.168.1.5:8080'; //change these later to the settings_web.py or whatever
						Picatcha.PUBLIC_KEY = 'test'; //change to settings.py
						Picatcha.FORMAT = '2';
						Picatcha.IMG_SIZE = '75';
						Picatcha.NOISE_LEVEL = '0';
						Picatcha.NOISE_TYPE = '0';
						PicatchaOptions = {
							'lang' : 'en',
							langOverride : '0'
						}
						window.onload = function() {
							Picatcha.create('picatcha');
						}
					</script>
					<div id="picatcha"></div>



				</div>

				<div id="custom_table">

					<br />

					<table style="width: 100%">
						<tr>
							<td colspan="3"><span class="header_text">APPLY
									CUSTOMIZATIONS</span></td>
						</tr>
						<tr>
							<td class="feature bold ">IMAGE OPTIONS</td>
							<td class="feature bold">GENERAL OPTIONS</td>
						</tr>
						<tr>
							<td class="feature ">
								<!-- Image Options -->
								<div class="customSubHeader">No Of Images</div> <input
								type="hidden" value="2" name="picatcha[format]"></input><span
								class="halfContent left"> <label for="f"
									class="leftIndent">No Of Images:</label></span> <span
								class="halfContent"> <select id="f">
										<option value="1">6</option>
										<option value="2" selected="selected">8</option>
										<option value="3">10</option>
										<option value="4">12</option>
								</select>
							</span> <br /> <br />

								<div class="customSubHeader">Image Size</div> <input
								name="picatcha[image_size]" type="hidden" value="75" /> <span
								class="halfContent left"> <label for="is"
									class="leftIndent">Image Size:</label></span> <span
								class="halfContent"> <select id="is">
										<option value="50">50</option>
										<option value="60">60</option>
										<option value="75" selected="selected">75</option>
								</select>
							</span> <br /> <br />

							</td>

							<!-- General Options -->
							<td class="feature">

								<div class="customSubHeader">Noise</div> <input type="hidden"
								value="0" name="picatcha[noise_type]"></input> <input
								type="hidden" value="0" name="picatcha[noise_value]"></input> <span
								class="halfContent left"> <label for="nt"
									class="leftIndent">Noise Type:</label>
							</span> <span class="halfContent"> <select id="nt">
										<option value="0">Random</option>
										<option value="1">Shadow</option>
										<option value="2">Pixelation</option>
								</select>
							</span> <br /> <br />

								<div class="customSubHeader">Localization</div> <span
								class="halfContent left"> <span class="leftIndent">Default
										Localization:</span>

							</span> <span class="halfContent"> <select name="lang"
									id="picatchaCustomLanguage" style="">
										<option value="en">English</option>
										<option value="ar">Arabic</option>
										<option value="bg">Bulgarian</option>
										<option value="fr">French</option>
										<option value="de">German</option>
										<option value="hi">Hindi</option>
										<option value="hu">Hungarian</option>
										<option value="it">Italian</option>
										<option value="ja">Japanese</option>
										<option value="ko">Korean</option>
										<option value="ru">Russian</option>
								</select>
							</span> <!-- <span class="halfContent left">
            <span class="leftIndent">Allow Users to override the default localization?</span>
          </span>
          <span class="halfContent">
            <input type='checkbox' value='1' id='cb2' />
          </span> -->

							</td>
						</tr>
					</table>
				</div>
				<div class="buttons buttonspad">
					<input type="button" name="button" value="Submit"
						onclick="liveRefresh()" id="submit" class="blue button">
				</div>
				<input type="hidden" name="picatcha[link]" value="1"></input> <input
					type="hidden" name="langOverride" value="0">
			</form>

		</div>

		<script type="text/javascript">
			var _gaq = _gaq || [];
			_gaq.push([ '_setAccount', 'UA-24757211-1' ]);
			_gaq.push([ '_setDomainName', '.picatcha.com' ]);
			_gaq.push([ '_trackPageview' ]);
			(function() {
				var ga = document.createElement('script');
				ga.type = 'text/javascript';
				ga.async = true;
				ga.src = ('https:' == document.location.protocol ? 'https://ssl'
						: 'http://www')
						+ '.google-analytics.com/ga.js';
				var s = document.getElementsByTagName('script')[0];
				s.parentNode.insertBefore(ga, s);
			})();
		</script>

		<script type="text/javascript">
			if (typeof jQuery != 'undefined') {
				jQuery(document)
						.ready(
								function($) {
									var filetypes = /\.(zip|exe|pdf|jar|doc*|xls*|ppt*|mp3)$/i;
									var baseHref = '';
									if (jQuery('base').attr('href') != undefined)
										baseHref = jQuery('base').attr('href');
									jQuery('a')
											.each(
													function() {
														var href = jQuery(this)
																.attr('href');
														if (href
																&& (href
																		.match(/^https?\:/i))
																&& (!href
																		.match(document.domain))) {
															jQuery(this)
																	.click(
																			function() {
																				var extLink = href
																						.replace(
																								/^https?\:\/\//i,
																								'');
																				_gaq
																						.push([
																								'_trackEvent',
																								'External',
																								'Click',
																								extLink ]);
																				if (jQuery(
																						this)
																						.attr(
																								'target') != undefined
																						&& jQuery(
																								this)
																								.attr(
																										'target')
																								.toLowerCase() != '_blank') {
																					setTimeout(
																							function() {
																								location.href = href;
																							},
																							200);
																					return false;
																				}
																			});
														} else if (href
																&& href
																		.match(/^mailto\:/i)) {
															jQuery(this)
																	.click(
																			function() {
																				var mailLink = href
																						.replace(
																								/^mailto\:/i,
																								'');
																				_gaq
																						.push([
																								'_trackEvent',
																								'Email',
																								'Click',
																								mailLink ]);
																			});
														} else if (href
																&& href
																		.match(filetypes)) {
															jQuery(this)
																	.click(
																			function() {
																				var extension = (/[.]/
																						.exec(href)) ? /[^.]+$/
																						.exec(href)
																						: undefined;
																				var filePath = href;
																				_gaq
																						.push([
																								'_trackEvent',
																								'Download',
																								'Click-'
																										+ extension,
																								filePath ]);
																				if (jQuery(
																						this)
																						.attr(
																								'target') != undefined
																						&& jQuery(
																								this)
																								.attr(
																										'target')
																								.toLowerCase() != '_blank') {
																					setTimeout(
																							function() {
																								location.href = baseHref
																										+ href;
																							},
																							200);
																					return false;
																				}
																			});
														}
													});
								});
			}
		</script>

		<!--Kissmetrics code-->
		<script type="text/javascript">
			var _kmq = _kmq || [];
			function _kms(u) {
				setTimeout(function() {
					var s = document.createElement('script');
					var f = document.getElementsByTagName('script')[0];
					s.type = 'text/javascript';
					s.async = true;
					s.src = u;
					f.parentNode.insertBefore(s, f);
				}, 1);
			}
			_kms('//i.kissmetrics.com/i.js');
			_kms('');
		</script>
		<!--Kissmetrics code end-->
</body>
</html>
