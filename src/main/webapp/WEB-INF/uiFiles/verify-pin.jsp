<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Verify PIN</title>
        <link rel="icon" type="image/png" href="images/tcslogo.png">
    
</head>
<body>
    <h1>Verify PIN</h1>
    <form action="${pageContext.request.contextPath}/verify-pin" method="post">
        <label for="pin">Enter PIN:</label>
        <input type="password" id="pin" name="pin" required>
        <button type="submit">Submit</button>
    </form>
    
    <!-- Display error message if exists -->
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
</body>
</html>
