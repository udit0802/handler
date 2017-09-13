<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HelloWorld page</title>
</head>
<body>
	${title}
	<br />
	<br /> Dear ${user}, you are successfully logged into this application.
	<br />

	<form:form modelAttribute="product" action='save-product' method="post"
		enctype="multipart/form-data">
		<fieldset>
			<legend>Add a product</legend>
			<!-- <p>
                    <label for="name">Product Name: </label>
                    <input id="name" path="name" />
                </p>
                <p>
                    <label for="description">Description: </label>
                    <input id="description" path="description" />
                </p>
                <p>
                    <label for="image">Product Images: </label>
                    <input type="file" name="images" multiple="multiple"/>
                </p> -->
			<form:label path="name">Name</form:label>
			<form:input path="name" />

			<form:label path="description">Description</form:label>
			<form:input path="description" />

			<form:label path="images">Images</form:label>
			<form:input type="file" path="images" multiple="multiple" />
			<p id="buttons">
				<input id="reset" type="reset" tabindex="4"> <input
					id="submit" type="submit" tabindex="5" value="Add Product">
			</p>
		</fieldset>
	</form:form>
	<a href="<c:url value='/logout' />">Logout</a>
</body>
</html>