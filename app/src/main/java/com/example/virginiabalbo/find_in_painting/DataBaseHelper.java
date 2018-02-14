package com.example.virginiabalbo.find_in_painting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by virginia on 10/02/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATA_BASE = "gallery";
    public static final int VERSION = 1;
    private final Context context;

    public DataBaseHelper (Context context) {
        super(context, DATA_BASE, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Painting.TABLE + " (" +
                Painting._ID + " INTEGER PRIMARY KEY, " +
                Painting.TITLE + " TEXT, " +
                Painting.ARTIST + " TEXT, " +
                Painting.IMAGESRC + " TEXT, " +
                Painting.THUMBSRC + " TEXT); "
        );

        db.execSQL("CREATE TABLE " + HiddenObject.TABLE + " (" +
                HiddenObject._ID + " INTEGER PRIMARY KEY, " +
                HiddenObject.DESCRIPTION + " TEXT, " +
                HiddenObject.XPOSIOTION + " INTEGER, " +
                HiddenObject.YPOSIOTION + " INTEGER, " +
                HiddenObject.WIDTH + " INTEGER, " +
                HiddenObject.HEIGHT + " INTEGER, " +
                HiddenObject.CHECKED + " INTEGER, " +
                HiddenObject.PAINTINGID + " INTEGER, " +
                "FOREIGN KEY (" + HiddenObject.PAINTINGID + ") REFERENCES " + Painting.TABLE + "(" + Painting._ID + "));"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public static class Painting {
        public static final String TABLE = "painting";
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String ARTIST = "artist";
        public static final String IMAGESRC = "imageSrc";
        public static final String THUMBSRC = "thumbSrc";
        public static final String[] COLUMNS = new String[]{
                _ID, TITLE, ARTIST, IMAGESRC, THUMBSRC
        };
    }

    public static class HiddenObject {
        public static final String TABLE = "hiddenObject";
        public static final String _ID = "_id";
        public static final String PAINTINGID = "paintingId";
        public static final String DESCRIPTION = "description";
        public static final String XPOSIOTION = "xPosition";
        public static final String YPOSIOTION = "yPosition";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String CHECKED = "checked";
        public static final String[] COLUMNS = new String[]{
                _ID, PAINTINGID, DESCRIPTION, XPOSIOTION, YPOSIOTION, WIDTH, HEIGHT, CHECKED
        };
    }
}
