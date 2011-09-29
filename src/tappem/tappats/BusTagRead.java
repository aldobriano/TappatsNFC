package tappem.tappats;


import java.util.ArrayList;

import mobisocial.nfc.NdefHandler;
import mobisocial.nfc.Nfc;
import tappem.tappats.threads.DownloadThread;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.nfc.NdefMessageParser;
import com.example.android.nfc.record.ParsedNdefRecord;

public class BusTagRead extends Activity implements NdefHandler{

	Nfc mNfc;
	static final String TAG = "ViewTag";

	/**
	 * This activity will finish itself in this amount of time if the user
	 * doesn't do anything.
	 */
	static final int ACTIVITY_TIMEOUT_MS = 1 * 1000;
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	Handler handler;
	DownloadThread downloadThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_screen);
		 mNfc = new Nfc(this);
		// mTagContent = (LinearLayout) findViewById(R.id.list);
		//mTitle = (TextView) findViewById(R.id.title);
		
//		mAdapter = NfcAdapter.getDefaultAdapter(this);
//
//        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
//        // will fill in the intent with the details of the discovered tag before delivering to
//        // this activity.
//        mPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//          
		 mNfc.addNdefHandler(this);
		 
		onNewIntent(getIntent());
	}
	
	public int handleNdef(NdefMessage[] ndefMessages) {
		
		if(ndefMessages != null){
		
		
		NdefMessage bluetoothTag = ndefMessages[0];
		Log.d("LIGHTPAD", "NFC TAG handling");
		//NdefRecord[] caca = bluetoothTag.getRecords();
		ArrayList<ParsedNdefRecord> pnd = (ArrayList<ParsedNdefRecord>) NdefMessageParser.parse(bluetoothTag);
		ParsedNdefRecord record = pnd.get(0);
			Uri testing = ((com.example.android.nfc.record.UriRecord) record).getUri();
			
			String query = testing.getEncodedQuery();
			String queryParameter = testing.getQueryParameter("stop_id");
			String path = testing.getPath();
			String authority = testing.getAuthority();
			String host = testing.getHost();
			Log.d("TAPPEM_DEBUG", "Host: " + host);
			Log.d("TAPPEM_DEBUG", "Authority: " + authority);
			Log.d("TAPPEM_DEBUG", "Path: " + path);
			Log.d("TAPPEM_DEBUG", "Query: " + query);
			Log.d("TAPPEM_DEBUG", "QueryParameter: " + queryParameter);
			
			if(path.equals("/tappats/nfc")){
				handleBusStop((String) queryParameter);
		
				return NDEF_CONSUME;
			}else
				return NDEF_PROPAGATE;
		}
			return NDEF_PROPAGATE;
			
	}
