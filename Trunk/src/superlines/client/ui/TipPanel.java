/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package superlines.client.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import superlines.client.SuperlinesController;
import superlines.client.ColorHelper;
import superlines.core.SuperlinesContext;
import superlines.core.SuperlinesRules;

/**
 *
 * @author Sashonk
 */
public class TipPanel extends JPanel{
    
    private SuperlinesController m_ctr;
    private int nextColorsCount = 0;

    private final static int SIZE = 40; 
    private List<JPanel> m_panels = new LinkedList<>();
    
    public void setController(final SuperlinesController ctr){
        m_ctr = ctr;
    }
    
    public void updatePanels(final List<Integer> data){
    	for(int i = 0; i<m_panels.size();i++){
    		JPanel p = m_panels.get(i);
    		//p.setPreferredSize(new Dimension(SIZE,SIZE));
    		p.setBackground(ColorHelper.number2Color(data.get(i)));
    	}
    }
    
    public void init(final SuperlinesContext ctx){
        
        int count  = ctx.getNextColors().size();
        if(nextColorsCount!=count){
            removeAll();
            m_panels.clear();
            for(int i = 0;i<count; i++){
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(SIZE,SIZE));
                add(panel);
                m_panels.add(panel);
            }    
            
            nextColorsCount = count;
            this.doLayout();
        }
        

         for(int i = 0;i<count; i++){
        	 JPanel  panel =m_panels.get(i);
        		 panel.setBackground(ColorHelper.number2Color(ctx.getNextColors().get(i)));
        	 

         }
            

        
        
    }
}
