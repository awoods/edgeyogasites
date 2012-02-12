/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package com.edge.media.service.util;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @author Andrew Woods
 *         Date: 2/10/12
 */
public class MemoryDbUtilTest extends TestCase {

    private MemoryDbUtil dbUtil;

    public void setUp() throws Exception {
        dbUtil = MemoryDbUtil.getInstance();
    }

    public void testNotUnique() throws Exception {
        for (int i = 0; i < 5; ++i) {
            Assert.assertFalse(dbUtil.notUnique(i));
            dbUtil.storeId(i);
            Assert.assertTrue(i + ": " + dbUtil.dump(), dbUtil.notUnique(i));
        }

        Assert.assertTrue(dbUtil.notUnique(0));
        Assert.assertFalse(dbUtil.notUnique(6));
    }

    public void testVerifyNotExpired() throws Exception {
        int guid = 33;
        int memid = 22;
        int daysValid = 1;

        try {
            dbUtil.verifyNotExpired(guid, memid, daysValid);
            Assert.fail("exception expected");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("No stream found"));
        }

        dbUtil.storeId(guid);
        try {
            dbUtil.verifyNotExpired(guid, memid, daysValid);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}
