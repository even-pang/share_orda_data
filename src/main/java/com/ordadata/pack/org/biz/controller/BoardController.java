/*
 * Sosun. All rights reserved.
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
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SessionUtil;
import com.ordadata.pack.charm.util.UploadUtil;

import com.ordadata.pack.charm.util.*;

/**
 * Class Summary. <br>
 * 게시판 관리 class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */

@Controller
public class BoardController   {

	@Resource(name="dbSvc")
	private DbController dbSvc;	
	
	private String mTableName = "TB_BOARD";	// 테이블 명
    private String BRD_UPLOADPATH = "/upload/board/";
    private String[] gArrWord = {"REG_NM"};

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
    @RequestMapping( value="/home/boardList.do" )
    public String boardList(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {


        HttpSession session = servletRequest.getSession( false );
        Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "USR");
        if(sesMap == null) sesMap = new HashMap();
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        Map brdMgrMap = new HashMap();
        List rsList;
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "LIST";
        String strBrdCateCd= "--";
        String strBrdSecret  = "N";
        
        int  nBrdListCnt  =  dbSvc.PAGE_ROWCOUNT;
		
        try {

        	reqMap.put("user_id",CommonUtil.nvl(sesMap.get("USER_ID")));
        	reqMap.put("user_type",CommonUtil.nvl(sesMap.get("USER_TYPE")));
        	// 비밀게시판 권한 점검
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
        	
         	if ( userMap == null ) {
         	   userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"REALUSR");  // GPIN
         	}            	
        	
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 brdMgrMap    = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin   = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD"));
            	 nBrdListCnt  = CommonUtil.getNullInt(brdMgrMap.get("LIST_CNT"), dbSvc.PAGE_ROWCOUNT);
            	 
            	 strBrdCateCd = CommonUtil.nvl(brdMgrMap.get("CATE_CD"));  
            	 strBrdSecret = CommonUtil.nvl(brdMgrMap.get("SECRET_USE_YN"));
            	 String strSiteGb = CommonUtil.nvl(brdMgrMap.get("SITE_GB"));
            	 
            	 if ( "ALL".equals(strSiteGb)) {
            		 Map menuMap    = dbSvc.dbDetail(reqMap,  "menu.menuDetail");
    
            		 if (menuMap != null ) {
            			 reqMap.put("site_gb", CommonUtil.nvl(menuMap.get("MENU_GB"))); 
            		 }            	      
            	 }
            	 
            	 if (!"".equals(strBrdCateCd)) {
            		 Map paramMap = new HashMap();
            		 paramMap.put("cd_type", strBrdCateCd);
            		 paramMap.put("not_comm_cd", "*");
            		 
            		 servletRequest.setAttribute( "cateList", dbSvc.dbList( paramMap, "code.codeList"));
            		 
            	 }
            }
            
            reqMap.put("cate_cd", strBrdCateCd);
            
        	int nPageRow     = dbSvc.getPageRowCount(reqMap, "page_row", nBrdListCnt);       	
            int nRowStartPos = nPageRow * ( dbSvc.getPageNow(reqMap, "page_now") - 1 );  // Row의 시작위치  
            
            if ( "".equals(strBrdMgrno))  {
            	reqMap.put("brd_mgrno", "-1");
            	
         	    CommonUtil.alertMsgGoUrl(servletResponse, "게시판 정보가 없습니다.","/");
        	    return null;
            } 
            
            reqMap.put("page_row", nPageRow);    
            reqMap.put("use_yn", "Y");          
                
            reqMap.put("front_yn", 'Y'); //home인지 admin인지 구분
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            servletRequest.setAttribute( "count",  Integer.toString(  dbSvc.dbCount( reqMap, "board.boardCount" ) ) );
            
            rsList = dbSvc.dbPagingList( reqMap, "board.boardList", nRowStartPos ,nPageRow) ;
            
            Map childMap =  dbSvc.dbDetail(reqMap, "menu.menuFirstChild"); //Child 메뉴가 있으면 가져오기
            if(childMap != null) {
            	reqMap.put("menu_no", childMap.get("MENU_NO"));
            }
            
