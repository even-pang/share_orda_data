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
import com.ordadata.pack.charm.util.ClientInfo;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.LoginManager;
import com.ordadata.pack.charm.util.SHA256Encode;
import com.ordadata.pack.charm.util.SessionUtil;

/**
 * Class Summary. <br>
 * 회원 관리 class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */

@Controller
public class MemberController   {

	@Resource(name="dbSvc")
	private DbController dbSvc;

	 /**
     * Method Summary. <br>
     * 로그인  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/home/login.do" )
    public String homeLogin(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        servletRequest.setAttribute( "reqMap",        reqMap );
        
        return "home/member/login";
    }	
	
	 /**
     * Method Summary. <br>
     * 로그아웃  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/home/logout.do" )
    public String homeLogout(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        servletRequest.setAttribute( "reqMap",        reqMap );
        
        return "home/member/logout";
    }	    
    
	 /**
     * Method Summary. <br>
     * 로그인  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/loginCheck.do" )
    public String  loginCheck(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        String strSite  	= CommonUtil.nvl(reqMap.get("site"), "home");
        String strPwd 	    = SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"), ""));
		/*
		 * String strPwd2 = SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd2"), ""));
		 */
        
        String returnUrl 	= CommonUtil.nvl(reqMap.get("returnurl"), "/" + strSite + "/index.do");        
        String strMenuNo 	= CommonUtil.nvl(reqMap.get("menu_no"));
         
        String strCode ="ERROR"; 
        
        String strLoginUrll = "/front/" + strSite + "/login.do?menu_no=" + strMenuNo;
        
