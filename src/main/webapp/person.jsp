<jsp:useBean id="person" scope="request" type="fr.iut.licenceproservlet.draft.Person"/>
<%--
  Created by IntelliJ IDEA.
  User: hugof
  Date: 02/03/2023
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Person Example</title>
</head>
<body>
<h1>Person Example</h1>
<p>Name: ${person.name}</p>
</body>
</html>
