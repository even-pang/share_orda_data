package com.ordadata.pack.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
 

public class UrlFilter implements Filter
{
 private FilterConfig filterConfig;
 


public void init(FilterConfig config) throws ServletException
 {
 this.filterConfig = config;
 }

 public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
 {
 String requestIP1 = "";
 String requestIP2 = "";
 String requestIP3 = "";

//아이피가 127.0.0.1이랑 111.224 로 시작하는 아이피만 허용한다는 뜻
   String allowIPScope = "127.0.0.1;111.224.*.*";


  HttpServletRequest httpReq = (HttpServletRequest) req;
  String strCallUrl = "";
  
  String strUrl = httpReq.getServletPath();
  System.out.println("Servlet Path :" + strUrl);
 
  strUrl = strUrl.toLowerCase();
  
  if (strUrl.indexOf("/aks/member/view.aspx") >= 0 ) {
	  strCallUrl = "/common/privacyPopup.do?menu_no=241"; 

  } else if (strUrl.indexOf("/aks/sub/sub_rsch.aspx") >= 0 ) {
	  strCallUrl = "/lab/contentsInfo.do?menu_no=296";
	  
  } else if (strUrl.indexOf("/glossary") >= 0 ) {
	  strCallUrl = "http://glossary.aks.ac.kr/";
	  
  } else if (strUrl.indexOf("/aks/default.aspx") >= 0)  {
	  strCallUrl = "/";
	  
  }  else if (strUrl.indexOf("/aks_home") >= 0)  {
	  strCallUrl = "/";
	  
  }  else if (strUrl.indexOf("/aks_kor") >= 0)  {
	  strCallUrl = "/";
	  
  }  else if (strUrl.indexOf("/aks") == 0)  {
	  strCallUrl = "/";
	  
  }  else if (strUrl.indexOf("/admission") >= 0 ) {
	  strCallUrl = "http://portal.aks.ac.kr/Entrance/login.jsp";
	  
  }
  
 if(!"".equals(strCallUrl))
 {
  
      PrintWriter out = resp.getWriter();
      resp.setContentType("text/html; charset=utf-8");

      out.println("<HTML>");
      out.println("<HEAD><TITLE>Move URL</TITLE></HEAD>");
      out.println("<BODY>");
      out.println("<meta http-equiv='refresh' content=\"0; url=" + strCallUrl + "\"></meta>");
      out.println("</BODY>");
      out.println("</HTML>");

      out.flush();
      out.close();
 }
 else
 {

    //여기 접근 가능
    chain.doFilter(req, resp);
 }
 }

 public void destroy()
 {

 }
}


