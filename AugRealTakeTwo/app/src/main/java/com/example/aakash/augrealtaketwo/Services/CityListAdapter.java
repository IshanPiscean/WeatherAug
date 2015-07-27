package com.example.aakash.augrealtaketwo.Services;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.aakash.augrealtaketwo.Model.NearByPlacesInfo;
import com.example.aakash.augrealtaketwo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aakash on 7/1/2015.
 */
public class CityListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String,ArrayList<NearByPlacesInfo>> directionToCitiesMap = new HashMap<String, ArrayList<NearByPlacesInfo>>();
    private ArrayList<String> availableDirectionsList = new ArrayList<String>();

    public CityListAdapter(Context context, HashMap<String, ArrayList<NearByPlacesInfo>> directionToCitiesMap, ArrayList<String> availableDirectionsMap) {
        this.context = context;
        this.directionToCitiesMap = directionToCitiesMap;
        this.availableDirectionsList = availableDirectionsMap;
    }

    @Override
    public int getGroupCount() {
        return directionToCitiesMap.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return directionToCitiesMap.get(availableDirectionsList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return directionToCitiesMap.get(availableDirectionsList.get(i));
    }

    @Override
    public Object getChild(int parent, int child) {
        return directionToCitiesMap.get(availableDirectionsList.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group,viewGroup,false);
        }
        TextView cityTextView = (TextView) convertView.findViewById(R.id.grp_row_name);
        cityTextView.setTypeface(null, Typeface.BOLD);
        ArrayList<NearByPlacesInfo> listOfNearBy = (ArrayList<NearByPlacesInfo>) getGroup(parent);
        if(listOfNearBy.size()>=1)
        cityTextView.setText(listOfNearBy.iterator().next().getPlaceDirection());
        return convertView;
    }

    @Override
    public View getChildView(int parent, int child, boolean isLastChild, View convertView, ViewGroup parentView) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_cities,parentView,false);
        }
        TextView cityTextView = (TextView) convertView.findViewById(R.id.city_list);
        NearByPlacesInfo nearByPlacesInfo = (NearByPlacesInfo)getChild(parent,child);
        cityTextView.setText(nearByPlacesInfo.getPlaceName()+","+ nearByPlacesInfo.getPlaceState());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
