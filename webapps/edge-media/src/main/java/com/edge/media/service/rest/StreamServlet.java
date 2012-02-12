package com.edge.media.service.rest;

import com.edge.media.service.beans.StreamBean;
import com.edge.media.service.util.HttpUtil;
import com.edge.media.service.util.MemoryDbUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Andrew Woods
 *         Date: 2/4/12
 */
public class StreamServlet extends HttpServlet {

    private static final String BASE_URL = "http://s3.amazonaws.com/aw-edge-audio/";
    private static final String DISPATCH_TARGET = "/jsp/stream.jsp";

    private static final String MEMID_PARAM = "mid";
    private static final String GUID_PARAM = "guid";

    private static final int DAYS_VALID = 1;


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {
        StreamBean streamBean = new StreamBean();
        String streamName;
        try {
            streamName = getStreamName(request);

        } catch (Exception e) {
            streamBean.setError(e.getMessage());
            dispatch(request, response, streamBean);
            return;
        }

        streamBean.setName(streamName);
        dispatch(request, response, streamBean);
    }

    private String getStreamName(HttpServletRequest request) throws Exception {
        // collect query parameters
        int guid = getParameter(GUID_PARAM, request);
        int memid = getParameter(MEMID_PARAM, request);

        MemoryDbUtil.getInstance().verifyNotExpired(guid, memid, DAYS_VALID);

        // determine stream name
        String name = getPathInfo(request);

        new HttpUtil().verifyUrl(BASE_URL, name, ".mp3");

        return name;
    }

    private int getParameter(String paramName, HttpServletRequest request)
        throws Exception {
        String param = request.getParameter(paramName);
        if (null == param) {
            StringBuilder err = new StringBuilder("Missing param: ");
            err.append(paramName);
            throw new Exception(err.toString());
        }

        try {
            return Integer.parseInt(param);

        } catch (NumberFormatException e) {
            StringBuilder err = new StringBuilder("Param must be an integer: ");
            err.append(paramName);
            err.append("=");
            err.append(param);
            throw new Exception(err.toString());
        }
    }

    private String getPathInfo(HttpServletRequest request) throws Exception {
        String pathInfo = request.getPathInfo();
        if (null == pathInfo) {
            StringBuilder err = new StringBuilder("Invalid URL, null pathInfo");
            throw new Exception(err.toString());
        }
        return pathInfo.substring(pathInfo.indexOf("/") + 1);
    }

    private void dispatch(HttpServletRequest request,
                          HttpServletResponse response,
                          StreamBean streamBean)
        throws IOException, ServletException {
        request.setAttribute("streamBean", streamBean);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(
            DISPATCH_TARGET);
        dispatcher.forward(request, response);
    }

}
