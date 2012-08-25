package org.cunxin.app.adapter;

import org.cunxin.app.servlet.ProcessDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAdapter {

    public AbstractAdapter() {
        this.doRegister();
    }

    protected abstract String registeredUri();

    protected abstract void doProcess(HttpServletRequest request, HttpServletResponse response);

    protected void doRegister() {
        ProcessDispatcher.registerPatchers(this.registeredUri(), this);
    }

}
