package stone.ast;

import java.util.Iterator;

public abstract class ASTree implements Iterable<ASTree> {
	
	public abstract ASTree child(int i);	//���ص�i���ӽڵ�
	public abstract int numChildren();	//�����ӽڵ�ĸ���
	public abstract Iterator<ASTree> children();//����һ��Iterator�����������α��������ӽڵ�
	public abstract String location();//����һ�����ڱ�ʾ�����﷨���ڵ��ڳ���������λ�õ��ַ���
	@Override
	//��children����������ͬ������һ�����������ڽ�ASTree��תΪIterable����ʱ���õ��÷���
	public Iterator<ASTree> iterator() {
		// TODO Auto-generated method stub
		return children();
	}

}
