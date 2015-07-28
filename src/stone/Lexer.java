package stone;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	public static String regexPat
	= "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
        + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
	private Pattern pattern = Pattern.compile(regexPat);	//����������ʽ�ַ�������һ��������ʽ����
	private ArrayList<Token> queue=new ArrayList<Token>();	//���ʷ���������ȡ���벢��õĵ��ʴ����queue��
	private boolean hasMore;	//�Ƿ�����ٶ�ȡ
	private LineNumberReader reader;
	
	public Lexer(Reader r)
	{
		hasMore=true;
		reader=new LineNumberReader(r);
	}
	
	public Token read() throws ParseException
	{
		if(fillQueue(0))
		{
			return queue.remove(0);
		}else{
			return Token.EOF;
		}
	}
	
	public Token peek(int i) throws ParseException
	{
		//�����ʶ����������i�����ʣ�����ȡ�������
		if(fillQueue(i))
		{
			return queue.get(i);
		}else{
			//�ѵ��ļ�ĩβ
			return Token.EOF;
		}
	}
	
	private boolean fillQueue(int i) throws ParseException{
		// TODO Auto-generated method stub
		//��䵥�ʶ����еĵ�������i��
		while(i >= queue.size())
		{
			//�����i������û��������ĩβ�������ٶ�ȡ����
			if(hasMore)
			{
				//��ȡһ��
				readLine();
			}else{
				
				return false;
			}
		}
		return true;
	}
	
	private void readLine() throws ParseException {
		
		String line;	//��ʾһ�еĴ���ȡ����
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO: handle exception
			throw new ParseException(e);
		}
		//�Ѿ�������ĩβ�������ٶ�ȡ������
		if(line == null)
		{
			hasMore=false;
			return;
		}
		
		int lineNo=reader.getLineNumber();	//Ŀǰ��ȡ���Ǵ���ĵڼ���
		//��ȡһ������ʵ�ʼ��ƥ���matcher����
		Matcher matcher = pattern.matcher(line);
		matcher.useTransparentBounds(true).useAnchoringBounds(false);
		
		int pos=0;
		int endPos=line.length();
		while(pos < endPos)
		{
			//ͨ��region�����޶��ö�����ƥ��ķ�Χ
			matcher.region(pos, endPos);
			//ͨ��lookingAt�����ڼ�鷶Χ�ڽ���������ʽƥ��
			if(matcher.lookingAt())
			{
				addToken(lineNo,matcher);
				//end��������ȡ��ƥ�䲿�ֵĽ���λ�ã��Ա��ڴʷ������������￪ʼ����ִ����һ��lookingAt�������ã�ֱ�����������в��ٺ��е���
				pos=matcher.end();
			}else{
				throw new ParseException("bad token at line"+lineNo);
			}
		}
		queue.add(new IdToken(lineNo,Token.EOL));
	}

	private void addToken(int lineNo, Matcher matcher) {
		// TODO Auto-generated method stub
		//��ȡ��������Ŷ�Ӧ�����ַ���
		String m=matcher.group(1);
		if(m != null)	//���ǿո�
		{
			if(matcher.group(2) == null)	//����ע��
			{
				Token token;
				if(matcher.group(3) != null)
					token=new NumToken(lineNo, Integer.parseInt(m));	//������������
				else if(matcher.group(4) != null)
					token=new StrToken(lineNo, toStringLiteral(m));	//���ַ���������
				else
					token=new IdToken(lineNo, m);	//��ʶ��
				
				queue.add(token);
			}
		}
	}
	
	//��ת���ַ�ת��֮����ַ���
	protected String toStringLiteral(String s){
		StringBuilder sb = new StringBuilder();
		int len=s.length()-1;
		for(int i=1; i<len;i++)
		{
			char c=s.charAt(i);
			if(c == '\\' && i+1<len){
				int c2=s.charAt(i+1);
				if(c2 == '"' || c2 == '\\')
					c=s.charAt(++i);	//���ַ�Ϊ �� ���� \
				else if(c2 == 'n')
				{
					++i;
					c='\n';	//���ַ�Ϊ���з�
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	//����������
	protected static class NumToken extends Token{
		
		private int value;
		//lineΪ����ڼ��ж����õ��ʣ�v��ʾ����������������Ӧ��ֵ
		protected NumToken(int line, int v) {
			super(line);
			value=v;
		}
		public boolean isNumber()
		{
			return true;
		}
		
		public String getText()
		{
			return Integer.toString(value);
		}
		public int getNumber()
		{
			return value;
		}
	}
	
	//��ʶ��������
	protected static class IdToken extends Token{
		
		private String text;
		//line��ʾ����ڼ��ж����õ��ʣ�id��ʾ�ñ�ʶ������Ӧ���ַ���
		protected IdToken(int line,String id){
			super(line);
			text=id;
		}
		
		public boolean isIdentifier() { return true; }
        public String getText() { return text; }
	}
	
	//�ַ���������
	protected static class StrToken extends Token {
        private String literal;
        //line��ʾ����ڼ��ж����õ��ʣ�str��ʾ�ñ�ʶ������Ӧ�Ľ�ת���ַ�ת��֮����ַ���
        StrToken(int line, String str) {
            super(line);
            literal = str;
        }
        public boolean isString() { return true; }
        public String getText() { return literal; }
    }
	
	
}
