<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page 

	import="java.util.*" 
	import="java.util.regex.Pattern"
	import="superlines.server.AccountDAO"
	import="superlines.core.Util"
%>

<%  

 String confirmed = request.getParameter("confirm");
 String created = request.getParameter("create");
 
 if(created!=null){
	 %> 
	 
	<div style="text-align:center; padding: 20px;"> На указанный вами почтовый адрес отправлен email с ссылкой для активации аккаунта.</div>
	 
	 
	 <% 
 }
 else if(confirmed!=null){
	 
	 String token = request.getParameter("token");
	 int st =  AccountDAO.get().confirmAccount(token);
	 
	 if(st == 0) {
	 %> 	 
	 <div style="text-align:center; padding: 20px;">  Аккаунт подтвержден!</div>	 	 
	 <%
	 }
	 else if (st == 1){
		 %> 
		 	 
		 	<div style="text-align:center; padding: 20px; color: red"> Заявка не найдена</div>			 
		 <%
	 }
	 else{
		 %>
		 
		 	<div style="text-align:center; padding: 20px; color: red">  Аккаунт не подтвержден!</div>			 
		 <%
	 }


 }
 else{


String name = request.getParameter("name");
String surname = request.getParameter("surname");
String login = request.getParameter("login");
String password = request.getParameter("password");
String email = request.getParameter("email");
	

%>



<form style='width: 300px; margin: 0 auto;' class='form' role='form' method="post">
	 <div class="form-group">
    	<label for="name">Имя:</label>
   		 <input  class="form-control" name="name" id="name">
	  </div>
	 <div class="form-group">
    	<label for="surname">Фамилия:</label>
   		 <input  class="form-control" name="surname" id="surname">
	  </div>
	 <div class="form-group">
    	<label for="login">Логин:</label>
   		 <input  class="form-control" name="login" id="login">
	  </div>
	 <div class="form-group">
    	<label for="password">Пароль:</label>
   		 <input type='password' class="form-control" name="password" id="password">
	  </div>	
	 <div class="form-group">
    	<label for="email">E-mail:</label>
   		 <input type='email' class="form-control" name="email" id="email">
	  </div>
	  <div class="form-group" >	  	  
	  <input class='button' style='display:inline-block; margin: 0 auto;' type="submit" value="Принять">
</div>		


<input type="hidden"  name="register" value="true">
<input type="hidden"  name="page" value="registration">	
</form>







<%
	
	String action = request.getParameter("register");

	if(action!=null){
		
		List<String> messages = new LinkedList<String>();
		
		if(isBlank(name)){
			messages.add("не указано имя");
		}
		
		if(isBlank(surname)){
			messages.add("не указана фамилия");		
		}
		if(isBlank(login)){
			messages.add("не указан логин");		
		}		
		if(isBlank(password)){
			messages.add("не указан пароль");		
		}		
		if(isBlank(email)){
			messages.add("не указан почтовый адрес");		
		}	
		
		
		if(messages.size()==0){
			if(!Util.isValidEmailAddress(email)){
				messages.add("введен некорректный почтовый адрес");							
			}
			
			if(!AccountDAO.get().isUniqueLogin(login)){
				messages.add("данный логин уже используется");						
			}			
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
		else{
			AccountDAO.get().createAccount(login, password, name, surname, email);
			
			response.sendRedirect("?page=registration&create");
		}
		
	}


 }

%>

<%!
	private static boolean isBlank(final String param){
		return param== null || param.equals("");
	}




%>





<div style="text-align: center"><a href="?">на главную</a></div>

