package cn.sean.dao;

import org.apache.ibatis.annotations.Param;

import cn.sean.domain.SuccessKilled;

/**
 * 
 * @author WangChao
 *
 */
public interface SuccessKilledDao {
	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId
	 * @param userPhone
	 * @return 插入的行数
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * 通过秒杀商品的ID查询明细SuccessKilled对象（该对象携带了seckill秒杀商品对象）
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
