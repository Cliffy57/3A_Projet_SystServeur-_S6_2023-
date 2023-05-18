<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Add Appointment</title>
  <!-- Add your CSS and JavaScript references here -->
</head>
<body>
<h1>Add Appointment</h1>
<form action="appointment" method="post">
  <label for="date">Date:</label>
  <input type="datetime-local" id="date" name="date" required>
  <br>
  <label for="client">Client:</label>
  <input type="text" id="client" name="client" required>
  <br>
  <label for="employee">Employee:</label>
  <input type="text" id="employee" name="employee" required>
  <br>
  <input type="submit" value="Add Appointment">
</form>

<!-- Add other elements and functionalities as needed -->
</body>
</html>
