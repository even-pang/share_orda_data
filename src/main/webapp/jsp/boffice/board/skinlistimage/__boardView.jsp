<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*" %>


<%
	Map  reqMap    = (Map)  request.getAttribute( "reqMap" );
	Map  dbMap     = (Map)  request.getAttribute( "dbMap" );
	Map  brdMgrMap = (Map)  request.getAttribute( "brdMgrMap" );
	List lstFile   = (List) request.getAttribute( "lstFile" );
	
	ServiceUtil sUtil = new ServiceUtil();
	UploadUtil  upUtil = new UploadUtil(request);
	
    String strParam  = "keykind="  + CommonUtil.nvl( reqMap.get( "keykind" ));
    strParam += "&keyword="        + CommonUtil.nvl( reqMap.get( "keyword" ));
    strParam += "&page_now="       + CommonUtil.nvl( reqMap.get( "page_now"));	
    strParam += "&brd_mgrno="      + CommonUtil.nvl( reqMap.get( "brd_mgrno"));
	
    String strWritePage = CommonUtil.nvl(reqMap.get("writepage"));
    
    Map userMap      = 	(Map)SessionUtil.getSessionAttribute(request,"ADM");

    String strMenuNo = CommonUtil.nvl( reqMap.get( "menu_no"));
    String strIFlag  = CommonUtil.nvl( reqMap.get( "iflag"));
    
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
 
  <jsp:include page="/boffice/include/inc_top.do"  flush="false"/>

 <!-- ------------------ HTML Editor Control Start ----------------------- -->

<script type="text/javascript" src="/fckeditor/fckeditor.js"></script>
<!-- script type="text/javascript" src="/fckeditor/fckconfig.js"></script>  -->

<script type="text/javascript">
	  
	function FCKeditor_OnComplete(editorInstance) {
	  // window.status = editorInstance.Description;
	} 
		
   function GetInnerHTML(vName)
   {
    // Get the editor instance that we want to interact with.
     var oEditor = FCKeditorAPI.GetInstance(vName) ;
     return oEditor.EditorDocument.body.innerHTML ;
   }
</script>
<!-- ------------------ HTML Editor Control End ----------------------- --> 


<script>	
	function fDelete()
	{
	   vObj = document.frmDelete;
	   
	   if ( !confirm("자료를 삭제하시겠습니까?"))
	      return;
	  
	   vObj.submit();	
	}	 
	 
	function fReply()
	{
	   vObj = document.frmWrite;
	   vObj.iflag.value = "<%=CommDef.ReservedWord.REPLY%>";
	   vObj.submit();
	} 
	
   function fModify()
   {
	  vObj = document.frmWrite;   
	  vObj.action = "/boffice/boardInput.do";
	  
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
		<div id="content" class="box">
			<h1 ><div id="top_nav_menu_nm">시스템 관리</div></h1>

			<!-- Headings -->
			<h2 ><div id="sub_nav_menu_nm">게시판관리</div></h2>

			
			<!-- Table (TABLE) -->
			<table class="tb01 view">
				           
			               <form  autocomplete="off" name="frmWrite"      method="post" action="/boffice/boardInput.do">
			                   <input name="iflag"          type="hidden" value="<%=CommDef.ReservedWord.INSERT%>">
			                   <input name="keykind"        type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keykind")) %>">
			                   <input name="keyword"        type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keyword")) %>">
			                   <input name="page_now"       type="hidden" value="<%=CommonUtil.nvl(reqMap.get("page_now")) %>">
			                   <input name="returl"         type="hidden" value="/boffice/boardList.do">
			                   <input name="brd_mgrno"      type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno")) %>">
			                   <input name="brd_no"         type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no")) %>">
			               </form> 
                 				           
				        
				           <form  autocomplete="off" name="frmDelete"      method="post" action="/boffice/boardDelete.do">
				               <input name="brd_no"    type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no")) %>">
				               <input name="brd_mgrno" type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno")) %>">
				               <input name="param"     type="hidden" value="<%=strParam%>">
				               <input name="returl"    type="hidden" value="/boffice/boardList.do">
			                   <input name="keykind"   type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keykind")) %>">
			                   <input name="keyword"   type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keyword")) %>">
				           </form>  			
	 	
			
		            	<colgroup>
		                	<col width="15%" />
		                    <col width="*%" />
		                </colgroup> 
		                <tr>
						    <th scope="col">제목</th>
						    <td scope="col"><%=CommonUtil.nvl(dbMap.get("TTL")) %></td>
						</tr>
						
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("WRITER_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">작성자 </th>
						     <td scope="col"><%=CommonUtil.nvl(dbMap.get("REG_NM")) %></td>
						    </td>
						</tr>	 
                  <% }%>
                  
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("PHONE_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">연락처 </th>
						     <td scope="col"><%=CommonUtil.nvl(dbMap.get("PHONE_NO")) %></td>
						    </td>
						</tr>	 
                  <% }%>                  
                   				 
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("EMAIL_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">이메일 </th>
						     <td scope="col"><%=CommonUtil.nvl(dbMap.get("EMAIL")) %></td>
						    </td>
						</tr>	 
                  <% }%>  

	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("TERM_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">기간 </th>
						     <td scope="col"><%=CommonUtil.getDateFormat(dbMap.get("SDT")) %> ~ <%=CommonUtil.getDateFormat(dbMap.get("EDT")) %></td>
						    </td>
						</tr>	 
                  <% }%> 

	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ADDR_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">주소 </th>
						     <td scope="col">(<%=CommonUtil.nvl(dbMap.get("ZIP_CD")) %>)&nbsp;<%=CommonUtil.getDateFormat(dbMap.get("ADDR1")) %>&nbsp;<%=CommonUtil.getDateFormat(dbMap.get("ADDR2")) %></td>
						</tr>	 
                  <% }%> 
												 
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("EMAIL_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">등록일 </th>
						     <td scope="col"><%=CommonUtil.getDateFormat(dbMap.get("REG_DT")) %></td>
						    </td>
						</tr>	 
                  <% }%>   						 


						
                  <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD1_USE_YN")))) {%> 
		                <tr>
						    <th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD1_TITLE"), "추가항목1") %> </th>
						    <td scope="col"><%=CommonUtil.nvl(dbMap.get("ETC_FIELD1")) %></td>
						</tr>	 
                  <% }%>   						 
						 
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD2_USE_YN")))) {%> 
		                <tr>
						    <th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD2_TITLE"), "추가항목2") %> </th>						     
						    <td scope="col"><%=CommonUtil.nvl(dbMap.get("ETC_FIELD2")) %></td>
						</tr>	 
                  <% }%> 						 
						
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("ETC_FIELD3_USE_YN")))) {%> 
		                <tr>
						    <th scope="col"><%=CommonUtil.nvl(brdMgrMap.get("ETC_FIELD3_TITLE"), "추가항목3") %> </th>						     
						    <td scope="col"><%=CommonUtil.nvl(dbMap.get("ETC_FIELD3")) %></td>
						</tr>	 
                  <% }%>							
						 
		                <tr>
						    <th scope="col">내용</th>
						    <td scope="col">
                            	<%=CommonUtil.getReplaceToHtml(dbMap.get("CTNT"))%>
                            	
