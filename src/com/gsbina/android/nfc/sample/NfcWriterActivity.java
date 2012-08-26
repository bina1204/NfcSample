package com.gsbina.android.nfc.sample;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class NfcWriterActivity extends Activity {

	private NfcWriterActivity mSelf = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc_writer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_nfc_writer, menu);
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

		enableTagWriteMode();

	}

	@Override
	protected void onPause() {
		super.onPause();
		mNfcAdapter.disableForegroundDispatch(mSelf);
	}

	private boolean mWriteMode;

	private void enableTagWriteMode() {
		mWriteMode = true;
		Intent intent = new Intent(mSelf, getClass());
		PendingIntent pendingIntent = PendingIntent.getActivity(mSelf, 0,
				intent, 0);
		IntentFilter[] filters = new IntentFilter[] {
			new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
		};

		mNfcAdapter.enableForegroundDispatch(mSelf, pendingIntent, filters,
				null);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String action = intent.getAction();
		if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

			String message = "";
			if (writeTag(createNdefMessage(), detectedTag)) {
				message = "Success: Wrote placeid to nfc tag";
			} else {
				message = "Write failed";
			}
			Toast.makeText(mSelf, message, Toast.LENGTH_SHORT).show();
		}
	}

	private NdefMessage createNdefMessage() {
		// NDEFRecordを生成
		Charset utfEncoding = Charset.forName("UTF-8");
		Locale locale = Locale.getDefault();

		final byte[] langBytes = locale.getLanguage().getBytes(utfEncoding);
		final byte[] textBytes = "Hello, NFC!".getBytes(utfEncoding);
		final int utfBit = 0;
		final char status = (char) (utfBit + langBytes.length);

		ByteBuffer buff = ByteBuffer.allocate(1 + langBytes.length
				+ textBytes.length);
		final byte[] data = buff.put((byte) status).put(langBytes)
				.put(textBytes).array();
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], data);
		NdefRecord[] records = {
			record
		};

		return new NdefMessage(records);
	}

	/**
	 * NFC タグにメッセージを書き込む。
	 * 
	 * @param message
	 *            メッセージ
	 * @param tag
	 *            タグ
	 * @return 書き込みに成功した場合は true 、それ以外は false を返す
	 */
	public boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		Ndef ndef = Ndef.get(tag);
		try {
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					return false;
				}
				if (ndef.getMaxSize() < size) {
					return false;
				}
				ndef.writeNdefMessage(message);
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					format.connect();
					format.format(message);
					return true;
				} else {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}

		return false;
	}
}
