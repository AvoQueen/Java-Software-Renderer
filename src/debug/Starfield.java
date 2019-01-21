package debug;

import core.application.processing.RenderContext;
import core.calculation.Vector4f;

public class Starfield {
	private final double m_spread;
	private final double m_speed;
	
	private final Vector4f[] m_star;
	
	public Starfield(int numberOfStars, double spread, double speed) 
	{
		m_spread = spread;
		m_speed  = speed;
		
		m_star  = new Vector4f[numberOfStars];
		
		for(int i = 0; i < numberOfStars; i++) 
		{	
			initStar(i);
		}
	}
	
	public void initStar(int index)
	{		
		m_star[index] = new Vector4f(2 * (Math.random() - 0.5) * m_spread,
						2 * (Math.random() - 0.5) * m_spread,
						(Math.random() + 1E-4) * m_spread, 1);
	}
	
	public void updateAndRender(RenderContext target, float delta) 
	{
		final double tanHalfFOV = Math.tan(Math.toRadians(70));
		
		target.clear((byte) 0x00);
		
		final int halfWidth = target.getWidth() / 2;
		final int halfHeight = target.getHeight() / 2;
		
		for(int i = 0; i < m_star.length; i++) 
		{
			m_star[i].setZ(m_star[i].getZ() - delta * m_speed);
			
			if(m_star[i].getZ() <= 0)
			{
				initStar(i);
			}
			
			final int x = (int)((m_star[i].getX() / (tanHalfFOV * m_star[i].getZ())) * halfWidth + halfWidth);
			final int y = (int)((m_star[i].getY() / (tanHalfFOV * m_star[i].getZ())) * halfHeight + halfHeight);
			
			if( x < 0 || x >= target.getWidth() || 
					y < 0 || y >= target.getHeight()) 
			{
				initStar(i);
			} 
			else 
			{
				target.drawPixel(x, y, 255, i % 255, 255, i % 255);
			}
			
		}
	}
}
