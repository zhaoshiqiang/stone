package stone;

import java.io.IOException;

public class ParseException extends Exception {

	public ParseException(Token t)
	{
		this("",t);
	}

	public ParseException(String msg, Token t) {
		// TODO Auto-generated constructor stub
		super("syntax error around" + location(t)+"."+msg); 
	}

	private static String location(Token t) {
		// TODO Auto-generated method stub
		if(t == Token.EOF)
		{
			return "the last line";
		}else{
			return "\""+t.getText()+"\" at line "+t.getLineNumber();
		}
	}
	
	public ParseException(IOException e)
	{
		super(e);
	}
	public ParseException(String msg)
	{
		super(msg);
	}
	
}
