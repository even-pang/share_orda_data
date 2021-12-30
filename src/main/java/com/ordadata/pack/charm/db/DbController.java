/**
 * Class Summary. <br>
 * DB Controller class.
 * @since 1.00
 * @version 1.00 - 2019. 09. 26
 * @author �젙�냼�꽑
 * @see
 */
package com.ordadata.pack.charm.db;
 
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordadata.pack.charm.util.CommDef;
import com.ordadata.pack.charm.util.CommonUtil;

@Service("dbSvc")
public class DbController
{
	public int PAGE_ROWCOUNT = CommDef.PAGE_ROWCOUNT;
	
	@Autowired
	@Resource(name="dbDao")
	public DbDao m_dao;
	
	public DbDao getDao(){
		return m_dao;
	}
    
	public int dbInsert( Map reqMap, String strXPath )
	{ 
		return (Integer) m_dao.insert( reqMap, strXPath );
	}	
	
	public int dbInsert( List lst, String strXPath )	
	{ 
		return (Integer) m_dao.insert( lst, strXPath );
	}	
	 
	
	public int dbUpdate( Map reqMap, String strXPath )
	{ 
		return (Integer) m_dao.update( reqMap, strXPath );
	}		
	
	public int dbDelete( Map reqMap, String strXPath )
	{ 
		return (Integer) m_dao.delete( reqMap, strXPath );
	}

	public int dbDelete(String strXPath )
	{ 
		return (Integer) m_dao.delete(strXPath );
	}	
	
	public List dbList(String strXPath )
	{ 
		return m_dao.list(strXPath);
	}		
	
	public List dbList( Map reqMap, String strXPath )
	{ 
		if(null == m_dao) System.out.println("알랄라라라라라ㅏ라라라라라라라라ㅏ");
		return m_dao.list(reqMap, strXPath);
	}		
		
	public List dbList( Map reqMap, String strXPath,   int iRowStartPos, int iCount)
	{ 
		return m_dao.list(reqMap, strXPath, iRowStartPos, iCount);
	}		
	
	public List dbPagingList( Map reqMap, String strXPath,   int iRowStartPos, int iCount)
	{ 
		return m_dao.dbPagingList(reqMap, strXPath, iRowStartPos, iCount);
	}		
	
	
	public Map dbDetail(String strXPath )
	{ 
		return m_dao.detail(strXPath);
	}		
	
	public Map dbDetail( Map reqMap, String strXPath )
	{ 
		return m_dao.detail(reqMap, strXPath);
	}		
	
	public int dbInt( Map reqMap, String strXPath)
	{ 
		return m_dao.getInt(reqMap, strXPath);
	}		
	
	public int dbInt( String strXPath)
	{ 
		return m_dao.getInt(strXPath);
	}	
	
	
	public long dbLong( String strXPath)
	{ 
		return m_dao.getLong(strXPath);
	}
	public long dbLong(Map reqMap, String strXPath)
	{
		return m_dao.getLong(reqMap, strXPath);
	}

	public int dbCount( String strXPath)
	{ 
		return m_dao.getCount(strXPath);
	}	
	
	public int dbCount( Map reqMap, String strXPath)
	{ 
		return m_dao.getCount(reqMap, strXPath);
	}		
	
    /**
     * Method Summary. <br>
     * �븳 �럹�씠吏��떦 �몴�떆�븷 嫄댁닔 媛믪쓣 議고쉶�븳�떎. method.
     * @param HashMap reqMap �뙆�씪誘명꽣 媛앹껜
     * @param String strMapKey HashMap�쓽 �궎
     * @return int �븳 �럹�씠吏��떦 �몴�떆�븷 嫄댁닔
     * @throws 
     * @since 1.00
     * @see
     */
	public int getPageRowCount(Map reqMap, String strMapKey)
	{
		return getPageRowCount(reqMap, strMapKey, PAGE_ROWCOUNT);
	}
	
    /**
     * Method Summary. <br>
     * �븳 �럹�씠吏��떦 �몴�떆�븷 嫄댁닔 媛믪쓣 議고쉶�븳�떎. method.
     * @param HashMap reqMap �뙆�씪誘명꽣 媛앹껜
     * @param String strMapKey HashMap�쓽 �궎
     * @return int �븳 �럹�씠吏��떦 �몴�떆�븷 嫄댁닔
     * @throws 
     * @since 1.00
     * @see
     */
	public int getPageRowCount(Map reqMap, String strMapKey, int nRowCount)
	{
		
		if (reqMap == null || reqMap.isEmpty())
			return nRowCount;
		
		try
		{
			nRowCount = Integer.parseInt( CommonUtil.getNullTrans(reqMap.get( strMapKey), nRowCount));
		} catch (Exception e) {
			nRowCount = PAGE_ROWCOUNT;
		}
		
		return nRowCount;
	}
	
   
    /**
     * Method Summary. <br>
     * �쁽�옱 �럹�씠吏� 媛믪쓣 議고쉶�븳�떎. method.
     * @param HashMap reqMap �뙆�씪誘명꽣 媛앹껜
     * @param String strMapKey HashMap�쓽 �궎
     * @return int �쁽�옱 �럹�씠吏� 媛�
     * @throws 
     * @since 1.00
     * @see
     */
	public int getPageNow(Map reqMap, String strMapKey)
	{
		int nPage = 1;
		
		if (reqMap == null || reqMap.isEmpty())
			return nPage;
		
		try
		{
			nPage = Integer.parseInt( CommonUtil.getNullTrans(reqMap.get( strMapKey), 1));
		} catch (Exception e) {
			nPage = 1;
		}
		
		return nPage;
	}		
	
}