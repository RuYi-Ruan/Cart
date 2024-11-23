package com.example.cart.service;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

@WebServlet("/deleteFile")
public class DeleteUploadFileServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "assets/images/";

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        JSONObject jsonResponse = new JSONObject();
        String fileName = request.getParameter("fileName");

        try {
            if (fileName != null && !fileName.contains("..") && !fileName.contains("/")) {
                File file = new File(getServletContext().getRealPath("") + UPLOAD_DIRECTORY, fileName);
                String canonicalPath = file.getCanonicalPath();
                String uploadDirCanonicalPath = new File(getServletContext().getRealPath("") + UPLOAD_DIRECTORY).getCanonicalPath();

                // 验证文件是否在指定目录中
                if (!canonicalPath.startsWith(uploadDirCanonicalPath)) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "文件路径不合法");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else if (file.exists() && file.delete()) {
                    jsonResponse.put("success", true);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "文件删除失败，可能文件被占用或权限不足");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "文件名非法");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "发生异常: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.getWriter().print(jsonResponse.toString());
    }
}
