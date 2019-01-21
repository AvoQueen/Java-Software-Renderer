package core.calculation;

public class Matrix4f
{
	private double[][] m;

	public Matrix4f()
	{
		m = new double[4][4];
	}

	public Matrix4f initIdentity()
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initScreenSpaceTransform(double halfWidth, double halfHeight)
	{
		m[0][0] = halfWidth;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = halfWidth - 0.5;
		m[1][0] = 0;	m[1][1] = -halfHeight;	m[1][2] = 0;	m[1][3] = halfHeight - 0.5;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initTranslation(double x, double y, double z)
	{
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(double x, double y, double z, double angle)
	{
		final double sin = (double)Math.sin(angle);
		final double cos = (double)Math.cos(angle);

		m[0][0] = cos+x*x*(1-cos); m[0][1] = x*y*(1-cos)-z*sin; m[0][2] = x*z*(1-cos)+y*sin; m[0][3] = 0;
		m[1][0] = y*x*(1-cos)+z*sin; m[1][1] = cos+y*y*(1-cos);	m[1][2] = y*z*(1-cos)-x*sin; m[1][3] = 0;
		m[2][0] = z*x*(1-cos)-y*sin; m[2][1] = z*y*(1-cos)+x*sin; m[2][2] = cos+z*z*(1-cos); m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(double x, double y, double z)
	{
		Matrix4f rx = new Matrix4f();
		Matrix4f ry = new Matrix4f();
		Matrix4f rz = new Matrix4f();

		rz.m[0][0] = (double)Math.cos(z);rz.m[0][1] = -(double)Math.sin(z);rz.m[0][2] = 0;				rz.m[0][3] = 0;
		rz.m[1][0] = (double)Math.sin(z);rz.m[1][1] = (double)Math.cos(z);rz.m[1][2] = 0;					rz.m[1][3] = 0;
		rz.m[2][0] = 0;					rz.m[2][1] = 0;					rz.m[2][2] = 1;					rz.m[2][3] = 0;
		rz.m[3][0] = 0;					rz.m[3][1] = 0;					rz.m[3][2] = 0;					rz.m[3][3] = 1;

		rx.m[0][0] = 1;					rx.m[0][1] = 0;					rx.m[0][2] = 0;					rx.m[0][3] = 0;
		rx.m[1][0] = 0;					rx.m[1][1] = (double)Math.cos(x);rx.m[1][2] = -(double)Math.sin(x);rx.m[1][3] = 0;
		rx.m[2][0] = 0;					rx.m[2][1] = (double)Math.sin(x);rx.m[2][2] = (double)Math.cos(x);rx.m[2][3] = 0;
		rx.m[3][0] = 0;					rx.m[3][1] = 0;					rx.m[3][2] = 0;					rx.m[3][3] = 1;

		ry.m[0][0] = (double)Math.cos(y);ry.m[0][1] = 0;					ry.m[0][2] = -(double)Math.sin(y);ry.m[0][3] = 0;
		ry.m[1][0] = 0;					ry.m[1][1] = 1;					ry.m[1][2] = 0;					ry.m[1][3] = 0;
		ry.m[2][0] = (double)Math.sin(y);ry.m[2][1] = 0;					ry.m[2][2] = (double)Math.cos(y);ry.m[2][3] = 0;
		ry.m[3][0] = 0;					ry.m[3][1] = 0;					ry.m[3][2] = 0;					ry.m[3][3] = 1;

		m = rz.mult(ry.mult(rx)).getM();

		return this;
	}

	public Matrix4f initScale(double x, double y, double z)
	{
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initPerspective(double fov, double aspectRatio, double zNear, double zFar)
	{
		final double tanHalfFOV = (double)Math.tan(fov / 2);
		final double zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	m[0][1] = 0;					m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;						m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;						m[2][1] = 0;					m[2][2] = (-zNear -zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;						m[3][1] = 0;					m[3][2] = 1;	m[3][3] = 0;


		return this;
	}

	public Matrix4f initOrthographic(double left, double right, double bottom, double top, double near, double far)
	{
		final double width = right - left;
		final double height = top - bottom;
		final double depth = far - near;

		m[0][0] = 2/width;m[0][1] = 0;	m[0][2] = 0;	m[0][3] = -(right + left)/width;
		m[1][0] = 0;	m[1][1] = 2/height;m[1][2] = 0;	m[1][3] = -(top + bottom)/height;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = -2/depth;m[2][3] = -(far + near)/depth;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;

		return this;
	}

	public Matrix4f initRotation(Vector4f forward, Vector4f up)
	{
		Vector4f f = forward.normalized();

		Vector4f r = up.normalized();
		r = r.cross(f);

		Vector4f u = f.cross(r);

		return initRotation(f, u, r);
	}

	public Matrix4f initRotation(Vector4f forward, Vector4f up, Vector4f right)
	{
		Vector4f f = forward;
		Vector4f r = right;
		Vector4f u = up;

		m[0][0] = r.getX();	m[0][1] = r.getY();	m[0][2] = r.getZ();	m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY();	m[1][2] = u.getZ();	m[1][3] = 0;
		m[2][0] = f.getX();	m[2][1] = f.getY();	m[2][2] = f.getZ();	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;

		return this;
	}

	public Vector4f transform(Vector4f r)
	{
		return new Vector4f(m[0][0] * r.getX() + m[0][1] * r.getY() + m[0][2] * r.getZ() + m[0][3] * r.getW(),
		                    m[1][0] * r.getX() + m[1][1] * r.getY() + m[1][2] * r.getZ() + m[1][3] * r.getW(),
		                    m[2][0] * r.getX() + m[2][1] * r.getY() + m[2][2] * r.getZ() + m[2][3] * r.getW(),
							m[3][0] * r.getX() + m[3][1] * r.getY() + m[3][2] * r.getZ() + m[3][3] * r.getW());
	}

	public Matrix4f mult(Matrix4f r)
	{
		Matrix4f res = new Matrix4f();

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				res.set(i, j, m[i][0] * r.get(0, j) +
						m[i][1] * r.get(1, j) +
						m[i][2] * r.get(2, j) +
						m[i][3] * r.get(3, j));
			}
		}

		return res;
	}

	public double[][] getM()
	{
		double[][] res = new double[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				res[i][j] = m[i][j];

		return res;
	}

	public double get(int x, int y)
	{
		return m[x][y];
	}

	public void setM(double[][] m)
	{
		this.m = m;
	}

	public void set(int x, int y, double value)
	{
		m[x][y] = value;
	}
}
