<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<div id="upload file">
    <form action="servlets.FileUploadHandler" method="post" >
        <input type="file" name="file" />
        <input type = "submit" value = "Upload File" />
    </form>
</div>

</body>
</html>
