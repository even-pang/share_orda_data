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

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;
import com.ordadata.pack.charm.util.SessionUtil;
import com.ordadata.pack.charm.util.UploadUtil;

/**
 * Class Summary. <br>
 * 게시판 관리 class.
 *
 * @author 이현도
 * @version 1.00 - 2019. 09. 26
 * @see
 * @since 1.00
 */

@Controller
public class AdminMenuController {

    @Resource(name = "dbSvc")
    private DbController dbSvc;


    private String mTableName = "TB_MENU";    // 테이블 명
    private String BRD_UPLOADPATH = "/upload/menu/";

    /**
     * Method Summary. <br>
     * 메뉴 목록 처리 method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/menu/menuList.do")
    public String menuList(HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map reqMap = CommonUtil.getRequestMap(servletRequest);

        String strPageUrl = "boffice/menu/menuList";

        try {
            Map userMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");
            System.out.println("===> " + userMap);
            if (!CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
                return "";
            }

            int nPageRow = dbSvc.getPageRowCount(reqMap, "page_row");
            int nRowStartPos = nPageRow * (dbSvc.getPageNow(reqMap, "page_now") - 1);  // Row의 시작위치

            reqMap.put("page_row", nPageRow);                                      // 현재 페이지

            String strUpMenuNo = CommonUtil.nvl(reqMap.get("up_menu_no"), CommDef.TOP_MENU_NO);
            reqMap.put("up_menu_no", strUpMenuNo);

            //상위메뉴로 올라가기위해 부모 menu_no 가져오기
            if ("".equals(CommonUtil.nvl(reqMap.get("parent_menu_no")))) {
                Map paramMap = new HashMap();
                paramMap.put("menu_no", CommonUtil.nvl(reqMap.get("up_menu_no")));

                Map mapDt = dbSvc.dbDetail(paramMap, "menu.menuDetail");

                if (mapDt != null) {
                    reqMap.put("parent_menu_no", CommonUtil.nvl(mapDt.get("UP_MENU_NO")));
                }
            }

            CommonUtil.setNullVal(reqMap, "menu_gb", CommDef.ADMIN_MENU_GB);
            System.out.println(nRowStartPos + "   " + nPageRow);
            servletRequest.setAttribute("reqMap", reqMap);
            servletRequest.setAttribute("count", Integer.toString(dbSvc.dbCount(reqMap, "adminMember.menuCount")));//
            servletRequest.setAttribute("list", dbSvc.dbList(reqMap, "adminMember.menuList", nRowStartPos, nPageRow));

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".menuList() : " + e.toString());

        }

        return strPageUrl;
    }

    /**
     * Method Summary. <br>
     * 메뉴 등록/수정 화면 method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/menu/menuInput.do")
    public String menuInput(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        Map detailMap = new HashMap();

        try {

            if (!CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
                return "";
            }

            String strRegNo = CommonUtil.getNullTrans(reqMap.get("menu_no"));

            reqMap.put("iflag", CommDef.ReservedWord.INSERT);

            if (!"".equals(strRegNo)) {

                detailMap = dbSvc.dbDetail(reqMap, "adminMember.menuDetail");
                if (detailMap != null)
                    reqMap.put("iflag", CommDef.ReservedWord.UPDATE);

                servletRequest.setAttribute("reqMap", reqMap);

                reqMap.put("rel_tbl", mTableName);
                reqMap.put("rel_key", strRegNo);

                servletRequest.setAttribute("lstFile", dbSvc.dbList(reqMap, "common.getUploadFile"));

            }

            Map mapParam = new HashMap();
            mapParam.put("sort_ord", "brd_nm");

            servletRequest.setAttribute("mrgList", dbSvc.dbList(mapParam, "boardmgr.boardMgrList"));

            servletRequest.setAttribute("userList", dbSvc.dbList(mapParam, "adminMember.userSelBoxList"));


        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".menuInput() : " + e.toString());
        }

        servletRequest.setAttribute("dbMap", detailMap);
        servletRequest.setAttribute("reqMap", reqMap);

        return "boffice/menu/menuWrite";
    }


    /**
     * Method Summary. <br>
     * Menu 입력&수정 처리 method
     *
     * @param actionMapping
     * @param form
     * @param servletRequest
     * @param servletResponse
     * @return ActionForward
     * @throws Exception description
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/menu/menuWork.do")
    public String menuWork(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        Map reqMap = null;
        String strMenuNo = "";
        String strUrl = "";
        UploadUtil uploadUtil = new UploadUtil(servletRequest, BRD_UPLOADPATH);
        uploadUtil.setDbDao(dbSvc.m_dao); //DB연결자

        String strContentType = CommonUtil.getNullTrans(servletRequest.getHeader("Content-Type"));
        boolean isFileUp = (strContentType.indexOf("multipart/form-data") > -1) ? true : false;  // 첨부파일이 존재하는지 확인

        reqMap = (isFileUp) ? uploadUtil.getRequestMap(servletRequest) : CommonUtil.getRequestMap(servletRequest);

        Map sesMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");

        if (sesMap == null) {
            strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/boffice/menu/menuList.do");
            strUrl += "?" + CommonUtil.nvl(reqMap.get("param"), "");

            CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);
            return null;
        }
        try {
            String strFlag = CommonUtil.setNullVal(reqMap.get("iflag"), "");

            reqMap.put("rel_tbl", mTableName);
            reqMap.put("reg_id", CommonUtil.nvl(sesMap.get("USER_ID")));
            reqMap.put("reg_nm", CommonUtil.nvl(sesMap.get("USER_NM")));

            if (CommonUtil.nvl(reqMap.get("brd_mgrno")).equals("")) {
                reqMap.put("brd_mgrno", null);
            }

            if (CommDef.ReservedWord.INSERT.equals(strFlag)) {
                strMenuNo = Long.toString(dbSvc.dbLong("adminMember.menuNextValue"));
                fillInsertMap(reqMap, strMenuNo);

                reqMap.put("rel_key", strMenuNo);
                reqMap.put("menu_no", strMenuNo);

                dbSvc.dbInsert(reqMap, "adminMember.menuInsert");

            } else if (CommDef.ReservedWord.UPDATE.equals(strFlag)) {
                reqMap.put("rel_key", reqMap.get("menu_no"));
                dbSvc.dbUpdate(reqMap, "adminMember.menuUpdate");
                strMenuNo = CommonUtil.getNullTrans(reqMap.get("menu_no"));
            }

            if (isFileUp) // 파일처리
                uploadUtil.uploadProcess(reqMap, servletRequest);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : " + e.toString());
        }

        String strParam = CommonUtil.nvl(reqMap.get("param"));

        strParam = CommonUtil.removeParam(strParam, "parent_menu_no");
        strParam = CommonUtil.removeParam(strParam, "up_menu_no");
        strParam = CommonUtil.removeParam(strParam, "menu_gb");

        strParam += "&up_menu_no=" + CommonUtil.nvl(reqMap.get("up_menu_no"));
        strParam += "&parent_menu_no=" + CommonUtil.nvl(reqMap.get("parent_menu_no"));
        strParam += "&menu_gb=" + CommonUtil.nvl(reqMap.get("menu_gb"), CommDef.ADMIN_MENU_GB);

        strUrl = CommonUtil.nvl(reqMap.get("returl")) + "?" + strParam;

        return "redirect:" + strUrl;

    }

    /**
     * Method Summary. <br>
     * 메뉴 삭제 처리 method.
     *
     * @param actionMapping   액션맵핑 객체
     * @param actionForm      액션폼 객체
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/menu/menuDelete.do")
    public String menuDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        String strUrl = CommonUtil.getNullTrans(reqMap.get("returl"), "/");

        try {
            servletRequest.setAttribute("reqMap", reqMap);
            Map sesMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");

            if (sesMap == null) {
                strUrl = CommonUtil.nvl(reqMap.get("listpage"), "/boffice/menu/menuList.do");
                strUrl += "?" + CommonUtil.nvl(reqMap.get("param"), "");

                CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);
                return null;
            }


            int nExists = dbSvc.dbCount(reqMap, "adminMember.menuDownCount");
            if (nExists > 0) {
                CommonUtil.alertMsgBack(servletResponse, "하위 메뉴가 존재합니다. 하위메뉴를 먼저 삭제하여 주십시오");
                return "";
            }

            String strBrdNo = CommonUtil.getNullTrans(reqMap.get("menu_no"));

            String[] arrBrdNo = strBrdNo.split(",");
            UploadUtil upUtil = new UploadUtil(servletRequest);
            upUtil.setDbDao(dbSvc.m_dao); //DB연결자

            for (int nLoop = 0; nLoop < arrBrdNo.length; nLoop++) {
                Map paramMap = new HashMap();
                paramMap.put("menu_no", arrBrdNo[nLoop]);
                dbSvc.dbDelete(paramMap, "adminMember.menuDelete");

                // 물리적인 파일 및 DB 정보삭제
                paramMap.put("rel_tbl", mTableName);
                paramMap.put("rel_key", arrBrdNo[nLoop]);

                upUtil.removeFile(paramMap);

            }

            String strParam = CommonUtil.nvl(reqMap.get("param"));

            strParam = CommonUtil.removeParam(strParam, "parent_menu_no");
            strParam = CommonUtil.removeParam(strParam, "up_menu_no");
            strParam += "&up_menu_no=" + CommonUtil.nvl(reqMap.get("up_menu_no"));
            strParam += "&parent_menu_no=" + CommonUtil.nvl(reqMap.get("parent_menu_no"));
            strParam += "&menu_gb=" + CommonUtil.nvl(reqMap.get("menu_gb"));

            strUrl = CommonUtil.nvl(reqMap.get("returl")) + "?" + strParam;

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".boardDelete() : " + e.toString());
        }

        return "redirect:" + strUrl;

    }


    /**
     * Method Summary. <br>
     * 관리자 게시판 입력 파리미터 처리 method
     *
     * @param reqMap     파라미터
     * @param strFlag    처리 flag
     * @param strNextVal
     * @see
     * @since 1.00
     */
    private void fillInsertMap(Map reqMap, String strNextVal) {
        int nDepth = 1;
        String strAllBrdNo = "";
        String strTopMenuNo = "";

        reqMap.put("menu_no", strNextVal);

        String strUpMenuNo = CommonUtil.nvl(reqMap.get("up_menu_no"), CommDef.TOP_MENU_NO);

        Map paramMap = new HashMap();
        paramMap.put("menu_no", reqMap.get("up_menu_no"));
        Map viewMap = dbSvc.dbDetail(paramMap, "adminMember.menuDetail");

        if (viewMap != null) {
            nDepth = Integer.parseInt(CommonUtil.nvl(viewMap.get("MENU_LVL"), "0")) + 1;
            strAllBrdNo = CommonUtil.nvl(viewMap.get("FULL_MENU_NO")) + strNextVal + ",";
            strTopMenuNo = CommonUtil.nvl(viewMap.get("TOP_MENU_NO"));
        } else {
            strAllBrdNo = strNextVal + ",";
            strTopMenuNo = strNextVal;
        }

        reqMap.put("menu_lvl", nDepth);
        reqMap.put("full_menu_no", strAllBrdNo);
        reqMap.put("top_menu_no", strTopMenuNo);
        reqMap.put("up_menu_no", strUpMenuNo);

    }

