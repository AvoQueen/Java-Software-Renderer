package core.shape;

import java.io.IOException;

import core.application.processing.Bitmap;

public abstract class MeshBuilder {

	public static Mesh create(String filename, Bitmap texture, MeshType type)
	{
		switch (type)
		{
		case OBJ:
			return MeshBuilder.createMeshFromOBJModel(filename, texture);
		case OFF:
			break;
		case PYL:
			break;
		case STL:
			break;
		default:
			break;
		}
		return null;
	}
	
	private static Mesh createMeshFromOBJModel(String filename, Bitmap texture) 
	{
		Mesh result = null;
		try
		{
			IndexedModel model = new OBJModel(filename).ToIndexedModel();
			result = new Mesh(model);
			result.setTexture(texture);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
