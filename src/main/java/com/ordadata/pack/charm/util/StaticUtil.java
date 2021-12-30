package com.ordadata.pack.charm.util;

import java.util.HashMap;
import java.util.Map;

public class StaticUtil {

	 /* object 선언 */
   private static StaticUtil m_theInstance = null;
   private static Map        m_data = new HashMap();
   private static long       m_startTime= System.currentTimeMillis();


   private  StaticUtil() {
	   m_startTime = System.currentTimeMillis();
   }
   
   /** 
    * Create and Get Instance
    * applied Singleton Pattern.
    */
   public static StaticUtil getInstance() {
       if (m_theInstance == null)
           m_theInstance = new StaticUtil();
       return m_theInstance;        
   }
	
   public static Object getVar(String strKey) {
	   
	   try {
		   
		   if ( m_startTime == 0 )
			   m_startTime = System.currentTimeMillis();
		   
		   long endTime = System.currentTimeMillis();
		   
		   Float fTime = ( endTime - m_startTime )/1000.0f;
		   String strTime = fTime.toString();
		   
		   int nPos = strTime.lastIndexOf( "." ) ;
		   if ( nPos > 0 ) {
		       strTime = strTime.substring( 0,  nPos  ) ;
		   }
		   
		   if ( Integer.parseInt(strTime) >= ( 30 * 60)) { // 30분이 경과한 경우
			   m_startTime = System.currentTimeMillis();
			   m_data.clear();
			   
			   System.out.println(" time check : " + strTime); 		       
			   
			   return null;
		   }
		   
	   } catch (Exception e) {}
	   return m_data.get(strKey);
   }
   
   public static void setVar(String strKey, Object obj) {
	   m_data.put(strKey, obj);
   }
   
   
	
	
}
