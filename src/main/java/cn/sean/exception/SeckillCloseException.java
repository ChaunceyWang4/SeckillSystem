package cn.sean.exception;

/**
 * 秒杀关闭异常，当秒杀结束时用户还要进行秒杀就会出现该异常
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
