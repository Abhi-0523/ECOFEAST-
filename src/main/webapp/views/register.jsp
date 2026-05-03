<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">🌱 EcoFeast</div>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <div class="form-container">
            <h2 style="text-align: center; color: #2ecc71; margin-bottom: 2rem;">Create Your Account</h2>

            <!-- Error/Success Messages -->
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
                    <strong>Success:</strong> <%= success %> Redirecting to login...
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="POST">
                <div style="display: flex; gap: 1rem;">
                    <div class="form-group" style="flex: 1;">
                        <label for="firstName">First Name:</label>
                        <input type="text" id="firstName" name="firstName" required placeholder="John">
                    </div>
                    <div class="form-group" style="flex: 1;">
                        <label for="lastName">Last Name:</label>
                        <input type="text" id="lastName" name="lastName" required placeholder="Doe">
                    </div>
                </div>

                <div class="form-group">
                    <label for="email">Email Address:</label>
                    <input type="email" id="email" name="email" required placeholder="your@email.com">
                </div>

                <div class="form-group">
                    <label for="phoneNumber">Phone Number:</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" required placeholder="1234567890" pattern="\d{10,15}">
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required placeholder="Min 6 characters" minlength="6">
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Confirm your password" minlength="6">
                </div>

                <div class="form-group">
                    <input type="submit" value="Register" class="btn btn-primary">
                </div>
            </form>

            <p style="text-align: center; margin-top: 1rem;">
                Already have an account? <a href="${pageContext.request.contextPath}/login" style="color: #2ecc71; text-decoration: none; font-weight: 600;">Login here</a>
            </p>
        </div>
    </div>

    <footer>
        <p>&copy; 2024 EcoFeast - Sustainable Food Redistribution System. All rights reserved.</p>
    </footer>
</body>
</html>
