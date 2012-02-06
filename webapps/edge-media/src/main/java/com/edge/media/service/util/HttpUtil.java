/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package com.edge.media.service.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.HeadMethod;

import java.io.IOException;

/**
 * @author Andrew Woods
 *         Date: 2/5/12
 */
public class HttpUtil {

    public void verifyUrl(String url, String path, String suffix) throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod method = new HeadMethod(url + path+suffix);
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(method);
        } catch (IOException e) {
            // do nothing
        }
        method.releaseConnection();

        if (statusCode != HttpStatus.SC_OK) {
            StringBuilder err = new StringBuilder("Stream does not exist: ");
            err.append(path);
            err.append(", ");
            err.append(statusCode);
            throw new Exception(err.toString());
        }
    }
}
