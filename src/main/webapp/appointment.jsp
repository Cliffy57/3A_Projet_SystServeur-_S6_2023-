<%--
  Created by IntelliJ IDEA.
  User: hugof
  Date: 11/05/2023
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<jsp:useBean id="appointment" scope="request" type="fr.iut.licenceproservlet.Appointment"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Liste des rendez-vous</title>
</head>
<body>
<h1>Liste des rendez-vous</h1>
<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Employé</th>
        <th>Client</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${appointment}" var="appointment">
        <tr>
            <td>${appointment.date}</td>
            <td>${appointment.employe.nom}</td>
            <td>${appointment.client.nom}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
