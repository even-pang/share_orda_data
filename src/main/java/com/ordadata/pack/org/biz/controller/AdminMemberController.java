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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.ClientInfo;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SHA256Encode;
import com.ordadata.pack.charm.util.SessionUtil;


@Controller
public class AdminMemberController  {

	@Resource(name="dbSvc")
	private DbController dbSvc;	
	
	private String mTableName = "TB_USER";	
    private String BRD_UPLOADPATH = "/upload/board/";


    @RequestMapping( value="/boffice/member/memberList.do" )
    public String memberList(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"ADM");
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}

        	int nPageRow     = dbSvc.getPageRowCount(reqMap, "page_row");       	
            int nRowStartPos = nPageRow * ( dbSvc.getPageNow(reqMap, "page_now") - 1 );  
            
            reqMap.put("page_row", nPageRow);                                      
            reqMap.put("user_id", CommonUtil.nvl(userMap.get("USER_ID")));
            reqMap.put("user_type", CommonUtil.nvl(userMap.get("USER_TYPE")));
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "count",  Integer.toString(  dbSvc.dbCount( reqMap, "adminMember.userCount" ) ) );
            servletRequest.setAttribute( "list",   dbSvc.dbList( reqMap, "adminMember.userList", nRowStartPos ,nPageRow) );           
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".memberList() : "+ e.toString()); 
        }
                
        return "boffice/member/memberList";
    }
    
    @RequestMapping( value="/boffice/member/memberUpdate.do" )
    public String memberUpdate( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "VIEW";
        
        try {
        	
        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"ADM");
        	reqMap.put("user_id", userMap.get("user_id"));
        	Map detailMap = dbSvc.dbDetail(reqMap, "member.userDetail");    	
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "dbMap", detailMap);
              
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".memberWrite() : "+ e.toString());
        }
        
      
        return  "boffice/member/memberDetail";
    }
    
    
    
    @RequestMapping( value="/boffice/member/memberInput.do" )
    public String memberInput( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "VIEW";
        
        try {
        	
        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	
        	
        	Map detailMap = dbSvc.dbDetail(reqMap, "member.userDetail");    	
        	if(!detailMap.isEmpty()) {
        		
        		reqMap.put("iflag",CommDef.ReservedWord.UPDATE);
        	}
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "dbMap", detailMap);
              
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".memberWrite() : "+ e.toString());
        }
        
      
        return  "boffice/member/memberWrite";
    }
    

    @RequestMapping( value="/boffice/memberModifyForm.do" )
    public String memberModifyForm( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "VIEW";
        
        try {
        	
        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	
            if ( !"".equals(strBrdMgrno)) 
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD")); 
            	 servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            }       	
        	
        	Map detailMap = dbSvc.dbDetail(reqMap, "member.userAdminDetail");    	
        	
        	Map paramMap = new HashMap();
        	paramMap.put("mod", "Y");
            
            servletRequest.setAttribute( "reqMap", reqMap);
            servletRequest.setAttribute( "dbMap", detailMap);
              
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".memberWrite() : "+ e.toString());
        }
        
        //return  "boffice/board/skin" + strBrdSkin.toLowerCase() + "/boardView";
        return  "boffice/member/memberModify";
    }
    
	
    @RequestMapping( value="/boffice/memberModify.do" )
    public String memberModify(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        servletRequest.setAttribute( "reqMap",        reqMap );
        String strCode = "OK";
        
        String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
        
    	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
    		return "";
    	}  
        try {
	    	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");

	        if (!CommonUtil.nvl(reqMap.get("pwd")).equals("")) { 
		        String pwdSha = SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd")));
		        reqMap.put("pwd", pwdSha);
	        }
	        
	     
	        
	    	dbSvc.dbUpdate(reqMap, "adminMember.adminUserUpdate");
            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
        CommonUtil.displayText(servletResponse, strCode);
       
        return null;
    }	     
          
  
    
    @RequestMapping( value="/boffice/memberWriteForm.do" )
    public String memberWriteForm( HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map brdMgrMap = new HashMap();
        
        String strBrdMgrno = CommonUtil.nvl(reqMap.get("brd_mgrno"));
        String strBrdSkin  = "VIEW";
        
        try {
        	
        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}        	
        	
            if ( !"".equals(strBrdMgrno)) 
            {
            	 brdMgrMap = dbSvc.dbDetail(reqMap,  "boardmgr.boardMgrDetail");
            	 strBrdSkin = CommonUtil.nvl(brdMgrMap.get("BRD_SKIN_CD")); 
            	 servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            }       	
            servletRequest.setAttribute( "brdMgrMap", brdMgrMap);
            servletRequest.setAttribute( "reqMap", reqMap);
              
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".memberWrite() : "+ e.toString());
        }
        
        //return  "boffice/board/skin" + strBrdSkin.toLowerCase() + "/boardView";
        return  "boffice/member/memberWrite";
    }
        
	
    @RequestMapping( value="/boffice/memberWrite.do" )
    public String memberWrite(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        int nExists = -1;
        String strCode = "DUP";
	    
        try {
    		
        	String userIp = ClientInfo.getClntIP(servletRequest); 
        	
        	
        	
        	if(CommonUtil.nvl(reqMap.get("iflag")).equals("INSERT")) {
    			reqMap.put("pwd", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
    			dbSvc.dbInsert(reqMap,  "member.userInsert");
    			int[] menuArr = {838,849,850,839,841,842,851,849,845};
    			for(int i = 0; i < menuArr.length; i ++) {
    				Map paramMap = new HashMap();
    				paramMap.put("user_id", CommonUtil.nvl(reqMap.get("user_id")));
    				paramMap.put("menu_no", menuArr[i]);
    				dbSvc.dbInsert(paramMap, "adminMember.userMenuInsert");
    			}
    			strCode="OK";
    		}else if(CommonUtil.nvl(reqMap.get("iflag")).equals("UPDATE")) {
    			if(CommonUtil.nvl(reqMap.get("pwdupdate")).equals("1")) {
    				reqMap.put("pwd", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
    			}
    			dbSvc.dbUpdate(reqMap,  "member.userUpdate");
    			strCode="UPDATE";
    		}
    		
    		
        	
            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
        CommonUtil.displayText(servletResponse, strCode);
       
        return null;
    }       
    

    

    @RequestMapping( value="/boffice/memberAuth.do" )
    public String memberAuth(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
 
        Map brdMgrMap = new HashMap();
        
        try {

        	if ( !CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
        		return "";
        	}
        	
        	reqMap.put("menu_gb","ADMIN");
        	
            servletRequest.setAttribute( "reqMap", reqMap);

            reqMap.put("sort_ord", " A.top_menu_no, A.menu_lvl, A.ord, A.full_menu_no " );
   		    List rsList = dbSvc.dbList(reqMap, "adminMember.menuList"); 
   		    servletRequest.setAttribute("treeMenu", CommonUtil.makeMenuXml(servletRequest, rsList)); 
   		    List rsMenuList = dbSvc.dbList(reqMap, "adminMember.menuAuthList"); 
   		    servletRequest.setAttribute( "menuList", rsMenuList);
   		    
   		    servletRequest.setAttribute( "userMap", dbSvc.dbDetail(reqMap, "member.userDetail"));
          
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".contentsList() : "+ e.toString());
        }

        return  "boffice/member/memberAuth";
    }
    
    
    @RequestMapping( value="/boffice/member/memberDelete.do" )
    public String postDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
    	
    	Map reqMap = CommonUtil.getRequestMap( servletRequest );	
    	String strBrdNo = "";
    	String strUrl = "";
    	
        Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest, "ADM");
        
        if (sesMap == null ) {
        	strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/");
        	strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );
        	CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);  
        	return null;  
        }
        
        try {
			dbSvc.dbDelete(reqMap, "member.memberDelete");
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".memberDelete() : "+ e.toString());
        }
        
        strUrl = CommonUtil.nvl(reqMap.get("returl"));
        strUrl += "?" + CommonUtil.nvl( reqMap.get( "param" ), "" );        

        return "redirect:/boffice/member/memberList.do";    
    }
    
}

