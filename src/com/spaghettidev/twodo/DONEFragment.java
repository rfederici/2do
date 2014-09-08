package com.spaghettidev.twodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link DONEFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class DONEFragment extends ListFragment {

	private dbHelper database;
	private SQLiteDatabase db;
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types and number of parameters
	public static DONEFragment newInstance() {
		DONEFragment fragment = new DONEFragment();
		// Inflate the layout for this fragment
		return fragment;
	}

	public DONEFragment() {
		// Required empty public constructor
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		database = new dbHelper(this.getActivity());
		Log.d("DB", "Getting a writable database...");
		db = database.getWritableDatabase();
		Log.d("DB", "...writable database gotten!");
		
		Cursor resultSet = database.getNotes(db);

		String[] fromColumns = {dbHelper.colContent, dbHelper.colID};
		int[] toViews = {R.id.text, R.id.dbid};
		
        this.setListAdapter(new SimpleCursorAdapter(this.getActivity(), R.layout.note_list_layout, resultSet, fromColumns, toViews));
        
		return super.onCreateView(inflater, container, savedInstanceState);
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
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		TextView src = (TextView)v.findViewById(R.id.text);
		TextView dbid = (TextView)v.findViewById(R.id.dbid);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(src.getText()).setTitle("Note ID: " + dbid.getText());
		AlertDialog dialog = builder.create();
		dialog.show();
        super.onListItemClick(l, v, position, id);
    }

}
