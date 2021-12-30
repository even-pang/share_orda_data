<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
Map reqMap = (Map) request.getAttribute("reqMap");
List lstRs = (List) request.getAttribute("list");
int nRowCount = CommonUtil.getNullInt((String) request.getAttribute("count"), 0);

String strLinkPage = "/boffice/menu/menuList.do"; // request.getRequestURL().toString(); // 현재 페이지

String strParam = CommonUtil.getRequestQueryString(request);
String strKeykind = CommonUtil.nvl(request.getAttribute("keykind"), "");
int nPageNow = CommonUtil.getNullInt(reqMap.get("page_now"), 1);
int nPerPage = CommonUtil.getNullInt(reqMap.get("page_row"), CommDef.PAGE_ROWCOUNT);

String strWritePage = "/boffice/menuList.do";
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="/boffice/include/inc_top.do" flush="false" />



<script>
	function fWrite(strUpMenuId) {
		vObj = document.frmWrite;

		vObj.up_menu_no.value = strUpMenuId;
		vObj.action = "/boffice/menu/menuInput.do";

		vObj.submit();
	}
	function fModify(strMenuId, strUpMenuId) {
		vObj = document.frmWrite;

		vObj.menu_no.value = strMenuId;
		vObj.up_menu_no.value = strUpMenuId;
		vObj.action = "/boffice/menu/menuInput.do";

		vObj.submit();
	}

	function fDelete(strMenuId, strUpMenuId) {
		vObj = document.frmWrite;

		if (!confirm("삭제하시겠습니까?"))
			return;

		vObj.menu_no.value = strMenuId;
		vObj.up_menu_no.value = strUpMenuId;
		vObj.action = "/boffice/menu/menuDelete.do";

		vObj.submit();
	}
</script>


</head>

<body>
	<jsp:include page="/boffice/include/inc_header.do" flush="false" />
	<div id="main">
		<div class="group">
			<div class="header">
				<h3>메뉴 관리</h3>
			</div>
			<div class="body">
				<form autocomplete="off" name="frmWrite" method="post"
					action="/boffice/menu/menuInput.do">
					<input name="keykind" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("keykind"))%>" />
					<input name="keyword" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("keyword"))%>" />
					<input name="page_now" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("page_now"))%>" />
					<input name="up_menu_no" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("up_menu_no"))%>" />
					<input name="parent_menu_no" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("parent_menu_no"))%>" />
					<input name="menu_gb" type="hidden"
						value="<%=CommonUtil.nvl(reqMap.get("menu_gb"))%>" />
					<input name="returl" type="hidden" value="<%=strLinkPage%>" />
					<input name="menu_no" type="hidden" value="" />
				</form>
				<table class="board_list_normal">
					<colgroup>
						<col width="4%">
							<col width="4%">
								<col width="50%">
									<col width="4%">
										<col width="4%">
											<col class="w_show" width="4%">
												<col class="w_show" width="30%">
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>메뉴번호</th>
							<th>메뉴명</th>
							<th>Depth</th>
							<th class="w_show">순서</th>
							<th class="w_show">사용여부</th>
							<th class="w_show">관리</th>
						</tr>
					</thead>
					<tbody>

						<%
						if (lstRs != null && lstRs.size() > 0) {

							ServiceUtil sUtil = new ServiceUtil();

							int iSeqNo = nRowCount - (nPageNow - 1) * nPerPage;
							for (int iLoop = 0; iLoop < lstRs.size(); iLoop++) {
								Map rsMap = (Map) lstRs.get(iLoop);
						%>
						<tr <%=((iLoop % 2) == 1) ? "class='bg'" : ""%>>
							<td class="num"><%=iLoop + 1%></td>
							<td class="center" style="cursor: pointer"><%=CommonUtil.nvl(rsMap.get("MENU_NO"))%></td>
							<td class="center postTitle"><a href="javascript: void(0)"
								class="link"><%=CommonUtil.nvl(rsMap.get("MENU_NM"))%></a></td>
							<td class="center"><%=CommonUtil.nvl(rsMap.get("MENU_LVL"))%></td>
							<td class="center w_show"><%=CommonUtil.nvl(rsMap.get("ORD"))%></td>
							<td class="center w_show"><%=CommonUtil.nvl(rsMap.get("USE_YN"))%></td>
							<td class="center state w_show"><span
								style="cursor: pointer;" class="active"
								onclick="location.href='<%=strLinkPage%>?up_menu_no=<%=CommonUtil.nvl(rsMap.get("MENU_NO"))%>&parent_menu_no=<%=CommonUtil.nvl(rsMap.get("UP_MENU_NO"))%>&menu_gb=<%=CommonUtil.nvl(rsMap.get("MENU_GB"))%>'">하위메뉴</span>
								<span style="cursor: pointer;" class="active"
								onclick="fModify('<%=CommonUtil.nvl(rsMap.get("MENU_NO"))%>','<%=CommonUtil.nvl(rsMap.get("UP_MENU_NO"))%>')">수정</span>
								<span style="cursor: pointer;" class="active"
								onclick="fDelete('<%=CommonUtil.nvl(rsMap.get("MENU_NO"))%>','<%=CommonUtil.nvl(rsMap.get("UP_MENU_NO"))%>')">삭제</span>
							</td>
						</tr>
						<%
						iSeqNo--;
						}
						} else {
						%>


						<tr>
							<td align="center" colspan="7"><%=CommDef.Message.NO_DATA%>
							</td>
						</tr>
						<%
						}
						%>


					</tbody>
				</table>
			</div>

			<div class="pagination">
				<%=CommonUtil.getAdmPageNavi(strLinkPage, nRowCount, nPageNow, strParam, CommDef.PAGE_PER_BLOCK, nPerPage)%>
			</div>
			<div class="board_list_btn right">
				<%
				if (CommonUtil.nvl(reqMap.get("parent_menu_no")) != null && CommonUtil.nvl(reqMap.get("parent_menu_no")) != "") {
				%>
				<input type="button" class="btn blue" value="상위메뉴"
					onClick="location.href='<%=strLinkPage%>?up_menu_no=<%=CommonUtil.nvl(reqMap.get("parent_menu_no"))%>&menu_gb=<%=CommonUtil.nvl(reqMap.get("menu_gb"))%>'">
				<%
					}
					%>

				<input type="button" class="btn blue" value="게시물 등록"
					onClick="fWrite('<%=CommonUtil.nvl(reqMap.get("up_menu_no"), CommDef.TOP_MENU_NO)%>')">
			</div>
		</div>
	</div>
</body>
</html>
