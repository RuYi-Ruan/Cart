package com.example.cart.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;

@WebServlet("/assets/*")
public class StaticResourceServlet extends HttpServlet {

    // 处理静态资源请求
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取请求路径
        String requestedPath = request.getRequestURI().substring(request.getContextPath().length());
        // 路径处理，去掉 /assets
        if (requestedPath.startsWith("/assets")) {
            requestedPath = requestedPath.substring("/assets".length());
        }

        // 设置文件所在的目录
        String assetsDir = getServletContext().getRealPath("/assets");

        // 拼接完整的文件路径
        Path filePath = Paths.get(assetsDir, requestedPath);

        // 检查文件是否存在
        if (Files.exists(filePath)) {
            // 根据文件类型设置响应头
            String mimeType = getServletContext().getMimeType(filePath.toString());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 默认类型
            }
            response.setContentType(mimeType);
            response.setContentLengthLong(Files.size(filePath));

            // 写文件到响应流
            try (InputStream inputStream = Files.newInputStream(filePath);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            // 如果文件不存在，返回 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
