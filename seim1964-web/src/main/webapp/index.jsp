<%@page import="edu.bbte.idde.seim1964.backend.model.Announcement"%>
<%@page import="java.util.Collection"%>
<!DOCTYPE html>
<html>
<head>
    <link href="index.css" rel="stylesheet" type="text/css">
    <title>Homepage</title>
</head>
<body>

<%
Collection<Announcement> announcements = (Collection<Announcement>)request.getAttribute("announcement");
%>

<h1>Announcement list:</h1>

<% for(Announcement annV:announcements){%>
    <div><h2><%= annV.getTitle() %></h2>
        <p id="p1">Posted by: <%= annV.getUserId() %></p>
        <p>Car Details:</p>
        <p>Brand: <%= annV.getCar().getBrand() %></p>
        <p>Model: <%= annV.getCar().getModel() %></p>
        <p>Price: <%= annV.getCar().getPrice() %></p>
        <p>Type: <%= annV.getCar().getType() %></p>
        <p>Color: <%= annV.getCar().getColor() %></p>
        <p>Description: <%= annV.getDescription() %></p>
        <p id="small"><%= annV.getDate() %></p></div>
<%}%>

    <p>
      <a id="big" href="/seim1964-web/logout">Log Out</a>
    </p>
</body>
</html>
