<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Post Search</title>
</head>
<body>
    <h1>Search Posts by Name</h1>
    
	
	<!-- 검색 폼 -->
	 <form action="${pageContext.request.contextPath}/posts/search" method="get">
	     <input type="text" name="search" value="${param.search}" placeholder="Search by name">
	     <button type="submit">Search</button>
	 </form>
    
    <hr/>

    <!-- 검색 결과 출력 -->
    <c:if test="${not empty posts}">
        <h2>Search Results:</h2>
        <ul>
            <c:forEach var="post" items="${posts}">
                <li>Post tile : ${post.title}: || User Name ${post.user.userName}</li>
            </c:forEach>
        </ul>
    </c:if>
    
    <c:if test="${empty posts}">
        <p>No posts found.</p>
    </c:if>

</body>
</html>