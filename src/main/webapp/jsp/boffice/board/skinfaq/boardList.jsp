<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*" %>


<%
  	Map  reqMap    = (Map)  request.getAttribute( "reqMap" );
  	List lstRs     = (List) request.getAttribute( "list" );
  	Map  brdMgrMap = (Map)  request.getAttribute( "brdMgrMap" );
  	List  cateList = (List) request.getAttribute( "cateList" );  	
  	
  	String strCateTitle  = CommonUtil.nvl(brdMgrMap.get("CATE_CD_TITLE"));
  	String strCateUseYn  = CommonUtil.nvl(brdMgrMap.get("CATE_CD_USE_YN"));  	
  	
  	int  nRowCount = CommonUtil.getNullInt( (String )request.getAttribute( "count" ), 0);

 	String strLinkPage   = "/boffice/boardList.do"; // request.getRequestURL().toString(); // 현재 페이지
  	
  	String strParam      = CommonUtil.getRequestQueryString( request );
  	String strKeykind    = CommonUtil.nvl( request.getAttribute( "keykind" ) , "");
  	int    nPageNow      = CommonUtil.getNullInt(reqMap.get( "page_now" ), 1 ) ;
  	int    nPerPage      = CommonUtil.getNullInt(reqMap.get( "page_row" ), CommDef.PAGE_ROWCOUNT ) ;
   	
  	int nCols = 5;
  	
  	nCols += ("Y".equals(strCateUseYn)) ? 1 : 0;
  	
  	
%>

<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="/boffice/include/inc_top.do"  flush="false"/>



<script>
 
   function fWrite()
   {
	  vObj = document.frmWrite;   
	
	  vObj.brd_no.value = '';
	  vObj.action = "/boffice/boardInput.do";
	  
	  vObj.submit();
   }   
   function fModify(strMgrno)
   {
	  vObj = document.frmWrite;   
	
	  vObj.brd_no.value = strMgrno; 

	  vObj.action = "/boffice/boardInput.do";
	  
	  vObj.submit();
   }
   
   function fView(strMgrno)
   {
	  vObj = document.frmWrite;   
	
	  vObj.brd_no.value = strMgrno; 

	  vObj.action = "/boffice/boardView.do";
	  
	  vObj.submit();
   }   
   
 
   
</script>


</head>

<body>

