package com.ordadata.pack.png;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

public class MakePng {
		ServletRequest request=null;
		ServletContext application=null;
		int deltaReleaseTime=120*1000; //블럭 리스트에서 아이피 제거를 실행하는 주기
		int deltaIpReleaseTime=40*1000; //아이피를 블럭시키는 시간
		int deltaCountCheckTime=5*60*1000; //카운터를 0으로 갱신시키는 주기 
		int deltaMaxCount=1000; //카운터 주기동안 받아들일수있는 최대양 
		int deltaReleaseBlockTime=600*1000;// 블럭감시 장치 해제 시간
		int deltaMaxLoadPage=100; //블럭감시중 허용되는 최대 페이지 로딩수 
		int deltaCurDelTime=120*1000; //이미지파일 삭제시간
		String testErr=null;
		String RealPath=null;
		String ClientIp=null;
	 	long NowTime=99999999;
	 	String FileName=null;
	 	String UniqId=null;
	 	public void setReleaseTime(int t)
		{
			deltaReleaseTime=t*100;
		}
		public void setIpReleaseTime(int t)
		{
			deltaIpReleaseTime=t*100;
		}
		public void setCountCheckTime(int t)
		{
			deltaCountCheckTime=t*100;
		}
		public void setMaxCount(int c)
		{
			deltaMaxCount=c;
		}
		public void setMaxLoadPage(int c)
		{
			deltaMaxLoadPage=c;
		}
		public void setNowTime(long t)
		{
			NowTime=t;
		}
		public void setRequestObj(ServletRequest obj)
		{
			request=obj;
			ClientIp=request.getRemoteAddr();
			UniqId=request.getParameter("UniqId");
			
		}
		public void setApplicationObj(ServletContext obj)
		{
			application=obj;
			RealPath=application.getRealPath("/antispam");
		}
		public int transInt(Object obj)
		{
			if (obj==null) return 0;
			else
	 		return Integer.parseInt((String)obj,10);
	 	}
	 	public long transLong(Object obj)
	 	{
	 		if (obj==null) return 0;
	 		else
	 		return Long.parseLong((String)obj,10);
	 	}
	 	
	 	public boolean transBool(Object obj)
	 	{
	 		if (obj==null) return false;
	 		else
	 		return new Boolean((String)obj).booleanValue();
	 	}
	 	
	 	public String [] mySplit(String str,String delim)
	 	{
	 		StringTokenizer Tokenizer=new StringTokenizer(str,delim);
	 		String [] Token=new String[Tokenizer.countTokens()];
	 		for (int i=0;Tokenizer.hasMoreTokens()==true;i++)
	 		{
	 		Token[i]=Tokenizer.nextToken();
	 		
	 		}
	 		
	 	return Token;
	 	}	
	 	
	 	public String getFileName()
	 	{
	 		return FileName;
	 	}
	 	
	 	public boolean IpTest()
	 	{
	 		
	 		long ReleaseTime;
	 		ReleaseTime=transLong(application.getAttribute("ReleaseTime"));
	 		
	 		if (NowTime-ReleaseTime>deltaReleaseTime)
	 		{
	 			if (BlockIpRelease()==false) return false;
	 			
	 			application.setAttribute("ReleaseTime",String.valueOf(NowTime));
	 		}
	 		
	 		FileInputStream fpBlockIpList=null;
	 		int fileSize=(int)(new File(RealPath+"/blockipList.txt").length());
	 		byte [] buffer=new byte[fileSize];
	 		String Ip;
	 		try
	 		{
	 			fpBlockIpList=new FileInputStream(RealPath+"/blockiplist.txt");
	 			fpBlockIpList.read(buffer);
	 			Ip=new String(buffer);
	 			if (Ip.length()==0) Ip="_";
	 			fpBlockIpList.close();
	 		}
			catch(IOException ioErr)
			{
				testErr=ioErr.toString();
				return false;
			}
				
			
			String [] BlockIp=mySplit(Ip,"_");
			for (int i=0;i<BlockIp.length;i++)
			{
				
				
				if (ClientIp.compareTo(BlockIp[i])==0)
				{
					return false;
				}
			}
			
			BlockIf();
			
			return true;
		}
		
