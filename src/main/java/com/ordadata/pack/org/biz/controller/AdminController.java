// 인코딩 : UTF-8
/*-------------------------------------------------------------------------------------*
 *	Author: byeong-soo Kim
 *	Created: 2011. 2. 28.
 *	Mail: biz21@naver.com
 *	Description: 
 *-------------------------------------------------------------------------------------*/
package com.ordadata.pack.org.biz.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.ordadata.pack.charm.util.ClientInfo;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SHA256Encode;
import com.ordadata.pack.charm.util.SessionUtil;

@Controller
public class AdminController  
{
	
	@Resource(name="dbSvc")
	private DbController dbSvc;
	
	@Autowired
	SqlSession SqlSession;
	
	
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
	 @RequestMapping( value="/boffice/" )
	 public String bofficeindex(HttpServletRequest servletRequest,
						 HttpServletResponse servletResponse) throws Exception {

		 return "boffice/index";
	 }


		 @RequestMapping( value="/admin/login.do" )
    public String login(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
		String returnUrl 	= reqMap.get("returnurl") == null ? "/boffice/common/form_list.jsp?brd_kind=" + CommDef.Board.BRD_NOTICE : "redirect:"+reqMap.get("returnurl");
		String strMenuUrl = "/boffice/menu/menuList.do";
		
        try {
        	String userIp = ClientInfo.getClntIP(servletRequest); // 접속IP
         	
        	reqMap.put("user_pw", SHA256Encode.Encode(CommonUtil.nvl(reqMap.get("user_pw"))));
			Map userMap =SqlSession.selectOne("adminMember.adminLogin", reqMap);
	        if (userMap != null) {
    			Map menuMap =dbSvc.dbDetail(userMap, "adminMember.userFirstMenu");
    			if (menuMap != null) {
    				String strMenuNo = CommonUtil.nvl(menuMap.get("TOP_MENU_NO"));
    				CommonUtil.setCookieObject(servletResponse, CommDef.COOKIE_ADMIN_TOP_MENU_NO, strMenuNo, 999);
    				strMenuUrl = CommonUtil.getMenuUrl(menuMap);
    				SessionUtil.setSessionAttribute(servletRequest,"ADM",userMap);
    			} else {
    	        	SessionUtil.removeSessionAttribute(servletRequest,"USR");
    				SessionUtil.removeSessionAttribute(servletRequest,"ADM");
    	        	return (String) CommonUtil.alertMsgGoUrl(servletResponse, "설정된 메뉴가 없습니다. 관리자에게 문의하여 주세요","/boffice/index.html");
    			}
    			if ( "ADMIN".equals(CommonUtil.nvl(userMap.get("user_type")))) {
    				strMenuUrl= "/boffice/member/memberList.do?menu_no=773";
    				CommonUtil.setCookieObject(servletResponse, CommDef.COOKIE_ADMIN_TOP_MENU_NO, "773", 999);
    			} else if ( "USER".equals(CommonUtil.nvl(userMap.get("user_type")))) {
    				strMenuUrl= "/boffice/member/memberUpdate.do?menu_no=850";
    				CommonUtil.setCookieObject(servletResponse, CommDef.COOKIE_ADMIN_TOP_MENU_NO, "850", 999);
    			}
	        } else {
	        	SessionUtil.removeSessionAttribute(servletRequest,"USR");
				SessionUtil.removeSessionAttribute(servletRequest,"ADM");
	        	return (String) CommonUtil.alertMsgGoUrl(servletResponse, "로그인을 실패했습니다. 다시 로그인해주세요","/boffice/index.html");
	        }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".login() : "+ e.toString());
        }
        
        return "redirect:" + strMenuUrl;   // 메뉴관리
    }	
	
  
	public String encrypt(String strData) { // 암호화 시킬 데이터
		  String strENCData = "";
		  
		  if ( strData == null || "".equals(strData))
			  return "";
		  
		  try {
			   MessageDigest md = MessageDigest.getInstance("MD5"); // "MD5 형식으로 암호화"
	
			   byte[] bytData = strData.getBytes();  
			   
			   md.update(bytData);
			   byte[] digest = md.digest();  //배열로 저장을 한다.
			   for (int i = 0; i < digest.length; i++) {
			    strENCData = strENCData + Integer.toHexString(digest[i] & 0xFF).toUpperCase();
			   }
		  } catch (NoSuchAlgorithmException e) {
		   System.out.print("암호화 에러" + e.toString());
		  }
		  return strENCData;  // 암호화된 데이터를 리턴...
  }
    
}