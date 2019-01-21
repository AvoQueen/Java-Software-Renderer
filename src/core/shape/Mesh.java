package core.shape;

import java.util.ArrayList;
import java.util.List;

import core.application.processing.Bitmap;
import core.application.processing.RenderContext;
import core.calculation.Matrix4f;
import core.calculation.Vector4f;

public class Mesh {
	private List<Vertex> m_vertices;
	private List<Integer> m_indices;

	protected double xRotFactor = 0;
	protected double yRotFactor = 0;
	protected double zRotFactor = 0;

	private Bitmap texture;
	
	public Mesh(IndexedModel model)
	{
		m_vertices = new ArrayList<Vertex>();
		for(int i = 0; i < model.getPositions().size(); i++)
		{
			m_vertices.add(new Vertex(
						model.getPositions().get(i),
						model.getTexCoords().get(i),
						model.getNormals().get(i)));
		}

		m_indices = model.getIndices();
	}

	public Mesh(Bitmap texture)
	{
		this.texture = texture;

		m_vertices = new ArrayList<Vertex>();
		m_indices = new ArrayList<Integer>();
	}

	public Mesh(Vector4f fillColor)
	{
		this.texture = new Bitmap(fillColor);

		m_vertices = new ArrayList<Vertex>();
		m_indices = new ArrayList<Integer>();
	}

	public void setRotate(double xRotFactor, double yRotFactor, double zRotFactor)
	{
		this.xRotFactor = xRotFactor;
		this.yRotFactor = yRotFactor;
		this.zRotFactor = zRotFactor;
	}

	public void setTexture(Bitmap texture)
	{
		this.texture = texture;
	}

	public void draw(RenderContext ctx, double rotation)
	{
		final Matrix4f transform = ctx.getTransformation(xRotFactor * rotation, yRotFactor * rotation,
				zRotFactor * rotation);

		for(int i = 0; i < m_indices.size(); i += 3)
		{
			ctx.drawTriangle(
					m_vertices.get(m_indices.get(i)).transform(transform),
					m_vertices.get(m_indices.get(i + 1)).transform(transform),
					m_vertices.get(m_indices.get(i + 2)).transform(transform),
					texture);
		}
		
	}
	
	public Vertex GetVertex(int i) { return m_vertices.get(i); }
	public int GetIndex(int i) { return m_indices.get(i); }
	public int GetNumIndices() { return m_indices.size(); }
}
