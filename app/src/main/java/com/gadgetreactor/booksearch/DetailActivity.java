package com.gadgetreactor.booksearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ASUS on 19/12/2014.
 */
public class DetailActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
    String mImageURL;
    ShareActionProvider mShareActionProvider;
    ImageButton mainButton;
    ProgressDialog mDialog;
    JSONAdapter mJSONBookAdapter;
    String readUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail);

        mainButton = (ImageButton) findViewById(R.id.read_button);

        mainButton.setOnClickListener(this);
        // TODO: add any other data you'd like as Extras

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading");
        mDialog.setCancelable(true);
        // Access the imageview from XML
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);

        // unpack the coverID from its trip inside your Intent
        String coverID = this.getIntent().getExtras().getString("coverID");
        String title = this.getIntent().getExtras().getString("title");
        readUrl = this.getIntent().getExtras().getString("link");
        // queryBook(link);

        // See if there is a valid coverID
        if (coverID.length() > 0) {

            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";

            // Use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.bookloading).into(imageView);
        } else {

            // If there is no cover ID in the object, use a placeholder
            imageView.setImageResource(R.drawable.bookcover);
        }

        if (readUrl.length() == 0) {
            mainButton.setVisibility(View.GONE);
        }

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(title);
    }

    private void setShareIntent() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Book Recommendation!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageURL);

        // Make sure the provider knows
        // it should work with that Intent
        // mShareActionProvider.setShareIntent(shareIntent);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_item_share) {
            setShareIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        // create an Intent to take you over to a new DetailActivity
        Intent readIntent = new Intent(this, ReadActivity.class);
        readIntent.putExtra("readUrl", readUrl);
// start the next Activity using your prepared Intent
        startActivity(readIntent);
    }

    private void queryBook(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "?bibkeys=OLID:"+ searchString +"&jscmd=data&callback=success";
        try {
            urlString = URLEncoder.encode(urlString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        String BOOK_URL = "http://openlibrary.org/api/books" + urlString;
        // Create a client to perform networking
        AsyncHttpClient client2 = new AsyncHttpClient();
        client2.setTimeout(1);
        // Show ProgressDialog to inform user that a task in the background is occurring
        mDialog.show();
        // Have the client get a JSONArray of data
        // and define how to respond
        RequestHandle bookread = client2.get(BOOK_URL,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject2) {
                        mDialog.dismiss();
                        // Display a "Toast" message
                        // to announce your success

                        Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                        // update the data in your custom method.
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        mDialog.dismiss();
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

}
