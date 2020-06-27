package com.example.grievanceregistrationform.dp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbFarmer extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "studentManager";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_DATA = "data";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STUDID = "farmerId";
    private static final String TABLE_CONTACTS = "student";

    public DbFarmer(Context paramContext) {
        super(paramContext, "studentManager", null, 1);
    }

    public void addData(String paramString1, String paramString2) {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("farmerId", paramString1);
        localContentValues.put("data", paramString2);
        localContentValues.put("password", "");
        localSQLiteDatabase.insert("student", null, localContentValues);
        localSQLiteDatabase.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
//        localSQLiteDatabase.delete("student", null, null);
//        localSQLiteDatabase.close();
        db.execSQL("delete from "+ "student");
        db.close();
    }

    public void deletefarmerId(String paramString) {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        localSQLiteDatabase.delete("student", "farmerId = ?", new String[]{paramString});
        localSQLiteDatabase.close();
    }

    public List<ArrayList<String>> getAllData() {
        ArrayList localArrayList1 = new ArrayList();
        Cursor localCursor = getWritableDatabase().rawQuery("SELECT  * FROM student", null);
        if (localCursor.moveToFirst())
            do {
                ArrayList localArrayList2 = new ArrayList();
                localArrayList2.add(localCursor.getString(0));
                localArrayList2.add(localCursor.getString(1));
                if (localCursor.getString(2) != null)
                    localArrayList2.add(localCursor.getString(2));
                localArrayList1.add(localArrayList2);
            }
            while (localCursor.moveToNext());
        return localArrayList1;
    }

    public int getCountByfarmerId(String paramString) {
        String str = "SELECT  * FROM student WHERE farmerId='" + paramString + "'";
        Cursor localCursor = getReadableDatabase().rawQuery(str, null);
        int i = localCursor.getCount();
        localCursor.close();
        return i;
    }

    // Getting single contact
    public ArrayList<String> getDataByfarmerId(String farmerId) {

        SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_STUDID,
                        KEY_DATA, KEY_PASSWORD}, KEY_STUDID + "=?",
                new String[]{farmerId}, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            return new ArrayList<String>() {{
                add(cursor.getString(0));
                add(cursor.getString(1));
                if (cursor.getString(2) != null) {
                    add(cursor.getString(2));
                }
            }};
        }
        return null;
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL("CREATE TABLE student(farmerId TEXT,data TEXT,password TEXT)");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS student");
        onCreate(paramSQLiteDatabase);
    }

    public int updatePassByfarmerId(String paramString1, String paramString2) {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("password", paramString2);
        return localSQLiteDatabase.update("student", localContentValues, "farmerId = ?", new String[]{paramString1});
    }

    public int updatedataByfarmerId(String paramString1, String paramString2) {
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("farmerId", paramString1);
        localContentValues.put("data", paramString2);
        return localSQLiteDatabase.update("student", localContentValues, "farmerId = ?", new String[]{paramString1});
    }
}

/* Location:           D:\Gopal\downloads\Apk decompile java\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     smart.breed.contest.db.DbStudent
 * JD-Core Version:    0.6.0
 */