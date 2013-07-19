#version 330 core

in vec4 in_Position;
in vec4 in_Color;

out vec4 pass_Color;

uniform float angle;
uniform vec2 position;

uniform float camZoom;
uniform vec2 camPosition;

void main(void){
	//****Transforming the vertex according to the objects down state 
	//rotating the vertex
	float rotatedX = in_Position.x * cos(angle) + in_Position.y * sin(angle);
	float rotatedY = in_Position.x * sin(angle) - in_Position.y * cos(angle);
	
	//scaling the vertex
	float scaledX = rotatedX * 1;
	float scaledY = rotatedY * 1;
	
	//moving the vertex
	float absX = scaledX + position.x;
	float absY = scaledY + position.y;
	
	//**** Transforming the vertex according to the camera state
	
	//Scaling the vertex by cameraZoom
	float camScaleX = absX*camZoom;
	float camScaleY = absY*camZoom;
	
	//Translating the camera by the camera position
	gl_Position.x = camScaleX - camPosition.x;
	gl_Position.y = camScaleY - camPosition.y;
	
	//Taking care of colors
	pass_Color = in_Color;
}