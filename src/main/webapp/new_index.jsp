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

<!-- Display error message -->
<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p style="color: red;"><%= error %></p>
<%
    }
%>

<!-- Add Appointment Button -->
<a href="appointment-servlet?action=new">
    <button type="button">Add Appointment</button>
</a>

<table border="1">
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
    <%
        List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
        if(appointments!=null){
            for(Appointment appointment : appointments){
    %>
    <tr>
        <td><%=appointment.getId()%></td>
        <td><%= DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(appointment.getDate()) %></td>
        <td><%=appointment.getClient().getLastName()+" "+appointment.getClient().getFirstName()%></td>
        <td><%=appointment.getEmployee().getLastName()+" "+appointment.getEmployee().getFirstName()%></td>
        <td>
            <!-- Modify Appointment Button -->
            <a href="appointment-servlet?action=modify&id=<%=appointment.getId()%>">
                <button type="button">Modify</button>
            </a>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="5">No appointments found</td> <!-- Adjust colspan to match number of columns -->
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>
