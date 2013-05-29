package web.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


import web.common.model.Target45List;
import web.common.model.Target45OneToOne;
import web.common.service.TransferTargetService;
import web.common.model.TargetList;
import web.common.model.OnetooneTarget;

public class TransferTargetController extends MultiActionController{
	
	
	private TransferTargetService transferTargetService = null;
	
	public void setTransferTargetService(TransferTargetService transferTargetService) {
		this.transferTargetService = transferTargetService;
	}
	
	public ModelAndView main(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		System.out.println("start ??? ");
		excuteTransferTarget();
		return new ModelAndView("/transferTarget.jsp");		
	}
	
	/**
	 * <p>dbID에 해당하는 접속정보를 가져오고 접속한다. 
	 * @param dbID
	 * @return
	 */
	private Connection getConnectionDBInfo(String dbID){
		Connection conn = null;
		Map<String, Object> mapdb = transferTargetService.getDBInfo(dbID);
		
		if(mapdb==null || mapdb.size()==0){
			return null;
		}
		
		String driver = String.valueOf(mapdb.get("driverClass"));
		String url = String.valueOf(mapdb.get("dbURL"));
		String user = String.valueOf(mapdb.get("dbUserID"));
		String password = String.valueOf(mapdb.get("dbUserPWD"));
		
		conn = DBUtil.getConnection(driver, url, user, password);
		
		return conn;
	}
	
	public List<Target45List> getTarget45List(String dbID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Target45List> target45Lists = new ArrayList();
		//String queryText = "select target_id, name, query, db_id, rs_count, type, user_id  from target_list where type='import' and target_id=2848";
		String queryText = "select target_id, name, query, db_id, rs_count, type, user_id  from target_list where type in ('import', 'general') ";
		Connection conn = getConnectionDBInfo(dbID);
		try{		
			ps = conn.prepareStatement(queryText);
			rs = ps.executeQuery();				
			while(rs.next()){
				Target45List target45List = new Target45List();
				target45List.setTarget_id(rs.getInt("target_id"));
				target45List.setName(rs.getString("name"));
				target45List.setDb_id(rs.getInt("db_id"));
				target45List.setQuery(rs.getString("query"));
				target45List.setRs_count(rs.getInt("rs_count"));
				target45List.setType(rs.getString("type"));
				target45List.setUser_id(rs.getString("user_id"));
				target45Lists.add(target45List);
			}
		}catch(Exception e){
			
		}finally{
			try{ps.close();}catch(Exception e){}
			try{rs.close();}catch(Exception e){}		
			try{conn.close();}catch(Exception e){}	
			
		}
		
		return target45Lists;
		
	}
	
	public List<Target45OneToOne> getTarget45OneToOne(String dbID, int target_id)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Target45OneToOne> target45OneToOnes = new ArrayList();
		String queryText = "select object_id, field_name, description from onetoone where target_id="+target_id+" order by object_id";
		Connection conn = getConnectionDBInfo(dbID);
		try{		
			ps = conn.prepareStatement(queryText);
			rs = ps.executeQuery();				
			while(rs.next()){
				Target45OneToOne target45OneToOne = new Target45OneToOne();
				target45OneToOne.setField_name(rs.getString("field_name"));
				target45OneToOne.setDescription(rs.getString("description"));
				target45OneToOne.setObject_id(rs.getInt("object_id"));
				
				target45OneToOnes.add(target45OneToOne);
			}
		}catch(Exception e){
			
		}finally{
			try{ps.close();}catch(Exception e){}
			try{rs.close();}catch(Exception e){}		
			try{conn.close();}catch(Exception e){}	
			
		}
		
