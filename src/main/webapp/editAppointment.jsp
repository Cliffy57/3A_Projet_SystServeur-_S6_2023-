<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.iut.licenceproservlet.Client" %>
<%@ page import="fr.iut.licenceproservlet.Employee" %>
<%@ page import="fr.iut.licenceproservlet.Appointment" %>

<%
  List<Client> clients = (List<Client>) request.getAttribute("clients");
  List<Employee> employees = (List<Employee>) request.getAttribute("employees");
  Appointment appointment = (Appointment) request.getAttribute("appointment");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Modify Appointment</title>
  <!-- Add your CSS and JavaScript references here -->
</head>
<body>
<h1>Modify Appointment</h1>
<form action="appointment-servlet" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= appointment.getId() %>">

  <label for="date">Date:</label>
  <input type="datetime-local" id="date" name="date" value="<%= appointment.getDate().toString() %>" required>
  <br>

  <label for="duration">Duration:</label>
  <input type="number" id="duration" name="duration" value="<%= appointment.getDuration() %>" min="1" required>
  <br>

  <label for="client">Client:</label>
  <select id="client" name="client_id" required>
    <% for (Client client : clients) { %>
    <option value="<%= client.getId() %>" <%= client.getId().equals(appointment.getClient().getId()) ? "selected" : "" %>><%= client.getFirstName() %> <%= client.getLastName() %></option>
    <% } %>
  </select>
  <br>

  <label for="employee">Employee:</label>
  <select id="employee" name="employee_id" required>
    <% for (Employee employee : employees) { %>
    <option value="<%= employee.getId() %>" <%= employee.getId().equals(appointment.getEmployee().getId()) ? "selected" : "" %>><%= employee.getFirstName() %> <%= employee.getLastName() %></option>
    <% } %>
  </select>
  <br>

  <input type="submit" value="Update Appointment">
</form>

<!-- Add other elements and functionalities as needed -->
</body>
</html>
