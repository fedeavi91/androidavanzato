package it.androidavanzato.fourwaynav;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.ProgramRaster;
import android.renderscript.ProgramStore;
import android.renderscript.RenderScriptGL;
import android.renderscript.ScriptC;

public abstract class BaseRS<T extends ScriptC> {

	protected Resources mRes;
	protected RenderScriptGL mRS;
	protected T mScriptC;
	


	// This provides us with the renderscript context and resources that
	// allow us to create the script that does rendering
	public void init(RenderScriptGL rs, Resources res) {
		mRS = rs;
		mRes = res;
		initRS();
	}

	protected abstract T createScriptC();
	protected abstract void initProgramVertex();
	protected abstract void initProgramFragment();

	private void initRS() {
		mScriptC = createScriptC();
		mRS.bindProgramRaster(ProgramRaster.CULL_NONE(mRS));
		mRS.bindProgramStore(ProgramStore.BLEND_ALPHA_DEPTH_TEST(mRS));
		initProgramVertex();
		initProgramFragment();
		mRS.bindRootScript(mScriptC);
	}


	protected Allocation loadTexture(int id) {
        Bitmap b = BitmapFactory.decodeResource(mRes, id, new BitmapFactory.Options());
        return Allocation.createFromBitmap(mRS, b,
                                           Allocation.MipmapControl.MIPMAP_ON_SYNC_TO_TEXTURE,
                                           Allocation.USAGE_GRAPHICS_TEXTURE);
	}

	public static float byteToChannel(int colchan) {
		return colchan * (1.0f / 255);
	}
}