    /**
     * Method Summary. <br>
     * 회원 목록 처리 method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/userList.do")
    public String userList(HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map reqMap = CommonUtil.getRequestMap(servletRequest);

        String strPageUrl = "boffice/userList";

        try {

            if (!CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
                return "";
            }

            int nPageRow = dbSvc.getPageRowCount(reqMap, "page_row");
            int nRowStartPos = nPageRow * (dbSvc.getPageNow(reqMap, "page_now") - 1);  // Row의 시작위치

            reqMap.put("page_row", nPageRow);                                      // 현재 페이지

            Map userMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");

            if (!"ADMIN".equals(CommonUtil.nvl(userMap.get("USER_TYPE")))) {
                reqMap.put("user_id", userMap.get("USER_ID"));
            }


            servletRequest.setAttribute("reqMap", reqMap);
            servletRequest.setAttribute("count", Integer.toString(dbSvc.dbCount(reqMap, "adminMember.userCount")));
            servletRequest.setAttribute("list", dbSvc.dbList(reqMap, "adminMember.userList", nRowStartPos, nPageRow));

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".userList() : " + e.toString());
        }

        return strPageUrl;
    }

    /**
     * Method Summary. <br>
     * 상단 목록 처리 method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/include/inc_header.do")
    public String userHeaderList(HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map reqMap = CommonUtil.getRequestMap(servletRequest);

        try {

            if (!CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
                return "";
            }

            Map userMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");
            reqMap.put("user_id", userMap.get("user_id"));
            reqMap.put("user_type", userMap.get("user_type"));
            reqMap.put("menu_gb", CommDef.ADMIN_MENU_GB);
            reqMap.put("use_yn", 'Y');
            //List menuMap  = (List) dbSvc.dbList( reqMap, "adminMember.usermenuTopList");
            //reqMap.put("menu_detail",  menuMap);
            servletRequest.setAttribute("list", dbSvc.dbList(reqMap, "adminMember.menuTopList"));

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".userList() : " + e.toString());
        }

        return "boffice/include/inc_header";
    }

    /**
     * Method Summary. <br>
     * 상단 목록 처리 method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/include/inc_top.do")
    public String userTopList(HttpServletRequest servletRequest,
                              HttpServletResponse servletResponse) throws Exception {
        return "boffice/include/inc_top";
    }


    /**
     * Method Summary. <br>
     * 메뉴 등록/수정 화면 method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/userInput.do")
    public String userInput(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        Map detailMap = new HashMap();

        try {

            if (!CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
                return "";
            }

            String strUserId = CommonUtil.getNullTrans(reqMap.get("user_id"));

            reqMap.put("iflag", CommDef.ReservedWord.INSERT);

            if (!"".equals(strUserId)) {

                detailMap = dbSvc.dbDetail(reqMap, "adminMember.userDetail");
                if (detailMap != null)
                    reqMap.put("iflag", CommDef.ReservedWord.UPDATE);

                servletRequest.setAttribute("reqMap", reqMap);
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".userInput() : " + e.toString());
        }

        servletRequest.setAttribute("dbMap", detailMap);
        servletRequest.setAttribute("reqMap", reqMap);

        return "boffice/userWrite";
    }

    /**
     * Method Summary. <br>
     * 입력&수정 처리 method
     *
     * @param actionMapping
     * @param form
     * @param servletRequest
     * @param servletResponse
     * @return ActionForward
     * @throws Exception description
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/userWork.do")
    public String userWork(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        String strUrl = "";

        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        Map sesMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");

        if (sesMap == null) {
            CommonUtil.alertAdminLoginGoUrl(servletResponse, "/");
            return null;
        }
        try {
            String strFlag = CommonUtil.setNullVal(reqMap.get("iflag"), "");

            reqMap.put("reg_id", CommonUtil.nvl(sesMap.get("user_id")));
            reqMap.put("reg_nm", CommonUtil.nvl(sesMap.get("user_nm")));

            if (CommDef.ReservedWord.INSERT.equals(strFlag)) {
                dbSvc.dbInsert(reqMap, "adminMember.userInsert");

            } else if (CommDef.ReservedWord.UPDATE.equals(strFlag)) {
                dbSvc.dbUpdate(reqMap, "adminMember.userUpdate");
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : " + e.toString());
        }

        strUrl = CommonUtil.nvl(reqMap.get("returl"), "/boffice/menu/user_list.jsp");
        strUrl += "?" + CommonUtil.nvl(reqMap.get("param"), "");

        return "redirect:" + strUrl;
    }


    /**
     * Method Summary. <br>
     * 회원정보를 삭제한다. method
     *
     * @param actionMapping
     * @param form
     * @param servletRequest
     * @param servletResponse
     * @return ActionForward
     * @throws Exception description
     * @see
     * @since 1.00
     */
    @RequestMapping(value = " /boffice/userDelete.do")
    public String userDelete(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        String strBrdNo = "";
        String strUrl = "/boffice/menu/user_list.jsp";

        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        Map sesMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");

        if (sesMap == null) {

            strUrl += "?" + CommonUtil.nvl(reqMap.get("param"), "");

            CommonUtil.alertAdminLoginGoUrl(servletResponse, strUrl);
            return null;
        }
        try {
            dbSvc.dbDelete(reqMap, "adminMember.userDelete");

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : " + e.toString());
        }

        strUrl = "/boffice/menu/user_list.jsp";
        strUrl += "?" + CommonUtil.nvl(reqMap.get("param"), "");

        CommonUtil.alertMsgGoUrl(servletResponse, "선택하신 자료를 삭제하였습니다.", strUrl);

        return null;
    }


