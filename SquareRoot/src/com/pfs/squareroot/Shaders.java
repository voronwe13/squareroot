package com.pfs.squareroot;

import java.util.ArrayList;

import android.content.res.Resources;
import android.util.Log;

import com.threed.jpct.GLSLShader;
import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.VertexAttributes;

public class Shaders {
	private static final String TAG = "Shaders";

	static Resources res = SquareRootActivity.activity.getResources();
	
	static String normalmapvs = "" +
			"attribute vec3 position;" + 
			"attribute vec2 texture0;" +
			"attribute vec3 normal;" +

			"varying vec2 vUV;" +
			"varying vec3 N;" +
			"varying vec3 V;" +

			"uniform mat4 modelViewMatrix;" +
			"uniform mat4 modelViewProjectionMatrix;" +
			"uniform mat4 normalMatrix;" +

			"void main( void ){" +
			"" +
			"	V = vec3(modelViewMatrix * vec4(position,1.));" +       
			"	N = normalize(mat3(normalMatrix) * normal);" +
			"	vUV = texture0;" +
			"	gl_Position = modelViewProjectionMatrix * vec4( position, 1.0 );" +
			"}";
	
	static String normalmapfs = Loader.loadTextFile(res.openRawResource(R.raw.normalmapfs));
	static String normalmapfsbt = Loader.loadTextFile(res.openRawResource(R.raw.normalmapfsbt));
	static String phongvs = Loader.loadTextFile(res.openRawResource(R.raw.phongvs));
	static String phongfs = Loader.loadTextFile(res.openRawResource(R.raw.phongfs));
	static String normalmapvs2 = Loader.loadTextFile(res.openRawResource(R.raw.vertexshader_offset));
	static String normalmapfs2 = Loader.loadTextFile(res.openRawResource(R.raw.fragmentshader_offset));
	
	static GLSLShader normalmap, phong;
	static Matrix normalmatrix;
	
	public static void setupShaders(){
		normalmap = new GLSLShader(phongvs, normalmapfsbt);
		phong = new GLSLShader(phongvs, phongfs);
		//normalmap = new GLSLShader(normalmapvs2, normalmapfs2);
		//normalmap.setStaticUniform("invRadius", 0.0003f);
		//normalmap.compile(new ArrayList<VertexAttributes>());
		Log.d(TAG, "normalmap shader is set up");
		normalmatrix = new Matrix();
	}
	
	public static void updateShaders(){
		//Log.d(TAG, "updateShaders called");
//		normalmatrix.setTo(Controls.camera.getBack());
//		Log.d(TAG, "normalmatrix start: "+normalmatrix.toString());
//		normalmatrix = normalmatrix.invert3x3();
//		Log.d(TAG, "normalmatrix inverted: "+normalmatrix.toString());
//		normalmatrix.setTo(normalmatrix.transpose());
//		Log.d(TAG, "normalmatrix inv-trans: "+normalmatrix.toString());
//		normalmap.setUniform("normalMatrix", normalmatrix);
//		normalmap.setUniform("heightScale", 0.05f);
		//Log.d(TAG, "updateShaders finished");
	}
	
}
