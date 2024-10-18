<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Create New Post</title>
</head>
<body>
    <h1>Create New Post</h1>

    <form:form method="post" action="${pageContext.request.contextPath}/posts" modelAttribute="post">
        <label for="name">Name:</label>
        <form:input path="title" />
        <br />

        <label for="post.user">User:</label>
		<select id="USER" name="user.userid" required>
		    <c:forEach var="user" items="${userList}">
		        <option value="${user.userid}">user Name : ${user.userName}</option>
		    </c:forEach>
		</select>
        <br />


        <label for="text">Text:</label>
        <form:textarea path="text" />
        <br />

        <button type="submit" class="btn">Submit</button>
        <a href="${pageContext.request.contextPath}/posts">Cancel</a>
    </form:form>
</body>
</html>