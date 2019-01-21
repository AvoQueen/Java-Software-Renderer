package core.calculation;

public class Vector4f {
	
	private double x;
	private double y;
	private double z;
	private double w;

	public Vector4f(double x, double y, double z, double w) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4f(Vector4f v) 
	{
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.w = v.getW();
	}

	public String toString() 
	{
		return "(" + x + " | " + y + " | " + z + " | " + w + ")";
	}

	public double length() 
	{
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public double dot(Vector4f v) 
	{
		return x * v.getX() + y * v.getY() + z * v.getZ() + w * v.getW();
	}
	
	public Vector4f cross(Vector4f v) 
	{
		final double x_ = y * v.getZ() - z * v.getY();
		final double y_ = z * v.getX() - x * v.getZ();
		final double z_ = x * v.getY() - y * v.getX();
		
		return new Vector4f(x_, y_, z_, 0);
	}
	
	public Vector4f lerp(Vector4f dest, double lerpFactor)
	{
		return dest.sub(this).mult(lerpFactor).add(this);
	}

	public Vector4f normalize() 
	{
		final double length = length();

		x /= length;
		y /= length;
		z /= length;
		w /= length;

		return this;
	}
	
	public Vector4f normalized() 
	{
		final double length = length();

		return new Vector4f(x / length, y / length, z / length, w / length);
	}

	public Vector4f rotate(Vector4f axis, double angle) 
	{		
		final double sin = Math.sin(-angle);
		final double cos = Math.cos(-angle);
		
		return cross(axis.mult(sin)).add((mult(cos)).add(axis.mult(dot(axis.mult(1- cos)))));
	}

	public Vector4f add(Vector4f v) 
	{
		return new Vector4f(x + v.getX(), y + v.getY(), z + v.getZ(), w + v.getW());
	}

	public Vector4f add(double n) 
	{
		return new Vector4f(x + n, y + n, z + n, w + n);
	}

	public Vector4f sub(Vector4f v) 
	{
		return new Vector4f(x - v.getX(), y - v.getY(), z - v.getZ(), w - v.getW());
	}

	public Vector4f sub(double n) 
	{
		return new Vector4f(x - n, y - n, z - n, w - n);
	}

	public Vector4f mult(Vector4f v) 
	{
		return new Vector4f(x * v.getX(), y * v.getY(), z * v.getZ(), w * v.getW());
	}

	public Vector4f mult(double n) 
	{
		return new Vector4f(x * n, y * n, z * n, w * n);
	}

	public Vector4f div(Vector4f v) 
	{
		return new Vector4f(x / v.getX(), y / v.getY(), z / v.getZ(), w / v.getW());
	}

	public Vector4f div(double n) 
	{
		return new Vector4f(x / n, y / n, z / n, w / n);
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
	
	public double getZ() 
	{
		return z;
	}

	public void setZ(double z) 
	{
		this.z = z;
	}
	
	public double getW() 
	{
		return w;
	}

	public void setW(double w) 
	{
		this.w = w;
	}
}
