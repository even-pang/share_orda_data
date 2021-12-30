<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>

<%
	//

	Map brdMgrMap = (Map) request.getAttribute("brdMgrMap");

	Map reqMap = (Map) request.getAttribute("reqMap");
	List lstRs = (List) request.getAttribute("list");
	int nRowCount = CommonUtil.getNullInt((String) request.getAttribute("count"), 0);

	String strLinkPage = "/boffice/boardList.do"; // request.getRequestURL().toString(); // 현재 페이지

	String strParam = CommonUtil.getRequestQueryString(request);
	String strKeykind = CommonUtil.nvl(request.getAttribute("keykind"), "");
	int nPageNow = CommonUtil.getNullInt(reqMap.get("page_now"), 1);
	int nPerPage = CommonUtil.getNullInt(reqMap.get("page_row"), CommDef.PAGE_ROWCOUNT);

	String strCateTitle = CommonUtil.nvl(brdMgrMap.get("CATE_CD_TITLE"));
	String strCateUseYn = CommonUtil.nvl(brdMgrMap.get("CATE_CD_USE_YN"));
	String strSortOrd = CommonUtil.nvl(brdMgrMap.get("ORD_USE_YN"));

	String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));

	String strTermUseYn = CommonUtil.nvl(brdMgrMap.get("TERM_USE_YN"));

	int nCols = 6;

	if ("Y".equals(strTermUseYn))
		nCols++;

	nCols += ("Y".equals(strCateUseYn)) ? 1 : 0;
	nCols += ("".equals(strSortOrd)) ? 0 : 1;
	nCols += ("162".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) ? 1 : 0;
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="/boffice/include/inc_top.do" flush="false" />



