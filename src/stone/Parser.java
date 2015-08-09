package stone;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import stone.ast.ASTList;
import stone.ast.ASTree;

public class Parser {
	protected static abstract class Element{
		protected abstract void parse(Lexer lexer,List<ASTree> res)
		throws ParseException;
		protected abstract boolean match(Lexer lexer) throws ParseException;
	}
	
	protected static class Tree extends Element{
		protected Parser parser;
		public Tree(Parser p) {parser = p;}
		@Override
		protected void parse(Lexer lexer, List<ASTree> res)
				throws ParseException {
			res.add(parser.parse(lexer));
		}

		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			return parser.match(lexer);
		}
	}
	
	protected static class OrTree extends Element{
		protected Parser[] parsers;
		protected OrTree(Parser[] p) { parsers=p; }
		@Override
		protected void parse(Lexer lexer, List<ASTree> res)
				throws ParseException {
			Parser p=choose(lexer);
			if(p == null)
				throw new ParseException(lexer.peek(0));
			else
				res.add(p.parse(lexer));
		}
		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			return choose(lexer) != null;
		}
		
		protected Parser choose(Lexer lexer) throws ParseException{
			for(Parser p : parsers)
				if(p.match(lexer))
					return p;
			return null;
		}
		protected void insert(Parser p){
			Parser[] newParsers = new Parser[parsers.length+1];
			newParsers[0]=p;
			System.arraycopy(parsers, 0, newParsers, 1, parsers.length);
			parsers=newParsers;
		}
	}
	
	public static final String factoryName = "create";
	protected static abstract class Factory {
		protected abstract ASTree make0(Object arg) throws Exception;
		protected ASTree make(Object arg){
			try {
				return make0(arg);
			} catch (IllegalArgumentException e1) {
				throw e1;
			} catch (Exception e2){
				throw new RuntimeException(e2);	//±‡“Î∆˜“—±¿
			}
		}
		protected static Factory getForASTList(Class<? extends ASTree> clazz){
			Factory f=get(clazz,List.class);
			if(f == null)
				f = new Factory() {
					@Override
					protected ASTree make0(Object arg) throws Exception {
						List<ASTree> results = (List<ASTree>)arg;
						if(results.size() == 1)
							return results.get(0);
						else
							return new ASTList(results);
					}
				};
			return f;	
		}
		private static Factory get(Class<? extends ASTree> clazz,
				Class<?> argType) {
			if(clazz == null)
				return null;
			
			try {
				final Method m = clazz.getMethod(factoryName, new Class<?>[]{argType});
				
				return new Factory() {
					@Override
					protected ASTree make0(Object arg) throws Exception {
						return (ASTree)m.invoke(null, arg);
					}
				};
			} catch (NoSuchMethodException e) {
				
			}
			
			try {
				final Constructor<? extends ASTree> c=clazz.getConstructor(argType);
				return new Factory() {
					protected ASTree make0(Object arg) throws Exception {
						return c.newInstance(arg);
					}
				};
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
	}
		
	protected List<Element> elements;
	protected Factory factory;
	
	public Parser(Class<? extends ASTree> clazz) {
		reset(clazz);
	}
	
	protected Parser(Parser p){
		elements=p.elements;
		factory=p.factory;
	}
	
	//÷¥––”Ô∑®∑÷Œˆ
	public ASTree parse(Lexer lexer) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean match(Lexer lexer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void reset(Class<? extends ASTree> clazz) {
		// TODO Auto-generated method stub
		
	}
}
