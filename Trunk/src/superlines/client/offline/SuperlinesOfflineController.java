package superlines.client.offline;


import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import superlines.client.Messages;
import superlines.client.SuperlinesController;
import superlines.client.ui.MainFrame;
import superlines.core.RulesHelper;
import superlines.core.SuperlinesBall;
import superlines.core.SuperlinesContext;
import superlines.core.SuperlinesListener;
import superlines.core.SuperlinesRules;
import superlines.core.SuperlinesTable;

public class SuperlinesOfflineController implements SuperlinesController{
	

	private  SuperlinesContext m_ctx;
	@Override
	public void spotClicked(int x, int y) {
		SuperlinesBall targetBall = m_ctx.getTable().getBalls()[x][y];
		SuperlinesBall clickedBall = m_ctx.getTable().getClickedBall();
		
		if(targetBall.getColor()==0){
			if(clickedBall==null){
				return;
			}
			

        	if(!m_ctx.getRules().isAllowLeap()){
            	List<SuperlinesBall> way = new LinkedList<>();
            	boolean checked = RulesHelper.checkWay(m_ctx, clickedBall, targetBall, way);
            	
            	if(!checked){
            		return;
            	}
        	}

			targetBall.setColor(clickedBall.getColor());
			clickedBall.setColor(0);
			m_ctx.getTable().setClickedBall(null);
			
                        List<SuperlinesBall> winBalls = new LinkedList<>();
                        int win = RulesHelper.countWin(m_ctx, targetBall, winBalls);

                        int score = m_ctx.getScore();
                        if(win>0){
                            m_ctx.setScore(score+win);
                            for(SuperlinesBall ball : winBalls){
                                ball.setColor(0);
                            }
                        }
                        else{
                            scatter();
                        }
		}
        else{
        	
        	m_ctx.getTable().setClickedBall(targetBall);
        	

        }
		
	}

	@Override
	public void scatter() {
        List<SuperlinesBall> newBalls = RulesHelper.scatter(m_ctx);		
        List<SuperlinesBall> winBalls = new LinkedList<>();
        
        for(SuperlinesBall b : newBalls){
        	if(b.getColor()==0){
        		continue;
        	}
        	
        	int win = RulesHelper.countWin(m_ctx, b, winBalls);
            int score = m_ctx.getScore();
            if(win>0){
                m_ctx.setScore(score+win);
                for(SuperlinesBall ball : winBalls){
                    ball.setColor(0);
                }
            }

        }
        
        
        if(RulesHelper.isTableFilled(m_ctx.getTable())){
        	m_ctx.getTable().setFilled(true);
        }
   
}

	
	private static SuperlinesContext createContext(){
		SuperlinesContext ctx = new SuperlinesContext();
		SuperlinesRules rules = new SuperlinesRules();
		rules.setShowTip(true);
		rules.setAllowLeap(false);
		rules.setCountFlat(true);
		rules.setCountSkew(true);
		rules.setMinScore(1000);
		rules.setMinWinBalls(5);
		rules.setExtraAward(50);
		rules.setNormalAward(100);
		rules.setScatterBallsCount(3);
		rules.setColorCount(5);
		rules.setTableWidth(10);
		rules.setShowTip(true);
		rules.setProgressiveEnabled(true);
		rules.setProgressive1Threshold(3000);
		rules.setProgressive2Threshold(7000);
		rules.setProgressive1Multiplier(2);
		rules.setProgressvive2Multiplier(4);
		ctx.setRules(rules);
		
		ctx.setScore(0);
		
    	SuperlinesTable t = new SuperlinesTable();
    	t.setWidth(ctx.getRules().getTableWidth());
    	t.setContext(ctx);
    	ctx.setTable(t);
    	

    	
    	return ctx;
	}
	
	
	public void start(){
		m_ctx =  createContext();       
        RulesHelper.populateNextolors(m_ctx);
        m_ctx.touch();
        
        scatter();		
	}
	
	@Override
	public void restart() {
		
		int result = m_ctx.getTable().isFilled() ? JOptionPane.YES_OPTION : JOptionPane.showConfirmDialog(MainFrame.get(), Messages.SURE, "", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION){

	        SuperlinesContext slctx =  createContext();
	        
	        for(SuperlinesListener l : m_ctx.getListeners()){
	        	slctx.registerListener(l);
	        }
	                    
	        m_ctx = slctx;
	        

	        RulesHelper.populateNextolors(m_ctx);
	        m_ctx.touch();
	        
	        scatter();
		}


	}

	@Override
	public SuperlinesContext getContext() {
		return m_ctx;
	}

}
