<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>


<%
Map reqMap = (Map) request.getAttribute("reqMap");
Map dbMap = (Map) request.getAttribute("dbMap");
List lstFile = (List) request.getAttribute("lstFile");

List lstUserList = (List) request.getAttribute("userList");
List lstMrg = (List) request.getAttribute("mrgList");

int nFileInputCnt = 1;

UploadUtil upUtil = new UploadUtil(request);
ServiceUtil sUtil = new ServiceUtil();

String strParam = "keykind=" + CommonUtil.nvl(reqMap.get("keykind"));
strParam += "&keyword=" + CommonUtil.nvl(reqMap.get("keyword"));
strParam += "&page_now=" + CommonUtil.nvl(reqMap.get("page_now"));
strParam += "&up_menu_no=" + CommonUtil.nvl(reqMap.get("up_menu_no"));
strParam += "&parent_menu_no=" + CommonUtil.nvl(reqMap.get("parent_menu_no"));
strParam += "&menu_gb=" + CommonUtil.nvl(reqMap.get("menu_gb"));

String strWritePage = CommonUtil.nvl(reqMap.get("writepage"));

Map userMap = (Map) SessionUtil.getSessionAttribute(request, "ADM");

String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
String strIFlag = CommonUtil.nvl(reqMap.get("iflag"));
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<jsp:include page="/boffice/include/inc_top.do" flush="false" />
<script src="/ckeditor/ckeditor.js"></script>
<script>
	function fSubmit() {
		vObj = document.frmInput;

		if (!checkEmpty(vObj.menu_nm, "메뉴명을 입력하세요"))
			return;
		if (!checkEmpty(vObj.menu_url, "메뉴URL을 입력하세요"))
			return;
		if (!checkRadioEmpty(vObj.use_yn, "사용여부를 선택하세요"))
			return;
		if (!checkEmpty(vObj.ord, "순서를 입력하세요"))
			return;

		vObj.submit();
	}
</script>

</head>

