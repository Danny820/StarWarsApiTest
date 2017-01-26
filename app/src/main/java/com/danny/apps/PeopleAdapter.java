package com.danny.apps;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.danny.apps.materialtests.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.swapi.models.People;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Danny on 22/1/2017.
 */

public class PeopleAdapter extends BaseAdapter {

    Activity context;
    LayoutInflater mInflater;
    ArrayList<People> arrayList;

    public PeopleAdapter (Activity activity, ArrayList<People> arrayList){
        this.context = activity;
        this.arrayList = arrayList;
        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PeopleAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new PeopleAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.people_row, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.birthYear = (TextView) convertView.findViewById(R.id.birth_year);
            holder.homeWorld = (TextView) convertView.findViewById(R.id.home_world);
            convertView.setTag(holder);
        } else {
            holder = (PeopleAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(arrayList.get(position).name);
        holder.birthYear.setText(arrayList.get(position).birthYear);


        if (arrayList.get(position).home.equals("")){
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(arrayList.get(position).homeWorldUrl,new MyHandler(holder.homeWorld, position));
        } else {
            holder.homeWorld.setText(arrayList.get(position).home);
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView birthYear;
        TextView homeWorld;
    }

    class MyHandler extends TextHttpResponseHandler{

        private final  WeakReference<TextView> textViewWeakReference;
        private int position;
        public MyHandler(TextView textView , int position) {
            this.textViewWeakReference = new WeakReference<>(textView);
            this.position = position;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                JSONObject object = new JSONObject(responseString);
                 final String homeWorld = object.getString("name");
                final TextView textView = textViewWeakReference.get();
                arrayList.get(position).home = homeWorld;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(textView == null){
                            return;
                        }
                        if (homeWorld == null) {
                            return;
                        }
                        textView.setText(homeWorld);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
