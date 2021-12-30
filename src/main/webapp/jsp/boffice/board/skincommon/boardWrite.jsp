<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>

<%
	Map  reqMap    = (Map)  request.getAttribute( "reqMap" );
	Map  dbMap     = (Map)  request.getAttribute( "dbMap" );
	Map  brdMgrMap = (Map)  request.getAttribute( "brdMgrMap" );
	List lstFile   = (List) request.getAttribute( "lstFile" );

	List lstUserList = (List) request.getAttribute( "userList" );
	
	List lstUrlTgt = (List) request.getAttribute( "lstUrlTgt" ); // URL Target 조회
	
	ServiceUtil sUtil = new ServiceUtil();
	UploadUtil  upUtil = new UploadUtil(request);
	
    String strParam  = "keykind="  + CommonUtil.nvl( reqMap.get( "keykind" ));
    strParam += "&keyword="        + CommonUtil.nvl( reqMap.get( "keyword" ));
    strParam += "&page_now="       + CommonUtil.nvl( reqMap.get( "page_now"));	
    strParam += "&brd_mgrno="      + CommonUtil.nvl( reqMap.get( "brd_mgrno"));
    strParam += "&menu_no="        + CommonUtil.nvl( reqMap.get( "menu_no"));
    
    String strWritePage = CommonUtil.nvl(reqMap.get("writepage"));
    
    Map userMap      = 	(Map)SessionUtil.getSessionAttribute(request,"ADM");

    String strMenuNo = CommonUtil.nvl( reqMap.get( "menu_no"));
    String strIFlag  = CommonUtil.nvl( reqMap.get( "iflag"));
    
    String strBrdMgrno =  CommonUtil.nvl(reqMap.get("brd_mgrno"));
    
	String strUserType = CommonUtil.nvl(userMap.get("USER_TYPE"));

%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<jsp:include page="/boffice/include/inc_top.do" flush="false" />

<!-- ------------------ HTML Editor Control Start ----------------------- -->
<script src="/ckeditor/ckeditor.js"></script>
<!-- ------------------ HTML Editor Control End ----------------------- -->


