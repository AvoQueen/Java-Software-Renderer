package core;

import core.application.Application;
import core.application.processing.Bitmap;
import core.calculation.Vector4f;
import core.shape.Mesh;
import core.shape.MeshBuilder;
import core.shape.MeshType;
import core.shape.Triangle;
import core.shape.Vertex;

public class Main {

	public static void main(String[] args) {
		
		Application renderer = new Application(600, 600, "AvoRenderer");
		renderer.setTranslation(0, 0, 25);
		
//		Vertex v4 = new Vertex(new Vector4f(-4, -4, 0, 1), new Vector4f(0,1,0,0));
//		Vertex v5 = new Vertex(new Vector4f(0, 4, 0, 1), new Vector4f(0.5,0,0,0));
//		Vertex v6 = new Vertex(new Vector4f(4, -4, 0, 1), new Vector4f(1,1,0,0));
//		
//		Triangle triangle = new Triangle(v5, v4, v6);
//		triangle.setTexture("textures/debug.jpg");
//		triangle.setRotate(0, 1, 0);
		
		Mesh mesh = MeshBuilder.create("meshes/trex.obj", new Bitmap("textures/debug.jpg"), MeshType.OBJ);
		mesh.setRotate(0, 1, 0);
		
		//renderer.addShape(triangle);
		renderer.addMesh(mesh);
		
		renderer.start();
	}

}
