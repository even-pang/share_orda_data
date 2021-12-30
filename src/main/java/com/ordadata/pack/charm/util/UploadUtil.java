/**
 * Class Summary. <br>
 * UploadUtil class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author 이현도
 * @see
 */
package com.ordadata.pack.charm.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ordadata.pack.charm.db.DbController;
import com.ordadata.pack.charm.db.DbDao;

@Component
public class UploadUtil {
    
    private String mUploadPath    = "";
    private String mDirectory     = "/upload/";
    private int   mMByte         = 100 ;
    private int   mLimitFileSize = ( mMByte * 1024 * 1024 ); // 100Mbyte
    
    @Resource(name="dbDao")
    private DbDao m_dbDao;
    
    public void setDbDao(DbDao dbDao) {
    	m_dbDao = dbDao;
    }
    
    public UploadUtil() {}
    
    public UploadUtil(HttpServletRequest servletRequest) {
    	init(servletRequest);
    }
    
    public UploadUtil(HttpServletRequest servletRequest,String szUploadPath, String szDir, int LimitFileSize) {
    	mUploadPath    = szUploadPath;
    	mDirectory     = szDir;
        mLimitFileSize = LimitFileSize;    
    	init(servletRequest);
    }
    
    public UploadUtil( HttpServletRequest servletRequest, String szUploadPath, String szDir ) {
    	mUploadPath    = szUploadPath;
    	mDirectory     = szDir;    
    	init(servletRequest);
    }    
    
    public UploadUtil(HttpServletRequest servletRequest, String szDir ) {
    	mDirectory     = szDir;    
    	init(servletRequest);
    }    
    
    public void init(HttpServletRequest servletRequest)
    {
    	String strOsName = System.getProperty("os.name").toUpperCase();
    	
    	if ( "".equals(mUploadPath) && strOsName.indexOf("WINDOW") > -1 ) {
    		mUploadPath = servletRequest.getRealPath("/");
    	}
    	
    	if ( "".equals(mUploadPath) ) {
    		mUploadPath = servletRequest.getRealPath("/");
    	}
    	
    	if ( "".equals(mDirectory)) {
    		mDirectory = "/upload/";
    	}
    	
    	// System.out.println("OS[" + strOsName + "] DIR[" + mUploadPath + mDirectory + "]");
    	
    }
    
    public Map getRequestMap(HttpServletRequest servletRequest) {
    	Map reqMap = new HashMap();
    	Vector  vtParam = new Vector();
    	Vector  vtValue = new Vector();
    	
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)servletRequest;
            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
            List<MultipartFile> fileList = null;
            
            while(iterator.hasNext()) {
            	fileList = multipartHttpServletRequest.getFiles(iterator.next());
            	
            	for(MultipartFile multipartFile : fileList) {
            		String fileName = multipartFile.getOriginalFilename();
                    String strRealName = CommonUtil.getUniqueValue() + "." + CommonUtil.getFileExt(fileName);

    				if(reqMap.containsKey("file_exits_nm")) {  // 파일 변경시 이전 내용을 지울 때 사용한다.
    				    ((List) reqMap.get("file_exits_nm")).add(fileName);
    				} else {
    				    reqMap.put("file_exits_nm", new ArrayList());
    				    ((List) reqMap.get("file_exits_nm")).add(fileName);
    				}

                    File file = new File(mUploadPath + mDirectory +	 strRealName);
                    long size = multipartFile.getSize();
                    System.out.println("param : name="+ multipartFile.getName() +" // file: name=" + fileName + "; ext=" + CommonUtil.getFileExt(fileName)+"; size="+ size);
                    
                    if(!(fileName == null || fileName.equals(""))) {
                      if((size <= 0 && size > mLimitFileSize )) {
                            reqMap.put("deny_file", CommonUtil.setNullVal(reqMap.get("deny_file"))+","+fileName);
                        } else {
                            try {
    							if(file.exists() == false) {
    								file.mkdirs();
    							}
    							
    							multipartFile.transferTo(file);
    						} catch (Exception e) {
    							System.out.println("==== 폴더가 존재하는지 확인해 주세요" + mUploadPath + mDirectory + fileName);
    							System.out.println(e.toString());
    							break;
    						}
                            
                            if(CommonUtil.setNullVal(multipartFile.getName()).indexOf("VBN_FORM_MediaFile")==0) {
                                if(reqMap.containsKey("replace_file_nm")) {
                                    ((List) reqMap.get("replace_file_nm")).add(mDirectory+fileName);
                                } else {
                                    reqMap.put("replace_file_nm", new ArrayList());
                                    ((List) reqMap.get("replace_file_nm")).add(mDirectory+fileName);
                                }
                            }

                            if(reqMap.containsKey("file_nm")) {
                                ((List) reqMap.get("file_nm")).add(fileName);
                                ((List) reqMap.get("file_realnm")).add(strRealName);
                                ((List) reqMap.get("file_ext")).add(CommonUtil.getFileExt(fileName));
                                ((List) reqMap.get("file_size")).add(String.valueOf(size));
                            } else {
                                reqMap.put("file_nm", new ArrayList());
                                ((List) reqMap.get("file_nm")).add(fileName);
                                
                                reqMap.put("file_realnm", new ArrayList());
                                ((List) reqMap.get("file_realnm")).add(strRealName);                                
                                
                                reqMap.put("file_ext", new ArrayList());
                                ((List) reqMap.get("file_ext")).add(CommonUtil.getFileExt(fileName));
                                
                                reqMap.put("file_size", new ArrayList());
                                ((List) reqMap.get("file_size")).add(String.valueOf(size));
                            }  // if(reqMap.containsKey("file_nm"))
                    
                        } // if((size <= 0
                    } else {
                        if(reqMap.containsKey("file_nm")) {
                            ((List) reqMap.get("file_nm")    ).add("");
                            ((List) reqMap.get("file_realnm")).add("");
                            ((List) reqMap.get("file_ext")   ).add("");
                            ((List) reqMap.get("file_size")  ).add("");
                        } else {
                        	
                            reqMap.put("file_nm", new ArrayList());
                            ((List) reqMap.get("file_nm")).add("");
                            
                            reqMap.put("file_realnm", new ArrayList());
                            ((List) reqMap.get("file_realnm")).add("");                            
                            
                            reqMap.put("file_ext", new ArrayList());
                            ((List) reqMap.get("file_ext")).add("");
                            
                            reqMap.put("file_size", new ArrayList());
                            ((List) reqMap.get("file_size")).add("");
                        }  
                    }
            	}
                
            } // while 문장 끝
            
