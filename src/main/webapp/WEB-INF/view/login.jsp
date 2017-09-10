<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>${title}</title>
        <style>
            //removing style,you can see complete css in the attached source.
        </style>
</head>
<body>
<br/><br/><br/><br/><br/><br/><br/>
<div class="login-block">
    <form name='loginForm'
                  action="<c:url value='j_spring_security_check' />" method='POST'>
        <h1>Login</h1>
        <input type="text" id="username" name="username" placeholder="Username"  />
        <input type="password" id="password" name="password" placeholder="Password" />
        <button>Submit</button>
    </form>
</div>
</body>
</html>