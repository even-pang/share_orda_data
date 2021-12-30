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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.ClientInfo;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SessionUtil;

/**
 * Class Summary. <br>
 * 硫붿씤 愿�由� class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author �씠�쁽�룄
 * @see
 */

@Controller
public class MainController   {

	@Resource(name="dbSvc")
	private DbController dbSvc;
	
	 /**
     * Method Summary. <br>
     * ORDADATA �솃�럹�씠吏� �씠�룞  method.
     * @param servletRequest HttpServletRequest 媛앹껜
     * @param servletResponse HttpServletResponse 媛앹껜
     * @return ActionForward 媛앹껜 -  �젙蹂�
     * @throws e Exception
     * @since 1.00
     * @see
     */
    @RequestMapping( value="/home" )
    public String homeIndex(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map paramMap = new HashMap();
        
        try {
    		
        	String userIp = ClientInfo.getClntIP(servletRequest); // �젒�냽IP


        	Map sesMap = (Map)SessionUtil.getSessionAttribute(servletRequest,"USR");
	    	if(sesMap != null && !sesMap.isEmpty()) {
	    		sesMap.put("user_id",CommonUtil.nvl(sesMap.get("USER_ID")));
	    	}
			reqMap.put("rel_tbl", "TB_MAIN");
			reqMap.put("rel_key", 1);

        	/*servletRequest.setAttribute( "calcText", getHomeCalendar(servletRequest, servletResponse));*/
        	
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".home/index() : "+ e.toString());
 
        }
        
        return "home/index";
    }



    
}
