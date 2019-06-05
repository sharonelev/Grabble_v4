package com.example.android.grabble_v4.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by user on 22/11/2017.
 */

public class DictionaryDbHelper extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "dictionary.sqlite";
    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;
    Context mContext;
    private SQLiteDatabase dictionaryDB;
    public final static String DATABASE_PATH ="/data/data/com.sha.android.tiles/databases/";

    // Constructor
    public DictionaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("path",DATABASE_PATH);
        this.mContext=context;
    }

    //Create a empty database on the system
    public void createDataBase() throws IOException

    {
        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            Log.v("path_PLUS_NAME", myPath);
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
            File dbfile_table = new File(myPath+"dict_5_letter_words");
            boolean checkDB_table = dbfile_table.exists();
            Log.v("checkDB", String.valueOf(checkDB_table));
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDataBase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        dictionaryDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        int i;
    }

    public boolean check_word(String word){

        boolean valid=false;

        int numberOfLetters = word.length();
        if(numberOfLetters>14)
            numberOfLetters=15; //table includes 15 and more letter words
        String TABLE_NAME = "dict_"+numberOfLetters+"_letter_words";
        String[] COLUMN_NAME={"word"};

       Cursor cursor = dictionaryDB.rawQuery("SELECT word FROM "+ TABLE_NAME + " WHERE word = ? COLLATE NOCASE", new String[] { word });

        String value="";
        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                value= cursor.getString(cursor.getColumnIndex("word"));
                Log.i("table names",value);
                cursor.moveToNext();
            }
        }
        cursor.close();
        if(!value.equals("")){
            valid=true;
        }
        return valid;

    }


    public synchronized void closeDataBase()throws SQLException
    {
        if(dictionaryDB != null)
            dictionaryDB.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



}
