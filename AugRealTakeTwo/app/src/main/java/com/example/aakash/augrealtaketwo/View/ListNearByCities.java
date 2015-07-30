package com.example.aakash.augrealtaketwo.View;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.aakash.augrealtaketwo.Model.DirectionCitiesMap;
import com.example.aakash.augrealtaketwo.Model.NearByPlacesInfo;
import com.example.aakash.augrealtaketwo.R;
import com.example.aakash.augrealtaketwo.Services.CityListAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class ListNearByCities extends ExpandableListActivity {

    private HashMap<String, ArrayList<NearByPlacesInfo>> fetchedDirectionToCitiesMap = new HashMap<String, ArrayList<NearByPlacesInfo>>();
    private ArrayList<String> fetchedAvailableDirectionsList = new ArrayList<String>();
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_near_by_cities);

        expandableListView = (ExpandableListView) findViewById(android.R.id.list);
        fetchedDirectionToCitiesMap = DirectionCitiesMap.directionToCitiesMap;
        fetchedAvailableDirectionsList = DirectionCitiesMap.availableDirectionsList;
        CityListAdapter cityListAdapter = new CityListAdapter(this, fetchedDirectionToCitiesMap, fetchedAvailableDirectionsList);
        expandableListView.setAdapter(cityListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                MainActivity.dataForNearbyCity = fetchedDirectionToCitiesMap.get(
                        fetchedAvailableDirectionsList.get(groupPosition)).get(
                        childPosition);

                MainActivity.showCurrentLocation = false;
                Intent showMainDisplay = new Intent(ListNearByCities.this, MainActivity.class);
                startActivity(showMainDisplay);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
