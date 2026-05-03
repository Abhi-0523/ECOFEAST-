package com.ecofeast.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * RBACFilter - Role-Based Access Control Filter
 * Enforces authorization rules based on user roles.
 * Redirects unauthenticated users to login and prevents unauthorized access.
 * This filter implements RBAC for the EcoFeast system.
 */
public class RBACFilter implements Filter {

    /**
     * Initializes the filter.
     * @param config the filter configuration
     * @throws ServletException if initialization fails
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        // Filter initialization code (currently empty)
    }

    /**
     * Main filter logic - checks user authentication and authorization.
     * Routes requests based on user role and requested resource.
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Don't create new session
        
        // Get the requested URI
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String requestPath = requestURI.substring(contextPath.length());
        
        // Define public URLs that don't require authentication
        if (isPublicURL(requestPath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            // User is not logged in - redirect to login page
            httpResponse.sendRedirect(contextPath + "/views/login.jsp");
            return;
        }
        
        // User is logged in - get user role from session
        String userRole = (String) session.getAttribute("userRole");
        
        // Check authorization based on URL path
        if (!isAuthorized(requestPath, userRole)) {
            // User doesn't have permission to access this resource
            httpResponse.sendRedirect(contextPath + "/views/error.jsp?error=unauthorized");
            return;
        }
        
        // User is authenticated and authorized - proceed with request
        chain.doFilter(request, response);
    }

    /**
     * Checks if the requested URL is publicly accessible without login.
     * @param requestPath the request path to check
     * @return true if the path is public, false otherwise
     */
    private boolean isPublicURL(String requestPath) {
        // Public URLs that don't require authentication
        return requestPath.equals("/") ||
               requestPath.equals("/index.jsp") ||
               requestPath.equals("/views/login.jsp") ||
               requestPath.equals("/views/register.jsp") ||
               requestPath.equals("/login") ||
               requestPath.equals("/register") ||
               requestPath.equals("/css/style.css") ||
               requestPath.startsWith("/views/error");
    }

    /**
     * Checks if the user has authorization to access the requested resource.
     * @param requestPath the request path to check
     * @param userRole the user's role (ADMIN or USER)
     * @return true if user is authorized, false otherwise
     */
    private boolean isAuthorized(String requestPath, String userRole) {
        // Admin URLs - only ADMIN role can access
        if (requestPath.startsWith("/admin")) {
            return "ADMIN".equals(userRole);
        }
        
        // User URLs - only USER role can access
        if (requestPath.startsWith("/user")) {
            return "USER".equals(userRole);
        }
        
        // Logout is allowed for both roles
        if (requestPath.equals("/logout")) {
            return true;
        }
        
        // Default: deny access
        return false;
    }

    /**
     * Cleans up filter resources.
     */
    @Override
    public void destroy() {
        // Filter cleanup code (currently empty)
    }
}
