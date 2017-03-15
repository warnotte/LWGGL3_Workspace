package GLSLShaderScanner;
/**
 * @author Warnotte Renaud
 *
 */
public class ShaderVariable
{
	String type;
	String name;
	
	// TODO : min-max ?
	
	public ShaderVariable(String type, String name)
	{
		super();
		this.type = type;
		this.name = name;
	}
	
	public synchronized String getType()
	{
		return type;
	}
	public synchronized void setType(String type)
	{
		this.type = type;
	
	}
	public synchronized String getName()
	{
		return name;
	}
	public synchronized void setName(String name)
	{
		this.name = name;
	
	}

	@Override
	public String toString()
	{
		return "ShaderVariable [type=" + type + ", name=" + name + "]";
	}

}
