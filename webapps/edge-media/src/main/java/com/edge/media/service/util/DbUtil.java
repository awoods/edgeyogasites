/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package com.edge.media.service.util;

/**
 * @author Andrew Woods
 *         Date: 2/10/12
 */
public interface DbUtil {
    public boolean notUnique(int guid) throws Exception;

    public void storeId(int guid) throws Exception;

    public void verifyNotExpired(int guid, int memid, int daysValid)
        throws Exception;
}
