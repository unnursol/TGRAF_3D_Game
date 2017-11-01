package com.ru.tgra.shapes.g3djmodel;

import java.nio.FloatBuffer;

public class Mesh {
	//public Vector<String> attributes;
	public FloatBuffer vertices;
	public FloatBuffer normals;

	public Mesh()
	{
		vertices = null;
		normals = null;
	}
}
