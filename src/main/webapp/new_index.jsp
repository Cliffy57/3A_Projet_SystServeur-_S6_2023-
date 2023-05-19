<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*, fr.iut.licenceproservlet.Appointment" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des rendez-vous</title>
</head>
<body>
<h1>Liste des rendez-vous</h1>

<!-- Add Appointment Button -->
<a href="appointment-servlet?action=new">
    <button type="button">Add Appointment</button>
</a>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Date et heure</th>
        <th>Nom du client</th>
        <th>Nom de l'employé</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
        if(appointments!=null){
            for(Appointment appointment : appointments){
    %>
    <tr>
        <td><%=appointment.getId()%></td>
        <td><%= DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(appointment.getDate()) %></td>
        <td><%=appointment.getClient().getLastName()%></td>
        <td><%=appointment.getEmployee().getLastName()%></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="4">No appointments found</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>
