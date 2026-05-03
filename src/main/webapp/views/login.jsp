<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a class="logo" href="${pageContext.request.contextPath}/">EcoFeast</a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <div class="form-container">
            <div class="form-header">
                <h2>User Login</h2>
            </div>

            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
            %>
            <% if (error != null) { %>
                <div class="alert alert-error">
                    <strong>Error:</strong> <%= error %>
                </div>
            <% } %>
            <% if (success != null) { %>
                <div class="alert alert-success">
                    <strong>Success:</strong> <%= success %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="POST">
                <div class="form-group">
                    <label for="email">Email Address:</label>
                    <input type="email" id="email" name="email" required placeholder="Enter your email" class="form-control">
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required placeholder="Enter your password" class="form-control">
                </div>

                <div class="form-group">
                    <input type="submit" value="Login" class="btn btn-primary">
                </div>
            </form>

            <p class="form-footer">
                Don't have an account? <a href="${pageContext.request.contextPath}/register" class="text-link">Register here</a>
            </p>

            <p class="demo-hint">
                <strong>Demo credentials:</strong><br>
                Admin: admin@ecofeast.com / admin123<br>
                User: abhibasnet@gmail.com / user123
            </p>
        </div>
    </div>

    <footer>
        <p>&copy; 2026 EcoFeast - Sustainable Food Redistribution. All rights reserved.</p>
    </footer>
</body>
</html>
