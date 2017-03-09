package cz.bassnick.testa;

import android.location.Address;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
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
        MakeGPSView();
        // example BoundingBox
        // POST https://api.munzee.com/map/boundingbox/
        // 'data={"exclude":"own","limit":3,"fields":"munzee_id,friendly_name,latitude,longitude,original_pin_image,proximity_radius_ft,creator_username", "points":{"box1":{"timestamp": 1304554511,"lat2":50.85229979649992,"lng1":12.919996137208939,"lng2":12.92009802365303,"lat1":50.84729979649992}}}'
        TextView tvLat = (TextView) findViewById(R.id.tvLat);
        TextView tvLong = (TextView) findViewById(R.id.tvLong);
        double ilat = Double.parseDouble(tvLat.getText().toString());
        double iLong = Double.parseDouble(tvLong.getText().toString());

        double lat1 = ilat - 0.0058578;
        double lat2 = ilat + 0.0058578;
        double lng1 = iLong - 0.0096317;
        double lng2 = iLong + 0.0096317;

        String requestData1 = "data";
        String requestData2 = "{\"limit\":999,\"fields\":\"munzee_id,friendly_name,latitude,longitude,original_pin_image,creator_username\",\"points\":{\"box1\":{\"timestamp\": 0,\"lat2\":" + String.valueOf(lat2).replace(',', '.') + ",\"lng1\":" + String.valueOf(lng1).replace(',', '.') + ",\"lng2\":" +String.valueOf(lng2).replace(',', '.') + ",\"lat1\":" + String.valueOf(lat1).replace(',', '.') + "}}}";

        //0.0058578NS
        //0,0096317WE

        // / example GetMunzeeById
        // POST https://api.munzee.com/munzee/
        //'data={"munzee_id":100, "closest":1}'


        ((TextView) (findViewById(R.id.tvCountMunzee))).setText("xxx");
        ((TextView) (findViewById(R.id.tvCountSpecial))).setText("yyy");
        PostTask p = new PostTask();
        p.parametr = requestData2;
        AsyncTask<String, String, String> result = p.execute(null,null,null);


    }
    private class PostTask extends AsyncTask<String, String, String> {
        public String parametr;
        private int numberOfMunzees;
        private int numberOfSpecials;

        @Override
        protected String doInBackground(String... data) {
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                String token = "KiJvZ85biNSZ7R8bR6BkbSdyvtiWfYv8pHL2HX3g";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://api.munzee.com/map/boundingbox/");
                httppost.setHeader("Authorization", "Bearer " + token);
                //nameValuePairs.add(new BasicNameValuePair("data", "{\"username\":\"coolant\"}"));
                nameValuePairs.add(new BasicNameValuePair("data", parametr));
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
            analyzeResult(result);

            ((TextView) (findViewById(R.id.tvCountMunzee))).setText(String.valueOf(numberOfMunzees));
            ((TextView) (findViewById(R.id.tvCountSpecial))).setText(String.valueOf(numberOfSpecials));

        }
        private void analyzeResult(String result)
        {
            numberOfMunzees = 0;
            numberOfSpecials = 0;
            int start = result.indexOf('[',0);
            int startPoleMunzee =result.indexOf('[',start + 1);
            int endPoleMunzee = result.indexOf(']', startPoleMunzee + 1);
            String poleMunzee = result.substring(startPoleMunzee, endPoleMunzee);
            String[] jednotliveMunzee = poleMunzee.split("\\}");
            if (jednotliveMunzee == null || jednotliveMunzee.length == 0)
            {
                return;
            }
            numberOfMunzees = jednotliveMunzee.length;
            for (int i = 0; i < jednotliveMunzee.length; i++)
            {
                /*
                "munzee_id": "830366",
                "friendly_name": "Schönherrpark #3",
                "latitude": "50.8497691154465",
                "longitude": "12.9200291633602",
                "original_pin_image": "http://static.munzee.com/images/pins/munzee.png",
                "creator_username": "BHaus"
                */
                int startMID = jednotliveMunzee[i].indexOf("\"munzee_id\":");
                int endMID = jednotliveMunzee[i].indexOf(",", startMID);
                String MID = jednotliveMunzee[i].substring(startMID, endMID);
                String ID = MID.split("\\:")[1];
                ID = ID.trim().replaceAll("\"", "");
                // todo -> volani funkce na zjisteni jestli hosti specialku
            }

        }
    }
}
