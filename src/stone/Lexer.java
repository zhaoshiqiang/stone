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
	private Pattern pattern = Pattern.compile(regexPat);	//依据正则表达式字符串创建一个正则表达式对象
	private ArrayList<Token> queue=new ArrayList<Token>();	//将词法分析器读取代码并获得的单词存放在queue中
	private boolean hasMore;	//是否可以再读取
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
		//将单词队列填充至第i个单词，并读取这个单词
		if(fillQueue(i))
		{
			return queue.get(i);
		}else{
			//已到文件末尾
			return Token.EOF;
		}
	}
	
	private boolean fillQueue(int i) throws ParseException{
		// TODO Auto-generated method stub
		//填充单词队列中的单词至第i个
		while(i >= queue.size())
		{
			//不足第i个，且没有至代码末尾，还能再读取单词
			if(hasMore)
			{
				//读取一行
				readLine();
			}else{
				
				return false;
			}
		}
		return true;
	}
	
	private void readLine() throws ParseException {
		
		String line;	//表示一行的待读取代码
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO: handle exception
			throw new ParseException(e);
		}
		//已经至代码末尾，不能再读取单词了
		if(line == null)
		{
			hasMore=false;
			return;
		}
		
		int lineNo=reader.getLineNumber();	//目前读取的是代码的第几行
		//获取一个用于实际检查匹配的matcher对象
		Matcher matcher = pattern.matcher(line);
		matcher.useTransparentBounds(true).useAnchoringBounds(false);
		
		int pos=0;
		int endPos=line.length();
		while(pos < endPos)
		{
			//通过region方法限定该对象检查匹配的范围
			matcher.region(pos, endPos);
			//通过lookingAt方法在检查范围内进行正则表达式匹配
			if(matcher.lookingAt())
			{
				addToken(lineNo,matcher);
				//end方法用于取得匹配部分的结束位置，以便于词法分析器从那里开始继续执行下一次lookingAt方法调用，直至待代码行中不再含有单词
				pos=matcher.end();
			}else{
				throw new ParseException("bad token at line"+lineNo);
			}
		}
		queue.add(new IdToken(lineNo,Token.EOL));
	}

	private void addToken(int lineNo, Matcher matcher) {
		// TODO Auto-generated method stub
		//获取与各个括号对应的子字符串
		String m=matcher.group(1);
		if(m != null)	//不是空格
		{
			if(matcher.group(2) == null)	//不是注解
			{
				Token token;
				if(matcher.group(3) != null)
					token=new NumToken(lineNo, Integer.parseInt(m));	//是整型字面量
				else if(matcher.group(4) != null)
					token=new StrToken(lineNo, toStringLiteral(m));	//是字符串字面量
				else
					token=new IdToken(lineNo, m);	//标识符
				
				queue.add(token);
			}
		}
	}
	
	//将转义字符转义之后的字符串
	protected String toStringLiteral(String s){
		StringBuilder sb = new StringBuilder();
		int len=s.length()-1;
		for(int i=1; i<len;i++)
		{
			char c=s.charAt(i);
			if(c == '\\' && i+1<len){
				int c2=s.charAt(i+1);
				if(c2 == '"' || c2 == '\\')
					c=s.charAt(++i);	//该字符为 ” 或者 \
				else if(c2 == 'n')
				{
					++i;
					c='\n';	//该字符为换行符
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	//整型字面量
	protected static class NumToken extends Token{
		
		private int value;
		//line为代码第几行读到该单词，v表示该整型字面量所对应的值
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
	
	//标识符字面量
	protected static class IdToken extends Token{
		
		private String text;
		//line表示代码第几行读到该单词，id表示该标识符所对应的字符串
		protected IdToken(int line,String id){
			super(line);
			text=id;
		}
		
		public boolean isIdentifier() { return true; }
        public String getText() { return text; }
	}
	
	//字符串字面量
	protected static class StrToken extends Token {
        private String literal;
        //line表示代码第几行读到该单词，str表示该标识符所对应的将转义字符转义之后的字符串
        StrToken(int line, String str) {
            super(line);
            literal = str;
        }
        public boolean isString() { return true; }
        public String getText() { return literal; }
    }
	
	
}
