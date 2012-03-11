/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 *     http://duracloud.org/license/
 */
package com.edge.media.service.util;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author Andrew Woods
 *         Date: 2/10/12
 */
public class MemoryDbNoGuidUtil implements DbUtil {

    private static MemoryDbNoGuidUtil instance;

    private Hashtable table; // memid -> (streamId -> dateTime)

    private MemoryDbNoGuidUtil() {
        table = new Hashtable();
    }

    public static MemoryDbNoGuidUtil getInstance() {
        if (null == instance) {
            instance = new MemoryDbNoGuidUtil();
        }
        return instance;
    }

    public boolean notUnique(int guid) throws Exception {
        return false;
    }

    public void storeId(int guid) throws Exception {
        String dateTime = new DateUtil().currentDateTime();
//        table.put(Integer.valueOf(guid), new Row(guid, dateTime));
    }

    public void verifyNotExpired(int guid, int memid, int daysValid)
        throws Exception {
        // Default method body

    }

    // FIXME: remove when guid works
    // NOTE: This method is an interim patch.
    // Replace with verifyNotExpired(int guid, int memid, int daysValid) when
    // guid is working
    public void verifyNotExpired(String streamId, int memid, int daysValid)
        throws Exception {
        boolean expired;
        FixMeRow row = (FixMeRow) table.get(Integer.valueOf(memid));
        if (null == row) {
            table.put(Integer.valueOf(memid), new FixMeRow(streamId));
            expired = false;

        } else if (row.isValid(streamId, daysValid)) {
            expired = false;

        } else {
            expired = true;
        }

        if (expired) {
            // Error if date has expired
            StringBuilder err = new StringBuilder("Stream has expired. ");
            err.append("It is valid for ");
            err.append(daysValid);
            err.append(daysValid == 1 ? " day. " : " days. ");
            err.append("memid=");
            err.append(memid);
            throw new Exception(err.toString());
        }
    }

    // FIXME: Remove this method with guid is working
    public String dump() {
        StringBuffer sb = new StringBuffer("memid,streamId,datetime\n");
        Iterator keys = table.keySet().iterator();
        while (keys.hasNext()) {
            Integer memid = (Integer) keys.next();
            FixMeRow row = (FixMeRow)table.get(memid);
            
//            sb.append(row.getGuid());
//            sb.append(",");
//            sb.append(row.getMemid());
//            sb.append(",");
//            sb.append(row.getDateTime());
//            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public void clear() {
        table.clear();
    }

    // FIXME: remove this class when guid is working.
    private static class FixMeRow {
        protected Hashtable rowTable; // streamId -> dateTime

        public FixMeRow(String streamId) {
            this.rowTable = new Hashtable();
            this.rowTable.put(streamId, new DateUtil().currentDateTime());
        }

        public boolean isValid(String streamId, int daysValid) {
            String dateTime = (String) rowTable.get(streamId);

            try {
                Calendar date = new DateUtil().fromString(dateTime);
                date.roll(Calendar.DAY_OF_MONTH, daysValid);
                return System.currentTimeMillis() > date.getTimeInMillis();

            } catch (Exception e) {
                return false;
            }
        }
    }

}
