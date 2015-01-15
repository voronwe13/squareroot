


attribute vec4 position;
attribute vec2 texture0;
attribute vec3 normal;
attribute vec4 tangent;


varying vec2 vUV;
varying vec3 N;
varying vec3 V;
varying vec3 T;
varying vec3 B;


uniform mat4 modelViewMatrix;
//uniform mat4 projectionMatrix;
uniform mat4 normalMatrix;
uniform mat4 modelViewProjectionMatrix;


void main( void )
{	vec4 V4 = modelViewMatrix * position;
	V = V4.xyz;
	//mat3 normmat = mat3(transpose(modelViewMatrix));
    N = normalize(mat3(modelViewMatrix) * normal);
	T = vec3(tangent);
	B = cross(normal, T);
	//N = normalize(normmat * normal);
    vUV = texture0;
//    gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );
	gl_Position = modelViewProjectionMatrix * position;
}