/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.megacitycab.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
/**
 * Filter for session-based authentication
 * Protects secured areas of the application
 */
@WebFilter(urlPatterns = {"/admin/*", "/driver/*", "/customer/*"})
public class SessionAuthenticationFilter implements Filter {
    
    // Paths that don't require authentication
    private static final String[] PUBLIC_PATHS = {
        "/login.jsp", 
        "/register.jsp", 
        "/index.jsp", 
        "/css/", 
        "/js/", 
        "/images/"
    };
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Check if the path is public
        String requestPath = httpRequest.getRequestURI();
        if (isPublicPath(requestPath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Get the session and check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = session != null && session.getAttribute("userId") != null;
        
        if (isLoggedIn) {
            // User is logged in, check if they have access to this path
            String userRole = (String) session.getAttribute("userRole");
            
            if (hasAccess(requestPath, userRole)) {
                // User has access, proceed
                chain.doFilter(request, response);
            } else {
                // User doesn't have access to this area
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/unauthorized.jsp");
            }
        } else {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
    
    /**
     * Check if the path is public and doesn't require authentication
     * @param path Request path
     * @return true if public, false otherwise
     */
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.contains(publicPath)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the user role has access to the requested path
     * @param path Request path
     * @param role User role
     * @return true if has access, false otherwise
     */
    private boolean hasAccess(String path, String role) {
        // Admin has access to everything
        if ("admin".equals(role)) {
            return true;
        }
        
        // Driver has access only to driver area
        if ("driver".equals(role) && path.contains("/driver/")) {
            return true;
        }
        
        // Regular user has access only to customer area
        if ("user".equals(role) && path.contains("/customer/")) {
            return true;
        }
        
        return false;
    }
}
