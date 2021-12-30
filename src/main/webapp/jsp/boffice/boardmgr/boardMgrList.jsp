<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
	Map reqMap = (Map) request.getAttribute("reqMap");
	List lstRs = (List) request.getAttribute("list");
	int nRowCount = CommonUtil.getNullInt((String) request.getAttribute("count"), 0);

	String strLinkPage = "/boffice/boardmgr/boardMgrList.do"; // request.getRequestURL().toString(); // 현재 페이지

	String strParam = CommonUtil.getRequestQueryString(request);
	String strKeykind = CommonUtil.nvl(request.getAttribute("keykind"), "");
	int nPageNow = CommonUtil.getNullInt(reqMap.get("page_now"), 1);
	int nPerPage = CommonUtil.getNullInt(reqMap.get("page_row"), CommDef.PAGE_ROWCOUNT);
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="/boffice/include/inc_top.do" flush="false" />



<script>
	function fWrite() {
		vObj = document.frmWrite;

		vObj.brd_mgrno.value = '';
		vObj.action = "/boffice/boardmgr/boardMgrInput.do";

		vObj.submit();
	}
	function fModify(strMgrno) {
		vObj = document.frmWrite;

		vObj.brd_mgrno.value = strMgrno;

		vObj.action = "/boffice/boardmgr/boardMgrInput.do";

		vObj.submit();
	}

	function fDelete(strMgrno) {
		vObj = document.frmWrite;

		if (!confirm("삭제하시겠습니까?"))
			return;

		vObj.brd_mgrno.value = strMgrno;
		vObj.action = "/boffice/boardmgr/boardMgrDelete.do";

		vObj.submit();
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
				<form autocomplete="off" name="frmWrite" method="post" action="/boffice/boardmgr/boardMgrInput.do">
					<input name="keykind" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keykind"))%>" />
					<input name="keyword" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keyword"))%>" />
					<input name="page_now" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("page_now"))%>" />
					<input name="returl" type="hidden" value="<%=strLinkPage%>" />
					<input name="brd_mgrno" type="hidden" value="" />
					<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>" />
				</form>
				<table class="board_list_normal">
					<colgroup>
						<col width="5%" />
						<col width="10%" />
						<col width="10%" />
						<col width="*%" />
						<col width="15%" />
						<col width="8%" />
						<col width="10%" />
						<col width="10%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="col">번호</th>
							<th scope="col">유형</th>
							<th scope="col">게시판번호</th>
							<th scope="col">게시판명</th>
							<th scope="col">사이트명</th>
							<th scope="col">사용여부</th>
							<th scope="col">등록일</th>
							<th scope="col">게시물건수</th>
						</tr>
					</thead>
					<tbody>

						<%
							if (lstRs != null && lstRs.size() > 0) {

								int iSeqNo = nRowCount - (nPageNow - 1) * nPerPage;
								for (int iLoop = 0; iLoop < lstRs.size(); iLoop++) {
									Map rsMap = (Map) lstRs.get(iLoop);
						%>
						<tr <%=((iLoop % 2) == 1) ? "class='bg'" : ""%>>
							<td><%=iSeqNo%></td>
							<td><%=CommonUtil.nvl(rsMap.get("BRD_SKIN_CD"))%></td>
							<td><%=CommonUtil.nvl(rsMap.get("BRD_MGRNO"))%></td>
							<td class="textLeft">
								<a href="javascript:fModify('<%=CommonUtil.nvl(rsMap.get("BRD_MGRNO"))%>')"><%=CommonUtil.nvl(rsMap.get("BRD_NM"))%></a>
							</td>
							<td><%=CommonUtil.nvl(rsMap.get("BRD_SNM"))%></td>
							<td><%=CommonUtil.nvl(rsMap.get("USE_YN"))%></td>
							<td><%=CommonUtil.getDateFormat(rsMap.get("REG_DT"))%></td>
							<td><%=CommonUtil.nvl(rsMap.get("BRD_REG_CNT"))%></td>
						</tr>
						<%
							iSeqNo--;
								}
							} else {
						%>


						<tr>
							<td align="center" colspan="8"><%=CommDef.Message.NO_DATA%>
							</td>
						</tr>
						<%
							}
						%>


					</tbody>
				</table>
				<div class="pagination">
					<%=CommonUtil.getAdmPageNavi(strLinkPage, nRowCount, nPageNow, strParam, CommDef.PAGE_PER_BLOCK,
					nPerPage)%>
				</div>
				<div class="board_list_btn right">
					<input type="button" class="btn blue" value="게시물 등록" onClick="fWrite('<%=CommonUtil.nvl(reqMap.get("up_menu_no"), CommDef.TOP_MENU_NO)%>')">
				</div>
			</div>
		</div>
	</div>

</body>
</html>
