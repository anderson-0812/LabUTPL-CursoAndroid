package com.jeeps.laboratorioutpl.model.user;

public class UserPutResult {
    private Boolean ok;
    private User sala = null;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public User getSala() {
        return sala;
    }

    public void setSala(User sala) {
        this.sala = sala;
    }
}
