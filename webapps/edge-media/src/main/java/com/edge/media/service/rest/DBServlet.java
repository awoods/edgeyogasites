package com.edge.media.service.rest;

import com.edge.media.service.util.MemoryDbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Andrew Woods
 *         Date: 2/10/12
 */
public class DBServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            String dump = MemoryDbUtil.getInstance().dump();
            out.print(dump);

        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        MemoryDbUtil.getInstance().clear();
        out.print("cleared");
    }

}