		public boolean BlockIpRelease()
		{
			FileInputStream fpBlockIpListR=null
					,fpBlockIpTimeR=null;
			int fileSize=0;
			byte [] buffer;
			try
			{
				fileSize=(int)(new File(RealPath+"/blockiplist.txt").length());
				fpBlockIpListR=new FileInputStream(RealPath+"/blockiplist.txt");
				
				buffer=new byte[fileSize];
	 			fpBlockIpListR.read(buffer);
	 			fpBlockIpListR.close();
			}
			catch(IOException ioErr) 
			{
				testErr=ioErr.toString();
				return false; 			
			}
			
			
			String TempIp=new String(buffer);
			if (TempIp.length()==0) TempIp="_";
			String [] IpList=mySplit(TempIp,"_");
			String TempTime=null;
			
			try
			{
				fileSize=(int)(new File(RealPath+"/blockiptime.txt").length());
				fpBlockIpTimeR=new FileInputStream(RealPath+"/blockiptime.txt");
				buffer=new byte[fileSize];
	 			fpBlockIpTimeR.read(buffer);
	 			TempTime=new String(buffer);
	 			if (TempTime.length()==0) TempTime="_";
	 			fpBlockIpTimeR.close();
			}
			catch(IOException ioErr)
			{
				testErr=ioErr.toString();
				return false;
			}
	 		String [] IpTime=mySplit(TempTime,"_");
	 		FileOutputStream fpBlockIpListW=null
	 			        ,fpBlockIpTimeW=null;
	 	  	try
	 	  	{
	 	  		fpBlockIpListW=new FileOutputStream(RealPath+"/blockiplist.txt",false);
	 	  		fpBlockIpTimeW=new FileOutputStream(RealPath+"/blockiptime.txt",false);
	 	  		for (int i=0;i<IpList.length;++i)
	 	  		{
	 	  			if (NowTime-Long.parseLong(IpTime[i],10)<deltaIpReleaseTime)
	 	  			{
	 	  				fpBlockIpListW.write(IpList[i].getBytes());
	 	  				fpBlockIpListW.write('_');
	 	  				fpBlockIpTimeW.write(IpTime[i].getBytes());
	 	  				fpBlockIpTimeW.write('_');
	 	  			}
	 	  		}
	 	  	fpBlockIpListW.close();
	 	  	fpBlockIpTimeW.close();
	 	  	
	 	  	}
	 	  	catch(IOException ioErr)
	 	  	{
	 	  		testErr=ioErr.toString();
	 	  		return false;
	 	  	}
	 	  	return true;
	 	}
	 	
	 	void BlockIf()
	 	{
	 		boolean BlockFlag=transBool(application.getAttribute("BlockFlag"));
	 		int Count=transInt(application.getAttribute("Count"));
	 		long CountTime=transLong(application.getAttribute("CountTime"));
	 		if (BlockFlag==true)
	 		BlockIpList();
	 		else
	 		{
	 			if (NowTime-CountTime>deltaCountCheckTime)
	 			{
	 				 application.setAttribute("Count",String.valueOf(0));
	 				 application.setAttribute("CountTime",String.valueOf(NowTime));
	 			}
	 			else
	 			{
	 				++Count;
	 				application.setAttribute("Count",String.valueOf(Count));
	 			}
	 			if (Count>deltaMaxCount)
	 			{
	 				application.setAttribute("BlockFlag",String.valueOf(true));
	 				application.setAttribute("BlockTime",String.valueOf(NowTime));
	 			}
	 		}
	 	}
	 	
