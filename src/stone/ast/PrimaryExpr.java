package stone.ast;

import java.util.List;

public class PrimaryExpr extends ASTList {

	public PrimaryExpr(List<ASTree> list) { super(list); }
	public static ASTree create(List<ASTree> c){
		//如果传入的参数只有一个子节点，则不会创建一个额外的节点，本应是子节点的子树将直接作为与该模式对应的抽象语法树使用
		return c.size() == 1 ? c.get(0) : new PrimaryExpr(c);
	}
	
}
