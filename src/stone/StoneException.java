package stone;

import stone.ast.ASTree;

@SuppressWarnings("serial")
public class StoneException extends RuntimeException {
	
	public StoneException(String m)
	{
		super(m);
	}
	public StoneException(String m,ASTree t)
	{
		super(m+""+t.location());
	}
	
}
