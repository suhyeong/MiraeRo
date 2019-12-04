package org.androidtown.miraero;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private String content;
    private int id;
    private int sellcount;
    private Bitmap bitmap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSellcount() {
        return sellcount;
    }

    public void setSellcount(int sellcount) {
        this.sellcount = sellcount;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