<%                  String strImgFile = ""; 
                    if ( lstFile != null && !lstFile.isEmpty()) {      
	                   for (int nLoop =0; nLoop < lstFile.size(); nLoop++)
	                   {
	                	   Map dbFile = (Map)lstFile.get(nLoop);
	                	   String strFileName = CommonUtil.nvl(dbFile.get("FILE_REALNM"));
	                	   
	                	   if ( CommonUtil.isImageFile(strFileName) ) {
%>	                		   
	                		   <br/>
	                		   <img src="<%=strFileName%>">
<% 	                		   
	                	   }	                	      
                       }
	                }%>	&nbsp; 

                          </td>
						</tr> 						 
						 
	              <% if ( "Y".equals(CommonUtil.nvl(brdMgrMap.get("URL_USE_YN")))) {%> 
		                <tr>
						    <th scope="col">URL </th>
						    <td scope="col"><a href="<%=CommonUtil.nvl(dbMap.get("URL")) %>" target="<%=CommonUtil.nvl(dbMap.get("URL_TARGET"))%>" ><%=CommonUtil.nvl(dbMap.get("URL")) %></a>
						</tr>	 
                  <% }%>  						 
						 
		                <tr>
						    <th scope="col">사용여부</th>
						    <td scope="col"><%=CommonUtil.nvl(dbMap.get("USE_YN")) %></td>
						</tr>						 
						 
<%                 if ( lstFile != null && !lstFile.isEmpty()) {   %>						 
		                <tr>
						    <th scope="col">첨부파일</th>
						    <td scope="col">
<%   
	                   for (int nLoop =0; nLoop < lstFile.size(); nLoop++)
	                   {
	                	   Map dbFile = (Map)lstFile.get(nLoop);
	%>							
							<%=CommonUtil.getIconFileExt(dbFile.get("FILE_NM"))%>&nbsp;<a href="javascript:download(<%=CommonUtil.getNullTrans(dbFile.get("FILE_NO"))%>)"><%=CommonUtil.getFileName(CommonUtil.getNullTrans(dbFile.get("FILE_NM")))%></a>
<%                     }%>
		                    </td>
						</tr>						 
				  <% } %>		 
 
			</table>
            <div class="btn_right">
              <% if ("Y".equals(CommonUtil.nvl(brdMgrMap.get("REPLY_USE_YN")))) { %>  
                 <a href="javascript:fReply()"><img src="/boffice/image/design/btn_answer.gif" alt="답변" /></a>
              <% } %>   
              <a href="javascript:fModify()"><img src="/boffice/image/design/btn_modify.gif" alt="수정" /></a>
              <a href="javascript:fDelete()">삭제</a>

              <a href="/boffice/boardList.do?<%=strParam%>"><img src="/boffice/image/design/btn_list.gif" alt="목록" /></a>
            </div>
		</div> <!-- /content -->

	</div> <!-- /cols -->

	<!-- Footer -->
       <!--<jsp:include page="/boffice/include/inc_footer.do" flush="false"/>-->
	<!-- /footer -->

</div> <!-- /main -->

<jsp:include page="/Common/inc_calendar.jsp" flush="false" />

</body>
</html>