package summer.noline.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import summer.noline.Database.Venue;
import summer.noline.R;
import summer.noline.VenueListAdapter;

public class MainActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private ListView listView;
    private ArrayList<Venue> venues = new ArrayList<Venue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);

        final String imageUrl = "https://api.learn2crack.com/android/images/donut.png";
        final VenueListAdapter venueListAdapter = new VenueListAdapter(this, venues);
        listView.setAdapter(venueListAdapter);

        Venue v = new Venue("Club 323", imageUrl, 20, "4614 Fifth Avenue, Pittsburgh, PA");
        venues.add(v);
        venueListAdapter.add(v);
        venueListAdapter.notifyDataSetChanged();

        actionBar = this.getSupportActionBar();
        actionBar.setTitle("Tickets");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ticket:
                return true;

            case R.id.qr_code:
                Intent toQR = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(toQR);
                return true;
            case R.id.profile:
                Intent toPro = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(toPro);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
