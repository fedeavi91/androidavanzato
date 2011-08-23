package it.androidavanzato;

import it.androidavanzato.romaski.fs.ImageCache;
import it.androidavanzato.romaski.widget.WebcamImageFilter;

import java.util.HashMap;

import android.app.Application;

/**
 * Questa classe rappresenta una istanza di Application con una utile
 * feature aggiuntiva: e' possibile impostare una serie di oggetti 
 * come oggetti di servizio generali, e implementare una struttura
 * di simil - Dependency Injection a-la Spring - il celebre 
 * framework Java Enterprise
 * 
 * @author Emanuele Di Saverio - emanuele.disaverio at gmail.com
 */
public class AndroidAvanzatoApplication extends Application {
	
	public static String IMAGE_CACHE_APPSERVICE = "imageCacheService";
	public static final String TAG = "RomaSki";
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (appServices == null) {
			appServices = new HashMap<String, Object>();
		}
		ImageCache cache = new ImageCache(getApplicationContext(), TAG);
		WebcamImageFilter filter = new WebcamImageFilter(getApplicationContext());
		cache.setOnSaveFilter(filter);
		cache.setOnLoadFilter(filter);
		registerAppService(AndroidAvanzatoApplication.IMAGE_CACHE_APPSERVICE, cache);
	}
	
	private static HashMap<String, Object> appServices;
	
	public static Object getAppService(String name) {
		return appServices.get(name);
	}
	
	protected static void registerAppService(String name, Object serv) {
		appServices.put(name, serv);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		appServices = null;
	}
}
