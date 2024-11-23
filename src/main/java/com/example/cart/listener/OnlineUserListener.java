package com.example.cart.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

public class OnlineUserListener implements HttpSessionListener {
    private static final AtomicInteger onlineUsers = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        onlineUsers.incrementAndGet();
        System.out.println("New session created. Online users: " + onlineUsers.get());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        onlineUsers.decrementAndGet();
        System.out.println("Session destroyed. Online users: " + onlineUsers.get());
    }

    public static int getOnlineUserCount() {
        return onlineUsers.get();
    }
}
