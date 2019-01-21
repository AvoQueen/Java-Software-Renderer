package core.calculation;

public class Vector2f {
	
	private double x;
	private double y;

	public Vector2f(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f v) 
	{
		this.x = v.getX();
		this.y = v.getY();
	}

	public String toString() 
	{
		return "(" + x + " | " + y + ")";
	}

	public double length() 
	{
		return Math.sqrt(x * x + y * y);
	}

	public double dot(Vector2f v) 
	{
		return x * v.getX() + y * v.getY();
	}
	
	public double cross(Vector2f v) 
	{
		return x * v.getY() - v.getX() * y;
	}

	public Vector2f normalize() 
	{
		final double length = length();

		x /= length;
		y /= length;

		return this;
	}

	public Vector2f rotate(double angle) 
	{
		final double rad = Math.toRadians(angle);
		final double cos = Math.cos(rad);
		final double sin = Math.sin(rad);
		
		return new Vector2f(x * cos - y * sin, x * sin + y * cos);
	}

	public Vector2f add(Vector2f v) 
	{
		return new Vector2f(x + v.getX(), y + v.getY());
	}

	public Vector2f add(double n) 
	{
		return new Vector2f(x + n, y + n);
	}

	public Vector2f sub(Vector2f v) 
	{
		return new Vector2f(x - v.getX(), y - v.getY());
	}

	public Vector2f sub(double n) 
	{
		return new Vector2f(x - n, y - n);
	}

	public Vector2f mult(Vector2f v) 
	{
		return new Vector2f(x * v.getX(), y * v.getY());
	}

	public Vector2f mult(double n) 
	{
		return new Vector2f(x * n, y * n);
	}

	public Vector2f div(Vector2f v) 
	{
		return new Vector2f(x / v.getX(), y / v.getY());
	}

	public Vector2f div(double n) 
	{
		return new Vector2f(x / n, y / n);
	}

	public double getX() 
	{
		return x;
	}

	public void setX(double x) 
	{
		this.x = x;
	}

	public double getY() 
	{
		return y;
	}

	public void setY(double y) 
	{
		this.y = y;
	}
}
