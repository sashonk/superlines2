package superlines.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import superlines.core.Rank;
import superlines.ws.RateData;
import superlines.ws.RateParameters;


public class RateDAO extends DAO{
	private static RateDAO instance;
	
	public static RateDAO get(){
		if(instance == null){
			instance = new RateDAO();
		}
		
		return instance;
	}
	
	public List<RateData> getRateData(final RateParameters params) throws Exception{
		List<RateData> result = new LinkedList<>();
		
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			c = m_dataSource.getConnection();
			st = c.createStatement();
			String sql = "select p.name name, p.rankid rank, (select sum(s.score) from scoredata s where s.userid = p.id and s.rankid = p.rankid) sum from profiles p order by rank desc, sum desc limit 10";
			
															
			rs = st.executeQuery(sql);
			while(rs.next()){
				RateData  data = new RateData();
				data.setName(rs.getString("name"));
				data.setScore(rs.getInt("sum"));
				data.setRank(Rank.getRank(rs.getInt("rank")));
				result.add(data);
			}
										
		}
		catch(Exception ex){
			m_log.error(ex);
		}
		finally{
				try{
					if(st!=null){
						st.close();
					}
					if(c!=null){
						c.close();
					}
				}catch(Exception ex){
					m_log.error(ex);
				
				}
		}
	
		
		
		return result;
	
	}
	
	private RateDAO(){}
}
