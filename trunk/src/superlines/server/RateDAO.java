package superlines.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	public Map<Rank, List<RateData>> getRateData(final RateParameters params) throws Exception{
		//List<RateData> result = new LinkedList<>();
		Map<Rank, List<RateData> > result = new HashMap<Rank, List<RateData>>();
		
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			c = m_dataSource.getConnection();
			st = c.createStatement();
			String sql = "select p.name name, p.surname surname, "
					+ "p.rankid rank, (select sum(s.score) "
					+ "from scoredata s where s.userid = p.id and "
					+ "s.rankid = p.rankid) sum "
					+ "from profiles p where p.accountid in (select r.user_name from user_roles r where r.role_name = 'user') "
					+ "order by rank desc, sum desc limit 10";
			
															
			rs = st.executeQuery(sql);
			while(rs.next()){
				RateData  data = new RateData();
				
				String surname = rs.getString("surname");
				String name = rs.getString("name");
				
				StringBuilder nameBuilder = new StringBuilder();
				nameBuilder.append(surname);
				if(name!=null && name.length()>0){
					nameBuilder.append(" ").append(name.charAt(0)).append('.');										
				}				
				data.setName(nameBuilder.toString());
				data.setScore(rs.getInt("sum"));
				Rank rank = Rank.getRank(rs.getInt("rank"));
				data.setRank(rank);
				
				List<RateData> list = result.get(rank);
				if(list==null){
					list = new LinkedList<RateData>();
					result.put(rank, list);
				}
				
				list.add(data);
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
