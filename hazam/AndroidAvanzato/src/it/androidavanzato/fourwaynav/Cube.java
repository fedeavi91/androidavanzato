package it.androidavanzato.fourwaynav;

import android.renderscript.Float3;
import android.renderscript.Mesh;
import android.renderscript.RenderScriptGL;

public class Cube {

	private RenderScriptGL mRS;
	private final float mSize;
	private final Float3 mOffset;

	public Cube(RenderScriptGL rs, float side, Float3 offset) {
		mRS = rs;
		mSize = side;
		mOffset = offset;
	}

	public Mesh toMesh() {
		Mesh.TriangleMeshBuilder tm = new Mesh.TriangleMeshBuilder(
				mRS, 3, Mesh.TriangleMeshBuilder.TEXTURE_0 
				| Mesh.TriangleMeshBuilder.NORMAL);
		mOffset.x -= mSize / 2;
		mOffset.y -= mSize / 2;
		mOffset.z -= mSize / 2;
		
		addQuad(tm, new Float3[] { 
				new Float3(mOffset.x, mOffset.y, mOffset.z), 
				new Float3(mOffset.x + mSize, mOffset.y, mOffset.z),
				new Float3(mOffset.x + mSize, mOffset.y + mSize, mOffset.z), 
				new Float3(mOffset.x, mOffset.y + mSize, mOffset.z) 
				}, 0);
		addQuad(tm, new Float3[] { 
				new Float3(mOffset.x, mOffset.y + mSize, mOffset.z + mSize),
				new Float3(mOffset.x, mOffset.y, mOffset.z + mSize), 
				new Float3(mOffset.x + mSize, mOffset.y, mOffset.z + mSize), 
				new Float3(mOffset.x + mSize, mOffset.y + mSize, mOffset.z + mSize) 
				}, 4);
		addQuad(tm, new Float3[] {
				new Float3(mOffset.x, mOffset.y, mOffset.z + mSize),
				new Float3(mOffset.x, mOffset.y, mOffset.z), 
				new Float3(mOffset.x + mSize, mOffset.y, mOffset.z), 
				new Float3(mOffset.x + mSize, mOffset.y, mOffset.z + mSize)
				}, 8);
		addQuad(tm, new Float3[] { 
				new Float3(mOffset.x, mOffset.y + mSize, mOffset.z),
				new Float3(mOffset.x, mOffset.y + mSize, mOffset.z + mSize),
				new Float3(mOffset.x + mSize, mOffset.y + mSize, mOffset.z + mSize) ,
				new Float3(mOffset.x + mSize, mOffset.y + mSize, mOffset.z)
				}, 12);
		addQuad(tm, new Float3[] { 
				new Float3(mOffset.x + mSize, mOffset.y + mSize, mOffset.z + mSize), 
				new Float3(mOffset.x + mSize, mOffset.y, mOffset.z + mSize) ,
				new Float3(mOffset.x + mSize, mOffset.y, mOffset.z), 
				new Float3(mOffset.x + mSize, mOffset.y + mSize, mOffset.z)
				}, 16);

		addQuad(tm, new Float3[] {
				new Float3(mOffset.x, mOffset.y + mSize, mOffset.z),
				new Float3(mOffset.x, mOffset.y, mOffset.z), 
				new Float3(mOffset.x, mOffset.y, mOffset.z + mSize), 
				new Float3(mOffset.x, mOffset.y + mSize, mOffset.z + mSize)
				}, 20);
		return tm.create(true);
		
	}

	private void addQuad(Mesh.TriangleMeshBuilder tm, Float3[] vertexes, int offset) {
		if (vertexes.length != 4) {
			throw new RuntimeException("A face should have 4 vertexes");
		}
		int face = 5 - offset / 4;
		float startX = face * 0.2f;
		if (startX > 0.8f) startX = 0.8f;
	
		tm.setTexture(startX, 0);
		tm.addVertex(vertexes[0].x, vertexes[0].y, vertexes[0].z);
		tm.setTexture(startX, 1);
		tm.addVertex(vertexes[1].x, vertexes[1].y, vertexes[1].z);
		tm.setTexture(startX + 0.2f, 1);
		tm.addVertex(vertexes[2].x, vertexes[2].y, vertexes[2].z);
		tm.setTexture(startX + 0.2f, 0);
		tm.addVertex(vertexes[3].x, vertexes[3].y, vertexes[3].z);
		
		// add triangles ClockWise and CCW to be sure
		tm.addTriangle(offset, offset + 1, offset + 2);
		tm.addTriangle(offset, offset + 2, offset + 1);
		tm.addTriangle(offset + 2, offset + 3, offset);
		tm.addTriangle(offset + 2, offset, offset + 3);
	}
}
