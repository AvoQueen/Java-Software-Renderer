package core.application;

import java.util.ArrayList;

import core.application.processing.RenderContext;
import core.calculation.Matrix4f;
import core.calculation.Vector4f;
import core.shape.Mesh;
import core.shape.Shape;

public class Application {

	private Display display;
	private RenderContext ctx;

	private double deltaT = 0.0d;
	private double rotationValue = 0;
	private boolean isRunning = false;
	private long previousTime;
	private long currentTime;
	
	public static Vector4f lightDir = new Vector4f(0, 0, -1, 0);

	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<Mesh> meshes = new ArrayList<Mesh>();

	public Application(int width, int height, String title)
	{
		this.display = new Display(width, height, title);
		this.ctx = display.getFrameBuffer();
	}

	public void run()
	{
		previousTime = System.nanoTime();
		while (isRunning)
		{
			currentTime = System.nanoTime();
			deltaT = (currentTime - previousTime) / 1E9;
			previousTime = currentTime;
			
			rotationValue += deltaT;
			
			//setTranslation(0, 0, 3 - Math.sin(rotationValue) * 2.5); //just playin around

			ctx.clearDepthBuffer();
			ctx.clear((byte) 0);
			//display.updateMousePos();

			render();
		}
	}

	public void start()
	{
		isRunning = true;
		run();
	}

	public void stop()
	{
		isRunning = false;
	}

	public void render()
	{
		for (Mesh mesh : meshes)
		{
			mesh.draw(ctx, rotationValue);
		}
		
		for (Shape shape : shapes) 
		{
			shape.draw(ctx, rotationValue);
		}
		
		display.SwapBuffers();
	}

	public void setTranslation(double x, double y, double z)
	{
		ctx.setTranslation(x, y, z);
	}

	public Matrix4f getTransformation(double xRot, double yRot, double zRot)
	{
		return ctx.getTransformation(xRot, yRot, zRot);
	}
	
	public void addShape(Shape s) {
		shapes.add(s);
	}
	
	public void removeShape(Shape s) {
		shapes.remove(s);
	}
	
	public void addMesh(Mesh m) {
		meshes.add(m);
	}
	
	public void removeMesh(Mesh m) {
		meshes.remove(m);
	}
}
