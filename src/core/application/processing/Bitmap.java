package core.application.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import core.calculation.Vector4f;

public class Bitmap {
	// The width, in pixel of the image
	private final int m_width;
	// The height, in pixel of the image
	private final int m_height;
	// Every pixel component in the image
	private final byte[] m_components;

	public Bitmap(int width, int height)
	{
		m_width 	 = width;
		m_height 	 = height;
		m_components = new byte[width * height * 4];
	}
	
	public Bitmap(Vector4f fillColor)
	{
		m_width 	 = 1;
		m_height 	 = 1;
		m_components = new byte[] {
				(byte) fillColor.getX(),
				(byte) fillColor.getY(),
				(byte) fillColor.getZ(),
				(byte) fillColor.getW()
		};
		
	}
	
	public Bitmap(String fileName)
	{
		int width = 0;
		int height = 0;
		byte[] components = null;

		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(fileName));
		} catch (IOException e)
		{
			e.printStackTrace();
			m_width 	 = 0;
			m_height 	 = 0;
			m_components = new byte[0];
			return;
		}

		width = image.getWidth();
		height = image.getHeight();

		final int imgPixels[] = new int[width * height];
		image.getRGB(0, 0, width, height, imgPixels, 0, width);
		components = new byte[width * height * 4];

		for(int i = 0; i < width * height; i++)
		{
			int pixel = imgPixels[i];

			components[i * 4]     = (byte)((pixel >> 24) & 0xFF); // A
			components[i * 4 + 1] = (byte)((pixel      ) & 0xFF); // B
			components[i * 4 + 2] = (byte)((pixel >> 8 ) & 0xFF); // G
			components[i * 4 + 3] = (byte)((pixel >> 16) & 0xFF); // R
		}

		m_width = width;
		m_height = height;
		m_components = components;
}

	public void clear(byte shade)
	{
		Arrays.fill(m_components, shade);
	}

	public void drawPixel(int x, int y, byte a, byte b, byte g, byte r)
	{
		final int index = (x + y * m_width) * 4;
		m_components[index	  ] = a;
		m_components[index + 1] = b;
		m_components[index + 2] = g;
		m_components[index + 3] = r;
	}

	public void drawPixel(int x, int y, int a, int b, int g, int r)
	{
		final int index = (x + y * m_width) * 4;
		m_components[index	  ] = (byte) a;
		m_components[index + 1] = (byte) b;
		m_components[index + 2] = (byte) g;
		m_components[index + 3] = (byte) r;
	}

	public void drawPixel_old(int x, int y, byte a, byte r, byte g, byte b)
	{
		final int index = (x + y * m_width) * 4;
		m_components[index] = a;
		m_components[index + 1] = r;
		m_components[index + 2] = g;
		m_components[index + 3] = b;
	}

	public void copyPixel(int destX, int destY, int srcX, int srcY, Bitmap src, double lightAmt)
	{
		final int destIndex = (destX + destY * m_width) * 4;
		final int srcIndex = (srcX + srcY * src.getWidth()) * 4;
		
		try 
		{
		m_components[destIndex	  ] = (byte) ((src.getComponent(srcIndex) & 0xFF) * lightAmt);
		m_components[destIndex + 1] = (byte) ((src.getComponent(srcIndex + 1) & 0xFF) * lightAmt);
		m_components[destIndex + 2] = (byte) ((src.getComponent(srcIndex + 2) & 0xFF) * lightAmt);
		m_components[destIndex + 3] = (byte) ((src.getComponent(srcIndex + 3) & 0xFF) * lightAmt);
		} 
		catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	public void copyToByteArray(byte[] dest)
	{
		for (int i = 0; i < m_width * m_height; i++)
		{
			dest[i * 3	  ] = m_components[i * 4 + 1];
			dest[i * 3 + 1] = m_components[i * 4 + 2];
			dest[i * 3 + 2] = m_components[i * 4 + 3];
		}
	}

	public void copyToIntArray(int[] dest)
	{
		for (int i = 0; i < m_width * m_height; i++)
		{
			int a = ((int) m_components[i * 4	 ]) << 24;
			int r = ((int) m_components[i * 4 + 1]) << 16;
			int g = ((int) m_components[i * 4 + 2]) << 8;
			int b = ((int) m_components[i * 4 + 3]);

			dest[i] = a | r | g | b;
		}
	}

	public int getWidth()
	{
		return m_width;
	}

	public int getHeight()
	{
		return m_height;
	}

	public byte[] getComponents()
	{
		return m_components;
	}
	
	public byte getComponent(int index) 
	{
		return m_components[index];
	}

}
