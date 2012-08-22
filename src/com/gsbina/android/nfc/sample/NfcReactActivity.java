package com.gsbina.android.nfc.sample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NfcReactActivity extends Activity {

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
}
