package com.example.virginiabalbo.find_in_painting.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.virginiabalbo.find_in_painting.DataBaseHelper;
import com.example.virginiabalbo.find_in_painting.domain.HiddenObject;
import com.example.virginiabalbo.find_in_painting.domain.Painting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virginia on 10/02/18.
 */

public class DAO {
    private DataBaseHelper helper;
    private SQLiteDatabase db;

    // Constructor
    public DAO(Context context){
        helper = new DataBaseHelper(context);
    }

    private SQLiteDatabase getDb(){
        if(db == null){
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close(){
        helper.close();
    }

    public long insertPainting(Painting painting){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Painting.TITLE, painting.getTitle());
        values.put(DataBaseHelper.Painting.ARTIST, painting.getArtist());
        values.put(DataBaseHelper.Painting.IMAGESRC, painting.getImageSrc());
        values.put(DataBaseHelper.Painting.THUMBSRC, painting.getThumbSrc());
        return getDb().insert(DataBaseHelper.Painting.TABLE, null, values);
    }

    public long insertHiddenObject(HiddenObject hiddenObject){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.HiddenObject.DESCRIPTION, hiddenObject.getDescription());
        values.put(DataBaseHelper.HiddenObject.PAINTINGID, hiddenObject.getPaintingId());
        values.put(DataBaseHelper.HiddenObject.XPOSIOTION, hiddenObject.getXPosition());
        values.put(DataBaseHelper.HiddenObject.YPOSIOTION, hiddenObject.getYPosition());
        values.put(DataBaseHelper.HiddenObject.WIDTH, hiddenObject.getWidth());
        values.put(DataBaseHelper.HiddenObject.HEIGHT, hiddenObject.getHeight());
        values.put(DataBaseHelper.HiddenObject.CHECKED, hiddenObject.getChecked());
        return getDb().insert(DataBaseHelper.HiddenObject.TABLE, null, values);
    }

    public List<Painting> listPaintings(){
        Cursor cursor = getDb().query(DataBaseHelper.Painting.TABLE, DataBaseHelper.Painting.COLUMNS,
                null, null, null, null, DataBaseHelper.Painting.TITLE);
        List<Painting> paintings = new ArrayList<>();
        while (cursor.moveToNext()){
            Painting painting = createPainting(cursor);
            paintings.add(painting);
        }
        cursor.close();
        return paintings;
    }

    public List<HiddenObject> listHiddenObjects(Integer paintingId){
        String selection = DataBaseHelper.HiddenObject.PAINTINGID + " = ? ";
        String[] selectionArgs = new String[]{ paintingId.toString() };
        Cursor cursor = getDb().query(DataBaseHelper.HiddenObject.TABLE, DataBaseHelper.HiddenObject.COLUMNS,
                selection, selectionArgs, null, null, DataBaseHelper.HiddenObject.DESCRIPTION);
        List<HiddenObject> hiddenObjects = new ArrayList<>();
        while (cursor.moveToNext()){
            HiddenObject hiddenObject = createHiddenObject(cursor);
            hiddenObjects.add(hiddenObject);
        }
        cursor.close();
        return hiddenObjects;
    }

    public int countAllHiddenObjects(Painting painting){
        String selection = DataBaseHelper.HiddenObject.PAINTINGID + " = ? ";
        String[] selectionArgs = new String[]{ painting.getId().toString() };
        Cursor cursor = getDb().query(DataBaseHelper.HiddenObject.TABLE, DataBaseHelper.HiddenObject.COLUMNS,
                selection, selectionArgs, null, null, DataBaseHelper.HiddenObject.DESCRIPTION);
        return cursor.getCount();
    }

    public int countCheckedHiddenObjects(Painting painting){
        String selection = DataBaseHelper.HiddenObject.PAINTINGID + " = ? AND " + DataBaseHelper.HiddenObject.CHECKED + " = ? ";
        String[] selectionArgs = new String[]{ painting.getId().toString(), "1" };
        Cursor cursor = getDb().query(DataBaseHelper.HiddenObject.TABLE, DataBaseHelper.HiddenObject.COLUMNS,
                selection, selectionArgs, null, null, DataBaseHelper.HiddenObject.DESCRIPTION);
        return cursor.getCount();
    }

    public Painting findPainting(Integer id) {
        String selection = DataBaseHelper.Painting._ID + " = ? ";
        String[] selectionArgs = new String[]{ id.toString() };
        Cursor cursor = getDb().query(DataBaseHelper.Painting.TABLE, DataBaseHelper.Painting.COLUMNS,
                selection, selectionArgs, null, null, null);
        Painting painting = null;
        if(cursor.moveToNext()){
            painting = createPainting(cursor);
        }
        cursor.close();
        return painting;
    }

    public void checkHiddenObject(Integer id) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.HiddenObject.CHECKED, "1");
        String whereClause = DataBaseHelper.Painting._ID + " = ? ";
        String[] whereArgs = new String[]{ id.toString() };
        getDb().update(DataBaseHelper.HiddenObject.TABLE, values, whereClause, whereArgs);
    }

    private Painting createPainting(Cursor cursor){
        return new Painting(
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.Painting._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Painting.TITLE)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Painting.ARTIST)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Painting.IMAGESRC)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Painting.THUMBSRC))
        );
    }

    private HiddenObject createHiddenObject(Cursor cursor){
        return new HiddenObject(
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject._ID)),
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject.PAINTINGID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.HiddenObject.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject.XPOSIOTION)),
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject.YPOSIOTION)),
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject.WIDTH)),
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject.HEIGHT)),
                cursor.getInt(cursor.getColumnIndex(DataBaseHelper.HiddenObject.CHECKED))
                );
    }
}
