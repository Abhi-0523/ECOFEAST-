<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">🌱 EcoFeast</div>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <div style="text-align: center; padding: 4rem 0;">
            <h1 style="font-size: 3rem; color: #e74c3c; margin-bottom: 1rem;">⚠️ Error</h1>
            <h2 style="color: #2c3e50; margin-bottom: 1rem;">Something Went Wrong</h2>
            
            <% 
                Integer errorCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
                String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
                Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
                
                if (errorCode == null) {
                    errorCode = response.getStatus();
                }
            %>

            <p style="font-size: 1.1rem; margin-bottom: 1.5rem;">
                <% if (errorCode != null) { %>
                    <strong>Error Code: <%= errorCode %></strong><br>
                <% } %>
                <% if (errorMessage != null) { %>
                    <%= errorMessage %><br>
                <% } %>
            </p>

            <% if (errorCode != null) { %>
                <% if (errorCode == 404) { %>
                    <p>The page you are looking for could not be found.</p>
                <% } else if (errorCode == 500) { %>
                    <p>An internal server error occurred. Please try again later.</p>
                <% } else if (errorCode == 403) { %>
                    <p>You do not have permission to access this resource.</p>
                <% } else { %>
                    <p>Please check your request and try again.</p>
                <% } %>
            <% } %>

            <div style="margin-top: 2rem;">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home Page</a>
                <a href="javascript:history.back()" class="btn btn-secondary">Go Back</a>
            </div>

            <% if (throwable != null) { %>
                <div style="margin-top: 2rem; text-align: left; background: #ecf0f1; padding: 1rem; border-radius: 4px; max-width: 600px; margin-left: auto; margin-right: auto;">
                    <h3 style="color: #e74c3c;">Exception Details (Development Only):</h3>
                    <pre style="overflow-x: auto; white-space: pre-wrap;"><%
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        throwable.printStackTrace(pw);
                        out.print(sw.toString());
                    %></pre>
                </div>
            <% } %>
        </div>
    </div>

    <footer>
        <p>&copy; 2024 EcoFeast - Sustainable Food Redistribution System. All rights reserved.</p>
    </footer>
</body>
</html>
