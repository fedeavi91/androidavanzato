package it.androidavanzato.ui;

import it.androidavanzato.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ModalTextEditHoneycombActivity extends Activity implements Callback {
	private TextView mTextEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modal_textedit);
		mTextEdit = (TextView) findViewById(R.id.text);		
	}

	public void edit(View v) {
		//startActionMode() e' un metodo della classe madre View,
		//se ritorna un valore diverso da null, la View non prevede ActionMode		
		ActionMode started = mTextEdit.startActionMode(this);
		if (started != null) {
			Toast.makeText(this, "Action mode started" ,Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "The View didn't start the action mode!" ,Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (item.getItemId() == R.id.vocali) {
			String content = mTextEdit.getText().toString();
			mTextEdit.setText( content.replaceAll("[aeiouAEIOU]", "") );
		} else if (item.getItemId() == R.id.consonanti) {
			String content = mTextEdit.getText().toString();
			mTextEdit.setText( content.replaceAll("[b-df-hj-np-tv-zB-DF-HJ-NP-TV-Z]", "") );			
		}
		mode.finish();
		return true;
	}

	/*
	 * L'ActionMode e' gestito in maniera similare ai Dialog e ai menu di sistema,
	 * ovvero e' presente una onCreate, che viene chiamata una sola volta, ed una onPrepare
	 * per modificare l'ActionMode ogni volta che viene richiamato.
	 */	
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.setTitle("Vuoi rimuovere le consonanti o le vocali?");
		mode.setSubtitle("Usa il menu accanto");
		
		//in alternativa a titolo e sottotitolo e' possibile impostare una vista custom
		//ImageView custom = new ImageView(this);
		//custom.setImageResource(R.drawable.icon);
		//mode.setCustomView(custom);
		
		mode.getMenuInflater().inflate(R.menu.actionmodemenu, mode.getMenu());
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return true;
	}
}
