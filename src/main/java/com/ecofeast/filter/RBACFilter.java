package com.ecofeast.filter;

import com.ecofeast.util.SessionUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Legacy role-based access filter.
 *
 * AuthFilter is the active filter registered in web.xml. This class is kept
 * consistent in case it is mapped manually by a servlet container config.
 */
public class RBACFilter implements Filter {
    private static final List<String[]> ROLE_MAPPINGS = Arrays.asList(
            new String[]{"/admin",     "ADMIN"},
            new String[]{"/donor",     "DONOR"},
            new String[]{"/ngo",       "NGO"},
            new String[]{"/volunteer", "VOLUNTEER"}
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String contextPath = httpRequest.getContextPath();
        String path = httpRequest.getRequestURI().substring(contextPath.length());
        String role = normalize(SessionUtil.getCurrentRole(httpRequest));

        for (String[] mapping : ROLE_MAPPINGS) {
            String prefix = mapping[0];
            String requiredRole = mapping[1];

            if (path.startsWith(prefix) && !"ADMIN".equals(role) && !requiredRole.equals(role)) {
                httpResponse.sendRedirect(contextPath + "/error/403.jsp");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}
