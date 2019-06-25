package com.jeeps.laboratorioutpl.model.user;

import java.util.List;

public class UserResult {
    private Boolean ok;
    private List<User> usuarios = null;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }
}
