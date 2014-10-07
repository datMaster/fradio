package com.datmaster.fradio.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datmaster.fradio.R;
import com.datmaster.fradio.SharedPreferencesWorker;
import com.datmaster.logic.MainLogic;
import com.datmaster.logic.MainLogic.MainLogicListener;

public class StationsAdapter extends BaseAdapter implements OnClickListener, MainLogicListener{
	
	private LayoutInflater inflater;
	private Activity activity;
	private CharSequence[] stations;
	private ArrayList<StationsViewHolder> viewList;
	private TypedArray logos;
	private int lastSelectedItem = 0;
	
	public StationsAdapter(Activity activity) {
		this.activity = activity;
		inflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		stations = this.activity.getResources().getTextArray(R.array.stations_name);
		viewList = new ArrayList<StationsViewHolder>();
		logos = this.activity.getResources().obtainTypedArray(R.array.stations_logos);
		for(int i = 0; i < stations.length; i ++)
			viewList.add(new StationsViewHolder());
		MainLogic.setOnUpdateListener(this);
	}
	
	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public Object getItem(int position) {
		return viewList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.radio_station_item, null);
		StationsViewHolder holder = viewList.get(position);
		holder.tvStationName = (TextView) convertView.findViewById(R.id.tvRadioStationName);
		holder.flLineSelect = (FrameLayout) convertView.findViewById(R.id.flRadioStationRow);
		holder.ivLogo = (ImageView) convertView.findViewById(R.id.ivLogoAdapter);		
		holder.pbLoading = (ProgressBar) convertView.findViewById(R.id.pbLoadingAdapter);		
		holder.tvStationName.setText(stations[position].toString());
		holder.flLineSelect.setTag(position);
		holder.flLineSelect.setOnClickListener(this);
		holder.ivLogo.setImageDrawable(logos.getDrawable(position));
		return convertView;
	}
	
	private class StationsViewHolder {
		public TextView tvStationName;
		public FrameLayout flLineSelect;
		public ImageView ivLogo;
		public ProgressBar pbLoading;
	}

	@Override
	public void onClick(View v) {
		lastSelectedItem = Integer.parseInt(v.getTag().toString());	
		SharedPreferencesWorker.setStationId(lastSelectedItem);
		if(MainLogic.getPlayStatus()) {
			MainLogic.stopPlay();
		}
		MainLogic.startPlay();
		viewList.get(lastSelectedItem).pbLoading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onTitleUpdate(String title) {		
	}

	@Override
	public void onBuffered() {
		viewList.get(lastSelectedItem).pbLoading.setVisibility(View.INVISIBLE);		
	}

}
