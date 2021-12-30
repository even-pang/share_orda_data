<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
Map reqMap = (Map) request.getAttribute("reqMap");
Map dbMap = (Map) request.getAttribute("dbMap");

String strCdType = CommonUtil.nvl(reqMap.get("cd_type"));
String strCommCd = CommonUtil.nvl(reqMap.get("comm_cd"));
String strIFlag = CommonUtil.nvl(reqMap.get("iflag"), CommDef.ReservedWord.INSERT);

if ("".equals(strCdType)) {
	strCommCd = "*";
}

if (dbMap == null)
	dbMap = new HashMap();
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="content-language" content="en" />
<meta name="robots" content="noindex,nofollow" />
<link rel="alternate stylesheet" media="screen,projection,print" type="text/css" href="/boffice/css/1col.css" title="1col" />
<!-- ALTERNATE: 1 COLUMN -->
<link rel="shortcut icon" href="/images/favicon.ico">
	<link rel="icon" href="/images/favicon.ico">
		<link href="https://fonts.googleapis.com/css?family=Lato|Nanum+Gothic&display=swap" rel="stylesheet">
			<link href="https://fonts.googleapis.com/css?family=Lato|Noto+Sans+KR:300,500,700,400|Nanum+Gothic&display=swap" rel="stylesheet">
				<link rel="stylesheet" type="text/css" href="/boffice/css/datepicer.css" />
				<script type="text/javascript" src="/js/n1Util.js"></script>
				<script type="text/javascript" src="/js/web.util.js"></script>


				<link rel="stylesheet" href="/css/kendoUI/kendo.default.min.css" />
				<link rel="stylesheet" href="/css/admin.css">
					<link rel="stylesheet" href="/css/admin2.css">
						<link rel="stylesheet" href="/css/bootstrap-datetimepicker.css" type="text/css">
							<script type="text/javascript" src="/js/jquery-1.12.3.min.js"></script>
							<script type="text/javascript" src="/js/common.js"></script>
							<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
							<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
								<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<title>코드관리</title>
</head>

<script>

   function fSubmit()
   {
	   vObj = document.frmCode;

	   if (!checkEmpty(vObj.cd_type, "대표코드를 입력하세요")) return;
	   if (!checkEmpty(vObj.comm_cd, "코드를 입력하세요")) return;
	   if (!checkEmpty(vObj.cd_nm,   "코드명를 입력하세요")) return;
	   
	   vObj.iflag.value = "<%=strIFlag%>
	";

		vObj.submit();
	}

	function fDelete() {
		vObj = document.frmCode;

		strMsg = "자료를 삭제하시겠습니까?";
		if (vObj.comm_cd.value == '*') {
			strMsg = "하위코드가 존재하는 경우 모두 삭제됩니다.\n\n자료를 삭제하시겠습니까?";
		}

		if (!confirm(strMsg))
			return;

		vObj.iflag.value = "DELETE";

		vObj.submit();
	}
</script>

<body>

	<div style="width: 450px">
		<!-- Content (Right Column) -->
		<div id="content" class="box" style="width: 450px">

			<!-- Headings -->
			<h2>코드 등록/수정</h2>

			<!-- Table (TABLE) -->
			<table class="board_list_normal" style="width: 450px">
				<form autocomplete="off" name="frmCode" method="post" action="/boffice/codeWork.do">
					<input name="iflag" type="hidden" value="<%=strIFlag%>">
					<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>">
					<colgroup>
						<col width="30%" />
						<col width="*%" />
					</colgroup>
					<tr>
						<th scope="col"><label for="caldate">대표코드</label></th>
						<td>
							<input type="text" name="cd_type" value="<%=strCdType%>" maxlength="10" class="input-text150" style="width: 150px;" <%=!"".equals(strCdType) ? " readonly" : ""%> />
						</td>
					</tr>
					<tr>
						<th scope="col"><label for="comm_cd">코드</label></th>
						<td>
							<input type="text" name="comm_cd" value="<%=strCommCd%>" maxlength="10" class="input-text150" style="width: 150px;" <%=!"".equals(strCommCd) ? " readonly" : ""%> />
						</td>
					</tr>

					<tr>
						<th scope="col"><label for="cd_nm">코드명</label></th>
						<td>
							<input type="text" name="cd_nm" value="<%=CommonUtil.nvl(dbMap.get("CD_NM"))%>" maxlength="100" class="input-text150" style="width: 150px;" />
						</td>
					</tr>

					<tr>
						<th scope="col"><label for="ord">비고</label></th>
						<td>
							<textarea rows="5" cols="30" name="cd_desc"><%=CommonUtil.nvl(dbMap.get("CD_DESC"))%></textarea>
						</td>
					</tr>

					<tr>
						<th scope="col"><label for="ord">순서</label></th>
						<td>
							<input type="text" name="ord" value="<%=CommonUtil.nvl(dbMap.get("ORD"))%>" maxlength="100" class="input-text150" style="width: 50px;" style="width:250px;IME-MODE:disabled;" onkeypress="onlysu();" />
						</td>
					</tr>
			</table>
			<div class="board_list_btn right" style="width: 450px">
				<input onclick="fSubmit()" type="button" value="완료" class="btn blue" />
				<%
				if ("UPDATE".equals(strIFlag)) {
				%>
				<input onclick="fDelete()" type="button" value="삭제" class="btn blue" />
				<%
				}
				%>
				<input onclick="self.close()" type="button" value="취소" class="btn blue" />
			</div>
		</div>
		<!-- /content -->

	</div>
	<!-- /main -->

</body>
</html>