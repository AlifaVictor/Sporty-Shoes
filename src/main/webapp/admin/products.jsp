<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>  
<%@page import="com.example.demo.entity.Product" %>  
<%@page import="java.util.*" %> 
<%@page import="java.nio.file.*" %>
<%@page import="java.io.*" %>  
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Setup Products</title>
</head>
<body>
<jsp:include page="../components/admin-header.jsp" ></jsp:include>
<jsp:include page="../components/admin-topbar.jsp" ></jsp:include>

Total Products: ${list.size()}  &nbsp;&nbsp; <a href="admineditproduct?id=0">Add Product</a><br>
<table border=1 cellspacing=2 cellpadding=4>
 	<tr>
 		<td><b>Product</b></td>
 		<td><b>Price</b></td>
 		<td><b>Added On</b></td>
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
 	<td><%=product.getDateAdded()%></td>
 	<% HashMap<Long, String> mapCats = (HashMap<Long, String>)request.getAttribute("mapCats");%>
    <td><%=mapCats.get(product.getID()) %></td>
    <%
		String path=product.getFilepath();
		byte[] images=Files.readAllBytes(new File(path).toPath());
		byte[] encodeBase64=Base64.getEncoder().encode(images);
		String base64Encoded=new String(encodeBase64,"UTF-8");
    %>
    <td><img alt="image" height="50" width="50" src="data:image/png;base64,<%=base64Encoded%>"></td>
    <td>
 	<a href="admineditproduct?id=<%=product.getID()%>">Edit</a> | <a href="admindeleteproduct?id=<%=product.getID()%>">Delete</a>
   </td>

 	<%} %>
</table> 	
<jsp:include page="../components/admin-footer.jsp"></jsp:include>
</body>
</html>