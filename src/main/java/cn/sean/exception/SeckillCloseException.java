package cn.sean.exception;

/**
 * ��ɱ�ر��쳣������ɱ����ʱ�û���Ҫ������ɱ�ͻ���ָ��쳣
 * @author WangChao
 *
 */
public class SeckillCloseException extends SeckillException{

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillCloseException(String message) {
		super(message);
	}

}
