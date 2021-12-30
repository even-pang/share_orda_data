package com.ordadata.pack.charm.db;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//@Repository("dbDao")
public class dbDao_old {
	protected Log log = LogFactory.getLog(dbDao_old.class);
	
	//@Autowired
	private SqlSessionTemplate sqlSession;

	protected void printQueryId(String queryId) {
		if (log.isDebugEnabled()) {
			log.debug("\t QueryId \t: " + queryId);
		}
	}

	public Object insert(Object params, String queryId) 
	{
		printQueryId(queryId);
		return sqlSession.insert(queryId, params);
	}

	public Object update(Object params, String queryId) 
	{
		printQueryId(queryId);
		return sqlSession.update(queryId, params);
	}

	public Object delete(Object params, String queryId) 
	{
		printQueryId(queryId);
		return sqlSession.delete(queryId, params);
	}

	public Object delete(String queryId) {
		printQueryId(queryId);
		return sqlSession.delete(queryId);
	}
	
	@SuppressWarnings( "unchecked" )
	public List list(String strXPath )
	{
		List  list = null;
		try
		{
			list = sqlSession.selectList( strXPath);
		}
		finally
		{
			//sqlSession.close( );
		}
		return list;
	}	
	

	@SuppressWarnings( "unchecked" )
	public List list( Map reqMap, String strXPath )
	{
		List  list = null;
		try
		{
			list = sqlSession.selectList(strXPath , reqMap );
		}
		finally
		{
			//sqlSession.close( );
		}
		return list;
	}		
	
 	@SuppressWarnings( "unchecked" )
	public List list( Map reqMap, String strXPath,   int iRowStartPos, int iCount)
	{
		List  list = null;
		
		try
		{
			RowBounds rbs = new RowBounds(iRowStartPos, iCount);
			list = sqlSession.selectList(strXPath , reqMap, rbs );
 
		}
		finally
		{
			//sqlSession.close( );
		}
		return list;
	}	 
	
 	
	@SuppressWarnings( "unchecked" )
	public List dbPagingList( Map reqMap, String strXPath,   int iRowStartPos, int iCount)
	{
		List  list = null;
		
		try
		{
			
			reqMap.put("page_start", (iRowStartPos + 1 ) ); // �떆�옉�쐞移�
			reqMap.put("page_end",   (iRowStartPos + iCount)  ); // 醫낅즺�쐞移�
			
			list = sqlSession.selectList(strXPath , reqMap );
		}
		finally
		{
			//sqlSession.close( );
		}
		return list;
	}	 
	 	
 	
 	
 	
 	
 	@SuppressWarnings( "unchecked" )
	public Map detail( Map reqMap, String strXPath )
	{
		Map  rsMap = null;
		
		try
		{
			rsMap = (Map)sqlSession.selectOne(strXPath , reqMap);
		}
		finally
		{
			//sqlSession.close( );
		}
		return rsMap;
	}	  	
	
 	@SuppressWarnings( "unchecked" )
	public Map detail( String strXPath)
	{
		Map  rsMap = null;
		
		try
		{
			rsMap = (Map)sqlSession.selectOne(strXPath);
		}
		finally
		{
			//sqlSession.close( );
		}
		return rsMap;
	}
	
	public int getInt(Map reqMap, String strXPath) {
		int nCount = -999;
 
		try
		{
			nCount = (Integer)sqlSession.selectOne(strXPath , reqMap);
		} catch (Exception e) {
			System.out.println(" [getInt] ==> " + e.toString());
		}  
		finally
		{
			//sqlSession.close( );
		}	

		return nCount;
	} 	
	
	public int getInt(String strXPath) {
		int nCount = -999;
 
		try
		{
			nCount = (Integer)sqlSession.selectOne(strXPath);
		} catch (Exception e) {
			System.out.println(" [getInt] ==> " + e.toString());
		}  
		finally
		{
			//sqlSession.close( );
		}	

		return nCount;
	} 	
	
	public long getLong(String strXPath) {
		long nCount = -999;
 
		try
		{
			nCount = (Long)sqlSession.selectOne(strXPath);
		} catch (Exception e) {
			System.out.println(" [long] ==> " + e.toString());
		}  
		finally
		{
			//sqlSession.close( );
		}	

		return nCount;
	}
	
	public int getCount(Map reqMap, String strXPath) {
		return getInt(reqMap, strXPath) ;
	}
	
	public int getCount(String strXPath) {
		return getInt(strXPath) ;
	}	
}
