package stone.ast;

import java.util.List;

public class PrimaryExpr extends ASTList {

	public PrimaryExpr(List<ASTree> list) { super(list); }
	public static ASTree create(List<ASTree> c){
		//�������Ĳ���ֻ��һ���ӽڵ㣬�򲻻ᴴ��һ������Ľڵ㣬��Ӧ���ӽڵ��������ֱ����Ϊ���ģʽ��Ӧ�ĳ����﷨��ʹ��
		return c.size() == 1 ? c.get(0) : new PrimaryExpr(c);
	}
	
}
