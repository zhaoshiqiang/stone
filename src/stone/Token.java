package stone;
//��Token����ɳ����ֻࣨ������û�г��󷽷�����ʹ���ǲ���ֱ��newһ��Token����
//�ָ��ĵ�����token�����ʾ
public abstract class Token {
	
	//new Token(-1){}�������ڲ���ķ�ʽ����һ�����󣬸ö���̳���Token���������
	public static final Token EOF=new Token(-1){};	//end of file
	public static final String EOL="\\n";	//end of line
	private int lineNumber;	//��ʾ�Ӵ���ڼ��ж����ĵ���
	
	protected Token(int line){this.lineNumber=line;}
	
	public int getLineNumber(){return lineNumber;}
	
	public boolean isIdentifier(){return false;}
	
	public boolean isNumber(){return false;}
	
	public boolean isString(){return false;}
	
	public int getNumber(){throw new StoneException("not number token");}
	
	public String getText(){return "";}
}