<div id="main">

	<jsp:include page="/boffice/include/inc_header.do" flush="false"/>

	<hr class="noscreen" />

	<!-- Columns -->
	<div id="wrapping" class="boardDiv">

	    	         
         
	<!-- Content (Right Column) -->
		<div id="content" class="box">

			<h1 ><div id="top_nav_menu_nm">&nbsp;</div></h1>

			<!-- Headings -->
			<h2 ><div id="sub_nav_menu_nm">&nbsp;</div></h2>
			
			<div class="listTblWrap" style="width: 100%; height: 100%; overflow: auto; white-space: nowrap; ">
				<!-- Table (TABLE) -->
				<table class="board_list_normal">
	                 <form  autocomplete="off" name="frmWrite"      method="post" action="/boffice/boardInput.do">
	                     <input name="keykind"     type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keykind")) %>"/>
	                     <input name="keyword"     type="hidden" value="<%=CommonUtil.nvl(reqMap.get("keyword")) %>"/>
	                     <input name="page_now"    type="hidden" value="<%=CommonUtil.nvl(reqMap.get("page_now")) %>"/>
	                     <input name="returl"      type="hidden" value="<%=strLinkPage%>"/>
	                     <input name="brd_mgrno"   type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno")) %>"/>
	                     <input name="brd_no"      type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no")) %>"/>
	                     <input name="menu_no"     type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no")) %>"/>
	                 </form>    			
				
	            	<colgroup>
	                   <col width="5%" />
	                 <% if ("Y".equals(strCateUseYn)) { %>  
	                   <col width="12%" />     
	                 <% } %>                     
	                   <col width="10%" />
	                   <col width="10%" />
	                   <col width="*%" />                       	
	                   <col width="9%" />
	                   <col width="10%" />
	                   <col width="10%" />
	                </colgroup>    	
	            	<thead>
					<tr>
	                  	<th scope="col">번호</th>
	                 <% if ("Y".equals(strCateUseYn)) { %>  	
	                    <th scope="col"><%=strCateTitle %></th>
	                 <% } %>                    	
	                    <th scope="col">이름</th>
	                    <th scope="col">아이디</th>
	                    <th scope="col">제목</th>
	                    <th scope="col">사용여부</th>
	                    <th scope="col">등록일</th>                      
	                    <th scope="col">조회건수</th>                      				    
					</tr>
	                </thead>
	                <tbody>
	 
	<% 
	    if(lstRs != null && lstRs.size() > 0){
	    	
	       int iSeqNo = nRowCount - ( nPageNow - 1 ) * nPerPage;
	       for( int iLoop = 0; iLoop < lstRs.size(); iLoop++ ) {
	            Map rsMap = ( Map ) lstRs.get( iLoop );
	    
	%>     
	                            <tr <%= ((iLoop % 2) == 1) ? "class='bg'" : "" %>>
	                            	<td ><%=iSeqNo%></td>
	                            <% if ("Y".equals(strCateUseYn)) { %>  	
	                                <td ><%=CommonUtil.nvl(rsMap.get("CATE_NM")) %></td>
	                            <% } %>                                  	
	                                <td ><%=CommonUtil.nvl(rsMap.get("USER_ID")) %></td>
	                                <td ><%=CommonUtil.nvl(rsMap.get("USER_NM")) %></td>
	                                <td class="textLeft"><%=CommonUtil.getReplyImg(CommonUtil.nvl(rsMap.get("DEPTH"))) %><a href="javascript:fView('<%=CommonUtil.nvl(rsMap.get("BRD_NO"))%>')"><%=CommonUtil.nvl(rsMap.get("TTL")) %></a></td>
	                                <td ><%=CommonUtil.nvl(rsMap.get("USE_YN")) %></td>
	                                <td ><%=CommonUtil.getDateFormat(rsMap.get("REG_DT")) %></td>                                
	                                <td ><%=CommonUtil.nvl(rsMap.get("VIEW_CNT")) %></td>                                
	                            </tr>
	<%       iSeqNo--;
	       }
	    } else {
	%>              
	 
	
	          <tr>
	            <td align="center" colspan="<%=nCols%>"><%=CommDef.Message.NO_DATA %></td>
	          </tr>       
	<%  } %> 
	
					 
	                </tbody>
				</table>
            </div>
           
            <div class="btn_right">
               <a class="btn3" href="javascript:fWrite()">글쓰기</a>
               
            </div>
             <div id="listPageDiv" style="margin-bottom: 100px;">
               <ul id="listPage"><%=CommonUtil.getAdmPageNavi( strLinkPage, nRowCount ,nPageNow, strParam, CommDef.PAGE_PER_BLOCK, nPerPage ) %></ul>
             </div>
            

			<form  autocomplete="off" action="<%=strLinkPage%>" method="post" id="search">
                     <input name="returl"      type="hidden" value="<%=strLinkPage%>">
                     <input name="brd_mgrno"   type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_mgrno")) %>">
                     <input name="brd_no"      type="hidden" value="<%=CommonUtil.nvl(reqMap.get("brd_no")) %>">				
				     <input name="menu_no"     type="hidden" value="<%=CommonUtil.nvl(reqMap.get("menu_no")) %>">
					<fieldset>
						<legend>Search</legend>
						<div class="search_box" style="width:600px">
						<% if ("Y".equals(strCateUseYn)) { %>  
						<%=strCateTitle %>
                       	    <select name="brd_cate_cd" class="input_txt">
						         <option value="" >전체</option>
						
							<% 
							    if(cateList != null && cateList.size() > 0){
							    	
							       for( int iLoop = 0; iLoop < cateList.size(); iLoop++ ) {
							            Map rsMap = ( Map ) cateList.get( iLoop );
							            
							            String strCode = CommonUtil.nvl(rsMap.get("COMM_CD"));
							    
							%> 						         
										         <option value="<%=strCode%>"    <%=(strCode.equals(CommonUtil.nvl(reqMap.get("brd_cate_cd"))))   ? " selected " : "" %>><%=CommonUtil.nvl(rsMap.get("CD_NM")) %></option>
							<%     }
							    }
							%>			         
						         
						    </select>&nbsp;&nbsp;&nbsp;
						 <% } %>
						
                       	    <select name="keykind" class="input_txt">
						         <option value="ttl"     <%=("ttl".equals(CommonUtil.nvl(reqMap.get("keykind"))))    ? " selected " : "" %>>제목</option>
						         <option value="ctnt"    <%=("ctnt".equals(CommonUtil.nvl(reqMap.get("keykind"))))   ? " selected " : "" %>>내용</option>
						         <option value="reg_nm"  <%=("reg_nm".equals(CommonUtil.nvl(reqMap.get("keykind")))) ? " selected " : "" %>>작성자</option>
						    </select>
                            &nbsp;&nbsp;&nbsp;
							<!-- p class="search_input"  -->
							   <input type="text" size="14" name="keyword" class="input-text" value="<%=CommonUtil.nvl(reqMap.get("keyword")) %>" />&nbsp;
							   <input type="submit" value="검색" class="input-submit-02" />
							 <!-- /p -->
						</div>  
					</fieldset>
				 </form>                        
            
            <!-- Search -->
<!--             
				<form  autocomplete="off" action="#" method="get" id="search">
					<fieldset>
						<legend>Search</legend>
                    
                        
						<div class="search_box">
                        	<a href="javascript:toggle('search-options');" class="ico-drop">검색옵션선택</a>
                            
						<p class="search_input"><input type="text" size="17" name="" class="input-text" />&nbsp;<input type="submit" value="OK" class="input-submit-02" /><br />
							<p  id="search-options" style="display:none;">
								<label><input type="checkbox" name="" checked="checked" /> 제목</label><br />
								<label><input type="checkbox" name="" /> 작성자</label><br />
								<label><input type="checkbox" name="" /> 내용</label>
							</p>
						</p>
						</div>  
						

					</fieldset>
				</form>
-->				
		</div> <!-- /content -->
		

	</div> <!-- /cols -->

	<!-- Footer -->
       <!--<jsp:include page="/boffice/include/inc_footer.do" flush="false"/>-->
	<!-- /footer -->

</div> <!-- /main -->

</body>
</html>		