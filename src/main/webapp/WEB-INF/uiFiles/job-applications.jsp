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
            <th>Actions</th> <!-- Updated to include view and delete actions -->
        </tr>
        <c:forEach var="application" items="${jobApplications}">
            <tr>
                <td>${application.id}</td>
                <td>${application.name}</td>
                <td>${application.email}</td>
                <td>${application.phone}</td>
                <td>
                    <!-- View Resume Button -->
                    <a href="${pageContext.request.contextPath}/view-resume?fileName=${application.resumeFileName}" target="_blank">View</a>
                </td>
                <td>
                    <!-- Delete Button -->
                    <form action="${pageContext.request.contextPath}/delete-application" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${application.id}">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
