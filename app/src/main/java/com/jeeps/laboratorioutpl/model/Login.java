package com.jeeps.laboratorioutpl.model;

import com.jeeps.laboratorioutpl.model.user.User;

public class Login {
    private boolean ok;
    private User usuario;
    private String token;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