<body>
	<jsp:include page="/boffice/include/inc_header.do" flush="false" />
	<div id="main">
		<!-- Columns -->
		<div id="cols" class="box">
			<!-- Content (Right Column) -->
			<div id="section">
				<div id="shadowDiv" class="configShadowDiv">
					<!-- Table (TABLE) -->
					<table class="board_list_normal">
						<form autocomplete="off" name="frmInput" method="post" enctype="multipart/form-data" action="/boffice/menu/menuWork.do">
							<input name="iflag" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("iflag"), CommDef.ReservedWord.INSERT)%>">
							<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no"))%>">
							<input name="up_menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("up_menu_no"))%>">
							<input name="top_menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("top_menu_no"))%>">
							<input name="parent_menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("parent_menu_no"))%>">
							<input name="menu_gb" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_gb"))%>">
							<input name="returl" type="hidden" value="/boffice/menu/menuList.do">
							<input name="param" type="hidden" value="<%=CommonUtil.nvl(strParam)%>">

							<tr>
								<th>메뉴명</th>
								<td>
									<input type="text" style="width: 700px" name="menu_nm" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("MENU_NM"))%>" maxlength="50" />
								</td>
							</tr>
							<tr>
								<th>메뉴 URL</th>
								<td>
									<input type="text" style="width: 700px" name="menu_url" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("MENU_URL"))%>" maxlength="250" />
								</td>
							</tr>

							<tr>
								<th>메뉴 Target</th>
								<td>
									<select name="url_target" class="input-text">
										<%=sUtil.getSelectBox("URLTGT", CommonUtil.nvl(dbMap.get("URL_TARGET")))%>
									</select>
								</td>
							</tr>
							<!-- 
				                <tr>
								    <th >메뉴 형식</th>
								    <td >
								       <select name="menu_fmt_cd" class="input-text">
								          sUtil.getSelectBox("MENUFMT", CommonUtil.nvl(dbMap.get("MENU_FMT_CD")))))) %>
								       </select>	
		                            </td>
								</tr>
		 -->

							<tr>
								<th>게시판 번호</th>
								<td>
									<select name="brd_mgrno" class="input-text">
										<option value="">선택하세요</option>
										<%
										if (lstMrg != null && !lstMrg.isEmpty()) {
											for (int nLoop = 0; nLoop < lstMrg.size(); nLoop++) {
												Map mapMgr = (Map) lstMrg.get(nLoop);

												String strSel = "";

												if (CommonUtil.nvl(dbMap.get("BRD_MGRNO")).equals(CommonUtil.nvl(mapMgr.get("BRD_MGRNO")))) {
											strSel = " selected ";
												}
										%>
										<option value="<%=mapMgr.get("BRD_MGRNO")%>" <%=strSel%>>[<%=mapMgr.get("BRD_MGRNO")%>]
											<%=mapMgr.get("BRD_NM")%></option>
										<%
										}
										}
										%>
									</select>
								</td>
							</tr>


							<tr>
								<th>메뉴설명</th>
								<td colspan="3">
									<%
									String strCtnt = (strIFlag.equals(CommDef.ReservedWord.UPDATE)) ? CommonUtil.nvl(dbMap.get("MENU_DESC")) : "";
									%>
									<textarea id="menu_desc" name="menu_desc" maxlength="65536" style="width: 100%; height: 300px" class='ckeditor'><%=CommonUtil.recoveryLtGt(CommonUtil.getReplaceToHtml(strCtnt))%></textarea>
								</td>
							</tr>

							<%-- <tr>
								    <th >관리부서</th>
								    <td ><input type="text" style="width:700px" name="dept_nm" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("DEPT_NM")) %>"  maxlength="50"/></td>
								</tr>
		 
		  		                <tr>
								    <th >부서전화번호</th>
								    <td ><input type="text" style="width:300px" name="dept_telno" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("DEPT_TELNO")) %>"  maxlength="30"/></td>
								</tr>
		 
		   		                <tr>
								    <th >담당자명</th>
								    <td ><input type="text" style="width:300px" name="dept_charge_nm" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("DEPT_CHARGE_NM")) %>"  maxlength="50"/></td>
								</tr>
		 
		    		            <tr>
								    <th >담당자 이메일</th>
								    <td ><input type="text" style="width:300px" name="dept_email" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("DEPT_EMAIL")) %>"  maxlength="50"/></td>
								</tr> --%>

							<tr>
								<th>사용여부</th>
								<td>
									<input type="radio" name="use_yn" class="input-text" value="Y" id="use_yn_y" <%=("Y".equals(CommonUtil.nvl(dbMap.get("USE_YN")))) ? " checked " : ""%> />
									<label for="use_yn_y">사용</label>
									<input type="radio" name="use_yn" class="input-text" value="N" id="use_yn_N" <%=("N".equals(CommonUtil.nvl(dbMap.get("USE_YN")))) ? " checked " : ""%> />
									<label for="use_yn_N">사용하지 않음</label>
								</td>
							</tr>

							<tr>
								<th>순서</th>
								<td>
									<input type="text" name="ord" style="width: 50px; IME-MODE: disabled;" onkeypress="onlysu();" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ORD"))%>" maxlength="5" />
								</td>
							</tr>
							<%
							// if ( "0".equals(CommonUtil.nvl(reqMap.get("up_menu_no")) )) {
							%>
							<%-- <tr>
								    <th >탑 정상 이미지</th>
								    <td >
				 	                 <%=upUtil.addInnerFileHtml(lstFile, nFileInputCnt, CommDef.IMG_MENU_NORMAL)%>
				                    </td>
								</tr>
								
				                <tr>
								    <th >탑 선택 이미지</th>
								    <td >
				 	                 <%=upUtil.addInnerFileHtml(lstFile, nFileInputCnt, CommDef.IMG_MENU_CHOOSE)%>
				                    </td>
								</tr> --%>
							<%
							// } else {
							%>
							<%-- <tr>
								    <th >로고 이미지</th>
								    <td >
				 	                 <%=upUtil.addInnerFileHtml(lstFile, nFileInputCnt, CommDef.IMG_MENU_LEFTLOG, true)%>
				                    </td>
								</tr>						
								
				                <tr>
								    <th >로고 설명 이미지</th>
								    <td >
				 	                 <%=upUtil.addInnerFileHtml(lstFile, nFileInputCnt, CommDef.IMG_MENU_LEFTLOG_TEXT, true)%>
				                    </td>
								</tr>						
								
		  					
				                <tr>
								    <th >내용타이틀 이미지</th>
								    <td >
				 	                 <%=upUtil.addInnerFileHtml(lstFile, nFileInputCnt, CommDef.IMG_MENU_TITLE)%>
				                    </td>
								</tr>					 --%>
							<%
							//}
							%>
							<tr>
								<td colspan="4" class="buttonTd" style="text-align: center">
									<button type="button" onclick="fSubmit();">완료</button>
									<button type="button" onclick="location.href='/boffice/menu/menuList.do?<%=strParam%>'">목록</button>
								</td>
							</tr>
					</table>
				</div>
			</div>
			</section>
			<%-- <div class="btn_right">
		                <a href="javascript:fSubmit()"><img src="/boffice/image/design/btn_modok.gif" alt="완료" /></a>
		                 <a href="/boffice/menu/menuList.do?<%=strParam%>"><img src="/boffice/image/design/btn_list.gif" alt="목록" /></a>
		            </div> --%>
			<!-- /content -->

		</div>
		<!-- /cols -->

		<!-- Footer -->
		<!--<jsp:include page="/boffice/include/inc_footer.do" flush="false"/>-->
		<!-- /footer -->

	</div>
	<!-- /main -->

</body>
</html>