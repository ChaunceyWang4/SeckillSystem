package cn.sean.exception;

/**
 * ��ɱ��ص�����ҵ���쳣
 * @author WangChao
 *
 */

public class SeckillException extends RuntimeException {

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillException(String message) {
		super(message);
	}
	
}
