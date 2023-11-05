<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="authenticate"> 
<div align="center">
<table>
<tr>
<td>Username : </td> <td> <input type="text"  name="username"/></td>
</tr>
<tr>
<td>Password : </td> <td> <input type="password" name="password"/></td>
</tr>
<tr>
<td><input type="submit" value="Admit me!" /></td>
<td><input type="submit" formaction="/insertadmin" value="Create new Admin!" /></td>
</tr>
</table>
</div>
</form>
</body>
</html>