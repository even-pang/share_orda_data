<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*" %>


<%
	Map  reqMap    = (Map)  request.getAttribute( "reqMap" );
	Map  dbMap     = (Map)  request.getAttribute( "dbMap" );
	List lstFile   = (List) request.getAttribute( "lstFile" );
	
	List lstUseYn   = (List) request.getAttribute( "lstUseYn" );
	
	ServiceUtil sUtil = new ServiceUtil();
 
    String strParam  = "keykind="  + CommonUtil.nvl( reqMap.get( "keykind" ));
    strParam += "&keyword="        + CommonUtil.nvl( reqMap.get( "keyword" ));
    strParam += "&page_now="       + CommonUtil.nvl( reqMap.get( "page_now"));	
    strParam += "&up_menu_no="     + CommonUtil.nvl( reqMap.get( "up_menu_no"));
    strParam += "&parent_menu_no=" + CommonUtil.nvl( reqMap.get( "parent_menu_no"));
	
    String strWritePage = CommonUtil.nvl(reqMap.get("writepage"));
    
    Map userMap      = 	(Map)SessionUtil.getSessionAttribute(request,"ADM");

    String strMenuNo = CommonUtil.nvl( reqMap.get( "menu_no"));
    String strIFlag  = CommonUtil.nvl( reqMap.get( "iflag"));
    
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
 
  <jsp:include page="/boffice/include/inc_top.do"  flush="false"/>

<script>

   function fSubmit()
   {
	   vObj = document.frmInput;

	   if (!checkEmpty     (vObj.brd_nm,  "게시판 명을 입력하세요")) return;
	   
	   vObj.submit();
   }
</script>

</head>

<body>

