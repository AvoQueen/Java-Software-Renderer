package core.shape;

import core.calculation.Matrix4f;
import core.calculation.Vector4f;

public class Vertex {
	private Vector4f m_pos;
	private Vector4f m_texCoords;
	private Vector4f m_normal;
	
	public Vertex(Vector4f pos, Vector4f texCoords, Vector4f normal)
	{
		m_pos = pos;
		m_texCoords = texCoords;
		m_normal = normal;
	}

	public double getX() 
	{
		return m_pos.getX();
	}

	public double getY() 
	{
		return m_pos.getY();
	}
	
	public double getZ() 
	{
		return m_pos.getZ();
	}

	public double getW() 
	{
		return m_pos.getW();
	}
	
	public Vector4f getTexCoords() 
	{
		return m_texCoords;
	}
	
	public Vector4f getPosition() 
	{
		return m_pos;
	}
	
	public Vector4f getNormal()
	{
		return m_normal;
	}
	
	public Vertex transform(Matrix4f transform) 
	{
		return new Vertex(transform.transform(m_pos), m_texCoords, transform.transform(m_normal)); //changed
	}
	
	public Vertex perspectiveDivide() 
	{
		final double w = m_pos.getW();
		return new Vertex(new Vector4f(m_pos.getX() / w, m_pos.getY() / w, m_pos.getZ() / w, w), m_texCoords, m_normal); //changed
	}
	
	public int triangleHandedness(Vertex maxYVert, Vertex midYVert) {
		double x1 = maxYVert.getX() - m_pos.getX();
		double y1 = maxYVert.getY() - m_pos.getY();
		double x2 = midYVert.getX() - m_pos.getX();
		double y2 = midYVert.getY() - m_pos.getY();
		
		return x1 * y2 - x2 * y1 < 0 ? 0 : 1;
	}
	
	public double triangleAreaTimesTwo(Vertex a, Vertex b) {
		double x1 = a.getX() - m_pos.getX();
		double y1 = a.getY() - m_pos.getY();
		double x2 = b.getX() - m_pos.getX();
		double y2 = b.getY() - m_pos.getY();
		
		return x1 * y2 - x2 * y1;
	}
	
	public boolean isInsideViewFrustum()
	{
		return
				Math.abs(m_pos.getX()) <= Math.abs(m_pos.getW()) &&
				Math.abs(m_pos.getY()) <= Math.abs(m_pos.getW()) &&
				Math.abs(m_pos.getZ()) <= Math.abs(m_pos.getW());
	}
	
	public Vertex Lerp(Vertex other, double amount)
	{
		return new Vertex(
				m_pos.lerp(other.getPosition(), amount),
				m_texCoords.lerp(other.getTexCoords(), amount),
				m_normal.lerp(other.getNormal(), amount));
	}
	
	public double get(int index)
	{
		switch (index)
		{
		case 0:
			return m_pos.getX();
		case 1:
			return m_pos.getY();
		case 2:
			return m_pos.getZ();
		case 3:
			return m_pos.getW();
		default:
			throw new IndexOutOfBoundsException();
		}
	}
}
