<%--
  Created by IntelliJ IDEA.
  User: hugof
  Date: 02/03/2023
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Variables Example</title>
</head>
<body>
<h1>Using Variables</h1>
<%-- Define the variables --%>
<% String firstName = "John"; %>
<% String lastName = "Doe"; %>
<% int age = 30; %>

<%-- Use the variables in the HTML code --%>
<p>Name: <%= firstName %> <%= lastName %></p>
<p>Age: <%= age %></p>
</body>
</html>

