<%@ page isELIgnored="false" %>
<img src="shoe5.jpg" align="top" width="15%" height="100"/>
<a href="home">Home</a> | 
<% if (session.getAttribute("user_id") == null ) 
{ %>
	<a href="login">Login/Signup</a> | 
<% }  else {
	
%>
	<a href="dashboard">Dashboard</a> | 
	<a href="cart">Cart</a> | 
	<a href="logout">Logout</a>
	<br>
	 Welcome to the world of foot wear shopping! <%=session.getAttribute("user_name") %>
	 <br>
	<a href="editprofile">Edit Profile</a> | 
	<a href="memberpurchases">Your Orders</a> 

<% }  %>


 
<br><br>