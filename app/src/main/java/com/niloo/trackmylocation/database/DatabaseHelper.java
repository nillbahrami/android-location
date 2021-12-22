package com.niloo.trackmylocation.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.niloo.trackmylocation.database.models.LocationModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "taxi-assistant.db";

    private Dao<LocationModel, Integer> itemDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);// R.raw.ormlite_config);
        boolean dbexist = checkdatabase(context);
        if (!dbexist) {

            // If database did not exist, try copying existing database from assets folder.
            try {
                File outfilename = context.getDatabasePath(DATABASE_NAME);

                InputStream myinput = context.getAssets().open(DATABASE_NAME);
                //String outfilename = DATABASE_PATH + DATABASE_NAME;
                Log.i(DatabaseHelper.class.getName(), "DB Path : " + outfilename);
                OutputStream myoutput = new FileOutputStream(outfilename);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.flush();
                myoutput.close();
                myinput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkdatabase(Context context) {
        boolean checkdb = false;

        File dbfile = context.getDatabasePath(DATABASE_NAME);
        checkdb = dbfile.exists();

        Log.i(DatabaseHelper.class.getName(), "DB Exist : " + checkdb);
        if(checkdb) {
            try {
                String path = Environment.getExternalStorageDirectory() + "/ShowJava/";
                new File(path).mkdirs();
                FileInputStream myinput = new FileInputStream(dbfile);
                FileOutputStream myoutput = new FileOutputStream(path + "taxi-assistant.sqlite");
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.close();
                myinput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return checkdb;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/


    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, LocationModel.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<LocationModel, Integer> getUserInfoDao() throws SQLException {
        if (itemDao == null) {
            itemDao = getDao(LocationModel.class);
        }
        return itemDao;
    }



}