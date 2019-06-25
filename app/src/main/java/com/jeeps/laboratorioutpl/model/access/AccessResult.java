package com.jeeps.laboratorioutpl.model.access;

import java.util.List;

public class AccessResult {
    private boolean ok;
    private List<AccessWithRoom> permisoDB;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<AccessWithRoom> getPermisoDB() {
        return permisoDB;
    }

    public void setPermisoDB(List<AccessWithRoom> permisoDB) {
        this.permisoDB = permisoDB;
    }
}
