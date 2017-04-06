package com.honyum.elevatorMan.data;

import android.database.Cursor;

/**
 * Created by changhaozhang on 16/3/5.
 */
public class KnowledgeInfo {

    private String id;

    private String title;

    private String keyWords;

    private String knType;

    private String parts;

    private String brand;

    private String content;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public String getKnType() {
        return knType;
    }

    public String getParts() {
        return parts;
    }

    public String getBrand() {
        return brand;
    }

    public String getContent() {
        return content;
    }

    public KnowledgeInfo(Cursor cursor) {
        if (null == cursor) {
            return;
        }

        this.id = cursor.getString(getIndexOfColumn(cursor, "id"));
        this.title = cursor.getString(getIndexOfColumn(cursor, "title"));
        this.keyWords = cursor.getString(getIndexOfColumn(cursor, "keywords"));
        this.knType = cursor.getString(getIndexOfColumn(cursor, "kntype"));
        this.brand = cursor.getString(getIndexOfColumn(cursor, "brand"));
        //this.content = cursor.getString(getIndexOfColumn(cursor, "content"));
    }

    private int getIndexOfColumn(Cursor cursor, String column) {
        return cursor.getColumnIndex(column);
    }
}
