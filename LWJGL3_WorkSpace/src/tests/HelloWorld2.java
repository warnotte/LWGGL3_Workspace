package tests;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL44.*;

import Base.HelloBase;
import Base.Object;
import Base.Shader;
import Base.ShaderBase;
import Base.TextureLoader;

public class HelloWorld2 extends HelloBase {

	public static void main(String[] args) {
		new HelloWorld2().run();
	}
	
	TextureLoader textureloader = new TextureLoader();
	int shaderProgram;
	int vao;
	
	// Uniform
	int uniColor;
	private Shader shader;
	
	@Override
	public void init() {
		
	
			 
			float[] vertices = {
					-0.5f,  0.5f, -0.5f, 1.0f,
					 0.5f,  0.5f, -0.5f, 1.0f,
					 0.5f, -0.5f, -0.5f, 1.0f,
					 -0.5f, -0.5f, -0.5f, 1.0f,
					 
					-0.5f,  0.5f, 0.5f, 1.0f,
					0.5f,  0.5f, 0.5f, 1.0f,
					 0.5f, -0.5f, 0.5f, 1.0f,
					 -0.5f, -0.5f, 0.5f, 1.0f,
					    
					 -0.5f,  0.5f, -0.5f, 1.0f,
						-0.5f, -0.5f, -0.5f, 1.0f,
						-0.5f, -0.5f, 0.5f, 1.0f,
					    -0.5f,  0.5f, 0.5f, 1.0f,
					     
					    0.5f,  0.5f, -0.5f, 1.0f,
						0.5f, -0.5f, -0.5f, 1.0f,
						0.5f, -0.5f, 0.5f, 1.0f,
					    0.5f,  0.5f, 0.5f, 1.0f,
					
						-0.5f,  0.5f, -0.5f, 1.0f,
						 0.5f,  0.5f, -0.5f, 1.0f,
						 0.5f,  0.5f, 0.5f, 1.0f,
						-0.5f,  0.5f, 0.5f, 1.0f,
	   
						-0.5f,  -0.5f, -0.5f, 1.0f,
						 0.5f,  -0.5f, -0.5f, 1.0f,
						 0.5f,  -0.5f, 0.5f, 1.0f,
						-0.5f,  -0.5f, 0.5f, 1.0f,
	   
					    
			};
			float[] normals = {
					0.0f, 0.0f, -1f, 
					0.0f, 0.0f, -1f, 
					0.0f, 0.0f, -1f, 
					0.0f, 0.0f, -1f,
					
					0.0f, 0.0f, 1f, 
					0.0f, 0.0f, 1f, 
					0.0f, 0.0f, 1f, 
					0.0f, 0.0f, 1f,
					
					1.0f, 0.0f, 0f, 
					1.0f, 0.0f, 0f, 
					1.0f, 0.0f, 0f, 
					1.0f, 0.0f, 0f,
				
					-1.0f, 0.0f, 0f, 
					-1.0f, 0.0f, 0f, 
					-1.0f, 0.0f, 0f, 
					-1.0f, 0.0f, 0f,
				
					0.0f, 1.0f, 0f, 
					0.0f, 1.0f, 0f, 
					0.0f, 1.0f, 0f, 
					0.0f, 1.0f, 0f,
				
					0.0f,-1.0f, 0.0f, 
					0.0f,-1.0f, 0.0f, 
					0.0f,-1.0f, 0.0f, 
					0.0f,-1.0f, 0.0f,
				
			};
			float[] colors = {
					1f, 0f, 0f, 1f,
					0f, 1f, 0f, 1f,
					0f, 0f, 1f, 1f,
					1f, 1f, 1f, 1f,
				
					
					0f, 0f, 1f, 1f,
					0f, 1f, 0f, 1f,
					1f, 0f, 0f, 1f,
					0f, 0f, 0f, 1f,
					
					0f, 0f, 1f, 1f,
					0f, 1f, 0f, 1f,
					1f, 0f, 0f, 1f,
					0f, 0f, 0f, 1f,
					
					0f, 0f, 1f, 1f,
					0f, 1f, 0f, 1f,
					1f, 0f, 0f, 1f,
					0f, 0f, 0f, 1f,
				
			};
			float[] uvs = {
					0f, 0f,
					0f, 1f, 
					1f, 1f, 
					1f, 0f,
					
					0f, 0f,
					0f, 1f, 
					1f, 1f, 
					1f, 0f,
					
					0f, 0f,
					0f, 1f, 
					1f, 1f, 
					1f, 0f,
					
					0f, 0f,
					0f, 1f, 
					1f, 1f, 
					1f, 0f,
					
					0f, 0f,
					0f, 1f, 
					1f, 1f, 
					1f, 0f,
					
					0f, 0f,
					0f, 1f, 
					1f, 1f, 
					1f, 0f,
					
				
			};
			// OpenGL expects to draw vertices in counter clockwise order by default
		/*	byte[] indices = {
					0, 1, 2,
					2, 3, 0
			};*/
			int[] indices = {
					0, 1, 2,
					2, 3, 0,
				
					4,5,6,
					6,7,4,
				
					8, 9,10,
					10,11 ,8,
					
					12,13,14,
					14,15,12,
					
					16,17,18,
					18,19,16,
					
					20,21,22,
					22,23,20
					
					
			};
		
			
			
			shader = new ShaderBase("simple");
			
			// Construit un object pret a être affiché.
			quad2 = new Object(textureloader, null, vertices, normals, colors, uvs, indices);
			quad2.setShader(shader);
		
		
	}
	Object quad2;
	
	
	/**
	 * 
	 */
	@Override
	protected void render() {
		// Utilise le shader programme
		shader.enable();
		quad2.render();
		
	}


	/* (non-Javadoc)
	 * @see HelloBase#destroy()
	 */
	@Override
	protected void destroy()
	{
		glDeleteProgram(shaderProgram);
		
	}


	/* (non-Javadoc)
	 * @see Base.HelloBase#updateGameState()
	 */
	@Override
	protected void updateGameState()
	{
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see Base.HelloBase#handleInput()
	 */
	@Override
	protected void handleInput()
	{
		// TODO Auto-generated method stub
		
	}

}