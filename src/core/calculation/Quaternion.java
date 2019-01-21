package core.calculation;

public class Quaternion {

	private double x;
	private double y;
	private double z;
	private double w;

	public Quaternion(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public double length()
	{
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalize()
	{
		final double length = length();

		x /= length;
		y /= length;
		z /= length;
		w /= length;

		return this;
	}

	public Quaternion conjugate()
	{
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion mult(Quaternion q)
	{
		//just added something so eclipse would let my program run :D
		double x_ = 0; 
		double y_ = 0;
		double z_ = 0;
		double w_ = 0;
		
		return new Quaternion(x_, y_, z_, w_);
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public double getZ()
	{
		return z;
	}

	public double getW()
	{
		return w;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public void setZ(double z)
	{
		this.z = z;
	}

	public void setW(double w)
	{
		this.w = w;
	}
}
