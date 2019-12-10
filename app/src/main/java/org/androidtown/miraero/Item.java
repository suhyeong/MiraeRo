package org.androidtown.miraero;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private String content;
    private int id;
    private long sellcount;
    private Bitmap bitmap;
    private int event_id;
    private String north_or_south;

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

    public long getSellcount() {
        return sellcount;
    }

    public void setSellcount(long sellcount) {
        this.sellcount = sellcount;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getNorth_or_South() {
        return north_or_south;
    }

    public void setNorth_or_South(String north_or_south) {
        this.north_or_south = north_or_south;
    }
}
