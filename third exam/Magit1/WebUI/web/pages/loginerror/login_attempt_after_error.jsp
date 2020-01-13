<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@page import="chat.utils.*" %>
<%@ page import="chat.constants.Constants" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>M.A.Git</title>
    <style>
        body {
            font-family: Arial;
            background-color:  #CFEAF1;
            color: black;
            margin: 0;
            padding: 14px 20px;
            text-align: center;
        }
        .container h1 {
            font-size: 40px;
            font-weight: bold;
            font-family: Lemon;
            color: black;
        }
    </style>
    <!-- Link the Bootstrap (from twitter) CSS framework in order to use its classes-->
    <link rel="stylesheet" href="../../common/bootstrap.min.css">

    <!-- Link jQuery JavaScript library in order to use the $ (jQuery) method-->
    <!-- <script src="common/jquery-2.0.3.min.js"></script>-->
    <!-- and\or any other scripts you might need to operate the JSP file behind the scene once it arrives to the client-->
</head>
<body>
<div class="container">
    <% String usernameFromSession = SessionUtils.getUsername(request);%>
    <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>
    <% if (usernameFromSession == null) {%>
    <h1>Welcome to M.A.Git</h1>
    <br/>
    <h2>Please enter a unique user name:</h2>
    <form method="GET" action="Login">
        <input type="text" name="<%=Constants.USERNAME%>" value="<%=usernameFromParameter%>"/>
        <input type="submit" value="Login"/>
    </form>
    <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>
    <% if (errorMessage != null) {%>
    <span class="bg-danger" style="color:red;"><%=errorMessage%></span>
    <% } %>
    <% } else {%>
    <h1>Welcome back, <%=usernameFromSession%></h1>
    <a href="../MAGitHubAccount/MAGitHubAccount.jsp">Click here to enter M.A.Git</a>
    <br/>
    <a href="login?logout=true" id="logout">logout</a>
    <% }%>
</div>
<img src="./picLogin.png" />
</body>
</html>
