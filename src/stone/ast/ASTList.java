package stone.ast;

import java.util.Iterator;
import java.util.List;

public class ASTList extends ASTree {
	protected List<ASTree> children;
	//list中的元素是ASTree，因为不知道元素类型时ASTLeaf还是ASTList，这里用了合成模式
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
			//若t是非叶节点，则递归调用，直到t为叶节点，输出其位置
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
