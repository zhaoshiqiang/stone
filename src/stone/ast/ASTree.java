package stone.ast;

import java.util.Iterator;

public abstract class ASTree implements Iterable<ASTree> {
	
	public abstract ASTree child(int i);	//返回第i个子节点
	public abstract int numChildren();	//返回子节点的个数
	public abstract Iterator<ASTree> children();//返回一个Iterator对象，用于依次遍历所有子节点
	public abstract String location();//返回一个用于表示抽象语法树节点在程序内所处位置的字符串
	@Override
	//与children方法功能相同，它是一个适配器，在将ASTree类转为Iterable类型时会用到该方法
	public Iterator<ASTree> iterator() {
		// TODO Auto-generated method stub
		return children();
	}

}
