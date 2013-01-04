package superlines.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import superlines.core.Configuration;
import superlines.core.Rank;

public class PromotionDAO extends DAO{
	private  final Map<String, String> m_data = new HashMap<>();
	private  final Map<Rank, Integer> m_standardRanks = new HashMap<>();
	
	private static PromotionDAO instance;
	
	public static PromotionDAO get(){
		if(instance==null){
			instance = new PromotionDAO();
		}
		
		return instance;
	}
	
	private PromotionDAO(){
		BufferedReader br = null;
		try{
		Configuration cfg = Configuration.get();
		File folder = cfg.getDataFolder();
		File promotionFolder = new File(folder, "promotion");
		
		for(File entry : promotionFolder.listFiles()){
			 br = new BufferedReader(new InputStreamReader(new FileInputStream(entry), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			do{
				line = br.readLine();
				if(line!=null){
					sb.append(line);
					sb.append('\n');
				}
			}while(line != null);
			
			m_data.put(entry.getName(), sb.toString());
			
			br.close();
		}
		}
		catch(Exception ex){
			m_log.error(ex);
		}
		finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					m_log.error(e);
				}
			}
		}
		
		
		Connection conn = null;
		PreparedStatement st = null;
		try{
			conn = m_dataSource.getConnection();
			st = conn.prepareStatement("select rankid r, normative n from rankdata");
			ResultSet rs = st.executeQuery();
			while(rs.next()){
				int rank = rs.getInt("r");
				int normative = rs.getInt("n");
				
				Rank r = Rank.getRank(rank);
				m_standardRanks.put(r, normative);
			}
					
		}
		catch(Exception ex){
			m_log.error(ex);
		}
		finally{
			if(st!=null){
				try{
					st.close();
				}
				catch(Exception ex){
					m_log.error(ex);					
				}
			}
			
			if(conn!=null){
				try{
					conn.close();
				}
				catch(Exception ex){
					m_log.error(ex);					
				}
			}
		}
		
/*		
		m_standardRanks.put(Rank.NEWBIE, Integer.valueOf(0));
		m_standardRanks.put(Rank.AVERAGE, Integer.valueOf(20000));
		m_standardRanks.put(Rank.EXPERIENCED, Integer.valueOf(50000));
		m_standardRanks.put(Rank.ADEPT, Integer.valueOf(100000));
		m_standardRanks.put(Rank.MASTER, Integer.valueOf(500000));
		m_standardRanks.put(Rank.GODLIKE, Integer.valueOf(1000000));*/
		
		
	}
	

	
	public String getPromotionMessage(final  Rank newRank, final String locale){
			return m_data.get(String.format("%s-%s.properties", newRank.name(), locale));
	}


	
	public int getQualifiedRate(final Rank rank){
		return m_standardRanks.get(rank);
	}
	
	public  int getQualifiedRateTest(final Rank rank){
		return m_standardRanks.get(rank)/10;
	}

}
