<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>HelloWorld page</title>
</head>
<body>
    ${title}<br/><br/>
    Dear ${user}, you are successfully logged into this application as admin.
    <br/>
    
    
    <form method="POST" action="<c:url value='uploadFile' />" enctype="multipart/form-data">
    <table>
        <tr>
            <td><label path="file">Select a file to upload</form:label></td>
            <td><input type="file" name="file" /></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form>

    <a href="<c:url value='/logout' />">Logout</a>
</body>
</html>