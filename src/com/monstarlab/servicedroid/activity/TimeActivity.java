package com.monstarlab.servicedroid.activity;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.monstarlab.servicedroid.R;
import com.monstarlab.servicedroid.model.Models.TimeEntries;
import com.monstarlab.servicedroid.util.TimeUtil;

public class TimeActivity extends ListActivity {
	
	public static final int INSERT_ID = Menu.FIRST;
	public static final int START_ID = Menu.FIRST + 1;
	public static final int STOP_ID = Menu.FIRST + 2;
	private static final int EDIT_ID = Menu.FIRST + 3;
	private static final int DELETE_ID = Menu.FIRST + 4;
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private static final String TIMER = "Timer";
    
    private static final String[] PROJECTION = new String[] { TimeEntries._ID, TimeEntries.LENGTH, TimeEntries.DATE };
    
    
	private TimeUtil mTimeHelper;
	private Cursor mCursor;
	private Boolean mTiming = false;
	private Long mTimerStart;
	private Handler mTimer = new Handler();
	private TextView mTimerView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Intent intent = getIntent();
		if(intent.getData() == null) {
			intent.setData(TimeEntries.CONTENT_URI);
		}
		
		//try to recover the Timer
		if(savedInstanceState != null) {
			mTimerStart = savedInstanceState.getLong(TIMER);
			if(mTimerStart != null) {
				mTiming = true;
			}
		}
		
		setView();
		
		
		mTimeHelper = new TimeUtil(this);
        
		fillData();
		
		registerForContextMenu(getListView());
	}
	
	public void fillData() {
		 // Get all of the entries from the database and create the item list
		mCursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);

        String[] from = new String[] { TimeEntries.DATE, TimeEntries.LENGTH };
        int[] to = new int[] { R.id.date, R.id.length };
 
        // Now create an cursor adapter and set it to display using our row
        // overriding setViewText to format the Date string
        SimpleCursorAdapter entries = new SimpleCursorAdapter(this, R.layout.time_row, mCursor, from, to) {
        	
        	@Override
        	public void setViewText(TextView v, String text) {
        		if(v.getId() == R.id.date){
					text = mTimeHelper.normalizeDate(text);
        			v.setText(text);
        		} else if(v.getId() == R.id.length) {
        			if(TextUtils.isEmpty(text)) {
        				text = "0";
        			}
        			
        			text = TimeUtil.toTimeString(Integer.parseInt(text));
        			
        			v.setText(text);
        			
        		} else {
        			super.setViewText(v, text);
        		}
        		
        	}
        	
        };
        
        setListAdapter(entries);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(0, INSERT_ID, 1, R.string.add_time)
			.setShortcut('3', 'a')
			.setIcon(android.R.drawable.ic_menu_add);
		
		//Start/Stop Time happens onPrepare
		
				
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		//menu depends on if user has start Service Timer
		if(mTiming) {
			menu.removeItem(START_ID);
			if(menu.findItem(STOP_ID) == null) {
				menu.add(0, STOP_ID, 2, R.string.stop_time).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
			}
		} else {
			menu.removeItem(STOP_ID);
			if(menu.findItem(START_ID) == null) {
				menu.add(0, START_ID, 2, R.string.start_time).setIcon(android.R.drawable.ic_menu_recent_history);
			}
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case INSERT_ID:
			//this.startActivity(new Intent(Intent.ACTION_INSERT, this.getIntent().getData()));
			createEntry();
			return true;
		case START_ID:
			startTimer();
			return true;
		case STOP_ID:
			stopTimer();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//menu.setHeaderTitle(title);
		menu.add(0, EDIT_ID, 0, R.string.edit);
		menu.add(0, DELETE_ID, 0, R.string.delete_time);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
		
		case EDIT_ID:
			editEntry(info.id);
			return true;
		
		case DELETE_ID:
			
			deleteEntry(info.id);
			return true;
		}
		
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		editEntry(id);
	}
	
	private void editEntry(long id) {
		Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
		Intent i = new Intent(Intent.ACTION_EDIT, uri, this, TimeEditActivity.class);
        startActivity(i);
	}
	
	private void createEntry() {
		Intent i = new Intent(Intent.ACTION_INSERT, getIntent().getData(), this, TimeEditActivity.class);
		startActivity(i);
	}
	
	private void deleteEntry(long id) {
		Uri entryUri = ContentUris.withAppendedId(getIntent().getData(), id);
		getContentResolver().delete(entryUri, null, null);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mTimerStart != null) {
			outState.putLong(TIMER, mTimerStart);
		}
	}
	
	private void startTimer() {
		mTiming = true;
		mTimerStart = SystemClock.uptimeMillis();
		setView();
	}
	
	private void stopTimer() {
		if(!mTiming) return;
		
		
		long timerEnd = SystemClock.uptimeMillis();
		long diff = timerEnd - mTimerStart;
		mTiming = false;
		mTimerStart = null;
		
		//mTimeAdapter.create((int)diff / 1000);
		ContentValues values = new ContentValues();
		values.put(TimeEntries.LENGTH, (int) diff / 1000);
		getContentResolver().insert(getIntent().getData(), values);
		setView();
		fillData();
	}
	
	private void setView() {
		if(mTiming) {
			setContentView(R.layout.time_timer);
			
			mTimerView = (TextView)findViewById(R.id.timer);
			
			mTimer.removeCallbacks(mTimerUpdateTask);
			mTimer.postDelayed(mTimerUpdateTask, 100);
		} else {
			setContentView(R.layout.time);
			mTimer.removeCallbacks(mTimerUpdateTask);
		}
	}
	
	private Runnable mTimerUpdateTask = new Runnable() {

		@Override
		public void run() {
			final long start = mTimerStart;
		    long current = SystemClock.uptimeMillis();
			long millis =  current - start;
		    
			
		    String text = TimeUtil.toTimeString((int) millis / 1000);
			mTimerView.setText(text);
			
			mTimer.postAtTime(this, current + (1000 * 15));
		}
		
	};
}
