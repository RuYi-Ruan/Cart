package com.example.cart.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@WebServlet("/upload")
@MultipartConfig // 标注此Servlet支持文件上传
public class UploadFileServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "/assets/images"; // 图片存储目录

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        JSONObject jsonResponse = new JSONObject();

        try {
            Part filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                // 检查文件类型是否为图片
                String fileType = filePart.getContentType();
                if (!(fileType.equals("image/jpeg") || fileType.equals("image/png") || fileType.equals("image/jpg"))) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "只能上传图片文件（JPEG或PNG）");
                    response.getWriter().print(jsonResponse.toString());
                    return;
                }

                // 生成唯一文件名，避免中文或特殊字符引起的问题
                String originalFileName = filePart.getSubmittedFileName();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + extension;

                // 获取最终目录路径
                String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIRECTORY;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // 创建最终目录
                }

                File uploadFile = new File(uploadDir, fileName);

                try (InputStream inputStream = filePart.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(uploadFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                // 返回图片URL，避免中文乱码问题
                String imageUrl = request.getScheme() + "://" + request.getServerName() + ":" +
                        request.getServerPort() + request.getContextPath() + UPLOAD_DIRECTORY + "/" + fileName;
                jsonResponse.put("success", true);
                jsonResponse.put("url", imageUrl);
                jsonResponse.put("fileName", fileName);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "未找到文件或文件为空。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "上传文件时发生错误：" + e.getMessage());
        }

        response.getWriter().print(jsonResponse.toString());
    }
}
