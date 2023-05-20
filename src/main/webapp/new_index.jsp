<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*, fr.iut.licenceproservlet.Appointment" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des rendez-vous</title>
    <!-- include Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <!-- include custom CSS -->
    <style>
        body {
            background-color: #f5f5f5;
        }
        .table {
            margin-top: 20px;
        }
        .btn {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="my-4">Liste des rendez-vous</h1>

    <!-- Display error message -->
    <% String error = (String) request.getAttribute("error"); if (error != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= error %>
    </div>
    <% } %>

    <!-- Add Appointment Button -->
    <a href="appointment-servlet?action=new" class="btn btn-primary">Add Appointment</a>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Date et heure</th>
            <th>Client</th>
            <th>Employé</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
            if(appointments!=null){
                for(Appointment appointment : appointments){ %>
        <tr>
            <td><%=appointment.getId()%></td>
            <td><%= DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(appointment.getDate()) %></td>
            <td><%=appointment.getClient().getLastName()+" "+appointment.getClient().getFirstName()%></td>
            <td><%=appointment.getEmployee().getLastName()+" "+appointment.getEmployee().getFirstName()%></td>
            <td>
                <!-- Modify Appointment Button -->
                <a href="appointment-servlet?action=modify&id=<%=appointment.getId()%>" class="btn btn-info">Modify</a>
            </td>
        </tr>
        <% } } else { %>
        <tr>
            <td colspan="5">No appointments found</td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <!-- Add Sorting Buttons -->
    <button type="button" onclick="window.location.href='appointment-servlet?action=sort&by=date'" class="btn btn-success">Sort by Date</button>
    <button type="button" onclick="window.location.href='appointment-servlet?action=sort&by=client'" class="btn btn-success">Sort by Client</button>
    <button type="button" onclick="window.location.href='appointment-servlet?action=sort&by=employee'" class="btn btn-success">Sort by Employee</button>
</div>
<!-- include Bootstrap JS and jQuery -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
