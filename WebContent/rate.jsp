<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page

import="java.util.List" 
import="superlines.server.RateDAO" 
import="superlines.ws.RateData" 
		

%>


<div class="title"><b>рейтинги</b></div>
<table border="1" cellpadding="5" width="40%" align="center" style="margin-bottom: 50px;">
	<tr>
		<th></th>
		<th>Имя</th>
		<th>Ранг</th>		
		<th>Очки</th>
		
	</tr>

	<%
		RateDAO dao = RateDAO.get();
	
		List<RateData> data = dao.getRateData(null);
		int count = 1;
		for(RateData item : data){
			%> 
			<tr> 
				<td align="center"><%= count %></td>
				<td align="center"><%= item.getName() %></td>
				<td align="center"><%= item.getRank() %></td>
				<td align="center"><%= item.getScore() %></td>
			
			</tr>
			<%
			
			count ++;
		}
	
	%>
	

</table>

<div style="text-align: center"><a href="?">на главную</a></div>