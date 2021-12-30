/**
 * Class Summary. <br>
 * CommDef class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */
package com.ordadata.pack.charm.util;

public class CommDef {
	//--------------------------------------- UPLOAD PATH ----------------------------------------------------------------
	public static final String BRD_UPLOADPATH		= "/upload/board/";		// 게시판 업로드 폴더
	//--------------------------------------------------------------------------------------------------------------------
 
	//--------------------------------------- Paging ---------------------------------------------------------------------
	public static final int	PAGE_ROWCOUNT					= 10; // 페이지당  표시할 갯수 
	public static final int	PAGE_PER_BLOCK					= 10; // 하단 페이지 번호 부여 갯수
	public static final int	PAGE_PHOTOROWCOUNT				=  9; // 하단 페이지 번호 부여 갯수
	
	public static final int	M_PAGE_ROWCOUNT					= 8; // 모바일 페이지당  표시할 갯수 
	public static final int	M_PAGE_PER_BLOCK				= 5; // 모바일 하단 페이지 번호 부여 갯수
	
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------- HTML Editor ----------------------------------------------------------------	
	public static final String EDITOR_TOOLBAR				   = "Default";   // Default,  Basic
	public static final String EDITOR_BASIC				       = "Basic";   // Default,  Basic
	public static final String EDITOR_FRONT				       = "Front";   // Default,  Basic
	public static final String EDITOR_ADMIN				       = "Admin";   // Default,  Basic
	//--------------------------------------------------------------------------------------------------------------------	
	
	public static final String SESREALNAME				       = "REALNAME";
	
	public static final String TOP_MENU_NO				       = "0";   // 메뉴구조 중 최상위
	public static final String ADMIN_MENU_GB		           = "ADMIN";   // 관리자 메뉴 코드
	
	public static final String IMG_MENU_TOP                    = "TOP";    // 탑 이미지
	public static final String IMG_MENU_LEFTLOG                = "LEFTLOG";    //LEFT 대표이미지
	public static final String IMG_MENU_LEFTLOG_TEXT           = "LEFTLOGTXT";    //LEFT 대표이미지
	public static final String IMG_MENU_NORMAL                 = "NORMAL"; // 메뉴 선택전 이미지
	public static final String IMG_MENU_CHOOSE                 = "CHOOSE"; // 메뉴 선택후 이미지
	public static final String IMG_MENU_TITLE                  = "TITLE";  // 타이틀 이미지
	
	public static final String COOKIE_ADMIN_TOP_MENU_NO        = "admin_top_menu_no";
	public static final String COOKIE_ADMIN_SUB_MENU_NO        = "admin_sub_menu_no";
	public static final String COOKIE_ADMIN_CUR_MENU_NO        = "admin_cur_menu_no";
	
	public static final String MENU_HOME                       = "HOME";  //
	public static final String MENU_ADMIN                      = "ADMIN";  //관리자메뉴
	public static final String MENU_COMMON                     = "COMMON";  //공통메뉴	 
	
	public static final String LOGIN_PAGE_UNIV                 = "/front/home/login.do?menu_no=122";	
	public static final String LOGIN_PAGE_HOME                 = "/front/home/login.do?menu_no=122";
	public static final String LOGIN_PAGE_LAB                  = "/front/lab/login.do";
	
	public static final String DEV_IP_ADDR                    = "124.54.78.227";
	 
	/**
	 * iflag 구분자  
	 * @author rokihoon
	 */
	public static class ReservedWord {
		public static final String INSERT  = "INSERT";
		public static final String UPDATE  = "UPDATE";
		public static final String DELETE  = "DELETE";
		public static final String REPLY   = "REPLY";
		public static final String EXCEL   = "EXCEL";
		
		public static final String NOTLOGIN		= "NOTLOGIN";
		public static final String ERROR		= "ERROR";
		public static final String ADMIN		= "ADMIN";
		public static final String USER			= "USER";
		public static final String EXIST		= "EXIST";
		public static final String OVERMAXCNT	= "OVERMAXCNT";
		public static final String SUCCESS		= "SUCCESS";
		
		public static final String BTN_HIDE     = "HIDE";
		public static final String BTN_SHOW     = "SHOW";
	}
	
	/**
	 * 컨텐츠관리 게시판 상세보기 없는 값 메세지
	 * @author rokihoon
	 */
	public static class Message	{
		public static final String NO_DATA = "No Data Found.";
	}

	/**
	 * 컨텐츠관리 게시판 GBN
	 * @author rokihoon
	 */
	public static class Board {
		
		// 열림마당
		public static final String BRD_NOTICE      = "NOTICE";     // 공지사항             
		
		public static final String BRD_CONTENT    = "CONTENT";     // 게시 내용 관리
		public static final String BRD_BANNER     = "BANNER";      //  메인 롤링메너
		public static final String BRD_NOWRITE    = "NOWRITE";     // 글쓰기 없음
		public static String getBoardName(String strCode)
		{
			String strName = "--";
			return strName;
		}
		
	}
}
