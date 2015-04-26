<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page


import="java.util.*"
import="superlines.server.RateDAO" 
import="superlines.ws.RateData" 
import="superlines.core.Rank"		

%>

<%
	Map<Integer, String> styleMap = new HashMap<Integer, String>();
	styleMap.put(Integer.valueOf(1), "'color:red; font-weight: bold'");
	styleMap.put(Integer.valueOf(2), "'color:green; font-weight:bold'");
	styleMap.put(Integer.valueOf(3), "'color:blue; font-weight:bold'");


	RateDAO dao = RateDAO.get();
	Map<Rank,List<RateData>> data = dao.getRateData(null);
	
	Set<Rank> ranksSorted = new TreeSet<Rank>(new Comparator<Rank>(){
	    public int compare(Rank o1, Rank o2){
	    	return Integer.valueOf(o2.getRank()).compareTo(Integer.valueOf(o1.getRank()));
	    }
	});

	ranksSorted.addAll(data.keySet());
	for(Rank rank : ranksSorted){
		List<RateData> list = data.get(rank);
		
		%> 
		<div class="title"><b>Ранг: <%= rank.toString() %></b></div>
		<table style='width: 300px; margin: 0 auto;' class='table table-striped table-bordered table-condensed'>
			<tr>
				<th></th>
				<th>Имя</th>	
				<th>Очки</th>
				
			</tr>
		 
		
		<% 
				
		int count = 1;
		for(RateData item : list){

			String style= styleMap.get(Integer.valueOf(count));
			%> 
			<tr <%= style==null ? "" : "style="+style  %>> 
				<td align="center"><%= count %></td>
				<td align="center"><%= item.getName() %></td>
				<td align="center"><%= item.getScore() %></td>
			
			</tr>
			<%
			
			count ++;
		}
		
		%> 
		</table>
		
		<% 
		
	}
	

%>



<div style="text-align: center"><a href="?">на главную</a></div>