package it.androidavanzato.nfc.hellotag;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class HelloTag extends ListActivity {
	private NfcAdapter mNfcAdapter;
	private ArrayAdapter<String> mListAdapter;

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		mListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

		getListView().setAdapter(mListAdapter);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

	}

	@Override
	public void onResume() {
		super.onResume();

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

	}

	@Override
	public void onPause() {
		super.onPause();

		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onNewIntent(Intent intent) {
		String action = intent.getAction();

		if (isNFCAction(action)) {

			mListAdapter.clear();

			mListAdapter.add(action);

			Bundle bundle = intent.getExtras();

			Tag tag = bundle.getParcelable(NfcAdapter.EXTRA_TAG);
			
			// ID
			byte[] tagID = tag.getId();
			StringBuffer id = new StringBuffer();
			for (byte b : tagID) {
				id.append(hexByte(b));
			}
			mListAdapter.add("ID: " + id.toString());

			// tech
			String[] techList = tag.getTechList();

			for (String tech : techList) {

				if (tech.equals(Ndef.class.getName())) {
					int size = Ndef.get(tag).getMaxSize();
					mListAdapter.add("Tech: " + tech + "(size: " + size + ")");
				} else if (tech.equals(MifareClassic.class.getName())) {
					int size = MifareClassic.get(tag).getSize();
					mListAdapter.add("Tech: " + tech + "(size: " + size + ")");
				} else {
					mListAdapter.add("Tech: " + tech);
				}
			}
		}
	}

	private boolean isNFCAction(String action) {
		return (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action));
	}

	private String hexByte(byte b) {
		int pos = b;

		if (pos < 0) {
			pos += 256;
		}

		String returnString = new String();
		returnString += Integer.toHexString(pos / 16);
		returnString += Integer.toHexString(pos % 16);
		return returnString;
	}
}