            Enumeration<String> enumeration = multipartHttpServletRequest.getParameterNames();
            while(enumeration.hasMoreElements()) {
            	String paramKey = enumeration.nextElement();
            	String[] arrVal = multipartHttpServletRequest.getParameterValues(paramKey);
            	
             	if (arrVal.length > 1) {
             		for(int i = 0; i < arrVal.length; i++) {
             			vtParam.add(paramKey);
             			vtValue.add(arrVal[i]);
             		}
             	} else {
             		vtParam.add(paramKey);
             		vtValue.add(arrVal[0] == null ? "" : arrVal[0].trim());
             	}
            }
            
         	//<form> 테크에서 같은 필드를 여러번 사용한 경우 List로 담고 한개만 있는 경우에는 Map로 사용한다.
           	for(int nLoop=0; nLoop < vtParam.size(); nLoop++){
        		String strParam = vtParam.get(nLoop).toString();
        		String strValue = CommonUtil.removeXSS(vtValue.get(nLoop).toString());
        		
                if(reqMap.containsKey(strParam)) {
                	if ( isMapType(vtParam, strParam) ) {  // 맵 형태인가
                		reqMap.put(strParam, strValue);
                	} else {
                        ((List) reqMap.get(strParam)).add(strValue);
                	}
                } else {
                	if ( isMapType(vtParam, strParam) ) {
                		reqMap.put(strParam, strValue);
                	} else  {
                        reqMap.put(strParam, new ArrayList());
                        ((List) reqMap.get(strParam)).add(strValue);
                	}
                }
        	 }
        	          
