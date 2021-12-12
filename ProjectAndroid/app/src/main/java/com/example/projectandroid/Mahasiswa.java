package com.example.projectandroid;

public class Mahasiswa {
    public String Username;
    public String Password;
    public String banned;

    public Mahasiswa(String username, String password, String banned) {
        Username = username;
        Password = password;
        this.banned = banned;

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
