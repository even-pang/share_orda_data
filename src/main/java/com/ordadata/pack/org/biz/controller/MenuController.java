/*
 * Copyright (c) 2008  sosun . All rights reserved.
 */
package com.ordadata.pack.org.biz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.ClientInfo;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.StaticUtil;

/**
 * Class Summary. <br>
 * 메뉴 관리 class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */

@Controller
public class MenuController   {

	@Resource(name="dbSvc")
	private DbController dbSvc;
	
	 /**
     * Method Summary. <br>
     * Menu Header  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/home/header.do" )
    public String menuHeader(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {

		} catch (Exception e) {
			System.out.println(e.toString());
		}
        
        return "home/include/header";
    }
    
    @RequestMapping(value="/home/subCmtBg.do")
    public String subBg(HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse) throws Exception {
    	
    	HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
    	try {
    		reqMap.put("file_gbn", "CHOOSE");
    		Map topMenuDt = dbSvc.dbDetail(reqMap, "menu.topMenuDetail");
    		
    		servletRequest.setAttribute("topMenuDt", topMenuDt);
    		
    	} catch(Exception e) {
        	e.printStackTrace();
        }
    	
    	return "home/include/subCmtBg";
    }

   	 /**
         * Method Summary. <br>
         * footer method.
         * @param servletRequest HttpServletRequest 객체
         * @param servletResponse HttpServletResponse 객체
         * @return ActionForward 객체 -  정보
         * @throws e Exception
         * @since 1.00
         * @see
         */     
        @RequestMapping( value="/home/topMenu.do" )
        public String topMenu(HttpServletRequest servletRequest,
                HttpServletResponse servletResponse) throws Exception {
            
            HttpSession session = servletRequest.getSession( false );
            Map reqMap = CommonUtil.getRequestMap( servletRequest );
            Map paramMap = new HashMap();
            
            try {
            	paramMap.put("use_yn", "Y");
            	paramMap.put("menu_gb", CommDef.MENU_HOME);
        		servletRequest.setAttribute("menuList", dbSvc.dbList(paramMap, "menu.menuAllDepthList"));
            	String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
            	
            } catch (Exception e) {
                System.out.println(this.getClass().getName() + ".home/topmenu : "+ e.toString());
     
            }        	
        	
            return "home/include/topMenu";
        }
        
	 /**
     * Method Summary. <br>
     * footer method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/home/footer.do" )
    public String menuFooter(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {
        
    	Map paramMap = new HashMap();
    	try {
		    	
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}
        return "home/include/footer";
    }    
    
	 /**
     * Method Summary. <br>
     * footer method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/home/quick.do" )
    public String menuQuick(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map paramMap = new HashMap();
        
        try {
    		
         
        	   Map menuDetail = dbSvc.dbDetail( reqMap, "menu.menuDetail");
        	   
        	   servletRequest.setAttribute( "deptDetail",   menuDetail );
        	  
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".home/topmenu : "+ e.toString());
 
        }       
    	
        return "home/include/quick";
    }        
    
	 /**
     * Method Summary. <br>
     * Menu Header  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/home/menuLeft.do" )
    public String menuLeft(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {
           reqMap.put("menu_gb", CommDef.MENU_HOME );            
           reqMap.put("use_yn",  "Y" );	
           
           if(reqMap.get("cts_no") != null) {
        	   reqMap.put("use_yn", null);
           }
        	
           CommonUtil.setNullVal(reqMap, "menu_no", "-1");
           
           List ctsList = dbSvc.dbList( reqMap, "contents.contentsList");
           
           if ( ctsList == null || ctsList.isEmpty()){ // 메뉴번호에 해당하는 콘텐츠가 존재하지 않은 경우 Child의 첫번째를 찾아냄
        	   Map childMap =  dbSvc.dbDetail(reqMap, "menu.menuFirstChild");
        	   
        	   if ( childMap != null ) {  // 새로운 메뉴 번호를 부여한다.
        		   reqMap.put("menu_no",  childMap.get("MENU_NO") );
        	 }        	   
           }            
           
           reqMap.put("use_yn",  "Y" );
           Map  subMainMap  = dbSvc.dbDetail(reqMap, "menu.subMenuInfo"); // 서브메인 정보
           if ( subMainMap == null )
        	   subMainMap = new HashMap();           
           
           servletRequest.setAttribute( "menuInfo",   dbSvc.dbDetail(reqMap, "menu.menuDetail") ); // 메뉴정보
           servletRequest.setAttribute( "subMenuInfo",   subMainMap ); // 서브 메인의 정보
           //servletRequest.setAttribute( "subMenuList",   dbSvc.dbList( reqMap, "menu.subLeftMenuList") );
           servletRequest.setAttribute( "reqMap",        reqMap );
           
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + "/home/menuLeft.do : "+ e.toString());
 
        }
        
        return "home/include/lnb";
    }
    
	 /**
     * Method Summary. <br>
     * Menu Header  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/home/menuNav.do" )
    public String menuHomeNav(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {
    	
    	getMenuNav(servletRequest, servletResponse);
        
        return "home/include/nav";
    }
    
    public void getMenuNav(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {
    	
        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {
                       
           reqMap.put("use_yn",  "Y" );	
           CommonUtil.setNullVal(reqMap, "menu_no", "-1"); // 메뉴번호가 없는 경우
           
           int nRowCount = dbSvc.dbCount( reqMap, "contents.contentsCount");
           
           if ( nRowCount <= 0 ){ // 메뉴번호에 해당하는 콘텐츠가 존재하지 않은 경우 Child의 첫번째를 찾아냄
        	   Map childMap =  dbSvc.dbDetail(reqMap, "menu.menuFirstChild");
        	   
        	   if ( childMap != null ) {
        		   reqMap.put("menu_no",  childMap.get("MENU_NO") );
        	   }        	   
           } 
        	   
           Map menuMap = dbSvc.dbDetail(reqMap, "menu.menuPathDetail");
           if ( menuMap == null) {
        	   menuMap = dbSvc.dbDetail(reqMap, "menu.menuDetail");
           }
           
           servletRequest.setAttribute( "menuMap",  menuMap );
        	
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + "/home/getMenuNav : "+ e.toString());
 
        }
        
    }
    
    
  }
