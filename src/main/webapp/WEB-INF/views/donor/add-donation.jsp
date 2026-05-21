<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty donation ? 'Edit' : 'Add'} Food Donation - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body style="display: flex; flex-direction: column; min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container">
        <div class="dashboard">
            <!-- Donor Sidebar -->
            <aside class="sidebar">
                <div class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/donor?action=dashboard">Dashboard Overview</a>
                    <a href="${pageContext.request.contextPath}/donor?action=addDonation" class="${empty donation ? 'active' : ''}">List New Food</a>
                    <a href="${pageContext.request.contextPath}/donor?action=manageDonations" class="${not empty donation ? 'active' : ''}">Manage Donations</a>
                    <a href="${pageContext.request.contextPath}/donor?action=requests">Incoming Requests</a>
                    <a href="${pageContext.request.contextPath}/donor?action=donationHistory">History</a>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="main-content">
                <div class="card" style="max-width: 700px; margin: 0 auto;">
                    <h2>${not empty donation ? 'Edit' : 'List New'} Food Donation</h2>
                    
                    <c:if test="${not empty error}">
                        <div class="alert alert-error">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/donor" method="post">
                        <input type="hidden" name="action" value="${not empty donation ? 'updateDonation' : 'submitDonation'}">
                        <c:if test="${not empty donation}">
                            <input type="hidden" name="donationId" value="${donation.donationId}">
                        </c:if>

                        <div class="form-group">
                            <label>Food Name / Title</label>
                            <input type="text" name="foodName" value="${donation.foodName}" required placeholder="e.g., 50 boxes of Rice and Curry">
                        </div>

                        <div class="form-group">
                            <label>Category</label>
                            <select name="categoryId" required>
                                <option value="">-- Select Category --</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.categoryId}" ${donation.categoryId == cat.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div style="display: flex; gap: 1rem;">
                            <div class="form-group" style="flex: 2;">
                                <label>Quantity</label>
                                <input type="number" name="quantity" value="${donation.quantity}" required min="1">
                            </div>
                            <div class="form-group" style="flex: 1;">
                                <label>Unit</label>
                                <select name="quantityUnit" required>
                                    <option value="kg" ${donation.quantityUnit == 'kg' ? 'selected' : ''}>kg</option>
                                    <option value="litres" ${donation.quantityUnit == 'litres' ? 'selected' : ''}>litres</option>
                                    <option value="pieces" ${donation.quantityUnit == 'pieces' ? 'selected' : ''}>pieces</option>
                                    <option value="boxes" ${donation.quantityUnit == 'boxes' ? 'selected' : ''}>boxes</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Expiry Date & Time</label>
                            <input type="datetime-local" name="expiryTime" value="${not empty donation ? donation.expiryTime : ''}" required>
                        </div>

                        <div style="display: flex; gap: 1rem;">
                            <div class="form-group" style="flex: 2;">
                                <label>Pickup Location Details</label>
                                <input type="text" name="pickupLocation" value="${donation.pickupLocation}" required placeholder="e.g., Back door of Green Restaurant, Main St.">
                            </div>
                            <div class="form-group" style="flex: 1;">
                                <label>City</label>
                                <input type="text" name="pickupCity" value="${donation.pickupCity}" required placeholder="e.g., Kathmandu">
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Description & Notes</label>
                            <textarea name="description" placeholder="Any specific handling instructions or details about the food...">${donation.description}</textarea>
                        </div>
                        
                        <div class="form-group">
                            <label>Image URL (Optional)</label>
                            <input type="text" name="imageUrl" value="${donation.imageUrl}" placeholder="https://example.com/image.jpg">
                        </div>

                        <button type="submit" class="btn btn-primary" style="width: 100%;">${not empty donation ? 'Update Donation' : 'List Food'}</button>
                    </form>
                </div>
            </main>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
