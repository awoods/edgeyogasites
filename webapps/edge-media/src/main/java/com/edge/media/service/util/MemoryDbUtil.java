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
public class MemoryDbUtil implements DbUtil {

    private static MemoryDbUtil instance;

    private Hashtable table; // guid -> Row

    private MemoryDbUtil() {
        table = new Hashtable();
    }

    public static MemoryDbUtil getInstance() {
        if (null == instance) {
            instance = new MemoryDbUtil();
        }
        return instance;
    }

    public boolean notUnique(int guid) throws Exception {
        return table.containsKey(Integer.valueOf(guid));
    }

    public void storeId(int guid) throws Exception {
        String dateTime = new DateUtil().currentDateTime();
        table.put(Integer.valueOf(guid), new Row(guid, dateTime));
    }

    public void verifyNotExpired(int guid, int memid, int daysValid)
        throws Exception {
        boolean expired;
        Row row = (Row) table.get(Integer.valueOf(guid));
        if (null != row) {
            Calendar date = new DateUtil().fromString(row.getDateTime());
            date.roll(Calendar.DAY_OF_MONTH, daysValid);

            // update database to set member making call
            row.setMemid(memid);

            expired = System.currentTimeMillis() > date.getTimeInMillis();

        } else {
            // Error if no stream found in DB
            StringBuilder err = new StringBuilder("No stream found. ");
            err.append("memid=");
            err.append(memid);
            err.append(", ");
            err.append("guid=");
            err.append(guid);
            throw new Exception(err.toString());
        }
        if (expired) {
            // Error if date has expired
            StringBuilder err = new StringBuilder("Stream has expired. ");
            err.append("It is valid for ");
            err.append(daysValid);
            err.append(daysValid == 1 ? " day. " : " days. ");
            err.append("memid=");
            err.append(memid);
            err.append(", ");
            err.append("guid=");
            err.append(guid);
            throw new Exception(err.toString());
        }
    }

    public String dump() {
        StringBuffer sb = new StringBuffer("guid,memid,datetime\n");
        Iterator values = table.values().iterator();
        while (values.hasNext()) {
            Row row = (Row) values.next();
            sb.append(row.getGuid());
            sb.append(",");
            sb.append(row.getMemid());
            sb.append(",");
            sb.append(row.getDateTime());
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public void clear() {
        table.clear();
    }


    private static class Row {
        private int guid;
        private int memid;
        private String dateTime;

        public Row(int guid, String dateTime) {
            this.guid = guid;
            this.memid = -1;
            this.dateTime = dateTime;
        }

        public void setMemid(int memid) {
            this.memid = memid;
        }

        public int getGuid() {
            return guid;
        }

        public int getMemid() {
            return memid;
        }

        public String getDateTime() {
            return dateTime;
        }
    }

}
