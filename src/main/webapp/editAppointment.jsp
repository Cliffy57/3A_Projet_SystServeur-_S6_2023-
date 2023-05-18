<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">


<title>Edit Appointment</title>
<!-- Add your CSS and JavaScript references here -->

</head>
<body>
<h1>Edit Appointment</h1>
<form action="appointment" method="post">
  <input type="hidden" name="id" value="${appointment.id}">
  <label for="date">Date:</label>
  <input type="datetime-local" id="date" name="date" value="${appointment.date}" required>
  <br>
  <label for="client">Client:</label>
  <input type="text" id="client" name="client" value="${appointment.client.name}" required>
  <br>
  <label for="employee">Employee:</label>
  <input type="text" id="employee" name="employee" value="${appointment.employee.name}" required>
  <br>
  <input type="submit" value="Save Changes">
</form>

<!-- Add other elements and functionalities as needed -->

</body>
</html>