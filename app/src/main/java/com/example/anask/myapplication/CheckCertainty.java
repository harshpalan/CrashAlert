package com.example.anask.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;



public class CheckCertainty extends Activity implements LocationListener {
    private LocationManager lm;
    public double latitude, longitude;
    public String No1, No2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_certainty);
        try {
            File myFile = new File("/sdcard/.emergencyNumbers.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            No1 = myReader.readLine();
            No2 = myReader.readLine();
            myReader.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        final PackageManager pm=getPackageManager();

//        try {
//            Intent waIntent = new Intent(Intent.ACTION_SEND);
//            waIntent.setType("text/plain");
//            String text = "Help! I've met with an accident at http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude); // Replace with your own message.
//
//            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//            //Check if package exists or not. If not then code
//            //in catch block will be called
//            waIntent.setPackage("com.whatsapp");
//
//            waIntent.putExtra(Intent.EXTRA_TEXT, text);
//            startActivity(Intent.createChooser(waIntent, "Share with"));
//
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
//                    .show();
//        }catch(Exception e){
//            e.printStackTrace();
//        }



        final SmsManager sms = SmsManager.getDefault();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMessage(No1, null, "Help! I've met with an accident at http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude), null, null);
                sms.sendTextMessage(No1, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll="+String.valueOf(latitude)+","+String.valueOf(longitude)+"&output=kml", null, null);
                sms.sendTextMessage(No2, null, "Help! I've met with an accident at http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude), null, null);
                sms.sendTextMessage(No2, null, "Nearby Hospitals http://maps.google.com/maps?q=hospital&mrt=yp&sll="+String.valueOf(latitude)+","+String.valueOf(longitude)+"&output=kml", null, null);

                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "Help! I've met with an accident at http://maps.google.com/?q="+String.valueOf(latitude)+","+String.valueOf(longitude); // Replace with your own message.

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
//                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.exit(1);
            }
        }, 15000);

        Button dismiss = (Button) findViewById(R.id.dismissB);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });


    }
    @Override
    public void onLocationChanged(Location location){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Toast.makeText(getApplicationContext(),"Lat and Long extracted",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProviderDisabled(String provider){
    }
    @Override
    public void onProviderEnabled(String provider){
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}