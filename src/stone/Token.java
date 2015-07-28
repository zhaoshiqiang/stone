package stone;
//将Token定义成抽象类（只是里面没有抽象方法），使我们不能直接new一个Token对象。
//分割后的单词用token对象表示
public abstract class Token {
	
	//new Token(-1){}以匿名内部类的方式创建一个对象，该对象继承了Token这个抽象类
	public static final Token EOF=new Token(-1){};	//end of file
	public static final String EOL="\\n";	//end of line
	private int lineNumber;	//表示从代码第几行读到的单词
	
	protected Token(int line){this.lineNumber=line;}
	
	public int getLineNumber(){return lineNumber;}
	
	public boolean isIdentifier(){return false;}
	
	public boolean isNumber(){return false;}
	
	public boolean isString(){return false;}
	
	public int getNumber(){throw new StoneException("not number token");}
	
	public String getText(){return "";}
}