<script>
	function fWrite() {
		vObj = document.frmWrite;

		vObj.brd_no.value = '';
		vObj.action = "/boffice/boardInput.do";

		vObj.submit();
	}
	function fModify(strMgrno) {
		vObj = document.frmWrite;

		vObj.brd_no.value = strMgrno;

		vObj.action = "/boffice/boardInput.do";

		vObj.submit();
	}

	function fView(strMgrno) {
		vObj = document.frmWrite;

		vObj.brd_no.value = strMgrno;

		vObj.action = "/boffice/boardView.do";

		vObj.submit();
	}

	function fExcelFormUpload() {
		vObj = document.getElementById("excel_upload");

		if (vObj.style.display == '') {
			vObj.style.display = 'none';
		} else {
			vObj.style.display = '';
		}

	}

	function fExcelUpload() {
		vObj = document.frmExcel;

		if (!checkEmpty(vObj.file_nm, "엑셀파일을 선택하여 주십시오"))
			return;

		if (!fileExcelTypeCheck(vObj.file_nm))
			return;

		vObj.submit();

	}

	function fAllDelete() {
		vObj = document.frmList;

		if (checkRadioCount(vObj.chk_brd_no) == 0) {
			alert("삭제할 데이터를 선택하여 주십시오");
			return;
		}

		if (!confirm("선택하신 데이터를 삭제하시겠습니까?"))
			return;

		frmWrite.brd_no.value = checkRadioValue(vObj.chk_brd_no);
		frmWrite.action = "/boffice/boardDelete.do";

		frmWrite.submit();

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
				<div class="board_list_top">
					<form autocomplete="off" action="<%=strLinkPage%>" method="post" id="search">
						<div class="board_search_wrap">
							<div class="ipt_group">
								<p class="sch_text w_show">검색구분 :</p>
								<select id="sch_gubun" name="keykind" class="ipt" title="sns" style="width: 150px;">
									<option value="titCon">제목 + 내용</option>
									<option value="rgDate">작성일자</option>
								</select>
								<div class="txtSchField schField">
									<input name="brd_mgrno" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno"))%>" /> <input name="brd_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no"))%>" /> <input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>" /> <input type="text" id="search" name="keyword" value="<%=CommonUtil.nvl(reqMap.get("keyword"))%>" class="ipt schIpt" placeholder="검색어를 입력하세요" style="width: 30%;" onKeyDown="if(event.keyCode==13) go_search();" />
								</div>
								<div class="dateSchField schField" style="display: none;">
									<div style="width: 100%; clear: both;" class="m_show"></div>
									<div class="dataSearch" style="margin-left: 15px;">
										<div class="ipt_group datepicker">
											<input type="text" id="sch_start_date" name="sch_start_date" class="ipt schIpt dataIpt" placeholder="시작일" style="width: 230px;"> <label for="sch_start_date" class="btn square trans"><i class="k-icon k-i-calendar"></i></label>
										</div>
									</div>
									<p class="sch_text w_show" style="margin-left: 15px;">~</p>
									<div class="dataSearch">
										<div class="ipt_group datepicker">
											<input type="text" id="sch_end_date" name="sch_end_date" class="ipt schIpt dataIpt" value="" placeholder="종료일" style="width: 230px;" /> <label for="sch_end_date" class="btn square trans"><i class="k-icon k-i-calendar"></i></label>
										</div>
									</div>
								</div>
								<div class="sch_btn_wrap">
									<span class="ipt_right addon"> <a href="javascript:this.submit();" class="btn blue">검색</a> <a href="" class="btn gray">초기화</a></span>
								</div>
							</div>
						</div>
					</form>
					<div class="board_list_info">
						전체 <span id="totalCount">11</span>개, 현재 페이지 <span id="totalCount">1</span>/2
					</div>
				</div>
				<form autocomplete="off" name="frmWrite" method="post" action="/boffice/boardInput.do">
					<input name="keykind" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keykind"))%>" />
					<input name="keyword" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keyword"))%>" />
					<input name="page_now" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("page_now"))%>" />
					<input name="returl" type="hidden" value="<%=strLinkPage%>" />
					<input name="brd_mgrno" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno"))%>" />
					<input name="brd_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no"))%>" />
					<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>" />
					<input name="param" type="hidden" value="<%=strParam%>" />
				</form>
				<table class="board_list_normal">

					<form autocomplete="off" name="frmList" method="post" action="/boffice/boardDelete.do">

						<%
							if ("130".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) {
						%>
						<!-- 전화번호 -->
						<thead>
							<tr>
								<th scope="col"><input name="all_brd_no" type="checkbox" onclick="checkRadioToggle(document.frmList, this.checked)" />선택</th>
								<th scope="col">번호</th>
								<%
									if ("Y".equals(strCateUseYn)) {
								%>
								<th scope="col"><%=strCateTitle%></th>
								<%
									}
								%>
								<th scope="col">구분</th>
								<th scope="col">담당자</th>
								<th scope="col">내선번호</th>
								<th scope="col">연락처</th>
								<th scope="col">등록일</th>
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
								<td>
									<input name="chk_brd_no" value="<%=CommonUtil.nvl(rsMap.get("BRD_NO"))%>" type="checkbox" />
								</td>
								<td><%=iSeqNo%></td>
								<%
									if ("Y".equals(strCateUseYn)) {
								%>
								<td><%=CommonUtil.nvl(rsMap.get("CATE_NM"))%></td>
								<%
									}
								%>
								<td class="textLeft">
									<a href="javascript:fView('<%=CommonUtil.nvl(rsMap.get("BRD_NO"))%>')"><%=CommonUtil.nvl(rsMap.get("TTL"))%></a>
								</td>
								<td><%=CommonUtil.nvl(rsMap.get("ETC_FIELD1"))%></td>
								<td><%=CommonUtil.nvl(rsMap.get("ETC_FIELD2"))%></td>
								<td><%=CommonUtil.nvl(rsMap.get("PHONE_NO"))%></td>
								<td><%=CommonUtil.getDateFormat(rsMap.get("REG_DT"))%></td>

							</tr>
							<%
								iSeqNo--;
										}
									} else {
							%>


							<tr>
								<td align="center" colspan="<%=nCols%>"><%=CommDef.Message.NO_DATA%></td>
							</tr>
							<%
								}
							%>
							<%
								} else {
							%>
							<colgroup>
								<col width="7%" />
								<col width="5%" />
								<%
									if ("Y".equals(strCateUseYn)) {
								%>
								<col width="15%" />
								<%
									}
								%>

								<%
									if ("162".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) {
								%>
								<!-- Articles -->
								<col width="15%" />
								<%
									}
								%>
								<col width="10%" />
								<col width="10%" />
								<col width="*%" />
								<col width="9%" />
								<col width="10%" />
								<col width="10%" />

								<%
									if ("Y".equals(strTermUseYn)) {
								%>
								<col width="17%" />
								<%
									}
								%>


								<%
									if ("114".equals(strBrdMgrno)) {
								%>
								<col width="10%" />
								<col width="10%" />
								<%
									}
								%>


								<%
									if ("Y".equals(strSortOrd)) {
								%>
								<col width="10%" />
								<%
									}
								%>
							</colgroup>
							<thead>
								<tr>
									<th scope="col"><input name="all_brd_no" type="checkbox" onclick="checkRadioToggle(document.frmList, this.checked)" />선택</th>
									<th scope="col">번호</th>
									<%
										if ("Y".equals(strCateUseYn)) {
									%>
									<th scope="col"><%=strCateTitle%></th>
									<%
										}
									%>
									<%
										if ("162".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) {
									%>
									<!-- Articles -->
									<th scope="col">Section</th>
									<%
										}
									%>
									<th scope="col">아이디</th>
									<th scope="col">이름</th>
									<th scope="col">제목</th>
									<%
										if ("228".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) {
									%>
									<!-- Articles -->
									<th scope="col">답변여부</th>
									<%
										}
									%>
									<th scope="col">등록일</th>
									<th scope="col">조회건수</th>

									<%
										if ("Y".equals(strTermUseYn)) {
									%>
									<th scope="col">기간</th>
									<%
										}
									%>

									<%
										if ("114".equals(strBrdMgrno)) {
									%>
									<th scope="col">작성자</th>
									<th scope="col">년월</th>
									<%
										}
									%>
									<%
										if ("Y".equals(strSortOrd)) {
									%>
									<th scope="col">정렬</th>
									<%
										}
									%>

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
									<td>
										<input name="chk_brd_no" value="<%=CommonUtil.nvl(rsMap.get("BRD_NO"))%>" type="checkbox" />
									</td>
									<td><%=iSeqNo%></td>
									<%
										if ("Y".equals(strCateUseYn)) {
									%>
									<td><%=CommonUtil.nvl(rsMap.get("CATE_NM"))%></td>
									<%
										}
									%>
									<%
										if ("162".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) {
									%>
									<!-- Articles -->
									<td class="textLeft"><%=CommonUtil.nvl(rsMap.get("ETC_FIELD2"))%></td>
									<%
										}
									%>
									<td><%=CommonUtil.nvl(rsMap.get("REG_ID"))%></td>
									<td><%=CommonUtil.nvl(rsMap.get("REG_NM"))%></td>
									<td class="textLeft"><%=CommonUtil.getReplyImg(CommonUtil.nvl(rsMap.get("DEPTH")))%><a href="javascript:fView('<%=CommonUtil.nvl(rsMap.get("BRD_NO"))%>')"><%=CommonUtil.nvl(rsMap.get("TTL"))%></a>
									</td>
									<%
										if ("228".equals(CommonUtil.nvl(reqMap.get("brd_mgrno")))) {
									%>
									<!-- Articles -->
									<td><%=CommonUtil.nvl(rsMap.get("DEPTH")).equals("1")
									? CommonUtil.getNullInt(rsMap.get("REPLY_CNT"), 0) > 1 ? "답변완료" : "미완료"
									: ""%></td>
									<%
										}
									%>
									<td><%=CommonUtil.getDateFormat(rsMap.get("REG_DT"), ".")%></td>
									<td><%=CommonUtil.nvl(rsMap.get("VIEW_CNT"))%></td>

									<%
										if ("Y".equals(strTermUseYn)) {
									%>
									<td><%=CommonUtil.getDateFormat(rsMap.get("SDT"))%>~<%=CommonUtil.getDateFormat(rsMap.get("EDT"))%></td>
									<%
										}
									%>

									<%
										if ("114".equals(strBrdMgrno)) {
									%>
									<td><%=CommonUtil.nvl(rsMap.get("REG_NM"))%></td>
									<td><%=CommonUtil.nvl(rsMap.get("ETC_FIELD1"))%></td>
									<%
										}
									%>

									<%
										if ("Y".equals(strSortOrd)) {
									%>
									<td><%=CommonUtil.nvl(rsMap.get("ORD"))%></td>
									<%
										}
									%>

								</tr>
								<%
									iSeqNo--;
											}
										} else {
								%>


								<tr>
									<td align="center" colspan="<%=nCols%>"><%=CommDef.Message.NO_DATA%></td>
								</tr>
								<%
									}
								%>
								<%
									}
								%>

							</tbody>
					</form>

				</table>
				<div class="pagination">
					<%=CommonUtil.getAdmPageNavi(strLinkPage, nRowCount, nPageNow, strParam, CommDef.PAGE_PER_BLOCK,
					nPerPage)%>
				</div>
				<div class="board_list_btn right">
					<input type="button" class="btn blue" value="게시물 등록" onClick="fWrite('<%=CommonUtil.nvl(reqMap.get("up_menu_no"), CommDef.TOP_MENU_NO)%>')">
				</div>
			</div>

			<!-- Search -->
		</div>
	</div>
	<!-- /main -->

</body>
</html>
