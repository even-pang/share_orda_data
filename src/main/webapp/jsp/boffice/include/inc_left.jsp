<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*"%>
<%@ page import="java.util.*"%>
<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   
   String strUri = request.getRequestURI();
   
   Map reqMap     = (Map)request.getAttribute( "reqMap" );
   Map menuNavMap = (Map)request.getAttribute( "menuNavInfo" );
   
   List lstRs = (List)request.getAttribute( "leftMenulist" );
   Map sesMap     = (Map)SessionUtil.getSessionAttribute(request,"ADM");
   
   if ( menuNavMap == null )
	   menuNavMap = new HashMap();
   if ( sesMap == null )
	   sesMap = new HashMap();
   
%>
	<!-- Aside (Left Column) -->
		<div id="subMenu" class="box">

			<ul><!--  class="box"> -->      
          
<script>
    function fGoMenu(vCurMenuNo, vMenuNo, vUrl)
    {
    	if (vUrl == "#this")
    		return;
    	
    	setCookie( "<%=CommDef.COOKIE_ADMIN_CUR_MENU_NO%>", vCurMenuNo, 2 );
    	setCookie( "<%=CommDef.COOKIE_ADMIN_SUB_MENU_NO%>", vMenuNo, 2 );
    	setCookie( "<%=CommDef.COOKIE_ADMIN_TOP_MENU_NO%>", <%=CommonUtil.nvl(reqMap.get("top_menu_no"), "22")%>, 2 );
    	
    	window.location.href=vUrl;
    	
    }
</script>     
   
<% 

    int  nChooseTopMenuNo = CommonUtil.getNullInt(CommonUtil.getCookieObject(request, CommDef.COOKIE_ADMIN_TOP_MENU_NO), -1);
    int  nChooseSubMenuNo = CommonUtil.getNullInt(CommonUtil.getCookieObject(request, CommDef.COOKIE_ADMIN_SUB_MENU_NO), -1);
    int  nChooseCurMenuNo = CommonUtil.getNullInt(CommonUtil.getCookieObject(request, CommDef.COOKIE_ADMIN_CUR_MENU_NO), -1);
    
    String strTopMenuNm   = "";    
    String strCurMenuNm   = "";
    int    nChooseUpMenuNo = CommonUtil.getNullInt(menuNavMap.get("UP_MENU_NO"), -1);
    int    nChooseBlockNo  = 1;
    
    if(lstRs != null && lstRs.size() > 0){
       int nBlockNo   = 0;	 
       int nSubCnt    = 0;
       int nSubMenuNo = 0;
       
       for( int iLoop = 0; iLoop < lstRs.size(); iLoop++ ) {
            Map rsMap = ( Map ) lstRs.get( iLoop );    
            
            int  nMenuLvl   = CommonUtil.getNullInt(rsMap.get("CONN_LVL"), 2 );
            int  nCurMenuNo = CommonUtil.getNullInt(rsMap.get("MENU_NO"), 0 );
            int  nChildCnt  = CommonUtil.getNullInt(rsMap.get("CHILD_CNT"), 0 );
            
            strTopMenuNm = CommonUtil.nvl(rsMap.get("MENU_NM"));
            
            if ( nMenuLvl <= 1)  // 1 Level은 처리하지 않음
            	continue;
            
            if ( nMenuLvl <= 2) {
            	nBlockNo++;
            	nSubCnt = 0;
            	nSubMenuNo   = CommonUtil.getNullInt(rsMap.get("MENU_NO"), 0 );
            		
            	if ( nChooseSubMenuNo <= 0 )
            		nChooseSubMenuNo = nSubMenuNo;             	
            } else {
                if ( "".equals(strCurMenuNm) && nChooseCurMenuNo <= 0 ) {  // 최초 메뉴가 없는 경우
                	strCurMenuNm = CommonUtil.nvl(rsMap.get("MENU_NM"));
                }            	
            }
            
            if( nChooseTopMenuNo == nChooseSubMenuNo ) // 선택된 메뉴가 없는 경우 최상위로 선택
            	nChooseSubMenuNo = nSubMenuNo;
            
            if (nChooseCurMenuNo == nCurMenuNo) { // 현재 메뉴명
            	strCurMenuNm = CommonUtil.nvl(rsMap.get("MENU_NM"));
            }
            
            if ( nMenuLvl <= 3 ) {
            	nSubCnt ++;
            }
            
            if ( nChooseUpMenuNo == nCurMenuNo )
            	nChooseBlockNo = nBlockNo;
            
            
            switch (nMenuLvl)
            {
               case 2 :
                         if ( nChildCnt == 0 ) {%>  	
                             <li onClick="clickblock(<%=nBlockNo%>)" <%= (nSubMenuNo == 702) ? "  id='withdrawal_propose' " : ""%> <%= (nChooseSubMenuNo == nSubMenuNo) ? "  class='subMenu subOn' " : ""%>><a href="javascript:fGoMenu('<%=nCurMenuNo %>','<%=nSubMenuNo%>','<%=CommonUtil.getMenuUrl(rsMap) %>')"><%=CommonUtil.nvl(rsMap.get("MENU_NM"), "-" ) %><span class="slidePM">─</span></a></li>
<%                       } else { %>
	                         <li onClick="clickblock(<%=nBlockNo%>);$(this).toggleClass('slideOn');" <%= (nChooseUpMenuNo == nSubMenuNo) ? "  class='subMenu subOn slideOn' " : ""%>><a href="#this"><%=CommonUtil.nvl(rsMap.get("MENU_NM"), "-" ) %></a><span class="slidePM">+</span></li> 
<%						 } break;
               case 3 :
               %>               
               
               <li class="slideOn" id="block<%=nBlockNo%>" <%= (nChooseSubMenuNo == nSubMenuNo) ? "class='subsubOn'" : " style='display:none' "%>> <a href="javascript:fGoMenu('<%=nCurMenuNo %>','<%=nSubMenuNo%>','<%=CommonUtil.getMenuUrl(rsMap)%>')" style='font-size:12px;'> - <%=CommonUtil.nvl(rsMap.get("MENU_NM"), "-" ) %></a>
<%                      break;
               default :         %>               
                	<div class="fsub_<%=nSubCnt -1%>" <%= (nChooseSubMenuNo == nSubMenuNo) ? "" : " style='display:none' "%> ><a href="javascript:fGoMenu('<%=nCurMenuNo %>','<%=nSubMenuNo%>','<%=CommonUtil.getMenuUrl(rsMap)%>')"><%=CommonUtil.nvl(rsMap.get("MENU_NM"), "-" ) %></a></div>
<%                    break; 
           }
        }
     }
%>          
  
  
			</ul>


		</div> <!-- /aside -->