<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page 
	import="java.util.*"
	import="superlines.server.AccountDAO"
%>

<%
	String login = request.getParameter("login");
	List<String> messages = new ArrayList<String>(2);
	boolean sent = false;
	if(login!=null){
		 int code = AccountDAO.get().remindPassword(login);
		
		 if (code == 0){%> 
		<div style="text-align:center; padding: 20px;">  Пароль выслан вам на почту </div>	 	  
		<%
			sent = true;
		 }
		 else if(code==1){
			 messages.add("пользователь не найден");
		 }
		 else if(code==2){
			 messages.add("не указан корректный почтовый адрес");
		 }
	}

	
	
	if(!sent){
%>

<form style='width: 500px; margin: 0 auto;' class='form-inline' style="text-align:center; margin-top: 20px; margin-bottom: 20px;">
<span style="margin: 10px;">Введите свой логин</span>
<span style="margin: 10px;"><input class='form-control' name="login"></span>
<span style="margin: 10px;"><input class='button form-control' type="submit" value="Напомнить"></span>
<input type="hidden"  name="page" value="remind">	
</form>

<% 
	}
	
	if(messages.size()>0){
	%>
		<ul>
			<% for(String message : messages) { %>
			
				<li style="color:red;"><%= message  %></li>
		
			<%  } %>
		</ul>
	
	<%
	
		}
 %>


<div style="text-align: center"><a href="?">на главную</a></div>