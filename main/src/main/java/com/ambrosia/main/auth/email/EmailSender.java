package com.ambrosia.main.auth.email;

public interface EmailSender {
    void send(String to, String email);
}
