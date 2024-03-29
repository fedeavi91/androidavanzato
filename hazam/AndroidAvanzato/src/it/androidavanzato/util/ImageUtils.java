package it.androidavanzato.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;

public class ImageUtils {
	public static Bitmap buildReflectedBitmap(final Bitmap orig, final int spacing, int startAlpha, float reflectionQuota) {
		final int width = orig.getWidth();
		final int height = orig.getHeight();
		final int reflectedHeight = (int) (height * reflectionQuota);

		//questa � la bitmap che verr� ritornata - larga come l'originale e alta una volta e mezzo
		//la profondit� di colore della mappa � 32 bit - modalit� 8888 invece di 565
		Bitmap toret = Bitmap.createBitmap(width, (height + reflectedHeight), Config.ARGB_8888);
		
		//premoltiplicare per una matrice che inverte le y
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		//immagine temporanea che conterr� la met� dell'immagine che costituisce il riflesso
		Bitmap reflectionImage = Bitmap.createBitmap(orig, 0, height - reflectedHeight, width, reflectedHeight, matrix, false);

		//disegnamo nell'immagine destinazione sia l'immagine sorgente che la porzione riflessa
		Canvas canvas = new Canvas(toret);
		canvas.drawBitmap(orig, 0, 0, null);
		canvas.drawBitmap(reflectionImage, 0, height + spacing, null);
		
		//ora aggiungiamo un effetto di trasparenza progressiva alla parte riflessa
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(
				0, height + spacing, //punto dai cui parte il gradiente 
				0, toret.getHeight() //punto di fine
				+ spacing, startAlpha << 24, Color.TRANSPARENT, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, toret.getHeight() + spacing, paint);
		reflectionImage.recycle();
		return toret;
	}
}
