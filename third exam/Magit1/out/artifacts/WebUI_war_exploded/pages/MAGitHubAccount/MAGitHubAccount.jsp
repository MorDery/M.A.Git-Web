<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MAGitHubAccount</title>
    <script src="../../common/jquery-2.0.3.min.js"></script>
    <script src="../../common/context-path-helper.js"></script>
    <script src="chatroom.js"></script>
    <script src ="MAGitHubAccount.js"></script>

    <link rel="stylesheet" href="../../common/bootstrap.min.css">
    <link rel="stylesheet" href="chatroom.css">

    <style>
        * {
            box-sizing: border-box;
        }

        .UploadXML
        {
            /*font-weight: bold;*/
            font-size: 20px;
            color:whitesmoke;
        }
        #notificationList {
            overflow-y: scroll;
            max-height: 100px;
        }

        /* Style the body */
        body {
            font-family: Lemon;
            background-color:  #CFEAF1;
            margin: 0;
        }

        /* Header/logo Title */
        .header {
            padding: 40px;
            text-align: center;
            background: #1A9CBC;
            color: whitesmoke;
        }

        /* Increase the font size of the heading */
        .header h1 {
            font-size: 50px;
            font-weight: bold;
        }

        /* Sticky navbar - toggles between relative and fixed, depending on the scroll position. It is positioned relative until a given offset position is met in the viewport - then it "sticks" in place (like position:fixed). The sticky value is not supported in IE or Edge 15 and earlier versions. However, for these versions the navbar will inherit default position */
        .navbar {
            overflow: hidden;
            padding: 10px;
            background-color: #116478;
            position: sticky;
            position: -webkit-sticky;
            top: 0;
        }

        /* Style the navigation bar links */
        .navbar a {
            background-color: #CFEAF1;
            float: left;
            display: block;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
        }


        /* Right-aligned link */
        .navbar a.right {

            float: right;
        }

        /* Change color on hover */
        .navbar a:hover {
            color: black;
        }

        /* Active/current link */
        .navbar a.active {
            background-color: #CFEAF1;
            color: white;
        }


        /* Create two unequal columns that sits next to each other */
        /* Sidebar/left column */
        .side {
            -ms-flex: 30%; /* IE10 */
            flex: 30%;
            background-color: #f1f1f1;
            padding: 20px;
        }

        /* Main column */
        .main {

            -ms-flex: 70%; /* IE10 */
            flex: 70%;
            background-color: #CFEAF1;
            padding: 20px;
        }
        .main h2{
            font-size: 20px;
        }
        .repositoryTable {
            border: 1px black solid;
            border-radius: 6px;
            width: 100%;
        }
        .otherUserRepositoryTable {
            border: 1px black solid;
            border-radius: 6px;
            width: 100%;
        }

        /* Chat */
        .Chat {
            padding: 20px;
            text-align: center;
            background: #ddd;
        }

        /* Responsive layout - when the screen is less than 700px wide, make the two columns stack on top of each other instead of next to each other */
        @media screen and (max-width: 700px) {
            .row {
                flex-direction: column;
                border: 1px black solid;
                border-radius: 6px;
                width: 100%;
            }
        }

        /* Responsive layout - when the screen is less than 400px wide, make the navigation links stack on top of each other instead of next to each other */
        @media screen and (max-width: 400px) {
            .navbar a {
                float: none;
                width: 100%;
            }
        }
        .teamSelector
        {
            cursor: pointer
        }
    </style>


</head>
<body>

<div class="header">
    <h1>My Account</h1>
</div>

<div class="navbar">
    <div class="repositoriesActions">
        <span class="UploadXML">Load repository from XML:</span>
        <input id="fileInput" type="file" class="fileInput" accept="xml" onchange="loadRepositoryClicked(event)" value=""/>
    </div>
</div>

<div class="main">
    <h2>My repositories</h2>
    <table class="repositoryTable">
        <thead>
        <th>Repository name</th>
        <th>Active branch name</th>
        <th>Number of branches</th>
        <th>Last commit date</th>
        <th>Last commit message</th>
        </thead>
        <tbody class="tableBody">
        </tbody>
    </table>

    <h2>Users with repositories</h2>
    <div class = "otherUsers">
        <div class="otherUsersNames">
            <ul class="otherUsersNamesList">

            </ul>
        </div>
    </div>
    <div class="selectedUserRepository">
        <table class = "otherUserRepositoryTable" >
            <thead class = "otherUserRepositoryThead">
            </thead>
            <tbody class="otherUserRepositorytableBody">
            </tbody>
        </table>
    </div>

    <div id="notificationArea">
        <h2>Notifications</h2>
        <div id="notificationList" class="span6">

        </div>
    </div>

</div>
</div>

<div class="Chat">
    <div class="side">
        <h2>Online chat</h2>
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-1">
                    <div class="sidebar">
                        <h4>Users</h4>
                        <div class="clearfix">
                            <div class="input">
                                <ul id="userslist">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="content">
                        <div id="chatwindow">
                            <h4>Chat</h4>
                            <div id="chatarea" class="span6"></div>
                            <form id="chatform" method="POST" action="sendChat">
                                <input type="text" id="userstring" name="userstring"/>
                                <input type="submit" value="Send"/>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="col-xs-1">
                    <div class="button">
                        <!--
                        Here we could give chat/logout, and the executed request was relative to this page url (=== <context path>/pages/chatroom/),
                        in this option it would have come out eventually with '<context path>/pages/chatroom/chat/logout'
                        (note that this option is the one used with the form above for the action of 'sendChat'...)

                        Another option, just to prove that the browser takes everything relative to the current page (<context path>/pages/chatroom/),
                        is to use relative path here.
                        So '../../chat/logout' steps backward 2 levels from current page (putting it in the root web app, right after the context path: '/<context path>/')
                        and from there assembles the rest, so we end up with <context path>/chat/logout.
                        (when this request will arrive to tomcat it will strip down the context path and will expect to find a registered servlet with the mapping of /chat/logout)
                        -->
                        <a href="../../logout">Logout</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
