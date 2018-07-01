package cn.sean.service;

import java.util.List;

import cn.sean.domain.Seckill;
import cn.sean.dto.Exposer;
import cn.sean.dto.SeckillExecution;
import cn.sean.exception.RepeatKillException;
import cn.sean.exception.SeckillCloseException;
import cn.sean.exception.SeckillException;

/**
 * ҵ��ӿڣ�վ��ʹ���ߵĽǶ���ƽӿ�
 * �������棺1�������������ȣ���������Ҫ�ǳ������2��������ҪԽ����Խ�ã�3���������ͣ�return����һ��Ҫ�Ѻû�return�쳣
 * @author WangChao
 *
 */
public interface SeckillService {
	/**
	 * ��ѯȫ����ɱ��¼
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * ��ѯ������ɱ��¼
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	//��������������Ҫ��Ϊ��һЩ�ӿ�
	/**
	 * ����ɱ����ʱ�����ɱ�ӿڵĵ�ַ���������ϵͳʱ�����ɱʱ��
	 * @param seckillId
	 * @return
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * ִ����ɱ���������ܳɹ�����ʧ�ܣ�����Ҫ�׳�����������쳣
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
			throws SeckillException,RepeatKillException,SeckillCloseException;
}
