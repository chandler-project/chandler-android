package com.chandlersystem.chandler.database;

import com.raizlabs.android.dbflow.annotation.Database;

import static com.chandlersystem.chandler.database.ChandlerDatabase.DB_NAME;
import static com.chandlersystem.chandler.database.ChandlerDatabase.DB_VERSION;

@Database(name = DB_NAME, version = DB_VERSION)
public class ChandlerDatabase {
    public static final String DB_NAME = "ChandlerDatabase";
    public static final int DB_VERSION = 1;
}
