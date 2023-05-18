<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Appointment Details</title>
    <!-- Add your CSS and JavaScript references here -->
</head>
<body>
<h1>Appointment Details</h1>
<table>
    <tr>
        <th>Date</th>
        <th>Client</th>
        <th>Employee</th>
    </tr>
    <tr>
        <td>${appointment.date}</td>
        <td>${appointment.client.name}</td>
        <td>${appointment.employee.name}</td>
    </tr>
</table>

<!-- Add other elements and functionalities as needed -->
</body>
</html>
