package core.shape;

import core.application.processing.Bitmap;
import core.application.processing.RenderContext;
import core.calculation.Matrix4f;

public class Triangle extends Shape {
	private Bitmap texture = new Bitmap(1,1);

	public Triangle(Vertex v1, Vertex v2, Vertex v3)
	{
		super(new Vertex[] { v1, v2, v3 });
	}
	
	public void setTexture(String path) {
		texture = new Bitmap(path);
	}
	
	@Override
	public void draw(RenderContext ctx, double rotation) {
		final Matrix4f transform = ctx.getTransformation(
				xRotFactor * rotation,
				yRotFactor * rotation,
				zRotFactor * rotation);
		
		ctx.drawTriangle(
				vertecies[0].transform(transform),
				vertecies[1].transform(transform),
				vertecies[2].transform(transform),
				texture);
	}
}
