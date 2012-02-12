<%@ taglib uri="http://java.sun.com/jstl/sql_rt" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<sql:query var="rs" dataSource="jdbc/edgeyoga_testids">
select * from useids
</sql:query>

<html>
  <head>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" />
    <title>DB Test</title>
  </head>
  <body>

  <h2>Results</h2>

<c:forEach var="row" items="${rs.rows}">
    Foo ${row.guid}<br/>
    Bar ${row.memid}<br/>
</c:forEach>

  </body>
</html>
