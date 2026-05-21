package com.ecofeast.filter;
import com.ecofeast.model.User;
import com.ecofeast.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * AuthFilter - Authentication & Role-Based Access Control (RBAC) Filter.
 *
 * This filter intercepts every HTTP request and:
 *  1. Allows public pages (index, login, register, about, contact, faq) without a session.
 *  2. Redirects unauthenticated users to the login page for any protected URL.
 *  3. Enforces role-based access — e.g., donors cannot visit /ngo/*, etc.
 *  4. Redirects authenticated users away from login/register to their dashboard.
 *
 * MVC Role: Security cross-cutting concern (Filter layer).
 * Applied globally via "/*" mapping (configured in web.xml).
 */
public class AuthFilter implements Filter {

    /**
     * Public paths that do NOT require authentication.
     * Requests to these paths bypass session checks.
     */
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/",
            "/index.jsp",
            "/login",
            "/register",
            "/logout",
            "/contact",
            "/views/contact.jsp",
            "/views/login.jsp",
            "/views/register.jsp",
            "/views/error.jsp",
            "/error/403.jsp",
            "/error/404.jsp"
    );

    /**
     * Public path PREFIXES (static resources, error pages, public views).
     */
    private static final List<String> PUBLIC_PREFIXES = Arrays.asList(
            "/css/",
            "/js/",
            "/images/",
            "/error/",
            "/views/about",
            "/views/faq"
    );

    /**
     * Role-to-path prefix mapping for RBAC enforcement.
     * Key = URL prefix, Value = required role name.
     */
    private static final List<String[]> ROLE_MAPPINGS = Arrays.asList(
            new String[]{"/admin",     "ADMIN"},
            new String[]{"/donor",     "DONOR"},
            new String[]{"/ngo",       "NGO"},
            new String[]{"/volunteer", "VOLUNTEER"}
    );

    private static final List<String> AUTH_ENTRY_PATHS = Arrays.asList(
            "/login",
            "/register",
            "/views/login.jsp",
            "/views/register.jsp"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void destroy() {
        // No cleanup required
    }

    /**
     * Core filter logic — called for every incoming HTTP request.
     *
     * @param servletRequest  the incoming request
     * @param servletResponse the outgoing response
     * @param chain           the filter chain to continue if allowed
     * @throws IOException      on I/O error
     * @throws ServletException on servlet error
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextPath = request.getContextPath();
        String requestURI  = request.getRequestURI();

        // Strip context path for simpler matching
        String path = requestURI.substring(contextPath.length());
        User loggedInUser = SessionUtil.getLoggedInUser(request);

        // --------------------------------------------------------
        // 1. Allow static resources and public pages unconditionally
        // --------------------------------------------------------
        if (isPublicPath(path)) {
            if (loggedInUser != null && isAuthEntryPath(path)) {
                response.sendRedirect(contextPath + getDashboardPath(loggedInUser));
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // --------------------------------------------------------
        // 2. Check if user is authenticated
        // --------------------------------------------------------
        if (loggedInUser == null) {
            // Not logged in — redirect to login with the original URL saved
            HttpSession session = request.getSession(true);
            session.setAttribute("redirectAfterLogin", getFullRequestPath(request));
            response.sendRedirect(contextPath + "/login");
            return;
        }

        // --------------------------------------------------------
        // 3. Authenticated — check role-based access
        // --------------------------------------------------------
        String userRole = getRoleName(loggedInUser);

        for (String[] mapping : ROLE_MAPPINGS) {
            String prefix       = mapping[0];
            String requiredRole = mapping[1];

            if (path.startsWith(prefix)) {
                // Admin can access everything
                if ("ADMIN".equals(userRole)) break;

                if (!requiredRole.equals(userRole)) {
                    // Wrong role — redirect to unauthorized page
                    response.sendRedirect(contextPath + "/error/403.jsp");
                    return;
                }
                break;
            }
        }

        // --------------------------------------------------------
        // 4. Approved users only (account_status check)
        // --------------------------------------------------------
        String status = normalize(loggedInUser.getAccountStatus());
        if (status != null && ("PENDING".equals(status) || "REJECTED".equals(status))) {
            // Only allow logout for pending/rejected accounts
            if (!path.equals("/logout")) {
                request.setAttribute("accountStatus", status);
                request.getRequestDispatcher("/error/account-pending.jsp").forward(request, response);
                return;
            }
        }

        // --------------------------------------------------------
        // 5. All checks passed — continue request
        // --------------------------------------------------------
        chain.doFilter(request, response);
    }

    /**
     * Checks if the request path is publicly accessible without authentication.
     *
     * @param path the request path (without context path)
     * @return true if path is public
     */
    private boolean isPublicPath(String path) {
        // Exact match
        if (PUBLIC_PATHS.contains(path)) return true;

        // Prefix match (static resources, error pages)
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }

        // Empty or root path
        if (path.isEmpty()) return true;

        return false;
    }

    private boolean isAuthEntryPath(String path) {
        return AUTH_ENTRY_PATHS.contains(path);
    }

    private String getFullRequestPath(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString == null || queryString.isBlank()) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + queryString;
    }

    private String getRoleName(User user) {
        if (user == null || user.getRole() == null) {
            return "";
        }
        return normalize(user.getRole().getRoleName());
    }

    private String getDashboardPath(User user) {
        String st = user.getAccountStatus() == null ? "" : user.getAccountStatus().trim().toUpperCase();
        if ("PENDING".equals(st)) {
            return "/error/account-pending.jsp?status=PENDING";
        }
        if ("REJECTED".equals(st)) {
            return "/error/account-pending.jsp?status=REJECTED";
        }
        switch (getRoleName(user)) {
            case "ADMIN":
                return "/admin?action=dashboard";
            case "DONOR":
                return "/donor?action=dashboard";
            case "NGO":
                return "/ngo?action=dashboard";
            case "VOLUNTEER":
                return "/volunteer?action=dashboard";
            default:
                return "/";
        }
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
