package com.gsbina.android.nfc.sample;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class NfcReceiveActivity extends Activity {

	private final NfcReceiveActivity mSelf = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc_receive);

		showNfcId(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_nfc_receive, menu);
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		showNfcId(intent);
	}

	private void showNfcId(Intent intent) {
		if (intent == null) {
			return;
		}

		String action = intent.getAction();
		if (action.isEmpty()) {
			return;
		}

		if (!action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
			return;
		}

		byte[] rawId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		String id = Util.bytesToString(rawId);

		Toast.makeText(mSelf, id, Toast.LENGTH_SHORT).show();

	}
}
