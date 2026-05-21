<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - EcoFeast</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 4rem auto;
            background: #fff;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body style="padding-top: 8rem; background-color: var(--bg-soft);">
    <jsp:include page="/components/navbar.jsp" />

    <div class="container" style="display: flex; justify-content: center; padding-bottom: 4rem;">
        <div class="food-card" style="max-width: 650px; width: 100%; padding: 3rem;">
            <div style="text-align: center; margin-bottom: 2.5rem;">
                <h2 style="font-size: 2.5rem; font-weight: 800; margin-bottom: 0.5rem;">Join EcoFeast</h2>
                <p style="color: var(--text-gray);">Create your account to start making a difference</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error" style="margin-bottom: 1.5rem;">
                    ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST">
                <div class="form-group" style="margin-bottom: 1.5rem;">
                    <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Register as</label>
                    <select name="role" required style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; appearance: none; font-family: inherit;">
                        <option value="">-- Select Role --</option>
                        <option value="DONOR">Food Donor (Restaurant, Hotel)</option>
                        <option value="NGO">NGO / Charity Organization</option>
                        <option value="VOLUNTEER">Volunteer (Transporter)</option>
                    </select>
                </div>

                <div class="form-group" style="margin-bottom: 1.5rem;">
                    <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Full Name</label>
                    <input type="text" name="fullName" required placeholder="John Doe" 
                           style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                </div>

                <div style="display: flex; gap: 1rem; margin-bottom: 1.5rem;">
                    <div style="flex: 1;">
                        <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Email</label>
                        <input type="email" name="email" required placeholder="john@example.com" 
                               style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                    </div>
                    <div style="flex: 1;">
                        <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Phone</label>
                        <input type="text" name="phone" required placeholder="1234567890" 
                               style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                    </div>
                </div>

                <div style="display: flex; gap: 1rem; margin-bottom: 1.5rem;">
                    <div style="flex: 1;">
                        <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Organization</label>
                        <input type="text" name="organization" placeholder="e.g. Green Valley" 
                               style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                    </div>
                    <div style="flex: 1;">
                        <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">City</label>
                        <input type="text" name="city" required placeholder="Kathmandu" 
                               style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                    </div>
                </div>

                <div class="form-group" style="margin-bottom: 2rem;">
                    <label style="font-weight: 600; font-size: 0.9rem; margin-bottom: 0.5rem; display: block;">Password</label>
                    <input type="password" name="password" required minlength="6" placeholder="Min. 6 characters" 
                           style="width: 100%; padding: 1rem; border-radius: 15px; border: 1px solid #eee; background: #fff; font-family: inherit;">
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%;">Register</button>
            </form>

            <p style="text-align: center; margin-top: 2rem; font-size: 0.95rem; color: var(--text-gray);">
                Already have an account? <a href="${pageContext.request.contextPath}/login" style="color: var(--primary-color); text-decoration: none; font-weight: 700;">Sign In</a>
            </p>
        </div>
    </div>

    <jsp:include page="/components/footer.jsp" />
</body>
</html>
