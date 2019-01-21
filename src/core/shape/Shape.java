package core.shape;

import core.application.processing.RenderContext;

public abstract class Shape {
	protected Vertex[] vertecies;
	protected double xRotFactor = 0;
	protected double yRotFactor = 0;
	protected double zRotFactor = 0;

	public Shape(Vertex[] vertecies)
	{
		this.vertecies = vertecies;
	}

	public void setRotate(double xRotFactor, double yRotFactor, double zRotFactor)
	{
		this.xRotFactor = xRotFactor;
		this.yRotFactor = yRotFactor;
		this.zRotFactor = zRotFactor;
	}

	public abstract void draw(RenderContext ctx, double rotation);

}
