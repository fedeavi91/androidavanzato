package it.androidavanzato.nfc.booknote;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BookNote extends Activity {
	private static final int DIALOG_FORMAT = 1;
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	private EditText mText;
	private ArrayAdapter<String> adapter;
	private Tag currentTag;
	private ArrayList<String> currentNotes = new ArrayList<String>();
	private ListView list;
	private Button addButton;

	// OK
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);

		setContentView(R.layout.main);

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		registerForContextMenu(list);

		addButton = (Button) findViewById(R.id.add_button);
		
		mText = (EditText) findViewById(R.id.text);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		prepareFilters();

		onNewIntent(getIntent());
	}

	// OK
	private void prepareFilters() {
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefIntent.addDataType("text/plain");
		} catch (Exception e) {
			throw new RuntimeException("fail", e);
		}

		IntentFilter formatableIntent = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		mFilters = new IntentFilter[] { formatableIntent, ndefIntent };
		mTechLists = new String[][] { new String[] { NdefFormatable.class.getName() }, new String[] { Ndef.class.getName() } };

	}

	// OK
	@Override
	public void onResume() {
		super.onResume();

		mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
	}

	// OK
	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		Log.i("************** Foreground dispatch", "Discovered tag with intent: " + intent);

		String action = intent.getAction();
		Bundle bundle = intent.getExtras();

		/*
		 * Potrebbe essere un tag formattabile NDEF
		 */
		if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
			handleActionTechDiscovered(bundle);
		} else if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
			handleActionNDEFDiscovered(bundle);
		} else if (action.equals(Intent.ACTION_SEND)) {
			handleActionSend(bundle);
		}
	}

	// OK
	private void handleActionTechDiscovered(Bundle bundle) {
		Tag tag = bundle.getParcelable(NfcAdapter.EXTRA_TAG);

		List<String> techList = Arrays.asList(tag.getTechList());

		if (techList.contains(NdefFormatable.class.getName())) {
			showDialog(DIALOG_FORMAT);
			currentTag = tag;
		} else if (techList.contains(Ndef.class.getName())) {
			// il tag e' vuoto
			currentNotes.clear();
			adapter.clear();
			currentTag = tag;
			
			checkWritable(tag);
		} else {
			showMessage("Tag non supportato");
			currentNotes.clear();
			adapter.clear();
			currentTag = null;
		}
	}

	// OK
	private void handleActionNDEFDiscovered(Bundle bundle) {
		Tag tag = bundle.getParcelable(NfcAdapter.EXTRA_TAG);

		currentTag = tag;

		checkWritable(tag);
		
		readNotes();
	}
	
	private void checkWritable(Tag tag) {
		if (! Ndef.get(tag).isWritable()) {
			showMessage("Il tag non è scrivibile");
			addButton.setEnabled(false);
		} else {
			addButton.setEnabled(true);
		}
	}

	// OK
	private void handleActionSend(Bundle bundle) {
		String text = bundle.getString(Intent.EXTRA_TEXT);
		if (text != null) {
			((EditText) findViewById(R.id.text)).setText(text);
		}
	}

	// OK
	private String hexByte(byte b) {
		int pos = b;
		if (pos < 0)
			pos += 256;
		String returnString = new String();
		returnString += Integer.toHexString(pos / 16);
		returnString += Integer.toHexString(pos % 16);
		return returnString;
	}

	// OK
	@Override
	public void onPause() {
		super.onPause();

		mNfcAdapter.disableForegroundDispatch(this);
	}


	// OK
	private String[] readRecords() {
		Ndef ndefTag = null;

		try {
			ndefTag = Ndef.get(currentTag);

			ndefTag.connect();

			NdefRecord[] records = ndefTag.getNdefMessage().getRecords();

			ArrayList<String> strings = new ArrayList<String>();
			for (NdefRecord record : records) {
				strings.add(decodeTextPayload(record.getPayload()));
			}

			String[] strs = new String[strings.size()];
			strings.toArray(strs);
			return strs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (ndefTag != null && ndefTag.isConnected()) {
				try {
					ndefTag.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// OK
	/*
	 * Un record e' costruito con:
	 * 
	 * Type Name Format: di che tipo di tag si tratta Type: e' il formato di
	 * dato interno (testo, URI, poster...) un ID il payload
	 * 
	 * Per un tag di testo, il payload e' costituito da: - STATUS: codifica
	 * (UTF-8/UTF-16), lunghezza del codice lingua - CODICE LINGUA - DATI VERI E
	 * PROPRI
	 */
	private byte[] encodeTextPayload(String text) throws Exception {
		byte[] languageInUse = Locale.getDefault().getLanguage().getBytes(Charset.forName("US_ASCII"));

		// assumiamo che sia sempre UTF-8
		byte status = (byte) (0x3f & languageInUse.length);

		byte[] textBytes = text.getBytes("UTF-8");

		byte[] payload = new byte[1 + languageInUse.length + textBytes.length];

		payload[0] = status;
		System.arraycopy(languageInUse, 0, payload, 1, languageInUse.length);
		System.arraycopy(textBytes, 0, payload, 1 + languageInUse.length, textBytes.length);

		return payload;
	}

	// OK
	private String decodeTextPayload(byte[] payload) throws Exception {
		byte status = payload[0];

		int languageCodeLenght = status & 0x3f;

		return new String(payload, 1 + languageCodeLenght, payload.length - 1 - languageCodeLenght, "UTF-8");
	}

	// OK
	private void formatTag() throws Exception {
		NdefFormatable ndefFormatableTag = null;

		try {
			ndefFormatableTag = NdefFormatable.get(currentTag);

			NdefMessage message = new NdefMessage(new NdefRecord[] {  });

			ndefFormatableTag.connect();
			ndefFormatableTag.format(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (ndefFormatableTag != null && ndefFormatableTag.isConnected()) {
					ndefFormatableTag.close();
				}
			} catch (Exception e) {

			}
		}

	}

	// OK
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_FORMAT:
				return createFormatDialog();
		}

		return super.onCreateDialog(id);
	}

	// OK
	private Dialog createFormatDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Questo tag non è formattato. Vuoi formattarlo adesso?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					formatTag();
				} catch (Exception e) {
					// TODO Auto-generated catch block

				}
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();

		return alert;
	}

	// OK
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add("Cancella");
	}

	// OK
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		currentNotes.remove(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		try {
			writeNotes();
			readNotes();
		} catch (Exception e) {

		}
		return true;
	}

	// OK
	private void readNotes() {
		currentNotes.clear();
		adapter.clear();
		
		currentNotes.addAll(Arrays.asList(readRecords()));

		for (String note : currentNotes) {
			if (note.trim().length() > 0) {
				adapter.add(note);
			}
		}
	}

	// OK
	private void writeNotes() throws Exception {
		Ndef ndefTag = null;

		try {
			ndefTag = Ndef.get(currentTag);

			if (ndefTag.isWritable()) {

				ArrayList<NdefRecord> recordList = new ArrayList<NdefRecord>(currentNotes.size());

				byte[] payload = null;
				NdefRecord record = null;

				for (String string : currentNotes) {
					payload = encodeTextPayload(string);
					record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
					recordList.add(record);
				}

				NdefRecord[] records = new NdefRecord[recordList.size()];
				recordList.toArray(records);
				NdefMessage message = new NdefMessage(records);

				ndefTag.connect();
				ndefTag.writeNdefMessage(message);
				mText.setText("");
			} else {
				showMessage("Il tag non è scrivibile");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (ndefTag != null && ndefTag.isConnected()) {
				ndefTag.close();
			}
		}

	}

	// OK
	public void addNote(View view) {

		String text = mText.getText().toString().trim();
		if (text.length() > 0) {
			currentNotes.add(0, text);

			try {
				writeNotes();
				readNotes();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	// OK
	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}
