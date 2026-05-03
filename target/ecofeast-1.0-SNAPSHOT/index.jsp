<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoFeast - Sustainable Food Redistribution</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">🌱 EcoFeast</div>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
            </ul>
        </div>
    </nav>

    <header class="hero">
        <div class="container">
            <h1>Welcome to EcoFeast</h1>
            <p>Sustainable Food Redistribution System</p>
            <p class="subtitle">Reduce food waste. Support your community. Build a sustainable future.</p>
        </div>
    </header>

    <section class="features">
        <div class="container">
            <h2>How It Works</h2>
            <div class="features-grid">
                <div class="feature-card">
                    <h3>For Businesses & Donors</h3>
                    <p>List surplus food items with details and expiry dates. Help communities access quality food.</p>
                </div>
                <div class="feature-card">
                    <h3>For Users in Need</h3>
                    <p>Browse available food items and submit requests. Collect verified, safe food donations.</p>
                </div>
                <div class="feature-card">
                    <h3>For Administrators</h3>
                    <p>Manage users, approve requests, and track distribution. Ensure system integrity and safety.</p>
                </div>
            </div>
        </div>
    </section>

    <section class="cta">
        <div class="container">
            <h2>Get Started Today</h2>
            <div class="buttons">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Login</a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">Create Account</a>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2024 EcoFeast - Sustainable Food Redistribution System. All rights reserved.</p>
    </footer>
</body>
</html>
