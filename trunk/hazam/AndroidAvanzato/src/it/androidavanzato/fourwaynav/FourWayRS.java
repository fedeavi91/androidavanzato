package it.androidavanzato.fourwaynav;

import it.androidavanzato.R;
import it.androidavanzato.fourwaynav.FourWayNavView.Roll;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ProgramFragmentFixedFunction.Builder.EnvMode;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.renderscript.ProgramVertexFixedFunction;
import android.renderscript.ProgramVertexFixedFunction.Constants;
import android.renderscript.Sampler;

/**
 * 
 * @author Emanuele Di Saverio (emanuele.di.saverio at frogdesign.com )
 *
 */
public class FourWayRS extends BaseRS<ScriptC_cube> {
	
	private ProgramVertexFixedFunction mProgVertex;
	private Constants mPVA;
	private Mesh mMesh;
	private ProgramFragmentFixedFunction mProgramFragment;
	private ScriptField_Roll mRollType;

	public FourWayRS() {
	}

	protected void initProgramVertex() {
		ProgramVertexFixedFunction.Builder pvb = new ProgramVertexFixedFunction.Builder(mRS);
		mProgVertex = pvb.create();

		mPVA = new ProgramVertexFixedFunction.Constants(mRS);
		((ProgramVertexFixedFunction) mProgVertex).bindConstants(mPVA);
		Matrix4f proj = new Matrix4f();
		proj.loadPerspective(90.0f, ((float) mRS.getWidth()) / mRS.getHeight(), 0.1f, 500.0f);
		
		mPVA.setProjection(proj);
		mScriptC.set_programVertex(mProgVertex);
		mRS.bindProgramVertex(mProgVertex);
	}

	@Override
	protected ScriptC_cube createScriptC() {
		return new ScriptC_cube(mRS, mRes, R.raw.cube);
	}
	
	public void roll(Roll r) {
		mScriptC.invoke_rollTo(r.value);
	}

	@Override
	protected void initProgramFragment() {
		mProgramFragment = new ProgramFragmentFixedFunction.Builder(mRS).setTexture(EnvMode.REPLACE, Format.RGBA, 0)
				.create();
		mProgramFragment.bindTexture(loadTexture(R.drawable.texture), 0);
		mProgramFragment.bindSampler(Sampler.CLAMP_LINEAR_MIP_LINEAR(mRS), 0);
		mRS.bindProgramFragment(mProgramFragment);
		mScriptC.set_programFragment(mProgramFragment);

		mMesh = new Cube(mRS, 50.0f, new Float3(0, 0, 0)).toMesh();
		mScriptC.set_cubeMesh(mMesh);
		

        mRollType = new ScriptField_Roll(mRS, 1);
        mScriptC.bind_roll(mRollType);
	}
}
