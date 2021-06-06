package com.example.gpstracker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.util.Log;
import android.content.Context;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
//import android.widget.Toast;
import java.io.IOException;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {
	private LocationManager locManager;
	private LocationListener locListener;
	//private Button btnActualizar;
	//private Button btnDesactivar;
	private TextView lblLatitud; 
	private TextView lblLongitud;
	private TextView lblPrecision;
	private TextView lblEstado;
	private TextView lblDatosGrabados;
	String tag = "GpsTracker";
	int datos_grabados = 0;
	private Calendar calendar; 
	private SimpleDateFormat dateFormat; 
	private String date; 
	private static final String FILE_NAME = "gps.csv";
	private String file = Environment.getExternalStorageDirectory().getPath() + "/gps/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblLongitud = (TextView)findViewById(R.id.LblPosLongitud);
        lblLatitud = (TextView)findViewById(R.id.LblPosLatitud);
        lblEstado = (TextView)findViewById(R.id.LblEstado);
        lblPrecision = (TextView)findViewById(R.id.LblPosPrecision);
        lblDatosGrabados =  (TextView)findViewById(R.id.lblDatos);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy-hhmmss");
        calendar = Calendar.getInstance();
        date = dateFormat.format(calendar.getTime());
        
        Log.d(tag, "Inthe onCreate() event:" + date);
    	try {
			CreateDirectory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Log.d(tag, "Inthe onCreate() Creando archivo:" + date);
        try {
            FileOutputStream fileinput = new FileOutputStream(file +  date +  FILE_NAME);
            PrintStream printstream = new PrintStream(fileinput);
            printstream.print("Latitude,Longitude,Altitude,Time\n");
            fileinput.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
 
    public void CreateDirectory() throws IOException {
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/gps");
        if (!directory.exists()) {
        	Log.d(tag, "Creating the directory "+ Environment.getExternalStorageDirectory().getPath() + "/gps");
       		directory.mkdir();
        }
    }

    public void save(String txtLatitud, String txtLongitud, String txtAltitude) {
    	//Log.d(tag, "saving:" + txtLongitud + " - " + txtLatitud);
    	//Log.d(tag, "saving to:"+ Environment.getExternalStorageDirectory().getPath());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        calendar = Calendar.getInstance();
        String tmpdate = dateFormat.format(calendar.getTime());
        try {
            OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(file +  date +  FILE_NAME,true));
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            buffered_writer.write(txtLatitud + "," + txtLongitud + "," + txtAltitude + "," + tmpdate + "\n");
            buffered_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        datos_grabados = datos_grabados + 1;
        lblDatosGrabados.setText("Datos: " + datos_grabados);
    }

    public void onRestart() {
    	super.onRestart();
    	Log.d(tag, "Inthe onRestart() event");
    }
    public void onResume() {
    	super.onResume();
    	Log.d(tag, "Inthe onResume() event");
    }
    public void onPause() {
    	super.onPause();
        Log.d(tag, "Inthe onPause() event");
    }
    public void onStop() {
    	super.onStop();
        Log.d(tag, "Inthe onStop() event");
    }
    public void onDestroy() {
    	super.onDestroy();
        Log.d(tag, "Inthe onDestroy() event");
    }
    public void onClick(View view) {
		comenzarLocalizacion();
	}

    public void onClick_salir(View v) {
	   	locManager.removeUpdates(locListener);
	    finish();
        System.exit(0);
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
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		Log.d(tag, "Inthe onLocationChanged() event");
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		Log.d(tag, "Inthe onProviderDisabled() event");
	    		lblEstado.setText("Provider OFF");
	    	}
	    	public void onProviderEnabled(String provider){
	    		Log.d(tag, "Inthe onProviderEnabled() event");
	    		lblEstado.setText("Provider ON ");
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.d(tag, "Inthe onStatusChanged() event");
	    		lblEstado.setText("Provider Status: " + status);
	    	}
    	};
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);
    }
    
    
    private void mostrarPosicion(Location loc) {
    	if(loc != null)
    	{
        	String str_latitud = String.valueOf(loc.getLatitude());
        	String str_longitud = String.valueOf(loc.getLongitude());
        	String str_altitude = String.valueOf(loc.getAltitude());
    		lblLongitud.setText("Longitud: " + str_longitud);
        	lblLatitud.setText("Latitud: " + str_latitud);
    		lblPrecision.setText("Precision: " + String.valueOf(loc.getAccuracy()));
    		Log.i("mostrarPosicion:", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
    		save(str_latitud, str_longitud, str_altitude);
    	} else {
    		lblLongitud.setText("Longitud: (sin_datos)");
    		lblLatitud.setText("Latitud: (sin_datos!)");
    		lblPrecision.setText("Precision: (sin_datos)");
    		save("null","null","null");
    	}
    }
}