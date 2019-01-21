package core.calculation;

import core.application.Application;
import core.shape.Vertex;

public class Gradients {
	
	private double[] m_texCoordX;
	private double[] m_texCoordY;
	private double[] m_oneOverZ;
	private double[] m_depth;
	private double[] m_lightAmt;

	private double m_texCoordXXStep;
	private double m_texCoordXYStep;
	private double m_texCoordYXStep;
	private double m_texCoordYYStep;
	private double m_oneOverZXStep;
	private double m_oneOverZYStep;
	private double m_depthXStep;
	private double m_depthYStep;
	private double m_lightAmtXStep;
	private double m_lightAmtYStep;
	
	public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert) 
	{
		final double oneOverdX = 1.0d /
				(((midYVert.getX() - maxYVert.getX()) *
				(minYVert.getY() - maxYVert.getY())) -
				((minYVert.getX() - maxYVert.getX()) *
				(midYVert.getY() - maxYVert.getY())));

		final double oneOverdY = -oneOverdX;

		m_oneOverZ 	= new double[3];
		m_texCoordX = new double[3];
		m_texCoordY = new double[3];
		m_depth 	= new double[3];
		m_lightAmt  = new double[3];
			
		m_depth[0] = minYVert.getPosition().getZ();
		m_depth[1] = midYVert.getPosition().getZ();
		m_depth[2] = maxYVert.getPosition().getZ();
		
		
		m_lightAmt[0] = saturate(minYVert.getNormal().dot(Application.lightDir)) * 0.9 + 0.1;
		m_lightAmt[1] = saturate(midYVert.getNormal().dot(Application.lightDir)) * 0.9 + 0.1;
		m_lightAmt[2] = saturate(maxYVert.getNormal().dot(Application.lightDir)) * 0.9 + 0.1;
		
		m_oneOverZ[0] = 1.0d / minYVert.getPosition().getW();
		m_oneOverZ[1] = 1.0d / midYVert.getPosition().getW();
		m_oneOverZ[2] = 1.0d / maxYVert.getPosition().getW();

		m_texCoordX[0] = minYVert.getTexCoords().getX() * m_oneOverZ[0];
		m_texCoordX[1] = midYVert.getTexCoords().getX() * m_oneOverZ[1];
		m_texCoordX[2] = maxYVert.getTexCoords().getX() * m_oneOverZ[2];

		m_texCoordY[0] = minYVert.getTexCoords().getY() * m_oneOverZ[0];
		m_texCoordY[1] = midYVert.getTexCoords().getY() * m_oneOverZ[1];
		m_texCoordY[2] = maxYVert.getTexCoords().getY() * m_oneOverZ[2];

		m_texCoordXXStep = calcXStep(m_texCoordX, minYVert, midYVert, maxYVert, oneOverdX);
		m_texCoordXYStep = calcYStep(m_texCoordX, minYVert, midYVert, maxYVert, oneOverdY);
		m_texCoordYXStep = calcXStep(m_texCoordY, minYVert, midYVert, maxYVert, oneOverdX);
		m_texCoordYYStep = calcYStep(m_texCoordY, minYVert, midYVert, maxYVert, oneOverdY);
		m_oneOverZXStep = calcXStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdX);
		m_oneOverZYStep = calcYStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdY);	
		m_depthXStep = calcXStep(m_depth, minYVert, midYVert, maxYVert, oneOverdX);
		m_depthYStep = calcYStep(m_depth, minYVert, midYVert, maxYVert, oneOverdY);
		m_lightAmtXStep = calcXStep(m_lightAmt, minYVert, midYVert, maxYVert, oneOverdX);
		m_lightAmtYStep = calcYStep(m_lightAmt, minYVert, midYVert, maxYVert, oneOverdY);
	}
	
	private double saturate(double value)
	{
		if(value < 0.0d)
			return 0.0d;
		if(value > 1.0d)
			return 1.0d;
		return value;
	}
	
	private double calcXStep(double[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, double oneOverdX)
	{
		return
			(((values[1] - values[2]) *
			(minYVert.getY() - maxYVert.getY())) -
			((values[0] - values[2]) *
			(midYVert.getY() - maxYVert.getY()))) * oneOverdX;
	}

	private double calcYStep(double[] values, Vertex minYVert, Vertex midYVert,
			Vertex maxYVert, double oneOverdY)
	{
		return
			(((values[1] - values[2]) *
			(minYVert.getX() - maxYVert.getX())) -
			((values[0] - values[2]) *
			(midYVert.getX() - maxYVert.getX()))) * oneOverdY;
	}
	
	public double getTexCoordX(int loc)
	{
		return m_texCoordX[loc];
	}

	public double getTexCoordY(int loc)
	{
		return m_texCoordY[loc];
	}
	
	public double getOneOverZ(int loc)
	{
		return m_oneOverZ[loc];
	}
	
	public double getDepth(int loc)
	{
		return m_depth[loc];
	}
	
	public double getLightAmt(int loc)
	{
		return m_lightAmt[loc];
	}

	public double getTexCoordXXStep()
	{
		return m_texCoordXXStep;
	}

	public double getTexCoordXYStep()
	{
		return m_texCoordXYStep;
	}

	public double getTexCoordYXStep()
	{
		return m_texCoordYXStep;
	}

	public double getTexCoordYYStep()
	{
		return m_texCoordYYStep;
	}
	
	public double getOneOverZXStep()
	{
		return m_oneOverZXStep;
	}

	public double getOneOverZYStep()
	{
		return m_oneOverZYStep;
	}
	
	public double getDepthXStep()
	{
		return m_depthXStep;
	}

	public double getDepthYStep()
	{
		return m_depthYStep;
	}
	
	public double getLightAmtXStep()
	{
		return m_lightAmtXStep;
	}
	
	public double getLightAmtYStep()
	{
		return m_lightAmtYStep;
	}
}
