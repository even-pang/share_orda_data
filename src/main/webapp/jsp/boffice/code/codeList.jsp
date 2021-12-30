<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
Map reqMap = (Map) request.getAttribute("reqMap");
List lstRs = (List) request.getAttribute("list");
int nRowCount = CommonUtil.getNullInt((String) request.getAttribute("count"), 0);

String today = CommonUtil.getCurrentDate("", "YYYYMMDD"); // 오늘 날짜

String strLinkPage = "/boffice/code/codeList.do"; //request.getRequestURL().toString(); // 현재 페이지
String strParam = CommonUtil.getRequestQueryString(request);

String strCommCd = CommonUtil.nvl(reqMap.get("comm_cd"));
String strCdType = CommonUtil.nvl(reqMap.get("cd_type"));
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="/boffice/include/inc_top.do" flush="false" />



<script>
	function fWrite() {
		vObj = document.frmWrite;
		vObj.brd_no.value = "";
		vObj.submit();
	}

	function fWrite(cd_type, comm_cd) {
		winURL = "/boffice/codeInput.do?cd_type=" + cd_type + "&comm_cd="
				+ comm_cd;
		popupWin(winURL, 'CodeWrite', '490', '450',
				' scrollbars=0,resizable=0 ');
	}
</script>


</head>

<body>
	<jsp:include page="/boffice/include/inc_header.do" flush="false" />
	<div id="main">
		<div class="group">
			<div class="header">
				<h3>Code List</h3>
			</div>
			<div class="body">
				<table class="board_list_normal">
					<colgroup>
						<col width="5%" />
						<col width="10%" />
						<col width="10%" />
						<col width="*%" />
						<col width="10%" />
						<%
						if (!"".equals(strCommCd)) {
						%>
						<col width="13%" />
						<%
						}
						%>
					</colgroup>
					<thead>
						<tr>
							<th scope="col">번호</th>
							<th scope="col">대분류</th>
							<th scope="col">코드</th>
							<th scope="col">코드명</th>
							<th scope="col">순서</th>
							<%
							if (!"".equals(strCommCd)) {
							%>
							<th scope="col">하위코드관리</th>
							<%
							}
							%>

						</tr>
					</thead>
					<tbody>

						<%
						if (lstRs != null && lstRs.size() > 0) {

							ServiceUtil sUtil = new ServiceUtil();

							int iSeqNo = 1;
							for (int iLoop = 0; iLoop < lstRs.size(); iLoop++) {
								Map rsMap = (Map) lstRs.get(iLoop);
						%>
						<tr <%=((iLoop % 2) == 1) ? "class='bg'" : ""%>>
							<td><%=iSeqNo%></td>
							<td class="textLeft">
								<a href="javascript:fWrite('<%=CommonUtil.nvl(rsMap.get("CD_TYPE"))%>','<%=CommonUtil.nvl(rsMap.get("COMM_CD"))%>')"><%=CommonUtil.nvl(rsMap.get("CD_TYPE"))%></a>
							</td>
							<td class="textLeft"><%=CommonUtil.nvl(rsMap.get("COMM_CD"))%></td>
							<td class="textLeft">
								<a href="javascript:fWrite('<%=CommonUtil.nvl(rsMap.get("CD_TYPE"))%>','<%=CommonUtil.nvl(rsMap.get("COMM_CD"))%>')"><%=CommonUtil.nvl(rsMap.get("CD_NM"))%></a>
							</td>
							<td><%=CommonUtil.nvl(rsMap.get("ORD"))%></td>
							<%
							if (!"".equals(strCommCd)) {
							%>
							<td>
								<a href="<%=strLinkPage%>?cd_type=<%=CommonUtil.nvl(rsMap.get("CD_TYPE"))%>">[하위코드관리]</a>
							</td>
							<%
							}
							%>
						</tr>
						<%
						iSeqNo++;
						}
						} else {
						%>


						<tr>
							<td align="center" colspan="<%=(!"".equals(strCommCd)) ? "6" : "5"%>"><%=CommDef.Message.NO_DATA%>
							</td>
						</tr>
						<%
						}
						%>


					</tbody>
				</table>
				<div class="board_list_btn right">
					<input type="button" class="btn blue" value="코드 등록" onclick="fWrite('<%=CommonUtil.nvl(strCdType)%>','')">
				</div>
			</div>
		</div>
	</div>
</body>
</html>
