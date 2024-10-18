<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Post</title>
</head>
<body>
    <h1>Edit Post</h1>

    <form:form method="post" modelAttribute="post" action="${pageContext.request.contextPath}/posts/${post.postid}/edit">
        <label for="title">Title:</label>
        <form:input path="title" />
        <br/>
        <label for="text">Content:</label>
        <form:textarea path="text"></form:textarea>
        <br/>
        <button type="submit">Save</button>
    </form:form>

    <a href="${pageContext.request.contextPath}/posts">Back to Posts</a>
</body>
</html>