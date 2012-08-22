package com.gsbina.android.nfc.sample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class NfcReactActivity extends Activity {

	private final NfcReactActivity mSelf = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc_tag_viewer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_nfc_tag_viewer, menu);
		return true;
	}

	private NfcAdapter mNfcAdapter;

	@Override
	protected void onResume() {
		super.onResume();

		mNfcAdapter = NfcAdapter.getDefaultAdapter(mSelf);

		if (mNfcAdapter == null) {
			Toast.makeText(getApplicationContext(), "Not found NFC feature",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		if (!mNfcAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(),
					"NFC feature is not available", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		Intent intent = new Intent(mSelf, getClass());
		PendingIntent pendingIntent = PendingIntent.getActivity(mSelf, 0,
				intent, 0);
		IntentFilter[] filters = new IntentFilter[] {
			new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
		};

		String[][] teckLists = new String[][] {
			{
					NfcA.class.getName(), NfcB.class.getName(),
					IsoDep.class.getName(), MifareClassic.class.getName(),
					MifareUltralight.class.getName(),
					NdefFormatable.class.getName(), NfcV.class.getName(),
					NfcF.class.getName(),
			}
		};

		mNfcAdapter.enableForegroundDispatch(mSelf, pendingIntent, filters,
				teckLists);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mNfcAdapter.disableForegroundDispatch(mSelf);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		String action = intent.getAction();
		if (action.isEmpty()) {
			return;
		}

		if (!action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			return;
		}

		byte[] rawId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		String id = Util.bytesToString(rawId);

		Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
	}
}
