package GLSLShaderScanner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author warnotte renaud
 *
 */
public class GLSLVariableFinder
{
	static String texte = "hello;" +
			"eok uniform int variable1 = 54;" +
			"eok uniform int variable_3; " +
			"uniform int TessLevelInner=4;"+
			"eok uniform int variable_4 ;" +
			"eok uniform int k;" +
			"uniform float blabla2;" +
			"uniform float blabla_4;" +
			"roprkrok;" +
			"prkoprko;;;";
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		List<ShaderVariable> listVar = getAllInputVariable(texte);
		for (Iterator<ShaderVariable> iterator = listVar.iterator(); iterator.hasNext();)
		{
			ShaderVariable shaderVariable = (ShaderVariable) iterator.next();
			System.err.println(""+shaderVariable);
		}
	}
	
	public static List<ShaderVariable> getAllInputVariable(String text)
	{
		List<ShaderVariable> list = new ArrayList<>();
		Pattern pattern = Pattern.compile("uniform \\w+ \\w+"); // not trailing ;
		Matcher matcher = pattern.matcher(texte);
		while (matcher.find())
		{
		//	System.err.println("--"+matcher.end());
			String exp = matcher.group();
			String variable_type = exp.split(" ")[1];
			String variable_name = exp.split(" ")[2];
			ShaderVariable item = new ShaderVariable(variable_type, variable_name);
			list.add(item);
		}
		return list;
	}

}
