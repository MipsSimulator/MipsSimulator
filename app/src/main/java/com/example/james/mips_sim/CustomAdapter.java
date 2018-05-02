package com.example.james.mips_sim;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter <DataItem> {

Context context;
int layoutResourceId;
List<DataItem> data = null;


public CustomAdapter(Context context, int resource, List<DataItem> objects) {
    super(context, resource, objects);

    this.layoutResourceId = resource;
    this.context = context;
    this.data = objects;

}

static class DataHolder
{
    TextView instName;
    TextView instNum;
}

public View getView(int position, View convertView, ViewGroup parent) {
   DataHolder holder = null;
   if (convertView == null)
   {
       LayoutInflater inflater = ((Activity) context).getLayoutInflater();
       convertView = inflater.inflate(layoutResourceId, parent);

       holder = new DataHolder();
       holder.instName = (TextView) convertView.findViewById(R.id.inst);
       holder.instNum = (TextView) convertView.findViewById(R.id.lineNum);

       convertView.setTag(holder);
   }
    else
   {
       holder = (DataHolder)convertView.getTag();
   }

   DataItem dataItem = data.get(position);
   holder.instName.setText(dataItem.instName);
   holder.instNum.setText(dataItem.lineNum);

   return  convertView;
}

}
