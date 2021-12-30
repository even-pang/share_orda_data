/*
 * Copyright (c) 2008 sosunj. All rights reserved.
 */
package com.ordadata.pack.charm.util;
  
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class CommonUtil {
    /**
     * Method Summary. <br>
     * �꽌釉붾┸ 由ы�섏뒪�듃瑜� Map�쑝濡� 蹂��솚�븯�뿬 由ы꽩.
     * @param request �꽌釉붾┸ 由ы�섏뒪�듃
     * @return map map
     * @throws e Exception
     * @since 1.00
     * @see
     */
    private static Random random =  new Random();
    
    /**
     * Constructor Summary. <br>
     * Constructor Description.
     * @since 1.00
     * @see
     */
    public CommonUtil() { };
       
    public static boolean isLogin(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String strUrl)
    {
    	return isLogin(servletRequest, servletResponse, strUrl, "USR");
    }    
    
    public static boolean isAdminLogin(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {
    	return isLogin(servletRequest, servletResponse, "", "ADM");
    }       
    
    public static boolean isLogin(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String strUrl, String strSesName)
    {
    	boolean bFlag = true;
    	try {
	    	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,strSesName);
	    	
	    	if (userMap == null) {
	    		bFlag = false;

	    		if ("ADM".equals(strSesName)) { 
	    		    CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);
	    		} else {
	    			CommonUtil.alertLoginGoUrl(servletResponse, strUrl);
	    		}
	    	}    	
    	} catch ( Exception e ) {
    		System.out.println(e.toString());
    	}
    	return bFlag;
    }       
    
    public static boolean isLoginSite(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String strUrl, String strReturl)
    {
    	boolean bFlag = true;
    	try {
	    	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
	    	
	    	if (userMap == null) {
	    		bFlag = false;

	    		CommonUtil.alertLoginGoUrl(servletResponse,  strUrl, strReturl);
	    	}    	
	    	
	    	String strUserId = CommonUtil.nvl(userMap.get("USER_ID"));
	    	
	    	if ("".equals(strUserId))  {
	    		bFlag = false;  
	    		CommonUtil.alertLoginGoUrl(servletResponse,  strUrl, strReturl);
	    	}
	    	
    	} catch ( Exception e ) {
    		System.out.println(e.toString());
    	}
    	return bFlag;
    }      
    
    public static String getDomainName(HttpServletRequest request) {
    	String strUrl = request.getRequestURL().toString();
    	
    	try {
    		int nPos = strUrl.indexOf("://") + 3;
    		
			strUrl = strUrl.substring(nPos);
			strUrl = strUrl.substring(0, strUrl.indexOf("/"));
			
			if (strUrl.indexOf(":") > -1) {
				strUrl = strUrl.substring(0, strUrl.indexOf(":"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "http://" + strUrl;
    }
    
    public static String getDomainUrl(HttpServletRequest request) {
    	String strUrl = request.getRequestURL().toString();
    	
    	try {
    		int nPos = strUrl.indexOf("://") + 3;
    		
			strUrl = strUrl.substring(nPos);
			strUrl = strUrl.substring(0, strUrl.indexOf("/"));
			
			/*if (strUrl.indexOf(":") > -1) {
				strUrl = strUrl.substring(0, strUrl.indexOf(":"));
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return "http://" + strUrl;
    }    
    
    public static String getDomainSSLUrl(HttpServletRequest request) {
    	String strUrl        = request.getRequestURL().toString();
    	String strServerName = request.getServerName();
    	String strHttp   = "http://";
    	
    	try {
    		int nPos = strUrl.indexOf("://") + 3;
    		
			strUrl = strUrl.substring(nPos);
			strUrl = strUrl.substring(0, strUrl.indexOf("/"));
			
			
			if ( strServerName.endsWith("aks.ac.kr") ) {
				//strHttp = "https://";
				strHttp = "http://";
			}
			
			 System.out.println("=====>" + strServerName);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return strHttp + strUrl;
    }        
    
  
    public static String getDomainSSLHttp(HttpServletRequest request) {
    	String strUrl        = request.getRequestURL().toString();
    	String strServerName = request.getServerName();
    	String strHttp   = "http://";
    	
    	try {
    		int nPos = strUrl.indexOf("://") + 3;
    		
			strUrl = strUrl.substring(nPos);
			strUrl = strUrl.substring(0, strUrl.indexOf("/"));
			
			
			if ( strServerName.endsWith("aks.ac.kr") ) {
				strHttp = "https://";
			}
			
			 System.out.println("=====>" + strServerName);
			 System.out.println("=====>" + strHttp);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return strHttp + strUrl;
    }       
    
    
    public static Map getRequestMap(HttpServletRequest request) {

        HashMap map = new HashMap();
        try {
            Map parameter = request.getParameterMap();

            if (parameter == null)
                return null;

            Iterator it = parameter.keySet().iterator();
            Object paramKey = null;
            String[] paramValue = null;
 
            while (it.hasNext()) {
                paramKey = it.next();
                paramValue = (String[]) parameter.get(paramKey);

                String strKey = paramKey.toString().toLowerCase();
                
                if (paramValue.length > 1) {
                	String[] arrVal = request.getParameterValues(paramKey.toString());
                	
                	for (int nLoop = 0; nLoop < arrVal.length; nLoop ++)
                	{
                		//if (!strKey.equals("ctnt")) {
                		//	arrVal[nLoop] = removeXSS(arrVal[nLoop]);
                		//} else {
                			arrVal[nLoop] =arrVal[nLoop];
                		//}
                	}
                	
                    map.put(strKey, arrVal);
                } else {      
                    map.put(strKey, (paramValue[0] == null) ? "" : paramValue[0].trim());
                }
            }
            return map;
        } catch (Exception e) {
            System.out.println("CommonUtil getRequestMap()" + e.toString());
            return null;
        }
    }

    public static String removeXSS(String strData) 
    {
    	String[] arrSrcCode = {"<script", "</script>","<", ">"};    	
    	String[] arrTgtCode = {"&ltscript_", "&lt/script_&gt", "&lt", "&gt"};
    	
    	//String[] arrSrcCode = {"<script", "</script>"};    	
    	//String[] arrTgtCode = {"&ltscript_", "&lt/script_&gt"};
    	

    	if ( strData == null || "".equals(strData) )
    		return strData;

       	for (int nLoop=0; nLoop < arrSrcCode.length; nLoop++)
       	{
       		strData = strData.replaceAll(arrSrcCode[nLoop], arrTgtCode[nLoop]);
       	}
    	
    	return strData;
    	
    }
    
    public static String recoveryLtGt(String strData)
    {
    	String[] arrSrcCode = {"&lt;", "&gt;","&lt", "&gt"};    	
    	String[] arrTgtCode = {"<", ">","<", ">"};
    	
    	if ( strData == null || "".equals(strData) )
    		return strData;
    	
    	for (int nLoop=0; nLoop < arrSrcCode.length; nLoop++)
    	{
    		strData = strData.replaceAll(arrSrcCode[nLoop], arrTgtCode[nLoop]);
    	}
    	
    	return strData;
    	
    }    
    
    /**
     * Method Summary. <br>
     * getUnivPageNavi �럹�씠吏� �꽕鍮꾧쾶�씠�뀡�쓣 留뚮뱶�뒗 �븿�닔
     * @param totCnt 寃뚯떆臾� 珥� 媛��닔
     * @param pageNow �쁽�옱 �럹�씠吏� 踰덊샇
     * @param param request query string
     * @param pagePerBlock �븳 釉붾윮�뿉 �몴�떆�븷 �럹�씠吏� 踰덊샇 媛��닔
     * @param numPerPage �븳 �럹�씠吏��떦 寃뚯떆臾� �씪�씤 �닔
     * @return pageNavi �럹�씠吏� �꽕鍮꾧쾶�씠�뀡 �씪�씤 臾몄옄�뿴
     * @throw Exception
     * @since 1.00
     * @see
     */ 
  
    public static String getUnivPageNavi(String link, int totCnt, int pageNow, String param, int pagePerBlock, int numPerPage) 
    {
    	String rtnNavi = "<ul>";
        String sDelim = "";
        String sPgDlm = "";
        String sParam = param;
        String strUrl = "";
        int iNext;
        int iPrev;

        if ( pageNow <= 0)
        	pageNow = 1;
        
        sParam = removeParam(param, "page_now"); 
        
        // 珥� �럹�씠吏� �닔
        int totalPage = (int) Math.ceil(totCnt / (numPerPage * 1d));

        // �쁽�옱 �럹�씠吏�媛� �냽�븳 釉붾윮 踰덊샇
        int currBlock = (int) Math.ceil(pageNow / (pagePerBlock * 1d));

        // 珥� 釉붾윮 媛��닔
        int totalBlock = (int) Math.ceil(totalPage / (pagePerBlock * 1d));

        // �쁽�옱 釉붾줉�쓽 �떆�옉 �럹�씠吏�
        int startPage = (currBlock - 1) * pagePerBlock + 1;
        // �쁽�옱 釉붾줉�쓽 留덉�留� �럹�씠吏�
        int endPage = startPage + pagePerBlock - 1;

        if (endPage > totalPage)
            endPage = totalPage;

        // �뙆�씪誘명꽣媛� �뾾�쑝硫� ? 遺숈엫
        if (link.indexOf("?") == -1)
            sDelim = "?";
        else
            sDelim = "&";

        if (!"".equals(sParam))
            sPgDlm = "&";
       
    	strUrl =  link + sDelim + sParam + sPgDlm;
    	    	
        if (currBlock > 1) {
            iPrev = (currBlock - 1)* pagePerBlock;

            rtnNavi += "<li class='li01'><a href='" + strUrl + "page_now=1'><img src='/university/images/board/btn_prev01.gif' alt='泥섏쓬'/>&nbsp;&nbsp;</a></li>";
            rtnNavi += "<li class='li01'><a href='" + strUrl + "page_now=" + iPrev +"'><img src='/university/images/board/btn_prev02.gif' alt='�씠�쟾'/>&nbsp;</a></li>";

        } else {
            rtnNavi += "<li class='li01'><a href='" + strUrl + "page_now=1'><img src='/university/images/board/btn_prev01.gif' alt='泥섏쓬'/>&nbsp;&nbsp;</a></li>";
            rtnNavi += "<li class='li01'><a href='" + strUrl + "page_now=1'><img src='/university/images/board/btn_prev02.gif' alt='�씠�쟾'/>&nbsp;</a></li>";

        }
        
        //�럹�씠吏� 踰덊샇
        if (endPage == 0) {
            rtnNavi += "<li><a href='#this' >1</a></li>";
        } else {
            for (int i = startPage; i <= endPage; i++) {           	
                if (i == pageNow) {
                	rtnNavi += "<li class='on'><a href='" + strUrl + "page_now=" + i + "'><strong>" + i + "</strong></a></li>";                   
                } else {
                    rtnNavi += "<li><a href='" + strUrl + "page_now=" + i + "'>" + i + "</a></li>";                  
                }
            }
        }

        if (currBlock < totalBlock) {
            iNext = (currBlock * pagePerBlock) + 1;   

						rtnNavi += "<li class='li01 ml_10'>&nbsp;&nbsp;<a href='" + strUrl+ "page_now="+iNext    +"'><img src='/university/images/board/btn_next02.gif' alt='�떎�쓬'/></a></li>";
						rtnNavi += "<li class='li01'>&nbsp;&nbsp;<a href='"       + strUrl +"page_now="+totalPage+"'><img src='/university/images/board/btn_next01.gif' alt='留덉�留�'/></a></li>";
        } else {
        
						rtnNavi += "<li class='li01 ml_10'>&nbsp;&nbsp;<a href='" + strUrl + "page_now=1'><img src='/university/images/board/btn_next02.gif'  alt='�떎�쓬'/></a></li>";
						rtnNavi += "<li class='li01'>&nbsp;&nbsp;<a href='"       + strUrl + "page_now="+totalPage+"'><img src='/university/images/board/btn_next01.gif' alt='留덉�留�'/></a></li>";

        }
        
        rtnNavi += "</ul>";
        
        return rtnNavi;
    }        
    
    /**
     * Method Summary. <br>
     * Front �럹�씠吏� �꽕鍮꾧쾶�씠�뀡�쓣 留뚮뱶�뒗 �븿�닔
     * @param totCnt 寃뚯떆臾� 珥� 媛��닔
     * @param pageNow �쁽�옱 �럹�씠吏� 踰덊샇
     * @param param request query string
     * @param pagePerBlock �븳 釉붾윮�뿉 �몴�떆�븷 �럹�씠吏� 踰덊샇 媛��닔
     * @param numPerPage �븳 �럹�씠吏��떦 寃뚯떆臾� �씪�씤 �닔
     * @return pageNavi �럹�씠吏� �꽕鍮꾧쾶�씠�뀡 �씪�씤 臾몄옄�뿴
     * @throw Exception
     * @since 1.00
     * @see
     */ 
    public static String getFrontPageNavi(String link, int totCnt, int pageNow, String param, int pagePerBlock, int numPerPage) 
    {
    	String rtnNavi = "";
        String sDelim = "";
        String sPgDlm = "";
        String sParam = param;
        String strUrl = "";
        int iNext;
        int iPrev;

        if ( pageNow <= 0)
        	pageNow = 1;
        
        sParam = removeParam(param, "page_now"); 
        
        // 珥� �럹�씠吏� �닔
        int totalPage = (int) Math.ceil(totCnt / (numPerPage * 1d));

        // �쁽�옱 �럹�씠吏�媛� �냽�븳 釉붾윮 踰덊샇
        int currBlock = (int) Math.ceil(pageNow / (pagePerBlock * 1d));

        // 珥� 釉붾윮 媛��닔
        int totalBlock = (int) Math.ceil(totalPage / (pagePerBlock * 1d));

        // �쁽�옱 釉붾줉�쓽 �떆�옉 �럹�씠吏�
        int startPage = (currBlock - 1) * pagePerBlock + 1;
        // �쁽�옱 釉붾줉�쓽 留덉�留� �럹�씠吏�
        int endPage = startPage + pagePerBlock - 1;

        if (endPage > totalPage)
            endPage = totalPage;

        // �뙆�씪誘명꽣媛� �뾾�쑝硫� ? 遺숈엫
        if (link.indexOf("?") == -1)
            sDelim = "?";
        else
            sDelim = "&";

        if (!"".equals(sParam))
            sPgDlm = "&";
       
    	strUrl =  link + sDelim + sParam + sPgDlm;
    	    	
        if (currBlock > 1) {
            iPrev = (currBlock - 1)* pagePerBlock;
            
			/*
			 * rtnNavi += "<a href=\"" + strUrl +
			 * "page_now=1\" class='pagingBtn leftBtn'></a>\n";
			 */
            rtnNavi += "<li><a href=\"" + strUrl + "page_now=" + iPrev +"\" class='pagingBtn leftBtn'>&lt;</a></li>\n";          
        } else {
			/*
			 * rtnNavi += "<a href=\"" + strUrl +
			 * "page_now=1\"><img src='/images/pagebefore2.png' alt='泥섏쓬�럹�씠吏�' /></a>\n";
			 */
            rtnNavi += "<li><a href=\"" + strUrl + "page_now=1\" class='pagingBtn leftBtn'>&lt;</a></li>\n";        	
        }
        
        //�럹�씠吏� 踰덊샇
        if (endPage == 0) {
            rtnNavi += "<li><a class='pagingNum on' href='#this' >1</a>";
        } else {
            for (int i = startPage; i <= endPage; i++) {           	
                if (i == pageNow) {
                	rtnNavi += "<li><a class=' on' href='" + strUrl + "page_now=" + i + "'>" + i + "</a></li>";                   
                } else {
                    rtnNavi += "<li><a href='" + strUrl + "page_now=" + i + "'>" + i + "</a></li>";                  
                }
            }
        }

        if (currBlock < totalBlock) {
            iNext = (currBlock * pagePerBlock) + 1;   
            
            rtnNavi += "<li><a href=\"" + strUrl+"page_now="+iNext+"\" class='pagingBtn rightBtn'>&gt;</a></li>\n";
			/*
			 * rtnNavi += "<a href=\"" + strUrl+"page_now="+
			 * totalPage+"\"><img src='/images/pagenext2.png' alt='留덉�留됲럹�씠吏�' /></a>";
			 */             
        } else {
            rtnNavi += "<li><a href=\"" + strUrl+"page_now=1\" class='pagingBtn rightBtn'>&gt;</a></li>\n";
			/*
			 * rtnNavi += "<a href=\"" + strUrl+"page_now="+
			 * totalPage+"\"><img src='/images/pagenext2.png' alt='留덉�留됲럹�씠吏�' /></a>";
			 */        	
        }
        
        rtnNavi += "";
        
        return rtnNavi;
    }         
    
    public static String getAdmPageNavi(String link, int totCnt, int pageNow, String param, int pagePerBlock, int numPerPage) 
    {
    	String rtnNavi = "";
        String sDelim = "";
        String sPgDlm = "";
        String sParam = param;
        String strUrl = "";
        int iNext;
        int iPrev;

        if ( pageNow <= 0)
        	pageNow = 1;
        
        sParam = removeParam(param, "page_now"); 
        
        // 珥� �럹�씠吏� �닔
        int totalPage = (int) Math.ceil(totCnt / (numPerPage * 1d));

        // �쁽�옱 �럹�씠吏�媛� �냽�븳 釉붾윮 踰덊샇
        int currBlock = (int) Math.ceil(pageNow / (pagePerBlock * 1d));

        // 珥� 釉붾윮 媛��닔
        int totalBlock = (int) Math.ceil(totalPage / (pagePerBlock * 1d));

        // �쁽�옱 釉붾줉�쓽 �떆�옉 �럹�씠吏�
        int startPage = (currBlock - 1) * pagePerBlock + 1;
        // �쁽�옱 釉붾줉�쓽 留덉�留� �럹�씠吏�
        int endPage = startPage + pagePerBlock - 1;

        if (endPage > totalPage)
            endPage = totalPage;

        // �뙆�씪誘명꽣媛� �뾾�쑝硫� ? 遺숈엫
        if (link.indexOf("?") == -1)
            sDelim = "?";
        else
            sDelim = "&";

        if (!"".equals(sParam))
            sPgDlm = "&";
       
    	strUrl =  link + sDelim + sParam + sPgDlm;
    	    	
        if (currBlock > 1) {
            iPrev = (currBlock - 1)* pagePerBlock;
            
            rtnNavi += " <span class='page_wrap'><a href=\"" + strUrl + "page_now=1\">&lt;&lt;</a></span>\n";
            rtnNavi += " <span class='page_wrap'><a  href=\"" + strUrl + "page_now=" + iPrev +"\">&lt;</a></span> \n";          
        } else {
            rtnNavi += " <span class='page_wrap'><a href=\"" + strUrl + "page_now=1\">&lt;&lt;</a></span>\n";
            rtnNavi += " <span class='page_wrap'><a  href=\"" + strUrl + "page_now=1\">&lt;</a></span> \n";        	
        }
         
        
        //�럹�씠吏� 踰덊샇
        if (endPage == 0) {
            rtnNavi += "<span class='page_wrap'><a href='#this' class='now_page'>1</a></span>&nbsp;";
        } else {
            for (int i = startPage; i <= endPage; i++) {           	
                if (i == pageNow) {
                	rtnNavi += "<span class='page_wrap'><a class='now_page' href='" + strUrl + "page_now=" + i + "'>" + i + "</a></span>";
                    if (i != endPage)
                        rtnNavi += "&nbsp;";
                } else {
                    rtnNavi += "<span class='page_wrap'><a href='" + strUrl + "page_now=" + i + "'>" + i + "</a></span>";
                    if (i != endPage)
                        rtnNavi += "&nbsp;";
                }
            }
        }

        if (currBlock < totalBlock) {
            iNext = (currBlock * pagePerBlock) + 1;   
            
            rtnNavi += "<span class='page_wrap'><a href=\"" + strUrl+"page_now="+iNext+"\">&gt;</a></span>\n";
            rtnNavi += "<span class='page_wrap'><a href=\"" + strUrl+"page_now="+totalPage+"\">&gt;&gt;</a></span>";             
        } else {
            rtnNavi += "<span class='page_wrap'><a href=\"" + strUrl+"page_now=1\">&gt;</a></span>\n";
            rtnNavi += "<span class='page_wrap'><a href=\"" + strUrl+"page_now="+totalPage+"\">&gt;&gt;</a></span>";        	
        }
        
        
        return rtnNavi;
    }        
       

    /**
     * Method Summary. <br>
     * Mobile �럹�씠吏� �꽕鍮꾧쾶�씠�뀡�쓣 留뚮뱶�뒗 �븿�닔
     * @param totCnt 寃뚯떆臾� 珥� 媛��닔
     * @param pageNow �쁽�옱 �럹�씠吏� 踰덊샇
     * @param param request query string
     * @param pagePerBlock �븳 釉붾윮�뿉 �몴�떆�븷 �럹�씠吏� 踰덊샇 媛��닔
     * @param numPerPage �븳 �럹�씠吏��떦 寃뚯떆臾� �씪�씤 �닔
     * @return pageNavi �럹�씠吏� �꽕鍮꾧쾶�씠�뀡 �씪�씤 臾몄옄�뿴
     * @throw Exception
     * @since 1.00
     * @see
     */ 
    public static String getMobilePageNavi(String link, int totCnt, int pageNow, String param, int pagePerBlock, int numPerPage) 
    {
    	String rtnNavi = "";
        String sDelim = "";
        String sPgDlm = "";
        String sParam = param;
        String strUrl = "";
        int iNext;
        int iPrev;

        if ( pageNow <= 0)
        	pageNow = 1;
        
        sParam = removeParam(param, "page_now"); 
        
        // 珥� �럹�씠吏� �닔
        int totalPage = (int) Math.ceil(totCnt / (numPerPage * 1d));

        // �쁽�옱 �럹�씠吏�媛� �냽�븳 釉붾윮 踰덊샇
        int currBlock = (int) Math.ceil(pageNow / (pagePerBlock * 1d));

        // 珥� 釉붾윮 媛��닔
        int totalBlock = (int) Math.ceil(totalPage / (pagePerBlock * 1d));

        // �쁽�옱 釉붾줉�쓽 �떆�옉 �럹�씠吏�
        int startPage = (currBlock - 1) * pagePerBlock + 1;
        // �쁽�옱 釉붾줉�쓽 留덉�留� �럹�씠吏�
        int endPage = startPage + pagePerBlock - 1;

        if (endPage > totalPage)
            endPage = totalPage;

        // �뙆�씪誘명꽣媛� �뾾�쑝硫� ? 遺숈엫
        if (link.indexOf("?") == -1)
            sDelim = "?";
        else
            sDelim = "&";

        if (!"".equals(sParam))
            sPgDlm = "&";
       
    	strUrl =  link + sDelim + sParam + sPgDlm;
    	    	
        if (currBlock > 1) {
            iPrev = (currBlock - 1)* pagePerBlock;
            
            rtnNavi += "<a href=\"" + strUrl + "page_now=1\"><img src='/mobile/img/pasing_home.jpg' alt='泥섏쓬�럹�씠吏�' /></a>\n";
            rtnNavi += "<a href=\"" + strUrl + "page_now=" + iPrev +"\"><img src='/mobile/img/pasing_left.jpg' alt='�씠�쟾' /></a>\n";          
        } else {
            rtnNavi += "<a href=\"" + strUrl + "page_now=1\"><img src='/mobile/img/pasing_home.jpg' alt='泥섏쓬�럹�씠吏�' /></a>\n";
            rtnNavi += "<a href=\"" + strUrl + "page_now=1\"><img src='/mobile/img/pasing_left.jpg' alt='�씠�쟾' /></a>\n";        	
        }
        
        //�럹�씠吏� 踰덊샇
        if (endPage == 0) {
            rtnNavi += "<a class='active' href='#this' >1</a>";
        } else {
            for (int i = startPage; i <= endPage; i++) {           	
                if (i == pageNow) {
                	rtnNavi += "<a class='active' href='" + strUrl + "page_now=" + i + "'>" + i + "</a>";                   
                } else {
                    rtnNavi += "<a href='" + strUrl + "page_now=" + i + "'>" + i + "</a>";                  
                }
            }
        }

        if (currBlock < totalBlock) {
            iNext = (currBlock * pagePerBlock) + 1;   
            
            rtnNavi += "<a href=\"" + strUrl+"page_now="+iNext+"\"><img src='/mobile/img/pasing_right.jpg' alt='�떎�쓬' /></a>\n";
            rtnNavi += "<a href=\"" + strUrl+"page_now="+totalPage+"\"><img src='/mobile/img/pasing_end.jpg' alt='留덉�留됲럹�씠吏�' /></a>";             
        } else {
            rtnNavi += "<a href=\"" + strUrl+"page_now=1\"><img src='/mobile/img/pasing_right.jpg' alt='�떎�쓬' /></a></li>\n";
            rtnNavi += "<a href=\"" + strUrl+"page_now="+totalPage+"\"><img src='/mobile/img/pasing_end.jpg' alt='留덉�留됲럹�씠吏�' /></a>";        	
        }
        
        rtnNavi += "";
        
        return rtnNavi;
    }         

    /**
     * Method Summary. <br>
     * @param sData String : �뜲�씠�꽣 媛�
     * @param sTrans String : null, "", "null"�씪 寃쎌슦 蹂�寃쏀븷媛�
     * @return String
     */
    public static int getNullInt(Object objData, int nTrans) {
    	return Integer.parseInt(getNullTrans(objData, nTrans));
    }   
    
    
    /**
     * Method Summary. <br>
     * @param sData String : �뜲�씠�꽣 媛�
     * @return String
     */
    public static String getNullTrans(String sData) {
        return getNullTrans(sData, "");
    }

    /**
     * Method Summary. <br>
     * @param sData String : �뜲�씠�꽣 媛�
     * @param sTrans String : null, "", "null"�씪 寃쎌슦 蹂�寃쏀븷媛�
     * @return String
     */
    public static String getNullTrans(String sData, String sTrans) {
        if (sTrans == null)
            sTrans = "";
        if (sData != null && !"".equals(sData) && !"null".equals(sData))
            return removeXSS(sData.trim());

        return removeXSS(sTrans);
    }

    /**
     * Method Summary. <br>
     * @param oData Object : 媛앹껜
     * @param sTrans String : null, "", "null"�씪 寃쎌슦 蹂�寃쏀븷媛�
     * @return String
     */
    public static String getNullTrans(Object oData, String sTrans) {
        if (sTrans == null)
            sTrans = "";
        if (oData != null && !"".equals(oData) && !"null".equals(oData))
            return removeXSS(oData.toString().trim());

        return removeXSS(sTrans);
    }

    public static String getNullTrans(Object oData, int nTrans) {
        return getNullTrans(oData, Integer.toString(nTrans));
    }

    /**
     * Method Summary. <br>
     * @param oData Object : 媛앹껜
     * @return String
     */
    public static String getComma(String strVal) {
    	
    	strVal = getNullTrans(strVal, "0");
    	
        DecimalFormat formatter = new DecimalFormat("#,##0");
        return formatter.format(Integer.parseInt(strVal));
    }
    
    public static String getComma(long nVal) {     
        return getComma(Long.toString(nVal));
    }    
    
    public static String XSSConv(Object oData, Map brdMgrMap) {
    	
    	String brd_mgrno = nvl(brdMgrMap.get("BRD_MGRNO"));
    	
    	if("181".equals(brd_mgrno) || "184".equals(brd_mgrno)) {
    		return XSSConv(oData, nvl(brdMgrMap.get("WRITE_AUTH_CD")));
    	} else {
    		return (String) oData;
    	}
    	
    	//return XSSConv(oData, nvl(brdMgrMap.get("USER_TYPE")));
    }        
    
    public static String XSSConv(Object oData, String strWriteAuth) {
    	
    	String strVal = nvl(oData);
    	if ("ADMIN".equals(strWriteAuth) || "MANAGER".equals(strWriteAuth) || "COUNSEL".equals(strWriteAuth)) {
    		strVal = recoveryLtGt(strVal);
    	}
    	
    	strVal = strVal.replaceAll("&ltbr/&gt", "<br/>");    	
    	strVal = strVal.replaceAll("&ltbr&gt", "<br/>");
    	
    	strVal = strVal.replaceAll("&lttable", "<table");    	
    	strVal = strVal.replaceAll("&lt/table&gt", "</table>");    	
    	
    	strVal = strVal.replaceAll("&lttr", "<tr");    	
    	strVal = strVal.replaceAll("&lt/tr&gt", "</tr>");      	
    	
    	strVal = strVal.replaceAll("&lttd", "<td");    	
    	strVal = strVal.replaceAll("&lt/td&gt", "</td>");    	
    	
        return strVal;
    }    
    
     
    
    
    /**
     * Method Summary. <br>
     * @param oData Object : 媛앹껜
     * @return String
     */
    public static String nvl(Object oData) {
        return getNullTrans(oData, "");
    }    
   
    /**
     * Method Summary. <br>
     * @param oData Object : 媛앹껜
     * @param sTrans String : null, "", "null"�씪 寃쎌슦 蹂�寃쏀븷媛�
     * @return String
     */
    public static String nvl(Object oData, String sTrans) {
    	return getNullTrans(oData, sTrans);
    }    
    
    /**
     * Method Summary. <br>
     * @param oData Object : 媛앹껜
     * @return String
     */
    public static String getNullTrans(Object oData) {
        return getNullTrans(oData, "");
    }

    /**
     * Method Summary. <br>
     * Method Description.
     * @param name description
     * @return description
     * @throws name description
     * @since 1.00
     * @see
     */
    public static String getFormParm(HttpServletRequest request) {
        return getFormParm(request, "");
    }

    /**
     * Method Summary. <br>
     * html hidden �냽�꽦�쓽 �뙆�씪誘명꽣 �깮�꽦 method.
     * @param request HttpServletRequest
     * @param notParam �젣�쇅 �뙆�씪誘명꽣
     * @return retQueryString
     * @throws name description
     * @since 1.00
     * @see
     */
    public static String getFormParm(HttpServletRequest request, String notParam) {
        HashMap map = new HashMap();

        String retQueryString = "";

        Map parameter = request.getParameterMap();
        Iterator it = parameter.keySet().iterator();
        Object paramKey = null;
        String[] paramValue = null;

        while (it.hasNext()) {

            paramKey = it.next();
            if (paramKey.equals(notParam))
                continue;

            paramValue = (String[]) parameter.get(paramKey);

            for (int i = 0; i < paramValue.length; i++) {
                retQueryString += "<input name=\"" + paramKey + "\" type=hidden value=\"" + removeXSS(paramValue[i]) + "\" >  \n";
            }
        }
        return retQueryString;
    }
    
         
    /**
     * Method Summary. <br>
     * �썝�븯�뒗 �뀈�룄 selectBox濡� 蹂��솚�븯�뒗 method
     * @param nStart
     * @param nEnd
     * @param nComp
     * @return strSel
     */
    public static String getYearSelectBox(int nStart, int nEnd, String nComp){
    	String strSel ="";
    	strSel += " <option value=''>" + "�꽑�깮(select)" + "</option>\n";
    	for(int i=nStart; i>=nEnd; i--){
    		String strSelected = Integer.toString(i).equals(nComp) ? " selected " : "";
    		strSel += " <option value=\'" + i + "\'" + strSelected + ">" + i + "</option>\n";
    	}
    	return strSel;
    }
    
    /**
     * Method Summary. <br>
     * �썝�븯�뒗 �뀈�룄 selectBox濡� 蹂��솚�븯�뒗 method
     * @param nStart
     * @param nEnd
     * @param nComp
     * @return strSel
     */
    public static String getStatsYearSelectBox(int nStart, int nEnd, String nComp){
    	String strSel ="";
    	for(int i=nStart; i<=nEnd; i++){
    		String strSelected = Integer.toString(i).equals(nComp) ? " selected " : "";
    		strSel += " <option value=\'" + i + "\'" + strSelected + ">" + i + "�뀈 </option>\n";
    	}
    	return strSel;
    }
    
    /**
     * Method Summary. <br>
     * �썝�븯�뒗 �썡 selectBox濡� 蹂��솚�븯�뒗 method
     * @param nStart
     * @param nEnd
     * @param nComp
     * @return strSel
     */
    public static String getMonthSelectBox(int nStart, int nEnd, String nComp){
    	String strSel ="";
    	for(int i=nStart; i<=nEnd; i++){
    		
    		String suffix = String.format("%02d", i); 

    		String strSelected = suffix.equals(nComp) ? " selected " : "";
    		strSel += " <option value=\'" + suffix + "\'" + strSelected + ">" + i + "</option>\n";
    	}
    	return strSel;
    }
    
   
     
    
    /**
     * Method Summary. <br>
     * 怨듯넻肄붾뱶瑜� checkbox濡� 蹂��솚�븯�뒗 method.
     * @param listRow List 怨듯넻肄붾뱶 荑쇰━ list
     * @param strCompare String selected index
     * @return selectbox String
     * @throws name description
     * @since 1.00
     * @see
     */  
    public static String getMakeCodeName(List listRow, String strCompare) {
        String strVal = "";
        try {
            Iterator iterator = listRow.iterator();
            strCompare = getNullTrans(strCompare,"").trim();
            
            while (iterator.hasNext()) {
                Map resultMap = (Map) iterator.next();
                String strCode = resultMap.get("COMM_CD").toString();
                String strName = (String) resultMap.get("CD_NM").toString();
                
                if ( strCompare.indexOf(strCode) >= 0 )
                {
                   if ( !"".equals(strVal)) 
                	strVal += " / ";
                   
                    strVal += strName;
                }
            }

        } catch (Exception e) {
            System.out.println("CommonService[getMakeSelectBox] ==>" + e.toString());
        }

        return strVal;
    }    
    
    
   
    /**
     * Throws :<br>
     * Parameters : HttpServletRequest request <br>
     * Parameters : String[] notParam �젣�쇅 �뙆�씪誘명꽣 Return Value : String <br>
     * �궡�슜 : HttpServletRequest瑜� �씠�슜�븯�뿬 �뙆�씪硫뷀꽣瑜� 由ы꽩�븿 <br>
     */
    public static String getRequestQueryString(HttpServletRequest request, String[] notParam) {
        String retQueryString = "";

        Map parameter = request.getParameterMap();
        Iterator it = parameter.keySet().iterator();
        Object paramKey = null;
        String[] paramValue = null;

        while (it.hasNext()) {
            paramKey = it.next();

            paramValue = (String[]) parameter.get(paramKey);
            
            boolean bParam = true;
            for (int i = 0; i < paramValue.length; i++) {
                for (int j = 0; j < notParam.length; j++) {
                    if (paramKey.equals(notParam[j])) {
                        bParam = false;
                    }
                }
                if(bParam){
                    retQueryString += "&" + paramKey + "=" + paramValue[i];
                }
            }
        }

        return retQueryString;
    }

    /**
     * Throws :<br>
     * Parameters : MultipartRequest request <br>
     * Return Value : String <br>
     * �궡�슜 : HttpServletRequest瑜� �씠�슜�븯�뿬 �뙆�씪硫뷀꽣瑜� 由ы꽩�븿 <br>
     */
    public static String getRequestQueryString(MultipartRequest multiReq) {
        String retQueryString = "";
        String[] paramValue = null;
        try {

            Enumeration eParam = ((ServletRequest) multiReq).getParameterNames();

            while (eParam.hasMoreElements()) {
                String strKey = (String) eParam.nextElement();

                paramValue = ((ServletRequest) multiReq).getParameterValues(strKey);

                if (retQueryString.length() > 0)
                    retQueryString = retQueryString + "&";

                retQueryString += retQueryString + strKey + "=" + ((ServletRequest) multiReq).getParameterValues(strKey)[0];
            }

            return retQueryString;
        } catch (Exception e) {
            System.out.println("getRequestMap()" + e.toString());
            return null;
        }

    }

    /**
     * Throws :<br>
     * Parameters : MultipartRequest request <br>
     * Return Value : String <br>
     * �궡�슜 : HttpServletRequest瑜� �씠�슜�븯�뿬 �뙆�씪硫뷀꽣 HashMap�뿉 ���옣�븯�뿬 諛섑솚 <br>
     */
    public static Map getRequestMap(HttpServletRequest request, boolean bFileUpload) {

        Map mapParam = new HashMap();
        Map retMap = new HashMap();
        if (!bFileUpload) {
            return getRequestMap(request);
        }

        try {
            Part part;
            MultipartParser multiReq = new MultipartParser(request, 10 * 1024 * 1024); // 10MB

            while ((part = multiReq.readNextPart()) != null) {
                String strKey = new String(part.getName());

                if (part.isParam()) { // �뙆�씪�씠 �븘�땺�븣
                    ParamPart paramPart = (ParamPart) part;
                    String strValue = new String(paramPart.getStringValue());
                    mapParam.put(strKey.toLowerCase(), strValue);
                    //System.out.println("param; strKey=" + strKey + ", value=" + strValue);
                }

            }
            retMap.put("param", mapParam);
            retMap.put("multi", multiReq);
            return retMap;
        } catch (Exception e) {
            System.out.println("getRequestMap()" + e.toString());
            return null;
        }
    }

    /**
     * Throws :<br>
     * Parameters : HttpServletRequest request <br>
     * Return Value : String <br>
     * �궡�슜 : HttpServletRequest瑜� �씠�슜�븯�뿬 �뙆�씪硫뷀꽣瑜� 由ы꽩�븿 <br>
     */
    public static String getRequestQueryString(HttpServletRequest request) throws Exception{

        HashMap map = new HashMap();

        String retQueryString = "";
        String browser = getBrowser(request);

        Map parameter = request.getParameterMap();
        Iterator it = parameter.keySet().iterator();
        Object paramKey = null;
        String[] paramValue = null;

        while (it.hasNext()) {
            paramKey = it.next();

            paramValue = (String[]) parameter.get(paramKey);

            for (int i = 0; i < paramValue.length; i++) {
                if (retQueryString.length() > 0)
                    retQueryString = retQueryString + "&";
                
                if(browser.equals("MSIE")) {
                	retQueryString = retQueryString + paramKey + "=" + URLEncoder.encode(paramValue[i], "UTF-8").replaceAll("\\+", "%20");
                } else if(browser.equals("Trident")) {
                	retQueryString = retQueryString + paramKey + "=" + URLEncoder.encode(paramValue[i], "UTF-8").replaceAll("\\+", "%20");
                } else {
                	retQueryString = retQueryString + paramKey + "=" + paramValue[i];
                }
            }
        }

        return retQueryString;

    }

    /**
     * Throws :<br>
     * Parameters : HttpServletRequest request <br>
     * Return Value : String <br>
     * �궡�슜 : HttpServletRequest瑜� �씠�슜�븯�뿬 �뙆�씪硫뷀꽣瑜� 由ы꽩�븿 <br>
     */
    public static String getRequestQueryString8859(HttpServletRequest request) {

        HashMap map = new HashMap();

        String retQueryString = "";

        Map parameter = request.getParameterMap();
        Iterator it = parameter.keySet().iterator();
        Object paramKey = null;
        String[] paramValue = null;

        while (it.hasNext()) {
            paramKey = it.next();
            paramValue = (String[]) parameter.get(paramKey);

            for (int i = 0; i < paramValue.length; i++) {
                if (retQueryString.length() > 0)
                    retQueryString = retQueryString + "&";

                try {
                    retQueryString = retQueryString + paramKey + "=" + URLEncoder.encode(paramValue[i], "8859_1");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return retQueryString;
    }

    /**
     * Throws :<br>
     * Parameters : HttpServletRequest request <br>
     * Return Value : String <br>
     * �궡�슜 : getRequestInputString �씠�슜�븯�뿬 �뙆�씪硫뷀꽣瑜� 由ы꽩�븿 <br>
     */
    public static String getRequestInputString(HttpServletRequest request) {
        return getRequestInputString(request, "");
    }

    public static String getRequestInputString(HttpServletRequest request, String notParam) {
        HashMap map = new HashMap();

        String retQueryString = "";

        Map parameter = request.getParameterMap();
        Iterator it = parameter.keySet().iterator();
        Object paramKey = null;
        String[] paramValue = null;

        while (it.hasNext()) {

            paramKey = it.next();
            if (paramKey.equals(notParam))
                continue;

            paramValue = (String[]) parameter.get(paramKey);

            for (int i = 0; i < paramValue.length; i++) {
                retQueryString += "<input name=\"" + paramKey + "\" type=hidden value=\"" + paramValue[i] + "\" >  \n";
            }
        }

        return retQueryString;

    }

    /**
     * 
     * �옉�뭹�뿰愿��씠誘몄� �뙆�씪 泥⑤� KEY媛� �궃�닔 �깮�꽦
     * @return strVal
     */
    public static String getUniqueValue()
    {
    	String strVal="";  

    	strVal = getCurrentDate("", "YYYYMMDDHHMISS");
    	Random rand =  new Random();
    	strVal += Long.toString(rand.nextLong());

    	return strVal;
    }
    
    
    /**
     * 
     * �엫�떆鍮꾨�踰덊샇 �깮�꽦
     * @return strVal
     */
    public static String shufflePasswd(int nLen)
    {
		 char[] charSet = new char[]{
		    '0','1','2','3','4','5','6','7','8','9'
		    ,'a','b','c','d','e','f','g','h','i','j','k','l','m'
		    ,'n','o','p','q','r','s','t','u','v','w','x','y','z'};
				  
		  int idx = 0;
		  StringBuffer sb = new StringBuffer();
	
		  for(int i=0; i<nLen; i++){
		   idx = (int)(charSet.length*Math.random());
		   sb.append(charSet[idx]);
		  }

    	return sb.toString();
    }
    
    /**
     * Throws : IOException <br>
     * Parameters : String StrDelimittoken : (ex) "/" , ".", "-" , String rtnFormmat <br>
     * Return Value : String <br>
     * �궡�슜 : �삤�뒛 �궇吏� 媛� 媛��졇�삤湲� <br>
     */
    public static String getCurrentDate(String StrDelimittoken, String rtnFormmat) {

        try {
            Calendar currentWhat = Calendar.getInstance();
            int currentYear = currentWhat.get(Calendar.YEAR);
            int currentMonth = currentWhat.get(Calendar.MONTH) + 1;
            int currentDay = currentWhat.get(Calendar.DAY_OF_MONTH);
            int currentHour = currentWhat.get(Calendar.HOUR);
            int currentMinute = currentWhat.get(Calendar.MINUTE);
            int currentSecond = currentWhat.get(Calendar.SECOND);

            String yearToday = padLeftwithZero(currentYear, 4); // 4�옄由� �뒪�듃留곸쑝濡� 蹂��솚
            String monthToday = padLeftwithZero(currentMonth, 2); // 2�옄由� �뒪�듃留곸쑝濡� 蹂��솚
            String dayToday = padLeftwithZero(currentDay, 2); // 2�옄由� �뒪�듃留곸쑝濡� 蹂��솚
            String hourToday = padLeftwithZero(currentHour, 2); // 2�옄由� �뒪�듃留곸쑝濡� 蹂��솚
            String minuteToday = padLeftwithZero(currentMinute, 2); // 2�옄由� �뒪�듃留곸쑝濡� 蹂��솚
            String secondToday = padLeftwithZero(currentSecond, 2); // 2�옄由� �뒪�듃留곸쑝濡� 蹂��솚

            if (rtnFormmat.equals("YYYY/MM/DD HH:MI:SS")) {
                return new String(yearToday + "/" + monthToday + "/" + dayToday + " " + hourToday + ":" + minuteToday
                        + ":" + secondToday);
            } else if (rtnFormmat.equals("YYYY-MM-DD")) {
                return new String(yearToday + "-" + monthToday + "-" + dayToday);
            } else if (rtnFormmat.equals("YYYYMMDD-HHMISS")) {
                return new String(yearToday + "" + monthToday + "" + dayToday + "-" + hourToday + "" + minuteToday
                        + "" + secondToday);
            } else if (rtnFormmat.equals("YYYYMMDDHHMISS")) {
                return new String(yearToday + "" + monthToday + "" + dayToday + hourToday + "" + minuteToday + ""
                        + secondToday);
            } else if (rtnFormmat.equals("YYYYMMDD")) {
                return new String(yearToday + monthToday + dayToday);
            } else if (rtnFormmat.equals("HH:MI:SS")) {
                return new String(hourToday + ":" + minuteToday + ":" + secondToday);
            } else if (rtnFormmat.equals("YYYY")) {
                return new String(yearToday);
            } else if (rtnFormmat.equals("MM")) {
                return new String(monthToday);
            } else if (rtnFormmat.equals("DD")) {
                return new String(dayToday);
            } else if (rtnFormmat.equals("HHMI")) {
                return new String(hourToday + minuteToday);
            } else if (rtnFormmat.equals("HH:MI")) {
                return new String(hourToday + ":" + minuteToday);
            } else if (rtnFormmat.equals("dafault")) {
                return new String(yearToday + StrDelimittoken + monthToday + StrDelimittoken + dayToday);
            } else {
                return new String(yearToday + StrDelimittoken + monthToday + StrDelimittoken + dayToday);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCurrentDate() {
        return getCurrentDate("", "");
    }

    /**
     * �쁽�옱 �궇吏쒖� 鍮꾧탳. <br>
     * 媛믪씠 �옉�쑝硫� -1, 媛숈쑝硫� 0, �겕硫� 1�쓣 諛섑솚�븳�떎.
     * @param objDate
     * @return -1, 0, 1
     * @since 1.00
     * @see
     */
    public static int getCurrentDateCompare(Object objDate) {

        if (objDate == null || "".equals(objDate.toString()))
            return -2;

        String strCompareDate = getConv(objDate);
        String strCurDate = CommonUtil.getCurrentDate();
        return strCompareDate.compareTo(strCurDate);
    }

    /**
     * Throws : IOException <br>
     * Parameters : 1) String startDate : �떆�옉�씪�옄 YYYYMMDD <br>
     * 2) String endDate : 醫낅즺�씪�옄 YYYYMMDD Return Value : int <br>
     * �궡�슜 : �궇吏쒖궗�씠�쓽 湲곌컙�쓣 �씪�닔濡� 由ы꽩�븳�떎 <br>
     */
    public static int getPeriodByDay(String startDate, String endDate) throws IOException {

        long endTimeStamp = Long.parseLong(getTimeStamp(endDate));
        long startTimeStamp = Long.parseLong(getTimeStamp(startDate));

        long periodBySecond = endTimeStamp - startTimeStamp;

        double fPeriodByDay = (periodBySecond / (60 * 60 * 24));

        return (int) Math.ceil(fPeriodByDay);

    }

    /** 1970�뀈 1�썡 1�씪 0�떆 0遺� 0珥� 遺��꽣 吏��젙�맂 �궇吏쒓퉴吏��쓽 珥덈�� 媛��졇�삩�떎. */
    private static String getTimeStamp(String endDate) throws IOException {

        int endYear = Integer.parseInt(endDate.substring(0, 4));
        int endMonth = Integer.parseInt(endDate.substring(4, 6));
        int endDay = Integer.parseInt(endDate.substring(6, 8));

        Calendar cal = Calendar.getInstance();
        cal.set(endYear, endMonth, endDay, 0, 0, 0);

        long timeStamp = 0L;
        String strTimeStamp = "";
        

        long stdYear = 1970L;

        long passedYear = (cal.get(Calendar.YEAR) - stdYear) * 365 * 24 * 60 * 60;
        long passedDay = cal.get(Calendar.DAY_OF_YEAR) * 24 * 60 * 60;
        long passedTime = (cal.get(Calendar.HOUR) * 60 * 60) + (cal.get(Calendar.MINUTE) * 60)
                + cal.get(Calendar.SECOND);

        timeStamp = passedYear + passedDay + passedTime;

        strTimeStamp = Long.toString(timeStamp);

        while (strTimeStamp.length() < 12)
            strTimeStamp = "0" + strTimeStamp;

        return strTimeStamp;
    }

    /**
     * Parameters : srcDate - 湲곗��씪�옄 <br>
     * cnt - 移댁슫�꽣 <br>
     * Return Value : String <br>
     * �궡�슜 : 湲곗��씪�뿉 �빐�떖 移댁슫�꽣 留뚰겮�쓽 �썡�닔瑜� 利앷� �떆�궓�떎. <br>
     */
    public static String addMonth(String srcDate, int cnt) {
        String rtnData = null;
        try {

            String year = srcDate.substring(0, 4);
            String month = srcDate.substring(4, 6);
            String day = srcDate.substring(6, 8);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            formatter.getCalendar().set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day),
                    0, 0, 0);
            formatter.getCalendar().add(Calendar.MONTH, cnt);

            Date chkDay = formatter.getCalendar().getTime();

            rtnData = (String) formatter.format(chkDay);

            year = rtnData.substring(0, 4);
            month = rtnData.substring(5, 7);
            day = rtnData.substring(8, 10);

            rtnData = year + month + day;
        } catch (Exception e) {
            e.printStackTrace();
            rtnData = "";
        }
        return rtnData;
    }

    /**
     * Parameters : srcDate - 湲곗��씪�옄 <br>
     * cnt - 移댁슫�꽣 <br>
     * Return Value : String <br>
     * �궡�슜 : 湲곗��씪�뿉 �빐�떖 移댁슫�꽣 留뚰겮�쓽 �씪�옄瑜� 利앷��떆�궓�떎. <br>
     */
    public static String addDay(String srcDate, int cnt) {
        String rtnData = null;
        try {

            String year = srcDate.substring(0, 4);
            String month = srcDate.substring(4, 6);
            String day = srcDate.substring(6, 8);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            formatter.getCalendar().set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day),
                    0, 0, 0);
            formatter.getCalendar().add(Calendar.DATE, cnt);

            Date chkDay = formatter.getCalendar().getTime();

            rtnData = (String) formatter.format(chkDay);

            year = rtnData.substring(0, 4);
            month = rtnData.substring(5, 7);
            day = rtnData.substring(8, 10);

            rtnData = year + month + day;
        } catch (Exception e) {
            e.printStackTrace();
            rtnData = "";
        }
        return rtnData;
    }
     
   /**
    * �쟾�솕踰덊샇 "-" �몴�떆 
    * @param objPhone
 	* @return getPhoneFormat
 	*/
    public static String getPhoneFormat(Object objPhone) {
    	
    	String phone = objPhone.toString();
    	
        return getPhoneFormat(phone, "-");
    }
   
   /**
    * �쟾�솕踰덊샇 7�옄由�, 8�옄由� 援щ텇�뿉 �뵲瑜� "-" �몴�떆
    * @param objPhone
 	* @param delim
 	* @return tmp
 	*/
    public static String getPhoneFormat(Object objPhone, String delim) {
       
	   String tmp = "";
       String phone = objPhone.toString();
       int  iDayLen = phone.length();

       if (iDayLen < 4) {
           tmp = phone;
       } else if(iDayLen < 8){
           tmp = phone.substring(0, 3) + delim + phone.substring(3, 7);
       } else if (iDayLen < 9){
           tmp = phone.substring(0, 4) + delim + phone.substring(4, 8);	   
       }

       return tmp;
   }

   
   /**
    * Parameters : String day, String delim <br>
    * Return Value : String <br>
    * �궡�슜 : Date Format 蹂�寃�. <br>
    */
   public static String getDateFormat(Object objDay) {
   	
	   if ( objDay == null)
	     return "";
	   
	   String day = objDay.toString();
  
	   day = day.replaceAll("-", "");
	   day = day.replaceAll("//.", "");	   
	   day = day.replaceAll(":", "");
	   day = day.replaceAll("\\[", "");
	   day = day.replaceAll("\\]", "");
	   day = day.replaceAll(" ", "");
	   
	   if (day.length() == 6) { 
		   return day.substring(0, 4) + "-" + day.substring(4, 6);
	   }
	   
	   if (day.length() < 8) 
	   	  return objDay.toString();
 
       return getDateFormat(day.substring(0, 8), "ymd");
   }
   
   public static String getAmericanDate(Object objDay)
   {
	   String strVal = "";
	   String strYear = "";
	   String strMonth = "";
	   
	   String strDate = "";
	   
	   if ( objDay == null)
		   return "";
	   
	   strDate = objDay.toString();
	   strVal  = strDate;
	   
	   try {
		   
		   strDate = strDate.replace(".", "");
		   strDate = strDate.replace("-", "");
		   strDate = strDate.replace("/", "");
		   
		   strYear = strDate.substring(0, 4);

           switch (Integer.parseInt(strDate.substring(4, 6)))
           {
	           case 1  : strMonth = "JAN"; break;
	           case 2  : strMonth = "FEB"; break;
	           case 3  : strMonth = "MAR"; break;
	           case 4  : strMonth = "APR"; break;
	           case 5  : strMonth = "MAY"; break;
	           case 6  : strMonth = "JUN"; break;
	           case 7  : strMonth = "JUL"; break;
	           case 8  : strMonth = "AUG"; break;
	           case 9  : strMonth = "SEP"; break;
	           case 10 : strMonth = "OCT"; break;
	           case 11 : strMonth = "NOV"; break;
	           case 12 : strMonth = "DEC"; break;
           }
		   
           strVal = strMonth + "," + strYear;
           
	   } catch ( Exception e) {
		   //System.out.println(e.toString());
	   }
	   
	   return strVal;
   }
   
   /**
    * Parameters : String day, String delim <br>
    * Return Value : String <br>
    * �궡�슜 : Date Format 蹂�寃�. <br>
    */
   public static String getDateTimeFormat(Object objDay) {
   	
	   if ( objDay == null)
		   return "";
	   
	   String day = objDay.toString();

	   day = day.replaceAll("-", "");
	   day = day.replaceAll("//.", "");	   
	   
	   if (day.length() < 12)
	   	  return objDay.toString();
 
       return getDateFormat(day, "-");
   }   
    /**
     * Parameters : String day, String delim <br>
     * Return Value : String <br>
     * �궡�슜 : Date Format 蹂�寃�. <br>
     */
    public static String getDateFormat(Object objDay, String delim) {
        String tmp = "";

        if (objDay == null)
            return tmp;
        String day = objDay.toString();
 
        day = day.replace(".", "");
        day = day.replace("-", "");
        day = day.replace("/", "");

        int  iDayLen = day.length();

        if (day == null || day.equals("") || delim == null) {
            tmp = "";
        } else if (iDayLen < 8) {
            tmp = day;
        } else if (delim.equals("ymd")) {
            tmp = day.substring(0, 4) + "." + day.substring(4, 6) + "." + day.substring(6, 8);
        } else if (delim.equals("YMD")) {
        	tmp = day.substring(0, 4) + "�뀈 " + day.substring(4, 6) + "�썡 " + day.substring(6, 8) + "�씪";
        } else {
        	tmp = day.substring(0, 4) + delim + day.substring(4, 6) + delim + day.substring(6, 8);
        	
        	if ( iDayLen > 11) {
        		tmp +=  " " + day.substring(8, 10) + "" + day.substring(10, 12) + "" + day.substring(12);
        	} else if ( iDayLen > 8) {
               tmp +=  " " + day.substring(8, 10) + "" + day.substring(10, 12); 
               //+ ":" + day.substring(12);
        	}  
        }
        
        return tmp;
    }
     
    /**
     * �슂�씪�쓣 �뼸�쓬. <br>
     * @param strDay
     * @return �슂�씪
     * @since 1.00
     * @see
     */
    public static String getWeekdayName(String strDay) {

        if (strDay == null || "".equals(strDay))
            return "";

        int year = Integer.parseInt(strDay.substring(0, 4));
        int month = Integer.parseInt(strDay.substring(4, 6));
        int day = Integer.parseInt(strDay.substring(6, 8));

        if (month == 1 || month == 2)
            year--;
        month = (month + 9) % 12 + 1;
        int y = year % 100;
        int century = year / 100;
        int week = ((13 * month - 1) / 5 + day + y + y / 4 + century / 4 - 2 * century) % 7;
        if (week < 0)
            week = (week + 7) % 7;
        String strWeek = "";
        switch (week) {
        case 0:
            strWeek = "�씪";
            break;
        case 1:
            strWeek = "�썡";
            break;
        case 2:
            strWeek = "�솕";
            break;
        case 3:
            strWeek = "�닔";
            break;
        case 4:
            strWeek = "紐�";
            break;
        case 5:
            strWeek = "湲�";
            break;
        case 6:
            strWeek = "�넗";
            break;
        }

        return strWeek;
    }

    public static String getWeekdayName(int nDay) {

    	String strWeek = "";
    
        switch (nDay) {
        case 1:
            strWeek = "�씪";
            break;
        case 2:
            strWeek = "�썡";
            break;
        case 3:
            strWeek = "�솕";
            break;
        case 4:
            strWeek = "�닔";
            break;
        case 5:
            strWeek = "紐�";
            break;
        case 6:
            strWeek = "湲�";
            break;
        case 7:
            strWeek = "�넗";
            break;
        }

        return strWeek;
    }    
    
    /**
     * �슂�씪 踰덊샇瑜� �뼸�쓬.(0:�씪�슂�씪) <br>
     * @param strDay
     * @return �슂�씪
     * @since 1.00
     * @see
     */
    public static int getWeekdayNum(String strDay) {

        if (strDay == null || "".equals(strDay))
            return 0;

        int year = Integer.parseInt(strDay.substring(0, 4));
        int month = Integer.parseInt(strDay.substring(4, 6));
        int day = Integer.parseInt(strDay.substring(6, 8));

        if (month == 1 || month == 2)
            year--;
        month = (month + 9) % 12 + 1;
        int y = year % 100;
        int century = year / 100;
        int week = ((13 * month - 1) / 5 + day + y + y / 4 + century / 4 - 2 * century) % 7;
        if (week < 0)
            week = (week + 7) % 7;
         

        return week;
    }    
    
    /**
     * 1 ~ 4遺꾧린 援ы븯湲�
     * @param strDay
     * @return description
     * @since 1.00
     * @see
     */
    public static String getSettle(String strDay) {
        String strRetVal = "";
        if ("".equals(strDay))
            strDay = getCurrentDate();

        int iMonth = Integer.parseInt(strDay.substring(4, 6));

        if (iMonth <= 3) {
            strRetVal = "1";
        } else if (iMonth <= 6) {
            strRetVal = "2";
        } else if (iMonth <= 9) {
            strRetVal = "3";
        } else if (iMonth <= 12) {
            strRetVal = "4";
        }

        return strRetVal;
    }

    /**
     * Parameters : int convert, int size <br>
     * Return Value : String <br>
     * �궡�슜 : �닽�옄�쓽 �쇊履� �옄由щ�� '0'�쑝濡� 梨꾩슫�떎. <br>
     */
    public static String padLeftwithZero(int convert, int size) throws IOException {
        Integer inTemp = new Integer(convert);
        String stTemp = new String();
        String stReturn = new String();

        stTemp = inTemp.toString();
        if (stTemp.length() < size)
            for (int i = 0; i < size - stTemp.length(); i++)
                stReturn += "0";

        return (stReturn + stTemp);
    }

    /**
     * Parameters : int convert, int size, String padString <br>
     * Return Value : String <br>
     * �궡�슜 : �쐞 硫붿냼�뱶 �솗�옣 , padding string�쓣 吏��젙�븳�떎. �쇊履쎌쑝濡� �뙣�뵫 <br>
     */

    private static String padLeftwithString(int convert, int size, String padString) throws IOException {
        Integer inTemp = new Integer(convert);
        String stTemp = new String();
        String stReturn = new String();

        stTemp = inTemp.toString();
        if (stTemp.length() < size)
            for (int i = 0; i < size - stTemp.length(); i++)
                stReturn += padString;

        return (stReturn + stTemp);
    }

    /**
     * 臾몄옄�뿴�쓣 �씤肄붾뵫�븳�떎. <br>
     * 湲곗〈 �썙�겕�꽬�뿉�꽌 �궗�슜以묒씠硫� 二쇰�쇰벑濡앸쾲�샇�� 媛숈� �젙蹂대�� get 諛⑹떇�쑝濡� url �샇異쒖떆 �궗�슜�맂�떎.
     * @param strIn �씤肄붾뵫�븷 臾몄옄�뿴
     * @return String �씤肄붾뵫�맂 臾몄옄�뿴
     * @since 1.00
     * @see
     */
    public static String DSEncode(String strIn) {
        strIn = strIn + "PASSWD";
        String retStr = "";
        for (int i = 0; i < strIn.length(); i++) {
            retStr += (char) ((int) strIn.charAt(i) + (i % 2) + 1);
        }
        return retStr;
    }

    /**
     * 臾몄옄�뿴�쓣 �뵒肄붾뵫�븳�떎. <br>
     * 湲곗〈 �썙�겕�꽬�뿉�꽌 �궗�슜以묒씠硫� 二쇰�쇰벑濡앸쾲�샇�� 媛숈� �젙蹂대�� get 諛⑹떇�쑝濡� url �샇異쒖떆 �궗�슜�맂�떎.
     * @param strIn �뵒肄붾뵫�븷 臾몄옄�뿴
     * @return String �뵒肄붾뵫�맂 臾몄옄�뿴
     * @since 1.00
     * @see
     */
    public static String DSDecode(String strIn) {
        String retStr = "";
        for (int i = 0; i < (strIn.length()); i++) {
            retStr += (char) ((int) (strIn.charAt(i)) - (i % 2) - 1);
        }
        if (retStr.length() < 2 || !retStr.substring(retStr.length() - 6).equals("PASSWD")) {
            retStr = "";
        } else {
            retStr = retStr.substring(0, retStr.length() - 6);

        }
        return retStr;
    }

    public static String convertHtmlTags(Object obj) {
    	if ( obj == null)
    		return "";
    	
    	return convertHtmlTags(obj.toString());
    }
    
    public static String convertHtmlTags(String s) {
    	s = CommonUtil.recoveryLtGt(s);
    	
        s = s.replaceAll("<[^>]*>", ""); //�젙洹쒖떇 �깭洹몄궘�젣
        s = s.replaceAll("\r\n", " "); //�뿏�꽣�젣嫄�
        return s;
    } 
 
    
    /**
     * HTML BODY, TD�벑�뿉 �뀓�뒪�듃濡� 蹂댁뿬以� 臾몄옄 �깮�꽦 value媛믪쑝濡� �뱾�뼱媛� 寃쎌슦 value="str"�씠�윴 �삎�깭�씠�뼱�빞 �븳�떎. value='str', value=str �씠�윴嫄� �삁�쇅�긽�솴�씠 諛쒖깮�븷 �닔
     * �엳�떎. < : &lt; > : &gt; & : &amp; space : &nbsp; " : &quot;
     * @param pm_sSrc �닔�젙�쓣 �썝�븯�뒗 臾몄옄�뿴
     * @return �듅�닔臾몄옄媛� Replace�맂 臾몄옄�뿴
     */
    public static String convertSpChar1(String pm_sSrc) {

        if (pm_sSrc == null)
            return "";

        StringBuffer lm_sBuffer = new StringBuffer();
        char[] charArray = pm_sSrc.toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            if (charArray[i] == '<') {
                lm_sBuffer.append("&lt;");
            } else if (charArray[i] == '>') {
                lm_sBuffer.append("&gt;");
                // } else if(charArray[i] == '&') {
                // lm_sBuffer.append("&amp;");
                // } else if(charArray[i] == ' ') {
                // lm_sBuffer.append("&nbsp;");
            } else if (charArray[i] == '\"') {
                lm_sBuffer.append("&quot;");
            } else {
                lm_sBuffer.append(charArray[i]);
            }
        }

        return lm_sBuffer.toString();
    }

    /**
     * �옄諛붿뒪�겕由쏀듃�뿉 "", �삉�뒗 ''�븞�뿉 �뱾�뼱媛� 臾몄옄 �깮�꽦 " : \" ' : \' carrige return : \n \ : \\
     * @param pm_sSrc �닔�젙�쓣 �썝�븯�뒗 臾몄옄�뿴
     * @return �듅�닔臾몄옄媛� Replace�맂 臾몄옄�뿴
     */
    public static String convertSpChar2(String pm_sSrc) {

        if (pm_sSrc == null)
            return "";

        StringBuffer lm_sBuffer = new StringBuffer();
        char[] charArray = pm_sSrc.toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            if (charArray[i] == '\"') {
                lm_sBuffer.append("\\\"");
            } else if (charArray[i] == '\'') {
                lm_sBuffer.append("\\\'");
            } else if (charArray[i] == '\n') {
                lm_sBuffer.append("\\n");
            } else if (charArray[i] == '\\') {
                lm_sBuffer.append("\\\\");
            } else {
                lm_sBuffer.append(charArray[i]);
            }
        }

        return lm_sBuffer.toString();
    }

    /**
     * request 媛앹껜 �젙蹂대�� 湲곕컲�쑝濡� context Root �쟾源뚯��쓽 URL �젙蹂대�� 諛섑솚�븳�떎.
     * @param request HttpServletRequest 媛앹껜
     * @return URL �젙蹂� (�삁. http://www.abc.com:7001)
     * @since 1.00
     * @see
     */
    public static String getServletUrl(HttpServletRequest request) {
        String sServerName = request.getServerName();
        int iServerPort = request.getServerPort();
        String sScheme = request.getScheme();

        String sServerUrl = sScheme + "://" + sServerName + (iServerPort == 80 ? "" : ":" + iServerPort);
        return sServerUrl;
    }

    /**
     * �엯�젰�맂 String�쓣 Delimeter濡� �넗�겕�굹�씠吏� �븯�뿬 �넗�겕�굹�씠吏뺣맂 �넗�겙�뱾�쓣 String 諛곗뿴濡� 諛섑솚�븳�떎.
     * @param pm_sString �넗�겕�굹�씠吏뺣릺�뒗 臾몄옄�뿴
     * @param pm_sDelimeter 臾몄옄�뿴瑜� 遺꾨━�븯�뒗 delimeter 臾몄옄
     * @return �넗�겕�굹�씠吏뺣맂 �넗�겙�뱾�쓽 String 諛곗뿴
     * @see java.util.StringTokenizer
     */
    public static String[] getTokens(String pm_sString, String pm_sDelimeter) {
        if (pm_sString == null)
            return null;

        StringTokenizer lm_oTokenizer = new StringTokenizer(pm_sString, pm_sDelimeter);
        String[] lm_sReturns = new String[lm_oTokenizer.countTokens()];
        for (int i = 0; lm_oTokenizer.hasMoreTokens(); i++) {
            lm_sReturns[i] = lm_oTokenizer.nextToken();
        }// for

        return lm_sReturns;
    }

    /**
     * Position Data瑜� �뼸湲곗쐞�빐 �엯�젰�맂 String�쓣 Delimeter濡� �넗�겕�굹�씠吏� �븯�뿬 �넗�겕�굹�씠吏뺣맂 �넗�겙�뱾�쓣 int 諛곗뿴濡� 諛섑솚�븳�떎.
     * @param pm_sString �넗�겕�굹�씠吏뺣릺�뒗 臾몄옄�뿴
     * @param pm_sDelimeter 臾몄옄�뿴瑜� 遺꾨━�븯�뒗 delimeter 臾몄옄
     * @return �넗�겕�굹�씠吏뺣맂 Position Data�넗�겙�뱾�쓽 int 諛곗뿴
     * @see java.util.StringTokenizer
     */
    public static int[] getTokensPos(String pm_sString, String pm_sDelimeter) {
        if (pm_sString == null)
            return null;

        StringTokenizer lm_oTokenizer = new StringTokenizer(pm_sString, pm_sDelimeter);
        int[] lm_nReturns = new int[lm_oTokenizer.countTokens()];

        for (int i = 0; lm_oTokenizer.hasMoreTokens(); i++) {
            lm_nReturns[i] = Integer.parseInt(lm_oTokenizer.nextToken());
        }// for

        return lm_nReturns;
    }

    /**
     * Position Data瑜� �뼸湲곗쐞�빐 �엯�젰�맂 String�쓣 Delimeter濡� �넗�겕�굹�씠吏� �븯�뿬 �넗�겕�굹�씠吏뺣맂 �넗�겙�뱾�쓣 int 諛곗뿴濡� 諛섑솚�븳�떎.
     * @param pm_sString �넗�겕�굹�씠吏뺣릺�뒗 臾몄옄�뿴
     * @param pm_sDelimeter 臾몄옄�뿴瑜� 遺꾨━�븯�뒗 delimeter 臾몄옄
     * @return �넗�겕�굹�씠吏뺣맂 Position Data�넗�겙�뱾�쓽 int 媛��닔瑜� �븣�젮以��떎
     * @see java.util.StringTokenizer
     */
    public static int getDelimeterCount(String pm_sString, String pm_sDelimeter) {
        if (pm_sString == null)
            return 0;

        StringTokenizer lm_oTokenizer = new StringTokenizer(pm_sString, pm_sDelimeter);
        return lm_oTokenizer.countTokens();
    }

    /**
     * pm_sDelimeter�쓽 �븳 臾몄옄�뵫�쓣 �넗�겕�굹吏뺤쓽 �븯�뒗 寃껋씠 �븘�땲�씪 StringTokenizer瑜� �궗�슜�븯吏� �븡怨� pm_sDelimeter �쟾泥대�� �씠�슜�븯�뿬 �넗�겕�굹�씠吏뺥븳�떎.
     * @param pm_sString �넗�겕�굹�씠吏뺣릺�뒗 臾몄옄�뿴
     * @param pm_sDelimeter 臾몄옄�뿴瑜� 遺꾨━�븯�뒗 delimeter 臾몄옄�뿴(臾몄옄�뿴 �쟾泥� �씠�슜)
     * @return �넗�겕�굹�씠吏뺣맂 �넗�겙�뱾�쓽 String 諛곗뿴
     */
    public static String[] getTokensWithMultiDelim(String pm_sString, String pm_sDelimeter) {
        String lm_sString = pm_sString;

        int lm_iLength = pm_sDelimeter.length();
        int lm_iIndex = -1;
        Vector lm_vList = new Vector();
        while ((lm_iIndex = lm_sString.indexOf(pm_sDelimeter)) != -1) {
            String lm_sToken = lm_sString.substring(0, lm_iIndex);
            lm_sString = lm_sString.substring(lm_iIndex + lm_iLength);
            lm_vList.addElement(lm_sToken);
        }// while
        if (lm_sString != null)
            lm_vList.addElement(lm_sString);
        String[] lm_sTokens = new String[lm_vList.size()];
        lm_vList.copyInto(lm_sTokens);
        return lm_sTokens;
    }

    /**
     * URL怨� �뙆�씪誘명꽣�뱾�쓣 議고빀�븯�뿬 URL �쓣 諛섑솚�븳�떎. <br>
     * �궡遺��쟻�쑝濡� URLEncoder�쓽 encode() 泥섎━瑜� �닔�뻾�븯寃� �맂�떎.
     * @param sUrl 湲곗��씠 �릺�뒗 URL �젙蹂�
     * @param params parameter �젙蹂대�� key, value濡� 媛�吏�怨� �엳�뒗 Properties 媛앹껜
     * @return sUrl�뿉 �뙆�씪誘명꽣�뱾�씠 寃고빀�맂 URL 臾몄옄�뿴
     * @since 1.00
     * @see
     */
    public static String getUrlString(String sUrl, Properties params) {
        Iterator iterator = params.keySet().iterator();

        StringBuffer sb = new StringBuffer();
        sb.append(sUrl);
        if (sUrl.lastIndexOf("?") < 0)
            sb.append("?");
        else
            sb.append("&");
        for (int i = 0; iterator.hasNext(); i++) {
            String sKey = (String) iterator.next();
            String sValue = params.getProperty(sKey);
            if (i != 0)
                sb.append("&");
            try {
                sb.append(sKey + "=" + URLEncoder.encode(sValue, "8859_1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }// while
        return sb.toString();
    }

    /**
     * str 臾몄옄�뿴 以묒뿉�꽌 �삤瑜몄そ�뿉 delChar�� 媛숈� 臾몄옄瑜� �궘�젣�븯�뿬 諛섑솚�븳�떎.
     * @param str �엯�젰臾몄옄�뿴
     * @param delChar �궘�젣�븷 臾몄옄
     * @return �삤瑜몄そ遺�遺꾩씠 delChar媛� �궘�젣 泥섎━�맂 臾몄옄�뿴
     */
    public static String delRightChar(String str, char delChar) {
        String value = str;

        while (value.length() > 0) {
            int i = value.length() - 1;
            if (value.charAt(i) == delChar) {
                value = value.substring(0, i);
            } else
                break;
        }
        return value;
    }

    /**
     * �쟾�떖 諛쏆� Date 媛앹껜�뿉 �쟾�떖 諛쏆� �궇吏� �뙣�꽩 �삎�깭濡� �몴�쁽�맂 �궇吏� 臾몄옄�뿴�쓣 諛섑솚�븳�떎.
     * @param pm_oDate formatting ���긽�씠 �릺�뒗 Date 媛앹껜
     * @param pm_sDatePattern formatting�뿉 �궗�슜�븷 �궇吏� �뙣�꽩
     * @return formatting �궇吏� 臾몄옄�뿴
     */
    public static String getDateString(Date pm_oDate, String pm_sDatePattern) {
        SimpleDateFormat lm_oFormat = new SimpleDateFormat(pm_sDatePattern);
        return lm_oFormat.format(pm_oDate);
    }

    /**
     * �쟾�떖 諛쏆� Request 媛앹껜�뿉 �쟾�떖 諛쏆� strKey媛� null�씠硫� "" 臾몄옄�뿴�쓣 諛섑솚�븳�떎.
     * @param request ���긽�씠 �릺�뒗 HttpServletRequest 媛앹껜
     * @param strKey Parameter Key
     * @return formatting �궇吏� 臾몄옄�뿴
     */
    public static String getNullConv(String strTarget) {
        return getNullConv(strTarget, "");
    }

    /**
     * �쟾�떖 諛쏆� Request 媛앹껜�뿉 �쟾�떖 諛쏆� strKey媛� null�씠硫� "" 臾몄옄�뿴�쓣 諛섑솚�븳�떎.
     * @param request ���긽�씠 �릺�뒗 HttpServletRequest 媛앹껜
     * @param strKey Parameter Key
     * @return formatting �궇吏� 臾몄옄�뿴
     */
    public static String getNullConv(String strTarget, String strConv) {
        String szTemp;

        if (strConv == null)
            strConv = "";

        if (strTarget == null || "".equals(strTarget) || "null".equals(strTarget)) {
            szTemp = strConv;
        } else {
            szTemp = strTarget;
        }

        return szTemp;
    }

    /**
     * <pre>
     *  
     *   �뒪�듃留곷궡�뿉 �룷�븿�맂 紐⑤뱺 �듅�젙 String�쓣 �떎瑜� String�쑝濡� 移섑솚�븯�뒗 硫붿냼�뱶.
     *   
     * </pre>
     * 
     * @param str �쟾泥� String
     * @param index_str 移섑솚���긽�씠 �릺�뒗 �듅�젙 String
     * @param new_str �듅�젙 String�쓣 移섑솚�븷 �깉濡쒖슫 String
     * @return �듅�젙 String�씠 �떎瑜� String�쑝濡� 諛붾�뚯뼱吏� String
     */
    public static String getChangeString(String str, String index_str, String new_str) {
        String temp = "";
        if (str != null && str.indexOf(index_str) != -1) {
            while (str.indexOf(index_str) != -1) {
                temp = temp + str.substring(0, str.indexOf(index_str)) + new_str;
                str = str.substring(str.indexOf(index_str) + index_str.length());
            }
            temp = temp + str;
        } else {
            temp = str;
        }

        return temp;
    }

    /**
     * <pre>
     *  
     *   ���냼臾몄옄 援щ텇�뾾�씠 String 媛믪뿉�꽌 �떆�옉�쐞移섎�� 二쇨퀬 李얘퀬�옄�븯�뒗 媛믪씠 紐뉖쾲吏� �닚�꽌�뿉 �엳�뒗吏� 由ы꽩�븯�뒗 硫붿냼�뱶.
     *   
     * </pre>
     * 
     * @param str String 媛�
     * @param indexstr 李얘퀬�옄 �븯�뒗 媛�
     * @param fromindex String �쓽 �떆�옉�쐞移�
     * @see StringHandler#indexOfaA(String str, String indexstr)
     * @return 李얘퀬�옄 �븯�뒗 媛믪쓽 String �쓽 �쐞移�
     */
    public static int indexOfaA(String str, String indexstr, int fromindex) {
        int index = 0;
        indexstr = indexstr.toLowerCase();
        str = str.toLowerCase();
        index = str.indexOf(indexstr, fromindex);
        return index;
    }

    /**
     * <pre>
     *  
     *   CSV �뙆�씪�쓣 �벑濡앹떆 &quot; &tilde; &quot; �궗�씠�쓽 肄ㅻ쭏(,)瑜� 移섑솚�븯�뒗 硫붿냼�뱶.
     *   
     * </pre>
     * 
     * @param strBody �쟾泥� String
     * @return �듅�젙 String�씠 �떎瑜� String�쑝濡� 諛붾�뚯뼱吏� String
     */
    public static String getCSVChangeString(String strBody) {
        String temp = "";
        String sRetVal = "";
        String index_str = "\"";
        int iStartPos = 0;
        int iEndPos = 0;

        iStartPos = strBody.indexOf(index_str);

        if (iStartPos != -1) {
            sRetVal = strBody.substring(0, iStartPos);

            while (iStartPos != -1) {
                iEndPos = indexOfaA(strBody, index_str, iStartPos + 1);

                temp = strBody.substring(iStartPos + 1, iEndPos);
                sRetVal = sRetVal + getChangeString(temp, ",", "^COMMA^");
                strBody = strBody.substring(iEndPos + 1);
                iStartPos = strBody.indexOf(index_str);

                // �떎�쓬 index_str �궗�씠�쓽 臾몄옄�뿴�쓣 遺숈씤�떎.
                if (iStartPos > 0) {
                    sRetVal += strBody.substring(0, (iStartPos));
                }
            }

            sRetVal += strBody;
        } else {
            sRetVal = strBody;
        }

        return sRetVal.replaceAll("\"", "").trim();
    }

    /**
     * Map 珥덇린媛� 泥섎━. <br>
     * Map�쓽 Key�뿉 �빐�떦�븯�뒗 媛믪씠 議댁옱�븯吏� �븡�쑝硫� 珥덇린媛믪쓣 遺��뿬�븳�떎.
     * @param map
     * @param strKey
     * @param strVal
     * @return description
     * @since 1.00
     * @see
     */
    public static Map setNullVal(Map map, String strKey, String strVal) {
        String strMapVal = "";

        if (map.get(strKey) != null && map.get(strKey) != "")
            strMapVal = map.get(strKey).toString().trim();

        if ("".equals(strMapVal))
            map.put(strKey, strVal);

        return map;
    }

    public static String getMapVal(Map map, String strKey) {
        return getMapVal(map, strKey, "");
    }    
    
    public static String getMapVal(Map map, String strKey, String strVal) {
        String strMapVal = strVal;

        if (map == null || "".equals(strKey))
        	return strVal;
        
        if (map.get(strKey) != null && map.get(strKey) != "")
            strMapVal = map.get(strKey).toString().trim();
        	
        return strMapVal;
    }
    
    public static String setNullVal(String strKey, String strVal) {
        if (strKey == null || "".equals(strKey))
            return strVal;
        return strKey;
    }

    public static String setNullVal(Object objKey, String strVal) {
        if (objKey != null) {
            String strKey = objKey.toString().trim();
            if (!"".equals(strKey))
                return strKey;
        }
        return strVal;
    }

    public static String setNullVal(Object objKey) {
        return setNullVal(objKey, "");
    }

    public static String getConv(Object objKey, String strVal) {
        // UtilEncoder utilEncoder = new UtilEncoder();

        if (objKey != null && !objKey.equals("null")) {
            /*
             * String strKey = objKey.toString().trim(); strKey = utilEncoder.toKorean(strKey);
             */

            String strKey = objKey.toString().trim();

            if (!"".equals(strKey))
                return strKey;
        }
        return strVal;
    }

    public static String getConv(Object objKey) {

        return getConv(objKey, "");
    }

    public static String getConv2(Object objKey) {
        return getRealContent(getConv(objKey));
    }

    public static String getConv2(Object objKey, String s) {
        return getRealContent(getConv(objKey, s));
    }

  

    /**
     * URL 以묒뿉�꽌 �빐�떦 �븘�뱶 媛믪쓣 �궘�젣�븿 <br>
     * Method Description.
     * @param strParam URL �뙆�씪硫뷀꽣
     * @param strNotWord �궘�젣�븷 �븘�뱶
     * @return String
     * @since 1.00
     * @see
     */
    public static String removeParam(String strParam, String strNotWord) {
        String strRetVal = "";

        try {
            if (strParam == null || "".equals(strParam))
                return "";
            if (strNotWord == null || "".equals(strNotWord))
                return "";

        	int iStartPos = indexOfaA(strParam, strNotWord, 0);
        	
            if (iStartPos < 0)
                return strParam;
            
            strRetVal = strParam;
            
            while (iStartPos >= 0)
            {
	            int iEndPos = indexOfaA(strRetVal, "&", iStartPos);
	
	            if (iEndPos <= 0)
	                iEndPos = strRetVal.length() - 1;
	            
	            strRetVal = strRetVal.substring(0, iStartPos) + strRetVal.substring(iEndPos + 1);
	            
            	iStartPos = indexOfaA(strRetVal, strNotWord, 0);
            }
        } catch (Exception e) {
            System.out.println("CommonUtil[removeParam]=>" + e.toString());
        }
        return strRetVal;

    }

    /**
     * �엯�젰 臾몄옄�뿴 �걹遺�遺꾩쓽 怨듬갚臾몄옄 " "瑜� �젣嫄고븳�떎. Method Description.
     * @param String str 怨듬갚�쓣 �젣嫄고븷 臾몄옄�뿴
     * @return String
     * @since 1.00
     * @see
     */
    public static String rightTrim(String str) {
        if (str == null || str.equals(""))
            return str;

        int i;
        int len = str.length();

        for (i = 0; i < len; i++) {
            if (str.charAt(len - i - 1) != ' ')
                break;
        }

        return str.substring(0, len - i);
    }

    public static String getFileName(Object strFileName) {

        if (strFileName != null && !"".equals(strFileName)) {
            return getFileName(strFileName.toString());
        } else {
            return "";
        }
    }    
    
    public static String getFileName(String strFileName) {

        if (strFileName != null && !"".equals(strFileName)) {
            return strFileName.substring(strFileName.lastIndexOf("/") + 1, strFileName.length());
        } else {
            return "";
        }

    }

    public static String getFileExt(String strFileName) {

        if (strFileName != null && !"".equals(strFileName)) {
            return strFileName.substring(strFileName.lastIndexOf(".") + 1, strFileName.length()); // �뙆�씪�솗�옣鍮쇨린
        } else {
            return "";
        }

    }

    public static String number_format(Object objNumber) {
        return number_format(objNumber, "#,##0");
    }

    public static String number_format(Object objNumber, String strFormat) {
        String strRetVal = "0";
        try {
            if (objNumber == null || "".equals(objNumber.toString()))
                return "0";
            String strNumber = objNumber.toString();

            Double dblNumber = Double.valueOf(strNumber);

            strFormat = ("".equals(strFormat)) ? "#,##0" : strFormat;

            DecimalFormat formatter = new DecimalFormat(strFormat);
            strRetVal = formatter.format(dblNumber);
        } catch (Exception e) {
            System.out.println("CommonUtil [number_format] " + e.toString());
        }
        return strRetVal;
    }

    public static String number_format(int objNumber) {
        
        return number_format(objNumber, "#,##0");
    }    
    
    public static String number_format(long objNumber) {
        
        return number_format(objNumber, "#,##0");
    }
    
    public static String number_format(float objNumber) {
        
        return number_format(objNumber, "#,##0");
    }
    
    public static String number_format(int objNumber, String strFormat) {
        String strRetVal = "0";
        try {
            if (objNumber <= 0) return "0";
            String strNumber = Integer.toString(objNumber);

            Double dblNumber = Double.valueOf(strNumber);

            strFormat = ("".equals(strFormat)) ? "#,##0" : strFormat;

            DecimalFormat formatter = new DecimalFormat(strFormat);
            strRetVal = formatter.format(dblNumber);
        } catch (Exception e) {
            System.out.println("CommonUtil [number_format] " + e.toString());
        }
        return strRetVal;
    }    
    
    public static boolean isNewText(String strDate) {

        if (getDateFormat(strDate, "").length() < 8) {
            return false;
        }

        int intPeriod = 0;
        try {
            intPeriod = getPeriodByDay(strDate, getCurrentDate());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (intPeriod > 7) {
            return false;
        }

        return true;
    }

    public static String getConvertFile(HttpServletRequest servletRequest, Object objFileName, Object objFileNo)
    {
 	
    	String strVal = "";
        if (objFileName == null || "".equals(objFileName.toString()))
            return "";

        if (objFileNo == null || "".equals(objFileNo.toString()))
            return "";        
       
        String strFileExt = getFileExt(objFileName.toString().toLowerCase());
        boolean bFlag = false;
       
        try {
	        String arrFile[] = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "hwp", "pdf"};
	        
	        for(int nLoop=0; nLoop < arrFile.length; nLoop++)
	        {
	            if ( arrFile[nLoop].equals(strFileExt)) {
	            	bFlag = true;
	            	break;
	            }	
	        }
	
	        if ( !bFlag )
	            return "";
          	
	        String strFileNo = objFileNo.toString();
	        
	        strVal  = "<a href='javascript:fConvertHtml(" + strFileNo + ")'>";
	        strVal += "<img src='/images/common/btn_html.gif'/>";  
	        strVal += "</a>";  
/*	   
	       	UploadUtil upload = new UploadUtil(servletRequest);
	      	String strFileNo   = objFileNo.toString();
	      	
	      	String strHtmlFile = upload.convertHtmlFile(strFileNo, objFileName.toString());
	        if ("".equals(strHtmlFile))
	        	return "";
	        
	        strVal  = "<a href='" + strHtmlFile + "' target='_blank'>";
	        strVal += "<img src='/images/common/btn_html.gif'/>";  
	        strVal += "</a>";
*/
        } catch ( Exception e) {
        	System.out.println(e.toString());
        }
        return strVal;
    }
    
    public static String getIconFileExt(Object objFileName) {
        if (objFileName == null || "".equals(objFileName.toString()))
            return "";

        String strFileExt = getFileExt(objFileName.toString().toLowerCase()).trim();
        String strIconImg = "/images/common/ic_txt.gif";

		if ("bmp".equals(strFileExt)) {
			strIconImg = "/images/common/ic_img.gif";
		} else if ("jpg".equals(strFileExt)) {
            strIconImg = "/images/common/ic_img.gif";
		} else if ("gif".equals(strFileExt)) {
            strIconImg = "/images/common/ic_img.gif";
        } else if ("doc".equals(strFileExt)) {
            strIconImg = "/images/common/ic_doc.gif";
        } else if ("docx".equals(strFileExt)) {
            strIconImg = "/images/common/ic_doc.gif";  
        } else if ("ppt".equals(strFileExt)) {
            strIconImg = "/images/common/ic_ppt.gif";
        } else if ("pptx".equals(strFileExt)) {
            strIconImg = "/images/common/ic_ppt.gif";
        } else if ("xls".equals(strFileExt)) {
            strIconImg = "/images/common/ic_ex.gif";
        } else if ("xlsx".equals(strFileExt)) {
            strIconImg = "/images/common/ic_ex.gif";            
        } else if ("zip".equals(strFileExt)) {
            strIconImg = "/images/common/ic_zip.gif";
        } else if ("pdf".equals(strFileExt)) {
            strIconImg = "/images/common/ic_pdf.gif";
        } else if ("wpad".equals(strFileExt)) {
            strIconImg = "/images/common/ic_wpad.gif";
        } else if ("txt".equals(strFileExt)) {
            strIconImg = "/images/common/ic_txt.gif";
        } else if ("exe".equals(strFileExt)) {
            strIconImg = "/images/common/ic_exe.gif";
        } else if ("hwp".equals(strFileExt)) {
            strIconImg = "/images/common/ic_hwp.gif";
        } else if ("reg".equals(strFileExt)) {
            strIconImg = "/images/common/ic_reg.gif";
        }

        return "<img src='" + strIconImg + "' alt='泥⑤��뙆�씪 �떎�슫濡쒕뱶' align='absmiddle' border='0'/>";
    }

    
    public static boolean isFileList(List lstFile) 
    {
    	boolean bFlag = false;
    	
    	if ( lstFile == null && lstFile.isEmpty()) 
             return false;
    	   
    	   
    	for (int nLoop =0; nLoop < lstFile.size(); nLoop++)
    	{
    	     	Map dbFile = (Map)lstFile.get(nLoop);
    	     	String strFileName = CommonUtil.nvl(dbFile.get("FILE_REALNM"));
    	     	   
    	     	if ( !CommonUtil.isImageFile(strFileName) ) {
    	     		 return true;           	      
    	        }
    	}
    	
    	return bFlag;
    }
    
 
    
    
    public static boolean isImageFile(Object objFileName) {
        if (objFileName == null || "".equals(objFileName.toString()))
            return false;

        String strFileExt = getFileExt(objFileName.toString().toLowerCase());
        boolean bImage = false;

		if ("bmp".equals(strFileExt)) {
			bImage = true;
		} else if ("jpg".equals(strFileExt)) {
			bImage = true;
		} else if ("gif".equals(strFileExt)) {
			bImage = true;
		} else if ("png".equals(strFileExt)) {
			bImage = true;
        } else if ("jpeg".equals(strFileExt)) {
			bImage = true;
        }

        return bImage;
    }
  

    public static String getTimeFormat(Object objTime, String strFormat) {

        String strTime = "";
        strFormat = strFormat.toUpperCase();
        String strRetTime = "";
        String strAmPm = "";
        int iHour = 0;
        try {
            if (objTime == null || "".equals(objTime.toString()))
                return "";

            strTime = objTime.toString();
            if ("".equals(strFormat)) {
                strRetTime = strTime.substring(0, 2) + ":" + strTime.substring(2, 4);
            } else if ("HH24:MM".equals(strFormat)) {
                strRetTime = strTime.substring(0, 2) + ":" + strTime.substring(2, 4);
            } else if ("AM HH:MM".equals(strFormat)) {
                iHour = Integer.parseInt(strTime.substring(0, 2));
                strAmPm = (iHour <= 12) ? "�삤�쟾 " : "�삤�썑";
                strRetTime = Integer.toString((iHour > 12) ? iHour - 12 : iHour);
                if (strRetTime.length() == 1)
                    strRetTime = "0" + strRetTime;
                strRetTime = strAmPm + " " + strRetTime + ":" + strTime.substring(2, 4);
            }

        } catch (Exception e) {
            System.out.println("Error CommonUtil getTimeFormat() " + e.toString());
        }

        return strRetTime;
    }

    public static String getCommonCodeLabel(List resultMapList, String searchCode, String codeFiled, String labelFiled) {
        if (resultMapList != null) {
            Iterator iterator = resultMapList.iterator();

            while (iterator.hasNext()) {
                Map resultMap = (Map) iterator.next();
                String strCode = CommonUtil.getConv(resultMap.get(codeFiled));

                if (strCode.equals(searchCode)) {
                    return CommonUtil.getConv(resultMap.get(labelFiled));
                }
            }
        }
        return "";
    }

    public static String getRight(String str, int len) {
        if (str.length() < len)
            return "";

        return str.substring(str.length() - len);
    }

    public static String getFileSize(Object obj)
    {
    	String strVal = "";
    	if ( obj == null )
    		return "";
    	
    	Long nSize = Long.parseLong(obj.toString());
    	
    	nSize /= 1000; // Byte瑜� KB濡� �솚�궛
    	
    	if (nSize < 1)
    		nSize = 1L;
    	
    	if ( nSize < 1000 ) {
    		strVal = String.format("%dKB", nSize);
    	} else {
    		strVal = String.format("%.2fMB", nSize/1000.0);
    	}
    		
    	return strVal;
    	
    }
    
    /**
     * double�쓣 �썝�븯�뒗 �삎�깭�쓽 �룷留룹쑝濡� 蹂��솚(String)�쑝濡� 諛섑솚 i == 1 : �냼�닽�젏�씠�븯 �븳�옄由� i == 2 : 而대쭏濡� 援щ텇�맂 �닽�옄 i == 3 : 遺숈뼱�엳�뒗�닽�옄
     * @param d
     * @param i
     * @return
     */
    public static String doubleToStr(double d, int i) {
        DecimalFormat decimalformat = null;
        String s = "";
        if (i == 1) {
            decimalformat = new DecimalFormat("######0.#");
        } else if (i == 2) {
            decimalformat = new DecimalFormat("#,###,##0");
        } else if (i == 3) {
            decimalformat = new DecimalFormat("######0");
        } else {
            decimalformat = new DecimalFormat("#,###,##0.#####");
        }

        s = decimalformat.format(d);
        return s;
    }

    /**
     * double�쓣 �냼�닔�젏�씠�븯 �옄由ъ닔瑜� �썝�븯�뒗 �삎�깭濡� 蹂��솚(String)�쑝濡� 諛섑솚
     * @param d
     * @param i - �냼�닽�젏�씠�븯 �옄由ъ닔
     * @return
     */
    public static String doubleStr(double d, int i) {
        DecimalFormat decimalformat = null;
        String szForm = "######0";
        String s = "";

        if (i > 0) {
            for (int z = 0; z < i; z++) {
                if (z == 0) {
                    szForm += ".0";
                } else {
                    szForm += "0";
                }
            }
        }

        decimalformat = new DecimalFormat(szForm);

        s = decimalformat.format(d);
        return s;
    }

   

   
    /**
     * 荑좏궎瑜� �꽕�젙�븳�떎. <br>
     * Method Description.
     * @param response
     * @param name 荑좉린紐�
     * @param value 媛�
     * @param iMinute �꽕�젙�떆媛� (遺�)
     * @throws java.io.UnsupportedEncodingException description
     * @since 1.00
     * @see
     */
    public static void setCookieObject(HttpServletResponse response, String name, String value, int iMinute)
            throws java.io.UnsupportedEncodingException {

        Cookie cookie = new Cookie(name, URLEncoder.encode(value, "euc-kr"));

        cookie.setMaxAge(60 * iMinute);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookieObject(HttpServletRequest request, String cookieName)
            throws UnsupportedEncodingException {

        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            return null;
        String value = null;
        for (int i = 0; i < cookies.length; i++) {

            if (cookieName.equals(cookies[i].getName())) {
                value = cookies[i].getValue();
                if ("".equals(value))
                    value = null;
                break;
            }
        }

        return (value == null ? null : URLDecoder.decode(value, "euc-kr"));
    }

    /**
     * <pre>
     *  Html �솕硫� 援ъ꽦�뿉 留먯쨪�엫 �몴�떆 湲곕뒫.
     * </pre>
     * 
     * @param inputString 蹂��솚�븷 String 媛�
     * @param max_Length 諛섑솚�븷 String Length
     * @return "aaaaaaaaa" 瑜� "aaaa..."�쑝濡� 蹂��솚�븯�뿬 Return
     */
    public static String getStrCut(String inputString, int max_Length) {

        String outputString = "";
        int string_size = 0;
        int new_size = 0;

        try {
            for (int i = 0; i < max_Length && i < inputString.length(); i++) {
                if (Character.getType(inputString.charAt(i)) == 5) {
                    string_size += 2;
                } else if (Character.getType(inputString.charAt(i)) == 1
                        || Character.getType(inputString.charAt(i)) == 2
                        || Character.getType(inputString.charAt(i)) == 15
                        || Character.getType(inputString.charAt(i)) == 24) {
                    string_size += 1;
                } else {
                    string_size += 1;
                }
            }

            if (inputString == null)
                return outputString;

            //if (max_Length < 4 || inputString.length() < max_Length)
            if (max_Length < 4 || string_size < max_Length)
                return inputString;

            for (int i = 0; new_size < max_Length - 3; i++) {
                if (Character.getType(inputString.charAt(i)) == 5) {
                    new_size += 2;
                } else if (Character.getType(inputString.charAt(i)) == 1
                        || Character.getType(inputString.charAt(i)) == 2
                        || Character.getType(inputString.charAt(i)) == 15
                        || Character.getType(inputString.charAt(i)) == 24) {
                    new_size += 1;
                } else {
                    new_size += 1;
                }

                if (new_size <= max_Length - 3) {
                    outputString += new Character(inputString.charAt(i)).toString();
                }
            }
         
            outputString += "...";
            return outputString;
        } catch (Exception E) {
            E.printStackTrace();
            return  inputString;
        }
    }
    
    
    public static String[] getArrayTime(String strTm) {
        String[] retTm = { "", "", "" };

        if (strTm.length() == 4) {
            String strHour = strTm.substring(0, 2);

            int iHour = Integer.parseInt(strHour);

            if (iHour > 12) {
                retTm[0] = "12";
                iHour -= 12;
            } else if (iHour == 0) {
                retTm[0] = "12";
                iHour = 12;
            } else {
                retTm[0] = "0";
            }

            retTm[1] = getRight("0" + iHour, 2);
            retTm[2] = strTm.substring(2);
        }

        return retTm;
    }

    public static String getListValueComma(List rsList, String strField) {
        String strRetVal = "";
        try {
            if (rsList != null && rsList.size() > 0) {
                for (int iLoop = 0; iLoop < rsList.size(); iLoop++) {
                    Map dbRow = (Map) rsList.get(iLoop);
                    strRetVal += ("".equals(strRetVal)) ? CommonUtil.getConv(dbRow.get(strField)) : ","
                            + CommonUtil.getConv(dbRow.get(strField));
                }
            }
        } catch (Exception e) {
            System.out.println("Error ==> getListValueComma() " + e.toString());
        }
        return strRetVal;
    }

    public static float[] getListToIntArrayFloat(List rsList, String strField) {

        return getListToIntArrayFloat(rsList, strField, 0, 0);
    }

    public static float[] getListToIntArrayFloat(List rsList, String strField, int iStartPos, int iEndPos) {
        if (rsList == null)
            return null;

        if (iEndPos > rsList.size())
            iEndPos = rsList.size();
        if (iEndPos <= 0)
            iEndPos = rsList.size();

        float[] rsArr = new float[iEndPos - iStartPos];

        int iCnt = 0;
        try {
            for (int iLoop = 0; iLoop < iEndPos; iLoop++) {

                if (iLoop < iStartPos)
                    continue;

                Map dbRow = (Map) rsList.get(iLoop);
                rsArr[iCnt++] = Float.parseFloat(CommonUtil.getConv(dbRow.get(strField), "0.0"));
            }
        } catch (Exception e) {
            System.out.println("Error ==> getListToArray() " + e.toString());
        }
        return rsArr;
    }

    public static int[] getListToIntArray(List rsList, String strField) {

        return getListToIntArray(rsList, strField, 0, 0);
    }

    public static int[] getListToIntArray(List rsList, String strField, int iStartPos, int iEndPos) {
        if (rsList == null)
            return null;

        if (iEndPos > rsList.size())
            iEndPos = rsList.size();
        if (iEndPos <= 0)
            iEndPos = rsList.size();

        int[] rsArr = new int[iEndPos - iStartPos];

        int iCnt = 0;
        try {
            for (int iLoop = 0; iLoop < iEndPos; iLoop++) {

                if (iLoop < iStartPos)
                    continue;

                Map dbRow = (Map) rsList.get(iLoop);
                rsArr[iCnt++] = Integer.parseInt(CommonUtil.getConv(dbRow.get(strField), "0"));
            }
        } catch (Exception e) {
            System.out.println("Error ==> getListToArray() " + e.toString());
        }
        return rsArr;
    }

    public static String[] getListToArray(List rsList, String strField) {

        return getListToArray(rsList, strField, 0, 0);
    }

    public static String[] getListToArray(List rsList, String strField, int iStartPos, int iEndPos) {
        if (rsList == null)
            return null;

        if (iEndPos > rsList.size())
            iEndPos = rsList.size();
        if (iEndPos <= 0)
            iEndPos = rsList.size();

        String[] rsArr = new String[iEndPos - iStartPos];

        int iCnt = 0;
        try {
            for (int iLoop = 0; iLoop < iEndPos; iLoop++) {

                if (iLoop < iStartPos)
                    continue;

                Map dbRow = (Map) rsList.get(iLoop);
                rsArr[iCnt++] = CommonUtil.getConv(dbRow.get(strField));
            }
        } catch (Exception e) {
            System.out.println("Error ==> getListToArray() " + e.toString());
        }
        return rsArr;
    }

    public static String getArrayValueComma(String[] rsArr) {
        return getArrayValueComma(rsArr, 0, 0);
    }

    public static String getArrayValueComma(String[] rsArr, int iStartPos, int iMax) {
        StringBuffer sb = new StringBuffer();

        try {
            if (rsArr == null)
                return "";
            if (iMax == 0)
                iMax = rsArr.length;
            for (int iLoop = iStartPos; iLoop < rsArr.length; iLoop++) {
                sb.append((sb.length() == 0) ? String.valueOf(rsArr[iLoop]) : "," + String.valueOf(rsArr[iLoop]));
            }
        } catch (Exception e) {
            System.out.println("Error ==> getArrayValueComma() " + e.toString());
        }
        return sb.toString();
    }

    public static String getArrayValueComma(int[] rsArr) {
        StringBuffer sb = new StringBuffer();

        try {
            if (rsArr == null)
                return "";
            for (int iLoop = 0; iLoop < rsArr.length; iLoop++) {
                sb.append((iLoop == 0) ? String.valueOf(rsArr[iLoop]) : "," + String.valueOf(rsArr[iLoop]));
            }
        } catch (Exception e) {
            System.out.println("Error ==> getArrayValueComma() " + e.toString());
        }
        return sb.toString();
    }
 
    public static String getRealContent(String source) {
        int start = source.indexOf("<DIV");
        if (start < 0)
            return source;

        int realStart = source.substring(start, source.length()).indexOf(">") + start + 1;
        int end = source.indexOf("</DIV");
        return source.substring(realStart, end);
    }

    public static String getTitleLimit(String title, int maxNum, int re_level) {
        int blankLen = 0;
        if (re_level != 0) {
            blankLen = (re_level + 1) * 2;
        }
        int tLen = title.length();
        int count = 0;
        char c;
        int s = 0;

        for (s = 0; s < tLen; s++) {
            c = title.charAt(s);
            if ((int) (count) > (int) (maxNum - blankLen)) {
                break;
            }

            if (c > 127)
                count += 2;
            else
                count++;
        }
        return (tLen > s) ? title.substring(0, s) + "..." : title;
    }

    
    /**
     * URL 以묒뿉�꽌 �빐�떦 �븘�뱶 媛믪쓣 �궘�젣�븿 <br>
     * Method Description.
     * @param strParam URL �뙆�씪硫뷀꽣
     * @param strNotWord �궘�젣�븷 �븘�뱶
     * @return String
     * @since 1.00
     * @see
     */
    public static String changeHtmlTag(String strParam) {
        String strSTag = "<table";
        String strETag = "</table>";
        String strTag  = "";
        
        //int iEndPos    = 0;
        int iStartPos  = 0;
        StringBuffer sb = new StringBuffer();
        
        try {
            if (strParam == null || "".equals(strParam))
                return "";
            
        	iStartPos = indexOfaA(strParam.toLowerCase(), strSTag, 0);
        	
            if (iStartPos < 0)
                return changeBrTag(strParam);
           
            sb.append(changeBrTag(strParam.substring(0, iStartPos)));
            strParam = strParam.substring(iStartPos);
            	
                
            iStartPos = strParam.lastIndexOf(strETag);
            if (iStartPos < 0) {
            	sb.append(changeBrTag(strParam));
            } else {
                sb.append(changeBrTag(strParam.substring(0, iStartPos + strETag.length())));
                strParam = strParam.substring(iStartPos + strETag.length());                
            } 
             
            sb.append(changeBrTag(strParam));
            
        } catch (Exception e) {
            System.out.println("CommonUtil[removeParam]=>" + e.toString());
        }
        return sb.toString();

    }    
    
    public static String changeBrTag(Object objKey) 
    {
        String strKey = getConv(objKey, "&nbsp;");
        
        strKey = recoveryLtGt(strKey);
        
        String strComp = strKey.toLowerCase();
        
         if ( strComp.indexOf("<br")     >= 0 || 
        	  strComp.indexOf("<li")     >= 0 || 
        	  strComp.indexOf("<table")  >= 0 ||
        	  strComp.indexOf("<tr")     >= 0 ||
        	  strComp.indexOf("<td")     >= 0 ||
        	  strComp.indexOf("<div")    >= 0 || 
        	  strComp.indexOf("<p")      >= 0 ) {
        } else {
        	strKey = strKey.replace("\r\n", "<br/>");
        	strKey = strKey.replace("\n", "<br/>");
        }
         
         return strKey;
    }
    
    public static String getReplaceToHtml(Object objKey) {
        String strKey = changeHtmlTag(getConv(objKey, "&nbsp;"));
        strKey = strReplace(strKey, "\\", "");        
        return strKey;
    }

    public static String strReplace(String stro, String s1, String s2) {
        int i = 0;
        String rStr = "";
        while(stro.indexOf(s1) > -1){
                i = stro.indexOf(s1);
                rStr += stro.substring(0,i)+s2;
                stro = stro.substring(i+s1.length());
        }
        return rStr+stro;
  }

    
    
    public static int getRandomInt(int limit) {
        int number = random.nextInt();
        number = (number >>> 16) & 0xffff;
        number /= (0xffff / limit);
        return number;
    }

   
  
    public static String getNow() {
        String now = "";
        String nows[] = new String[3];
        int date[] = new int[3];
        Calendar cal = Calendar.getInstance();

        date[0] = cal.get(cal.MONTH) + 1;
        date[1] = cal.get(cal.DATE);
        date[2] = cal.get(cal.HOUR_OF_DAY);

        for (int i = 0; i < date.length; i++) {
            if (date[i] < 10) {
                nows[i] = "0" + new Integer(date[i]).toString();
            } else {
                nows[i] = new Integer(date[i]).toString();
            }
            now = now + nows[i];
        }
        return String.valueOf(cal.get(cal.YEAR)) + now;
    }

    public static int getLastDay(String days) {
        int ls = 0;
        int year = Integer.parseInt(days.substring(0, 4));
        int mon = Integer.parseInt(days.substring(4, 6));
        switch (mon) {
        case 2:
            ls = yoonMon(year);
            break;
        case 4:
        case 6:
        case 9:
        case 11:
            ls = 30;
            break;
        default:
            ls = 31;
        }
        return ls;
    }

    public static int yoonMon(int year) {
        int yn = 0;
        int fyear = (int) (year / 100);
        int byear = year % 100;
        if (fyear % 4 == 0 && year % 4 == 0) {
            yn = 29;
        } else {
            if (byear != 0 && byear % 4 == 0) {
                yn = 29;
            } else {
                yn = 28;
            }
        }
        return yn;
    }
   
    
    /**<pre>
     * �뀈�썡�씪 �궗�씠�뿉 援щ텇�옄 sep瑜� 泥④��븳�떎. 援щ텇�옄媛� "/"�씤 寃쎌슦 "yyyymmdd" -> "yyyy/mm/dd"媛� �맂�떎.
     *
     * @return java.lang.String
     * @param str �궇吏�(yyyymmdd)
     */
    public static String date(String str, String sep) {
      String temp = null;
      if (str == null)
        return "";
      int len = str.length();

      if (len != 8)
        return str;
      if ( (str.equals("00000000")) || (str.equals("       0")) || (str.equals("        ")))
        return "";
      temp = str.substring(0, 4) + sep + str.substring(4, 6) + sep + str.substring(6, 8);

      return temp;
    }
    
    public static long toLong(Float fNum) {
        String strVal;
        
        strVal = String.valueOf(fNum);
     
        if ( strVal.indexOf('.') > 0 ) {
          
            strVal =  strVal.substring(0, strVal.indexOf('.'));
        }    
       
        return Long.valueOf(strVal);
        
    }
    
    public static long toLong(float fNum){
        return toLong( (Float) fNum );
    }
    
    public static String getSelBoxRepeat(int nStart, int nEnd, int nComp, int nSize)
    {
        return getSelBoxRepeat(nStart, nEnd, String.valueOf(nComp), nSize);
    }
    
    public static String getSelBoxRepeat(int nStart, int nEnd, String strComp, int nSize)
    {
        return getSelBoxRepeat(nStart, nEnd, strComp, nSize);
    }
    
    public static String getSelBoxRepeat(int nStart, int nEnd, Object objComp, int nSize)
    {
        StringBuffer strBuf = new StringBuffer();  
        String strValue = new String();
        String strComp  = "";
        
        if ( objComp != null)
            strComp = objComp.toString(); 
     
        String strFormat = "%0" + String.valueOf(nSize) + "d";
 
        for (int nLoop = nStart; nLoop <= nEnd; nLoop ++ )
        {
           strBuf.append("<option value='"); 
           if ( nSize > 0 ) 
               strValue = String.format(strFormat, nLoop);
           else 
               strValue = String.valueOf(nLoop);
           strBuf.append(strValue + "' " );
           if ( String.valueOf(nLoop).equals(strComp))
               strBuf.append(" selected " );
           strBuf.append(" >"  + String.valueOf(nLoop) + " </option> " + "\n");               
        }
        
        
        return strBuf.toString();
    }
    

 
    /**
     * Method Summary. <br>
     * New �씠誘몄�瑜� �몴�떆
     * @param imgPath �씠誘몄� 寃쎈줈
     * @return imgSize �씠誘몄� �궗�씠利� 諛곗뿴
     * @throws Exception ex
     * @since 1.00
     * @see
     */    
    
    public static String getNewImage(Object objDt)
    {
        String strBaseDt;
        String strCurDt = addDay(getCurrentDate(), -2);
        
        if ( objDt == null) return "";
        
        strBaseDt = objDt.toString();
        if ( "".equals(strBaseDt)) return "";
        
        if ( strBaseDt.length() > 8) 
        	strBaseDt = strBaseDt.replace("-", "").substring(0, 8);
        
        if ( Integer.parseInt(strBaseDt) >= Integer.parseInt(strCurDt) )
            return  "<img src='/img/sub/community/list_new.jpg' alt='new icon' title='new icon'>";
        
        return "";
    }
 
    /**
     * Method Summary. <br>
     * File Text Read
     * @param imgPath �씠誘몄� 寃쎈줈
     * @return imgSize �씠誘몄� �궗�씠利� 諛곗뿴
     * @throws Exception ex
     * @since 1.00
     * @see
     */    
        
    public static String getFileRead(String strFile)
    {
    	String strContext="";  
    	
        try {            
        	  Reader reader = new InputStreamReader(  new FileInputStream(strFile),"UTF-8");            
        	  BufferedReader fin = new BufferedReader(reader);            
            
        	  String fileContent;            
        	  while ((fileContent = fin.readLine())!=null) {                
        		  strContext +=  fileContent + "\n";    
              }                        
        	  //Remember to call close.             
        	  //calling close on a BufferedReader/BufferedWriter             
        	  // will automatically call close on its underlying stream             
        	  fin.close();                    
        	  } catch (IOException e) 
        	  {            e.printStackTrace();        }
 
   	   return strContext;
    }    
    
 
    
    public static String getInStr(List lstGroupMin, String strField)
    {
    	String strVal = "";
    	
    	for(int nLoop=0; nLoop < lstGroupMin.size(); nLoop++)
    	{
    		Map  dbRow = (Map) lstGroupMin.get(nLoop);
    		
    		String strMap  = CommonUtil.getMapVal(dbRow, strField);
    		
    		if (!"".equals(strMap))
    		{
	    		if (!"".equals(strVal)) strVal += ",";
	    		strVal += CommonUtil.getMapVal(dbRow, strField);
    		}
    	}
    	
    	return strVal;
    }    
    
    /**
     * Method Summary. <br>
     * 二쇰�쇰벑濡앸쾲�샇 �젏寃�
     * @param 二쇰�쇰벑濡앸쾲�샇
     * @return 二쇰�쇰벑濡앸쾲�샇 �뿬遺�
     * @throws Exception ex
     * @since 1.00
     * @see
     */        
    public static boolean isJumin( String jumin ) { 
    	 boolean isKorean = true; 
    	 int check = 0;
    	 
    	 if( jumin == null || jumin.length() != 13 ) return false; 
    	 if( Character.getNumericValue( jumin.charAt( 6 ) ) > 4 && Character.getNumericValue( jumin.charAt( 6 ) ) < 9 ) { 
    	  isKorean = false; 
    	 } 

    	 for( int i = 0 ; i < 12 ; i++ ) { 
    	  if( isKorean ) check += ( ( i % 8 + 2 ) * Character.getNumericValue( jumin.charAt( i ) ) ); 
    	   else check += ( ( 9 - i % 8 ) * Character.getNumericValue( jumin.charAt( i ) ) ); 
    	 } 

    	 if( isKorean ) { 
    	  check = 11 - ( check % 11 ); 
    	  check %= 10; 
    	 }else { 
    	 int remainder = check % 11; 
    	  if ( remainder == 0 ) check = 1; 
    	  else if ( remainder==10 ) check = 0; 
    	  else check = remainder; 

    	  int check2 = check + 2; 
    	  if ( check2 > 9 ) check = check2 - 10; 
    	  else check = check2; 
    	 } 

    	 if( check == Character.getNumericValue( jumin.charAt( 12 ) ) ) return true; 
    	 else return false; 

    } 
    
    
    
    public static String scriptMsg(String strMsg)
    {
    	return scriptMsg(strMsg, "");
    }
    
    public static String scriptMsg(String strMsg, String strUrl)
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append("<script>");
    	sb.append("alert('" + strMsg + "')");
    	
    	sb.append("</script>");

    	if ( !"".equals(strUrl))
    		sb.append("<meta http-equiv='refresh' content='0;url=" + strUrl + "'>");    	
    	
    	return sb.toString();
    }
    
    public static String getOneFileName(List fileList, String strCompareGbn)
    {
    	 
        int nCnt = 0;
        String strFileName = "";
        
        if(fileList != null && fileList.size() > 0){

           for( int iLoop = 0; iLoop < fileList.size(); iLoop++ ) {
               Map fileMap = ( Map ) fileList.get( iLoop );
               
               String strFileGbn = getNullTrans(fileMap.get("FILE_GBN"),"");
 
               if (strCompareGbn.equals(strFileGbn) ||  "".equals(strCompareGbn) ) 
               {
            	   strFileName = getNullTrans(fileMap.get("FILE_NM"));
            	   break;
               }
           }
        }   
        
        return strFileName;
    }
    
    public static List getFileGbnList(List fileList)
    {
    	return getFileGbnList(fileList, "");
    }

    public static List getFileGbnList(List fileList, String strCompareGbn)
    {
    	 
        List valLst = new ArrayList<Map>();
 
        if(fileList != null && fileList.size() > 0){

           for( int iLoop = 0; iLoop < fileList.size(); iLoop++ ) {
               Map fileMap = ( Map ) fileList.get( iLoop );
               
               String strFileGbn = getNullTrans(fileMap.get("FILE_GBN"),"");
               if (strCompareGbn.equals(strFileGbn) ||  "".equals(strCompareGbn) ) 
               {
            	   Map rowMap  = new HashMap<String, String>();
            	   
            	   Iterator iter = fileMap.keySet().iterator();
            	   
            	   while( iter.hasNext()) {
	            	    String key    = (String)iter.next();
	            	    String value  = (String)fileMap.get( key );
	            	  
	            	    System.out.println( key +": " + value );
	            	    rowMap.put(key, value);
	            	    valLst.add(rowMap);
            	   }
              }
           }   
        
        }    
        
        return valLst;
    }
    
  //MD5�븫�샇�솕
	 public static String getEncryptString(String str) {
		 java.security.MessageDigest md5 = null;
	        try {
	            md5 = java.security.MessageDigest.getInstance("MD5");
	        } catch (Exception e) {
	            return "";
	        }

	        String eip;
	        byte[] bip;
	        String temp = "";
	        String tst = str;

	        bip = md5.digest(tst.getBytes());
	        for (int i = 0; i < bip.length; i++) {
	            eip = "" + Integer.toHexString((int) bip[i] & 0x000000ff);
	            if (eip.length() < 2)
	                eip = "0" + eip;
	            temp = temp + eip;
	        }
	        return temp;
	
	    }
	   
	 public static String getImageResizeStr(HttpServletRequest req, String strImg, int nFixWidth) {
		 
		 return getImageResizeStr(req, strImg, nFixWidth, 9000);
	 }
	 
	 
	 public static String getImageResizeStr(HttpServletRequest req, String strImg, int nFixWidth, int nFixHeight)
	 {	 
		 String strImgWH = ""; 
		 try {
		     int nArrSize[] = getImageResize(req, strImg, nFixWidth, nFixHeight);
			 if ( nArrSize[0] > 0 && nArrSize[1] > 0)
			 {
				strImgWH = " width = '" + nArrSize[0] + "' height='" + nArrSize[1] + "'";
			 }
		 } catch ( Exception e) {}
		 
		 return strImgWH;
	 }
	 
	 public static int[] getImageResize(HttpServletRequest req, String strImg, int nFixWidth, int nFixHeight)
	 {
		 
		 int nReWidth = 0;
		 int nReHeight = 0;
		 int nRate     = 0;
		 int nArrSize[] = {0,0};
	     try {
 
	    	 if ( strImg == null || "".equals(strImg))
	    		 return nArrSize;
	    	 
	    	 String strUrl = getServletUrl(req)   + strImg;
	    	 
	        // URL url = new URL(strUrl);
	        // Image image = ImageIO.read(url);
 
	    	   // Read from an input stream
	    	 
	    	 String strPath = req.getRealPath("/")   + strImg;
	    	 
	    
	    	 InputStream is = new BufferedInputStream( new FileInputStream(strPath));
	    	 Image  image = ImageIO.read(is);	    	 
	    	 
	         int nWidth  = image.getWidth(null);
	         int nHeight = image.getHeight(null);
	         
	         ///System.out.println("Original width = " + nWidth + ", height = " + nHeight);
	         
	         if ( nWidth >= nFixWidth ) // �썝�븯�뒗 湲몄씠蹂대떎 �꼻�씠媛� �꼻�� 寃쎌슦
	         {
	        	 nRate   = ( nFixWidth * 100 ) /  nWidth  ; 
	        	 nWidth  = ( nWidth    *  nRate ) / 100 ; 
	        	 nHeight = ( nHeight   *  nRate ) / 100 ; 
	         }
	         
	         if ( nHeight >= nFixHeight ) // �썝�븯�뒗 湲몄씠蹂대떎 �꼻�씠媛� �꼻�� 寃쎌슦
	         {
	        	 nRate   = ( nFixHeight * 100)  / nHeight  ; 
	        	 nWidth  = ( nWidth     * nRate ) / 100 ; 
	        	 nHeight = ( nHeight    * nRate ) / 100 ; 
	         }
	         
	         ///System.out.println("Resize width = " + nWidth + ", height = " + nHeight);
	         
	    	 nArrSize[0] = nWidth;
	    	 nArrSize[1] = nHeight;
	         
	     } catch (Exception e) {
	    	 
	    	 //nArrSize[0] = nFixWidth;
	    	// nArrSize[1] = nFixHeight;	    	 
	    	 System.out.println(e.toString());
	         //System.out.println("�뙆�씪�씠 �뾾�뒿�땲�떎.");
	     }
	     
	     return nArrSize;
	 }
	 
	 
	 public static  String getMediaScript(String strFile )
	 {
		 String rFileNm ;
		 
		 rFileNm  = " <object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95' width='1000' height='520'  id='MediaPlayer1'>"
		         + "              <param name='Filename' value='" + strFile + "'>                                        "
		         + "              <param name='wmode' value='transparent'>                                                     "
		         + "              <param name='AutoSize' value='1'>                                                            "
		         + "              <param name='AutoStart' value='true'>                                                        "
		         + "              <param name='AutoRewind' value='0'>                                                          "
		         + "              <param name='ClickToPlay' value='false'>                                                     "
		         + "              <param name='Enabled' value='0'>                                                             "
		         + "              <param name='EnableContextdir' value='false'>                                                "
		         + "              <param name='EnableTracker' value='0'>                                                       "
		         + "              <param name='Mute' value='0'>                                                                "
		         + "              <param name='ShowCaptioning' value='0'>                                                      "
		         + "              <param name='ShowControls' value='0'>                                                        "
		         + "              <param name='ShowAudioControls' value='false'>                                               "
		         + "              <param name='ShowDisplay' value='false'>                                                     "
		         + "              <param name='ShowTracker' value='true'>                                                     "
		         + "              <param name='VideoBorderWidth' value='0'>                                                    "
		         + "              <param name='ShowStatusBar' value='0'>                                                       "
		         + "  </object>				                                                                                  ";
		 
		 return rFileNm;
	 }
	 
	   	public static Object displayScreen(HttpServletResponse response, String strTxt) throws IOException{

			response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();
			output.print(strTxt);

			output.flush();
			output.close();

			return null;
		}	 	 
	 
	 
	   	public static Object displayText(HttpServletResponse response, String strTxt) throws IOException{

			response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();
			
			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");				
			
			output.print(strTxt);
			
			output.println("</head>");
			output.println("</html>");			
			output.flush();
			output.close();

			return null;
		}	 

		public static Object alertMsgGoParentFunc(HttpServletResponse response, String msg, String strFunc) throws IOException{

	   		response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");
			output.println("<script type='text/javascript' >");
			output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
			output.println("window.opener." + strFunc+";");
			output.println("window.close();");
			output.println("</script>");

			output.println("</head>");
			output.println("</html>");

			output.flush();
			output.close();

			return null;
		}		   	
	   	
	   	public static Object alertGoParentUrl(HttpServletResponse response, String returnUrl) throws IOException{

	   		response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");
			
			output.println("<script type='text/javascript' >");
			output.println("window.opener.location.href='" + returnUrl+"';");
			output.println("window.close();");
			output.println("</script>");

			output.println("</head>");
			output.println("</html>");			

			output.flush();
			output.close();

			return null;
		}
	   	public static Object alertGoParentScript(HttpServletResponse response, String targetNm, String returnFunc) throws IOException{

			response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");			
			
			output.println("<script type='text/javascript' >");
			output.println("window.opener.name='"+targetNm+"';");
			output.println("window.opener.location.href='javascript:"+returnFunc+";';");
			output.println("window.opener.name='';");
			output.println("window.close();");
			output.println("</script>");

			output.println("</head>");
			output.println("</html>");				
			
			output.flush();
			output.close();

			return null;
		}


		public static Object alertMsgGoParentUrl(HttpServletResponse response, String msg, String returnUrl) throws IOException{

	   		response.setContentType("text/html;charset=euc-kr");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");			
			
			output.println("<script type='text/javascript' >");
			output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
			//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
			output.println("window.opener.location.href='" + returnUrl+"';");
			output.println("window.close();");
			output.println("</script>");

			output.println("</head>");
			output.println("</html>");				

			output.flush();
			output.close();

			return null;
		}
 
		public static Object alertAdminLoginGoUrl(HttpServletResponse response,  String returnUrl) {
            try {
			   		response.setContentType("text/html;charset=utf-8");
		
					PrintWriter output = response.getWriter();
					
					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");					
					
					output.println("<form name='frmLogin' method='post' action='/boffice/index.html'>");
					output.println("<input type='hidden' name='returnurl' value='" + returnUrl + "'>");
					output.println("</form>");
					output.println("<script type='text/javascript' >");
					output.println("alert('Please login.');");
					//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
					output.println("   frmLogin.submit(); ");
					output.println("</script>");
					
					output.println("</head>");
					output.println("</html>");						
					
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}			
		
		public static Object alertLoginGoUrl(HttpServletResponse response,  String strUrl, String returnUrl) {
            try {
			   		response.setContentType("text/html;charset=utf-8");
		
					PrintWriter output = response.getWriter();
					
					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");						
					
					output.println("<form name='frmLogin' method='post' action='" + strUrl +"'>");
					output.println("<input type='hidden' name='returnurl' value='" + returnUrl + "'>");
					output.println("</form>");
					output.println("<script type='text/javascript' >");
					output.println("alert('Please login.');");
					//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
					output.println("   frmLogin.submit(); ");
					output.println("</script>");
					
					output.println("</head>");
					output.println("</html>");						
					
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}			
		
		
		public static Object alertFormLogin(HttpServletResponse response,  Map reqMap,  String strSiteGb,  String strSendUrl,  String returnUrl) {
            try {
            	

			   		response.setContentType("text/html;charset=utf-8");
		
					PrintWriter output = response.getWriter();

					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");					
					
					output.println("<form name='frmLogin' method='post' action='" + strSendUrl +"'>");
					output.println("<input type='hidden' name='user_id' value='" + CommonUtil.nvl(reqMap.get("user_id")) + "'>");
					output.println("<input type='hidden' name='user_pw' value='" + CommonUtil.nvl(reqMap.get("user_pw")) + "'>");
					output.println("<input type='hidden' name='menu_id' value='" + CommonUtil.nvl(reqMap.get("menu_id"), "122") + "'>");
					output.println("<input type='hidden' name='site' value='" + strSiteGb + "'>");
					output.println("<input type='hidden' name='returnurl' value='" + returnUrl + "'>");
					output.println("</form>");
					output.println("<script type='text/javascript' >");

					output.println("   frmLogin.submit(); ");
					output.println("</script>");
					
					output.println("</head>");
					output.println("</html>");						
					
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}				
		
		public static Object alertLoginGoUrl(HttpServletResponse response,  String returnUrl) {
            try {
			   		response.setContentType("text/html;charset=utf-8");
		
					PrintWriter output = response.getWriter();

					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");					
					
					output.println("<form name='frmLogin' method='post' action='/boffice/'>");
					output.println("<input type='hidden' name='returnurl' value='" + returnUrl + "'>");
					output.println("</form>");
					output.println("<script type='text/javascript' >");
					output.println("alert('Please login.');");
					//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
					output.println("   frmLogin.submit(); ");
					output.println("</script>");
					
					output.println("</head>");
					output.println("</html>");						
					
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}		
		
		public static Object alertMsgGoUrl(HttpServletResponse response, String msg, String returnUrl) throws IOException{
            try {
			   		response.setContentType("text/html;charset=euc-kr");
		
					PrintWriter output = response.getWriter();
		
					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");					
					
					output.println("<script type='text/javascript' >");
					if (!"".equals(msg)) 
					{
					   output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
					}   
					//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
					output.println("window.location.href='" + returnUrl+"';");
					output.println("</script>");
		
					output.println("</head>");
					output.println("</html>");						
		
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}

		public static Object alertMsgSelfClose(HttpServletResponse response, String msg, String returnUrl) throws IOException{
            try {
			   		response.setContentType("text/html;charset=euc-kr");
		
					PrintWriter output = response.getWriter();
		
					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");						
					
					output.println("<script type='text/javascript' >");
					output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
					//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
					output.println("self.close()");
					output.println("</script>");
		
					output.println("</head>");
					output.println("</html>");						
		
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}		
		
		public static Object alertMsgOpenSelfClose(HttpServletResponse response, String msg) throws IOException{
            try {
			   		response.setContentType("text/html;charset=euc-kr");
		
					PrintWriter output = response.getWriter();
		
					output.println("<html>"); 
					output.println("<head>");
					output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
					output.println("<title>ORDADATA</title>");	
					
					output.println("<script type='text/javascript' >");
					output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
					//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
					output.println("window.opener.location.reload(true);");
					output.println("self.close()");
					output.println("</script>");
					
					output.println("</head>");
					output.println("</html>");	
		
					output.flush();
					output.close();
            } catch ( Exception e) {
            	System.out.println(e.toString());
            }

			return null;
		}				
		
		public static Object alertMsgConfirmUrl(HttpServletResponse response, String msg, String yesUrl, String cancelUrl) throws IOException{

	   		response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");				
			
			output.println("<script type='text/javascript' >");
			output.println("if(confirm('"+msg.replaceAll("\\'", "\\\"")+"')) {");
			//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
			output.println("window.location.href='" + yesUrl+"';");
			output.println("} else { ");
			output.println("window.location.href='" + cancelUrl+"';");
			output.println("}</script>");
			
			output.println("</head>");
			output.println("</html>");	
			
			output.flush();
			output.close();

			return null;
		}

		public static Object alertMsgClose(HttpServletResponse response, String msg) throws IOException{

			response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");	
			
			output.println("<script type='text/javascript'>");
			output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
			//output.println("alert('"+kscToAsc(msg).replaceAll("\\'", "\\\"")+"');");
			output.println("window.close();");
			output.println("</script>");

			output.println("</head>");
			output.println("</html>");				

			output.flush();
			output.close();

			return null;
		}

		public static Object alertMsgBack(HttpServletResponse response, String msg) throws IOException{

			response.setContentType("text/html;charset=utf-8");

			PrintWriter output = response.getWriter();

			output.println("<html>"); 
			output.println("<head>");
			output.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
			output.println("<title>ORDADATA</title>");				
			
			output.println("<script type='text/javascript' charset='utf-8'>");
			output.println("alert('"+msg.replaceAll("\\'", "\\\"")+"');");
			//output.println("alert('"+kscToAsc1(msg).replaceAll("\\'", "\\\"")+"');");
			output.println("window.history.back();");
			output.println("</script>");

			output.println("</head>");
			output.println("</html>");				
			
			output.flush();
			output.close();

			return null;
		}
	 
      public static String getReplyImg(String strDepth)
      {
    	  String strVal = "";
    		
    	  if (strDepth == null || "".equals(strDepth))
    		  return "";
    	  
    	  try {
    		  int nDepth = Integer.parseInt(strDepth);
    		  for (int nLoop = 1; nLoop < nDepth; nLoop++)
    		  {
    			  strVal += "&nbsp;&nbsp;&nbsp;";
    		  }
    		  
    		  if ( !"".equals(strVal)) 
    		  {
    			  strVal += "<img src='/images/common/ico_reply.gif'>";
    		  }
    		  
    	  } catch ( Exception e) {}
    	  
    	  
    	  return strVal;
      }
      
      public static String getSecretImg(String strDepth)
      {
    	  String strVal = "";
    		
    	  if ("Y".equals(strDepth)) {  
    			  strVal += "<img src='/images/common/ico_secret.jpg' style='width:16px;height:16px'>";
    	  }
    	  return strVal;
      }      
       
      public  static Map getAccessInfo(HttpServletRequest request)   {
     	 Map retMap = new HashMap();
 	    
 	    try {
 	        
 	        HttpSession session = request.getSession(true);			
 			String check = (String)session.getAttribute("counter");
 			
 			if (check != null && check.length() != 0) {
 			   return null;
 			}
 				
 			String userAgent = request.getHeader("User-Agent")==null ? "Etc":request.getHeader("User-Agent");
 			String browser = null;
 			String os = null;

 			int i1 = userAgent.indexOf("MSIE");
 			int i2 = 0;
 			if (i1 != -1) {
 				browser = userAgent.substring(i1);
 				i2 = browser.indexOf(";");
 				if (i2 != -1)
 					browser = browser.substring(0, i2);

 				if (userAgent.indexOf("Opera") != -1)
 					browser = userAgent.substring(userAgent.indexOf("Opera"));

 			} else {
 				if (userAgent.indexOf("Firefox") != -1)
 					browser = userAgent.substring(userAgent.indexOf("Firefox"));
 				else if (userAgent.indexOf("Netscape") != -1)
 					browser = userAgent.substring(userAgent.indexOf("Netscape"));
 				else
 					browser = "unknown";
 			}

 			if (browser.length() > 15)
 				browser = browser.substring(0, 14);

 			i1 = userAgent.indexOf("Windows");
 			if (i1 != -1)
 				os = userAgent.substring(i1);
 			else
 				os = "unknown";

 			i2 = os.indexOf(")");
 			if (i2 != -1)
 				os = os.substring(0, i2);

 			int i3 = os.indexOf(";");
 			if (i3 != -1)
 				os = os.substring(0, i3);

 			if (os.length() > 15)
 				os = os.substring(0, 14);				
 			
 			retMap.put("ipaddr",  request.getRemoteAddr());
 			retMap.put("referer", (String)session.getAttribute("referer"));
 			retMap.put("browser", browser);
 			retMap.put("os",      os);
 		 	
 			session.setAttribute("counter", "TRUE");
  
 	    } catch (Exception exception) {
 	        System.out.print(exception.toString());
 	        retMap.clear();
 	    }
 	    
 	    return retMap;
 			
 	}           

	    public static String getListTransXml(List rsLst, String strRoot)
		 {
			 String strVal = "";
			 try {
				 
				 if ( rsLst != null && !rsLst.isEmpty()) {
					 for (int nLoop=0; nLoop < rsLst.size(); nLoop++) {
						 
						 strVal += "<" + strRoot + ">"; 
						 Map rsMap = (Map)rsLst.get(nLoop);
							 
						  Iterator iter = rsMap.keySet().iterator();
				
							 while( iter.hasNext()) 
							 {
								  String key = (String)iter.next();
								  String value = nvl(rsMap.get( key ));
					              					     
								  strVal += "<" + key.toLowerCase() + ">";
								  strVal += "<![CDATA[" + value.trim() + "]]>";
								  strVal += "</" + key.toLowerCase() + ">\n";
							 }
						 strVal += "</" + strRoot + ">"; 						 
					 }
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}	 

		    return strVal;
		 }	        

	 public static Map getUniv2DepthMenu(List menuList, String strUpMenuNo) {   

	     String strMenu = "";	     
		 String strUrl  = "";
		 Map retMap = new HashMap();
		 
	     try {
	         for(int nLoop=0; nLoop < menuList.size(); nLoop ++)
	         {  
	         	Map rsMap = (Map)menuList.get(nLoop);
	         	if (!strUpMenuNo.equals(CommonUtil.nvl(rsMap.get("UP_MENU_NO")))) 
	         		continue;
	            
	         	//strUrl  = CommonUtil.nvl(rsMap.get("MENU_URL")); 
	         	//strUrl += (( strUrl.indexOf("?") > 0 ) ? "&" : "?" ) + "menu_no=" + CommonUtil.nvl(rsMap.get("MENU_NO"));	
	         	strUrl = getMenuUrl(rsMap);
	         	
	         	if ( "".equals(strMenu)) 
	         		retMap.put("first", strUrl);
	         	
	         	strMenu += "<li><a href='" + strUrl + "' target='" + CommonUtil.nvl(rsMap.get("URL_TARGET")) + "'>" + CommonUtil.nvl(rsMap.get("MENU_NM")) + "</a></li>"; 
	         }			 
		 } catch ( Exception e) {}
		 
		 retMap.put("menulist", strMenu);
		 
		 return retMap;

	 }
	 
	 public static String getMenuUrl(Map rsMap) {
		String strUrl   = nvl(rsMap.get("MENU_URL")); 
		String strMgrno = nvl(rsMap.get("BRD_MGRNO"));
		
		if ( strUrl.equals("#this"))
			strUrl = "";
		
		if ( strMgrno != null && !"".equals(strMgrno)) {
			
			strUrl  = CommonUtil.removeParam(strUrl, "brd_mgrno");
      		strUrl += (( strUrl.indexOf("?") > 0 ) ? "&" : "?" ) + "brd_mgrno=" + strMgrno;	
      	}
      	
		if(strUrl.indexOf("menu_no") < 1) {
			strUrl  = CommonUtil.removeParam(strUrl, "menu_no");
			strUrl += (( strUrl.indexOf("?") > 0 ) ? "&" : "?" ) + "menu_no=" + CommonUtil.nvl(rsMap.get("MENU_NO"));
		}
		
		if ("".equals(strUrl))
			strUrl = "#this";
		
     	return strUrl;
		 
	 }

	 public static Map getFirstMenuUrl(List rsList, Map curMap) {
			Map retMap = curMap; 
			
			String strTopFullMenuNo = CommonUtil.nvl(curMap.get("FULL_MENU_NO")); 
			
			String strDepth = "";
			String strUpMenuNo = "0";
			
		    for(int nLoop=0; nLoop < rsList.size(); nLoop++)
		    {
			  Map rsMap = (Map)rsList.get(nLoop);
			  String strMenuNo = nvl(rsMap.get("MENU_NO"));
			  
			  String strCurFullNo = nvl(rsMap.get("FULL_MENU_NO"));
			  
			  if (strCurFullNo.indexOf(strTopFullMenuNo) < 0 )  
				  continue;	  
			  
			  if ( !strDepth.equals(nvl(rsMap.get("MENU_LVL")))) { 
			      strDepth    = nvl(rsMap.get("MENU_LVL"));
			      retMap = rsMap;

			      int nChildCnt = getNullInt(rsMap.get("CHILD_CNT"), 0);
			      if ( nChildCnt == 0 )
			    	  break;
			  }
			  
		    }
		    
	     	return retMap;			
	 }
	  
     
	    public static String getCurrBoardData(String strYyyymm, String strDay, List lstRs) {
	    	return getCurrBoardData(strYyyymm, strDay, lstRs, false);
	    }
	    
	    public static String getCurrBoardData(String strYyyymm, String strDay, List lstRs, boolean isMain ) {
	    	String strVal = "";
	    	String strDate = "";
	    	String strTitle = "";
	    	String strBrdNo = "";
	    	
	    	if ( lstRs == null || lstRs.isEmpty())
	    		return "";
	    	
             
	    	try {
	    		  if ( Integer.parseInt(strDay) < 10 ) {
	    			  strDate = strYyyymm + "0" + strDay;
	    		  } else {
	    			  strDate = strYyyymm +  strDay;
	    		  }
	    		  int nDate = Integer.parseInt(strDate);
    		  
	    		  for(int nLoop=0; nLoop < lstRs.size(); nLoop++) {
	    			  Map rsMap = (Map)lstRs.get(nLoop);
	    			  
	    			  int nSdt = CommonUtil.getNullInt(rsMap.get("SDT"), 0);
	    			  int nEdt = CommonUtil.getNullInt(rsMap.get("EDT"), 0);
	    			  String strTtlVal = CommonUtil.nvl(rsMap.get("TTL")).replaceAll("'", "");
	    			  
	    			  if ( nSdt <= nDate && nEdt >= nDate ) {
	    				  if ( !isMain ) {
	    				     strVal += "<br/><a href='javascript:fView(" + strYyyymm + "," +  CommonUtil.nvl(rsMap.get("BRD_NO")) + ")' title=\"" + strTtlVal + "\">" + CommonUtil.getStrCut(strTtlVal, 12) + "</a>";
	    				  } else {
	    					 strVal = "<a href='javascript:fCalcBoardView(" + strYyyymm + "," +  CommonUtil.nvl(rsMap.get("BRD_NO")) + ")'  title=\"" + strTtlVal + "\">" + strDay + "</a>";
	    					 
	    					 if (!"".equals(strTitle)) {
	    						 strTitle += "&#13;";
	    					 }	 
	    					 strTitle += "&nbsp;" + strTtlVal ;
	    					 
	    					 strBrdNo = CommonUtil.nvl(rsMap.get("BRD_NO"));
	    					 
	    					//  break;
	    				  }
	    			  }
	    		  }

	    		  
	    		  if ( isMain && !"".equals(strTitle)) {
	    			  
	    			  strVal = "<a href='javascript:fCalcBoardView(" + strYyyymm + "," +  strBrdNo  + ")'  title=\"" + strTitle + "\">" + strDay + "</a>";
	    		  }
	    		  
	    		  
	    		  
	    	} catch ( Exception e) {}    	
	    	return strVal; 
	    }	
	    
	 public static String getHomeDepthMenu(List menuList, String strMenuNo) {	    
        
		StringBuffer sb = new StringBuffer(); 
		 
        if (menuList == null || menuList.isEmpty())
        	return "";
        
        for ( int nNest=0; nNest < menuList.size(); nNest++ )
        {
          	 Map topMap = (Map)menuList.get(nNest);  
          	 String strUpMenuNo = CommonUtil.nvl(topMap.get("UP_MENU_NO"));
          	 int    nMenuLvl    = CommonUtil.getNullInt(topMap.get("MENU_LVL"), 0); 
          	 
          	 if ( !strMenuNo.equals(strUpMenuNo) || nMenuLvl != 2 )
          		 continue;
          	 
          	 String strUrl  = CommonUtil.getMenuUrl(topMap);
          	 
          	 String strUrlTarget = CommonUtil.nvl(topMap.get("URL_TARGET"), "_self");
          	 
          	 int nChildCnt = CommonUtil.getNullInt(topMap.get("CHILD_CNT"), 0);
          	 int nMenuNo   = CommonUtil.getNullInt(topMap.get("MENU_NO"), 0);
          	 
          	 
          	 
          	 if ( nChildCnt == 0 ) {          
          		sb.append("<li><a href='" + strUrl + "' target='" + strUrlTarget + "'>" + nvl(topMap.get("MENU_NM")) + "</a></li>\n");
  	         } else {
                   String strClass = "d02";
                   
                   if ( nMenuNo == 79 ) strClass = "d03";
                   if ( nMenuNo == 80 ) strClass = "d04";
                   if ( nMenuNo == 106 ) strClass = "d04";

                   sb.append("<li class='abc'><a href='" + strUrl +  "' target='" + strUrlTarget + "'>" + nvl(topMap.get("MENU_NM"))+ "</a>\n");
                   sb.append("<ul class='" + strClass + "'>");
					        
                   
                   for ( int nLoop=0; nLoop < menuList.size(); nLoop++ )
                   {
                     	 Map subMap = (Map)menuList.get(nLoop);  
                     	 String strSubUpMenuNo = CommonUtil.nvl(subMap.get("UP_MENU_NO"), "0");
                     	 
                     	 if ( nMenuNo != Integer.parseInt(strSubUpMenuNo) )
                     		 continue;                   
                   
                     	 String strTarget = CommonUtil.nvl(subMap.get("URL_TARGET"), "_self");
                     	 
                     	strUrl  = CommonUtil.getMenuUrl(subMap);
                     	sb.append("<li><a href='" + strUrl +  "' target='" + strUrlTarget + "'>" + nvl(subMap.get("MENU_NM")) + "</a></li>\n"); 
                   }
                   
                   sb.append("   </ul>\n ");
                   sb.append(" </li>\n ");
 	
               }
           }
        
        return sb.toString();
        
    }
	 
	    /**
	     * Method Summary. <br>
	     *  硫붾돱 援ъ“ XML �깮�꽦 method
	     * @param servletRequest
	     * @param Map
	     * @return String
	     * @throws  
	     * @since 1.00
	     * @see
	     */    
	 public static  String makeMenuXml(HttpServletRequest request, List rsLst) {
	 
		  	 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		     DocumentBuilder db = null;
		     String LI_TAG = "li";
		     String UL_TAG = "ul";
		     String strVal = "";
		     String ROOT_TAG = "root";
		     
		     if ( rsLst == null || rsLst.isEmpty())
		    	 return "";
		     
		     try{
		         db = dbf.newDocumentBuilder();
		     }catch(ParserConfigurationException e){
		         System.out.println(e.toString());
		     }	   
		   
	        try { 
	        	
	            Document doc = db.newDocument();
	            Element rootEle = doc.createElement(ROOT_TAG);
	            doc.appendChild(rootEle);        	
	        	
	            Element child = doc.createElement(LI_TAG);
	            
	            child.setAttribute("id", "menu_id_0");
	            child.setTextContent("硫붾돱");
	            child.setAttribute("class", "folder expanded");
	            
	            rootEle.appendChild(child);
	            
	            for (int nLoop=0; nLoop < rsLst.size(); nLoop++) {
	            	Map rsMap = (Map)rsLst.get(nLoop);
	            	
	            	String strTagId    = "menu_id_"     + CommonUtil.nvl(rsMap.get("MENU_NO"));
	            	String strTagUId   = "menu_id_"     + CommonUtil.nvl(rsMap.get("UP_MENU_NO"));
	            	String strTagGrpId = "grp_menu_id_" + CommonUtil.nvl(rsMap.get("UP_MENU_NO"));
	            	String strMenuLvl  =  CommonUtil.nvl(rsMap.get("MENU_LVL"), "1");
	            	
	            	Node pNode = getFindNode(rootEle, strTagGrpId); // �긽�쐞硫붾돱踰덊샇 李얜뒗�떎.
	            	if ( pNode == null ) {
	            		pNode = getFindNode(rootEle, strTagUId); // UL �뀒洹� �긽�쐞硫붾돱踰덊샇 李얜뒗�떎.
	            	}
	            	
	            	if ( pNode != null && pNode.getChildNodes().getLength() <= 1 ) { // 李얠� Node�뿉 �븯�쐞 �끂�뱶媛� �뾾�뒗 寃쎌슦
	            		if (LI_TAG.equals(pNode.getNodeName())) {
	                        Element childLi = doc.createElement(UL_TAG);
	                        childLi.setAttribute("id", strTagGrpId);
	                        pNode.appendChild(childLi);
	                        
	                        pNode = childLi;
	            		} 
	            	}
	            	
	            	String strMenuNm = CommonUtil.nvl(rsMap.get("MENU_NM"));
	            	if ("TAB".equals(CommonUtil.nvl(rsMap.get("MENU_FMT_CD")))) {
	            		strMenuNm = "[�뀦]" + strMenuNm;
	            	}
	                child = doc.createElement(LI_TAG);
	                
	                child.setAttribute("id", strTagId);
	                
	                child.setTextContent(strMenuNm);
	           
	                if (Integer.parseInt(strMenuLvl) <= 0) { // 泥섏쓬�뿉 �렯移섎룄濡� 泥섎━�븿
	                	child.setAttribute("class", "folder expanded");
	                }
	                
	                if ( pNode == null ) {
	            	   rootEle.appendChild(child);
	                } else {
	                	pNode.appendChild(child);
	                }
	            	
	            }
		
	            // XML 援ъ“瑜� String 援ъ“濡� 諛섑솚
	            TransformerFactory tf = TransformerFactory.newInstance( );
	            Transformer t = null;
	            try
	            {
	                t = tf.newTransformer( );
	            }
	            catch ( TransformerConfigurationException e )
	            {
	            	System.out.println(e.toString());
	            }
	            t.setOutputProperty( OutputKeys.ENCODING , "utf-8" );
	            t.setOutputProperty( OutputKeys.METHOD , "xml" );
	            t.setOutputProperty( OutputKeys.INDENT , "yes" );
	            t.setOutputProperty( OutputKeys.CDATA_SECTION_ELEMENTS , "yes" );
	            StringWriter sw = new StringWriter( );
	            try
	            {
	                t.transform( new DOMSource( doc ) , new StreamResult( sw ) );
	            }
	            catch ( TransformerException e )
	            {
	            	System.out.println(e.toString());
	            }
	            
//	            System.out.println( sw.toString( ) );     
	            
	            strVal = sw.toString();
	            
	        } catch ( Exception e) {
	        	System.out.println(e.toString());
	        }
	        
	        // 硫붾돱援ъ“�뿉 �븘�슂�븳 遺�遺꾨쭔 �씫�쓬
	        String strSRoot = "<" +ROOT_TAG + ">";
	        String strERoot = "</" + ROOT_TAG + ">";
	        
	        int nFirst = strVal.indexOf(strSRoot);
	        int nLast  = strVal.indexOf(strERoot);     
	        
	        strVal = strVal.substring(0, nLast);
	        strVal = strVal.substring(nFirst + strSRoot.length());
	 
	        return strVal;
	        
	    }   
	   
	   /**
	    * Method Summary. <br>
	    * Node 李얘린 method
	    * @param Element Root �끂�뱶
	    * @param String  李얘퀬�옄 �븯�뒗 �궎
	    * @return void
	    * @throws  
	    * @since 1.00
	    * @see
	    */       
	 public static  Node getFindNode(Element rootEle, String strFindKey) {
		  // NodeList nodeLst =  rootEle.getChildNodes();
		   Node nodeVal = null;
		   NodeList nodeLst = rootEle.getElementsByTagName("*");
		   
		   
		   for(int nLoop=0; nLoop < nodeLst.getLength(); nLoop++)
		   {
			   Node node = nodeLst.item(nLoop);
			   
			   NamedNodeMap nodeMap = node.getAttributes();
			   Node attNode = nodeMap.getNamedItem("id");
			   if (strFindKey.equals(attNode.getNodeValue())) {
				   nodeVal = node;
				   break;
			   }
		   }
		   
		   return nodeVal;
    }


    public static boolean isWriteAuth(HttpServletRequest servletRequest, String strBrdWriteAuthCd)
    {
    	
    	if ("ALL".equals(strBrdWriteAuthCd))
    		return true;
    	
    	if (CommDef.Board.BRD_NOWRITE.equals(strBrdWriteAuthCd))
    		return false;
    	
    	try {
		    	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
		    	if ( userMap == null || userMap.isEmpty())
		    		return false;
		    	
		    	String strUserType = nvl(userMap.get("USER_TYPE"));
		    	
		    	if (!CommDef.ReservedWord.ADMIN.equals(strUserType)) {
		    		strUserType = CommDef.ReservedWord.USER;
		    	}
		    	
		    	if ( strUserType.equals(strBrdWriteAuthCd))
		    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    	
    	return false;
    	
    }
    
    /**
     * �씪�떒�쐞 �궇吏� 李⑥씠 怨꾩궛�븯湲�
     * @param regdt
     * @return
     * @throws Exception
     */
    public static long diffDays(Object regdt) throws Exception{
    	
    	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String stDate = df.format(new Date());
        Date date = df.parse(stDate);
        
        String stRegdt = ((String)regdt).replace("-", "").substring(0, 8);
        Date oDate = df.parse(stRegdt);
        
        long diff = date.getTime() - oDate.getTime();
        long diffDays = diff / (24*60*60*1000);
    	
        return diffDays;
    }
    
    /**
     * 釉뚮씪�슦�� 援щ텇 �뼸湲�.
     *
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Trident") > -1) {     // IE11 臾몄옄�뿴 源⑥쭚 諛⑹�
            return "Trident";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }
    
    /**
     * 臾몄옄�뿴 UTF-8 �씤肄붾뵫
     * @param request
     * @param str
     * @return
     * @throws Exception
     */
    public static String getUrlEncodeString(HttpServletRequest request, String str) throws Exception{
    	
    	String browser = getBrowser(request);
    	String encodedString = str;
    	
        if (browser.equals("MSIE")) {
        	encodedString = URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Trident")) {          // IE11 臾몄옄�뿴 源⑥쭚 諛⑹�
        	encodedString = URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
        } 
    	
    	return encodedString;
    	
    }
    
    /**
     * �뙆�씪 議댁옱 �뿬遺� 泥댄겕
     * @param request
     * @param src 泥댄겕 �뙆�씪 寃쎈줈
     * @param nSrc 蹂�寃� �뙆�씪 寃쎈줈
     * @return
     */
    public static String checkExistFile(HttpServletRequest request, String src, String nSrc) {
    	
    	String path = request.getSession().getServletContext().getRealPath(src);
    	File file = new File(path);
    	
    	if(!file.isFile()) {
    		if(nSrc != null && !"".equals(nSrc)) {
    			src = nSrc;
    		} 
    	} 
    	
    	return src;
    }

    /**
     * @param map 蹂��솚�븷 臾몄옄
     * @return �궎媛믪쓣 �냼臾몄옄濡� 蹂��솚�븳 留�
     */
     
   	public static String mapToJson(Map<String, Object> map) {
    	 JSONObject json = new JSONObject();
         try {
         	for (Map.Entry<String, Object> entry : map.entrySet()) {
            	String key = entry.getKey();
                Object value = entry.getValue();
                json.put(key, value);
            }
         } catch (JSONException e) {
         	System.out.println(e.toString());
         }
         return json.toString();
	}

   	/**
     * IP瑜� long���엯�쑝濡� 蹂��솚
     * @param ipAddress
     * @return
     */
    public static long ipToLong(String ipAddress) {
        String[] ipAddressTemp = ipAddress.split("\\.");
        long ipAddressLong = (Long.parseLong(ipAddressTemp[0]) << 24) + (Long.parseLong(ipAddressTemp[1]) << 16) + (Long.parseLong(ipAddressTemp[2]) << 8) + (Long.parseLong(ipAddressTemp[3]));
        return ipAddressLong;
    }
    
    public static String converSChars(Date date) {
//    	Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		int year = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH) + 1;
//		int day = cal.get(Calendar.DATE);
//		int hour = cal.get(Calendar.HOUR_OF_DAY);;
//		int minute = cal.get(Calendar.MINUTE);;
//		
//		String tParam = convertSChars("" + year)
//				+ convertSChars(String.format("%02d", month))
//				+ convertSChars(String.format("%02d", day))
//				+ convertSChars(String.format("%02d", hour))
//				+ convertSChars(String.format("%02d", minute));
//        	
//		return tParam;
    	
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return String.format("%d", cal.getTimeInMillis() / 1000);
	}
    
    public static String convertSChars(String strNum) {
        String strChars= ")!@#$%^&*(";
        int len = strNum.length();
        String r = "";
        for(int i=0;i<len;i++) {
        	char m = strNum.charAt(i);
            r += strChars.charAt((int)(m) - 48);
        }
        return r;
    }
    
    public static String createSequenceKey(int strNum) {
    	String r = "";
    	r=Integer.toString(strNum);
    	int header = Integer.valueOf(r.substring(0,2));
    	char result = (char) header;
    	
    	r = result + r.substring(2);
    	
        return r;
    }

}