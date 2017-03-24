package cz.bassnick.testa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.net.Uri;

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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    TextView textview;
    private String[] temp_munzeeIDs;
    private int lastCountSpecials = 0;
    private int countSpecials = 0;
    private int lastAll = 0;
    private int all = 0;
    public CustomList<String> prgmNameList = new CustomList<String>();
    public CustomList<android.graphics.Bitmap> prgmImages = new CustomList<Bitmap>();

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
        lastCountSpecials = countSpecials;
        lastAll = all;
        countSpecials = 0;
        all = 0;
        MakeGPSView();
        // example BoundingBox
        // POST https://api.munzee.com/map/boundingbox/
        // 'data={"exclude":"own","limit":3,"fields":"munzee_id,friendly_name,latitude,longitude,original_pin_image,proximity_radius_ft,creator_username", "points":{"box1":{"timestamp": 1304554511,"lat2":50.85229979649992,"lng1":12.919996137208939,"lng2":12.92009802365303,"lat1":50.84729979649992}}}'
        TextView tvLat = (TextView) findViewById(R.id.tvLat);
        TextView tvLong = (TextView) findViewById(R.id.tvLong);
        double ilat = Double.parseDouble(tvLat.getText().toString());
        double iLong = Double.parseDouble(tvLong.getText().toString());
        //ilat = 49.4649000093341;
        //iLong = 18.083184119314;
        ilat = 49.47808895192653;
        iLong = 17.97852884978056;
        double lat1 = ilat -/* 0.0009;*/0.0058578;
        double lat2 = ilat + /*0.0009;*/0.0058578;
        double lng1 = iLong -/* 0.0009;*/0.0096317;
        double lng2 = iLong +/* 0.0009;*/0.0096317;

        String requestData1 = "data";
        String requestData2 = "{\"limit\":999,\"fields\":\"munzee_id\",\"points\":{\"box1\":{\"timestamp\": 0,\"lat2\":" + String.valueOf(lat2).replace(',', '.') + ",\"lng1\":" + String.valueOf(lng1).replace(',', '.') + ",\"lng2\":" +String.valueOf(lng2).replace(',', '.') + ",\"lat1\":" + String.valueOf(lat1).replace(',', '.') + "}}}";

        //0.0058578NS
        //0,0096317WE

        // / example GetMunzeeById
        // POST https://api.munzee.com/munzee/
        //'data={"munzee_id":100, "closest":1}'


        ((TextView) (findViewById(R.id.tvCountMunzee))).setText("-");
        ((TextView) (findViewById(R.id.tvCountSpecial))).setText("-");
        PostTask p = new PostTask();
        p.action = 0;
        p.parametr = requestData2;
        AsyncTask<String, String, String> result = p.execute(null,null,null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Button btn = (Button)findViewById(R.id.btnGetCount);
                btn.performClick();
            }
        }, 480000);
    }

    public void countSpecial()
    {
        if (temp_munzeeIDs != null && temp_munzeeIDs.length > 0)
            for (String mid:temp_munzeeIDs) {
                PostTask x = new PostTask();
                x.action = 1;
                x.parametr = mid;
                AsyncTask<String, String, String> result2 = x.execute(null,null,null);
            }
    }

    public void ShowFounded() {
        ListView lv = (ListView) findViewById(R.id.listView);
        if (prgmNameList != null && prgmImages != null)
        {
            String[] names = new String[prgmNameList.size];
            android.graphics.Bitmap[] images = new Bitmap[prgmImages.size];
            for (int i = 0; i < prgmNameList.size; i++)
            {
                names[i] = prgmNameList.get(i);
            }
            for (int j = 0; j < prgmImages.size; j++)
            {
                images[j] = prgmImages.get(j);
            }
            lv.setAdapter(new CustomAdapter(this, names,images));
        }
    }

    class DownloadImage extends AsyncTask<String, String, String> {

        private Exception exception;
        private android.graphics.Bitmap bitmapStream;
        private String pinIconUrl;
        private String description;

        @Override
        protected String doInBackground(String... urls) {
            try {
                //URL url = new URL(urls[0]);
                //String pinTest = "https://munzee.global.ssl.fastly.net/images/pins/faun.png";
                String pinTest = pinIconUrl.replace("\\", "");
                //imageView1 = (ImageView) findViewById(R.id.image1);


                URL url = null;
                try {
                    url = new URL(pinTest);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                //imageView1 = (ImageView) findViewById(R.id.image1);
                // imageView1.setImageURI(android.net.Uri.parse(url.toString()));

                try {
                    bitmapStream = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                  //  imageView1.setImageBitmap(is);
                } catch (Throwable t) {
                    t.printStackTrace();
                }


            } catch (Exception e) {
                this.exception = e;

                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String icon) {
            MainActivity.this.prgmImages.add(bitmapStream);
            MainActivity.this.prgmNameList.add(description);
            MainActivity.this.ShowFounded();

            //ImageView imageView1 = (ImageView) findViewById(R.id.image1);
            //imageView1.setImageBitmap(bitmapStream);
        }
    }

    private class PostTask extends AsyncTask<String, String, String> {
        public String parametr;
        private int action;
        private int numberOfMunzees;
        private int numberOfSpecials;

        @Override
        protected String doInBackground(String... data) {
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                String token = "EX6igmDMApYsG6uwYZWxwO5vxWQTFN14OoepK4oW";
                HttpPost httppost = null;
                HttpClient httpclient = new DefaultHttpClient();
                if (action == 0) {
                    httppost = new HttpPost("https://api.munzee.com/map/boundingbox/");
                    httppost.setHeader("Authorization", "Bearer " + token);
                    nameValuePairs.add(new BasicNameValuePair("data", parametr));
                }
                if (action == 1) {
                    httppost = new HttpPost("https://api.munzee.com/munzee/");
                    httppost.setHeader("Authorization", "Bearer " + token);
                    nameValuePairs.add(new BasicNameValuePair("data", "{\"munzee_id\": " + parametr +", \"closest\":0}"));
                }
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
            if (action == 0)
            {
                analyzeResult(result);
                ((TextView) (findViewById(R.id.tvCountMunzee))).setText(String.valueOf(numberOfMunzees) + " (minule: " + String.valueOf(lastAll)+") munzee v oblasti +-665m.");
                MainActivity.this.prgmNameList = new CustomList<String>();
                MainActivity.this.prgmImages = new CustomList<Bitmap>();
                countSpecial();
            }
            if (action == 1) {
                analyzeOrDisplayMunzee(result);
                ((TextView) (findViewById(R.id.tvCountSpecial))).setText(String.valueOf(all) + " prozkoumaných munzee.\r\n"+ String.valueOf(countSpecials) + " (minule: " + String.valueOf(lastCountSpecials) +") nalezených speciálek");
            }

        }

        private void analyzeOrDisplayMunzee(String result) {
            int startUntil = result.indexOf("\"special_good_until\":");
            if (startUntil > 0) {
                countSpecials++;
                int endUntil = result.indexOf(",", startUntil);
                String stimestamp = result.substring(startUntil, endUntil).split("\\:")[1].trim().replace("\"", "");
                long timestamp = Long.parseLong(stimestamp);
                Date time = new Date(timestamp * 1000);
                DateFormat df = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                String datumexpirace = df.format(time);
               // TextView where1 = (TextView) findViewById(R.id.where1);
               // where1.setText(datumexpirace);

                int startPinIcon = result.indexOf("\"pin_icon\":");
                int endPinIcon = result.indexOf(",", startPinIcon);
                String pinIconUrl = result.substring(startPinIcon + "\"pin_icon\":".length(), endPinIcon).trim().replace("\"", "");

                DownloadImage di = new DownloadImage();
                di.pinIconUrl = pinIconUrl;
                di.description = datumexpirace;
                AsyncTask<String, String, String> resultImage = di.execute(null,null,null);
            }
            all++;
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
            temp_munzeeIDs = new String[numberOfMunzees];
            for (int i = 0; i < jednotliveMunzee.length; i++)
            {
                String ID = jednotliveMunzee[i].split("\\:")[1];
                ID = ID.trim().replaceAll("\"", "");
                temp_munzeeIDs[i] = ID;
            }

        }
    }
}

