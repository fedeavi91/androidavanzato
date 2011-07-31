package it.androidavanzato.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

public class RotateAnimation extends Animation {
	private Camera camera;
	private View front;
	private View back;
	private float centerX;
	private float centerY;
	private boolean visibilitySwapped;

	/**
	 * Le due view di Front e Back devono essere figli diretti della View alla quale applico l'animazione!!!
	 * @param front
	 * @param back
	 */
	public RotateAnimation(View front, View back) {
		this.front = front;
		this.back = back;
		setDuration(1000);
		setFillAfter(true);
		setInterpolator(new OvershootInterpolator());
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		//nel metodo di initialize precomputiamo alcuni valori che ci torneranno utili 
		//per l'animazione, e ci assicuriamo che le immagini abbiano le visibilitˆ corrette
		centerX = width / 2;
		centerY = height / 2;
		camera = new Camera();
		front.setVisibility(View.VISIBLE);
		back.setVisibility(View.GONE);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		//applyTransformation viene chiamata ad ogni iterazione dell'animazione,
		//e il contratto prevede che venga modificato l'oggetto Transformation
		//che rappresenta come viene trasformato il rendering per l'istante corrente
		
		float degrees = (float) (180.0 * interpolatedTime);
		//interpolatedTime  un float che va da 0.0f a 1.0f, ed  l'istante
		//corrente del timeframe dell'animazione normalizzato e interpolata
		//secondo l'oggetto interpolator settato (linear, accelerato etc)


		//Trasformation  composto da una matrice di rototraslazione
		//e un valore di Alpha (in futuro espandibile)
		final Matrix matrix = t.getMatrix();

		//Camera  un oggetto helper che aiuta a computare una matrice di rototraslazione
		//concettualmente  una cinepresa, ruotarla significa computare effetti di
		//parallasse e prospettiva in uno spazio 3D
		camera.save();
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();
		
		//vogliamo ruotare per˜ intorno al centro quindi prima ci spostiamo al centro dela vista
		matrix.postTranslate(centerX, centerY);
		
		//...poi avviene la rotazione
		
		//poi torno indietro
		matrix.preTranslate(-centerX, -centerY);
		
		//se ho superato la metˆ dell tempo, scambio le viste front e back
		if (interpolatedTime >= 0.5f && !visibilitySwapped) {
			front.setVisibility(View.GONE);
			back.setVisibility(View.VISIBLE);
			visibilitySwapped = true;
		}
	}
}
