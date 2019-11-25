package com.gysoft.im.common.core.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录过滤器
 *
 * @author 周宁
 * @Date 2018-08-09 17:07
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        if (!urlMatch(req) && session.getAttribute("userId") == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"msg\": \"用户未登录\",\"code\": " + 302 + ",\"result\":  " + null + "}");
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

    /**
     * 判断url是否需要经过登录验证
     *
     * @param request
     * @return
     */
    private boolean urlMatch(HttpServletRequest request) {
        String serviceUrl = request.getRequestURL().toString();
        String queryStr = request.getQueryString();
        if (queryStr != null && queryStr.trim().length() > 0) {
            serviceUrl += "?" + queryStr.trim();
        }
        if (serviceUrl != null) {

            if (serviceUrl.indexOf("?") != -1) {
                serviceUrl = serviceUrl.substring(0, serviceUrl.indexOf("?"));
            }
            serviceUrl = serviceUrl.replace("\\", "/");
            if (serviceUrl.endsWith("/")) {
                serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
                if (serviceUrl.endsWith("/")) {
                    serviceUrl = serviceUrl.substring(0, serviceUrl.length() - 1);
                }
            }
            if (serviceUrl.endsWith(".js")
                    || serviceUrl.endsWith(".css")
                    || serviceUrl.endsWith(".html")
                    || serviceUrl.contains("/swagger/")
                    || serviceUrl.contains("/api-docs")
                    || serviceUrl.contains("/swagger-resources")
                    || serviceUrl.contains("/configuration/ui") ||
                    serviceUrl.contains("/login")) {
                return true;
            }
        }
        return false;
    }
}
