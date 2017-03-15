package Base;
/**
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

import GLSLShaderScanner.GLSLVariableFinder;
import GLSLShaderScanner.ShaderVariable;

/**
 * @author user
 *
 */
public abstract class Shader
{
	String filename = null;
	// Shader variables
	protected int vsId = -1;
	protected int tsEvalId = -1;
	protected int tsCtrlId = -1;
	protected int gsId = -1;
	protected int fsId = -1;
	
	protected int pId = 0;
	
	protected int uniform_time = -1;
	
	protected int projectionMatrixLocation = 0;
	protected int viewMatrixLocation = 0;
	protected int modelMatrixLocation = 0; // TODO : pas suer que ca soit ici 

	
	// LIST DES UNIFORM ?
	// LIST DES VALEUR DES UNIFORM ?
	List<ShaderVariable> variables = new ArrayList<ShaderVariable>();
	
	boolean reloadShader = false;
	private long oldtime;
	private float curtime;
	
	
	private Shader()
	{
		
	}
	
	public Shader(String shaderFileName)
	{
		this.filename=shaderFileName;
		initShader();
	}
	
	/**
	 * 
	 */
	private void initShader()
	{
		int errorCheckValue = GL11.glGetError();

		String vsFile = "shaders/"+filename+".vertex";
		String fsFile = "shaders/"+filename+".fragment";
		String gsFile = "shaders/"+filename+".geometry";
		String tsFile = "shaders/"+filename+".tesscontrol";
		String teFile = "shaders/"+filename+".tesseval";
		
		// Load the vertex shader
		vsId = this.loadShader(vsFile, GL20.GL_VERTEX_SHADER);
		// Load the fragment shader
		fsId = this.loadShader(fsFile, GL20.GL_FRAGMENT_SHADER);
		// Load the tesselation shader
		if (new File(tsFile).exists())
			tsCtrlId = this.loadShader(tsFile, GL40.GL_TESS_CONTROL_SHADER);
		// Load the geometry shader
		if (new File(teFile).exists())
			tsEvalId = this.loadShader(teFile, GL40.GL_TESS_EVALUATION_SHADER);
		// Load the geometry shader
		if (new File(gsFile).exists())
			gsId = this.loadShader(gsFile, GL32.GL_GEOMETRY_SHADER);

		if (vsId==-1) System.err.println("VS "+filename+" doesn't load correctly");
		if (fsId==-1) System.err.println("FS "+filename+" doesn't load correctly");
		if (tsCtrlId==-1) System.err.println("TC "+filename+" doesn't load correctly");
		if (tsEvalId==-1) System.err.println("TE "+filename+" doesn't load correctly");
		if (gsId==-1) System.err.println("GS "+filename+" doesn't load correctly");
					
		// Create a new shader program that links both shaders
		pId = GL20.glCreateProgram();
		
	
		GL20.glAttachShader(pId, vsId);
		if (tsCtrlId!=-1)
		GL20.glAttachShader(pId, tsCtrlId);
		if (tsEvalId!=-1)
		GL20.glAttachShader(pId, tsEvalId);
		if (gsId!=-1)
		GL20.glAttachShader(pId, gsId);
		GL20.glAttachShader(pId, fsId);
		
		GL20.glLinkProgram(pId);
		
		// Position information will be attribute 0
		GL20.glBindAttribLocation(pId, 0, "Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(pId, 1, "Color");
		// Normal information will be attribute 2
		GL20.glBindAttribLocation(pId, 2, "Normal");
		// Texture information will be attribute 3
		GL20.glBindAttribLocation(pId, 3, "Uvs");
				
		// Get matrices uniform locations
		projectionMatrixLocation = GL20.glGetUniformLocation(pId, "projectionMatrix");
		viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
		modelMatrixLocation = GL20.glGetUniformLocation(pId, "modelMatrix");
		uniform_time = GL20.glGetUniformLocation(pId, "timer");
			
		init_user_uniform();
		
		GL20.glValidateProgram(pId);
		
		errorCheckValue = GL11.glGetError();
		if (errorCheckValue != GL11.GL_NO_ERROR) {
			System.out.println("ERROR - Could not create the shaders:" + errorCheckValue+ " value - "+errorCheckValue);
			System.exit(-1);
		}
	}
	
	protected abstract void init_user_uniform();
	
	public int loadShader(String filename, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e)
		{
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}

		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		String s = GL20.glGetShaderInfoLog(shaderID, 1000);

		if (!s.isEmpty())
		{
			System.err.println("Error compiling " + filename);
			System.err.println(s);
			return -1;
		}

		
		// TODO : This is just for info, nothing is really implemented.
		List<ShaderVariable> listVar = GLSLVariableFinder.getAllInputVariable(shaderSource.toString());
		for (Iterator<ShaderVariable> iterator = listVar.iterator(); iterator.hasNext();)
		{
			ShaderVariable shaderVariable = iterator.next();
			System.err.println(""+shaderVariable);
		}
		
		return shaderID;
	}
	
	
	/**
	 * 
	 */
	public void enable()
	{
		if (reloadShader==true)
			reloadShader_();
		GL20.glUseProgram(pId);
		GL20.glUniform1f(uniform_time, curtime);
		
		long time = System.currentTimeMillis();
		long elapsed =  time - oldtime;
		curtime+=(elapsed/1000f);
	}
	
	
	/**
	 * 
	 */
	public void disable()
	{
		GL20.glUseProgram(0);
	}
	

	/**
	 * @param matrix44Buffer
	 */
	public void updateProjectionMatrix(FloatBuffer matrix44Buffer)
	{
		GL20.glUniformMatrix4fv(projectionMatrixLocation, false, matrix44Buffer);
	}
	
	/**
	 * @param matrix44Buffer
	 */
	public void updateViewMatrix(FloatBuffer matrix44Buffer)
	{
		GL20.glUniformMatrix4fv(viewMatrixLocation, false, matrix44Buffer);
	}
	
	/**
	 * @param matrix44Buffer
	 */
	public void updateModelMatrix(FloatBuffer matrix44Buffer)
	{
		GL20.glUniformMatrix4fv(modelMatrixLocation, false, matrix44Buffer);
	}

	/**
	 * 
	 */
	public void destroy()
	{
		
		/*try
		{
			if (Display.isCurrent()==true)
			Display.makeCurrent();
		} catch (LWJGLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		GL20.glUseProgram(0);
		GL20.glDetachShader(pId, vsId);
		GL20.glDetachShader(pId, tsEvalId);
		GL20.glDetachShader(pId, tsCtrlId);
		GL20.glDetachShader(pId, gsId);
		GL20.glDetachShader(pId, fsId);
		
		GL20.glDeleteShader(vsId);
		GL20.glDeleteShader(fsId);
		GL20.glDeleteShader(gsId);
		GL20.glDeleteShader(tsEvalId);
		GL20.glDeleteShader(tsCtrlId);
		GL20.glDeleteProgram(pId);
	}
	
	/**
	 * 
	 */
	public void askToreloadShader()
	{
		
	}
	
	
	private void reloadShader_()
	{
		if (reloadShader == true)
		{
			destroy();
			initShader();
			reloadShader = false;
		}
	}
}



