<%@ page import="fr.iut.licenceproservlet.draft.Car" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: hugof
  Date: 09/03/2023
  Time: 18:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Cars</title>
</head>
<body>
<h1>All Cars</h1>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Model</th>
        <th>Color</th>
    </tr>
    </thead>
    <tbody>
    <% List<Car> cars = (List<Car>) request.getAttribute("cars");
        for (Car car : cars) { %>
    <tr>
        <td><%= car.getId() %></td>
        <td><%= car.getModel() %></td>
        <td><%= car.getColor() %></td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>