//	void resolveIntent(Intent intent) throws IOException {
//		
//		// Parse the intent
//		String action = intent.getAction();
//		System.out.println("ABOUT TO RESOLVE");
//		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
//		{
//			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//			
//			
//			
//			
//			
//           
//			
//			System.out.println("YES!!!  ");
//			System.out.println(new String(tagFromIntent.getId()));
//			System.out.println(asHex(tagFromIntent.getId()));
//			
//			String tagId = asHex(tagFromIntent.getId());
//			
//			
//
//			
//			
//			
//
//			// Create and launch the download thread
//	         downloadThread = new DownloadThread(this);
//	        downloadThread.start();
//	        
//	        // Create the Handler. It will implicitly bind to the Looper
//	        // that is internally created for this thread (since it is the UI thread)
//	        handler = new Handler();
//	        try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//	        downloadThread.enqueueDownload(new NfcTagServerTask(tagId),this);
//			
//			
//			
//			
//			
//		}
//		else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
//			// When a tag is discovered we send it to the service to be save. We
//			// include a PendingIntent for the service to call back onto. This
//			// will cause this activity to be restarted with onNewIntent(). At
//			// that time we read it from the database and view it.
//			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//			
//			System.out.println("BYTE ID ARRAY: " + tagFromIntent.getId());
//			CharSequence text = "BYTE ID ARRAY: " + tagFromIntent.getId();
//			int duration = Toast.LENGTH_SHORT;
//
//			Toast toast = Toast.makeText(this, text, duration);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();
//			finish();
//			
//		
//
//		}
////		else {
////			System.out.println("AQUI");
////			Log.e(TAG, "Unknown intent " + intent);
////			CharSequence text = "Sorry about that!  Can you tap the tag again?";
////			int duration = Toast.LENGTH_SHORT;
////
////			Toast toast = Toast.makeText(this, text, duration);
////			toast.setGravity(Gravity.CENTER, 0, 0);
////			toast.show();
////			finish();
////			return;
////		}
//		
//		
//
//	}

	
      
     

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		try {
			if (mNfc.onNewIntent(this, intent)){ 
			   	  // v.vibrate(100);
			   	   return;
			}
//			resolveIntent(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
    public void onPause() {
        super.onPause();
        mNfc.onPause(this);
   //mAdapter.disableForegroundDispatch(this);
    }
	@Override
    public void onResume() {
        super.onResume();
        try {
			//resolveIntent(getIntent());
        	mNfc.onResume(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 //       mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }


	public static long getLong(byte[] array, int offset) {
	    if(array.length == 4)
	    {
	    	return
		      ((long)(array[offset+3] & 0xff) << 24) |
		      ((long)(array[offset+2] & 0xff) << 16) |
		      ((long)(array[offset+1] & 0xff) << 8) |
		      ((long)(array[offset] & 0xff));
	    }
		if(array.length == 8)
		return
	      ((long)(array[offset]   & 0xff) << 56) |
	      ((long)(array[offset+1] & 0xff) << 48) |
	      ((long)(array[offset+2] & 0xff) << 40) |
	      ((long)(array[offset+3] & 0xff) << 32) |
	      ((long)(array[offset+4] & 0xff) << 24) |
	      ((long)(array[offset+5] & 0xff) << 16) |
	      ((long)(array[offset+6] & 0xff) << 8) |
	      ((long)(array[offset+7] & 0xff));
	    else
	    	return 
	    	((long)(array[offset] & 0xff) << 48) |
		      ((long)(array[offset+1] & 0xff) << 40) |
		      ((long)(array[offset+2] & 0xff) << 32) |
		      ((long)(array[offset+3] & 0xff) << 24) |
		      ((long)(array[offset+4] & 0xff) << 16) |
		      ((long)(array[offset+5] & 0xff) << 8) |
		      ((long)(array[offset+6] & 0xff));
	  }
	 /**
     * converts a byte[] to a hex string
     * @param buf
     * @return
     */
 	public static String asHex (byte buf[]) {
 		StringBuffer strbuf = new StringBuffer(buf.length * 2);
 		int i;

 		for (i = 0; i < buf.length; i++) {
 			if (((int) buf[i] & 0xff) < 0x10)
 				strbuf.append("0");

 			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
 		}

 		return strbuf.toString();
 	}
 	/**
 	 * converts a hex string to a byte []
 	 * @param hex
 	 * @return
 	 */
 	public static byte[] fromHex(String hex) {
 	    int len = hex.length();
 	    byte[] data = new byte[len / 2];
 	    for (int i = 0; i < len; i += 2) {
 	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i+1), 16));
 	    }
 	    return data;
 	}

 	
 	public void handleBusStop(String stopId)
 	{
 		if(stopId == null || stopId.equals("ERROR"))
 		{
 			String text = "Sorry, this tag hasn't been activated yet, use GPS instead! Our team is working on it! Thanks for letting us know.";
			
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(this, text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
 		}else{
 			Intent i = new Intent(this, NextBus.class);
 			i.putExtra(BusAdapter.STOP_ID, stopId);
 			startActivity(i);
 		}
		
		finish();
 	}
 	public void setProgressBarVisible(boolean visible)
	{
		ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
		if(!visible)
			pb.setVisibility(View.GONE);
		else
			pb.setVisibility(View.VISIBLE);
	}
	

	

	
}
