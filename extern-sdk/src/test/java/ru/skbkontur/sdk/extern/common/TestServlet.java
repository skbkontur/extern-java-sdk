package ru.skbkontur.sdk.extern.common;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(ResponseData.INSTANCE.getResponseCode());
        resp.getWriter().print(ResponseData.INSTANCE.getResponseMessage());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(ResponseData.INSTANCE.getResponseCode());
        resp.getWriter().print(ResponseData.INSTANCE.getResponseMessage());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(ResponseData.INSTANCE.getResponseCode());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(ResponseData.INSTANCE.getResponseCode());
    }
}
