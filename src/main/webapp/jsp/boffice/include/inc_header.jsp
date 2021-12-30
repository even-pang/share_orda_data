<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ordadata.pack.charm.util.*, java.util.*"%>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">
<%
List lstRs = (List) request.getAttribute("list");
Map sesMap = (Map) SessionUtil.getSessionAttribute(request, "ADM");
Map reqMap = (Map) request.getAttribute("reqMap");
int nTopMenuNo = CommonUtil.getNullInt(CommonUtil.getCookieObject(request, CommDef.COOKIE_ADMIN_TOP_MENU_NO), -1);
%>
<script>
    function fGoTopMenu(vMenuNo, vUrl)
    {
    	if (vUrl == "#this")
    		return;
    	
    	setCookie( "<%=CommDef.COOKIE_ADMIN_TOP_MENU_NO%>", vMenuNo, 2 );
    	setCookie( "<%=CommDef.COOKIE_ADMIN_SUB_MENU_NO%>", vMenuNo, 2 );


		window.location.href = vUrl;

	}

	$(document).ready(function() {
		if ($("#sch_gubun").val() == "user")
			$(".txtSchField").show();
		if ($("#sch_gubun").val() == "posttitle")
			$(".txtSchField").show();
		else if ($("#sch_gubun").val() == "rgDate")
			$(".dateSchField").show();
		$("#sch_gubun").change(function() {
			if ($(this).val() == "user")
				$(".txtSchField").show();
			if ($(this).val() == "posttitle")
				$(".txtSchField").show();
			else if ($(this).val() == "rgDate")
				$(".dateSchField").show();
		})
	});
</script>
<div class="logoDiv w_show">
	<img src="/images/logo.png">
</div>
<nav id="sidebar" class="w_show">
	<div class="menu on" data-level="1">
		<h2>ALL CATEGORIES</h2>
		<ul>
			<%
			if (lstRs != null) {
				String menuLvl = "";
				String preMenuLvl = "";
				int limitCnt = 9; //1Depth 나오는 갯수
				int count = 0;

				for (int i = 0; i < lstRs.size(); i++) {
					Map menuMap = (Map) lstRs.get(i);
					menuLvl = CommonUtil.nvl(menuMap.get("MENU_LVL"));

					String strUrl = CommonUtil.getMenuUrl(menuMap);
					String strUrlTarget = CommonUtil.nvl(menuMap.get("URL_TARGET"), "_self");
					if ("1".equals(menuLvl)) {
				if (count >= limitCnt)
					break;
				if ("1".equals(preMenuLvl)) {
			%>
			</ul>
		</li>
		<%
		} else if ("2".equals(preMenuLvl)) {
		%>
				</li>
			</ul>
		</li>
		<%
		} else if ("3".equals(preMenuLvl)) {
		%>
				</li>
			</ul>
		</li>
		<%
		}
		%>
		<li class="ico_leftArrow mainMenu"><a href="<%=CommonUtil.getNullInt(menuMap.get("child_cnt"), 0) > 0 ? "#" : strUrl%>"><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></a>
			<div class="menu">
			
				<h2><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></h2>
				<a class="back" href="javascript:void(0);">뒤로가기</a>
				<ul>
					<%
					count++;
					} else if ("2".equals(menuLvl)) {
					if ("2".equals(preMenuLvl)) {
					%>
				</li>
		<%
		} else if ("3".equals(preMenuLvl)) {
		%>
		</li>
		<%
		}
		%>
		<li ><a href="<%=strUrl%>" target="<%=strUrlTarget%>"><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></a> <%
 } else if ("3".equals(menuLvl)) {
 %>
			<ul>
				<li><a href="<%=strUrl%>" target="<%=strUrlTarget%>"><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></a></li>
			</ul> <%
 }
 preMenuLvl = menuLvl;
 }
 if ("3".equals(preMenuLvl)) {
 %></li>
		<%
		}
		%>
			</ul>
		</li>
		<%
		}
		%>
		</ul>
	</div>
</nav>
<div id="m_header" class="m_show">
	<img src="/images/logo.png" style="cursor: pointer; transition: all .3s;" class="m_main_header_logo"> <img src="/images/x_icon.png" class="m_menu_closeBtn">
	<div class="m_show m_menu_icon">
		<a href="#this" class="gnb__item gnbToggle txtHidden" aria-label="전체메뉴보기">
			전체메뉴보기 <span class="gnbToggle__item"></span> <span class="gnbToggle__item"></span> <span class="gnbToggle__item"></span>
		</a>
	</div>
</div>
<div class="m_menu m_show">
	<ul id="m_menu_ul">
		<%
		if (lstRs != null) {
			String menuLvl = "";
			String preMenuLvl = "";
			int limitCnt = 9; //1Depth 나오는 갯수
			int count = 0;

			for (int i = 0; i < lstRs.size(); i++) {
				Map menuMap = (Map) lstRs.get(i);
				menuLvl = CommonUtil.nvl(menuMap.get("MENU_LVL"));

				String strUrl = CommonUtil.getMenuUrl(menuMap);
				String strUrlTarget = CommonUtil.nvl(menuMap.get("URL_TARGET"), "_self");

				if ("1".equals(menuLvl)) {
			if (count >= limitCnt)
				break;
			if ("1".equals(preMenuLvl)) {
		%>
		</ul>
	</li>
	<%
	} else if ("2".equals(preMenuLvl)) {
	%>
			</li>
		</ul>
	</li>
	<%
	} else if ("3".equals(preMenuLvl)) {
	%>
			</li>
		</ul>
	</li>
	<%
	}
	%>
	<li class="m_main_menu"><a href="<%=CommonUtil.getNullInt(menuMap.get("child_cnt"), 0) > 0 ? "#" : strUrl%>"><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></a>
		<ul class="m_sub_menu" style="display:none;">
			<li>
				<%
				count++;
				} else if ("2".equals(menuLvl)) {
				if ("2".equals(preMenuLvl)) {
				%>
	<%
	} else if ("3".equals(preMenuLvl)) {
	%>
	</li>
	<%
	}
	%>
	<li><a href="<%=strUrl%>" target="<%=strUrlTarget%>"><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></a> <%
 } else if ("3".equals(menuLvl)) {
 %>
		<ul>
			<li><a href="<%=strUrl%>" target="<%=strUrlTarget%>"><%=CommonUtil.nvl(menuMap.get("MENU_NM"))%></a></li>
		</ul> <%
 }
 preMenuLvl = menuLvl;
 }
 if ("3".equals(preMenuLvl)) {
 %></li>
	<%
	}
	%>
		</ul>
	</li>
	<%
	}
	%>
	</ul>
</div>