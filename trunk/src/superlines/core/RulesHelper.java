package superlines.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import superlines.deicstra.DeicstraArea;
import superlines.deicstra.NoWayException;

public class RulesHelper {
	
		private static long counter = 0;
	
		public static boolean checkWay(final SuperlinesContext ctx, final SuperlinesBall begin, final SuperlinesBall end, final List<SuperlinesBall> way){
			if(ctx.getRules().isAllowLeap()){
				return true;
			}
			
			int[][] area = new int[ctx.getTable().getSize()][ctx.getTable().getSize()];
			for(int i = 0; i<ctx.getTable().getSize(); i++){
				for(int j = 0; j<ctx.getTable().getSize(); j++){
					if(ctx.getTable().getBall(i, j).getColor()!=0){
						area[i][j] = -1;
					}
					else{
						area[i][j] = 0;
					}
				}				
			}
			
			DeicstraArea da = new DeicstraArea();
			da.makeArea(area);
			
	
				List<Point> path = da.findWay(begin.getX(), begin.getY(), end.getX(), end.getY());
				if(path==null){
					return false;
				}
				
				for(Point p : path){
					way.add(ctx.getTable().getBall(p.x, p.y));
				}
				
				return true;

			
		}
		
		public static boolean isTableFilled(final SuperlinesTable table){
			boolean filled = true;
			for(int i = 0; i<table.getSize(); i++){
				for(int j = 0; j<table.getSize(); j++){
					if(table.getBall(i,j).getColor()==0){
						filled = false; 
					}
				}
			}		
			return filled;
		}
    
        public static List<SuperlinesBall> scatter(final SuperlinesContext ctx){
			SuperlinesTable t = ctx.getTable();
			SuperlinesRules r = ctx.getRules(); 
	        List<SuperlinesBall> balls = getEmptyBalls(t);
	        List<SuperlinesBall> newBalls = new ArrayList<>(r.getScatterBallsCount());
                
                if(ctx.getNextColors().size()==0){
                    populateNextolors(ctx);
                }
   
                
     
                Random rnd = new Random(System.currentTimeMillis());
                for(int i = 0;i<r.getScatterBallsCount(); i++){
                    if(balls.size()==0){
                        break;
                    }
                    
                    int value = rnd.nextInt(balls.size());                    
                    SuperlinesBall b = balls.get(value);
                    b.setColor(ctx.getNextColors().get(i).intValue());
                    newBalls.add(b);
                    balls.remove(b);
                }
                
                populateNextolors(ctx);
                return newBalls;
        }
        
        public static void populateNextolors(final SuperlinesContext ctx){
            List<Integer> nextColors = new LinkedList<Integer>();
    
            SuperlinesRules r = ctx.getRules();
            int count =r.getScatterBallsCount();
            
   
            Random rnd = new Random(System.currentTimeMillis()+(++counter));
            for(int i = 0; i<count ; i++){
                int number = rnd.nextInt(r.getColorCount())+1; //ZERO-exclusive
                nextColors.add(Integer.valueOf(number));                        
            }
            
            ctx.setNextColors(nextColors);
        }
    
        public static List<SuperlinesBall> getEmptyBalls(final SuperlinesTable table){
            List<SuperlinesBall> balls = new LinkedList<SuperlinesBall>();
            for(int i = 0; i<table.getSize(); i++){
                for(int j = 0; j<table.getSize(); j++){
                	SuperlinesBall ball = table.getBall(i, j);
                	if(ball.getColor()==0){
                		balls.add(ball);
                	}
                }
            }
            
            return balls;
        }
    
	public static int countWin(final SuperlinesContext ctx, final SuperlinesBall b, final List<SuperlinesBall> winBalls){
		int totalScore = 0;
		
		SuperlinesTable t = ctx.getTable();
		SuperlinesRules r = ctx.getRules();                
                List<SuperlinesBall> collected;
            
		
                if(r.isCountFlat()){
                    collected = collect(t,b, 0, 1);
                    if(collected.size()>=r.getMinWinBalls()){
                    	winBalls.addAll(collected);
                    }
                    
                    collected=collect(t,b, -1, 0);
                    if(collected.size()>=r.getMinWinBalls()){
                    	winBalls.addAll(collected);    
                    }
                }
                
                if(r.isCountSkew()){    
                    collected = collect(t,b , -1,1);
                    if(collected.size()>=r.getMinWinBalls()){
                    	winBalls.addAll(collected);    
                    }
                    
                     collected = collect(t,b, -1, -1);
                     if(collected.size()>=r.getMinWinBalls()){
                     	winBalls.addAll(collected);    
                     }             
                }

		
		if(winBalls.size()>=r.getMinWinBalls()){
			totalScore +=r.getNormalAward();
			totalScore += r.getExtraAward()* (winBalls.size()-r.getMinWinBalls());
		}
		
		if(r.isProgressiveEnabled()){
			int currentScore = ctx.getScore();
			if(currentScore>r.getProgressive2Threshold()){
				totalScore*=r.getProgressive2Multiplier();
			}
			else if(currentScore>r.getProgressive1Threshold()){
				totalScore*=r.getProgressive1Multiplier();
			}
		}
								
		return totalScore;
	}
	

	
	private static List<SuperlinesBall> collect(final SuperlinesTable table, final SuperlinesBall ball, int incx, int incy){
		
		List<SuperlinesBall> balls = new LinkedList<SuperlinesBall>();
		balls.add(ball);
		int targetColor=ball.getColor();
		
		for(int i = 0; i<2; i++){
			int x = ball.getX();
			int y = ball.getY();
			while(true){
				x += incx;
				y += incy;
				
				if(x<0 || x>=table.getSize() || y<0 || y>=table.getSize()){
					break;
				}
				
				int c = table.getBall(x, y).getColor();
				if(c==targetColor){
					balls.add(table.getBall(x, y));
				}
				else{
					break;
				}
			}
		
			incx*=-1;
			incy*=-1;
		}
		
		
		return balls;
	}
	

}
