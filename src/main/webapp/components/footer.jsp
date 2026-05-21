<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer>
    <div class="container">
        <div class="footer-grid">
            <div class="footer-col">
                <a href="${pageContext.request.contextPath}/" class="logo" style="margin-bottom: 1.5rem; display: block;">🌱 EcoFeast</a>
                <p style="color: var(--text-gray);">Empowering communities through sustainable food redistribution and waste reduction.</p>
            </div>
            <div class="footer-col">
                <h4>Quick Links</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/about.jsp">About Us</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/faq.jsp">FAQs</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>Community</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/register">Join as Donor</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Join as NGO</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Volunteer</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>Connect</h4>
                <div style="display: flex; gap: 1rem; font-size: 1.2rem;">
                    <a href="https://www.facebook.com/login/" target="_blank" style="color: var(--text-gray);">Facebook</a>
                    <a href="https://www.instagram.com/accounts/login/" target="_blank" style="color: var(--text-gray);">Instagram</a>
                    <a href="https://www.tiktok.com/login" target="_blank" style="color: var(--text-gray);">TikTok</a>
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2024 EcoFeast - Sustainable Food Redistribution System. All rights reserved.</p>
        </div>
    </div>
</footer>
