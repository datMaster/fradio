package com.datmaster.fradio.fragments;

import com.datmaster.fradio.MainActivity;
import com.datmaster.fradio.R;
import com.datmaster.fradio.adapters.StationsAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class StationsFragment extends Fragment {
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	public static Fragment newInstance(int sectionNumber) {

		Fragment fragment = new StationsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_stations, container,
				false);
		ListView lvStations = (ListView) rootView.findViewById(R.id.lvstations);
		lvStations.setAdapter(new StationsAdapter(getActivity()));
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
