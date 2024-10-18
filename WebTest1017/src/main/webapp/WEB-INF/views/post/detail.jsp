<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Post Detail</title>
</head>
<body>
    <h1>${post.title}</h1>
    <p><strong>title:</strong> ${post.title}</p>
    <p><strong>user:</strong> ${post.user.userName}</p>
    <p><strong>Text:</strong> ${post.text}</p>

    <!-- 게시글 수정 -->
	<a href="${pageContext.request.contextPath}/posts/${post.postid}/edit" class="btn">Edit Post</a>

 

    <!-- 게시글 삭제 버튼 -->
    <form action="${pageContext.request.contextPath}/posts/${post.postid}/delete" method="post">
        <button type="submit" class="btn btn-cancel" onclick="return confirm('Are you sure you want to delete this post?');">Delete Post</button>
    </form>

    <a href="${pageContext.request.contextPath}/posts">Back to Posts</a>
</body>
</html>