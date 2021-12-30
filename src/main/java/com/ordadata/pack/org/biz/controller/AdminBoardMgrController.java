/*
 * Copyright (c) 2008 Sosun. All rights reserved.
 */
package com.ordadata.pack.org.biz.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SessionUtil;
import com.ordadata.pack.charm.util.UploadUtil;

/**
 * Class Summary. <br>
 * 게시판 관리 class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */

@Controller
public class AdminBoardMgrController  {

	@Resource(name="dbSvc")
	private DbController dbSvc;	
	
	private String mTableName = "TB_BOARDMGR";	// 테이블 명
    private String BRD_UPLOADPATH = "/upload/board/";
     
	 /**
     * Method Summary. <br>
     * 게시판 목록 처리 method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/boffice/boardmgr/boardMgrList.do" )
    public String boardList(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}
        	
        	int nPageRow     = dbSvc.getPageRowCount(reqMap, "page_row");       	
            int nRowStartPos = nPageRow * ( dbSvc.getPageNow(reqMap, "page_now") - 1 );  // Row의 시작위치        	

            reqMap.put("page_row", nPageRow);                                      // 현재 페이지
            
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "count",  Integer.toString(  dbSvc.dbCount( reqMap, "boardmgr.boardMgrCount" ) ) );
            servletRequest.setAttribute( "list",   dbSvc.dbList( reqMap, "boardmgr.boardMgrList", nRowStartPos ,nPageRow) );           
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardMgrList() : "+ e.toString());
        }
        
        return "boffice/boardmgr/boardMgrList";
        
    }
       
    /**
     * Method Summary. <br>
     * 게시판 미리보기 method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @since 1.00
     * @see
     */
    
    @RequestMapping( value="/boffice/boardmgr/boardMgrView.do" )
    public String boardView( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        String strBrdUrl  = CommonUtil.nvl(reqMap.get("brd_url"), "boffice/formView");
        
        try {
        	
        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	
        	Map detailMap = dbSvc.dbDetail(reqMap, "boardmgr.boardMgrDetail");    	
          
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "detail", detailMap);
             
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardView() : "+ e.toString());
        }
        
        return strBrdUrl;
    }
    

    
    /**
     * Method Summary. <br>
     * 게시판 등록/수정 화면 method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @since 1.00
     * @see
     */
    @RequestMapping( value="/boffice/boardmgr/boardMgrInput.do" )
    public String boardInput(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map detailMap = new HashMap();
        
        String strRegNo = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strIFlag = CommonUtil.nvl(reqMap.get("iflag"));        
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
            
            reqMap.put("iflag", CommDef.ReservedWord.INSERT);
      
            if ( !"".equals(strRegNo)){
            	
            	detailMap = dbSvc.dbDetail(reqMap, "boardmgr.boardMgrDetail");
            	if ( detailMap != null)
            		reqMap.put("iflag", CommDef.ReservedWord.UPDATE);
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardMgrInput() : "+ e.toString());
        }
        
        if ( strIFlag.equals(CommDef.ReservedWord.REPLY))
        {
        	reqMap.put("iflag", CommDef.ReservedWord.REPLY);
        }        
        
        Map  paramMap = new HashMap();
        paramMap.put("cd_type"    , "USEYN");
        paramMap.put("not_comm_cd", "*");
   		
        servletRequest.setAttribute( "lstUseYn",   dbSvc.dbList(paramMap, "code.codeList") );        
        
        servletRequest.setAttribute( "dbMap", detailMap);
        servletRequest.setAttribute( "reqMap", reqMap);
        
        return "boffice/boardmgr/boardMgrWrite";
    }    
  
    
    /**
     * Method Summary. <br>
     * 게시판 입력&수정 처리 method
     * @param actionMapping
     * @param form
     * @param servletRequest
     * @param servletResponse
     * @return ActionForward
     * @throws Exception description
     * @since 1.00
     * @see
     */
    @RequestMapping( value="/boffice/boardmgr/boardMgrWork.do" )
    public String boardWork(HttpServletRequest servletRequest, 	HttpServletResponse servletResponse) throws Exception {
    	
    	String strBrdNo = "";
    	String strUrl = "";

    	Map reqMap = CommonUtil.getRequestMap( servletRequest );	
        
        Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "ADM");
        
        if (sesMap == null ) {
        	strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/");
        	strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );
        	
        	CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);  
        	return null;  
        }
        try {
        	String strFlag    = CommonUtil.setNullVal(reqMap.get("iflag"), "");
        	reqMap.put("reg_ip", servletRequest.getRemoteAddr());
        	
            if ( CommDef.ReservedWord.INSERT.equals(strFlag)) 
            {
            	dbSvc.dbInsert(reqMap, "boardmgr.boardMgrInsert");	 

            } else if ( CommDef.ReservedWord.UPDATE.equals(strFlag)) 
            {  	
            	dbSvc.dbUpdate(reqMap, "boardmgr.boardMgrUpdate");	  
            }            
 
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : "+ e.toString());
        }
        
        strUrl = CommonUtil.nvl(reqMap.get("returl"));
        strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );        

        return "redirect:" + strUrl;    
    }        
          
     
    /**
     * Method Summary. <br>
     *  게시판 삭제 처리 method.
     * @param actionMapping 액션맵핑 객체
     * @param actionForm 액션폼 객체
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @since 1.00
     * @see
     */
    @RequestMapping( value="/boffice/boardmgr/boardMgrDelete.do" )
    public String boardDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        Map reqMap = CommonUtil.getRequestMap( servletRequest );   
        String strUrl = CommonUtil.getNullTrans(reqMap.get("returl"), "/");

        try {
            servletRequest.setAttribute("reqMap", reqMap);
            Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "ADM");
            
            if (sesMap == null ) {

            	strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/");
            	strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );
            	
            	CommonUtil.alertLoginGoUrl(servletResponse, strUrl);
            	return null;  
            }            
            
            String strBrdNo = CommonUtil.getNullTrans(reqMap.get("brd_no"));
            
            String[] arrBrdNo = strBrdNo.split(",");
            UploadUtil upUtil = new UploadUtil(servletRequest);
            upUtil.setDbDao(dbSvc.m_dao); //DB연결자
            
            for(int nLoop=0; nLoop < arrBrdNo.length; nLoop++)
            {
            	Map paramMap = new HashMap();
            	paramMap.put("brd_no", arrBrdNo[nLoop]);
            	dbSvc.dbDelete(paramMap, "board.boardDelete");
            	
             
            	// 물리적인 파일 및 DB 정보삭제
            	paramMap.put("rel_tbl", mTableName);
            	paramMap.put("rel_key", arrBrdNo[nLoop]);
            	
            	upUtil.removeFile(paramMap);

            }
             
            String strParam = CommonUtil.nvl( reqMap.get( "param" ), "" );
            strParam = CommonUtil.removeParam(strParam, "brd_kind");
            
            strUrl = CommonUtil.nvl(reqMap.get("returl"));
            strUrl += "?" + strParam + "&brd_kind=" + CommonUtil.nvl(reqMap.get("brd_kind"));
            
            // strUrl = CommonUtil.nvl(reqMap.get("returl"));
            //strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );          
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardDelete() : "+ e.toString());
        }
        
        return "redirect:" + strUrl;   
        
    }    
         
  
}
