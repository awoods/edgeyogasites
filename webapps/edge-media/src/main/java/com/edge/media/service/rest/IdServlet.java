/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package com.edge.media.service.rest;

import com.edge.media.service.util.DbUtil;
import com.edge.media.service.util.SqlDbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * @author Andrew Woods
 *         Date: 1/25/12
 */
public class IdServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            int id = generateAndStoreUniqueId();
            out.print(id);

        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }

    private int generateAndStoreUniqueId() throws Exception {
        // generate new ID
        int id = generateId();

        // verify that ID is unique
        DbUtil dbUtil = new SqlDbUtil();
        while (dbUtil.notUnique(id)) {
            id = generateId();
        }

        // insert ID into DB
        dbUtil.storeId(id);
        return id;
    }

    private int generateId() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(Integer.MAX_VALUE);
    }

}