/*
 * Copyright (c) 2008 Sosun. All rights reserved.
 */
package com.ordadata.pack.org.biz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminBoardController  {

	@Resource(name="dbSvc")
	private DbController dbSvc;
	
	@Autowired
	SqlSession sqlSession;
	
	
	private String mTableName = "TB_BOARD";	// 테이블 명
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
    @RequestMapping( value="/boffice/boardList.do" )
    public String boardList(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "LIST";
        String strBrdCateCd = "--";
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}
        	
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"ADM");
        	reqMap.put("user_id",CommonUtil.nvl(userMap.get("USER_ID")));
        	reqMap.put("user_type",CommonUtil.nvl(userMap.get("USER_TYPE")));
        	String strUserType = CommonUtil.nvl(userMap.get("USER_TYPE"));        	
        	
        	int nPageRow     = dbSvc.getPageRowCount(reqMap, "page_row");       	
            int nRowStartPos = nPageRow * ( dbSvc.getPageNow(reqMap, "page_now") - 1 );  // Row의 시작위치        	
            
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD")); 
            	 strBrdCateCd = CommonUtil.nvl(brdMgrMap.get("CATE_CD"));
            	 
            	 if ("Y".equals(brdMgrMap.get("ORD_USE_YN"))) 
            	    reqMap.put("sort_ord", " ord ");
            	 
            	 if (!"".equals(strBrdCateCd)) {
            		 Map paramMap = new HashMap();
            		 paramMap.put("cd_type", strBrdCateCd);
            		 paramMap.put("not_comm_cd", "*");
            		 
            		 servletRequest.setAttribute( "cateList", dbSvc.dbList( paramMap, "code.codeList"));            		
            	 }            	 
            }
            	
            if ( "".equals(strBrdMgrno)) 
            	reqMap.put("brd_mgrno", "-1");
            
            reqMap.put("cate_cd", strBrdCateCd);                                      // 현재 페이지
            reqMap.put("page_row", nPageRow);                                      // 현재 페이지
            
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            servletRequest.setAttribute( "count",  Integer.toString(  dbSvc.dbCount( reqMap, "board.boardCount" ) ) );
            servletRequest.setAttribute( "list",   dbSvc.dbPagingList( reqMap, "board.boardList", nRowStartPos ,nPageRow) );           
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardList() : "+ e.toString()); 
        }
        
        
        return "boffice/board/skin" + strBrdSkin.toLowerCase() + "/boardList";
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
    
    @RequestMapping( value="/boffice/boardView.do" )
    public String boardView( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "VIEW";
        
        try {
        	
        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return null;
        	}        	
        	
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"ADM");
        	String strUserType = CommonUtil.nvl(userMap.get("USER_TYPE"));
        	
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD")); 
            	 servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            }       	
        	
        	Map detailMap = dbSvc.dbDetail(reqMap, "board.boardDetail");    	
            
            servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "dbMap", detailMap);
             
            reqMap.put("rel_tbl", mTableName);
            reqMap.put("rel_key", reqMap.get("brd_no"));
            
            String strRegNo = CommonUtil.nvl(reqMap.get("brd_no"));

            if (detailMap != null)
            {
            	reqMap.put("brd_mgrno", strBrdMgrno);
            	//servletRequest.setAttribute( "nextprevList", dbSvc.dbList(reqMap, "board.boardNextProvList"));
            }             
            
            if ( ! strRegNo.equals("")){
            	servletRequest.setAttribute( "lstFile",   dbSvc.dbList(reqMap, "common.getUploadFile") );
            }
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardView() : "+ e.toString());
        }
        
        //return  "boffice/board/skin" + strBrdSkin.toLowerCase() + "/boardView";
        return  "boffice/board/skincommon/boardView";
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
    @RequestMapping( value="/boffice/boardInput.do" )
    public String boardInput(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map detailMap = new HashMap();
        
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "WRITE";
        
        
        String strRegNo = CommonUtil.nvl(reqMap.get("brd_no"));
        String strIFlag = CommonUtil.nvl(reqMap.get("iflag"));        
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD"));
            	 
            	 if ("Y".equals(CommonUtil.nvl(brdMgrMap.get("URL_USE_YN")))) { // URL 선택시 타켓
            		 Map cParamMap = new HashMap();
            		 cParamMap.put("cd_type", "URLTGT");
            		 cParamMap.put("not_comm_cd", "*");
            		 servletRequest.setAttribute( "lstUrlTgt",   dbSvc.dbList(cParamMap, "code.codeList") );
            	 }            	 
            }        	 
            
            reqMap.put("iflag", CommDef.ReservedWord.INSERT);
      
            if ( !"".equals(strRegNo)){
            	
            	detailMap = dbSvc.dbDetail(reqMap, "board.boardDetail");
            	if ( detailMap != null)
            		reqMap.put("iflag", CommDef.ReservedWord.UPDATE);
            	
             	servletRequest.setAttribute( "reqMap", reqMap);
                
                reqMap.put("rel_tbl", mTableName);
                reqMap.put("rel_key", strRegNo);
                
            	servletRequest.setAttribute( "lstFile",   dbSvc.dbList(reqMap, "common.getUploadFile") );
            }
            
            servletRequest.setAttribute( "userList",   dbSvc.dbList( reqMap, "adminMember.userSelBoxList") );
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardInput() : "+ e.toString());
        }
        
        if ( strIFlag.equals(CommDef.ReservedWord.REPLY))
        {
        	reqMap.put("iflag", CommDef.ReservedWord.REPLY);
        }        
        
        servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
        servletRequest.setAttribute( "dbMap", detailMap);
        servletRequest.setAttribute( "reqMap", reqMap);
        
        //return  "boffice/board/skin" + strBrdSkin.toLowerCase() + "/boardWrite";
        return  "boffice/board/skincommon/boardWrite";
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
    @RequestMapping( value="/boffice/boardWork.do" )
    public String boardWork(HttpServletRequest servletRequest, 	HttpServletResponse servletResponse) throws Exception {
    	
    	Map reqMap = null;
    	String strBrdNo = "";
    	String strUrl = "";
    	UploadUtil uploadUtil = new UploadUtil(servletRequest, BRD_UPLOADPATH );
    	uploadUtil.setDbDao(dbSvc.m_dao); //DB연결자
    	
        String strContentType =  CommonUtil.getNullTrans(servletRequest.getHeader("Content-Type"));
        boolean isFileUp = (strContentType.indexOf("multipart/form-data") > -1) ? true :false;  // 첨부파일이 존재하는지 확인
        
        reqMap = ( isFileUp ) ? uploadUtil.getRequestMap(servletRequest):CommonUtil.getRequestMap( servletRequest );	
        
        String strFileOverSize = CommonUtil.nvl(reqMap.get("FILE_OVER_SIZE"));
        
        if ( !"".equals(strFileOverSize)) {
        	CommonUtil.alertMsgBack(servletResponse, strFileOverSize) ;
        	return null; 
        }
        
        
        Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "ADM");
        
        if (sesMap == null ) {
        	strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/");
        	strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );
        	
        	CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);  
        	return null;  
        }
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strSiteGb   = getArrayVal(reqMap);        
        
        try {
        	String strFlag    = CommonUtil.setNullVal(reqMap.get("iflag"), "");
        	
        	reqMap.put("rel_tbl", mTableName);
        	reqMap.put("reg_id", CommonUtil.nvl(sesMap.get("USER_ID")));
        	
        	if ( "".equals(CommonUtil.nvl(reqMap.get("reg_nm")))) {
        	   reqMap.put("reg_nm", CommonUtil.nvl(sesMap.get("USER_NM")));
        	}
        	
        	//reqMap.put("ctnt", LocalString(CommonUtil.nvl(reqMap.get("ctnt"))));
        	
        	
        	reqMap.put("site_gb", strSiteGb);
        	
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 Map brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 
            	 if (strSiteGb == null || "".equals(strSiteGb)) {
            	    strSiteGb = CommonUtil.nvl(brdMgrMap.get("SITE_GB"), "ALL");
            	    reqMap.put("site_gb", strSiteGb);    
            	 }
            }           	        	
            
            //reqMap.put("site_gb", strSiteGb);
            if ( CommDef.ReservedWord.INSERT.equals(strFlag) || CommDef.ReservedWord.REPLY.equals(strFlag)	) 
            {
            	strBrdNo =  Integer.toString(dbSvc.dbInt("board.boardNextValue"));
            	fillInsertMap(reqMap, strFlag, strBrdNo);
            	            	
        	    reqMap.put("rel_key", strBrdNo);
        	    reqMap.put("brd_no",  strBrdNo);
        	    
        	    dbSvc.dbInsert(reqMap, "board.boardInsert");	 
                
        	    dbSvc.dbUpdate(reqMap, "boardmgr.boardMgrRegCntAdd");
        
            } else if ( CommDef.ReservedWord.UPDATE.equals(strFlag)) 
            {
            	reqMap.put("rel_key", reqMap.get("brd_no"));   	
            	dbSvc.dbUpdate(reqMap, "board.boardUpdate");	  
                strBrdNo = CommonUtil.getNullTrans(reqMap.get("brd_no"));
                
                // 답변완료인 경우 상위 게시글도 답변완료로 처리함
                if ( "CERPR40".equals(reqMap.get("cate_cd"))) {
                	dbSvc.dbUpdate(reqMap, "board.boardProcUpdate"); 
                }
                
            }            
 
            if ( isFileUp ) // 파일처리
            	uploadUtil.uploadProcess(reqMap, servletRequest);
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : "+ e.toString());
        }
        
        strUrl = CommonUtil.nvl(reqMap.get("returl"));
        strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );        

        return "redirect:" + strUrl;    
    }        
         
   
    
    
    private String getArrayVal(Map reqMap) {
    	String[] arrVal;
    	String strCode = "";
    	try {
    		Object obj = (Object)reqMap.get("site_gb");
    		
    		if (obj instanceof String[]) {
    		
    		    arrVal = (String[])(Object)reqMap.get("site_gb");
        	
	    		for(int nLoop=0; nLoop < arrVal.length; nLoop++) {
	    			if ( !"".equals(strCode)) {
	    				strCode += ",";
	    			}
	    			
	    			strCode += arrVal[nLoop];
	    		}
    		} else if(obj instanceof List) {
    		    List arrList = (List)reqMap.get("site_gb");
            	
	    		for(int nLoop=0; nLoop < arrList.size(); nLoop++) {
	    			
	    			String strVal = (String)arrList.get(nLoop);
	    			
	    			if ( !"".equals(strCode)) {
	    				strCode += ",";
	    			}
	    			
	    			strCode += strVal;
	    		}

    		} else {
    			strCode = (String)reqMap.get("site_gb");
    		}
    	} catch ( Exception e) {
    		System.out.print(e.toString());
    		
    		strCode = (String)reqMap.get("site_gb");
    	}
    	
    	if ("".equals(strCode))
    		strCode = "ALL";
    	
    	return strCode;
    }
    
    /**
     * Method Summary. <br>
     * 관리자 게시판 입력 파리미터 처리 method
     * @param reqMap 파라미터
     * @param strFlag 처리 flag
     * @param strNextVal
     * @since 1.00
     * @see
     */
    private void fillInsertMap(Map reqMap, String strFlag, String strNextVal)
    {
    	reqMap.put("brd_reg_no", strNextVal);
    	if ("REPLY".equals(strFlag))
    	{
    		Map paramMap = new HashMap();
    		paramMap.put("brd_no", reqMap.get("old_brd_reg_no"));
    		Map viewMap  = dbSvc.dbDetail(paramMap, "board.boardDetail");
    		
    		int    nDepth      = Integer.parseInt(CommonUtil.getNullTrans(viewMap.get("DEPTH"), "0")) + 1;
    		String strAllBrdNo = CommonUtil.getNullTrans(viewMap.get("ALL_REG_NO")) + strNextVal + ",";
    		
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
    @RequestMapping( value="/boffice/boardDelete.do" )
    public String boardDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        Map reqMap = CommonUtil.getRequestMap( servletRequest );   
        String strUrl = CommonUtil.getNullTrans(reqMap.get("returl"), "/");

        String strParam = CommonUtil.nvl( reqMap.get( "param" ), "" );
        strParam = CommonUtil.removeParam(strParam, "brd_kind");
        
        strUrl = CommonUtil.nvl(reqMap.get("returl"));
        strUrl += "?" + strParam + "&brd_kind=" + CommonUtil.nvl(reqMap.get("brd_kind"));
        
        try {
            servletRequest.setAttribute("reqMap", reqMap);
            Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "ADM");
            
            if (sesMap == null ) {
            	strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/");
            	strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );
            	
            	CommonUtil.alertLoginGoUrl(servletResponse, strUrl);
            	return null;  
            }            
            
            String strBrdNo    = CommonUtil.getNullTrans(reqMap.get("brd_no"));
            String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
            
            String[] arrBrdNo = strBrdNo.split(",");
            UploadUtil upUtil = new UploadUtil(servletRequest);
            upUtil.setDbDao(dbSvc.m_dao); //DB연결자
            
            if ( !"".equals(strBrdMgrno))  // 게시판의 스킨을 조회함
            {
            	 Map brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 if ("DB".equals(CommonUtil.nvl(brdMgrMap.get("DELFLAG"))))  // DB Flag만 변경을 하는 경우
            	 {
                     for(int nLoop=0; nLoop < arrBrdNo.length; nLoop++)
                     {
                     	Map paramMap = new HashMap();
                     	paramMap.put("brd_no", arrBrdNo[nLoop]);
                     	paramMap.put("brd_mgrno", strBrdMgrno);  
                     	
                     	dbSvc.dbUpdate(paramMap, "board.boardDelflagUpdate");
                     	dbSvc.dbUpdate(paramMap, "boardmgr.boardMgrRegCntSub");
                     } 
            		  
            		 return "redirect:" + strUrl; 
            	 }
            }                   
   

            
            for(int nLoop=0; nLoop < arrBrdNo.length; nLoop++)
            {
            	Map paramMap = new HashMap();
            	paramMap.put("brd_no", arrBrdNo[nLoop]);
            	dbSvc.dbDelete(paramMap, "board.boardDelete");
            	
             
            	// 물리적인 파일 및 DB 정보삭제
            	paramMap.put("rel_tbl", mTableName);
            	paramMap.put("rel_key", arrBrdNo[nLoop]);
            	
            	upUtil.removeFile(paramMap);
            	
            	dbSvc.dbUpdate(reqMap, "boardmgr.boardMgrRegCntSub");
            }
             
            

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardDelete() : "+ e.toString());
        }
        
        return "redirect:" + strUrl;   
    }    
    
    
}