    /**
     * Method Summary. <br>
     * 관리자에 할당 된 메뉴를 조회한다. method.
     *
     * @param servletRequest  HttpServletRequest 객체
     * @param servletResponse HttpServletResponse 객체
     * @return ActionForward 객체 - 리턴 페이지 정보
     * @throws e Exception
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/setUserMenu.do")
    public String setUserMenu(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession(false);
        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        Map detailMap = new HashMap();

        try {

            if (!CommonUtil.isAdminLogin(servletRequest, servletResponse)) {
                return "";
            }

            servletRequest.setAttribute("list", dbSvc.dbList(reqMap, "adminMember.setUserMenuList"));
            servletRequest.setAttribute("reqMap", reqMap);

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".userInput() : " + e.toString());
        }

        return "boffice/setMenuList";
    }

    /**
     * Method Summary. <br>
     * 사용자 메뉴를 입력&수정 처리 method
     *
     * @param actionMapping
     * @param form
     * @param servletRequest
     * @param servletResponse
     * @return ActionForward
     * @throws Exception description
     * @see
     * @since 1.00
     */
    @RequestMapping(value = "/boffice/userMenuWork.do")
    public String userMenuWork(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {

        String strUrl = "";

        Map reqMap = CommonUtil.getRequestMap(servletRequest);
        Map sesMap = (Map) SessionUtil.getSessionAttribute(servletRequest, "ADM");

        if (sesMap == null) {
            CommonUtil.alertAdminLoginGoUrl(servletResponse, "/");
            return null;
        }
        try {
            String strMenuNo = CommonUtil.setNullVal(reqMap.get("all_menu_no"), "");
            String strUserId = CommonUtil.setNullVal(reqMap.get("user_id"), "");

            if (!"".equals(strUserId)) {
                dbSvc.dbDelete(reqMap, "adminMember.userMenuDelete"); // 사용자 메뉴 삭제

                String[] arrMenuNo = strMenuNo.split(",");
                for (int nLoop = 0; nLoop < arrMenuNo.length; nLoop++) {
                    Map paramMap = new HashMap();
                    paramMap.put("user_id", strUserId);
                    paramMap.put("menu_no", arrMenuNo[nLoop]);

                    dbSvc.dbInsert(paramMap, "adminMember.userMenuInsert");
                }
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".work() : " + e.toString());
        }

        return (String) CommonUtil.alertMsgSelfClose(servletResponse, "메뉴 권한을 설정하였습니다.", "");
    }


}
