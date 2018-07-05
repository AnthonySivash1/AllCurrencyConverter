package com.example.margarita.allcurrencyconverter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.margarita.allcurrencyconverter.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;

    private TextView mUrlDisplayTextView;

    private TextView mSearchResultsTextView;
    private EditText newedittext;
    private Spinner mySpinner;
    private Spinner mySpinner2;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);


        //newedittext=(EditText) findViewById(R.id.secondparam) ;

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
       // mySpinner


        //first spinner
         mySpinner=(Spinner) findViewById(R.id.spinner1);
         mySpinner2=(Spinner) findViewById(R.id.spinner2);
//setting the first spinner
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));

        myAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mySpinner.setAdapter(myAdapter);

        //setting second spinner
        ArrayAdapter<String> myAdapter2=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));

        myAdapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mySpinner2.setAdapter(myAdapter2);


    }




    /**
     * This method retrieves the search text from the EditText, constructs
     * the URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our (not yet created) {@link //GithubQueryTask}
     */
    private void makeGithubSearchQuery() {
       // String githubQuery = mSearchBoxEditText.getText().toString();
      String githubQuery = mySpinner.getSelectedItem().toString();//toString();
     // String newparam = newedittext.getText().toString();

        String newparam=mySpinner2.getSelectedItem().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery, newparam);

       //mUrlDisplayTextView.setText(githubSearchUrl.toString());


        new GitHubQueryTask().execute(githubSearchUrl);

    }


    public class GitHubQueryTask extends AsyncTask<URL, Void, String>
    {
        private MainActivity j;
       // String s= newedittext.toString();
        @Override
        protected String doInBackground(URL... url) {
            // String t=null;

            URL searchURL=url[0];
            String githubSearchResults= null;
            try{
                githubSearchResults=NetworkUtils.getResponseFromHttpUrl(searchURL);





            }
            catch (Exception e){
                e.printStackTrace();
            }
            return githubSearchResults;
            // return s;
        }

        @Override
        protected void onPostExecute(String s) {

            if(s!=null&&!s.equals(""))
            {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject test = jsonObject.getJSONObject("rates");
                    //return test.getString("EUR");
                    String z= mSearchBoxEditText.getText().toString();
                    double test1=Double.parseDouble(z);
                    String t= test.getString(mySpinner2.getSelectedItem().toString());
                    double test2=Double.parseDouble(t);
                    double test3=test1 * test2;
                    // String t=test.getString("EUR");
                   // s=t;
                    mSearchResultsTextView.setText(Double.toString(test3));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            super.onPostExecute(s);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
