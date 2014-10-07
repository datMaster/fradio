package com.datmaster.logic;

import android.view.View;
import android.view.View.OnClickListener;

import com.datmaster.fradio.R;
import com.datmaster.fradio.SharedPreferencesWorker;
import com.datmaster.holders.MainFragmentHolder;
import com.datmaster.logic.MainLogic.MainLogicListener;

public class MainFragmentWorker implements MainLogicListener, OnClickListener {
	
	private MainFragmentHolder holder; 	
	private CharSequence[] stationsNames;
	
	public MainFragmentWorker(MainFragmentHolder holder) {
		this.holder = holder;
		this.stationsNames = this.holder.activity.getResources().getTextArray(R.array.stations_name);
		this.holder.ivButton.setOnClickListener(this);
		MainLogic.setOnUpdateListener(this);
		initViews();
	}
	
	private void initViews() {
		holder.tvStation.setText(getStationName());
		holder.ivButton.setBackgroundResource(MainLogic.getPlayStatus() ? 
				android.R.drawable.ic_media_pause : 
				android.R.drawable.ic_media_play);
		if(MainLogic.isLoadingInProgress())
			onPlayViews();
		else
			MainLogic.updateTitle();
	}
	
	private String getStationName() {
		return stationsNames[SharedPreferencesWorker.getStationId()].toString();
	}

	@Override
	public void onTitleUpdate(final String title) {		
		holder.activity.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				if(MainLogic.getPlayStatus())
					holder.tvMetaData.setText(title);
			}
		});		
	}

	@Override
	public void onBuffered() {
		hideProgress();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivPlayPause:
			if(MainLogic.getPlayStatus()) { 
				MainLogic.stopPlay();
				onStopViews();
			}
			else {
				MainLogic.startPlay();
				onPlayViews();
			}
			break;

		default:
			break;
		}
	}
	
	private void onPlayViews() {
		showProgress();
		holder.ivButton.setBackgroundResource(android.R.drawable.ic_media_pause);
	}
	
	private void onStopViews() {
		holder.ivButton.setBackgroundResource(android.R.drawable.ic_media_play);
		holder.tvMetaData.setText(null);
	}
	
	private void showProgress() {
		holder.pbLoading.setVisibility(View.VISIBLE);
	}
	
	private void hideProgress() {
		holder.pbLoading.setVisibility(View.INVISIBLE);
	}
}
