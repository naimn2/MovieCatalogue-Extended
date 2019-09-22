package com.muflihun.moviecatalogue5.helpers;

import android.database.Cursor;

import com.muflihun.moviecatalogue5.models.Item;

import static com.muflihun.moviecatalogue5.db.DatabaseContract.TableColumns.*;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Item> mapCursorToArrayList(Cursor itemCursor) {
        ArrayList<Item> itemList = new ArrayList<>();

        while (itemCursor.moveToNext()) {
            int id = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(_ID));
            String title = itemCursor.getString(itemCursor.getColumnIndexOrThrow(TITLE));
            String poster = itemCursor.getString(itemCursor.getColumnIndexOrThrow(POSTER));
            String overview = itemCursor.getString(itemCursor.getColumnIndexOrThrow(OVERVIEW));
            double vote = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow(VOTE));;
            double popularity = itemCursor.getDouble(itemCursor.getColumnIndexOrThrow(POPULARITY));
            String release = itemCursor.getString(itemCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String language = itemCursor.getString(itemCursor.getColumnIndexOrThrow(LANGUAGE));
            String backdrop = itemCursor.getString(itemCursor.getColumnIndexOrThrow(BACKDROP));
            itemList.add(new Item(id, title, poster, overview, vote, popularity, release, language, backdrop));
        }
        return itemList;
    }
}
