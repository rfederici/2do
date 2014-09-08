package com.spaghettidev.twodo;


import java.util.Stack;
import java.util.logging.Logger;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;



public class MainActivity extends ActionBarActivity implements OnClickListener, OnFragmentInteractionListener {
	
	private ImageView DONE, TODO;
	private SQLiteDatabase db;
	private dbHelper database;
	private final static Logger log = Logger.getLogger(MainActivity.class.getName());
	
	private ViewFlipper vf;
	private HomeFragment hF;
	private TODOFragment tF;
	private DONEFragment dF;
	
	// Keeps track of your whereabouts through the backstack.
	private Stack<Character> fragStack;
	// 'H' = Home | 'T' = Todo | 'D' = Done
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		hF = new HomeFragment();
		tF = new TODOFragment();
		dF = new DONEFragment();
		
		vf = (ViewFlipper)findViewById(R.id.vf);
		
		TODO = (ImageView)findViewById(R.id.TODO);
		TODO.setOnClickListener(this);
		DONE = (ImageView)findViewById(R.id.DONE);
		DONE.setOnClickListener(this);
		
		fragStack = new Stack<Character>();
		
		// Upon initial creation, load the home fragment
		if (savedInstanceState == null) goHome();
		
		// Open Database
		database = new dbHelper(MainActivity.this);
		Log.d("DB", "Getting a writable database...");
		db = database.getWritableDatabase();
		Log.d("DB", "...writable database gotten!");
		
	}

	@Override
	public void onClick(View source) {

		if (source == TODO) {
			
			if (fragStack.peek() == 'T') {
				
				// Save the note
				String noteText = tF.getNoteText();
				if (!noteText.trim().equals("")) {
					Log.d("NOTE", "Adding note: " + noteText);
					database.addNote(db, noteText.trim());
				}
				
				goHome();
				
			}
			
			else {
				tF = new TODOFragment();
				goTodo();
				
			}
			
		}
		
		if (source == DONE) {
			
			if (fragStack.peek() == 'D') {

				ListView notes = dF.getListView();
				
				for (int i = 0; i < notes.getCount(); i++) {
					CheckBox c =  (CheckBox) ((ViewGroup) notes.getChildAt(i)).getChildAt(0);
					if (c.isChecked()) {
						ContentValues args = new ContentValues();
					    args.put(dbHelper.colIsDone, 1);
					    TextView t =  (TextView) ((ViewGroup) notes.getChildAt(i)).getChildAt(2);
					    db.update(dbHelper.tblNotes, args, dbHelper.colID + "='" + t.getText() + "'", null);
					}
				}
				
				goHome();
			}
			
			else {

				dF = new DONEFragment();
				goDone();
				
			}
			
		}
		
	}
	
	@Override
	public void onBackPressed() {
		if (fragStack.isEmpty()){
			fragStack.add('H');
		}
		log.info("stack before: " + fragStack.toString());
		fragStack.pop();
		log.info("stack after: " + fragStack.toString());
		if (fragStack.isEmpty()) finish();
		else if (fragStack.size() == 1){
			goHome();
			return;
		}
		else if (fragStack.peek() == 'H'){
			goHome();
			return;
		}
		else if (fragStack.peek() == 'T'){
			fragStack.pop();
			goTodo();
			return;
		}
		else if (fragStack.peek() == 'D'){
			fragStack.pop();
			goDone();
			return;
		}
		
	}
	
	private void goHome(){
		
		hideKeyboard();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.f1, hF).commit();
		vf.setDisplayedChild(0);
		TODO.setImageResource(R.drawable.todo2);
		DONE.setImageResource(R.drawable.done2);
		fragStack.removeAllElements();
		fragStack.push('H');
		
	}
	private void goTodo(){
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.f1, tF).commit();
		vf.setDisplayedChild(1);
		TODO.setImageResource(R.drawable.todo2);
		DONE.setImageResource(R.drawable.donegs);
		fragStack.push('T');
		
	}
	private void goDone(){
		
		hideKeyboard();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.f1, dF).commit();
		TODO.setImageResource(R.drawable.todogs);
		DONE.setImageResource(R.drawable.done2);
		vf.setDisplayedChild(2);
		fragStack.push('D');
		
	}
	
	private void hideKeyboard() {
	    if(getCurrentFocus()!=null) {
	        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	    }
	}

	public void onFragmentInteraction(Uri uri) {}

	@Override
	public void onFragmentInteraction(Uri uri, View origin) {}

	@Override
	public void addTaskInList(String text, long listId) {}

	@Override
	public void closeFragment(Fragment fragment) {}
	
	@Override
	protected void onResume() {
		if (fragStack == null) {
			fragStack = new Stack<Character>();
		}
		if (fragStack.isEmpty()){
			fragStack.add('H');
		}
		super.onResume();
	}

}
