/*
 * PinDroid - http://code.google.com/p/PinDroid/
 *
 * Copyright (C) 2010 Matt Schmidt
 *
 * PinDroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * PinDroid is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PinDroid; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package com.pindroid.activity;

import com.pindroid.R;
import com.pindroid.Constants;
import com.pindroid.providers.BookmarkContentProvider;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

public class Main extends AppBaseListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedState);
		
		setContentView(R.layout.main);
		
		String[] MENU_ITEMS = new String[] {getString(R.string.main_menu_my_bookmarks),
				getString(R.string.main_menu_my_unread_bookmarks),
				getString(R.string.main_menu_my_tags),
				getString(R.string.main_menu_network_bookmarks),
				getString(R.string.main_menu_recent_bookmarks)};

		setListAdapter(new ArrayAdapter<String>(this, R.layout.main_view, MENU_ITEMS));

		Intent intent = getIntent();

		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			if(intent.hasExtra(SearchManager.QUERY)){
				Intent i = new Intent(mContext, MainSearchResults.class);
				i.putExtras(getIntent().getExtras());
				startActivity(i);
				finish();
			} else {
				onSearchRequested();
			}
		} else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
			
			Uri data = intent.getData();
			String path = null;
			String tagname = null;
			
			if(data != null) {
				path = data.getPath();
				tagname = data.getQueryParameter("tagname");
			}
			
			if(data.getScheme() == null || !data.getScheme().equals("content")){
				Intent i = new Intent(Intent.ACTION_VIEW, data);
				
				startActivity(i);
				finish();				
			} else if(path.contains("bookmarks") && TextUtils.isDigitsOnly(data.getLastPathSegment())) {
				Intent viewBookmark = new Intent(this, ViewBookmark.class);
				viewBookmark.setData(data);
				
				Log.d("View Bookmark Uri", data.toString());
				startActivity(viewBookmark);
				finish();
			} else if(tagname != null) {
				Intent viewTags = new Intent(this, BrowseBookmarks.class);
				viewTags.setData(data);
				
				Log.d("View Tags Uri", data.toString());
				startActivity(viewTags);
				finish();
			}
		}
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	if(position == 0){
		    		
		    		Intent i = new Intent();
		    		i.setAction(Intent.ACTION_VIEW);
		    		i.addCategory(Intent.CATEGORY_DEFAULT);
		    		Uri.Builder data = new Uri.Builder();
		    		data.scheme(Constants.CONTENT_SCHEME);
		    		data.encodedAuthority(mAccount.name + "@" + BookmarkContentProvider.AUTHORITY);
		    		data.appendEncodedPath("bookmarks");
		    		i.setData(data.build());
		    		
		    		Log.d("uri", data.build().toString());
		    		
		    		startActivity(i);
		    	} else if(position == 1){
		    		Intent i = new Intent();
		    		i.setAction(Intent.ACTION_VIEW);
		    		i.addCategory(Intent.CATEGORY_DEFAULT);
		    		Uri.Builder data = new Uri.Builder();
		    		data.scheme(Constants.CONTENT_SCHEME);
		    		data.encodedAuthority(mAccount.name + "@" + BookmarkContentProvider.AUTHORITY);
		    		data.appendEncodedPath("bookmarks");
		    		data.appendQueryParameter("unread", "1");
		    		i.setData(data.build());
		    		
		    		Log.d("uri", data.build().toString());
		    		
		    		startActivity(i);
		    	} else if(position == 2){
		    		Intent i = new Intent();
		    		i.setAction(Intent.ACTION_VIEW);
		    		i.addCategory(Intent.CATEGORY_DEFAULT);
		    		Uri.Builder data = new Uri.Builder();
		    		data.scheme(Constants.CONTENT_SCHEME);
		    		data.encodedAuthority(mAccount.name + "@" + BookmarkContentProvider.AUTHORITY);
		    		data.appendEncodedPath("tags");
		    		i.setData(data.build());
		    		
		    		Log.d("uri", data.build().toString());
		    		
		    		startActivity(i);
		    	} else if(position == 3){
		    		
		    		Intent i = new Intent();
		    		i.setAction(Intent.ACTION_VIEW);
		    		i.addCategory(Intent.CATEGORY_DEFAULT);
		    		Uri.Builder data = new Uri.Builder();
		    		data.scheme(Constants.CONTENT_SCHEME);
		    		data.encodedAuthority("network@" + BookmarkContentProvider.AUTHORITY);
		    		data.appendEncodedPath("bookmarks");
		    		i.setData(data.build());
		    		
		    		Log.d("uri", data.build().toString());
		    		
		    		startActivity(i);
		    	} else if(position == 4){
		    		
		    		Intent i = new Intent();
		    		i.setAction(Intent.ACTION_VIEW);
		    		i.addCategory(Intent.CATEGORY_DEFAULT);
		    		Uri.Builder data = new Uri.Builder();
		    		data.scheme(Constants.CONTENT_SCHEME);
		    		data.encodedAuthority("recent@" + BookmarkContentProvider.AUTHORITY);
		    		data.appendEncodedPath("bookmarks");
		    		i.setData(data.build());
		    		
		    		Log.d("uri", data.build().toString());
		    		
		    		startActivity(i);
		    	}
		    }
		});
	}
}