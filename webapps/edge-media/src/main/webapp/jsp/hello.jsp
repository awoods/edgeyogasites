<html>
<head>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" />
<title>Sample Application JSP Page</title>
</head>
<body bgcolor=white>

<table border="0">
<tr>
<td align=center>
<img src="images/tomcat.gif">
</td>
<td>
<h1>Sample Application JSP Page</h1>
This is the output of a JSP page that is part of the Hello, World
application.
</td>
</tr>
</table>

<%= new String("Hello!") %>
<p>
<%= System.getProperty("java.version") %>
</p><p>
<%= System.getProperty("java.class.path") %>
</p>
<%= System.getProperty("java.library.path") %>
</p>
<%= System.getProperty("catalina.base") %>

</body>
</html>
