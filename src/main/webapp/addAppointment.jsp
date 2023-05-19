<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.iut.licenceproservlet.Client" %>
<%@ page import="fr.iut.licenceproservlet.Employee" %>

<%
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<Employee> employees = (List<Employee>) request.getAttribute("employees");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Appointment</title>
    <!-- Add your CSS and JavaScript references here -->
</head>
<body>
<h1>Add Appointment</h1>
<form action="appointment-servlet" method="post">
    <input type="hidden" name="action" value="add">
    <label for="date">Date:</label>
    <input type="datetime-local" id="date" name="date" required>
    <br>

    <label for="duration">Duration:</label>
    <input type="number" id="duration" name="duration" min="1" required>
    <br>

    <label for="client">Client:</label>
    <select id="client" name="client" required>
        <% for (Client client : clients) { %>
        <option value="<%= client.getId() %>"><%= client.getFirstName() %> <%= client.getLastName() %></option>
        <% } %>
    </select>
    <br>

    <label for="employee">Employee:</label>
    <select id="employee" name="employee" required>
        <% for (Employee employee : employees) { %>
        <option value="<%= employee.getId() %>"><%= employee.getFirstName() %> <%= employee.getLastName() %></option>
        <% } %>
    </select>
    <br>

    <input type="submit" value="Add Appointment">
</form>

<!-- Add other elements and functionalities as needed -->
</body>
</html>
