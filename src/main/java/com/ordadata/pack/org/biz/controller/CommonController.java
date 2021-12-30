/*
 * Copyright (c) 2008 Sosun. All rights reserved.
 */
package com.ordadata.pack.org.biz.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.util.CommonUtil;

/**
 * Class Summary. <br>
 * 怨듯넻 class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author �씠�쁽�룄
 * @see
 */

@Controller
public class CommonController  {

	@Resource(name="dbSvc")
	private DbController dbSvc;		
	
	
    @RequestMapping( value="/common/download.do" )
    public void download(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {
    		Map fileMap = dbSvc.dbDetail(reqMap, "common.getUploadFile");
          
            String strFileName     = CommonUtil.nvl(fileMap.get("FILE_NM"));
            String strFileRealName = CommonUtil.nvl(fileMap.get("FILE_REALNM"));
            String strPath     = servletRequest.getRealPath("/")+ "upload/board/";
		    
            PrintStream printstream = new PrintStream(servletResponse.getOutputStream(), true);
 
		    File file = new File(strPath,strFileRealName);
		    FileInputStream fin = new FileInputStream(file);
		         
		    int ifilesize = (int)file.length();
		    byte b[] = new byte[ifilesize];
       
		    String strSaveFile = CommonUtil.getFileName(strFileName);
		    
		    //strSaveFile = new String( strSaveFile.getBytes("EUC-KR"), "UTF-8" );
		    //strSaveFile = new String( strSaveFile.getBytes("UTF-8"), "EUC-KR" );
		    
		    strSaveFile = new String( strSaveFile.getBytes("EUC-KR"), "8859_1" ); 
 
		    
		    servletResponse.setContentLength(ifilesize);
            servletResponse.setContentType("application/smnet");
	        //servletResponse.setHeader("Content-Disposition","attachment; filename="+CommonUtil.getFileName(strFileName)+";");
            servletResponse.setHeader("Content-Disposition","attachment; filename=" + strSaveFile +";");
         
	        ServletOutputStream oout = servletResponse.getOutputStream();
	         
	        fin.read(b);
	         
	        oout.write(b,0,ifilesize);
	        //oout.flush();
	        oout.close();
		         
	        fin.close(); 
            
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".download.do() : "+ e.toString());
        }
 
    }
    
    @ResponseBody
    @RequestMapping( value="/common/allDownload.do" )
    public String allDownload(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        Map retMap = new HashMap();
        
        try {
        	reqMap.put("lstSeq", CommonUtil.nvl(reqMap.get("seq_no")));
        	List<Map> fileList = dbSvc.dbList(reqMap, "common.getUploadFileList");
    		//Map fileMap = dbSvc.dbDetail(reqMap, "common.getUploadFile");
        	String all_file_no = "";
        	for(Map fileMap : fileList) {
	            all_file_no += CommonUtil.nvl(fileMap.get("FILE_NO"))+",";
        	}
            retMap.put("all_file_no", all_file_no.substring(0, all_file_no.length()-1));
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".download.do() : "+ e.toString());
        }
 
        return CommonUtil.mapToJson(retMap);
    }
 
	/**
     * �씠誘몄� �뾽濡쒕뱶
     * @param request
     * @param response
     * @param upload
     */
    @RequestMapping(value = "/common/ckeditorUpload.do")
    public void communityImageUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload) {
 
        OutputStream out = null;
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
 
        try{
        	String strRealPath = request.getSession().getServletContext().getRealPath("/");
            String fileName = upload.getOriginalFilename();
            byte[] bytes = upload.getBytes();
            String uploadPath = strRealPath + "/fckfiles/" + fileName;  //���옣寃쎈줈
 
            out = new FileOutputStream(new File(uploadPath));
            out.write(bytes);
            String callback = request.getParameter("CKEditorFuncNum");
 
            printWriter = response.getWriter();
            String fileUrl =  "/fckfiles/" + fileName;  //url寃쎈줈
 
/*            printWriter.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("
                    + callback
                    + ",'"
                    + fileUrl
                    + "','�씠誘몄�瑜� �뾽濡쒕뱶 �븯���뒿�땲�떎.'"
                    + ")</script>");*/
           
            printWriter.println("{"
            		+ "\"uploaded\": 1,"
            		+ "\"fileName\": \"" + fileName + "\","
            		+ "\"url\": \""+ fileUrl +"\""
            		+ "}");
            printWriter.flush();
 
        }catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
    
	 /**
     * Method Summary. <br>
     * �뙆�씪 �떎�슫濡쒕뱶  method.
     * @param servletRequest HttpServletRequest 媛앹껜
     * @param servletResponse HttpServletResponse 媛앹껜
     * @return ActionForward 媛앹껜 -  �젙蹂�
     * @throws e Exception
     * @since 1.00
     * @see
     */     
 /*   @RequestMapping( value="/common/fileview.do" )
    public void fileview(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception {

        HttpSession session = servletRequest.getSession( false );
        Map reqMap = CommonUtil.getRequestMap( servletRequest );
        
        try {
    		Map fileMap = dbSvc.dbDetail(reqMap, "common.getUploadFile");
          
            String strFileName = CommonUtil.nvl(fileMap.get("FILE_REALNM"));
            String strFileNo   = CommonUtil.nvl(fileMap.get("FILE_NO"));
            String strPath     = servletRequest.getRealPath("/");
		     
	       	UploadUtil upload = new UploadUtil(servletRequest);
	       	upload.setDbDao(dbSvc.m_dao); //DB�뿰寃곗옄
	       	
	      	String strHtmlFile = upload.convertHtmlFile(strFileNo, strFileName);
	        if ("".equals(strHtmlFile)) {
	        	CommonUtil.alertMsgSelfClose(servletResponse, "HTML濡� 蹂��솚�븯�뒗 怨쇱젙�뿉�꽌 �삤瑜섍� 諛쒖깮�븯���뒿�땲�떎.", ""); 
	        } else {
	        	CommonUtil.alertMsgGoUrl(servletResponse, "", strHtmlFile );
	        }
            
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".download.do() : "+ e.toString());
        }

    }*/

    @ResponseBody
	@RequestMapping(value = "/file-upload", method = RequestMethod.POST)
	public String fileUpload(
			@RequestParam("article_file") List<MultipartFile> multipartFile
			, HttpServletRequest request) {
		
		String strResult = "{ \"result\":\"FAIL\" }";
		String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
		String fileRoot;
		try {
			// �뙆�씪�씠 �엳�쓣�븣 �깂�떎.
			if(multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {
				
				for(MultipartFile file:multipartFile) {
					fileRoot = contextRoot + "upload/";
					System.out.println(fileRoot);
					
					String originalFileName = file.getOriginalFilename();	//�삤由ъ��궇 �뙆�씪紐�
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//�뙆�씪 �솗�옣�옄
					String savedFileName = UUID.randomUUID() + extension;	//���옣�맆 �뙆�씪 紐�
					
					File targetFile = new File(fileRoot + savedFileName);	
					try {
						InputStream fileStream = file.getInputStream();
						//FileUtils.copyInputStreamToFile(fileStream, targetFile); //�뙆�씪 ���옣
						
					} catch (Exception e) {
						//�뙆�씪�궘�젣
						//FileUtils.deleteQuietly(targetFile);	//���옣�맂 �쁽�옱 �뙆�씪 �궘�젣
						e.printStackTrace();
						break;
					}
				}
				strResult = "{ \"result\":\"OK\" }";
			}
			// �뙆�씪 �븘臾닿쾬�룄 泥⑤� �븞�뻽�쓣�븣 �깂�떎.(寃뚯떆�뙋�씪�븣, �뾽濡쒕뱶 �뾾�씠 湲��쓣 �벑濡앺븯�뒗寃쎌슦)
			else
				strResult = "{ \"result\":\"OK\" }";
		}catch(Exception e){
			e.printStackTrace();
		}
		return strResult;
	}
}
