package stone.ast;

import java.util.Iterator;
import java.util.List;

public class ASTList extends ASTree {
	protected List<ASTree> children;
	//list�е�Ԫ����ASTree����Ϊ��֪��Ԫ������ʱASTLeaf����ASTList���������˺ϳ�ģʽ
	public ASTList(List<ASTree> list) {children=list;}
	
	@Override
	public ASTree child(int i) {return children.get(i);}
	
	@Override
	public int numChildren() {return children.size();}

	@Override
	public Iterator<ASTree> children() {return children.iterator();}

	@Override
	public String location() {
		for(ASTree t :children)
		{
			//��t�Ƿ�Ҷ�ڵ㣬��ݹ���ã�ֱ��tΪҶ�ڵ㣬�����λ��
			String s=t.location();
			if(t != null)
				return s;
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('(');
		String sep="";
		for(ASTree t :children)
		{
			builder.append(sep);
			sep=" ";
			builder.append(t.toString());
		}
		return builder.append(')').toString();
	}
}
