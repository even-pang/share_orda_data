<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>관리자 로그인</title>
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Manjari|family=Lato|Nanum+Gothic&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="/css/login.css">
		<link rel="shortcut icon" href="/images/favicon.ico">
		<link rel="icon" href="/images/favicon.ico">
		<META content="text/html; charset=ks_c_5601-1987" http-equiv=Content-Type>
		<META content=text/javascript http-equiv=Content-Script-Type>
		<META content=text/css http-equiv=Content-Style-Type>
		<META content=no http-equiv=imagetoolbar>
		<META name=GENERATOR content="MSHTML 9.00.8112.16457">

		<script type=text/javascript src="/js/jquery-1.7.2.min.js"></script>
		<script src="/js/n1Util.js"  type="text/javascript"></script>
		<script>
			$(document).ready(function() {
				$("#user_id").focus();
			});

			function fSubmit()
			{
				 vObj = document.frmLogin;
				 if (!checkEmpty(vObj.user_id, "아이디를 입력해주세요.")) return false;
				 if (!checkEmpty(vObj.user_pw, "비밀번호를 입력해주세요.")) return false;
				 
				 vObj.submit();
				 return true;
			}	
			
			function enterkey() {
		        if (window.event.keyCode == 13) {
		        	fSubmit();
		        }
			}
		</script>
	</head>
	<style>
	.wrapTitle {
	    font-weight: bold;
	    font-size: 20px;
	}
	</style>
	<body oncontextmenu="return false" onselectstart="return false" ondragstart="return false">
	    <div id="wrap">
	        <div id="loginDiv">
	            <div id="logo">
	            	<h1 id="logoText">
	            		<img src="/images/logo.png" class="admin_logo"><br>
	            		<p class="wrapTitle">Admin Login</p>
                		<img src="/images/dotdotdot.PNG" class="dotImg">
	            	</h1>
	            </div>
	            <form name="frmLogin" action="/admin/login.do" method="post" onsubmit="return fSubmit()">
                    <p class="inputP"><input type="text" name = "user_id" id="userid" onkeyup="enterkey();" placeholder="ID" class="input" autocomplete="off" maxlength="20"></p>
	                <p class="inputP" style="margin-bottom: 30px;"><input type="password" onkeyup="enterkey();" name = "user_pw" id="pwd" placeholder="PWD" class="input" maxlength="20"></p>
	                
                    <!-- 
	                <p class="inputP"><i class="fas fa-user"></i><input type="text" name = "user_id" id="userid" onkeyup="enterkey();" placeholder="아이디를 입력하세요." class="input" autocomplete="off"></p>
	                <p class="inputP" style="margin-bottom: 30px;"><i class="fas fa-lock"></i><input type="password" onkeyup="enterkey();" name = "user_pw" id="pwd" placeholder="비밀번호를 입력하세요" class="input"></p>
	                -->
		            <p><input type="button" value="LOGIN" class = "loginBtn" id="loginBtn" onclick="fSubmit();"></p>
	            </form>
	        </div>
	    </div>
	</body>
</html>