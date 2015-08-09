package stone.ast;

import java.util.ArrayList;
import java.util.Iterator;

import stone.Token;

public class ASTLeaf extends ASTree {

	private static ArrayList<ASTree> empty=new ArrayList<ASTree>();
	protected Token token;
	
	public ASTLeaf(Token t) {this.token=t;}

	@Override
	public ASTree child(int i) { throw new IndexOutOfBoundsException();}

	@Override
	//Ҷ�ڵ�û���ӽڵ㣬���Դ˷�������0
	public int numChildren() {return 0;}

	@Override
	//������ռ��Ϲ�����iterator����
	public Iterator<ASTree> children() {return empty.iterator();}

	@Override
	public String location() {return "at line" + token.getLineNumber();}
	
	public Token token(){return token;}

}
