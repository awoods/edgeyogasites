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
 *         Date: 2/4/12
 */
public class DateUtilTest extends TestCase {

    private DateUtil util;

    public void setUp() {
        util = new DateUtil();
    }

    public void testCurrentDateTime() throws Exception {
        String dateTime = util.currentDateTime();
        Assert.assertNotNull(dateTime);
    }

}
