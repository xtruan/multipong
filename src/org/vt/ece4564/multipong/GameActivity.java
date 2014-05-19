package org.vt.ece4564.multipong;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends HomeActivity implements AccelerometerListener {
	private PowerManager.WakeLock wl;
	private String TAG = "GameActivity";
	private PongView pongView_;
	private static Context CONTEXT;
	private int opp_, opp_score_, count_ = 0;
	private int[] scores_ = { 0, 0 };
	// private ArrayList<String> messages_;
	private String[] messages_ = { "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// messages_ = new ArrayList();
		if (HomeActivity.server_) {
			sendMessage("start");
		}

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

		Log.d(TAG, "requesting No Title");
		// getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		Log.d(TAG, "got No Title");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Log.d(TAG, "Before setContentView");
		setContentView(R.layout.pong_view);
		// Log.d(TAG, "After setContentView");

		pongView_ = (PongView) findViewById(R.id.pong);
		CONTEXT = this;
		// Log.d(TAG, "Pong game found and running...");

		if (HomeActivity.server_) {
			Log.d(TAG, "SERVER");
			// pongView_.killBall();
			// pongView_.createBall();
		}
	}

	public static Context getContext() {
		return CONTEXT;
	}

	public void onResume() {
		super.onResume();
		if (AccelerometerManager.isSupported()) {
			AccelerometerManager.startListening(this);
		}
		wl.acquire();
	}

	public void onPause() {
		super.onPause();

		wl.release();
	}

	public void onDestroy() {
		gameFlag_ = true;
		super.onDestroy();
		if (AccelerometerManager.isListening()) {
			AccelerometerManager.stopListening();
		}
		// if (mChatService != null)
		// mChatService.stop();
		// wl.release();
	}

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		if (pongView_.initialized_) {
			scores_ = pongView_.getScores();

			if (!HomeActivity.server_) {
				pongView_.killBall();
			}
			
			if (scores_[0] < 10 && scores_[1] < 10) {
				// TODO: implement sensitivity settings
				// calculate tilt
				double tx, ty, tz; // angles in radians
				int dx, dy, dz; // tilt increments ~2 degrees
				tx = Math.atan(x / Math.sqrt(y * y + z + z));
				ty = Math.atan(y / Math.sqrt(x * x + z * z));
				tz = Math.atan(z / Math.sqrt(x * x + y * y));
				dx = (int) (tx * 90 / Math.PI);
				dy = (int) (ty * 90 / Math.PI);
				dz = (int) (tz * 90 / Math.PI);
				// Log.d(TAG, "Shift Paddle By: "+dy);

				pongView_.movePaddle(dy);

				count_++;
				
				if (count_ == 100) {
					sendMessage("_s_" + scores_[1] + "_s_");
					count_ = 0;
				}
				
				if (HomeActivity.haveBall_ == true) {
					messages_ = HomeActivity.ballMessage_.trim().split("_");
				}
				
				if (HomeActivity.haveScore_ == true) {
					opp_score_ = Integer.parseInt(HomeActivity.scoreMessage_);
					pongView_.setOppScore(opp_score_);
					HomeActivity.haveScore_ = false;
				}
				
				try {
					sendMessage("" + pongView_.getPaddleX());
				} catch (Exception e) {

				}
				try {
					if ( HomeActivity.haveBall_ == true /* && ((messages_[0].equals("b") && messages_[2].equals("b"))
					  || (messages_[1].equals("b") && messages_[3].equals("b")))*/ ) {
						
						HomeActivity.haveBall_ = false;
						HomeActivity.server_ = true;

						if (messages_[0].equals("b")) {
							pongView_.setBall(Integer.parseInt(messages_[1]));
						} else {
							pongView_.setBall(Integer.parseInt(messages_[2]));
						}

						// pongView_.setBall(messages_);
					}
				} catch (Exception e) {

				}
				if (HomeActivity.server_ == true && !pongView_.checkBall()) {
					HomeActivity.server_ = false;
					sendMessage("" + pongView_.getBall());
					sendMessage("" + pongView_.getBall());
					sendMessage("" + pongView_.getBall());
					// HomeActivity.server_ = false;
				}

				try {
					opp_ = Integer.parseInt(HomeActivity.readMessage_);
				} catch (Exception e) {
					Log.d(TAG, "ParseInt Error");
				}

				// pongView_.moveOppPaddle(opp_);
				// pongView_.setOppPaddleX(opp_);
				// sendMessage("" + dx + " " + dy + " " + dz);
				// pongView_.getBall().
				// sendMessage("" + dy);
				// sendMessage("" + pongView_.getPaddleX());
				try {
					pongView_.setOppPaddleX(opp_);
				} catch (Exception e) {
					// pongView_.setOppPaddleX(opp_);
				}
			}

			else {
				if (AccelerometerManager.isListening()) {
					AccelerometerManager.stopListening();
				}
				sendMessage("_s_" + scores_[1] + "_s_");
				sendMessage("_s_" + scores_[1] + "_s_");
				sendMessage("_s_" + scores_[1] + "_s_");
				String msg = "";
				if (scores_[0] > scores_[1]) {
					msg = "Win!  Score: " + scores_[0] + "-" + scores_[1]
							+ "!\n";
				} else {
					msg = "Lose! Score: " + scores_[0] + "-" + scores_[1]
							+ "!\n";
				}

				//String FILENAME = "scores.txt";

				File scoreFile = new File(
						Environment.getExternalStorageDirectory()
								+ "/mp_scores.txt");
				if (!scoreFile.exists()) {
					try {
						scoreFile.createNewFile();
					} catch (IOException e) {
						Log.d(TAG, e.toString());
					}
				}

				FileOutputStream fos;
				try {
					Log.d(TAG, "File write start!");
					fos = new FileOutputStream(scoreFile, true);
					fos.write(msg.getBytes());
					fos.flush();
					fos.close();
					Log.d(TAG, "File write end!");
				} catch (Exception e) {
					Log.d(TAG, "File error!");
				}

				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				alt_bld.setMessage(msg)
						.setCancelable(false)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
										// Action for 'Yes' Button
									}
								});
				/*
				 * .setNegativeButton("No", new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { // Action for 'NO'
				 * Button dialog.cancel(); } });
				 */
				AlertDialog alert = alt_bld.create();
				// Title for AlertDialog
				alert.setTitle("Game Over!");
				// Icon for AlertDialog
				//alert.setIcon(R.drawable.icon);
				alert.show();

				// Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
				// finish();
			}
		}
	}

	public void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			// Toast.makeText(this, R.string.not_connected,
			// Toast.LENGTH_SHORT).show();
			Log.d(TAG, "Not connected!");
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			// mOutStringBuffer.setLength(0);
			// mOutEditText.setText(mOutStringBuffer);
		}
	}

}
