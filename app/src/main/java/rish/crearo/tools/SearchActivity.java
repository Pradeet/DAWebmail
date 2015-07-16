package rish.crearo.tools;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


//Not needed
public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String search_requested = intent
                    .getStringExtra(SearchManager.QUERY);
            System.out.println(search_requested);
            Toast.makeText(getApplicationContext(),
                    "search - " + search_requested, Toast.LENGTH_SHORT).show();
        }
    }
}

