package com.example.cart.controller;

import java.io.*;

import com.example.cart.thymeleaf.ThymeleafBaseServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/LoginView")
public class LoginProcess extends ThymeleafBaseServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.processTemplate("login", request, response);
    }
}