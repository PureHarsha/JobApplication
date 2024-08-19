<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Job Applications</title>
        <link rel="icon" type="image/png" href="images/tcslogo.png">
    
</head>
<body>
    <h1>Submitted Job Applications</h1>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Resume</th>
        </tr>
        <c:forEach var="application" items="${jobApplications}">
            <tr>
                <td>${application.id}</td>
                <td>${application.name}</td>
                <td>${application.email}</td>
                <td>${application.phone}</td>
                <td><a href="${pageContext.request.contextPath}/download?fileName=${application.resumeFileName}">Download</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>

