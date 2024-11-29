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
	
	
	<!-- 댓글 목록 -->
	<h2>Comments</h2>
	<c:if test="${!empty comments}">
	    <ul>
	        <c:forEach var="comment" items="${comments}">
	            <li>
	                <strong>comment | ${comment.user.userName} (UserName)</strong>: ${comment.text}
	                <form method="get" action="${pageContext.request.contextPath}/posts/${post.postid}/comments/${comment.commentid}/edit" style="display:inline;">
	                    <button type="submit">Edit</button>
	                </form>
	                <form method="post" action="${pageContext.request.contextPath}/posts/${post.postid}/comments/${comment.commentid}/delete" style="display:inline;">
	                    <button type="submit" onclick="return confirm('Are you sure you want to delete this comment?');">Delete</button>
	                </form>
	            </li>
	        </c:forEach>
	    </ul>
	</c:if>
	<c:if test="${empty comments}">
	    <p>No comments yet.</p>
	</c:if>


	 <!-- 댓글 추가 폼 -->
	<h2>Add a Comment</h2>
	<form:form method="post" modelAttribute="comment" action="${pageContext.request.contextPath}/posts/${post.postid}/comments">
		<label for="comment.user">User:</label>
		<select id="USER" name="user.userid" required>
		    <c:forEach var="user" items="${userList}">
		        <option value="${user.userid}">user Name : ${user.userName}</option>
		    </c:forEach>
		</select>
	    <br/>
		
	    <label for="text">Comment:</label>
	    <form:textarea path="text"></form:textarea>
	    <br/>
	    <button type="submit">Submit Comment</button>
	</form:form>
</body>
</html>