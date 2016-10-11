package com.soaibscosmos.todolist.db;

import android.provider.BaseColumns;

/**
 * Created by soaib on 10/11/16.
 */

public class TaskContract {

    public static final String DB_NAME = "com.soaibscosmos.todolist.db";
    public static final int DB_VERSION = 1;

    /*By implementing the BaseColumns interface, your inner class can inherit a primary key field
    * called _ID that some Android classes such as cursor adaptors will expect it to have. It's not
    * required, but this can help your database work harmoniously with the Android framework.
    */
    public class TaskEntry implements BaseColumns{
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
    }
}
