package com.example.gpstracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.content.Context;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Button;

public class MainActivity extends Activity {
	private LocationManager locManager;
	private LocationListener locListener;
  
	//private Button btnActualizar;
	//private Button btnDesactivar;
	private TextView lblLatitud; 
	private TextView lblLongitud;
	private TextView lblPrecision;
	private TextView lblEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          
        //btnActualizar = (Button)findViewById(R.id.BtnActualizar);
        //btnDesactivar = (Button)findViewById(R.id.BtnDesactivar);
        lblLatitud = (TextView)findViewById(R.id.LblPosLatitud);
        lblLongitud = (TextView)findViewById(R.id.LblPosLongitud);
        lblPrecision = (TextView)findViewById(R.id.LblPosPrecision);
        lblEstado = (TextView)findViewById(R.id.LblEstado);
    }

    public void onClick(View view) {
		comenzarLocalizacion();
	}

    public void onClick2(View v) {
	   	locManager.removeUpdates(locListener);
	}

    private void comenzarLocalizacion()
    {
    	//Obtenemos una referencia al LocationManager
    	locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	//locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
        //        2000,10, locListener);
    	//Obtenemos la última posición conocida
    	Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	//Mostramos la última posición conocida
    	mostrarPosicion(loc);
    	//Nos registramos para recibir actualizaciones de la posición
    	Log.i("", "ComenzarLocalizacion: ");
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		lblEstado.setText("Provider OFF");
	    	}
	    	public void onProviderEnabled(String provider){
	    		lblEstado.setText("Provider ON ");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		lblEstado.setText("Provider Status: " + status);
	    	}
    	};
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);
    }
    
    
    private void mostrarPosicion(Location loc) {
    	Log.i("", "mostrarPosicion: ");
    	if(loc != null)
    	{
    		lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
    		lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
    		lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
    		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    	}
    	else
    	{
    		lblLatitud.setText("Latitud: (sin_datos)");
    		lblLongitud.setText("Longitud: (sin_datos)");
    		lblPrecision.setText("Precision: (sin_datos)");
    	}
    }
}