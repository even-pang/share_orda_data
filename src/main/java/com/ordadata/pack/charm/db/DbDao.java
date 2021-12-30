/**
 * Class Summary. <br>
 * DBDAO class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author �젙�냼�꽑
 * @see
 */
package com.ordadata.pack.charm.db;
 
import java.sql.Array;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
 

/**
 * @author Administrator
 *
 */
@Repository("dbDao")
@Mapper
public class DbDao extends commonDAO
{
	
	private  SqlSession sqlSession = null;
	public DbDao( )
	{
		getConn();
	}
	 
	private void getConn() {
		 if ( sqlSession == null) {
			 sqlSession = getSqlSession( );
			// System.out.println( "========================  DB DAO sqlSession  " + this );
		 }
	}
	
	public int insert( Map reqMap, String strXPath )
	{
		//SqlSession sqlSession = getSqlSession( );
		getConn();
		
		int nSuccess = 0;
		
		try {
			nSuccess = sqlSession.insert( strXPath , reqMap );
		} catch ( Exception e) { 
		    System.out.println(e.toString());
		}finally {
			//sqlSession.commit(true );
			//sqlSession.close( );
		}
		return nSuccess;
	}	
	
	public int insert( List lst, String strXPath )
	{
		//SqlSession sqlSession = getSqlSession( );
		
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(getSqlSession().getConfiguration());
		SqlSession sqlSessionBatch = sqlSessionFactory.openSession(ExecutorType.BATCH, false);

		int nSuccess = 0;
		
		try {
			
			for(int nLoop=0; nLoop < lst.size(); nLoop++)
			{
				Map reqMap = (Map)lst.get(nLoop);
			    nSuccess = sqlSessionBatch.insert( strXPath , reqMap );
			}
		} catch ( Exception e) { 
		    System.out.println(e.toString());
		    sqlSessionBatch.rollback();
		}finally {
			sqlSessionBatch.commit(true );
			sqlSessionBatch.close( );
		}
		return nSuccess;
	}		
	
	public int update( Map reqMap, String strXPath )
	{
		//SqlSession sqlSession = getSqlSession( );
		getConn();
		
		int nSuccess = 0;
		
		try {
			nSuccess = sqlSession.update(strXPath , reqMap );
		} catch ( Exception e) { 
		    System.out.println(e.toString());
		}finally {
			//sqlSession.commit(true);
			//sqlSession.close( );
		}
		return nSuccess;
	}	 
	
	
	public int delete( Map reqMap, String strXPath )
	{
		//SqlSession sqlSession = getSqlSession( );
		getConn();
		
		int nSuccess = 0;
		
		try {
			nSuccess = sqlSession.delete(strXPath , reqMap );
		} catch ( Exception e) { 
		    System.out.println(e.toString());
		}finally {
 
			//sqlSession.commit(true);
			//sqlSession.close( );
		}
		return nSuccess;
	}	
	
 
	public int delete( String strXPath)
	{
		//SqlSession sqlSession = getSqlSession( );
		getConn();
		
		int nSuccess = 0;
		
		try {
			nSuccess = sqlSession.delete(strXPath );
		} catch ( Exception e) { 
		    System.out.println(e.toString());
		}finally {
			//sqlSession.commit(true);
			//sqlSession.close( );
		}
		return nSuccess;
	}		

	@SuppressWarnings( "unchecked" )
	public List list(String strXPath )
	{
		//SqlSession sqlSession = getSqlSession( );
		getConn();
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
		//SqlSession sqlSession = getSqlSession( );
		getConn();
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
		//SqlSession sqlSession = getSqlSession( );
 		getConn();
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
		//SqlSession sqlSession = getSqlSession( );
		getConn();
		List  list = null;
		
		try
		{
			
			reqMap.put("page_start", (iRowStartPos ) ); // �떆�옉�쐞移�
			reqMap.put("page_end",   (iRowStartPos + iCount)  ); // 醫낅즺�쐞移�
			
			/*System.out.println("iRowStartPos:" + iRowStartPos);
			System.out.println("iCount:" + iCount);
			
			System.out.println("page_start:" + (iRowStartPos + 1 ));
			System.out.println("page_end:" + (iRowStartPos + iCount));*/
			
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
 		getConn();
		//SqlSession sqlSession = getSqlSession( );
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
 		getConn();
		//SqlSession sqlSession = getSqlSession( );
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
		//SqlSession sqlSession = getSqlSession( );
		
		getConn();
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
	    //iCount = Integer.parseInt(super.queryForObject(strXPath, map).toString());

		return nCount;
	} 	
	public long getLong(String strXPath) {
		getConn();
		//SqlSession sqlSession = getSqlSession( );
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
	    //iCount = Integer.parseInt(super.queryForObject(strXPath, map).toString());

		return nCount;
	}

	public long getLong(Map reqMap, String strXPath) {
		getConn();
		//SqlSession sqlSession = getSqlSession( );
		long nCount = -999;

		try
		{
			nCount = (Long)sqlSession.selectOne(strXPath, reqMap);
		} catch (Exception e) {
			System.out.println(" [long] ==> " + e.toString());
		}
		finally
		{
			//sqlSession.close( );
		}
		//iCount = Integer.parseInt(super.queryForObject(strXPath, map).toString());

		return nCount;
	}


	public int getInt(String strXPath) {
		getConn();
		//SqlSession sqlSession = getSqlSession( );
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
	    //iCount = Integer.parseInt(super.queryForObject(strXPath, map).toString());

		return nCount;
	} 	
	
	public int getCount(Map reqMap, String strXPath) {
		return getInt(reqMap, strXPath) ;
	}
	
	public int getCount(String strXPath) {
		return getInt(strXPath) ;
	}	
	
}