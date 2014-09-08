package com.spaghettidev.twodo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TODOFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TODOFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class TODOFragment extends Fragment {

	private View v;
	private EditText quickNote;
	
	private OnFragmentInteractionListener mListener;

	// TODO: Rename and change types and number of parameters
	public static TODOFragment newInstance() {
		TODOFragment fragment = new TODOFragment();
		return fragment;
	}

	public TODOFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		v = inflater.inflate(R.layout.fragment_todo, container, false);
		
		(new Handler()).postDelayed(new Runnable() {

            @SuppressLint("Recycle")
			public void run() {             
                quickNote= (EditText) v.findViewById(R.id.quickNote);

                quickNote.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                quickNote.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));                       

            }
        }, 200);
		
		return v;
	}

	// TODO: Rename method, update argument and hook method into UI event
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
	
	public String getNoteText(){
		return quickNote.getText().toString();
	}

}
