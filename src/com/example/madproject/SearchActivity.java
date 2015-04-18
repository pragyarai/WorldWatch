package com.example.madproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONException;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



	public class SearchActivity extends Activity {
		
		String object;
		String objectId;
		EditText searchText;
		String searchString;
		ProgressDialog pd;
		List<News> newsList;
		ListView ListingNews;
		ArrayAdapter<News> adapter;
		ParseUser user;
		String NEWS_API;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_search);
			user = ParseUser.getCurrentUser();
			if(user != null) {
				 objectId = user.getObjectId();
				 Log.d("demo", "objectId"+objectId);
				
		        }
			
			ListingNews=(ListView) findViewById(R.id.newsList);
			DialogFragment addDialog = new SearchDialogBox();
			addDialog.show(getFragmentManager(), "Search News");
		}
		
		class SearchDialogBox extends DialogFragment {


			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				searchText = new EditText(getActivity());

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setView(searchText);
				builder.setMessage("Search news").setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						 searchString = searchText.getText().toString();
						
						 if(user != null) {
							 
							  NEWS_API = "http://ec2-54-191-68-232.us-west-2.compute.amazonaws.com:8080/WorldWatch-1.0/rest/ww/"+objectId+"/search/";
					        }
						 
						if (isEmpty(searchString)) {
							Toast.makeText(getActivity(), "Search field cannot be empty", Toast.LENGTH_LONG).show();
						} else {
							Log.d("demo", "Serach string"+searchString);
							new FetchNews().execute(NEWS_API+searchString);
						
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				return builder.create();
			}
		}
		
		class FetchNews extends AsyncTask<String, Void, List<News>> {

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(SearchActivity.this);
				pd.setTitle("Loading");
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.setMessage("Loading Results...");
				pd.show();
			}

			@Override
			protected List<News> doInBackground(String... params) {
				String in = HttpUtil.getStringFromURL(params[0]);
				try {
					return NewsUtil.NewsJSONParser.parseNews(in);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
				
			}

			@Override
			protected void onPostExecute(List<News> result) {
				newsList = result;				
				ParseObject searchTable = new ParseObject("SearchTable");
				searchTable.put("objectId",objectId );	
				searchTable.put("searchKey", searchString);
				searchTable.put("time",News.max );				
				searchTable.saveInBackground();
				adapter = new ArrayAdapter<News>(SearchActivity.this, android.R.layout.simple_list_item_1, result);
				ListingNews.setAdapter(adapter);
				if(pd != null) {
					pd.dismiss();
				}
			}

		}
		public static boolean isEmpty(String str) {
			if(str == null)
				return true;
			if(str.trim().length() == 0)
				return true;
			return false;
		}
}
