package org.cunxin.app.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.cunxin.app.adapter.AbstractAdapter;
import org.cunxin.app.module.CunxinServiceModule;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ProcessDispatcher extends HttpServlet {

    private final static Injector injector = Guice.createInjector(new CunxinServiceModule());
    private final static Map<String, AbstractAdapter> adaptersMap = new HashMap<String, AbstractAdapter>();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

        RequestDispatcher dispatcher = null;
        String param = request.getParameter("dispatcher");
        if (param == null)
            throw new ServletException("Missing parameter in Controller.");

    }

    public static void registerPatchers(String uri, AbstractAdapter abstractAdapter) {
        adaptersMap.put(uri, abstractAdapter);
    }
}