	 	boolean BlockIpList()
	 	{
	 		if (NowTime-transLong(application.getAttribute("BlockTime"))>deltaReleaseBlockTime)
	 		{
	 			application.setAttribute("BlockFlag",String.valueOf(false));
	 			FileOutputStream fpTempW=null;
	 					try
	 					{
	 						fpTempW=new FileOutputStream(RealPath+"/iplist.txt",false);
	 						fpTempW.close();
	 					}
	 					catch(IOException ioErr)
	 					{
	 						testErr=ioErr.toString();
	 						return false;
	 					}
	 					try
	 					{
	 						fpTempW=new FileOutputStream(RealPath+"/ipcount.txt",false);
	 						fpTempW.close();
	 					}
	 					catch(IOException ioErr)
	 					{
	 						testErr=ioErr.toString();
	 						return false;
	 					}	
	 					
	 		}
	 		else
	 		{
	 			FileInputStream fpIpListR=null,
	 					 fpIpCountR=null;
	 			int fileSize=0;
	 			byte [] buffer;
	 			try
	 			{
	 				fileSize=(int)(new File(RealPath+"/iplist.txt").length());
	 				fpIpListR=new FileInputStream(RealPath+"/iplist.txt");
	 				buffer=new byte[fileSize];
	 				fpIpListR.read(buffer);
	 				fpIpListR.close();
	 				
	 			}
	 			catch(IOException ioErr)
	 			{
	 				testErr=ioErr.toString();
	 				return false;
	 			}
	 			
	 			
	 			
	 			String TempIp=new String(buffer);
	 			if (TempIp.length()==0) TempIp="_";
	 			String [] IpList=mySplit(TempIp,"_");
	 			
	 			try
	 			{
	 				fileSize=(int)(new File(RealPath+"/ipcount.txt").length());
	 				fpIpCountR=new FileInputStream(RealPath+"/ipcount.txt");
	 				buffer=new byte[fileSize];
	 				fpIpCountR.read(buffer);
	 				fpIpCountR.close();
	 			}
	 			catch(IOException ioErr)
	 			{
	 				testErr=ioErr.toString();
	 				return false;
	 			}
	 			
	 			
	 			String TempCount=new String(buffer);
	 			if (TempCount.length()==0) TempCount="_";
	 			String [] IpCount=mySplit(TempCount,"_");
	 			
	 			for (int i=0;i<IpList.length;++i)
	 			{
	 				if (ClientIp.compareTo(IpList[i])==0)
	 				{
	 					
	 					int Temp=Integer.parseInt(IpCount[i],10);
	 					++Temp;
	 					IpCount[i]=String.valueOf(Temp);
	 					if (Temp>deltaMaxLoadPage)
	 					{
	 						FileOutputStream fpBlockIpListA=null,
	 						                 fpBlockIpTimeA=null;
	 						try
	 						{
	 							fpBlockIpListA=new FileOutputStream(RealPath+"/blockiplist.txt",true);
	 							fpBlockIpListA.write(ClientIp.getBytes());
	 							fpBlockIpListA.write('_');
	 							fpBlockIpListA.close();
	 						}
	 						catch(IOException ioErr)
	 						{
	 							testErr=ioErr.toString();
	 							return false;
	 						}
	 						
	 						try
	 						{
	 							fpBlockIpTimeA=new FileOutputStream(RealPath+"/blockiptime.txt",true);
	 							fpBlockIpTimeA.write((String.valueOf(NowTime)).getBytes());
	 							fpBlockIpTimeA.write('_');
	 							fpBlockIpTimeA.close();
	 						}
	 						catch(IOException ioErr)
	 						{
	 							testErr=ioErr.toString();
	 							return false;
	 						}
	 						
	 					}
	 					FileOutputStream fpIpCountW=null;
	 					try
	 					{
	 						fpIpCountW=new FileOutputStream(RealPath+"/ipcount.txt",false);
	 						for (i=0;i<IpList.length;++i)
	 						{
	 							fpIpCountW.write(IpCount[i].getBytes());
	 							fpIpCountW.write('_');
	 						}
	 					fpIpCountW.close();
	 					}
	 					catch(IOException ioErr)
	 					{
	 						testErr=ioErr.toString();
	 						return false;
	 					}
	 					
	 					return true;
	 				}
	 			}
	 			FileOutputStream fpIpListA=null,
	 			                 fpIpCountA=null;
	 			try
	 			{
	 				fpIpListA=new FileOutputStream(RealPath+"/iplist.txt",true);
	 				fpIpListA.write(ClientIp.getBytes());
	 				fpIpListA.write('_');
	 				fpIpListA.close();
	 			}
	 			catch(IOException ioErr)
	 			{
	 				testErr=ioErr.toString();
	 				return false;
	 			}
	 			
	 			try
	 			{
	 				fpIpCountA=new FileOutputStream(RealPath+"/ipcount.txt",true);
	 				fpIpCountA.write('1');
	 				fpIpCountA.write('_');
	 				fpIpCountA.close();
	 			}
	 			catch(IOException ioErr)
	 			{
	 				testErr=ioErr.toString();
	 				return false;
	 			}
	 			
	 		}
	 	return true;
	 	}
	void delBmp()
	{
		File Tempdir=new File(application.getRealPath("/antispambmp"));
		File [] dir=Tempdir.listFiles();
		for (int i=0;i<dir.length;i++)
		{
			if (NowTime-dir[i].lastModified()>deltaCurDelTime)
			{
		 		if (dir[i].isFile())
		 		{
		 			dir[i].delete();
		 		}
			}
		}
	}
	public String Start()
	{
		if (NowTime-transLong(application.getAttribute("CurDelTime"))>deltaCurDelTime)
	 	{
	 		delBmp();
	 		application.setAttribute("CurDelTime",String.valueOf(NowTime));
	 	}
		String Pass1=String.valueOf((int)(Math.random()*9));
		String Pass2=String.valueOf((int)(Math.random()*9));
		String Pass3=String.valueOf((int)(Math.random()*9));
		String Pass4=String.valueOf((int)(Math.random()*9));
		String PassCode=Pass1+Pass2+Pass3+Pass4;
		MakePngBean pn=new MakePngBean();
		FileName=application.getRealPath("/antispambmp")+"/"+NowTime+".png";
		pn.setNumber(PassCode);
		pn.setFilePath(FileName);
		pn.getMakePng();
	 	FileName=application.getRealPath("/antispambmp")+"/"+NowTime+".jsp";
	 	FileOutputStream fpPassFileW;
	 	  	try
	 	  	{
	 	  	fpPassFileW=new FileOutputStream(FileName,false);
	 	  	fpPassFileW.write('<');
	 	  	fpPassFileW.write('%');
	 	  	fpPassFileW.write('/');
	 	  	fpPassFileW.write('/');
	 	  	fpPassFileW.write(String.valueOf(PassCode).getBytes());
	 	  	fpPassFileW.write('_');
	 	  	fpPassFileW.write(String.valueOf(UniqId).getBytes());	
	 	  	fpPassFileW.write('%');
	 	  	fpPassFileW.write('>');
	 	  	
	 	  	
	 	  	fpPassFileW.close();
	 	  	
	 	  	}
	 	  	catch(IOException ioErr)
	 	  	{
	 	  		
	 	  	}
	 	
	FileName=NowTime+".jsp";
	return NowTime+".png";
	}
}
