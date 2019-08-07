package com.jeeps.laboratorioutpl.model.access;

import java.util.List;

public class AccessResult {
    private boolean ok;
    private List<AccessWithRoom> accesos;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<AccessWithRoom> getAccesos() {
        return accesos;
    }

    public void setAccesos(List<AccessWithRoom> accesos) {
        this.accesos = accesos;
    }
}