<script>

   function fSubmit()
   {
	   vObj = document.frmInput;

	   if (!checkEmpty     (vObj.ttl,  "제목을 입력하세요")) return;
	   
	   <% if ("115".equals(strBrdMgrno) || "178".equals(strBrdMgrno) ) { %>
	   try {
		   
		   if ( vObj.cate_cd.value !='CERPR40' )
		   {
		        vCtnt = trim(GetInnerHTML('ans_ctnt'));
		        vCtnt = vCtnt.replace("&nbsp;","");
	
	            if ( vCtnt != "") {
	            	if (confirm("처리상태를 답변완료로 변경하시겠습니까?")) {
	            		vObj.cate_cd.value ='CERPR40';
	            	}
	            }
		   }
	   } catch (e) {}            
	   <% } %>
	   
	   try {
		   if ( !fileUpCheck(vObj.file_nm )) return;
	   } catch (e) {}
	   	   
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
					<form autocomplete="off" name="frmInput" method="post" enctype="multipart/form-data" action="/boffice/boardWork.do">
						<input name="iflag" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("iflag"), CommDef.ReservedWord.INSERT) %>" />
						<input name="brd_mgrno" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno")) %>" />
						<input name="brd_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no")) %>" />
						<input name="returl" type="hidden" value="/boffice/boardList.do" />
						<input name="param" type="hidden" value="<%=CommonUtil.nvl(strParam) %>" />
						<input name="old_brd_reg_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no")) %>" />
						<input name="menu_no" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no")) %>" />
						<table class="board_list_normal">
							<colgroup>
								<col width="5%" />
								<col width="*%" />
								<col width="10%" />
								<col width="20%" />
							</colgroup>
							<tr>
								<th scope="col">제목</th>
								<td scope="col" colspan="3"><input type="text" style="width: 700px" name="ttl" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("TTL")).replace("\"", "'") %>" maxlength="300" /></td>
							</tr>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("TOP_FIX_USE_YN")))) {%>
							<tr>
								<th scope="col">상단공지여부</th>
								<td scope="col" colspan="3"><select name="notice_yn">
										<option value="">선택하세요</option>
										<%=sUtil.getComboBox("USEYN", CommonUtil.nvl(dbMap.get("NOTICE_YN"))) %>
								</select></td>
							</tr>
							<% }%>

							<% if ( "ALL".equals(CommonUtil.nvl(brdMgrMap.get("SITE_GB")))) {%>
							<tr>
								<th scope="col">적용사이트</th>
								<td scope="col" colspan="3">
									<%-- <select name="site_gb">
									          <option value="">선택하세요</option>
									           <%=sUtil.getComboBox("SITEGB", CommonUtil.nvl(dbMap.get("SITE_GB"))) %> 
									       </select> --%> HOME <input type="hidden" name="site_gb" value="HOME" />
								</td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("CATE_CD_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("CATE_CD_TITLE"))%></th>
								<td scope="col" colspan="3"><select name="cate_cd">
										<option value="">선택하세요</option>
										<%=sUtil.getComboBox(CommonUtil.nvl(brdMgrMap.get("CATE_CD")), CommonUtil.nvl(dbMap.get("CATE_CD"))) %>
								</select></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("WRITER_USE_YN")))) {%>
							<tr>
								<th scope="col">작성자</th>
								<td scope="col" colspan="3"><input type="text" name="reg_nm" style="width: 100px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("REG_NM")) %>" maxlength="30" /></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("PHONE_USE_YN")))) {%>
							<tr>
								<th scope="col">연락처</th>
								<td scope="col" colspan="3"><input type="text" name="phone_no" style="width: 100px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("PHONE_NO")) %>" maxlength="20" /></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("EMAIL_USE_YN")))) {%>
							<tr>
								<th scope="col">이메일</th>
								<td scope="col" colspan="3"><input type="text" name="email" style="width: 200px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("EMAIL")) %>" maxlength="50" /></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("EMAIL_RECV_USE_YN")))) {%>
							<tr>
								<th scope="col">이메일 수신여부</th>
								<td scope="col" colspan="3"><input type="radio" name="email_recv_yn" class="input-text" value="Y" id="email_recv_yn_y" <%=("Y".equals(CommonUtil.nvl(dbMap.get("EMAIL_RECV_YN")))) ? " checked " : "" %> /> <label for="use_yn_y">받음</label> <input type="radio" name="email_recv_yn" class="input-text" value="N" id="email_recv_yn_N" <%=("N".equals(CommonUtil.nvl(dbMap.get("EMAIL_RECV_YN")))) ? " checked " : "" %> /> <label for="use_yn_N">받지 않음</label></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("TERM_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("TERM_TITLE"), "기간") %></th>
								<td scope="col" colspan="3"><input type="date" name="sdt" style="width: 150px" class="input-text" value="<%=CommonUtil.getDateFormat(dbMap.get("SDT"), "-") %>" maxlength="10" /> &nbsp;
								</a> ~ <input type="date" name="edt" style="width: 150px" class="input-text" value="<%=CommonUtil.getDateFormat(dbMap.get("EDT"), "-") %>" maxlength="10" /> &nbsp; 
								</a></td>
							</tr>
							<% } else if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("DATE_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("DATE_TITLE"), "날짜") %></th>
								<td scope="col" colspan="3"><input type="text" name="sdt" style="width: 80px" class="input-text" value="<%=CommonUtil.getDateFormat(dbMap.get("SDT")) %>" maxlength="10" /> &nbsp; <a href="javascript:cal_open(frmInput.sdt)"> <img src="/Common/icon_cal.gif" alt="달력">
								</a></td>
							</tr>
							<% } %>



							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ADDR_USE_YN")))) {%>
							<tr>
								<th scope="col">주소</th>
								<td scope="col" colspan="3">우편&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;번호: <input type="text" name="zip_cd" style="width: 80px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ZIP_CD")) %>" maxlength="7" readonly /> &nbsp;
									&nbsp; <a href="#">[우편번호]</a> <br /> 주&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;
									&nbsp;소: <input type="text" name="addr1" style="width: 700px" class="input-text" value="<%=CommonUtil.getDateFormat(dbMap.get("ADDR1")) %>" readonly /> <br /> 나머지주소: <input type="text" name="addr2" style="width: 700px" class="input-text" value="<%=CommonUtil.getDateFormat(dbMap.get("ADDR2")) %>" />
								</td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("REGDT_USE_YN")))) {%>
							<tr>
								<th scope="col">등록일</th>
								<td scope="col" colspan="3"><input type="text" name="reg_dt" style="width: 80px" class="input-text" value="<%=CommonUtil.getDateFormat(dbMap.get("REG_DT")) %>" maxlength="10" /> &nbsp; <a href="javascript:cal_open(frmInput.reg_dt)"> <img src="/Common/icon_cal.gif" alt="달력">
								</a></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("URL_USE_YN")))) {%>
							<tr>
								<th scope="col">URL</th>
								<td scope="col"><input type="text" name="url" style="width: 350px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("URL")) %>" maxlength="250" /></td>
								<th scope="col">URL 타켓</th>
								<td scope="col"><select name="url_target">
										<%=sUtil.getMakeSelectBox(lstUrlTgt, CommonUtil.nvl(dbMap.get("URL_TARGET"))) %>
								</select></td>

							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ORD_USE_YN")))) {%>
							<tr>
								<th scope="col">정렬순서</th>
								<td scope="col" colspan="3"><input type="text" name="ord" style="width: 60px" class="input-text" onkeypress="onlysu();" style="IME-MODE:disabled;" value="<%=CommonUtil.nvl(dbMap.get("ORD")) %>" maxlength="5" /></td>
							</tr>

							<% } %>


							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("POPSIZE_USE_YN")))) {%>
							<tr>
								<th scope="col">팝업 넓이</th>
								<td scope="col" colspan="3"><input type="text" name="win_width" style="width: 60px" class="input-text" onkeypress="onlysu();" style="IME-MODE:disabled;" value="<%=CommonUtil.nvl(dbMap.get("WIN_WIDTH")) %>" maxlength="5" /></td>
							</tr>

							<tr>
								<th scope="col">팝업 높이</th>
								<td scope="col" colspan="3"><input type="text" name="win_height" style="width: 60px" class="input-text" onkeypress="onlysu();" style="IME-MODE:disabled;" value="<%=CommonUtil.nvl(dbMap.get("WIN_HEIGHT")) %>" maxlength="5" /></td>
							</tr>

							<% }%>


							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("SECRET_USE_YN")))) {%>
							<tr>
								<th scope="col">비밀글</th>
								<td scope="col" colspan="3"><%=sUtil.getRadioBox("USEYN","secret_yn", CommonUtil.nvl(dbMap.get("SECRET_USE_YN"), "N")) %></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD1_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD1_TITLE"), "추가항목1") %></th>
								<td scope="col" colspan="3"><input type="text" name="etc_field1" style="width: 500px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD1")) %>" maxlength="200" /></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD2_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD2_TITLE"), "추가항목2") %></th>
								<td scope="col" colspan="3"><input type="text" name="etc_field2" style="width: 500px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD2")) %>" maxlength="200" /></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD3_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD3_TITLE"), "추가항목3") %></th>
								<td scope="col" colspan="3"><input type="text" name="etc_field3" style="width: 500px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD3")) %>" maxlength="200" /></td>
							</tr>
							<% }%>

							<% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD4_USE_YN")))) {%>
							<tr>
								<th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD4_TITLE"), "추가항목4") %></th>
								<td scope="col" colspan="3"><input type="text" name="etc_field4" style="width: 500px" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD4")) %>" maxlength="200" /></td>
							</tr>
							<% }%>
							<!--						 
					                <tr>
									    <th scope="col">댓글 사용 여부</th>
									    <td scope="col" colspan="3"><%=sUtil.getRadioBox("USEYN","cmt_use_yn", CommonUtil.nvl(dbMap.get("CMT_USE_YN"), "N")) %>
									    </td>
									</tr>	
									
					                <tr>
									    <th scope="col">첨부파일 개수</th>
									    <td scope="col" colspan="3"><input type="text"  name="attach_file_cnt" style="width:50px;IME-MODE:disabled;" onkeypress="onlysu();"  class="input-text"  value="<%=CommonUtil.nvl(dbMap.get("ATTACH_FILE_CNT"), "0") %>" maxlength="2"/></td>
									</tr>							 
															 
			-->
							<%
							if ("228".equals(CommonUtil.nvl(reqMap.get("brd_mgrno"))) && CommonUtil.nvl(reqMap.get("iflag")).equals("REPLY")) {
							%>
							<!-- Articles -->
							<%
							if ("Y".equals(CommonUtil.nvl(brdMgrMap.get("CTNT_USE_YN")))) {
							%>
							<tr>
								<th scope="col">내용</th>
								<td scope="col" colspan="3"><%=CommonUtil.recoveryLtGt(CommonUtil.getReplaceToHtml(dbMap.get("CTNT")))%></td>
								</td>
							</tr>
							<tr>
								<th scope="col">답변</th>
								<td scope="col" colspan="3"><textarea name="ctnt" cols="70" rows="7" id="ctnt"></textarea></td>
							</tr>
							<script>
								// Replace the <textarea id="editor1"> with a CKEditor
								// instance, using default configuration.
								CKEDITOR.replace('ctnt');
							</script>
							<%
							}
							%>

							<%
							} else {
							%>
							<tr>
								<th scope="col">내용</th>
								<td scope="col" colspan="3"><textarea name="ctnt" cols="70" rows="7" id="ctnt"><%=CommonUtil.nvl(dbMap.get("CTNT"))%></textarea></td>
							</tr>
							<script>
								// Replace the <textarea id="editor1"> with a CKEditor
								// instance, using default configuration.
								CKEDITOR.replace('ctnt');
							</script>
							<tr>
								<%
								}
								%>

								<!--  사이버민원실 -->
								<tr>
									<th scope="col">사용여부</th>
									<td scope="col" colspan="3"><%=sUtil.getRadioBox("USEYN", "use_yn", CommonUtil.nvl(dbMap.get("USE_YN"), "Y"))%></td>
								</tr>

								<%
								int nFileCnt = CommonUtil.getNullInt(brdMgrMap.get("ATTACH_FILE_CNT"), -1);

								if (nFileCnt > 0) {
								%>
								<tr>
									<th scope="col">첨부파일</th>
									<td scope="col" colspan="3"><%=upUtil.addInnerFileHtml(lstFile, nFileCnt)%></td>
								</tr>
								<%
								}
								%>
								<tr>
									<td colspan="4" class="buttonTd" style="text-align: center">
										<button type="button" onclick="fSubmit();">완료</button>
										<button type="button" onclick="location.href='/boffice/boardList.do?<%=strParam%>'" class="">목록</button>
									</td>
								</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
		<!-- /content -->

	</div>
	<!-- /cols -->

	<!-- Footer -->
	<!--<jsp:include page="/boffice/include/inc_footer.do" flush="false"/>-->
	<!-- /footer -->

	<!-- /main -->

	<jsp:include page="/Common/inc_calendar.jsp" flush="false" />

</body>
</html>