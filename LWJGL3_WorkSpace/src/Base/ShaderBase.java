package Base;

import org.lwjgl.opengl.GL20;


/**
 * @author user
 *
 */
public class ShaderBase extends Shader
{
	int uniform_Texture_0;
	int uniform_fragShaderMode;
	int uniform_TessLevelInner;
	int uniform_TessLevelOuter;

	int FragshaderMode = 0;
	private int TessLevelOuter=4;
	private int TessLevelInner=4;
	
	
	public ShaderBase(String shaderFilenameprefix)
	{
		super(shaderFilenameprefix);
		
	}
	
	

	/**
	 * 
	 */
	@Override
	protected void init_user_uniform()
	{
		uniform_Texture_0 = GL20.glGetUniformLocation(pId, "texture_0");
		uniform_fragShaderMode = GL20.glGetUniformLocation(pId, "fragShaderMode");
		uniform_TessLevelInner = GL20.glGetUniformLocation(pId, "TessLevelInner");
		uniform_TessLevelOuter = GL20.glGetUniformLocation(pId, "TessLevelOuter");
	}
	
	/**
	 * 
	 */
	@Override
	public void enable()
	{
		super.enable();
		GL20.glUniform1i(uniform_Texture_0, 0);
		GL20.glUniform1i(uniform_fragShaderMode, FragshaderMode);
		GL20.glUniform1i(uniform_TessLevelOuter, TessLevelOuter);
		GL20.glUniform1i(uniform_TessLevelInner, TessLevelInner);
	}

	public synchronized int getFragshaderMode()
	{
		return FragshaderMode;
	}

	public synchronized void setFragshaderMode(int fragshaderMode)
	{
		FragshaderMode = fragshaderMode;
	}

	public synchronized int getTessLevelOuter() {
		return TessLevelOuter;
	}

	public synchronized void setTessLevelOuter(int tessLevelOuter) {
		TessLevelOuter = tessLevelOuter;
		askToreloadShader();
	}

	public synchronized int getTessLevelInner() {
		return TessLevelInner;
	}

	public synchronized void setTessLevelInner(int tessLevelInner) {
		TessLevelInner = tessLevelInner;
		askToreloadShader();
	}
}