            servletRequest.setAttribute( "list",      rsList); 
            servletRequest.setAttribute( "menuMap", dbSvc.dbDetail(reqMap, "menu.menuDetail"));
  
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardList() : "+ e.toString()); 
        }
        
       	System.out.println("returnUrl = " +getMenuGb(reqMap) + "/board/skin" + strBrdSkin.toLowerCase() + "/boardList");
       	return getMenuGb(reqMap) + "/board/skin" + strBrdSkin.toLowerCase() + "/boardList";
    }
    
    public String getMenuGb(Map reqMap) {
    	 Map menuMap   = new HashMap();
    	 String strMenuGb = "home";
    	 
    	try {
    		menuMap = dbSvc.dbDetail(reqMap,  "menu.menuDetail");
    		
    		if ( menuMap != null) {
    			strMenuGb = CommonUtil.nvl(menuMap.get("MENU_GB"));
            }
    	} catch ( Exception e) {
    		System.out.println(e.toString());
    	}
        
    	return strMenuGb.toLowerCase();
        
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
    
    @RequestMapping( value="/home/boardView.do" )
    public String boardView( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "LIST";
        
        try {
        	
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD"));
            	 servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            }       	 
        	
            if ("".equals(CommonUtil.nvl(reqMap.get("brd_no")))) { // View 화면에서 페이지를 이동하는 경우 해당 페이지의 첫번째 게시판 번호를 조회한다.
               reqMap.put("brd_no", getPageBoardNo(servletRequest, servletResponse, brdMgrMap));	
            }
            
        	//Map detailMap = dbSvc.dbDetail(reqMap, "board.boardDetail");
        	List detailList = dbSvc.dbList(reqMap, "board.boardDetailList");
        	
        	Map detailMap = null;
            if(detailList != null && detailList.size() >= 1) {
            	detailMap = (Map) detailList.get(0);
            }
 
        	// 비밀글 허용여부 확인
        	String strSecretYn = CommonUtil.nvl(detailMap.get("SECRET_YN"));
        	if ("Y".equals(strSecretYn)) {
        		Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
        		
        	 	if ( userMap == null ) {
        	    	userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"REALUSR");  // GPIN
        	    }            		
        		
        		if ( userMap == null || userMap.isEmpty()) {
       				CommonUtil.alertMsgBack(servletResponse, "비밀글입니다. 로그인을 하여 주십시오.");
       				
        		} else {
        		   reqMap.put("reg_id",    CommonUtil.nvl(userMap.get("USER_ID")));
        		   reqMap.put("user_gpin", CommonUtil.nvl(userMap.get("USER_GPIN")));
        		}
        		
        		if ( dbSvc.dbCount( reqMap, "board.isSecretCheck" ) <= 0 )
        		{
        			CommonUtil.alertMsgBack(servletResponse, "비밀번호 또는 회원 정보가 일치하지 않습니다.");
        			return null;
        		}
        	}
        	
        	servletRequest.setAttribute( "menuMap", dbSvc.dbDetail(reqMap, "menu.menuDetail"));
            servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "dbMap", detailMap);
            servletRequest.setAttribute( "dbList", detailList);
             
            reqMap.put("rel_tbl", mTableName);
            reqMap.put("rel_key", reqMap.get("brd_no"));
            
            String strRegNo = CommonUtil.getNullTrans(reqMap.get("brd_no"));

            if (detailMap != null)
            {
            	dbSvc.dbUpdate(reqMap, "board.boardAddViewCntUpdate"); // View 시 1씩 증가
            	reqMap.put("brd_mgrno", strBrdMgrno);
//            	servletRequest.setAttribute( "nextprevList", dbSvc.dbList(reqMap, "board.boardNextProvList"));
            }             
            
            if ( ! strRegNo.equals("")){
            	servletRequest.setAttribute( "lstFile",   dbSvc.dbList(reqMap, "common.getUploadFile") );
            }
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardView() : "+ e.toString());
        }
       
        return  getMenuGb(reqMap) + "/board/skin" + strBrdSkin.toLowerCase() + "/boardView";
    }
    
    public String getPageBoardNo( HttpServletRequest servletRequest, 
    		                      HttpServletResponse servletResponse,
    		                      Map brdMgrMap) throws Exception 
    {
    	Map reqMap = CommonUtil.getRequestMap( servletRequest );
    	String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
    	String strBrdNo = "";
    	try {
	        int nBrdListCnt      = CommonUtil.getNullInt(brdMgrMap.get("LIST_CNT"), CommDef.PAGE_ROWCOUNT);
	    	int nPageRow     = dbSvc.getPageRowCount(reqMap, "page_row", nBrdListCnt);       	
	        int nRowStartPos = nPageRow * ( dbSvc.getPageNow(reqMap, "page_now") - 1 );  // Row의 시작위치  
	        
	        if ( "".equals(strBrdMgrno)) 
	        	reqMap.put("brd_mgrno", "-1");
	        
	        reqMap.put("page_row", nPageRow);    
	        reqMap.put("use_yn", "Y");         
	        
	        List rsList = dbSvc.dbPagingList( reqMap, "board.boardList", nRowStartPos ,nPageRow); 
	        if (rsList != null && !rsList.isEmpty()) {
	        	Map rsMap = (Map)rsList.get(0);
	        	
	        	strBrdNo = CommonUtil.nvl(rsMap.get("BRD_NO"));
	        	
	        }

    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}

    	return strBrdNo;
    
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
    @RequestMapping( value="/home/boardInput.do" )
    public String boardInput(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap    = CommonUtil.getRequestMap( servletRequest );
        Map detailMap = new HashMap();
        
        String strFlag = CommonUtil.nvl(reqMap.get("iflag"), CommDef.ReservedWord.INSERT);
        
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "LIST";        
        String strBrdAuth  = "";
        
        try {
        	
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD")); 
            	 strBrdAuth = CommonUtil.nvl(brdMgrMap.get("WRITE_AUTH_CD"));
            	 
            	 servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            } 
 
 
        	// 비밀게시판 권한 점검
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
         	if ( userMap == null ) {
          	   userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"REALUSR");  // GPIN
          	}              
            
        	
            if ( !writeAuthCheck(servletRequest, servletResponse, strBrdAuth) ) {
            	return null;
            }
        	
            String strRegNo = CommonUtil.getNullTrans(reqMap.get("brd_no"));
     
            if ( !"".equals(strRegNo)){
            	
            	detailMap = dbSvc.dbDetail(reqMap, "board.boardDetail");
            	
            	if (!CommDef.ReservedWord.REPLY.equals(strFlag)) 
            	{
	            	if ( detailMap != null)
	            		reqMap.put("iflag", CommDef.ReservedWord.UPDATE);
	                
	            	Map paramMap = new HashMap();
	            	
	            	paramMap.put("rel_tbl", mTableName);
	            	paramMap.put("rel_key", strRegNo);
	                
	            	servletRequest.setAttribute( "lstFile",   dbSvc.dbList(paramMap, "common.getUploadFile") );
            	}
            	
             	servletRequest.setAttribute( "reqMap", reqMap);            	
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardInput() : "+ e.toString());
        }
        
        servletRequest.setAttribute( "menuMap", dbSvc.dbDetail(reqMap, "menu.menuDetail"));
        servletRequest.setAttribute( "dbMap", detailMap);
        servletRequest.setAttribute( "reqMap", reqMap);
        
        return  getMenuGb(reqMap) + "/board/skin" + strBrdSkin.toLowerCase() + "/boardWrite";
    }    
  
    private boolean writeAuthCheck( HttpServletRequest servletRequest, HttpServletResponse servletResponse, String strBrdAuth) {
    	boolean bRet = false;
    	try {    	
		    	if (strBrdAuth == null || "".equals(strBrdAuth) || CommDef.Board.BRD_NOWRITE.equals(strBrdAuth)) {
		    		CommonUtil.alertMsgBack(servletResponse, "글쓰기 권한이 없습니다.");
		    		return false;
		    	}

		    	//##@@ Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
		    	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
		     	if ( userMap == null ) {
		         	   userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"REALUSR");  // GPIN
		         }  	    	
		    	
		    	if ( userMap == null || userMap.isEmpty()) {
		    		CommonUtil.alertMsgBack(servletResponse, "로그인을 하여 주십시오");
		    		return false;
		    	}
		    	
		    	String strUserType =  CommonUtil.nvl(userMap.get("USER_TYPE"));

		    	if ( "ALL".equals(strBrdAuth))
		    		return true;
		    	
		    	if ( strBrdAuth.equals(strUserType) || "ADMIN".equals(strUserType))
		    		return true;    	
				    	
		    	CommonUtil.alertMsgBack(servletResponse, "글쓰기 권한이 없습니다.");
		    	
    	} catch ( Exception e) {
    		System.out.println(e.toString());
    	}
    	
    	return false;
    	    	
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
    @RequestMapping( value="/home/boardWork.do" )
    public String boardWork(HttpServletRequest servletRequest, 	HttpServletResponse servletResponse) throws Exception {
    	
    	Map reqMap = null;
    	String strBrdNo = "";
    	String strUrl = "";
    	UploadUtil uploadUtil = new UploadUtil(servletRequest, BRD_UPLOADPATH );
        
    	uploadUtil.setDbDao(dbSvc.m_dao); //DB연결자
    	
        String strContentType =  CommonUtil.getNullTrans(servletRequest.getHeader("Content-Type"));
        boolean isFileUp = (strContentType.indexOf("multipart/form-data") > -1) ? true :false;  // 첨부파일이 존재하는지 확인
        
        reqMap = ( isFileUp ) ? uploadUtil.getRequestMap(servletRequest):CommonUtil.getRequestMap( servletRequest );	

    	//##@@ Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
    	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
     	if ( userMap == null ) {
      	   userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"REALUSR");  // GPIN
      	}           
        
     	if ( userMap == null || userMap.isEmpty() )
     	{
	        if ( !isLogin(servletRequest, servletResponse, reqMap))
	        	return null; 
     	}
    	
        String strUserId   = CommonUtil.nvl(userMap.get("USER_ID"));
        String strUserNm   = CommonUtil.nvl(userMap.get("USER_NM"));
        String strUserGpin = CommonUtil.nvl(userMap.get("USER_GPIN"));
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strSiteGb   = "ALL";
        String strBrdSkin  = "LIST";
        
        try {
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 Map brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 if ("Y".equals(CommonUtil.nvl(brdMgrMap.get("WRITER_USE_YN")))) {
            		 strUserNm = CommonUtil.nvl(reqMap.get("reg_nm")); 
            	 }
            	 
            	 strSiteGb = CommonUtil.nvl(brdMgrMap.get("SITE_GB"), "ALL");
            }           	
        	
        	String strFlag    = CommonUtil.setNullVal(reqMap.get("iflag"), "");
 
        	reqMap.put("rel_tbl", mTableName);
        	reqMap.put("reg_id", strUserId);
        	reqMap.put("reg_nm", strUserNm);
        	reqMap.put("site_gb", strSiteGb);        	
        	reqMap.put("user_gpin", strUserGpin); 
        	
        		
            if ( CommDef.ReservedWord.INSERT.equals(strFlag) || CommDef.ReservedWord.REPLY.equals(strFlag)	) 
            {
            	strBrdNo = Integer.toString(dbSvc.dbInt("board.boardNextValue"));
            	fillInsertMap(reqMap, strFlag, strBrdNo);
            	
            	/*if ( "".equals(CommonUtil.getNullTrans(reqMap.get( "brd_kind"))))
        	        reqMap.put("brd_kind", "NO_BOARD" );*/
            	
        	    reqMap.put("rel_key", strBrdNo);
        	    reqMap.put("brd_no",  strBrdNo);
        	    
        	    dbSvc.dbInsert(reqMap, "board.boardInsert");	 
                
        	    dbSvc.dbUpdate(reqMap, "boardmgr.boardMgrRegCntAdd");

            } else if ( CommDef.ReservedWord.UPDATE.equals(strFlag)) 
            {
            	if (!isChildData(servletResponse, reqMap))
            		return null;
            	
            	reqMap.put("rel_key", reqMap.get("brd_no"));   	
            	dbSvc.dbUpdate(reqMap, "board.boardUserUpdate");	  
                strBrdNo = CommonUtil.getNullTrans(reqMap.get("brd_no"));
            }            
 
            if ( isFileUp ) // 파일처리
            	uploadUtil.uploadProcess(reqMap, servletRequest);           
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : "+ e.toString());
        }
        
        if ("125".equals(strBrdMgrno)) {
        	CommonUtil.alertMsgGoUrl(servletResponse, "문의 및 개선의견을 정상적으로 저장하였습니다.", "/home/boardInput.do?brd_mgrno=125&menu_no=186")  ;
        	return null;
        } else {
	        strUrl = CommonUtil.nvl(reqMap.get("returl"));
	        strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );
	        return "redirect:" + strUrl;
        }            
    }        
          
    private boolean isChildData(HttpServletResponse servletResponse, Map reqMap) {
    	boolean bFlag = false;
    	
    	try {
    		int nCnt = dbSvc.dbCount(reqMap, "board.isChildCount");
    		if ( nCnt > 0 ) {
    		   CommonUtil.alertMsgBack(servletResponse, "답글에 존재하여 삭제 또는 수정을 할 수 없습니다.");
    		} else {
    			bFlag = true;
    		}
    		
    	} catch ( Exception e) {
    		System.out.println(e.toString());
    	}
    	
    	return bFlag;
    	
    }
    
    
    /**
     * Method Summary. <br>
     *  게시판 입력 파리미터 처리 method
     * @param reqMap 파라미터
     * @param strFlag 처리 flag
     * @param strNextVal
     * @since 1.00
     * @see
     */
    private void fillInsertMap(Map reqMap, String strFlag, String strNextVal)
    {
    	reqMap.put("brd_no", strNextVal);
    	if ("REPLY".equals(strFlag))
    	{
    		Map paramMap = new HashMap();
    		paramMap.put("brd_no", reqMap.get("old_brd_reg_no"));
    		Map viewMap  = dbSvc.dbDetail(paramMap, "board.boardDetail");
    		
    		int    nDepth      = Integer.parseInt(CommonUtil.nvl(viewMap.get("DEPTH"), "0")) + 1;
    		String strAllBrdNo = CommonUtil.nvl(viewMap.get("ALL_REG_NO")) + strNextVal + ",";
    		
    		reqMap.put("depth", nDepth);
    		reqMap.put("all_reg_no", strAllBrdNo);
    		reqMap.put("top_reg_no",  viewMap.get("TOP_REG_NO"));
    		reqMap.put("high_reg_no", reqMap.get("old_brd_reg_no"));
    		
    	} else {
    		reqMap.put("depth", "1");
    		reqMap.put("all_reg_no",  strNextVal + ",");
    		reqMap.put("top_reg_no",  strNextVal);
    		reqMap.put("high_reg_no", "0");
    	}
  
    	
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
    @RequestMapping( value="/home/boardDelete.do" )
    public String boardDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        Map reqMap = CommonUtil.getRequestMap( servletRequest );   
        String strUrl = CommonUtil.getNullTrans(reqMap.get("returl"), "/");
        boolean bUsrGpinSesson = false;
        
        try {
            servletRequest.setAttribute("reqMap", reqMap);
            
            Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "USR");
         	if ( sesMap == null ) {
         		sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"REALUSR");  // GPIN
         		bUsrGpinSesson = true;
          	}  
         	
            String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
        
            if ( sesMap ==  null ) {
                isLogin(servletRequest, servletResponse, reqMap);
                return null;
            }
            
            
        	if (!isChildData(servletResponse, reqMap))
        		return null;
        	
            String strBrdNo = CommonUtil.getNullTrans(reqMap.get("brd_no"));
            
            String[] arrBrdNo = strBrdNo.split(",");
            UploadUtil upUtil = new UploadUtil(servletRequest);
            upUtil.setDbDao(dbSvc.m_dao); //DB연결자
            
            for(int nLoop=0; nLoop < arrBrdNo.length; nLoop++)
            {
            	Map paramMap = new HashMap();
            	paramMap.put("brd_no", arrBrdNo[nLoop]);
            	paramMap.put("reg_id", CommonUtil.nvl(sesMap.get("USER_ID")));
            	
            	if ( bUsrGpinSesson ) {     // G-PIN으로 로그인을  한 경우       	
            		 paramMap.put("user_gpin", CommonUtil.nvl(sesMap.get("USER_GPIN")));
            	     dbSvc.dbDelete(paramMap, "board.boardUserGpinDelete");
            	} else {
            		 dbSvc.dbDelete(paramMap, "board.boardUserDelete");
            	}
 
             
            	// 물리적인 파일 및 DB 정보삭제
            	paramMap.put("rel_tbl", mTableName);
            	paramMap.put("rel_key", arrBrdNo[nLoop]);
            	
            	upUtil.removeFile(paramMap);
            	dbSvc.dbUpdate(reqMap, "boardmgr.boardMgrRegCntSub");
            }
              
            String strParam = CommonUtil.nvl( reqMap.get( "param" ), "" );
            strParam = CommonUtil.removeParam(strParam, "brd_kind");
            strUrl += "?" + strParam;
            
                
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardDelete() : "+ e.toString());
        }
        
        return "redirect:" + strUrl;    
        
    }    
    
     private boolean isLogin(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Map reqMap) {

    	 try {
        	    String strHpKind = getMenuGb(reqMap);
        	    String strUrl = "";
        	    
        		Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
            	if ( userMap == null || userMap.isEmpty()) {
            		
                    String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
                    String strBrdSkin  = "LIST";
                        
                    if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
                    {
                    	 Map brdMgrMap    = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
                    	 strBrdSkin   = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD"));
                    }            		
                    
                    if ("home".equals(strHpKind)) {
                    	strUrl= CommDef.LOGIN_PAGE_HOME;	
                    } 
                    
                    String strRetUrl = servletRequest.getRequestURL().toString();
            		strRetUrl += "?menu_no="   + CommonUtil.nvl( reqMap.get( "menu_no" ));
            		strRetUrl += "&brd_mgrno=" + CommonUtil.nvl( reqMap.get( "brd_mgrno" ));
        	    	
        	    	CommonUtil.alertLoginGoUrl(servletResponse, strUrl, strRetUrl);
        	    	
        	    	return false;
            	}        
               
    	 } catch (Exception e) {
    		 System.out.println(e.toString());
    	 }
    	 
    	 return true;
    } 
  
}