<div id="main">

    <jsp:include page="/boffice/include/inc_header.do" flush="false"/>
    
	<hr class="noscreen" />

	<!-- Columns -->
	<div id="cols" class="box">

	 	

		<!-- Content (Right Column) -->
		<section>
                <div id="section">
                    <div id="shadowDiv" class="configShadowDiv">
                        <table class="board_list_normal">
				         <form  autocomplete="off" name="frmInput" method="post"  action="/boffice/boardmgr/boardMgrWork.do">
				            <input name="iflag"       type="hidden" value="<%=CommonUtil.nvl(reqMap.get("iflag"), CommDef.ReservedWord.INSERT) %>">
				            <input name="brd_mgrno"   type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno")) %>">
				            <input name="returl"      type="hidden" value="/boffice/boardmgr/boardMgrList.do">
				            <input name="param"       type="hidden" value="<%=CommonUtil.nvl(strParam) %>">			
					        <input name="menu_no"     type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no")) %>">
				            	<colgroup>
				                	<col width="15%" />
				                    <col width="35%" />
				                    <col width="15%" />
				                    <col width="35%" />
				                </colgroup> 
				                <tr>
								    <th scope="col">게시판 명</th>
								    <td scope="col" colspan="3"><input type="text" style="width:700px" name="brd_nm" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("BRD_NM")) %>" maxlength="100"/></td>
								</tr>
				                <tr>
								    <th scope="col">사이트명</th>
								    <td scope="col" colspan="3"><input type="text" style="width:700px" name="brd_snm" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("BRD_SNM")) %>" maxlength="50"/></td>
								</tr>
		
				                <tr>
								    <th scope="col">적용사이트  </th>
								    <td scope="col" colspan="3">
								       <select name="site_gb" class="input-text">
								           <%=sUtil.getSelectBox("MENU", CommonUtil.nvl(dbMap.get("SITE_GB"))) %>
								       </select>						    
								    </td>
								</tr>
		
				                <tr>
								    <th scope="col">게시판 유형</th>
								    <td scope="col" colspan="3">
								       <select name="brd_skin_cd" class="input-text">
								           <%=sUtil.getSelectBox("SKIN", CommonUtil.nvl(dbMap.get("BRD_SKIN_CD"))) %>
								       </select>						    
								    </td>
								</tr>
			 
				                <tr>  
								    <th scope="col">사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"use_yn", CommonUtil.nvl(dbMap.get("USE_YN"), "Y")) %>
								    </td>
								</tr>
								
				                <tr>
								    <th scope="col">리스트 개수</th>
								    <td scope="col" colspan="3">
								       <input type="text"  name="list_cnt" style="width:50px;IME-MODE:disabled;" onkeypress="onlysu();"  class="input-text"  value="<%=CommonUtil.nvl(dbMap.get("LIST_CNT"), "10") %>" maxlength="3"/>
								                ※ [0]을 입력시 사용자 화면에서 페이징이 나오지 않음
								    </td>
								</tr>				
		
				                <tr>
								    <th scope="col">게시글 삭제여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox("DELGB","delflag", CommonUtil.nvl(dbMap.get("DELFLAG"), "DEL")) %></td>
								</tr>
		
		                        <tr>
								    <th scope="col">내용 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"ctnt_use_yn", CommonUtil.nvl(dbMap.get("CTNT_USE_YN"), "Y")) %></td>
								</tr>
		
		
				                <tr>
								    <th scope="col">글쓰기 권한</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox("WAUTH","write_auth_cd", CommonUtil.nvl(dbMap.get("WRITE_AUTH_CD"), "ALL")) %></td>
								</tr>
								 
				                <tr>
								    <th scope="col">첨부파일 개수</th>
								    <td scope="col" colspan="3"><input type="text"  name="attach_file_cnt" style="width:50px;IME-MODE:disabled;" onkeypress="onlysu();"  class="input-text"  value="<%=CommonUtil.nvl(dbMap.get("ATTACH_FILE_CNT"), "0") %>" maxlength="2"/></td>
								</tr>							 
								
				                <tr>
								    <th scope="col">상단공지 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"top_fix_use_yn", CommonUtil.nvl(dbMap.get("TOP_FIX_USE_YN"), "N")) %>
								    </td>
								</tr>							
								 
				                <tr>
								    <th scope="col">카테고리  사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"cate_cd_use_yn", CommonUtil.nvl(dbMap.get("CATE_CD_USE_YN"), "N")) %></td>
		                            <th scope="col">코드명</th>						    
		                            <td scope="col">
		                               <select name="cate_cd" class="input-text">
		                                   <option value="">선택하세요</option>
								           <%=sUtil.getSelectBox("*", CommonUtil.nvl(dbMap.get("CATE_CD"))) %>
								       </select> 타이틀명&nbsp;
		                                <input type="text" style="width:200px" name="cate_cd_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("CATE_CD_TITLE"),"카테고리") %>" maxlength="50"/> 
		                            </td>						    
								</tr>						 
		
				                <tr>
								    <th scope="col">통합검색 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"total_search_use_yn", CommonUtil.nvl(dbMap.get("TOTAL_SEARCH_USE_YN"), "N")) %>
								    </td>
								</tr>						 
		 
				                <tr>
								    <th scope="col">정렬필드 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"ord_use_yn", CommonUtil.nvl(dbMap.get("ORD_USE_YN"), "N")) %>
								    </td>
								</tr>								 
		 						 
				                <tr>
								    <th scope="col">작성자 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"writer_use_yn", CommonUtil.nvl(dbMap.get("WRITER_USE_YN"), "N")) %>
								    </td>
								</tr>						 
		
				                <tr>
								    <th scope="col">연락처 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"phone_use_yn", CommonUtil.nvl(dbMap.get("PHONE_USE_YN"), "N")) %>
								    </td>
								</tr>						 
		
				                <tr>
								    <th scope="col">이메일  사용여부</th>
								    <td scope="col" colspan="3">
								        <%=sUtil.getRadioBox(lstUseYn,"email_use_yn", CommonUtil.nvl(dbMap.get("EMAIL_USE_YN"), "N")) %>
								        <br/>※ 이메일  수신여부 : <%=sUtil.getRadioBox(lstUseYn,"email_recv_use_yn", CommonUtil.nvl(dbMap.get("EMAIL_RECV_USE_YN"), "N")) %>
								    </td>
								</tr>
									 					 
				                <tr>
								    <th scope="col">게시기간   사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"term_use_yn", CommonUtil.nvl(dbMap.get("TERM_USE_YN"), "N")) %></td>
		                            <th scope="col">게시기간 타이틀</th>						    
		                            <td scope="col">
		                                <input type="text" style="width:100px" name="term_sdt_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("TERM_SDT_TITLE"),"시작일") %>" maxlength="50"/> ~
		                                <input type="text" style="width:100px" name="term_edt_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("TERM_EDT_TITLE"),"종료일") %>" maxlength="50"/>
		                            </td>						    
								</tr>						 
								
														 
				                <tr>
								    <th scope="col">날짜   사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"date_use_yn", CommonUtil.nvl(dbMap.get("DATE_USE_YN"), "N")) %></td>
		                            <th scope="col">날짜   타이틀</th>						    
		                            <td scope="col"><input type="text" style="width:200px" name="date_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("DATE_TITLE"),"날짜") %>" maxlength="50"/></td>
								</tr>							
		
				                <tr>
								    <th scope="col">팝업사이즈 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"popsize_use_yn", CommonUtil.nvl(dbMap.get("POPSIZE_USE_YN"), "N")) %>
								    </td>
								</tr>	
		
				                <tr>
								    <th scope="col">URL 사용여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"url_use_yn", CommonUtil.nvl(dbMap.get("URL_USE_YN"), "N")) %>
								          <br/>※목록화면에서바로 이동하기 : <%=sUtil.getRadioBox(lstUseYn,"direct_url_use_yn", CommonUtil.nvl(dbMap.get("DIRECT_URL_USE_YN"), "N")) %>						    
								    </td>
								</tr>	
		 
		
				                <tr>
								    <th scope="col">답글쓰기  여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"reply_use_yn", CommonUtil.nvl(dbMap.get("REPLY_USE_YN"), "N")) %>
								    </td>
								</tr>
															 
				                <tr>
								    <th scope="col">등록일 별도입력</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"regdt_use_yn", CommonUtil.nvl(dbMap.get("REGDT_USE_YN"), "N")) %>
								    </td>
								</tr>
															 
				                <tr>
								    <th scope="col">비밀글 사용 여부</th>
								    <td scope="col" colspan="3"><%=sUtil.getRadioBox(lstUseYn,"secret_use_yn", CommonUtil.nvl(dbMap.get("SECRET_USE_YN"), "N")) %>
								    </td>
								</tr>							 
		
				                <tr>
								    <th scope="col">추가항목1   사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"etc_field1_use_yn", CommonUtil.nvl(dbMap.get("ETC_FIELD1_USE_YN"), "N")) %></td>
		                            <th scope="col">추가항목1   타이틀</th>						    
		                            <td scope="col"><input type="text" style="width:200px" name="etc_field1_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD1_TITLE")) %>" maxlength="50"/></td>
								</tr>						
		
				                <tr>
								    <th scope="col">추가항목2   사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"etc_field2_use_yn", CommonUtil.nvl(dbMap.get("ETC_FIELD2_USE_YN"), "N")) %></td>
		                            <th scope="col">추가항목2   타이틀</th>						    
		                            <td scope="col"><input type="text" style="width:200px" name="etc_field2_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD2_TITLE")) %>" maxlength="50"/></td>
								</tr>						
		
				                <tr>
								    <th scope="col">추가항목3   사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"etc_field3_use_yn", CommonUtil.nvl(dbMap.get("ETC_FIELD3_USE_YN"), "N")) %></td>
		                            <th scope="col">추가항목3   타이틀</th>						    
		                            <td scope="col"><input type="text" style="width:200px" name="etc_field3_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD3_TITLE")) %>" maxlength="50"/></td>
								</tr>						
											
		
				                <tr>
								    <th scope="col">추가항목4   사용여부</th>
								    <td scope="col"><%=sUtil.getRadioBox(lstUseYn,"etc_field4_use_yn", CommonUtil.nvl(dbMap.get("ETC_FIELD4_USE_YN"), "N")) %></td>
		                            <th scope="col">추가항목4   타이틀</th>						    
		                            <td scope="col"><input type="text" style="width:200px" name="etc_field4_title" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("ETC_FIELD4_TITLE")) %>" maxlength="50"/></td>
								</tr>										
															 
				                <tr>
								    <th scope="col">썸네일가로사이즈</th>
								    <td scope="col" colspan="3"><input type="text"  style="width:50px;IME-MODE:disabled;" onkeypress="onlysu();"  name="thm_width_size" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("THM_WIDTH_SIZE")) %>" maxlength="10"/></td>
								</tr>
													 
				                <tr>
								    <th scope="col">썸네일세로사이즈</th>
								    <td scope="col" colspan="3"><input type="text" style="width:50px;IME-MODE:disabled;" onkeypress="onlysu();"  name="thm_height_size" class="input-text" value="<%=CommonUtil.nvl(dbMap.get("THM_HEIGHT_SIZE")) %>" maxlength="10"/></td>
								</tr>						 
		 	 
		                <tr>
						    <th scope="col">내용</th>
						    <td scope="col" colspan="3">
		                    <textarea name="brd_desc" cols="70" rows="7" class="input-text"><%=CommonUtil.nvl(dbMap.get("BRD_DESC")) %></textarea>  	
		                    </td>
						</tr> 						 
								 
					   </form>
					   <tr>
                         <td colspan="4" class="buttonTd" style="text-align: center">
                             <button onclick="fSubmit();">완료</button>
                             <button onclick="location.href='/boffice/boardmgr/boardMgrList.do?<%=strParam%>'">목록</button>
                         </td>                                
                     </tr>
					</table>
		           <%--  <div class="btn_right">
		                <a href="javascript:fSubmit()"><img src="/boffice/image/design/btn_modok.gif" alt="완료" /></a>
		                 <a href="/boffice/boardmgr/boardMgrList.do?<%=strParam%>"><img src="/boffice/image/design/btn_list.gif" alt="목록" /></a>
		            </div> --%>
	            </div>
            </div>
        </section><!-- /content -->

	</div> <!-- /cols -->

	<!-- Footer -->
       <!--<jsp:include page="/boffice/include/inc_footer.do" flush="false"/>-->
	<!-- /footer -->

</div> <!-- /main -->

</body>
</html>