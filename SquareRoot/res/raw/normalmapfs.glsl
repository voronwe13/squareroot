#extension GL_OES_standard_derivatives : require

precision mediump float;

// The variable naming scheme is prefixing
// an 'a' for attributes, 'v' for varying, and 'u' for uniform.
varying vec2 vUV;
varying vec3 N;
varying vec3 V;

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

   vec3 dxyzx = vec3(dFdx(V.x), dFdx(V.y), dFdx(V.z));
   vec3 dxyzy = vec3(dFdy(V.x), dFdy(V.y), dFdy(V.z));
   vec2 duvx = vec2(dFdx(vUV.s),dFdx(vUV.t));
   vec2 duvy = vec2(dFdy(vUV.s),dFdy(vUV.t));

   float det = 1./(duvx.x*duvy.y - duvy.x*duvx.y);
   vec2 uvinvcol1 = vec2(duvy.y, -duvy.x);
   vec2 uvinvcol2 = vec2(-duvx.y, duvx.x);
   uvinvcol1 = uvinvcol1*det;
   uvinvcol2 = uvinvcol2*det;
   vec3 mcol1 = vec3(uvinvcol1.x * dxyzx.x + uvinvcol1.y * dxyzy.x,
					 uvinvcol1.x * dxyzx.y + uvinvcol1.y * dxyzy.y,
					 uvinvcol1.x * dxyzx.z + uvinvcol1.y * dxyzy.z);
   vec3 mcol2 = vec3(uvinvcol2.x * dxyzx.x + uvinvcol2.y * dxyzy.x,
					 uvinvcol2.x * dxyzx.y + uvinvcol2.y * dxyzy.y,
					 uvinvcol2.x * dxyzx.z + uvinvcol2.y * dxyzy.z);   
   
   mat3 M = mat3(mcol1, mcol2, N);
   vec3 newnormal = normalize(M*texnormal);
   vec3 L = normalize(lightPositions[0] - V);   
   vec3 E = normalize(-V); // we are in Eye Coordinates, so EyePos is (0,0,0)  
   vec3 R = normalize(-reflect(L, newnormal));
 
//   vec3 R = normalize(-reflect(L,N));    

   //calculate Diffuse Term:  
   vec4 Idiff = vec4(diffuseColors[0] * max(dot(N,L), 0.0),1.);
   Idiff = clamp(Idiff, 0.0, 1.0);     
   
   // calculate Specular Term:
   vec4 Ispec = vec4(specularColors[0] * pow(max(dot(R,E),0.0),50.),1.);
   Ispec = clamp(Ispec, 0.0, 1.0); 
   // write Total Color:
   
/*  
//   gl_FragColor = Iamb + texture2D( textureUnit0, vec2( vUV.s, vUV.t ) ) * Idiff + Ispec;
*/
	gl_FragColor = ambientColor + texture2D( textureUnit0, vec2( vUV.s, vUV.t ) )*Idiff + Ispec;
}