package superlines.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import superlines.core.Rank;
import superlines.core.SuperlinesRules;

public class RulesDAO extends DAO{
	
	private static RulesDAO instance;
	
	public static RulesDAO get(){
		if(instance==null){
			instance = new RulesDAO();
		}
		
		return instance;
	}
	
	private  SuperlinesRules getRules(final Rank rank) {
		
		Connection conn = null;
		PreparedStatement st = null;
		SuperlinesRules rules = null;
				
		try{
			conn = m_dataSource.getConnection();
			st = conn.prepareStatement("select ballscount ballsCount, normalAward normalAward, " +
					"extraAward extraAward, scatterCount scatterCount, colorCount colorCount, " +
					"tableSize tableSize, minScore minScore, progressiveEnabled progressiveEnabled, " +
					"progressive1Threshold progressive1Threshold, progressive2Threshold progressive2Threshold, " +
					"progressive1Mul progressive1Mul, progressive2Mul progressive2Mul from rankdata where rankid = ?");
			st.setInt(1, rank.getRank());
			
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				rules = new SuperlinesRules();
				
				rules.setMinWinBalls(rs.getInt("ballsCount"));
				rules.setNormalAward(rs.getInt("normalAward"));
				rules.setExtraAward(rs.getInt("extraAward"));
				rules.setScatterBallsCount(rs.getInt("scatterCount"));
				rules.setColorCount(rs.getInt("colorCount"));
				rules.setTableWidth(rs.getInt("tableSize"));
				rules.setMinScore(rs.getInt("minScore"));
				rules.setProgressiveEnabled(rs.getBoolean("progressiveEnabled"));
				rules.setProgressive1Threshold(rs.getInt("progressive1Threshold"));
				rules.setProgressive2Threshold(rs.getInt("progressive2Threshold"));
				rules.setProgressive1Multiplier(rs.getInt("progressive1Mul"));
				rules.setProgressvive2Multiplier(rs.getInt("progressive2Mul"));
				
			}
		}
		catch(Exception ex){
			m_log.error(ex);
		}
		finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					m_log.error(e);
				}
			}
			
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					m_log.error(e);
				}
			}
			
		}
		
		return rules;
	}
	
	public int getAcceptableResult(final Rank rank) throws Exception{
		Connection conn = null;
		PreparedStatement st = null;
				
		try{
			conn = m_dataSource.getConnection();
			st = conn.prepareStatement("select minScore minScore from rankdata where rankid  = ? ");
			st.setInt(1, rank.getRank());
			
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				int result = rs.getInt("minScore");
				return result;
			}
			
			throw new Exception("failed get acceptable result");
		}

		finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					m_log.error(e);
				}
			}
			
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					m_log.error(e);
				}
			}
			
		}
	}
	
	public  SuperlinesRules createRules(final Rank rank){
		SuperlinesRules rules = getRules(rank);
		rules.setShowTip(true);
		rules.setAllowLeap(false);
		rules.setCountFlat(true);
		rules.setCountSkew(true);
				
		return rules;
	}
}
