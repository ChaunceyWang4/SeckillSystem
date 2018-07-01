package cn.sean.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.sean.domain.Seckill;

/**
 * 
 * @author WangChao
 *
 */
public interface SeckillDao {
	/**
	 * �����
	 * @param seckillId
	 * @param killTime
	 * @return ���Ӱ����������1����ʾ���¿��ļ�¼����
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	
	/**
	 * ͨ��ID��ѯ��ɱ����Ʒ��Ϣ
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * ����ƫ������ѯ��ɱ��Ʒ�б�
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}