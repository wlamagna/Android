package net.learn2develop.HelloWorld;
import android.app.Activity;

import android.util.Log;
import android.view.Window;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
	String tag = "Lifecycle";
	CharSequence[] items = { "Google", "Apple", "Microsoft" };
	boolean[] itemsChecked = new boolean [items.length];
	ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Log.d(tag, "In the onCreate() event");
    }

    public void onStart()
    {
    	super.onStart();
    	Log.d(tag, "In the onStart() event");
    }

    public void onRestart()
    {
    super.onRestart();
    Log.d(tag, "In the onRestart() event");
    }

    
    public void onResume()
    {
    super.onResume();
    Log.d(tag, "In the onResume() event");
    }
    public void onPause()
    {
    super.onPause();
    Log.d(tag, "In the onPause() event");
    }

    public void onStop() {
    	super.onStop();
    	Log.d(tag, "In the onStop() event");
    }
    public void onDestroy() {
    	super.onDestroy();
    	Log.d(tag, "In the onDestroy() event");
    }

    public void onClick(View v) {
    	showDialog(0);
    }

    public void onClick2(View v) {
    	//---show the dialog---
    	final ProgressDialog dialog = ProgressDialog.show(
    	this, "Doing something", "Please wait...", true);
    	new Thread(new Runnable(){
    		public void run(){
    			try {
	    			//---simulate doing something lengthy---
	    			Thread.sleep(5000);
	    			//---dismiss the dialog---
	    			dialog.dismiss();
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    	}).start();
   	}
    
    
    public void onClick3(View v) {
   	
    	showDialog(1);
    	progressDialog.setProgress(0);
    	new Thread(new Runnable(){
    		public void run(){
    			for (int i=1; i<=15; i++) {
    				try {
    					//---simulate doing something lengthy---
    					Thread.sleep(1000);
    					//---update the dialog---
    					progressDialog.incrementProgressBy((int)(100/15));
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    			progressDialog.dismiss();
    		}
    	}).start();
    }

    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
	    case 0:
		    Builder builder = new AlertDialog.Builder(this);
		    builder.setIcon(R.drawable.ic_launcher);
		    builder.setTitle("This is a dialog with some simple text...");
		    builder.setPositiveButton("OK",
		    		new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int whichButton) {
		    				Toast.makeText(getBaseContext(),
		    				"OK clicked!", Toast.LENGTH_SHORT).show();
		    			}
		    		}
		    );
		    builder.setNegativeButton("Cancel",
	    		new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int whichButton) {
	    				Toast.makeText(getBaseContext(),
	    					"Cancel clicked!", Toast.LENGTH_SHORT).show();
	    			}
	    		}
		    );
		    builder.setMultiChoiceItems(items, itemsChecked,
	    		new DialogInterface.OnMultiChoiceClickListener() {
	    			public void onClick(DialogInterface dialog,
	    					int which, boolean isChecked) {
	    					Toast.makeText(getBaseContext(),
	    						items[which] + (isChecked ? " checked!":" unchecked!"),
	    						Toast.LENGTH_SHORT).show();

	    					}
	    		}
		    );
		    return builder.create();
	    case 1:
	    	progressDialog = new ProgressDialog(this);
	    	progressDialog.setIcon(R.drawable.ic_launcher);
	    	progressDialog.setTitle("Downloading files...");
	    	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

	    	progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
	    	new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog,
		    	int whichButton) {
		    		Toast.makeText(getBaseContext(),
		    				"OK clicked!", Toast.LENGTH_SHORT).show();
		    	}
	    	});
	    	progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
	    			new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog,
		    			int whichButton) {
			    			Toast.makeText(getBaseContext(),
			    			"Cancel clicked!", Toast.LENGTH_SHORT).show();
	    				}
	    	});
	    	return progressDialog;
    	}
    	return null;
    }
}
