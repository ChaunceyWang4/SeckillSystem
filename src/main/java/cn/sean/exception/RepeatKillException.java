package cn.sean.exception;

/**
 * 重复秒杀异常，是一个运行前异常，故不需要我们手动try catch
 * spring事务管理只支持运行前异常的回滚操作
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
