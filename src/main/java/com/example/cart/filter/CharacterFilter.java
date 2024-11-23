package com.example.cart.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebFilter("/*")
public class CharacterFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 统一设置请求和响应的编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 对 request 进行包装
        CharacterRequest characterRequest = new CharacterRequest((HttpServletRequest) request);
        filterChain.doFilter(characterRequest, response); // 使用包装后的 request 对象
    }

    public void destroy() {}
}

// 继承默认包装类 HttpServletRequestWrapper
class CharacterRequest extends HttpServletRequestWrapper {
    public CharacterRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        // 调用被包装对象的 getParameter() 方法，获得请求参数
        String value = super.getParameter(name);
        if (value == null) {
            return null;
        }

        // 判断请求方式，避免不必要的转换
        String method = super.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            try {
                // 仅当默认编码为 ISO-8859-1 时才进行转换
                if (!"UTF-8".equalsIgnoreCase(super.getCharacterEncoding())) {
                    value = new String(value.getBytes("iso-8859-1"), "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported Encoding", e);
            }
        }
        // 返回处理后的结果
        return value;
    }
}
