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
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
        }
        form {
            margin-top: 20px;
        }
        form .form-group {
            margin-bottom: 15px;
        }
        form .form-group label {
            display: inline-block;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="appointment-servlet" class="btn btn-secondary my-2">Return</a>
    <h1 class="my-4">Add Appointment</h1>
    <form action="appointment-servlet" method="post">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label for="date">Date:</label>
            <input type="datetime-local" id="date" name="date" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="duration">Duration:</label>
            <input type="number" id="duration" name="duration" min="1" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="client">Client:</label>
            <select id="client" name="client" class="form-control" required>
                <% for (Client client : clients) { %>
                <option value="<%= client.getId() %>"><%= client.getFirstName() %> <%= client.getLastName() %></option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="employee">Employee:</label>
            <select id="employee" name="employee" class="form-control" required>
                <% for (Employee employee : employees) { %>
                <option value="<%= employee.getId() %>"><%= employee.getFirstName() %> <%= employee.getLastName() %></option>
                <% } %>
            </select>
        </div>

        <input type="submit" value="Add Appointment" class="btn btn-primary">
    </form>
</div>

<!-- Include Bootstrap JS and jQuery -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
