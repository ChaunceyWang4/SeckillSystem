package cn.sean.service;

import java.util.List;

import cn.sean.domain.Seckill;
import cn.sean.dto.Exposer;
import cn.sean.dto.SeckillExecution;
import cn.sean.exception.RepeatKillException;
import cn.sean.exception.SeckillCloseException;
import cn.sean.exception.SeckillException;

/**
 * 业务接口：站在使用者的角度设计接口
 * 三个方面：1、方法定义粒度，方法定义要非常清楚；2、参数，要越简练越好；3、返回类型，return类型一定要友好或return异常
 * @author WangChao
 *
 */
public interface SeckillService {
	/**
	 * 查询全部秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	//下面是我们最重要行为的一些接口
	/**
	 * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
	 * @param seckillId
	 * @return
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作，可能成功或者失败，所以要抛出我们允许的异常
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
			throws SeckillException,RepeatKillException,SeckillCloseException;
}
