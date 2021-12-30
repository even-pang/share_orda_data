<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="charm.util.*, java.util.*" %>

<%

   SessionUtil.removeSessionAttribute(request,"USR");
   SessionUtil.removeSessionAttribute(request,"ADM");
   session.invalidate();


%>
<script type="text/javascript">
<!--
		alert("로그아웃 되었습니다.");
		window.location.href="/boffice/";
-->
</script>

 