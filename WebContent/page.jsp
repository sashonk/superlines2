<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Superlines 2.0</title>

<style>
	body{
		font-family: times new roman;
		
	}
	

	
	td.content{
		font-size: 14px;
	}
	

	
	div.title{
		text-align: center;
		margin-top: 40px;
		margin-bottom: 5px;

	}

</style>

</head>
<body>
	<table width="50%" border="0" align="center" style="margin-bottom: 50px;">
		<tr ><td align="center" style="font-size: 48px; font-weight: bold;"><img  src="tree.png" width="60" height="60">superlines 2.0<img src="tree.png" width="60" height="60"></td></tr>
		<tr> <td align="center" style="font-size: 36px; font-weight: bold; color:red;"> новогоднее издание!</td> </tr>
		<tr><td class="content">
		
			<% 
				String p = request.getParameter("page");
				if(p==null){
					p = "";
				}
			
				
			%>
			
			<%  if(p.equals("rules")){ %>
				<%@ include file="rules.jsp" %>				
				<% 	} 
				else if(p.equals("rate")){ %>
				<%@ include file="rate.jsp" %>
				<% 	}
				else if(p.equals("registration")){ %>
				<%@ include file="registration.jsp" %>
				<% 	}
				else if(p.equals("remind")){ %>
				<%@ include file="remind.jsp" %>
				<% 	}
				else{ %>
				<%@ include file="index.jsp" %>			
			<% 	}%>

		
		</td></tr>
		<tr><td align="center" style="font-size:11px;">&copysashonk 2012</td></tr>		
	</table>
	

</body>
</html>