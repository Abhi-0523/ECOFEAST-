<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .profile-card {
            max-width: 640px;
            margin: 2rem auto;
            background: #fff;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.08);
        }
        .profile-card h1 { font-size: 1.75rem; font-weight: 800; margin-bottom: 0.25rem; }
        .profile-card .subtitle { color: var(--text-gray); margin-bottom: 1.5rem; }
        .form-row { margin-bottom: 1.25rem; }
        .form-row label { display: block; font-weight: 600; font-size: 0.9rem; margin-bottom: 0.4rem; }
        .form-row input {
            width: 100%;
            padding: 0.85rem 1rem;
            border-radius: 12px;
            border: 1px solid #e5e5e5;
            font-family: inherit;
            box-sizing: border-box;
        }
        .form-actions { margin-top: 1.5rem; display: flex; gap: 1rem; align-items: center; }
    </style>
</head>
<body style="padding-top: 6rem; background-color: var(--bg-soft); min-height: 100vh;">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container" style="padding-bottom: 4rem;">
        <div class="profile-card">
            <h1>Profile</h1>
            <p class="subtitle">Update your account details</p>

            <c:if test="${not empty error}">
                <div class="alert alert-error" style="margin-bottom: 1rem;">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success" style="margin-bottom: 1rem;">${success}</div>
            </c:if>

            <c:set var="u" value="${sessionScope.loggedInUser}" />

            <div id="clientError" class="alert alert-error" style="display:none; margin-bottom: 1rem;"></div>

            <form action="${pageContext.request.contextPath}/profile" method="POST"
                  onsubmit="return validateProfileForm(this);">
                <div class="form-row">
                    <label for="fullName">Name <span style="color:#e74c3c;">*</span></label>
                    <input type="text" id="fullName" name="fullName" required maxlength="100"
                           placeholder="Your full name"
                           value="${not empty fullName ? fullName : u.fullName}">
                </div>

                <div class="form-row">
                    <label>Email</label>
                    <input type="email" value="${u.email}" disabled
                           style="background:#f5f5f5; color:#666;">
                </div>

                <div class="form-row">
                    <label for="phone">Phone <span style="color:#e74c3c;">*</span></label>
                    <input type="text" id="phone" name="phone" required maxlength="20"
                           placeholder="9800000000"
                           value="${not empty phone ? phone : u.phone}">
                </div>

                <div class="form-row">
                    <label for="organization">Organization</label>
                    <input type="text" id="organization" name="organization" maxlength="150"
                           placeholder="Optional"
                           value="${not empty organization ? organization : u.organization}">
                </div>

                <div class="form-row">
                    <label for="address">Address</label>
                    <input type="text" id="address" name="address" maxlength="255"
                           value="${not empty address ? address : u.address}">
                </div>

                <div style="display:flex; gap:1rem;">
                    <div class="form-row" style="flex:1;">
                        <label for="city">City</label>
                        <input type="text" id="city" name="city" maxlength="80"
                               value="${not empty city ? city : u.city}">
                    </div>
                    <div class="form-row" style="flex:1;">
                        <label for="state">State</label>
                        <input type="text" id="state" name="state" maxlength="80"
                               value="${not empty state ? state : u.state}">
                    </div>
                </div>

                <div class="form-row">
                    <label for="zipCode">ZIP Code</label>
                    <input type="text" id="zipCode" name="zipCode" maxlength="20"
                           value="${not empty zipCode ? zipCode : u.zipCode}">
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/" class="btn" style="text-decoration:none;">Cancel</a>
                </div>
            </form>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />

    <script>
        function validateProfileForm(form) {
            var errorBox = document.getElementById('clientError');
            var name = form.fullName.value.trim();
            var phone = form.phone.value.trim();
            if (!name) {
                errorBox.textContent = 'Name is required.';
                errorBox.style.display = 'block';
                form.fullName.focus();
                return false;
            }
            if (!phone) {
                errorBox.textContent = 'Phone number is required.';
                errorBox.style.display = 'block';
                form.phone.focus();
                return false;
            }
            errorBox.style.display = 'none';
            return true;
        }
    </script>
</body>
</html>
