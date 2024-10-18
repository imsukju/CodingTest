<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register Page</title>
</head>
<body>
    <h2>회원가입</h2>
    <form action="${pageContext.request.contextPath}/users" method="post">
        
        <label for="passwd">UserName:</label>
        <input type="Text" id="UserName" name="UserName"><br> 
 
        
        
        <button type="submit">Register</button>
    </form>
</body>
</html>
