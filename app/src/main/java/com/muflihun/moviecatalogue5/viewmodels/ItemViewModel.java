package com.muflihun.moviecatalogue5.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.muflihun.moviecatalogue5.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ItemViewModel extends ViewModel {
    public static final String API_KEY = "7f147371d293faae7c59388c4d5591f5";
    public static final String ITEM_MOVIE = "movie";
    public static final String ITEM_TVSHOW = "tv";
    private MutableLiveData<ArrayList<Item>> listItem = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Item>> getListItem() {
        return listItem;
    }

    public void setItem(final String item){
        String url = "https://api.themoviedb.org/3/discover/"+item+"?api_key="+API_KEY+"&language=en-US";
        final ArrayList<Item> list = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jObject = new JSONObject(result);
                    JSONArray jArray = jObject.getJSONArray("results");

                    for (int i=0; i<jArray.length(); i++){
                        JSONObject jItem = jArray.getJSONObject(i);
                        list.add(new Item(jItem, item.equals("movie")? Item.MOVIE_DATA_KEYS: Item.TVSHOW_DATA_KEYS));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listItem.postValue(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }
}
