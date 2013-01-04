package superlines.client;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ColorHelper {
	
	private final static Map<Integer, Color> m_colorMap = new HashMap<Integer, Color>();
	static{
		m_colorMap.put(Integer.valueOf(0), Color.gray);
		m_colorMap.put(Integer.valueOf(1),	new Color(255, 0, 0));
		m_colorMap.put(Integer.valueOf(2), new Color(0, 255, 0));
		m_colorMap.put(Integer.valueOf(3), new Color(255,255,0));
		m_colorMap.put(Integer.valueOf(4),new Color(0,0,255));
		m_colorMap.put(Integer.valueOf(5), new Color(153,0,153));
		m_colorMap.put(Integer.valueOf(6), new Color(102, 102, 0));
		m_colorMap.put(Integer.valueOf(7), new Color(255,102,0));
		m_colorMap.put(Integer.valueOf(8), new Color(0,255,255));
		m_colorMap.put(Integer.valueOf(9), new Color(0,102,51));
		m_colorMap.put(Integer.valueOf(10), new Color(153,153,255));		
		m_colorMap.put(Integer.valueOf(11), new Color(153,153,255));
		m_colorMap.put(Integer.valueOf(12), new Color(123,0,0));		
		m_colorMap.put(Integer.valueOf(13), new Color(255,153,255));			
	}
	
	
	public static Color number2Color(int nums){
		return m_colorMap.get(Integer.valueOf(nums));
	}
	

	
	public static int color2Number(final Color c){
		for(Entry<Integer, Color> entry : m_colorMap.entrySet()){
			if(entry.getValue()==c){
				return entry.getKey().intValue();
			}
		}
		
		return 0;
	}
}
