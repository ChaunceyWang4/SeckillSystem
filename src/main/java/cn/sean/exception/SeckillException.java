package cn.sean.exception;

/**
 * 秒杀相关的所有业务异常
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
