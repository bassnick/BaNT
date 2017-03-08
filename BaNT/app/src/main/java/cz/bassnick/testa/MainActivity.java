package cz.bassnick.testa;

import android.location.Address;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MakeGPSView();


    }

    private void MakeGPSView() {
        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);


        //{
        String stringLatitude = String.valueOf(gpsTracker.latitude);
        textview = (TextView)

                findViewById(R.id.tvLat);

        textview.setText(stringLatitude);

        String stringLongitude = String.valueOf(gpsTracker.longitude);
        textview = (TextView)

                findViewById(R.id.tvLong);

        textview.setText(stringLongitude);



        List<Address> las = gpsTracker.getGeocoderAddress(this);
        Address a = las.get(0);
        try {
            a.getAddressLine(0);
            textview = (TextView) findViewById(R.id.tvAddress);
            textview.setText(a.getAddressLine(0));
        } catch (Exception exc){}

        try {
            a.getAddressLine(1);
            textview = (TextView) findViewById(R.id.tvCity);
            textview.setText(a.getAddressLine(1));
        }catch (Exception exc){}
        try {
            a.getAddressLine(2);
            textview = (TextView) findViewById(R.id.tvCountry);
            textview.setText(a.getAddressLine(2));
        }catch (Exception exc){     }
        //   }

        //else

        //{
        // can't get location
        // GPS or Network is not enabled
        //// Ask user to enable GPS/network in settings
        //    gpsTracker.showSettingsAlert();
        //}

        /*
        *
        49.4670012072511
        17.97093591094017
        *
        */
        //KiJvZ85biNSZ7R8bR6BkbSdyvtiWfYv8pHL2HX3g
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.varna_lab_geo_locations, menu);
        return true;
    }

    //z9UpxHtDErFMCiPGZCDFRE0qBc3f9jD9ZApWn53w
    public void GetCount(View view) {
        // example BoundingBox
        // POST https://api.munzee.com/map/boundingbox/
        // 'data={"exclude":"own","limit":3,"fields":"munzee_id,friendly_name,latitude,longitude,original_pin_image,proximity_radius_ft,creator_username", "points":{"box1":{"timestamp": 1304554511,"lat2":50.85229979649992,"lng1":12.919996137208939,"lng2":12.92009802365303,"lat1":50.84729979649992}}}'

        // example GetMunzeeById
        // POST https://api.munzee.com/munzee/
        //'data={"munzee_id":100, "closest":1}'


        ((TextView) (findViewById(R.id.tvCountMunzee))).setText("xxx");
        ((TextView) (findViewById(R.id.tvCountSpecial))).setText("yyy");
        MakeGPSView();
        PostTask p = new PostTask();
        AsyncTask<String, String, String> result = p.execute(null,null,null);


    }
    private class PostTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                String token = "KiJvZ85biNSZ7R8bR6BkbSdyvtiWfYv8pHL2HX3g";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://api.munzee.com/user/");
                httppost.setHeader("Authorization", "Bearer " + token);
                nameValuePairs.add(new BasicNameValuePair("data", "{\"username\":\"coolant\"}"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                HttpResponse response = httpclient.execute(httppost);
                String result = new BasicResponseHandler().handleResponse(response);
                return result;
            } catch (ClientProtocolException e) {
                return e.getMessage();
            } catch (IOException e) {
                return e.getMessage();
            }
             catch (Throwable t)
            {
                return t.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result)
        {
            // todo
            ((TextView) (findViewById(R.id.tvCountMunzee))).setText(result.substring(0,20));
        }
    }
}
