#extension GL_OES_standard_derivatives : require

precision mediump float;

// The variable naming scheme is prefixing
// an 'a' for attributes, 'v' for varying, and 'u' for uniform.
varying vec2 vUV;
varying vec3 N;
varying vec3 V;
varying vec3 T;
varying vec3 B;

/*
uniform sampler2D uSampler;
uniform vec3 ambient;
uniform vec3 diffuse;
uniform vec3 specular;
uniform float shininess;
uniform vec3 lightsource;
*/

uniform sampler2D textureUnit0;
uniform sampler2D textureUnit1;
uniform vec4 ambientColor;
uniform vec3 diffuseColors[8];
uniform vec3 specularColors[8];
uniform float shininess;
uniform vec3 lightPositions[8];

void main( void )
{

   vec3 texnormal = vec3(texture2D( textureUnit1, vec2( vUV.s, vUV.t )));
   texnormal = texnormal*2. - 1.;

   vec3 Tnorm = normalize(T);
   vec3 Bnorm = normalize(B);
	mat3 M = mat3(Tnorm,  N, Bnorm);
   vec3 newnormal = normalize(M*texnormal);
   vec3 L = normalize(lightPositions[0] - V);   
   vec3 E = normalize(-V); // we are in Eye Coordinates, so EyePos is (0,0,0)  
   vec3 R = normalize(-reflect(L, texnormal));
 
//   vec3 R = normalize(-reflect(L,N));    

   //calculate Diffuse Term:  
   vec4 Idiff = vec4(diffuseColors[0] * max(dot(N,L), 0.0),1.);
   Idiff = clamp(Idiff, 0.0, 1.0);     
   
   // calculate Specular Term:
   vec4 Ispec = vec4(specularColors[0] * pow(max(dot(R,E),0.0),50.1),1.);
   Ispec = clamp(Ispec, 0.0, 1.0); 
   // write Total Color:
   
/*  
//   gl_FragColor = Iamb + texture2D( textureUnit0, vec2( vUV.s, vUV.t ) ) * Idiff + Ispec;
*/
	gl_FragColor = ambientColor + texture2D( textureUnit0, vec2( vUV.s, vUV.t ) )*Idiff + Ispec;
}