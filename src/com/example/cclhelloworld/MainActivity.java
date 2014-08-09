/*
 * Copyright (C) 2014 Google Inc. All Rights Reserved. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.cclhelloworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.common.api.Status;
import com.google.sample.castcompanionlibrary.cast.DataCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.DataCastConsumerImpl;

import java.util.ArrayList;


/**
 * Main activity to send messages to the receiver.
 */
public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int REQUEST_CODE = 1;

   // cast manager
   private static DataCastManager mCastMgr;

   // custom data channel
	private static HelloWorldChannel mHelloWorldChannel;

   private static String APPLICATION_ID;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));

      APPLICATION_ID = getResources().getString(R.string.app_id);

      // create data channel
      mHelloWorldChannel = new HelloWorldChannel();

      // initialize the cast manager
      createCastManager(getApplicationContext());

		// When the user clicks on the button, use Android voice recognition to
		// get text
		Button voiceButton = (Button) findViewById(R.id.voiceButton);
		voiceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startVoiceRecognitionActivity();
			}
		});
	}

   private static DataCastManager createCastManager(Context ctx) {
      if (null == mCastMgr) {
         mCastMgr = DataCastManager.initialize(ctx, APPLICATION_ID, mHelloWorldChannel.getNamespace());
         mCastMgr.incrementUiCounter();
      }
      mCastMgr.setContext(ctx);

      return mCastMgr;
   }

	/**
	 * Android voice recognition
	 */
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.message_to_cast));
		startActivityForResult(intent, REQUEST_CODE);
	}

	/*
	 * Handle the voice recognition response
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if (matches.size() > 0) {
				Log.d(TAG, matches.get(0));
				sendMessage(matches.get(0));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
      mCastMgr.incrementUiCounter();
	}

	@Override
	protected void onPause() {
      mCastMgr.decrementUiCounter();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);

      // Add the MediaRouter via our cast manager
      mCastMgr.addMediaRouterButton(menu, R.id.media_route_menu_item);

		return true;
	}


	/**
	 * Send a text message to the receiver
	 * 
	 * @param message
	 */
	private void sendMessage(String message) {
      if (mCastMgr.isConnected()) {
         try {
            mCastMgr.sendDataMessage(message, mHelloWorldChannel.getNamespace());
         } catch (Exception e) {
            Log.e(TAG, "Sending message failed", e);
         }
		} else {
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Custom message channel
	 */
	class HelloWorldChannel extends DataCastConsumerImpl {

		/**
		 * @return custom namespace
		 */
		public String getNamespace() {
			return getString(R.string.namespace);
		}

		/*
		 * Receive message from the receiver app
		 */
		@Override
		public void onMessageReceived(CastDevice castDevice, String namespace, String message) {
			Log.d(TAG, "onMessageReceived: " + message);
		}

      @Override
      public void onMessageSendFailed(Status status) {
         Log.d(TAG, "onMessageSendFailed: " + status);
      }

	}

}