        String Msg = "아이디 또는 비밀번호를 확인해주세요.";
        try {
    		        	
        	String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
        	
			Map userMap = dbSvc.dbDetail(reqMap, "member.userDetail");

			// 비밀번호가 일치하는지 확인한다.
			 if (userMap != null) {
				 String strDBPwd = CommonUtil.nvl(userMap.get("PWD"));
					/* String strDBPwd2 = CommonUtil.nvl(userMap.get("PWD2")); */
				 if ( !strPwd.equals(strDBPwd))  {  // 비밀번호가 일치하지 않는 경우
					 userMap = null;  
					 Msg = "아이디 또는 비밀번호를 확인해주세요.";
					} 
				 /*
				 * else if ( !strPwd2.equals(strDBPwd2)) { // 2차 비밀번호가 일치하지 않는 경우 userMap =
				 * null; Msg = "2차 비밀번호가 일치하지 않습니다."; }
				 */
			 }
	        if (userMap != null) {	  
        		strCode = "LOGIN";
        		servletResponse.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			    SessionUtil.setSessionAttribute(servletRequest,"USR",userMap);
	    		
			    if(LoginManager.getInstance().isUsing(CommonUtil.nvl(userMap.get("USER_ID")))) {
			    	LoginManager.getInstance().removeSession(CommonUtil.nvl(userMap.get("USER_ID")));
			    }
			    LoginManager.getInstance().setSession(session, CommonUtil.nvl(userMap.get("USER_ID")));
	        } else {
	        	SessionUtil.removeSessionAttribute(servletRequest,"USR");
				SessionUtil.removeSessionAttribute(servletRequest,"ADM");
				strCode = "FAIL";
	        }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".login() : "+ e.toString());
            strCode = "FAIL"; 
        }
        
        if ( "FAIL".equals(strCode)) {
        	CommonUtil.alertMsgGoUrl(servletResponse, Msg, strLoginUrll);        	
        	return null;
        }
        	
        return "redirect:" + returnUrl;
    }
         
	 /**
     * Method Summary. <br>
     * 로그인  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
/*    @RequestMapping( value="/front/loginGPinCheck2.do" )
    public void loginGPinCheck2(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {
    	
    }*/

    
	 /**
     * Method Summary. <br>
     * Home 회원가입 Agree  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/home/memberAgree.do" )
    public String homeMemberAgree(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        servletRequest.setAttribute( "reqMap",        reqMap );
        
        return "home/member/memberAgree";
    }	    
        
	 /**
     * Method Summary. <br>
     * Home 회원 2년 후 약관동의를 받아야 함  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/home/memberAgreeAfter.do" )
    public String memberAgreeAfter(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        servletRequest.setAttribute( "reqMap",        reqMap );
        
        return "home/member/memberAgreeAfter";
    }    
    
	 /**
     * Method Summary. <br>
     * 회원가입 Form  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/home/memberForm.do" )
    public String memberForm(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        servletRequest.setAttribute( "reqMap",        reqMap );
        
        return "home/member/signup1_yak";
    }	    
    
    /**
     * Method Summary. <br>
     * 회원가입 Form  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/home/memberForm22.do" )
    public String memberForm2(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {
			if(reqMap.get("manager_id") != null && !CommonUtil.nvl(reqMap.get("manager_id")).contentEquals("")) {
				Map detailMap = dbSvc.dbDetail(reqMap,"adminFranchise.franchiseDetail");
				if(detailMap != null && !detailMap.isEmpty())
					reqMap.put("fran_name",CommonUtil.nvl(detailMap.get("FRAN_NAME")));
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
        servletRequest.setAttribute( "reqMap", reqMap );
        
        return "home/member/signup2_submitInfo";
    }	    
    
    
   
   	 /**
         * Method Summary. <br>
         * Univ 회원수정 Form  method.
         * @param servletRequest HttpServletRequest 객체
         * @param servletResponse HttpServletResponse 객체
         * @return ActionForward 객체 -  정보
         * @throws e Exception
         * @since 1.00
         * @see
         */     
        @RequestMapping( value="/front/home/memberModifyForm.do" )
        public String homeMemberModifyForm(HttpServletRequest servletRequest,
                HttpServletResponse servletResponse) throws Exception {

            HttpSession session = servletRequest.getSession( false );
            Map reqMap = CommonUtil.getRequestMap( servletRequest );
            servletRequest.setAttribute( "reqMap",        reqMap );
            
            String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
            
            boolean isLogin = CommonUtil.isLoginSite(servletRequest, servletResponse, CommDef.LOGIN_PAGE_HOME, "/front/home/memberModifyForm.do?menu_no=" + strMenuNo);
            if ( !isLogin)
            	return null;
            
            Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
             
            String strFromSite = CommonUtil.nvl(userMap.get("FROM_SITE"));
            
        	reqMap.put("user_id", userMap.get("USER_ID"));
        	servletRequest.setAttribute("userMap", dbSvc.dbDetail(reqMap, "member.userDetail"));
        	
            return "home/member/memberModifyForm";
        }	        	
    	
    
	 /**
    * Method Summary. <br>
    * 회원 찾기 Form  method.
    * @param servletRequest HttpServletRequest 객체
    * @param servletResponse HttpServletResponse 객체
    * @return ActionForward 객체 -  정보
    * @throws e Exception
    * @since 1.00
    * @see
    */     
   @RequestMapping( value="/front/home/userSearch.do" )
   public String userSearchForm(HttpServletRequest servletRequest,
           HttpServletResponse servletResponse) throws Exception {

       HttpSession session = servletRequest.getSession( false );
       Map reqMap = CommonUtil.getRequestMap( servletRequest );
       servletRequest.setAttribute( "reqMap",        reqMap );
       
       return "home/member/find_id";
   }	    
   
   /**
    * Method Summary. <br>
    * 회원 찾기 Form  method.
    * @param servletRequest HttpServletRequest 객체
    * @param servletResponse HttpServletResponse 객체
    * @return ActionForward 객체 -  정보
    * @throws e Exception
    * @since 1.00
    * @see
    */     
   @RequestMapping( value="/front/home/userSearch2.do" )
   public String userSearchForm2(HttpServletRequest servletRequest,
           HttpServletResponse servletResponse) throws Exception {

       HttpSession session = servletRequest.getSession( false );
       Map reqMap = CommonUtil.getRequestMap( servletRequest );
       servletRequest.setAttribute( "reqMap",        reqMap );
       
       return "home/member/find_pwd";
   }	    
       
   @RequestMapping( value="/home/member/pwdModifyForm.do" )
   public String pwdModifyForm(HttpServletRequest servletRequest,
           HttpServletResponse servletResponse) throws Exception {

       HttpSession session = servletRequest.getSession( false );
       Map reqMap = CommonUtil.getRequestMap( servletRequest );
       servletRequest.setAttribute( "reqMap",        reqMap );
       
       return "home/member/pwdModify";
   }	
   
   @RequestMapping( value="/home/pwdMod.do" )
   public String pwdMod(HttpServletRequest servletRequest,
           HttpServletResponse servletResponse) throws Exception {

       Map reqMap = CommonUtil.getRequestMap( servletRequest );
       
       servletRequest.setAttribute( "reqMap",        reqMap );
       int chkUpdate = -999;
       
       try {
			
    	   reqMap.put("pwd", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
    	   reqMap.put("pwd2", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd2"))));
    	   
    	   chkUpdate = dbSvc.dbUpdate(reqMap,"member.userPwdModify");
    	   
       } catch ( Exception e) {
    	   System.out.println(e.toString());
       }
       
       if (chkUpdate < 0) {
    	   CommonUtil.alertMsgGoUrl(servletResponse, "일치하는 아이디가 없습니다.", "/front/home/userSearch2.do");
    	   return null;
       } else {
    	   CommonUtil.alertMsgGoUrl(servletResponse, "Successfully modified.", "/front/home/login.do");
    	   return null;
       }
   }
   
   @RequestMapping( value="/front/memberDupCheck.do" )
    public void memberDupCheck(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        int nExists = -1;
        String strCode = "DUP";
        
       // String strUrl ="redirect:" + CommonUtil.getDomainUrl(servletRequest) + "/front/memberInsertHttpMsg.do?code=";
        
        try {
    		
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
        	
        	nExists = dbSvc.dbCount(reqMap, "member.loginExists");
        	
        	if ( nExists <= 0 ) {
        	   strCode="OK";
        	}    	            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
        CommonUtil.displayText(servletResponse, strCode);
      
    }       	
    	
	 /**
     * Method Summary. <br>
     * 회원 등록  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/memberInsert.do" )
    public void memberInsert(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        int nExists = -1;
        String strCode = "DUP";
        String strServerName = servletRequest.getServerName();
        
       // String strUrl ="redirect:" + CommonUtil.getDomainUrl(servletRequest) + "/front/memberInsertHttpMsg.do?code=";
        
        try {
 
        	String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
        	
            System.out.println("strServerName:[" + strServerName + "] DEV_IP_ADDR:" +CommDef.DEV_IP_ADDR );

        	reqMap.put("reg_ip",    userIp);
        	
        	Map paramMap = new HashMap();
        	paramMap.put("user_id",CommonUtil.nvl(reqMap.get("user_id")));
        	
        	nExists = dbSvc.dbCount(paramMap, "member.loginExists");
        	
        	if ( nExists <= 0 ) {
        		reqMap.put("pwd",SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
        		reqMap.put("my_key",CommonUtil.createSequenceKey(dbSvc.dbCount("member.getMemberSeqKey")));
        		dbSvc.dbInsert(reqMap, "member.userInsert");
        		strCode="OK";
        	}
            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
        //CommonUtil.displayText(servletResponse, strCode);
        
        if ("DUP".equals(strCode)) {
        	CommonUtil.alertMsgGoUrl(servletResponse, "Member ID already exists.","/front/home/memberForm.do");
        } else if ("OK".equals(strCode)) {
        	CommonUtil.alertMsgGoUrl(servletResponse, "You have been successfully registered.", "/home/index.do");
        } else {
        	CommonUtil.alertMsgGoUrl(servletResponse, "Membership registration failed.","/front/home/memberForm.do");
        }        
        
        //return strUrl + strCode;
    }    
    
	 /**
     * Method Summary. <br>
     * 회원 수정  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/memberUpdate.do" )
    public void memberUpdate(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        int nExists = -1;
        String strCode = "NODATA";
        String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
        
        try {
        	String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
            boolean isLogin = CommonUtil.isLoginSite(servletRequest, servletResponse, CommDef.LOGIN_PAGE_UNIV, "/home/userModify.do?menu_no=" + strMenuNo);
            if ( !isLogin)
            	return ;
            
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
         
        	reqMap.put("user_id", userMap.get("USER_ID"));        	
        	reqMap.put("reg_ip", userIp);
        	
        	if (!CommonUtil.nvl(reqMap.get("pwd")).equals("")) {
        		reqMap.put("pwd", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
			}
        	
        	if (!CommonUtil.nvl(reqMap.get("pwd2")).equals("")) {
        		reqMap.put("pwd2", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd2"))));
			}
        	
        	
        	int dupCount = dbSvc.dbCount(reqMap, "member.loginChangeEffect");
        	 
        	nExists = dbSvc.dbCount(reqMap, "member.loginModifyExists");
        	if ( nExists > 0 ) {
        		dbSvc.dbInsert(reqMap, "member.userUpdate");
               strCode="OK";
        	} 
            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
        if ("NODATA".equals(strCode)) {
        	CommonUtil.alertMsgBack(servletResponse, "비밀번호를 잘 못 입력하셨거나 데이터가 존재하지 않습니다.");
        } else if ("OK".equals(strCode)) {
        	CommonUtil.alertMsgGoUrl(servletResponse, "Successfully modified.", "/");
        } else {
        	CommonUtil.alertMsgBack(servletResponse, "Unable to edit member profile.");
        }   
        
    }      
    
    /**
     * Method Summary. <br>
     * 비밀번호 변경  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/home/pwdModifyWork.do" )
    public void pwdModify(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        int nExists = -1;
        String strCode = "NODATA";
        String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
        
        try {
        	String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
        	if (!CommonUtil.nvl(reqMap.get("pwd")).equals("")) {
        		reqMap.put("pwd", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
			}
        	
       		dbSvc.dbInsert(reqMap, "member.userUpdate");
            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
       	CommonUtil.alertMsgGoUrl(servletResponse, "Correction has been completed normally.", "/front/home/login.do");
    }      
    
	 /**
     * Method Summary. <br>
     * 회원 탈퇴  method.
     * @param servletRequest HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 -  정보
     * @throws e Exception
     * @since 1.00
     * @see
     */     
    @RequestMapping( value="/front/memberLeave.do" )
    public void memberLeave(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        int nExists = -1;
        String strCode = "NODATA";
        String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
        
        try {
        	String strMenuNo = CommonUtil.nvl(reqMap.get("menu_no"));
        	String strSiteGb = CommonUtil.nvl(reqMap.get("join_site"), CommDef.MENU_HOME);
        	
        	String strLoginPage =  ( strSiteGb.equals( CommDef.MENU_HOME)) ? CommDef.LOGIN_PAGE_HOME : CommDef.LOGIN_PAGE_UNIV;
        	
            boolean isLogin = CommonUtil.isLoginSite(servletRequest, servletResponse, strLoginPage, "/front/memberModifyForm.do?menu_no=" + strMenuNo);
            if ( !isLogin)
            	return;
            
        	Map userMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
         
        	reqMap.put("user_id", userMap.get("USER_ID"));        	
        	reqMap.put("reg_ip", userIp);
        	reqMap.put("pwd",    SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd"))));
        	reqMap.put("pwd2",    SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("pwd2"))));
        	
        	int managerCheck = dbSvc.dbCount(reqMap,"member.managerCheck");
			
        	int A = 3;
        	nExists = dbSvc.dbCount(reqMap, "member.leaveExists");
        	
        	if ( nExists > 0 ) {
        		
        		dbSvc.dbUpdate(reqMap, "member.userLeave");
        		
        		SessionUtil.removeSessionAttribute(servletRequest,"USR");
        		SessionUtil.removeSessionAttribute(servletRequest,"ADM");
        		session.invalidate();               
               
        		strCode="OK";
        	}
            
        } catch ( Exception e) {
        	System.out.println(e.toString());
        	strCode = "ERROR";
        }
 
        if ("NODATA".equals(strCode)) {
        	CommonUtil.alertMsgBack(servletResponse, "비밀번호나 전화번호를 잘 못 입력하셨거나 데이터가 존재하지 않습니다.");
        } else if ("OK".equals(strCode)) {
        	CommonUtil.alertMsgGoUrl(servletResponse, "정상적으로 탈퇴되었습니다.", "/");
        } else {
        	CommonUtil.alertMsgBack(servletResponse, "오류가 발생하여 탈퇴가 실패되었습니다.");
        } 
        
    }         
    
	
	/**
	 * Method Summary. <br>
	 * 회원정보수정  method.
	 * @param servletRequest HttpServletRequest 객체
	 * @param servletResponse HttpServletResponse 객체
	 * @return ActionForward 객체 -  정보
	 * @throws e Exception
	 * @since 1.00
	 * @see
	 */     
	@RequestMapping( value="/home/userModify.do" )
	public String userModify(HttpServletRequest servletRequest,
	        HttpServletResponse servletResponse) throws Exception {
	
	    HttpSession session = servletRequest.getSession( false );
	    Map reqMap = CommonUtil.getRequestMap( servletRequest );
	    servletRequest.setAttribute( "reqMap",        reqMap );
	    
	    try {
	    	boolean isLogin = CommonUtil.isLoginSite(servletRequest, servletResponse, CommDef.LOGIN_PAGE_HOME, "/home/login.do");
	    	Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
	    	sesMap.put("user_id",CommonUtil.nvl(sesMap.get("USER_ID")));
	    	servletRequest.setAttribute("userMap", dbSvc.dbDetail(sesMap,"member.userAllDetail"));
	    	
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".userModify() : "+ e.toString());
        }
 
	    return "home/member/edit_member_info";
	}

}
