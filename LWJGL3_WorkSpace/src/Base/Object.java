package Base;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;


/**
 * @author user
 * 
 */
public class Object {

	// Quad variables
	private int vaoId = 0;
	private int vboId = 0;
	private int vbocId = 0;
	private int vboiId = 0;
	private int indicesCount = 0;

	Shader shader = null;
//	private ShaderGeoDebug	shader2;
	
	private Matrix4f modelMatrix = null;

	private Vector3f Pos = null;
	private Vector3f Angle = null;
	private Vector3f Scale = null;

	TextureLoader textureloader;
	
	public Object(TextureLoader textureloader, Shader shader)
	{
		this.textureloader=textureloader;
		this.shader = shader;
		// Setup model matrix
		modelMatrix = new Matrix4f();
		
		// Set the default quad rotation, scale and position values
		Pos = new Vector3f(0, 0, 0);
		Angle = new Vector3f(0, 0, 0);
		Scale = new Vector3f(1, 1, 1);

		updateModelMatrixes();

	}
	
	

	public Object(TextureLoader textureloader, ShaderBase shader, FloatBuffer vertices, FloatBuffer normals, FloatBuffer colors, FloatBuffer uvs, IntBuffer indices) {
		this(textureloader, shader);
		initBuffers(vertices, normals, colors, uvs, indices);
	}

	
	public Object(TextureLoader textureloader, ShaderBase shader, float[] vertices, float[] normals, float[] colors, float[] uvs, int[] indices) {
	
		this(textureloader, shader);
		// Create a FloatBuffer with the proper size to store our matrices later
		// matrix44Buffer = BufferUtils.createFloatBuffer(16);
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();

		FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(normals.length);
		normalsBuffer.put(normals);
		normalsBuffer.flip();

		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
		colorsBuffer.put(colors);
		colorsBuffer.flip();

		FloatBuffer uvsBuffer = BufferUtils.createFloatBuffer(uvs.length);
		uvsBuffer.put(uvs);
		uvsBuffer.flip();

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		initBuffers(verticesBuffer, normalsBuffer, colorsBuffer, uvsBuffer, indicesBuffer);
		
	}
	
	/**
	 * @param vertices
	 * @param normals
	 * @param colors
	 * @param uvs
	 * @param indices
	 */
	private void initBuffers(FloatBuffer vertices, FloatBuffer normals, FloatBuffer colors, FloatBuffer uvs, IntBuffer indices)
	{
		//destroy();
		indicesCount = indices.limit();;
		
		// Vertices, the order is not important. XYZW instead of XYZ

		// Create a new Vertex Array Object in memory and select it (bind)
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind) -
		// VERTICES
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Create a new VBO for the indices and select it (bind) - COLORS
		vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colors, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Create a new VBO for the indices and select it (bind) - NORMAL
		vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normals, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Create a new VBO for the indices and select it (bind) - TEXTURE UV
		vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, uvs, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(3, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	
	/**
	 * 
	 */
	public void render() {
		shader.enable();

		// Bind the texture
		// GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIds[textureSelector]);

/*		 GL13.glActiveTexture(GL13.GL_TEXTURE0);
			
		Texture texture;
		try
		{
			//texture = textureloader.getTexture("CasseTete2.png");
			texture = textureloader.getTexture("Wandelb.jpg");
			
			texture.bind();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
		
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);

		// Bind to the index VBO that has all the information about the order of
		// the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		
		if (shader.tsCtrlId!=-1)
		{
			GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, 3);
			//GL40.glPatchParameteri(GL40.GL_PATCH_DEFAULT_OUTER_LEVEL, 3);
			//GL40.glPatchParameteri(GL40.GL_PATCH_DEFAULT_INNER_LEVEL, 2);
		
		}
		
		// Draw the vertices
		if (shader.tsCtrlId!=-1)
			GL11.glDrawElements(GL40.GL_PATCHES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		else
			GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);

		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);

		GL30.glBindVertexArray(0);
		
		shader.disable();
	}
	
	public void reloadShader()
	{
		shader.askToreloadShader();
		updateModelMatrixes();
	}
	
	protected void updateModelMatrixes() {
		modelMatrix.identity();
		// Scale, translate and rotate model
		modelMatrix=modelMatrix.scale(Scale);
		modelMatrix=modelMatrix.translate(Pos);
		modelMatrix=modelMatrix.rotate(this.degreesToRadians(Angle.z), new Vector3f(0, 0, 1));
		modelMatrix=modelMatrix.rotate(this.degreesToRadians(Angle.y), new Vector3f(0, 1, 0));
		modelMatrix=modelMatrix.rotate(this.degreesToRadians(Angle.x), new Vector3f(1, 0, 0));
		
		//Matrix4f.scale(Scale, modelMatrix, modelMatrix);
		//Matrix4f.translate(Pos, modelMatrix, modelMatrix);
		//Matrix4f.rotate(this.degreesToRadians(Angle.z), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		//Matrix4f.rotate(this.degreesToRadians(Angle.y), new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		//Matrix4f.rotate(this.degreesToRadians(Angle.x), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);

		// Upload matrices to the uniform variables
		if (shader!=null)
		{
		shader.enable();

		FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);
		//modelMatrix.put
		//modelMatrix.store(matrix44Buffer);
		//matrix44Buffer.flip();
		shader.updateModelMatrix(matrix44Buffer);

		shader.disable();

		}
	}


	/**
	 * 
	 */
	public void destroy() {
		shader.destroy();

		// Select the VAO
		GL30.glBindVertexArray(vaoId);

		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);

		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId);

		// Delete the color VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbocId);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboiId);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);

	}

	public synchronized Vector3f getPos() {
		return Pos;
	}

	public synchronized void setPos(Vector3f pos) {
		Pos = pos;
		updateModelMatrixes();
	}

	/**
	 * @param x
	 * @param f
	 * @param z
	 */
	public void setPosition(float x, float y, float z) {
		Pos.x = x;
		Pos.y = y;
		Pos.z = z;
		updateModelMatrixes();
	}

	public synchronized Vector3f getAngle() {
		return Angle;
	}

	public synchronized void setAngle(Vector3f angle) {
		Angle = angle;
		updateModelMatrixes();
	}

	/**
	 * @param x
	 * @param y
	 * @param f
	 */
	public void setRotation(float x, float y, float z) {
		Angle.x = x;
		Angle.y = y;
		Angle.z = z;
		updateModelMatrixes();
	}

	public synchronized Vector3f getScale() {
		return Scale;
	}

	public synchronized void setScale(Vector3f scale) {
		Scale = scale;
		updateModelMatrixes();
	}

	public void avance()
	{
		
	}
	
	/**
	 * @param f
	 * @param g
	 * @param h
	 */
	public void setScale(float f, float g, float h) {
		Scale.x = f;
		Scale.y = g;
		Scale.z = h;
		updateModelMatrixes();
	}

	
	private float degreesToRadians(float degrees) {
		return degrees * (float) (Math.PI / 180d);
	}



	/**
	 * @param shaderDebug
	 */
	public void setShader(Shader shaderDebug)
	{
		this.shader=shaderDebug;
		updateModelMatrixes();
		
	}

	
	

}
