package cn.sean.exception;

/**
 * �ظ���ɱ�쳣����һ������ǰ�쳣���ʲ���Ҫ�����ֶ�try catch
 * spring�������ֻ֧������ǰ�쳣�Ļع�����
 * @author WangChao
 *
 */

public class RepeatKillException extends SeckillException{

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatKillException(String message) {
		super(message);
	}
	
}
