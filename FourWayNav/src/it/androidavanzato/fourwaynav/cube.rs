//versione di Rendescript che si vuole usare
#pragma version(1)

//costante per il package nel quale generare le risorse
//deve essere uguale al package dell'applicazione
#pragma rs java_package_name(it.androidavanzato.fourwaynav)

//includere le pimitive di grafica. Verra' inclusa anche rs_math.rsh
#include "rs_graphics.rsh"

//invocato automaticamente
void init() {
}

rs_program_vertex programVertex;
rs_program_fragment programFragment;
rs_program_store programStore;
rs_mesh cubeMesh;

float rot = 0.0;

//interpolatore accel - decel
float accelDecelInterpolator(float input) {
   return (float) ( cos((input + 1) * M_PI) / 2.0f) + 0.5f;
}

float overshootInterpolator(float t) {
   t -= 1.0f;
   return t * t * (3.0 * t + 2.0) + 1.0f;
}

bool animating = false;
long startTime = 0;
const float ANIM_DURATION = 1000.0;

void rollLeft() {
	animating = true;
	startTime = rsUptimeMillis();
}

enum {
    ROLL_LEFT = 0,
    ROLL_UP = 1,
    ROLL_DOWN = 2,
    ROLL_RIGHT = 3,
    ROLL_NONE = -1
};

typedef struct __attribute__((packed, aligned(4))) Roll {
    long startTime;
    int type;
} Roll_t;
Roll_t *roll;

void rollTo(int type) {
	roll->startTime = rsUptimeMillis();
	roll->type = type;
}

int root(int launchID) {

    // Clear the background color
    rsgClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    rsgClearDepth(100.0f);
    rsgBindProgramVertex(programVertex);
    rsgBindProgramFragment(programFragment);
    rsgBindProgramStore(programStore);
    
    
	float aspect = (float)rsgGetWidth() / (float)rsgGetHeight();
    rs_matrix4x4 proj;
    rsMatrixLoadPerspective(&proj, 90.0f, aspect, 0.1f, 1000.0f);
    //rsgProgramVertexLoadProjectionMatrix(&proj);

	//rsDebug("Aspect was: ", aspect);
    rs_matrix4x4 matrix;
    rsMatrixLoadIdentity(&matrix);
    rsMatrixTranslate(&matrix, 0.0f, 0.0f, -70.0f);
   	rsMatrixRotate(&matrix, rot, 0.0f, 1.0f, 0.0f);
        
   	rsgProgramVertexLoadModelMatrix(&matrix);
   	if (animating) {
   		float normalized = (rsUptimeMillis() - startTime) / ANIM_DURATION;
   		rot = overshootInterpolator(normalized) * 90.0f;
   		if (normalized >= 1.0) {
   			animating = false;
   		}
   	}
    rsgDrawMesh(cubeMesh);
    
    // il valore ritornato e' l'intervallo di loop, in millisecondi
    // ritornando 0 si imposta il redraw su richiesta
    // ritornando 1 invece non si richiedono 1000 fotogrammi al secondo in quando
    // Android limita a 60 fps, che e' il massimo valore di refresh fisico dello schermo
    return 1;
}
