<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Request Food - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- NGO Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/ngo?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=browse" class="active">Browse Food</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requests">My Requests</a>
                    <a href="${pageContext.request.contextPath}/ngo?action=requestHistory">History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card" style="max-width: 600px; margin: 0 auto;">
                    <h2>Request Food: ${donation.foodName}</h2>
                    
                    <div style="background: var(--light-bg); padding: 1.5rem; border-radius: 8px; margin-bottom: 1.5rem;">
                        <h4 style="margin-bottom: 10px; color: var(--text-color);">Donation Details</h4>
                        <p><strong>Donor:</strong> ${donation.donorName}</p>
                        <p><strong>Available Quantity:</strong> ${donation.quantity} ${donation.quantityUnit}</p>
                        <p><strong>Expiry Time:</strong> ${donation.expiryTime}</p>
                        <p><strong>Location:</strong> ${donation.pickupLocation}, ${donation.pickupCity}</p>
                        <p><strong>Description:</strong> ${donation.description}</p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert alert-error">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/ngo" method="post">
                        <input type="hidden" name="action" value="submitRequest">
                        <input type="hidden" name="donationId" value="${donation.donationId}">
                        
                        <div class="form-group">
                            <label>Quantity to Request (${donation.quantityUnit})</label>
                            <input type="number" name="quantity" min="1" max="${donation.quantity}" required placeholder="Max available: ${donation.quantity}">
                        </div>
                        
                        <div class="form-group">
                            <label>Message to Donor (Optional)</label>
                            <textarea name="message" placeholder="State why you need this food or any pickup details..." style="min-height: 100px;"></textarea>
                        </div>
                        
                        <div style="display: flex; gap: 1rem; margin-top: 1rem;">
                            <button type="submit" class="btn btn-primary" style="flex: 1;">Submit Request</button>
                            <a href="${pageContext.request.contextPath}/ngo?action=browse" class="btn btn-secondary" style="flex: 1; text-align: center;">Cancel</a>
                        </div>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
