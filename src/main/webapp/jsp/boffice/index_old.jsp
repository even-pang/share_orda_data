<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0055)http://academy.rankup.co.kr/RAD/rankup_index/index.html -->
<HTML><HEAD><title>Doge</title>
<%-- <%
	//현 접속자 IP
	String cIp = request.getRemoteAddr();

	//허가된 IP
	String aIpTable[] = {"123.140.92.48", "123.140.92.24", "1.237.220.16"};
	
	int i;
	int iOk = 0;
	for(i = 0; i < aIpTable.length; i++) {
		if(aIpTable[i].equals(cIp)) {
			iOk = 1;
			break;
		}
	}
	
	if(iOk == 0) {
		System.out.println("[IP차단] Access Denied : " +cIp);
		response.sendRedirect("/");

		return;
	}
%> --%>

<META content="text/html; charset=ks_c_5601-1987" http-equiv=Content-Type>
<META content=text/javascript http-equiv=Content-Script-Type>
<META content=text/css http-equiv=Content-Style-Type>
<META content=no http-equiv=imagetoolbar>
<LINK rel=stylesheet type=text/css href="/boffice/css/main_admin.css">

<script type=text/javascript src="/js/jquery-1.7.2.min.js"></script>
<script src="/js/n1Util.js"  type="text/javascript"></script>

<META name=GENERATOR content="MSHTML 9.00.8112.16457"></head>
<script type=text/javascript>
	$(document).ready(function() {
		$("#user_id").focus();
	});
	
	function fSubmit()
	{
		 vObj = document.frmLogin;
		 if (!checkEmpty(vObj.user_id, "아이디를 입력해주세요.")) return false;
		 if (!checkEmpty(vObj.user_pw, "비밀번호를 입력해주세요.")) return false;
		 
		 return true;
	}	
 
	
	function fEnter(evt)
	{
		if (event.keyCode==13) 
		{
			document.frmLogin.submit();
        }  
	}
	
</script>

<body>
<div style="position: absolute; right: 0px">
  <!-- <a href="/" target=_blank><img border=0 src="/boffice/image/login/rt_bt.png"></a> -->
</div>
  <table style="background: url(/boffice/image/login/bg_img_bg2.jpg)" border=0 cellSpacing=0 cellpadding=0 width="100%" height="100%">
  <tr>
    <td vAlign=top>
      <table style="background: url(/boffice/image/login/bg_img2.jpg) no-repeat center top"  border=0 cellSpacing=0 cellpadding=0 width=940 align=center>
        <tr>
          <td vAlign=top align=left>
            <table border=0 cellSpacing=0 cellpadding=0 width="100%">
              <tr>
                <td height=345 vAlign=top align=left>&nbsp;</td></tr>
              <tr>
                <td vAlign=top align=center>
                  <table border=0 cellSpacing=0 cellpadding=0 width="100%" align=center>
                    <tr>
                      <td vAlign=top align=center>
                        <table border=0 cellSpacing=0 cellpadding=0>
                          <form name="frmLogin" action="/admin/login.do" method="post" onsubmit="return fSubmit()">
	                          <tr>
	                            <td vAlign=top align=left>
	                              <table style="border-bottom: #d6d6d6 1px solid; border-left: #d6d6d6 1px solid; padding-left: 80px; width: 230px; background: url(/boffice/image/login/id.jpg) #fff no-repeat 7px 7px; border-top: #d6d6d6 1px solid; border-right: #d6d6d6 1px solid"  border=0 width=190>
	                                <tr>
	                                  <td height=30 width=80>&nbsp;</td>
	                                  <td><input id="user_id" name="user_id" style="border-bottom:0px; border-left:0px; border-top:0px; border-right:0px" style="ime-mode:inactive"    tabIndex="1" size="14" maxlength="20"></td>
	                                </tr>
	                               </table>
	                            </td>
	                            <td style="padding-left: 20px;" rowSpan=2>
	                               <input type="image" src="/boffice/image/login/btn_login.png" alt="로그인 버튼" />
	                            </td>
	                          </tr>
	                          <tr>
	                            <td vAlign=top align=left>
	                              <table style="border-bottom: #d6d6d6 1px solid; border-left: #d6d6d6 1px solid; padding-left: 80px; width: 230px; background: url(/boffice/image/login/pw.jpg) #fff no-repeat 7px 7px; border-top: #d6d6d6 1px solid; border-right: #d6d6d6 1px solid" border=0 width=190>
	                                <tr>
	                                   <td height=30 width=80>&nbsp;</td>
	                                   <td><input name="user_pw" id="user_pw"  size=14 type="password" maxlength="20" style="border-bottom: 0px; border-left: 0px; border-top: 0px; border-right: 0px" tabIndex=2 ></td></tr>
	                                </table>
	                             </td>
	                          </tr>
	                          <tr>
	                            <td height=15></td></tr>
	                          <tr>
	                            <td vAlign=top colSpan=2 align=left></td>
	                        </tr>
                        </form>
                        
                        </table>
                       </td></tr>
                       </table>
                       </td>
                       </tr>
                       
                       </table></td></tr>
                       
                       </table>
                       </td></tr>
  <tr>
    <td height=30></td></tr>
  <tr>
    <td bgColor=#d6d6d6 height=1></td></tr>
  <tr>
    <td height=20></td></tr>
  <tr>
    <td align=center>&copy; 2016 <a href="#">kwangshin.ac.kr</a>, All Rights Reserved &reg;</td></tr>
  <tr>
    <td height=20></td></tr></table>

</BODY></HTML>
