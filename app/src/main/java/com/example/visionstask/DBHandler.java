package com.example.visionstask;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "user_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String GENDER_COL = "gender";
    private static final String IMAGE_PATH = "imagePath";
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + GENDER_COL + " TEXT,"
                + IMAGE_PATH + " TEXT)";
        db.execSQL(query);
    }

    public void addNewUser(String userName, String userGender, String userImagePath) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COL, userName);
        values.put(GENDER_COL, userGender);
        values.put(IMAGE_PATH, userImagePath);
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public ArrayList<UserDataPojo> fetchUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorUserData = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<UserDataPojo> userDataArrayList = new ArrayList<>();
        if (cursorUserData.moveToFirst()) {
            do {
                userDataArrayList.add(new UserDataPojo(cursorUserData.getString(1),
                        cursorUserData.getString(2),
                        cursorUserData.getString(3)));
            } while (cursorUserData.moveToNext());
        }
        cursorUserData.close();
        return userDataArrayList;
    }

    public void deleteUser(ArrayList<UserDataPojo> userDataPojoArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();


        for (int i = 0; i<userDataPojoArrayList.size();i++){
            db.delete(TABLE_NAME, "name=?", new String[]{userDataPojoArrayList.get(i).getName()});
        }
//        db.delete(TABLE_NAME, "name=?", new String[]{name});
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
