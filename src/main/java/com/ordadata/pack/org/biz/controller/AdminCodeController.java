/*
 * Copyright (c) 2008 Sosun. All rights reserved.
 */
package com.ordadata.pack.org.biz.controller;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Summary. <br>
 * Code 관리 class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */

@Controller
public class AdminCodeController   {
 
	@Resource(name="dbSvc")
	private DbController dbSvc;
	
	 /**
     * Method Summary. <br>
     * 코드 목록 처리 method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/boffice/code/codeList.do" )
    public String menuList(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );

        String strPageUrl  = "boffice/code/codeList";
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}

        	String strCdType = CommonUtil.nvl(reqMap.get("cd_type"));
        	String strCommCd = CommonUtil.nvl(reqMap.get("comm_cd"));
        	
        	if ( "".equals(strCdType)) {
        		reqMap.put("comm_cd", "*");
        		reqMap.put("ord", " cd_type ");
        	} else {
        		reqMap.remove("comm_cd");
        		
        		reqMap.put("not_comm_cd", "*");
        	}
        	
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "count",  Integer.toString(  dbSvc.dbCount( reqMap, "code.codeCount" ) ) );
            servletRequest.setAttribute( "list",   dbSvc.dbList( reqMap, "code.codeList") );
               
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".codeList() : "+ e.toString());
 
        }
        
        return strPageUrl;
    }
         
    /**
     * Method Summary. <br>
     * 코드 등록/수정 화면 method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @since 1.00
     * @see
     */
    @RequestMapping( value="/boffice/codeInput.do" )
    public String boardInput(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map detailMap = new HashMap();
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	      	
            String strCdType = CommonUtil.getNullTrans(reqMap.get("cd_type"));
            
            reqMap.put("iflag", CommDef.ReservedWord.INSERT);
      
            if ( !"".equals(strCdType)){
            	
            	detailMap = dbSvc.dbDetail(reqMap, "code.codeDetail");
            	if ( detailMap != null)
            		reqMap.put("iflag", CommDef.ReservedWord.UPDATE);
            	
             	servletRequest.setAttribute( "reqMap", reqMap);
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".codeInput() : "+ e.toString());
        }
        
        servletRequest.setAttribute( "dbMap", detailMap);
        servletRequest.setAttribute( "reqMap", reqMap);
        
        return "boffice/code/codeWrite";
    }    
    
    
    /**
     * Method Summary. <br>
     * 코드 입력&수정 처리 method
     * @param actionMapping
     * @param form
     * @param servletRequest
     * @param servletResponse
     * @return ActionForward
     * @throws Exception description
     * @since 1.00
     * @see
     */
    @RequestMapping( value="/boffice/codeWork.do" )
    public String codeWork(HttpServletRequest servletRequest, 	HttpServletResponse servletResponse) throws Exception {
    	
    	String strUrl = "";
    	String msg = "";
        
    	Map reqMap  = CommonUtil.getRequestMap( servletRequest );	      
        Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "ADM");
        
        if (sesMap == null ) {
        	CommonUtil.alertAdminLoginGoUrl(servletResponse, "/");  
        	return null;  
        }
        try {
        	String strFlag    = CommonUtil.nvl(reqMap.get("iflag"));
        	
            if ( CommDef.ReservedWord.INSERT.equals(strFlag)) 
            {
            	dbSvc.dbInsert(reqMap, "code.codeInsert");
            	msg = "저장하였습니다.";
            } else if ( CommDef.ReservedWord.UPDATE.equals(strFlag)) {  	
            	dbSvc.dbUpdate(reqMap, "code.codeUpdate");
            	msg = "수정하였습니다.";
            } else if ( CommDef.ReservedWord.DELETE.equals(strFlag)) {
            	
            	String strCommCd = CommonUtil.nvl(reqMap.get("comm_cd")); 
            	if ( "*".equals(strCommCd)) {
            		reqMap.remove("comm_cd");
            	}
            	
            	dbSvc.dbDelete(reqMap, "code.removeCode");
            	msg = "삭제하였습니다.";
            }            
 
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : "+ e.toString());
        }
            
        CommonUtil.alertMsgOpenSelfClose(servletResponse, msg);
        
        return  null;
    }        
              
	  
}
