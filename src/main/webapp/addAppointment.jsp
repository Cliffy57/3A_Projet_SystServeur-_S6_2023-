<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

    <%--  Label and input for a double that is duration--%>
    <label for="duration">Duration:</label>
    <input type="number" id="duration" name="duration" required>
    <br>

    <label for="clientFirstName">Client First Name:</label>
    <input type="text" id="clientFirstName" name="clientFirstName" required>
    <br>
    <label for="clientLastName">Client Last Name:</label>
    <input type="text" id="clientLastName" name="clientLastName" required>
    <br>

    <label for="employeeFirstName">Employee First Name:</label>
    <input type="text" id="employeeFirstName" name="employeeFirstName" required>
    <br>
    <label for="employeeLastName">Employee Last Name:</label>
    <input type="text" id="employeeLastName" name="employeeLastName" required>
    <br>

    <input type="submit" value="Add Appointment">
</form>

<!-- Add other elements and functionalities as needed -->
</body>
</html>
