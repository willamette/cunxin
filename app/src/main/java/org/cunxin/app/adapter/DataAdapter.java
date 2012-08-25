package org.cunxin.app.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataAdapter extends AbstractAdapter {

    @Override
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected String registeredUri() {
        return "data";
    }
}
