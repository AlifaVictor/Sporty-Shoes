<%@page import="com.example.demo.entity.Category"%>
<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>     
<%@ page isELIgnored="false" %>
<%@page import="java.util.*" %> 
<%@page import="java.nio.file.*" %>
<%@page import="java.io.*" %>  
<%@page import="com.example.demo.entity.Product" %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sporty Shoes</title>
</head>
<body>
<jsp:include page="components/header.jsp" ></jsp:include>
<jsp:include page="components/topbar.jsp" ></jsp:include>

<table border=1 cellspacing=2 cellpadding=4>
 	<tr>
 		<td><b>Product</b></td>
 		<td><b>Price</b></td>
 		<td><b>Category</b></td> 
 		<td></td>
 	</tr>
 	<%
List<Product> list=(List<Product>)request.getAttribute("list");
for(Product product:list){
%>
<tr>
<td><%=product.getName() %></td>
<td><%=product.getPrice() %></td>
<% 
HashMap<Long, String> mapCats = (HashMap<Long, String>)request.getAttribute("mapCats");
%>
<td><%=mapCats.get(product.getID()) %></td>
<%
String path=product.getFilepath();
byte[] images=Files.readAllBytes(new File(path).toPath());
byte[] encodeBase64=Base64.getEncoder().encode(images);

String base64Encoded=new String(encodeBase64,"UTF-8");
%>

<td><img alt="image" height="50" width="50" src="data:image/png;base64,<%=base64Encoded%>"></td>
<td>
 	  			<a href="cartadditem?id=<%=product.getID()%>">Add To Cart</a>
 	  		</td>
</tr>
<%} %>
 	
 	  		
 			
 	  	
 	  
</table>

<jsp:include page="components/footer.jsp"></jsp:include>
</body>
</html>