package org.vt.ece4564.multipong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ScoresActivity extends ListActivity {

	private final String FILE = "scores.txt";
	private final String TAG = "ScoresActivity";
	
	//TODO: change to ArrayList
	private ArrayList<String> scores_;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scores_ = new ArrayList<String>();
		
		//create file if does not exist
		
		File scoreFile = new File(Environment.getExternalStorageDirectory() + "/mp_scores.txt");
		
		//FileOutputStream fos = new FileOutputStream(scoreFile);
		/*
		try {
			fos_ = openFileOutput(FILE, Context.MODE_PRIVATE);
			fos_.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		Log.d(TAG, "Reading File");

		//TODO: Load Scores from storage
		BufferedReader reader;
		String temp;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(scoreFile.getCanonicalPath())));
			temp = reader.readLine();
			while(temp!=null) {
				scores_.add(temp);
				temp = reader.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d(TAG,"Scores Read");
		
		setListAdapter(new ArrayAdapter<String>(this,
		          android.R.layout.simple_list_item_1, scores_));
		  getListView().setTextFilterEnabled(false);
	
	}
	
	public void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
}
