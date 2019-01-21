package core.application;


import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import core.application.processing.RenderContext;


public class Display extends Canvas implements KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JFrame 		 m_frame;
	private final RenderContext  m_frameBuffer;
	private final BufferedImage  m_displayImage;
	private final byte[] 		 m_displayComponents;
	private final BufferStrategy m_bufferStrategy;
	private final Graphics 		 m_graphics;
	
	public double mouseX = 0, mouseY = 0;
	
	public Display(int width, int height, String title) 
	{
		Dimension size = new Dimension(width, height);
			
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		m_frameBuffer 		= new RenderContext(width, height);
		m_displayImage 		= new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		m_displayComponents = ((DataBufferByte) m_displayImage.getRaster().getDataBuffer()).getData();
		
		m_frame = new JFrame();
		m_frame.add(this);
		m_frame.pack();
		m_frame.setTitle(title);
		m_frame.setResizable(false);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setLocationRelativeTo(null);
		m_frame.setVisible(true);	
		
		m_frame.addKeyListener(this);
		
		createBufferStrategy(1);
		m_bufferStrategy = getBufferStrategy();
		m_graphics = m_bufferStrategy.getDrawGraphics();
	}
	
	public void SwapBuffers() 
	{
		m_frameBuffer.copyToByteArray(m_displayComponents);
		m_graphics.drawImage(m_displayImage, 0, 0, m_frameBuffer.getWidth(), m_frameBuffer.getHeight(), null);
		m_bufferStrategy.show();
	}
	
	public RenderContext getFrameBuffer() {
		return m_frameBuffer;
	}
	
	public double getMouseX() {
		return m_frame.getMousePosition().x;
	}
	
	public double getMouseY() {
		return m_frame.getMousePosition().y;
	}
	
	public void updateMousePos() {
		mouseX = m_frame.getMousePosition().x;
		mouseY = m_frame.getMousePosition().y;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
