//versione di Rendescript che si vuole usare
#pragma version(1)

//costante per il package nel quale generare le risorse
//deve essere uguale al package dell'applicazione
#pragma rs java_package_name(it.androidavanzato.fourwaynav)

//includere le primitive di grafica. Verra' inclusa anche rs_math.rsh
#include "rs_graphics.rsh"

rs_program_vertex programVertex;
rs_program_fragment programFragment;
rs_program_store programStore;
rs_mesh cubeMesh;

float overshootInterpolator(float t) {
	t -= 1.0f;
	return t * t * (3.0 * t + 2.0) + 1.0f;
}

const float ANIM_DURATION = 1000.0;

enum {
	ROLL_LEFT = 0, ROLL_UP = 1, ROLL_DOWN = 2, ROLL_RIGHT = 3, ROLL_NONE = -1
};

typedef struct __attribute__((packed, aligned(4))) Roll {
	long startTime;
	int type;
	float rot;
} Roll_t;
Roll_t *roll;

void rollTo(int type) {
	roll->startTime = rsUptimeMillis();
	roll->type = type;
	roll->rot = 0.0;
}


//invocato automaticamente
void init() {
}

int root(int launchID) {
	// Clear the background color
	rsgClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	rsgClearDepth(1000.0f);
	rsgBindProgramVertex(programVertex);
	rsgBindProgramFragment(programFragment);
	rsgBindProgramStore(programStore);

	float aspect = (float) rsgGetWidth() / (float) rsgGetHeight();
	rs_matrix4x4 proj;
	rsMatrixLoadPerspective(&proj, 90.0f, aspect, 0.1f, 1000.0f);
	//rsgProgramVertexLoadProjectionMatrix(&proj);

	rs_matrix4x4 matrix;
	rsMatrixLoadIdentity(&matrix);
	rsMatrixTranslate(&matrix, 0.0f, 0.0f, -70.0f);
	//rsDebug("Aspect was: ", aspect);
	switch (roll->type) {
	case ROLL_LEFT:
		rsMatrixRotate(&matrix, roll->rot, 0.0f, 1.0f, 0.0f);
		break;
	case ROLL_RIGHT:
		rsMatrixRotate(&matrix, roll->rot, 0.0f, -1.0f, 0.0f);
		break;
	case ROLL_UP:
		rsMatrixRotate(&matrix, roll->rot, 1.0f, 0.0f, 0.0f);
		break;
	case ROLL_DOWN:
		rsMatrixRotate(&matrix, roll->rot, -1.0f, 0.0f, 0.0f);
		break;
	}

	rsgProgramVertexLoadModelMatrix(&matrix);
	if (roll->type != ROLL_NONE) {
		float normalized = (rsUptimeMillis() - roll->startTime) / ANIM_DURATION;
		roll->rot = overshootInterpolator(normalized) * 90.0f;
		if (normalized >= 1.0) {
			roll->type = ROLL_NONE;
		}
	}
	rsgDrawMesh(cubeMesh);

	// il valore ritornato e' l'intervallo di loop, in millisecondi
	// ritornando 0 si imposta il redraw su richiesta
	// ritornando 1 invece non si richiedono 1000 fotogrammi al secondo in quando
	// Android limita a 60 fps, che e' il massimo valore di refresh fisico dello schermo
	return 1;
}
