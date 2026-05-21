<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unauthorized - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-container {
            text-align: center;
            padding: 5rem 20px;
            max-width: 600px;
            margin: 0 auto;
        }
        .error-container h1 { font-size: 4rem; color: var(--error-color); margin-bottom: 1rem; }
        .error-container p { font-size: 1.2rem; margin-bottom: 2rem; color: #555; }
    </style>
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container error-container">
        <h1>403</h1>
        <h2>Access Denied</h2>
        <p>You do not have permission to access this page.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Return Home</a>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
