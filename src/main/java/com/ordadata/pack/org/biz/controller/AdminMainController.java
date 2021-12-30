// �씤肄붾뵫 : UTF-8
/*-------------------------------------------------------------------------------------*
 *	Author: byeong-soo Kim
 *	Created: 2011. 2. 28.
 *	Mail: biz21@naver.com
 *	Description: 
 *-------------------------------------------------------------------------------------*/
package com.ordadata.pack.org.biz.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SessionUtil;
import com.ordadata.pack.charm.util.UploadUtil;

@Controller
public class AdminMainController {

	@Resource(name = "dbSvc")
	private DbController dbSvc;

	@Autowired
	SqlSession SqlSession;

	private String BOARD_UPLOADPATH = "/upload/board/";

	
	@ResponseBody
	@RequestMapping(value = "/boffice/uploadImage.do", method = RequestMethod.POST, produces = "application/text; charset=UTF-8" )
	public String ajaxUploadImage(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws Exception {
		Map reqMap = null;
		String strBrdNo = "";
		String strUrl = "";
		String strContentType = CommonUtil.getNullTrans(servletRequest.getHeader("Content-Type"));
		Map sesMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");
		UploadUtil uploadUtil = new UploadUtil(servletRequest, BOARD_UPLOADPATH);
		boolean isFileUp = (strContentType.indexOf("multipart/form-data") > -1) ? true : false; // 泥⑤ 뙆 씪 씠 議댁옱 븯 뒗吏 솗 씤
		reqMap = (isFileUp) ? uploadUtil.getRequestMap(servletRequest) : CommonUtil.getRequestMap(servletRequest);
		Gson gson = new Gson();
		String jsonName = gson.toJson(reqMap.get("file_nm"));
		String jsonRealName = gson.toJson(reqMap.get("file_realnm"));

		return jsonName + "|" + jsonRealName;
	}
}