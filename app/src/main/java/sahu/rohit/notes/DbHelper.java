package sahu.rohit.notes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table notes(title text primary key,body text, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists notes");
    }

    public boolean insert(String title, String body, byte[] image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        contentValues.put("body",body);
        contentValues.put("image",image);
        long ins = db.insert("notes",null,contentValues);
        if(ins == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public List<notes_model> getdetails()
    {
        List<notes_model> notes_model_List = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from notes",null);
        if(cursor.moveToFirst())
        {
            do
            {
                String title = cursor.getString(0);
                String body = cursor.getString(1);
                byte[] id_image = cursor.getBlob(2);

                notes_model notes_model = new notes_model(title,body,id_image);
                notes_model_List.add(notes_model);
            }
            while (cursor.moveToNext());
        }
        return notes_model_List;
    }
}
