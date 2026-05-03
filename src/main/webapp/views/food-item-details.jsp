<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ecofeast.models.User" %>
<%@ page import="com.ecofeast.models.FoodItem" %>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    FoodItem foodItem = (FoodItem) request.getAttribute("foodItem");
    if (foodItem == null) {
        response.sendRedirect(request.getContextPath() + "/user/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= foodItem.getItemName() %> - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="logo">EcoFeast</div>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/user/dashboard">Portal</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <div class="container" style="max-width: 640px; margin: 2rem auto;">
        <div class="card" style="padding: 1.5rem;">
            <h1><%= foodItem.getItemName() %></h1>
            <p><strong>Category:</strong> <%= foodItem.getCategory() != null ? foodItem.getCategory() : "-" %></p>
            <p><strong>Quantity available:</strong> <%= foodItem.getQuantity() %></p>
            <p><strong>Expiry:</strong> <%= foodItem.getExpiryDate() != null ? foodItem.getExpiryDate().toString() : "-" %></p>
            <p><strong>Location:</strong> <%= foodItem.getLocation() != null ? foodItem.getLocation() : "-" %></p>
            <p><strong>Status:</strong> <%= foodItem.getStatus() != null ? foodItem.getStatus() : "-" %></p>
            <% if (foodItem.getDescription() != null && !foodItem.getDescription().isEmpty()) { %>
                <p><strong>Description:</strong> <%= foodItem.getDescription() %></p>
            <% } %>

            <form method="post" action="${pageContext.request.contextPath}/user/dashboard" style="margin-top: 1.5rem;">
                <input type="hidden" name="action" value="requestFood">
                <input type="hidden" name="itemId" value="<%= foodItem.getItemId() %>">
                <div class="form-group">
                    <label for="quantity">Request quantity</label>
                    <input type="number" id="quantity" name="quantity" min="1" max="<%= foodItem.getQuantity() %>" value="1" required>
                </div>
                <button type="submit" class="btn btn-primary">Request this item</button>
            </form>

            <p style="margin-top: 1rem;"><a href="${pageContext.request.contextPath}/user/dashboard">Back to portal</a></p>
        </div>
    </div>
</body>
</html>
