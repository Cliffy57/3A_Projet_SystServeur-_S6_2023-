# Subject : TODO
![img.png](img.png)

## TODO
![img_1.png](img_1.png)

## Notes
Faire Hibernate pour joindre les classes a la db H2, rewrite les classes avec les annotations hibernate

Faire des pages pour test si ca fonctionne

Servlets : Vous aurez besoin d'au moins une servlet pour gérer les demandes HTTP. Cependant, vous pouvez en avoir plusieurs pour gérer différents types d'actions. Par exemple, une servlet pour afficher la liste des rendez-vous, une autre pour ajouter un rendez-vous, une autre pour modifier un rendez-vous, etc. En fonction de la complexité de l'application, vous pouvez vous retrouver avec entre 3 et 8 servlets.

JSP : Vous aurez besoin d'au moins une JSP pour afficher le contenu de l'application. Cependant, vous pouvez en avoir plusieurs pour afficher différents types d'informations. Par exemple, une JSP pour afficher la liste des rendez-vous, une autre pour ajouter un rendez-vous, une autre pour modifier un rendez-vous, etc. En fonction de la complexité de l'application, vous pouvez vous retrouver avec entre 3 et 8 JSP.

## PROBLEMES

c'est ca qui créer l'erreur : <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> dans les jsp l'usage 
de jstl provoque une erreur : Type Rapport d'exception

message java.lang.NoClassDefFoundError: javax/servlet/jsp/tagext/TagLibraryValidator

description Le serveur a rencontré une erreur interne qui l'a empêché de satisfaire la requête.

exception jakarta.servlet.ServletException: java.lang.NoClassDefFoundError: javax/servlet/jsp/tagext/TagLibraryValidator
org.apache.jasper.servlet.JspServlet.service(JspServlet.java:333)
jakarta.servlet.http.HttpServlet.service(HttpServlet.java:631)
org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)