            return reqMap;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".getRequestMap() : "+ e.toString());
            e.printStackTrace();
            reqMap.put("FILE_OVER_SIZE", "최대 첨부 가능 사이즈는 " + mMByte + "Mbyte 입니다."); 

            return reqMap;
        }
    }
    
    /*public Map getRequestMap(HttpServletRequest servletRequest) {
    	HashMap reqMap  = new HashMap();
    	Vector  vtParam = new Vector();
    	Vector  vtValue = new Vector();
        try {
            MultipartParser mp = new MultipartParser(servletRequest, mLimitFileSize , true, false, "UTF-8");
            
            Part part = null;
            
            String permitExt = "MP4,FLV,XLS,XLSX,DOC,DOCX,PPT,PPTX,GIF,TIFF,TIF,JPG,JPEG,HWP,GUL,TXT,PDF,ZIP,SWF,PNG,BMP,EPS";

            while((part = mp.readNextPart()) != null) 
            {      
                if(part.isFile()) 
                {
                    FilePart filePart = (FilePart) part;
                    
                    String fileName = filePart.getFileName();
                    String strRealName = CommonUtil.getUniqueValue() + "." + CommonUtil.getFileExt(fileName);

					if(reqMap.containsKey("file_exits_nm")) {  // 파일 변경시 이전 내용을 지울 때 사용한다.
					    ((List) reqMap.get("file_exits_nm")).add(fileName);
					} else {
					    reqMap.put("file_exits_nm", new ArrayList());
					    ((List) reqMap.get("file_exits_nm")).add(fileName);
					}

                    File file = new File(filePart.getFilePath());
                    long size = file.length();
 
                    //System.out.println("part.isFile() param : name="+ filePart.getName() +" // file: name=" + fileName + "; ext=" + CommonUtil.getFileExt(fileName)+"; size="+ size);
                    if(fileName != null) 
                    {
                      if((size <= 0 && size > mLimitFileSize ) || permitExt.indexOf(CommonUtil.getFileExt(fileName).toUpperCase()) < 0) {
                            reqMap.put("deny_file", CommonUtil.setNullVal(reqMap.get("deny_file"))+","+fileName);
                          //  System.out.println("=====> deny_file : "+ fileName +", size : "+ size + ", ext : "+ CommonUtil.getFileExt(fileName));
                        } else {
                            try {
								filePart.setRenamePolicy(new DefaultFileRenamePolicy());
								file = new File(mUploadPath + mDirectory);
								
								if(file.exists() == false) {
									file.mkdirs();
								}
								
								file = new File(mUploadPath + mDirectory + strRealName);
								
								size = filePart.writeTo(file);
								strRealName = filePart.getFileName();
							} catch (Exception e) {
								System.out.println("==== 폴더가 존재하는지 확인해 주세요" + mUploadPath + mDirectory + fileName);
								System.out.println(e.toString());
								break;
							}
                            
                            if(CommonUtil.setNullVal(filePart.getName()).indexOf("VBN_FORM_MediaFile")==0) {
                                if(reqMap.containsKey("replace_file_nm")) {
                                    ((List) reqMap.get("replace_file_nm")).add(mDirectory+fileName);
                                } else {
                                    reqMap.put("replace_file_nm", new ArrayList());
                                    ((List) reqMap.get("replace_file_nm")).add(mDirectory+fileName);
                                }
                            }

                            if(reqMap.containsKey("file_nm")) {
                                ((List) reqMap.get("file_nm")).add(fileName);
                                ((List) reqMap.get("file_realnm")).add(strRealName);
                                ((List) reqMap.get("file_ext")).add(CommonUtil.getFileExt(fileName));
                                ((List) reqMap.get("file_size")).add(String.valueOf(size));
                            } else {
                                reqMap.put("file_nm", new ArrayList());
                                ((List) reqMap.get("file_nm")).add(fileName);
                                
                                reqMap.put("file_realnm", new ArrayList());
                                ((List) reqMap.get("file_realnm")).add(strRealName);                                
                                
                                reqMap.put("file_ext", new ArrayList());
                                ((List) reqMap.get("file_ext")).add(CommonUtil.getFileExt(fileName));
                                
                                reqMap.put("file_size", new ArrayList());
                                ((List) reqMap.get("file_size")).add(String.valueOf(size));
                            }  // if(reqMap.containsKey("file_nm"))
                    
                        } // if((size <= 0
                    } else {  
                        if(reqMap.containsKey("file_nm")) {
                            ((List) reqMap.get("file_nm")    ).add("");
                            ((List) reqMap.get("file_realnm")).add("");
                            ((List) reqMap.get("file_ext")   ).add("");
                            ((List) reqMap.get("file_size")  ).add("");
                        } else {
                        	
                            reqMap.put("file_nm", new ArrayList());
                            ((List) reqMap.get("file_nm")).add("");
                            
                            reqMap.put("file_realnm", new ArrayList());
                            ((List) reqMap.get("file_realnm")).add("");                            
                            
                            reqMap.put("file_ext", new ArrayList());
                            ((List) reqMap.get("file_ext")).add("");
                            
                            reqMap.put("file_size", new ArrayList());
                            ((List) reqMap.get("file_size")).add("");
                        }  
                    }
                } else if(part.isParam()) {
                    ParamPart paramPart = (ParamPart) part;
                	vtParam.add(paramPart.getName());
                	vtValue.add(paramPart.getStringValue());
                                        
                     //System.out.println("param: name=" + CommonUtil.setNullVal(paramPart.getName()) + "; value=" + CommonUtil.setNullVal(paramPart.getStringValue()));
                }
            } // while 문장 끝
            
            //<form> 테크에서 같은 필드를 여러번 사용한 경우 List로 담고 한개만 있는 경우에는 Map로 사용한다.
           	for(int nLoop=0; nLoop < vtParam.size(); nLoop++){
        		String strParam = vtParam.get(nLoop).toString();
        		String strValue = CommonUtil.removeXSS(vtValue.get(nLoop).toString());
        		
                if(reqMap.containsKey(strParam)) {
                	if ( isMapType(vtParam, strParam) ) {  // 맵 형태인가
                		reqMap.put(strParam, strValue);
                	} else {
                        ((List) reqMap.get(strParam)).add(strValue);
                	}
                } else {
                	if ( isMapType(vtParam, strParam) ) {
                		reqMap.put(strParam, strValue);
                	} else  {
                        reqMap.put(strParam, new ArrayList());
                        ((List) reqMap.get(strParam)).add(strValue);
                	}
                }
        	 }
        	          
            return reqMap;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".getRequestMap() : "+ e.toString());
            e.printStackTrace();
            reqMap.put("FILE_OVER_SIZE", "최대 첨부 가능 사이즈는 " + mMByte + "Mbyte 입니다."); 

            return reqMap;
        }
    }*/
    
    private boolean isMapType(Vector vParam, String strName)
    {
    	int nCnt = 0;
        try
        {
        	if ( "file_gbn".equals(strName) || "file_no".equals(strName) || "height".equals(strName) || "width".equals(strName))  // File에 관련된 GBN과 SEQ는 List에 담는다.
        	   return false;
        	
	    	for(int nLoop=0; nLoop < vParam.size(); nLoop++){
	    		if ( strName.equals(vParam.get(nLoop).toString()))  
	    			nCnt++;
	    	}
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".isListType() : "+ e.toString());
            e.printStackTrace();
        }    		
    	return (nCnt < 2) ? true : false;
    }
    
    public Map uploadProcess(Map reqMap, HttpServletRequest servletRequest) {
    	
    	return uploadProcess(reqMap, servletRequest, "");
    }
    
    public Map uploadProcess(Map reqMap, HttpServletRequest servletRequest, String strRelKey) {
        try {
        	
        	DbController cSvc = null;
        	
        	if ( m_dbDao == null )
        		cSvc = new DbController();
        	
        	List lstRmFileSeq     = null;
        	
            if ("".equals(strRelKey) )
            	strRelKey = CommonUtil.setNullVal(reqMap.get("rel_key"));
            else           
                reqMap.put("rel_key", strRelKey);             
            
            String strRelTbl      = CommonUtil.setNullVal(reqMap.get("rel_tbl"));
            
            List lstFileSeq       = (List)reqMap.get("file_no");
            List lstFileNm        = (List)reqMap.get("file_nm");
            List lstFileRealNm    = (List)reqMap.get("file_realnm");
            List lstFileExistsNm  = (List)reqMap.get("file_exits_nm");
            List lstReplaceFileNm = (List)reqMap.get("replace_file_nm");
 
            String strRmSeq       =  CommonUtil.setNullVal(reqMap.get("rm_no"));
            String[] arrRmSeq     = strRmSeq.split(",");
            
            if ( arrRmSeq.length > 1)
                lstRmFileSeq     = (List)reqMap.get("rm_no");
            else {
            	lstRmFileSeq  = new  ArrayList();
            	lstRmFileSeq.add(strRmSeq);
            }
            
            if(reqMap.get("file_des1") != null)
            	fileDescUpdate(reqMap); // 파일의 설명 부분을 저장한다. 
            
            Map sqlMap = new HashMap();

            if(lstRmFileSeq != null && !lstRmFileSeq.isEmpty()) {  // 삭제를 선택한 것을 지운다.
                sqlMap.put("lstSeq", lstRmFileSeq);
                removeFile(sqlMap);
                sqlMap.clear();
            }            
            
            if(!"".equals(strRelTbl) && !"".equals(strRelKey) && lstFileNm != null) 
            {
                sqlMap.put("rel_tbl", strRelTbl);
                sqlMap.put("rel_key",  strRelKey);
                
                int index = 0;
                
                List lstDesc1 = null;
                List lstDesc2 = null;
                
                try {
                  lstDesc1 = ((ArrayList) reqMap.get("file_des1"));
                  lstDesc2 = ((ArrayList) reqMap.get("file_des2"));
                } catch (Exception e) {}
                
                for(int iLoop=0; iLoop< lstFileNm.size();iLoop++) {
                    if(!"".equals(CommonUtil.setNullVal(lstFileNm.get(iLoop)))) 
                    {
                        sqlMap.put("file_nm",     mDirectory + lstFileNm.get(iLoop));
                        sqlMap.put("file_realnm", mDirectory + lstFileRealNm.get(iLoop));   
                        sqlMap.put("file_ext",    ((ArrayList) reqMap.get("file_ext")).get(iLoop));                          
                        sqlMap.put("file_size",   ((ArrayList) reqMap.get("file_size")).get(iLoop));     
                        
                        // 수정한 부분.................................
                        for(int j = index ; j < ((List)reqMap.get("file_exits_nm")).size() ; j++){
                            if(CommonUtil.setNullVal(((ArrayList) reqMap.get("file_exits_nm")).get(j), "") != ""){
                               int[] iSize = {0, 0};
                              // iSize = CommonUtil.getImgSize( mUploadPath + "/" + mDirectory + (String)((List)reqMap.get("file_exits_nm")).get(j) );
                              // sqlMap.put("width",    (reqMap.get("width")    != null) ? iSize[0] : "0");      
                              // sqlMap.put("height",   (reqMap.get("height")   != null) ? iSize[1] : "0");

                               sqlMap.put("file_gbn",  ((ArrayList) reqMap.get("file_gbn")).get(j) );
                               
                               if ( lstDesc1 != null && lstDesc1.size() > j )
                                    sqlMap.put("file_des1", lstDesc1.get(j) );
                               
                               if ( lstDesc2 != null && lstDesc2.size() > j )
                                   sqlMap.put("file_des2", lstDesc2.get(j) );                               
                            
                               index = j+1;    
                               break;
                            }
                        }
                        
                        sqlMap.put("file_no", (reqMap.get("file_no") != null) ? CommonUtil.setNullVal(((ArrayList) reqMap.get("file_no")).get(iLoop), "") : "");
                        
                        if ( m_dbDao != null ) {
                        	m_dbDao.insert(sqlMap, "common.insertUpload"); 
                        } else {
                        	cSvc.dbInsert(sqlMap, "common.insertUpload"); 
                        }
                        
                                 
                                         
                        // 추가한 부분.......................................
                        //if(!CommonUtil.setNullVal(reqMap.get("thumnail")).equals("")){
                        //createThumnail(sqlMap);    
                       // }
                        
                    }
                }  // For 
            }
                       
            if ( lstFileExistsNm != null && lstFileSeq != null)  // 수정대상 파일을 삭제한다.
            { 
                for(int iLoop=0; iLoop< lstFileExistsNm.size(); iLoop++) {
                	String strFileEName = CommonUtil.nvl(lstFileExistsNm.get(iLoop));
                	String strFileSeq   = CommonUtil.nvl(lstFileSeq.get(iLoop));

                    if(!"".equals(strFileEName) && !"".equals(strFileSeq)) 
                    {                    	
                        if(!CommonUtil.setNullVal(reqMap.get("thumnail")).equals("")){
                            removeFile(strFileSeq, reqMap.get("thumnail").toString());
                        }else{
                            removeFile(strFileSeq);
                        }
                    }
                }            	
            }
            	        
            return reqMap;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".uploadProcess() : "+ e.toString());
            e.printStackTrace();
            return null;
        }
    }
    
    public void fileDescUpdate(Map reqMap)
    {
    	List lstDesc1    = null;
    	List lstDesc2    = null;
    	List lstFileSeq  = null;
    	
    	try {
    		lstFileSeq  = (ArrayList)reqMap.get("file_no"); 
    		lstDesc1    = (ArrayList)reqMap.get("file_des1");
            lstDesc2    = (ArrayList)reqMap.get("file_des2");
    	} catch ( Exception  e) {
    		
    	}
        
        if ( lstFileSeq == null || lstFileSeq.isEmpty())
        	return;
        
        try {
        	
        	DbController cSvc = null;
        	
        	if ( m_dbDao == null )
        		cSvc = new DbController();
        	
			for (int nLoop=0; nLoop < lstFileSeq.size(); nLoop++)
			{
	 
				String strFileNo = (String)lstFileSeq.get(nLoop);
				if ( "".equals(strFileNo))
					continue;
				
				Map paramMap   = new HashMap();
				paramMap.put("file_des1", lstDesc1.get(nLoop));
				if ( lstDesc2 !=null ) {
				   paramMap.put("file_des2", lstDesc2.get(nLoop));
				}
				paramMap.put("file_no",   lstFileSeq.get(nLoop));
				 
				if (m_dbDao != null ) {
					m_dbDao.update(paramMap, "common.updateDescUpload");
				} else {
				    cSvc.dbUpdate(paramMap, "common.updateDescUpload");
				}
			}
		} catch (Exception e) {
			 
			//e.printStackTrace();
		}
        	 
    }
    
    public void removeFile(Map reqMap) {
    	try {
	        if(reqMap != null) {
	        	
	        	DbController cSvc = null;
	        	
	        	if ( m_dbDao == null )
	        		cSvc = new DbController();
	                
	        	List lstUpload = null;
	        	if ( m_dbDao != null ) {
	        		lstUpload = m_dbDao.list(reqMap, "common.getUploadFile");
	        	} else {
	              lstUpload = cSvc.dbList(reqMap, "common.getUploadFile");
	        	}
	            
	            
	            if(lstUpload != null && !lstUpload.isEmpty()) 
	            {
	            	for(int i=0;i<lstUpload.size();i++) 
	            	{
		                Map dbRow = (Map) lstUpload.get(i);               
		                //File delFile = new File(UPLOADPATH+"\\"+ CommonUtil.setNullVal(dbRow.get("FILE_NM")));
	                    File delFile = new File(mUploadPath+ CommonUtil.setNullVal(dbRow.get("FILE_REALNM")));
		                if(delFile.exists()) delFile.delete();
		                
		                
		                // 추가한 부분...........
		                if(!CommonUtil.setNullVal(reqMap.get("thumnail")).equals(""))
		                {
		                    StringTokenizer st = new StringTokenizer( CommonUtil.setNullVal(dbRow.get("FILE_REALNM")), ".");
		                    String file_nm = st.nextToken();
		                    File delThumnailFile = new File(mUploadPath + file_nm+"_thumb.jpg");
		                    if(delThumnailFile.exists()) delThumnailFile.delete();
		                }
		                
		                reqMap.put("file_no", dbRow.get("FILE_NO"));
		                
		                if ( m_dbDao != null) {
		                	m_dbDao.delete(reqMap, "common.removeUpload");	
		                } else {
		                    cSvc.dbDelete(reqMap, "common.removeUpload");
		                }
	            	}     
	            }
	            
	            
	        }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".removeFile()1 : "+ e.toString());
            e.printStackTrace();
        }	        
    }
    
    public void removeFile(String strFileSeq) {
        try {
	        if(!"".equals(CommonUtil.setNullVal(strFileSeq))) {
	            Map reqMap = new HashMap();
	            reqMap.put("file_no", strFileSeq);
	            removeFile(reqMap);
	        }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".removeFile()2 : "+ e.toString());
            e.printStackTrace();
        }	 	        
    }
    

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 추가한 부분
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeFile(String strFileSeq, String thumNail) {
        try {
            if(!"".equals(CommonUtil.setNullVal(strFileSeq))) {
                Map reqMap = new HashMap();
                reqMap.put("file_no", strFileSeq);
                reqMap.put("thumnail", thumNail);
                removeFile(reqMap);
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".removeFile()2 : "+ e.toString());
            e.printStackTrace();
        }               
    }
    public String addFileHtml(List lstFile, int nFileCnt) {
        return addFileHtml(lstFile, nFileCnt, "");
    }
    public String addFileHtml(List lstFile, int nFileCnt, String strFileGbn) {
    	
	    String  strCompare = "";
	    StringBuffer sbHtml = new StringBuffer();
	    int  nFileSize = 0;
	 	 if( lstFile != null && lstFile.size() > 0) {
	  
	 		 for( int iLoop = 0; iLoop < lstFile.size(); iLoop++ ) {
	 			 Map fileMap = ( Map ) lstFile.get( iLoop );
	 			 strCompare = CommonUtil.getNullTrans(fileMap.get("FILE_GBN"), "");
	 			 
	 			 if (strFileGbn.equals(strCompare)) {
	 		    	sbHtml.append("<TR> \n");
	 		    	sbHtml.append("   <Th height=30>첨부파일</Th> \n");
	 		    	sbHtml.append("    <TD>");	 				 
	 			    sbHtml.append("         <input type='file'    name='file_nm'   style='width:250px;' >\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_no'  value='" + fileMap.get("FILE_NO") + "'>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n<br />");	 			    
	 			    sbHtml.append(CommonUtil.getFileName((String)fileMap.get("FILE_NM")) + "&nbsp;\n");
	 			    sbHtml.append("         <input type='checkbox' name='rm_no'   value='" + fileMap.get("FILE_NO") +"'/>[삭제]\n");
	 		    	sbHtml.append("    </TD>");
	 			    sbHtml.append("<TR> \n");
	 			    
	 			   nFileSize++;
	            } 
	        }       	  
	    } 
	   
        for(int iLoop = nFileSize; iLoop < nFileCnt; iLoop++)
        {
	    	sbHtml.append("<TR> \n");
	    	sbHtml.append("   <Th height=30>첨부파일</Th> \n");
	    	sbHtml.append("    <TD>");	 				     	
		    sbHtml.append("        <input type='file'    name='file_nm'   style='width:300px;' >\n");
		    sbHtml.append("        <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
		    sbHtml.append("        <input type='hidden'  name='file_no'  value=''>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n");		    
	    	sbHtml.append("    </TD>");
		    sbHtml.append("<TR> \n"); 			    
        }
        
        return sbHtml.toString();
    }
    
    public String addInnerFileHtml(List lstFile, int nFileCnt) {
        return addInnerFileHtml(lstFile, nFileCnt, "", false);
    }
    
    public String addInnerFileHtml(List lstFile, int nFileCnt, String strFileGbn) {
    	return addInnerFileHtml(lstFile, nFileCnt, strFileGbn, false);
    }
    
    public String addInnerFileHtml(List lstFile, int nFileCnt, String strFileGbn, boolean bDesc) {
    	
	    String  strCompare = "";
	    StringBuffer sbHtml = new StringBuffer();
	    int  nFileSize = 0;
	 	 if( lstFile != null && lstFile.size() > 0) {
	  
	 		 for( int iLoop = 0; iLoop < lstFile.size(); iLoop++ ) {
	 			 Map fileMap = ( Map ) lstFile.get( iLoop );
	 			 strCompare = CommonUtil.getNullTrans(fileMap.get("FILE_GBN"), "");
	 			 
	 			 if (strFileGbn.equals(strCompare)) {
	 				if ( nFileSize > 0 ) {
	 					sbHtml.append("<br/>\n");
	 				}
	 				 
	 			    sbHtml.append("         <input type='file'    name='file_nm'   style='width:150px;height:20px' >\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_no'  value='" + fileMap.get("FILE_NO") + "'>\n");
	 			    sbHtml.append(CommonUtil.getFileName((String)fileMap.get("FILE_NM")) + "&nbsp;\n");
	 			    
	 			    sbHtml.append( ((bDesc) ? "&nbsp;&nbsp;설명:":"") +"        <input type='" + ((bDesc) ? "text" : "hidden") + "' name='file_des1'   style='width:150px;' maxlength='150' value='" + CommonUtil.nvl(fileMap.get("DES1")) + "'>\n");
	 			    
	 			    
	 			    sbHtml.append("         <input type='checkbox' name='rm_no'   value='" + fileMap.get("FILE_NO") +"'/>[삭제]\n");
	 			   nFileSize++;
	            } 
	        }       	  
	    } 
	 	 
        for(int iLoop = nFileSize; iLoop < nFileCnt; iLoop++)
        {
			if ( iLoop > 0  ) {
 				sbHtml.append("<br/>\n");
 			}
			
		    sbHtml.append("        <input type='file'    name='file_nm'   style='width:150px;' >\n");
		    sbHtml.append("        <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
		    sbHtml.append("        <input type='hidden'  name='file_no'  value=''>\n");
		    
		    sbHtml.append(((bDesc) ? "&nbsp;&nbsp;설명:":"") + "        <input type='" + ((bDesc) ? "text" : "hidden") + "'    name='file_des1'   style='width:150px;' maxlength='150'>\n");
		    
        }
        
        return sbHtml.toString();
    }
        
    public String addUnivFileHtml(List lstFile, int nFileCnt) {
        return addUnivFileHtml(lstFile, nFileCnt, "");
    }
    public String addUnivFileHtml(List lstFile, int nFileCnt, String strFileGbn) {
    	
	    String  strCompare = "";
	    StringBuffer sbHtml = new StringBuffer();
	    int  nFileSize = 0;
	 	 if( lstFile != null && lstFile.size() > 0) {
	  
	 		 for( int iLoop = 0; iLoop < lstFile.size(); iLoop++ ) {
	 			 Map fileMap = ( Map ) lstFile.get( iLoop );
	 			 strCompare = CommonUtil.getNullTrans(fileMap.get("FILE_GBN"), "");
	 			 
	 			 if (strFileGbn.equals(strCompare)) {
	 				if ( nFileSize > 0 ) {
	 					sbHtml.append("\n");
	 				}
	 				 
	 				sbHtml.append("  <tr class='bgtitle" + (((iLoop % 2 ) == 0 ) ? "2" : "3") + " bdbnon'>  \n");
	 				sbHtml.append("     <td class=' bdlnon  ac fb fc_blue '>첨부파일</td>\n");
	 				sbHtml.append("     <td class='al'>\n");
	 				sbHtml.append("  	  <div class='upfile'>\n");
	 			    sbHtml.append("         <input type='file'    name='file_nm'   style='width:250px;' class='input_text' >\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_no'  value='" + fileMap.get("FILE_NO") + "'>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n<br />");	 			    
	 			    sbHtml.append(CommonUtil.getFileName((String)fileMap.get("FILE_NM")) + "&nbsp;\n");
	 			    sbHtml.append("         <input type='checkbox' name='rm_no'   value='" + fileMap.get("FILE_NO") +"'/>[삭제]\n");

	 				sbHtml.append("  	  </div>\n");
	 				sbHtml.append("     </td>\n");
	 				sbHtml.append("  </tr>	\n");			
	 				
	 			    
	 			   nFileSize++;
	            } 
	        }       	  
	    } 
	 	 
        for(int iLoop = nFileSize; iLoop < nFileCnt; iLoop++)
        {
			if ( iLoop > 0  ) {
 				sbHtml.append("\n");
 			}
			
			 
				sbHtml.append("  <tr class='bgtitle" + (((iLoop % 2 ) == 0 ) ? "2" : "3") + " bdbnon'>  \n");
				sbHtml.append("     <td class=' bdlnon  ac fb fc_blue '>첨부파일</td>\n");
				sbHtml.append("     <td class='al'>\n");
				sbHtml.append("  	  <div class='upfile'>\n");
			    sbHtml.append("        <input type='file'    name='file_nm'   style='width:300px;' class='input_text' >\n");
			    sbHtml.append("        <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
			    sbHtml.append("        <input type='hidden'  name='file_no'  value=''>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n");			    
				sbHtml.append("  	  </div>\n");
				sbHtml.append("     </td>\n");
				sbHtml.append("  </tr>	\n");				
			
        }
        
        return sbHtml.toString();
    }    
    
   
    public String addHomeFileHtml(List lstFile, int nFileCnt) {
        return addHomeFileHtml(lstFile, nFileCnt, "");
    }
    public String addHomeFileHtml(List lstFile, int nFileCnt, String strFileGbn) {
    	
	    String  strCompare = "";
	    StringBuffer sbHtml = new StringBuffer();
	    int  nFileSize = 0;
	 	 if( lstFile != null && lstFile.size() > 0) {
	  
	 		 for( int iLoop = 0; iLoop < lstFile.size(); iLoop++ ) {
	 			 Map fileMap = ( Map ) lstFile.get( iLoop );
	 			 strCompare = CommonUtil.getNullTrans(fileMap.get("FILE_GBN"), "");
	 			 
	 			 if (strFileGbn.equals(strCompare)) {
	 				if ( nFileSize > 0 ) {
	 					sbHtml.append("\n");
	 				}
	 				 
	 				sbHtml.append(" <tr>\n");
	 				sbHtml.append("   <td scope='col' abbr='첨부파일'><label for=\"file_nm\">첨부파일</label></td>\n");
	 				sbHtml.append("   <td class='wrt' colspan='3'>\n");
	 			    sbHtml.append("         <input type='file'    name='file_nm'  id='file_nm'   style='width:100%;height:auto' class='write' >\n");
	 			    sbHtml.append(CommonUtil.getFileName((String)fileMap.get("FILE_NM")) + "&nbsp;\n");
	 			    sbHtml.append("         <input type='checkbox' name='rm_no'   value='" + fileMap.get("FILE_NO") +"' style='width:12px;' />[삭제]\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_no'  value='" + fileMap.get("FILE_NO") + "'>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n <br />");	 			    
	 				sbHtml.append("   </td>				          \n");
	 				sbHtml.append(" </tr>\n");		 				
	 			    
	 			   nFileSize++;
	            } 
	        }       	  
	    } 
	 	 
        for(int iLoop = nFileSize; iLoop < nFileCnt; iLoop++)
        {
			if ( iLoop > 0  ) {
 				sbHtml.append("\n");
 			}
			
				sbHtml.append(" <tr>\n");
 				sbHtml.append("   <td scope='col' abbr='첨부파일'><label for=\"file_nm\">첨부파일</label></td>\n");
 				sbHtml.append("   <td class='wrt' colspan='3'>\n");
			    sbHtml.append("        <input type='file'    name='file_nm'  id='file_nm'   style='width:100%;height:auto' class='write' >\n");
			    sbHtml.append("        <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
			    sbHtml.append("        <input type='hidden'  name='file_no'  value=''>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n");			    
 				sbHtml.append("   </td>				          \n");
 				sbHtml.append(" </tr>\n");		 				
			
        }
        
        return sbHtml.toString();
    }        
    
    
    public String addLabFileHtml(List lstFile, int nFileCnt) {
        return addLabFileHtml(lstFile, nFileCnt, "");
    }
    public String addLabFileHtml(List lstFile, int nFileCnt, String strFileGbn) {
    	
	    String  strCompare = "";
	    StringBuffer sbHtml = new StringBuffer();
	    int  nFileSize = 0;
	 	 if( lstFile != null && lstFile.size() > 0) {
	  
	 		 for( int iLoop = 0; iLoop < lstFile.size(); iLoop++ ) {
	 			 Map fileMap = ( Map ) lstFile.get( iLoop );
	 			 strCompare = CommonUtil.getNullTrans(fileMap.get("FILE_GBN"), "");
	 			 
	 			 if (strFileGbn.equals(strCompare)) {
	 				if ( nFileSize > 0 ) {
	 					sbHtml.append("\n");
	 				}
	 				 
	 				sbHtml.append("<tr class='bdbnon '>\n");
	 				sbHtml.append("<th class=' bdlnon'>첨부파일</th>\n");
	 				sbHtml.append("<td class='al'>\n");
	 				sbHtml.append("	<div class='upfile'>\n");
	 			    sbHtml.append("         <input type='file'    name='file_nm'   style='width:250px;height:20px' class='write' >\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
	 			    sbHtml.append("         <input type='hidden'  name='file_no'  value='" + fileMap.get("FILE_NO") + "'>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n<br />");	 			    
	 			    sbHtml.append(CommonUtil.getFileName((String)fileMap.get("FILE_NM")) + "&nbsp;\n");
	 			    sbHtml.append("         <input type='checkbox' name='rm_no'   value='" + fileMap.get("FILE_NO") +"'/>[삭제]\n");
	 				sbHtml.append("	</div>\n");		
	 				sbHtml.append("  </td>\n");
	 				sbHtml.append("</tr>\n");  	 				
	 			    
	 			   nFileSize++;
	            } 
	        }       	  
	    } 
	 	 
        for(int iLoop = nFileSize; iLoop < nFileCnt; iLoop++)
        {
			if ( iLoop > 0  ) {
 				sbHtml.append("\n");
 			}
			
				sbHtml.append("<tr class='bdbnon '>\n");
 				sbHtml.append("<th class=' bdlnon'>첨부파일</th>\n");
 				sbHtml.append("<td class='al'>\n");
 				sbHtml.append("	<div class='upfile'>\n");
			    sbHtml.append("        <input type='file'    name='file_nm'   style='width:300px;height:20px' class='write' >\n");
			    sbHtml.append("        <input type='hidden'  name='file_gbn'  value='" + strFileGbn + "'>\n");
			    sbHtml.append("        <input type='hidden'  name='file_no'  value=''>\n");
	 			    sbHtml.append("<br />※ 첨부파일은 최대 10MBytes까지 업로드 가능합니다.\n");			    
 				sbHtml.append("	</div>\n");		
 				sbHtml.append("  </td>\n");
 				sbHtml.append("</tr>\n");  	 				
			
        }
        
        return sbHtml.toString();
    }        
        
    
    
  
    
    
    /**
     * 썸네일 생성
    * @param orgFile 원본 이미지 파일 객체
    * @param destFile 썸네일로 저장될 이미지 파일 객체
    * @param width 줄일 가로 길이
    * @param height 줄일 세로 길이
    * @return 썸네일 파일이름
    * @throws IOException
     */
     public void createThumnail(String orgFileName, String destFileName, int nFixWidth, int nFixHeight, boolean bFixSize)
     {
 
         StringTokenizer st = new StringTokenizer( orgFileName, ".");
         String file_nm = st.nextToken();
        // String strFile = mUploadPath + sqlMap.get("file_nm").toString();    	 
    	 
         File orgFile  = new File (orgFileName);
         File destFile = new File (destFileName);
         int  nRate;
         
    	 try {
			     Image srcImg = null;
			     String suffix = orgFileName.substring(orgFileName.lastIndexOf('.') + 1).toLowerCase();
			
			     if (suffix.equals("jpg") || suffix.equals("bmp") || suffix.equals("png") || suffix.equals("gif")) {
			        srcImg = ImageIO.read(orgFile);
			     } else {
			      //  srcImg = new ImageIcon(orgFile.toString()).getImage();
			    	 return;
			     }
			  
                 int nArrSize[] = getImageSize(srcImg, nFixWidth, nFixHeight, bFixSize);
			     int destWidth = nArrSize[0], destHeight = nArrSize[1];
			
			     Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
			     int pixels[] = new int[destWidth * destHeight];
			     PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
			     
			     pg.grabPixels();
			     
			     BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			     destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
			     ImageIO.write(destImg, suffix, destFile);
			     
    	 } catch ( Exception e) {
    		  System.out.println(e.toString());
    	 }
   
	     //return destFile.getName();
     }
 
     private int[] getImageSize(Image srcImg,  int nFixWidth, int nFixHeight, boolean bFixSize)
     {
    	 int nArrSize[] = {0,0};
    	 int nRate ;
    	 
    	 try {
    		 
		     int srcWidth  = srcImg.getWidth(null);
		     int srcHeight = srcImg.getHeight(null);
		     
	         if ( bFixSize ) { // 사용자가 지정한 사이즈로 쎔네일을 저장함
	        	 nArrSize[0] = nFixWidth;
	        	 nArrSize[1] = nFixHeight; 
	         } else {
		         if ( srcWidth >= nFixWidth ) // 원하는 길이보다 넓이가 넓은 경우
		         {
		        	 nRate      = ( nFixWidth * 100 ) /  srcWidth  ; 
		        	 srcWidth  = ( srcWidth    *  nRate ) / 100 ; 
		        	 srcHeight = ( srcHeight   *  nRate ) / 100 ; 
		         }
		         
		         if ( srcHeight >= nFixHeight ) // 원하는 길이보다 넓이가 넓은 경우
		         {
		        	 nRate      = ( nFixHeight * 100)  / srcHeight  ; 
		        	 srcWidth  = ( srcWidth   * nRate ) / 100 ; 
		        	 srcHeight = ( srcHeight  * nRate ) / 100 ; 
		         }	
		         
	        	 nArrSize[0] = srcWidth;
	        	 nArrSize[1] = srcHeight; 		         
		         
	         }    
    	 } catch ( Exception e) {
    		 System.out.println("사이즈를 계산하는데 실패하였습니다.");
    	 }
    	 
         return nArrSize;
     }
     
     public String convertHtmlFile(String strFileNo, String strFileName)
     {
    	 String strVal ="";
    	 try {
    		    Runtime runtime = Runtime.getRuntime();
    		    
    		    //strFileName = new String( strFileName.getBytes("EUC-KR"), "8859_1" ); 
    		    
    		    String strFilePath = mUploadPath + strFileName;
    		    String strSavePath = "/upload/convert/";
    		    strFilePath = strFilePath.replace("//", "/");
    		    String strCopyFile = strSavePath + strFileNo + "." + CommonUtil.getFileExt(strFileName);
    		    
    		    fileCopy(strFilePath, mUploadPath + strCopyFile);
    		    
    		    String[] arrParam = {"/bin/synap/sn3hcv.sh",
    		    		             mUploadPath + strCopyFile,
    		    		             mUploadPath + strSavePath};
    		    
    		     ///home/gtie/WebContent/synap/input/synap_hwp_test.hwp /home/gtie/WebContent/synap/output/
    		    
    	        Process process = runtime.exec(arrParam);
    	        InputStream is = process.getInputStream();
    	        InputStreamReader isr = new InputStreamReader(is);
    	        BufferedReader br = new BufferedReader(isr);
    	        String line;
    	 
    	        System.out.printf("Output of running %s is:", Arrays.toString(arrParam));
    	        String strContent = "";
    	       while ((line = br.readLine()) != null) {
    	    	   strContent += line;
    	           System.out.println(line);
    	       }
      
    	       if ( strContent.indexOf("completed") > 0 ) {
    	    	   strVal = strSavePath + CommonUtil.getFileName(strCopyFile) + ".htm";
    	       }

    	       System.out.println("===============>" + strVal);	     	       
    	       
    	 } catch ( Exception e) {}      
    	  
    	 
    	 return strVal;
     }
     
     //파일을 삭제하는 메소드
     public static void fileDelete(String deleteFileName) {
      File I = new File(deleteFileName);
      I.delete();
     }
     
     //파일을 복사하는 메소드
     public  void fileCopy(String inFileName, String outFileName) {
    	 
System.out.println("inFileName==>" + inFileName);    	 
System.out.println("outFileName==>" + outFileName);    	 
    	 
      try {
       FileInputStream fis = new FileInputStream(inFileName);
       FileOutputStream fos = new FileOutputStream(outFileName);
       
       int data = 0;
       while((data=fis.read())!=-1) {
        fos.write(data);
       }
       fis.close();
       fos.close();
       
      } catch (IOException e) {
       e.printStackTrace();
      }
     }     
     
   //파일을 이동하는 메소드
     public void fileMove(String inFileName, String outFileName) {
      try {
       FileInputStream fis = new FileInputStream(inFileName);
       FileOutputStream fos = new FileOutputStream(outFileName);
       
       int data = 0;
       while((data=fis.read())!=-1) {
        fos.write(data);
       }
       fis.close();
       fos.close();
       
       //복사한뒤 원본파일을 삭제함
       fileDelete(inFileName);
       
      } catch (IOException e) {
       e.printStackTrace();
      }
     }
     
     //디렉토리의 파일 리스트를 읽는 메소드
     public List<File> getDirFileList(String dirPath)
     {
      // 디렉토리 파일 리스트
      List<File> dirFileList = null;
      
      // 파일 목록을 요청한 디렉토리를 가지고 파일 객체를 생성함
      File dir = new File(dirPath);
      
      // 디렉토리가 존재한다면
      if (dir.exists())
      {
       // 파일 목록을 구함
       File[] files = dir.listFiles();
       
       // 파일 배열을 파일 리스트로 변화함 
      dirFileList = Arrays.asList(files);
      }
      
      return dirFileList;
     }     
     
     

     
     
}