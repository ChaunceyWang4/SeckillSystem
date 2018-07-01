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
	 * ���빺����ϸ���ɹ����ظ�
	 * @param seckillId
	 * @param userPhone
	 * @return ���������
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * ͨ����ɱ��Ʒ��ID��ѯ��ϸSuccessKilled���󣨸ö���Я����seckill��ɱ��Ʒ����
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
