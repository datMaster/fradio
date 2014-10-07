package com.datmaster.fradio.fragments;

import com.datmaster.fradio.MainActivity;
import com.datmaster.fradio.R;
import com.datmaster.holders.MainFragmentHolder;
import com.datmaster.logic.MainFragmentWorker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Fragment newInstance(int sectionNumber) {

		Fragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);

		return fragment;
	}

	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);		
		MainFragmentHolder holder = new MainFragmentHolder();
		holder.tvMetaData = (TextView) rootView.findViewById(R.id.tvStatioInfo);
		holder.tvStation = (TextView) rootView.findViewById(R.id.tvStationName);
		holder.ivButton = (ImageView) rootView.findViewById(R.id.ivPlayPause);
		holder.pbLoading = (ProgressBar) rootView.findViewById(R.id.pbLoading);
		holder.activity = getActivity();
		new MainFragmentWorker(holder);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
