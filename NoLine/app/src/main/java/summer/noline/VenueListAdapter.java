package summer.noline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import summer.noline.Database.Venue;

/**
 * Created by sumkit on 11/1/16.
 */

public class VenueListAdapter extends ArrayAdapter<Venue> {
    public VenueListAdapter(Context c, ArrayList<Venue> vs){
        super(c,0,vs);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.venue_item, viewGroup, false);
        }

        ImageView image=(ImageView) view.findViewById(R.id.image);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView price = (TextView) view.findViewById(R.id.price);

        if(this.getCount() > 0) {
            Venue venue = this.getItem(i);
            name.setText(venue.getName());
            price.setText("$"+String.format("%.2f", venue.getTicketPrice()));
            new ToBitmapTask(image).execute(venue.getImage());
        }
        return view;
    }

    class ToBitmapTask extends AsyncTask<String, Void, Bitmap> {
        private Bitmap bitmap;
        private ImageView imageView;
        public ToBitmapTask(ImageView iv) {
            this.imageView = iv;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String strUrl = strings[0];
            try {
                URL url = new URL(strUrl);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (MalformedURLException e) {
                Log.e("ToBitmapTask", "to url: "+e.toString());
            } catch (IOException e) {
                Log.e("Venue", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}