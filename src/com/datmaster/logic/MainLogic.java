package com.datmaster.logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import com.datmaster.fradio.R;
import com.datmaster.fradio.SharedPreferencesWorker;

public class MainLogic {
	
	private static Context context;
	private static boolean isPlay;	
	private static MediaPlayer mediaPlayer;
	private static Timer timer;		
	private static MainLogicListener listener = null;
	private static CharSequence[] urls;
	private static boolean loadingInProgress;

	public interface MainLogicListener {
		public void onTitleUpdate(String title);
		public void onBuffered();
	}
	
	public static void init(Context cont) {
		context = cont;		
		isPlay = false;
		loadingInProgress = false;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (isPlay)
					updateTitle();
			}
		}, 0, 3000);
		urls = context.getResources().getTextArray(R.array.stations_urls);
	}
	
	public static void setOnUpdateListener(MainLogicListener listn) {
		listener = listn;
	}
	
	private static String getUrl() {
		return urls[SharedPreferencesWorker.getStationId()].toString();
	}
	
	public static boolean getPlayStatus() {
		return isPlay;
	}
		
		
	public static boolean isLoadingInProgress() {
		return loadingInProgress;
	}

	public static void startPlay() {		
		loadingInProgress = true;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(getUrl());
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
					isPlay = true;
					loadingInProgress = false;
					if(listener != null) {
						listener.onBuffered();
						updateTitle();
					}
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void stopPlay() {		
		isPlay = false;		
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.release();			
			mediaPlayer = null;
		}
	}			

	public static void updateTitle() {
		new Thread(new Runnable() {
			public void run() {
				String title = null;
				String metaData = null;
				try {
					URL updateURL = new URL(getUrl());
					URLConnection conn = updateURL.openConnection();
					conn.setRequestProperty("Icy-MetaData", "1");
					int interval = Integer.valueOf(conn
							.getHeaderField("icy-metaint")); // You can get more
																// headers if
																// you wish.
																// There is
																// other useful
																// data.
					InputStream is = conn.getInputStream();
					int skipped = 0;
					while (skipped < interval) {
						skipped += is.skip(interval - skipped);
					}
					int metadataLength = is.read() * 16;
					int bytesRead = 0;
					int offset = 0;
					byte[] bytes = new byte[metadataLength];
					while (bytesRead < metadataLength && bytesRead != -1) {
						bytesRead = is.read(bytes, offset, metadataLength);
						offset = bytesRead;
					}
					metaData = new String(bytes).trim();
					if(metaData.length() > 13)
					title = metaData.substring(
							metaData.indexOf("StreamTitle='") + 13,
							metaData.indexOf(";",
									metaData.indexOf("StreamTitle='")) - 1)
							.trim();
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}				
				
				if(listener != null)
					listener.onTitleUpdate(title);								
			}
		}).start();
	}
}
