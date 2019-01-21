package core.shape;

import core.calculation.Gradients;

public class Edge {
	
	private double m_x;
	private final double m_xStep;
	private final int m_yStart;
	private final int m_yEnd;
	private double m_texCoordX;
	private final double m_texCoordXStep;
	private double m_texCoordY;
	private final double m_texCoordYStep;
	private double m_oneOverZ;
	private final double m_oneOverZStep;
	private double m_depth;
	private final double m_depthStep;
	private double m_lightAmt;
	private final double m_lightAmtStep;
	
	public Edge(Gradients gradients, Vertex minYVert, Vertex maxYVert, int minYVertIndex)
	{
		m_yStart = (int) Math.ceil(minYVert.getY());
		m_yEnd 	 = (int) Math.ceil(maxYVert.getY());
		
		final double yDist = maxYVert.getY() - minYVert.getY();
		final double xDist = maxYVert.getX() - minYVert.getX(); 
		
		final double yPreStep = m_yStart - minYVert.getY();
		
		m_xStep = (double) xDist / yDist;
		m_x = minYVert.getX() + yPreStep * m_xStep;
		
		final double xPreStep = m_x - minYVert.getX();
		
		m_texCoordX = gradients.getTexCoordX(minYVertIndex) +
				gradients.getTexCoordXXStep() * xPreStep +
				gradients.getTexCoordXYStep() * yPreStep;
		
		m_texCoordXStep = gradients.getTexCoordXYStep() + gradients.getTexCoordXXStep() * m_xStep;

		m_texCoordY = gradients.getTexCoordY(minYVertIndex) +
				gradients.getTexCoordYXStep() * xPreStep +
				gradients.getTexCoordYYStep() * yPreStep;
		
		m_texCoordYStep = gradients.getTexCoordYYStep() + gradients.getTexCoordYXStep() * m_xStep;
		
		m_oneOverZ = gradients.getOneOverZ(minYVertIndex) +
				gradients.getOneOverZXStep() * xPreStep +
				gradients.getOneOverZYStep() * yPreStep;
		m_oneOverZStep = gradients.getOneOverZYStep() + gradients.getOneOverZXStep() * m_xStep;
		
		m_depth = gradients.getDepth(minYVertIndex) +
				gradients.getDepthXStep() * xPreStep +
				gradients.getDepthYStep() * yPreStep;
		m_depthStep = gradients.getDepthYStep() + gradients.getDepthXStep() * m_xStep;
		
		m_lightAmt = gradients.getLightAmt(minYVertIndex) +
				gradients.getLightAmtXStep() * xPreStep +
				gradients.getLightAmtYStep() * yPreStep;
		m_lightAmtStep = gradients.getLightAmtYStep() + gradients.getLightAmtXStep() * m_xStep;
	}
	
	public void step() 
	{
		m_x += m_xStep;
		m_texCoordX += m_texCoordXStep;
		m_texCoordY += m_texCoordYStep;
		m_oneOverZ += m_oneOverZStep;
		m_depth += m_depthStep;
		m_lightAmt += m_lightAmtStep;
	}
	
	
	public double getX() 
	{
		return m_x;
	}
	
	public int getYStart()
	{
		return m_yStart;
	}
	
	public int getYEnd()
	{
		return m_yEnd;
	}
	
	public double getTexCoordX()
	{
		return m_texCoordX;
	}
	
	public double getTexCoordY()
	{
		return m_texCoordY;
	}
	
	public double getOneOverZ() 
	{
		return m_oneOverZ;
	}
	
	public double getDepth() 
	{
		return m_depth;
	}
	
	public double getLightAmt()
	{
		return m_lightAmt;
	}
}
