package core.application.processing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.calculation.Gradients;
import core.calculation.Matrix4f;
import core.calculation.Vector4f;
import core.shape.Edge;
import core.shape.Vertex;

public class RenderContext extends Bitmap{
	
	private double m_zBuffer[];
	
	private double FOV 		 = Math.toRadians(70);
	private double nearPlane = 0.1;
	private double farPlane  = 1000;
	
	public Matrix4f translation;
	public Matrix4f projection;
	
	public RenderContext(int width, int height) 
	{
		super(width, height);
		
		m_zBuffer = new double[width * height];
		
		translation = new Matrix4f().initTranslation(0, 0, 0);
		projection = new Matrix4f().initPerspective(FOV, width / height, nearPlane, farPlane);
	}
	
	public void clearDepthBuffer()
	{
		for(int i=0; i < m_zBuffer.length; i++)
		{
			m_zBuffer[i] = Double.MAX_VALUE;
		}
	}
	
	public void drawTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture) 
	{
		if(v1.isInsideViewFrustum() && v2.isInsideViewFrustum() && v3.isInsideViewFrustum())
		{
			fillTriangle(v1, v2, v3, texture);
			return;
		}
		
		
		List<Vertex> vertices = new ArrayList<>();
		List<Vertex> auxillaryList = new ArrayList<>();
		
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		
		if(clipPolygonAxis(vertices, auxillaryList, 0) &&
				clipPolygonAxis(vertices, auxillaryList, 1) &&
				clipPolygonAxis(vertices, auxillaryList, 2))
		{
			final Vertex initalVertex = vertices.get(0);
			
			for(int i = 1; i < vertices.size() - 1; i++)
			{
				fillTriangle(initalVertex, vertices.get(i), vertices.get(i + 1), texture);
			}
		}
	}
	
	private boolean clipPolygonAxis(List<Vertex> vertices, List<Vertex> auxillaryList, int componentIndex)
	{
		clipPolygonComponents(vertices, componentIndex, 1.0d, auxillaryList);
		vertices.clear();
		
		if(auxillaryList.isEmpty())
		{
			return false;
		}
		
		clipPolygonComponents(auxillaryList, componentIndex, -1.0d, vertices);
		auxillaryList.clear();
		
		return !vertices.isEmpty();
	}
	
	private void clipPolygonComponents(List<Vertex> vertices, int componentIndex, double componentFactor,
			List<Vertex> result)
	{
		Vertex previousVertex = vertices.get(vertices.size() - 1);
		double previousComponent = previousVertex.get(componentIndex) * componentFactor;
		boolean previousInside = previousComponent <= previousVertex.getPosition().getW();
		
		Iterator<Vertex> iterator = vertices.iterator();
		while(iterator.hasNext())
		{
			Vertex currentVertex = iterator.next();
			double currentComponent = currentVertex.get(componentIndex) * componentFactor;
			boolean currentInside = currentComponent <= currentVertex.getPosition().getW();
			
			if(currentInside ^ previousInside)
			{
				final double lerpAmt = (previousVertex.getPosition().getW() - previousComponent) / 
						((previousVertex.getPosition().getW() - previousComponent) - 
						(currentVertex.getPosition().getW() - currentComponent));
				
				result.add(previousVertex.Lerp(currentVertex, lerpAmt));
			}
			
			if(currentInside)
			{
				result.add(currentVertex);
			}
			
			previousVertex = currentVertex;
			previousComponent = currentComponent;
			previousInside = currentInside;
		}
	}
	
	private void fillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture) 
	{
		final Matrix4f screenSpaceTransform = new Matrix4f().initScreenSpaceTransform(getWidth() / 2, getHeight() / 2);
		
		Vertex minYVert = v1.transform(screenSpaceTransform).perspectiveDivide();
		Vertex midYVert = v2.transform(screenSpaceTransform).perspectiveDivide();
		Vertex maxYVert = v3.transform(screenSpaceTransform).perspectiveDivide();
		
		//back-face-culling start
			if(minYVert.triangleAreaTimesTwo(maxYVert, midYVert) >= 0)
			{
				return;
			}
		//back-face-culling end
		
		if(maxYVert.getY() < midYVert.getY())
		{
			final Vertex temp = maxYVert;
			maxYVert = midYVert;
			midYVert = temp;
		}
		
		if(midYVert.getY() < minYVert.getY())
		{
			final Vertex temp = midYVert;
			midYVert = minYVert;
			minYVert = temp;
		}
		
		if(maxYVert.getY() < midYVert.getY())
		{
			final Vertex temp = maxYVert;
			maxYVert = midYVert;
			midYVert = temp;
		}
		
		scanTriangle(minYVert, midYVert, maxYVert, minYVert.triangleAreaTimesTwo(maxYVert, midYVert) >= 0, texture);
	}
	
	private void scanTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean handedness, Bitmap texture)
	{
		Gradients gradients = new Gradients(minYVert, midYVert, maxYVert);
		Edge topToBottom 	= new Edge(gradients, minYVert, maxYVert, 0);
		Edge topToMiddle 	= new Edge(gradients, minYVert, midYVert, 0);
		Edge middleToBottom = new Edge(gradients, midYVert, maxYVert, 1);

		scanEdges(gradients, topToBottom, topToMiddle, handedness, texture);
		scanEdges(gradients, topToBottom, middleToBottom, handedness, texture);
	}
	
	private void scanEdges(Gradients gradients, Edge a, Edge b, boolean handedness, Bitmap texture) 
	{
		Edge left = a;
		Edge right = b;
		
		if(handedness)
		{
			Edge temp = left;
			left = right;
			right = temp;
		}
		
		final int yStart = b.getYStart();
		final int yEnd = b.getYEnd();
		
		for(int j = yStart; j < yEnd; j++)
		{
			drawScanLine(gradients, left, right, j, texture);
			left.step();
			right.step();
		}
	}
	
	private void drawScanLine(Gradients gradients, Edge left, Edge right, int j, Bitmap texture) 
	{
		final int xMin = (int) Math.ceil(left.getX());
		final int xMax = (int) Math.ceil(right.getX());
		
		final double xPreStep = xMin - left.getX();
		
//		final double xDist = right.getX() - left.getX();
//		final double texCoordXXStep = (right.getTexCoordX() - left.getTexCoordX()) / xDist;
//		final double texCoordYXStep = (right.getTexCoordY() - left.getTexCoordY()) / xDist;
//		final double oneOverZXStep 	= (right.getOneOverZ() - left.getOneOverZ()) / xDist;
//		final double depthXStep 	= (right.getDepth() - left.getDepth()) / xDist;
//		final double lightAmtXStep 	= (right.getLightAmt() - left.getLightAmt()) / xDist; 
		
		final double texCoordXXStep = gradients.getTexCoordXXStep();
		final double texCoordYXStep = gradients.getTexCoordYXStep();
		final double oneOverZXStep 	= gradients.getOneOverZXStep();
		final double depthXStep 	= gradients.getDepthXStep();
		final double lightAmtXStep 	= gradients.getLightAmtXStep();
		
		double texCoordX = left.getTexCoordX() + texCoordXXStep * xPreStep;
		double texCoordY = left.getTexCoordY() + texCoordYXStep * xPreStep;
		double oneOverZ  = left.getOneOverZ() + oneOverZXStep * xPreStep;
		double depth 	 = left.getDepth() + depthXStep * xPreStep;
		double lightAmt  = left.getLightAmt() + lightAmtXStep * xPreStep;
		
		for(int i = xMin; i < xMax; i++)
		{	
			final int index = i + j * getWidth();
			
			if(depth < m_zBuffer[index])
			{
				m_zBuffer[index] = depth;
				final double z = 1 / oneOverZ;
				final int srcX = (int) ((texCoordX * z) * (texture.getWidth() - 1) + 0.5);
				final int srcY = (int) ((texCoordY * z) * (texture.getHeight() - 1) + 0.5);
		
				copyPixel(i, j, srcX, srcY, texture, lightAmt);
			}
			
			oneOverZ += oneOverZXStep;
			texCoordX += texCoordXXStep;
			texCoordY += texCoordYXStep;
			depth += depthXStep;
			lightAmt += lightAmtXStep;
		}	
	}
	
	public void setTranslation(double x, double y, double z) {
		translation = new Matrix4f().initTranslation(x, y, z);
	}
	
	public Matrix4f getTransformation(double xRot, double yRot, double zRot) {
		final Matrix4f rotation = new Matrix4f().initRotation(xRot, yRot, zRot);
		
		return projection.mult(translation.mult(rotation));
	}

}