		return target45OneToOnes;
		
	}
	
	private int insertTargetList(TargetList targetList)
	{
		return transferTargetService.insertTargetList(targetList);
	}
	
	private int getMaxTargetID()
	{
		return transferTargetService.getMaxTargetID();
	}
	
	/**
	 * <p>테이블을 얻어오거나 생성한다.
	 * @return
	 */
	private String getFileTableName(){
		String tableName = "";
		
		 tableName = Constant.FILE_TABLE +"_"+ DateUtils.getYearMonth();			 
		 List<Map<String, Object>> tableNames = transferTargetService.getFileImportTableIsExist(tableName);
		 //없다면 생성해준다.
		 if(tableNames==null || tableNames.size()==0){
			
			 if(transferTargetService.createFileImportTable(tableName)<0){
				 tableName = ""; //실패라면 빈테이블 
			 }
		 }
		return tableName;
	}
	

	private int insertOnetooneTarget(List<OnetooneTarget> onetooneTargetList)
	{
		return transferTargetService.insertOnetooneTarget(onetooneTargetList);
	}
	
	private int updateTargetingEnd(String state, int count, String query, String tableName,  int targetID, String count_query)
	{
		return transferTargetService.updateTargetingEnd(state, count, query, tableName, targetID, count_query);
	}
	
	private int insertTargetData(String dbID, String query, int targetID, List<OnetooneTarget> onetooneTargetList)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Target45OneToOne> target45OneToOnes = new ArrayList();
		int result = 0;
		Connection conn = getConnectionDBInfo(dbID);
		try{		
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();				
			
				
			insertParsingFileImport(rs, targetID, onetooneTargetList);
				
				
			
		}catch(Exception e){
			
		}finally{
			try{ps.close();}catch(Exception e){}
			try{rs.close();}catch(Exception e){}		
			try{conn.close();}catch(Exception e){}	
			
		}
		
		return 0;
	}
	
	/**
	 * <p>파일을 파싱해서 tm_fileimport에 인서트한다. 
	 * @param onetooneTarget
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void  insertParsingFileImport(ResultSet rs, int targetID, List<OnetooneTarget> onetooneTargetList){
		
		String tableName =  getFileTableName();
			
		String insertSQL = "";
		
		insertSQL = "INSERT INTO "+tableName+" (targetID,"; //sql문 생성

		String selectSQL = "SELECT ";
		String columnSQL = "";
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			columnSQL += onetooneTargetList.get(i).getFieldName()+",";
		}
		columnSQL = columnSQL.substring(0,columnSQL.length()-1);
		
		
		insertSQL = insertSQL + columnSQL + ") VALUES("+targetID+",";
		
		selectSQL = selectSQL + columnSQL +" FROM "+tableName+" WHERE targetID="+targetID;
		
		for( int i = 0; i < onetooneTargetList.size(); i ++ ) {
			insertSQL += "?,";
		}
		insertSQL = insertSQL.substring(0,insertSQL.length()-1)+")";
		
		System.out.println("insertSQL : " + insertSQL);
		System.out.println("selectSQL : " + selectSQL);
		
		List<Object> paramList = new ArrayList();
		List<String> params = null;
	
		try {
		while(rs.next()){
			params = new ArrayList();
					
			
				for(int i=0; i < onetooneTargetList.size(); i ++) 
				{
					params.add(rs.getString(i+1)==null?"":rs.getString(i+1));
				}
				paramList.add(params);
			
			
		}//end while
		} catch (Exception e) {
	
			e.printStackTrace( System.out);
		}
			
		transferTargetService.insertFileImport(insertSQL,paramList);	//tm_fileimport에 인서트한다. 
		 
		
		 
			
	}
	
	public void excuteTransferTarget()
	{
		System.out.println("start : ");
		List<Target45List> target45Lists = getTarget45List("11");
		int resultVal = -1;
		if(target45Lists !=null && target45Lists.size()!=0)
		{
			for(Target45List target45List : target45Lists)
			{
				try{
				List<OnetooneTarget> onetooneTargetList = new ArrayList<OnetooneTarget>();
				TargetList targetList = new TargetList();	
				targetList.setTargetName("[4.5]" + target45List.getName());
				System.out.println("target45List.getName() : " + target45List.getName());
				System.out.println("target45List.getTarget_id() : " + target45List.getTarget_id());
				targetList.setDescription("");
				targetList.setTargetGroupID(1);
				targetList.setUserID(target45List.getUser_id());
				if(target45List.getDb_id()==1)
				{
					targetList.setTargetType(Constant.TARGET_TYPE_FILE);		//파일등록
					targetList.setDbID(Constant.TM5_DBID);  //파일등록이므로 기본 TM DB
				}
				else if(target45List.getDb_id()!=1)
				{
					targetList.setTargetType(Constant.TARGET_TYPE_DB);;	        //쿼리
					targetList.setDbID(String.valueOf(target45List.getDb_id()+10));  
				}
				targetList.setShareType("1");
				targetList.setState(Constant.TARGET_STATE_READY);
				targetList.setUploadKey("");  //파일 업로드 키 
				String uploadKey = "";
				targetList.setQueryText(target45List.getQuery());
				targetList.setBookMark("N");
				String query_select = "";
				String query_select_temp = "";
				int targetID = 0;
				//1. 대상자를 저장한다.
				resultVal = insertTargetList(targetList);
				if(resultVal>0)
					targetID = getMaxTargetID();
				
				
				//2. 원투원  정보를 저장
				List<Target45OneToOne> target45OneToOnes = getTarget45OneToOne("11", target45List.getTarget_id());
				if(target45OneToOnes!=null && target45OneToOnes.size()!=0)
				{
					OnetooneTarget onetooneTarget = null;
					for(int i=0; i < target45OneToOnes.size(); i++ ) 
					{
						Target45OneToOne target45OneToOne = target45OneToOnes.get(i);
						onetooneTarget = new OnetooneTarget();
						onetooneTarget.setTargetID(targetID);
						onetooneTarget.setCsvColumnPos(i+1);
					
						if(target45List.getDb_id()==1)
							onetooneTarget.setFieldName("col"+String.valueOf(i+1));
						else if(target45List.getDb_id()!=1)
							onetooneTarget.setFieldName(target45OneToOne.getField_name());
						System.out.println("getDescription : " + target45OneToOne.getDescription() + " - " + target45List.getTarget_id());
						if(target45OneToOne.getObject_id()==1)
							onetooneTarget.setOnetooneID(1);
						else if(target45OneToOne.getObject_id()==2)
							onetooneTarget.setOnetooneID(3);
						else if(target45OneToOne.getObject_id()==3)
							onetooneTarget.setOnetooneID(4);
						else if(target45OneToOne.getObject_id()==4)
							onetooneTarget.setOnetooneID(5);
						else if(target45OneToOne.getObject_id()==5)
							onetooneTarget.setOnetooneID(6);
						else if(target45OneToOne.getObject_id()==6)
							onetooneTarget.setOnetooneID(7);
						else if(target45OneToOne.getObject_id()==7)
							onetooneTarget.setOnetooneID(8);
						else if(target45OneToOne.getObject_id()==8)
							onetooneTarget.setOnetooneID(9);
						else if(target45OneToOne.getObject_id()==9)
							onetooneTarget.setOnetooneID(10);
						else if(target45OneToOne.getObject_id()==10)
							onetooneTarget.setOnetooneID(11);
						else if(target45OneToOne.getObject_id()==11)
							onetooneTarget.setOnetooneID(12);
						else if(target45OneToOne.getObject_id()==12)
							onetooneTarget.setOnetooneID(13);
						else if(target45OneToOne.getObject_id()==13)
							onetooneTarget.setOnetooneID(14);
						/*
						else if(target45OneToOne.getDescription().equals("[기타8]"))
							onetooneTarget.setOnetooneID(15);
						else if(target45OneToOne.getDescription().equals("[기타9]"))
							onetooneTarget.setOnetooneID(16);
						else if(target45OneToOne.getDescription().equals("[기타10]"))
							onetooneTarget.setOnetooneID(17);
						else if(target45OneToOne.getDescription().equals("[휴대폰]"))
							onetooneTarget.setOnetooneID(2);
						else if(target45OneToOne.getDescription().equals("[텍스트1]"))
							onetooneTarget.setOnetooneID(18);
						else if(target45OneToOne.getDescription().equals("[텍스트2]"))
							onetooneTarget.setOnetooneID(19);
						else if(target45OneToOne.getDescription().equals("[텍스트3]"))
							onetooneTarget.setOnetooneID(20);
						*/
						onetooneTarget.setFieldDesc(target45OneToOne.getDescription());
						if(target45List.getDb_id()==1)
						{
							if(target45OneToOne.getObject_id()==1)
								query_select = query_select + ", email";
							else if(target45OneToOne.getObject_id()==2)
								query_select = query_select + ", first";
							else if(target45OneToOne.getObject_id()==3)
								query_select = query_select + ", second";
							else if(target45OneToOne.getObject_id()==4)
								query_select = query_select + ", third";
							else if(target45OneToOne.getObject_id()==5)
								query_select = query_select + ", fourth";
							else if(target45OneToOne.getObject_id()==6)
								query_select = query_select + ", fifth";
							else if(target45OneToOne.getObject_id()==7)
								query_select = query_select + ", sixth";
							else if(target45OneToOne.getObject_id()==8)
								query_select = query_select + ", seventh";
							else if(target45OneToOne.getObject_id()==9)
								query_select = query_select + ", eighth";
							else if(target45OneToOne.getObject_id()==10)
								query_select = query_select + ", ninth";
							else if(target45OneToOne.getObject_id()==11)
								query_select = query_select + ", tenth";
							else if(target45OneToOne.getObject_id()==12)
								query_select = query_select + ", eleventh";
							else if(target45OneToOne.getObject_id()==13)
								query_select = query_select + ", twelfth";
							/*
							else if(target45OneToOne.getDescription().equals("[기타8]"))
								query_select = query_select + ", thirteenth";
							else if(target45OneToOne.getDescription().equals("[기타9]"))
								query_select = query_select + ", fourteenth";
							else if(target45OneToOne.getDescription().equals("[기타10]"))
								query_select = query_select + ", fifteenth";
							else if(target45OneToOne.getDescription().equals("[휴대폰]"))
								query_select = query_select + ", sixteenth";
							else if(target45OneToOne.getDescription().equals("[텍스트1]"))
								query_select = query_select + ", seventeenth";
							else if(target45OneToOne.getDescription().equals("[텍스트2]"))
								query_select = query_select + ", eighteenth";
							else if(target45OneToOne.getDescription().equals("[텍스트3]"))
								query_select = query_select + ", nineteenth";
							*/
							query_select_temp = query_select_temp + ", " +onetooneTarget.getFieldName();
						}
						else
						{
							query_select = query_select + ", " +target45OneToOne.getField_name();
						}
						onetooneTargetList.add(onetooneTarget);
					}
					insertOnetooneTarget(onetooneTargetList);
					
					String query = target45List.getQuery();
					String tableName = getFileTableName();
					
					if(target45List.getDb_id()==1)
					{
						String query_temp = "SELECT " + query_select.substring(1) + " " + target45List.getQuery().substring(target45List.getQuery().toUpperCase().indexOf(" FROM "));
						insertTargetData("11",query_temp, targetID, onetooneTargetList);
						
						query = "SELECT " + query_select_temp.substring(1) + " FROM " + tableName + " WHERE targetID="+targetID; 
  
					}
					String count_query = "select count(*) from (" + query +") a ";
					updateTargetingEnd(Constant.TARGET_STATE_FINISH, target45List.getRs_count(), query, tableName, targetID, count_query);
				}
				else
				{
					updateTargetingEnd(Constant.TARGET_STATE_ERROR, target45List.getRs_count(), "", "", targetID, "");
				}
				}catch(Exception e){
					System.out.println("error !! target45List.getTarget_id() : " + target45List.getTarget_id());
					System.out.println(e);
				}finally{
					continue;
				}
				
			}//end for
		}
		System.out.println("end : ");
	}
}
