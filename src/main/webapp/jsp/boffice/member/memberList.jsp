<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
	Map reqMap = (Map) request.getAttribute("reqMap");
	List lstRs = (List) request.getAttribute("list");
	int nRowCount = CommonUtil.getNullInt((String) request.getAttribute("count"), 0);

	String strLinkPage = "/boffice/member/memberList.do"; // request.getRequestURL().toString(); // 현재 페이지

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
		vObj = document.search;
		vObj.user_id.value = '';
		vObj.action = "/boffice/member/memberInput.do";
		vObj.submit();
	}
	function fModify(strMgrno) {
		vObj = document.search;
		vObj.user_id.value = strMgrno;
		vObj.action = "/boffice/member/memberInput.do";

		vObj.submit();
	}
	
	function fSearch() {
		vObj = document.search;
		vObj.submit();
	}
</script>


</head>

<body>
	<jsp:include page="/boffice/include/inc_header.do" flush="false" />
	<div id="main">
		<div class="group">
			<div class="header">
				<h3>Member List</h3>
			</div>
			<div class="body">
			<div class="board_list_top">
					<div class="board_search_wrap">
	                    <div class="ipt_group">
	                   		<form action="<%=strLinkPage%>" method="post" id="search" name="search">
							<input name="page_now" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("page_now"))%>" />
							<input name="returl" type="hidden" value="<%=strLinkPage%>" />
							<input name="user_id" type="hidden" value="" />
							<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>" />
								
		                    	<select id="sch_gubun" name="keykind" class="ipt" title="sns" style="width: 150px;">
		                    		<option value="user"     <%=("user".equals(CommonUtil.nvl(reqMap.get("keykind"))))    ? " selected " : "" %>>이름/아이디</option>
		                    	</select>
		                    	<div class="txtSchField schField">
		                        	<input type="text" id="search" name="keyword" value="<%=CommonUtil.nvl(reqMap.get("keyword")) %>" class="ipt schIpt" placeholder="검색어를 입력하세요" style="width: 30%;" onKeyDown="if(event.keyCode==13) go_search();">
								</div>
		                        <div class="sch_btn_wrap">
		                        	<span class="ipt_right addon">
		                        		<a href="#" onclick="javascript:fSearch();" class="btn blue">검색</a>
		                        	</span>
		                        </div>
	                        </form>
	                    </div> 
					</div>
	            </div>
				<table class="board_list_normal">
					<colgroup>
						<col width="7%" />
						<col width="15%" />
						<col width="13%" />
						<col width="*%" />
						<col width="15%" />
					</colgroup>
					<thead>
						<tr>
							<th scope="col">NO.</th>
							<th scope="col">아이디</th>
							<th scope="col">성함</th>
							<th scope="col">전화번호</th>
							<th scope="col">가입날짜</th>
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
							<td class="center"><%=iSeqNo%></td>
							<td class="center"><a
								href="javascript:fModify('<%=CommonUtil.nvl(rsMap.get("USER_ID"))%>')"><%=CommonUtil.nvl(rsMap.get("USER_ID"))%></a>
							</td>
							<td class="center"><%=CommonUtil.nvl(rsMap.get("USER_NM"))%></td>
							<td class="center"><%=CommonUtil.nvl(rsMap.get("TELNO"))%></td>
							<td class="center"><%=CommonUtil.getDateFormat(rsMap.get("REG_DT"), ".")%></td>
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
					<input type="button" class="btn blue" value="멤버 등록" onClick="fWrite();">
				</div>
			</div>
		</div>
	</div>

</body>
</html>
