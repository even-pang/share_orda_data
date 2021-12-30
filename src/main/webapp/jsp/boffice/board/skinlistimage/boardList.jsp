<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
	Map brdMgrMap = (Map) request.getAttribute("brdMgrMap");

	Map reqMap = (Map) request.getAttribute("reqMap");
	List lstRs = (List) request.getAttribute("list");
	int nRowCount = CommonUtil.getNullInt((String) request.getAttribute("count"), 0);

	String strLinkPage = "/boffice/boardList.do"; // request.getRequestURL().toString(); // 현재 페이지

	String strParam = CommonUtil.getRequestQueryString(request);
	String strKeykind = CommonUtil.nvl(request.getAttribute("keykind"), "");
	int nPageNow = CommonUtil.getNullInt(reqMap.get("page_now"), 1);
	int nPerPage = CommonUtil.getNullInt(reqMap.get("page_row"), CommDef.PAGE_ROWCOUNT);

	String strTermUseYn = CommonUtil.nvl(brdMgrMap.get("TERM_USE_YN"));

	int nColSpan = 6;

	if ("Y".equals(strTermUseYn))
		nColSpan++;
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

	function fn_resizeRcmPstSize(win_width) {
		$(".w_scroll").show();
		$(".m_scroll").hide();
		if (win_width >= 1280) {
			$(".rcm_pst_wrap .rcm_pst").css("width", "396px").css("height",
					"374px");
			$(".rcm_pst_wrap .shadow").css("width", "396px").css("height",
					"374px");
			$(".rcm_pst_wrap .imgWrap").css("height", "246px");
			$(".artList > div").css("height", "auto");
			$(".rcm_pst_wrap .shadow").show();
		} else {
			var w = $(".rcm_pst_wrap").outerWidth();
			var w2 = $(".artList > div").outerWidth();
			$(".artList > div").css("height", w2 * 1.2062 + "px");

			var div_w = 0, div_ratio = 0, img_ratio = 0;
			if (win_width < 767) {
				div_w = (w / 2) - 2.5;
				div_ratio = 1.159;
				img_ratio = 1.52;
				$(".rcm_pst_wrap .shadow").hide();
				$(".m_scroll").show();
				$(".w_scroll").hide();
			} else if (win_width < 1024) {
				div_w = (w / 3) - 12;
				div_ratio = 1.159;
				img_ratio = 1.52;
				$(".rcm_pst_wrap .shadow").hide();
			} else if (win_width < 1280) {
				div_w = (w / 3) - 36;
				div_ratio = 0.9444;
				img_ratio = 1.52;
				$(".rcm_pst_wrap .shadow").show();
			}

			$(".rcm_pst_wrap .rcm_pst").css("width", div_w + "px").css(
					"height", (div_w * div_ratio) + "px");
			$(".rcm_pst_wrap .shadow").css("width", div_w + "px").css("height",
					(div_w * div_ratio) + "px");
			$(".rcm_pst_wrap .imgWrap").css(
					"height",
					($(".rcm_pst_wrap .shadow").outerHeight() / img_ratio)
							+ "px");
		}
	}
</script>


</head>

<body>
	<jsp:include page="/boffice/include/inc_header.do" flush="false" />
	<!-- main -->
	<div id="main">
		<div class="group">
			<div class="header">
				<h3><%=CommonUtil.nvl(brdMgrMap.get("BRD_NM"))%></h3>
			</div>
			<div class="body">
				<form autocomplete="off" name="frmWrite" method="post" action="/boffice/boardInput.do">
					<input name="keykind" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keykind"))%>" />
					<input name="keyword" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keyword"))%>" />
					<input name="page_now" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("page_now"))%>" />
					<input name="returl" type="hidden" value="<%=strLinkPage%>" />
					<input name="brd_mgrno" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno"))%>" />
					<input name="brd_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no"))%>" />
					<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>" />
				</form>
				<div class="artListWrap">
					<div class="artList">
						<%
							if (lstRs != null && lstRs.size() > 0) {

								int iSeqNo = nRowCount - (nPageNow - 1) * nPerPage;
								for (int iLoop = 0; iLoop < lstRs.size(); iLoop++) {
									Map rsMap = (Map) lstRs.get(iLoop);

									String strImgFile = CommonUtil.nvl(rsMap.get("FILE_REALNM"));
									boolean bImg = CommonUtil.isImageFile(strImgFile);
						%>
						<div>
							<a href="javascript:fView('<%=CommonUtil.nvl(rsMap.get("BRD_NO"))%>')">
								<div class="imgWrap"><%=(bImg) ? "<img src='" + strImgFile + "'/>" : "&nbsp;"%></div>
								<p class="en bold"><%=CommonUtil.nvl(rsMap.get("TTL"))%></p>
								<p><%=CommonUtil.getDateFormat(rsMap.get("SDT"))%>~<%=CommonUtil.getDateFormat(rsMap.get("EDT"))%></p>
								<p><%=CommonUtil.getDateFormat(rsMap.get("REG_DT"))%></p>
							</a>
						</div>
						<%
							iSeqNo--;
								}
							} else {
						%>
						<div>
							<a href="#">
								<div class="imgWrap">&nbsp;</div>
								<p class="en bold">NODATA</p>
								<p></p>
								<p></p>
							</a>
						</div>
						<%
							}
						%>
					</div>

				</div>
			</div>
			<div class="pagination">
				<%=CommonUtil.getAdmPageNavi(strLinkPage, nRowCount, nPageNow, strParam, CommDef.PAGE_PER_BLOCK,nPerPage)%>
			</div>
			<div class="board_list_btn right">
				<input type="button" class="btn blue" value="게시물 등록" onClick="fWrite('<%=CommonUtil.nvl(reqMap.get("up_menu_no"), CommDef.TOP_MENU_NO)%>')">
			</div>
		</div>
	</div>
	<!-- /main -->
	<script>
		$(document).ready(function() {
			$(".schField").hide();
			if ($("#sch_gubun").val() == "titCon")
				$(".txtSchField").show();
			else if ($("#sch_gubun").val() == "rgDate")
				$(".dateSchField").show();
			$("#sch_gubun").change(function() {
				$(".schField").hide();
				if ($(this).val() == "titCon")
					$(".txtSchField").show();
				else if ($(this).val() == "rgDate")
					$(".dateSchField").show();
			})
		});
		function reset() {
			$("#sch_gubun option:first").attr("selected", "selected");
			$(".schField").hide();
			$(".txtSchField").show();
			$(".schIpt").each(function() {
				$(this).val('');
			})
		}
	</script>
</body>
</html>