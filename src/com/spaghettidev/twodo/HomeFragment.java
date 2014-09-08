package com.spaghettidev.twodo;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spaghettidev.twodo.FileDialog.FileSelectedListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link HomeFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class HomeFragment extends Fragment implements FileSelectedListener{

	public ListView homeList;
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types and number of parameters
	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		homeList = (ListView) v.findViewById(R.id.home_scroll);
		if (homeList != null){
			homeList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if(position == 0){
						File mPath = new File(Environment.getExternalStorageDirectory() + File.separator + "TODO");
						FileDialog fileDialog = new FileDialog(getActivity(), mPath);
			            fileDialog.setFileEndsWith(".txt");
			            fileDialog.addFileListener(HomeFragment.this);
			            fileDialog.setSelectDirectoryOption(false);
			            fileDialog.showDialog();
					}
					
					if(position == 1){
						dbHelper database = new dbHelper(parent.getContext());
						Log.d("DB", "Getting a writable database...");
						SQLiteDatabase db = database.getWritableDatabase();
						Log.d("DB", "...writable database gotten!");
						database.exportCSV(db);
					}
					if(position == 2){
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setMessage(R.string.nib_content).setTitle(R.string.nib_title);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
		        }
		    });
		}
		return v;
		
	}

	public void onButtonPressed(Uri uri) {
		if (mListener != null) {

		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void fileSelected(File file) {
    	dbHelper database = new dbHelper(this.getActivity().getApplicationContext());
		Log.d("DB", "Getting a writable database...");
		SQLiteDatabase db = database.getWritableDatabase();
		Log.d("DB", "...writable database gotten!");
		database.importCSV(file, db);
	}

}
