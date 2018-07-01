package cn.sean.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import cn.sean.dao.SeckillDao;
import cn.sean.dao.SuccessKilledDao;
import cn.sean.domain.Seckill;
import cn.sean.domain.SuccessKilled;
import cn.sean.dto.Exposer;
import cn.sean.dto.SeckillExecution;
import cn.sean.enums.SeckillStatEnum;
import cn.sean.exception.RepeatKillException;
import cn.sean.exception.SeckillCloseException;
import cn.sean.exception.SeckillException;
import cn.sean.service.SeckillService;

/**
 * 
 * @author WangChao
 *
 */
//@Component @Service @Repository @Controller
@Service
public class SeckillServiceImpl implements SeckillService {

	//��־����
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	//����һ�������ַ�������ɱ�ӿڣ���salt����ֵ�ַ�������Ϊ�˱����û��³����ǵ�md5ֵ��ֵ�������Խ����Խ��
	private final String salt="hidoifiewhnfienfnvnoiijijojaoi";
	
	//ע��service����
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill=seckillDao.queryById(seckillId);
		if (seckill==null) {
			return new Exposer(false,seckillId);
		}//˵���鲻������ɱ��Ʒ�ļ�¼
		
		//����ɱδ����
		Date startTime=seckill.getStartTime();
		Date endTime=seckill.getEndTime();
		//ϵͳ��ǰʱ��
		Date nowTime=new Date();
		if (startTime.getTime()>nowTime.getTime()||endTime.getTime()<nowTime.getTime()) {
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		
		//��ɱ������������ɱ��Ʒ��ID���������ӿڼ��ܵ�md5
		String md5=getMD5(seckillId);
		return new Exposer(true,md5,seckillId);
	}
	
	private String getMD5(long seckillId) {
		String base=seckillId+'/'+salt;
		String md5=DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	//��ɱ�Ƿ�ɹ������ɹ��������棬��������ϸ�������׳��쳣������ع�
	@Override
	@Transactional
	/**
	 * ʹ��ע��������񷽷����ŵ�:
     * 1.�����ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷��
     * 2.��֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ���������������RPC/HTTP������߰��뵽���񷽷��ⲿ
     * 3.�������еķ�������Ҫ������ֻ��һ���޸Ĳ�����ֻ��������Ҫ�������
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		
		if (md5==null||!md5.equals(getMD5(seckillId))) {
			//��ɱ���ݱ���д
			throw new SeckillException("seckill data rewrite");
		}
		//ִ����ɱ�߼�������棬���ӹ�����ϸ
		Date nowTime=new Date();
		
		try {
			//�����
			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if (updateCount <= 0) {
				//û�и��¿���¼��˵����ɱ����
				throw new SeckillCloseException("seckill is closed");
			} else {
				//��������˿�棬˵����ɱ�ɹ���������ϸ
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				//�жϸ���ϸ�Ƿ��ظ����룬���û��Ƿ��ظ���ɱ
				if (updateCount <= 0) {
					throw new RepeatKillException("seckill repeated");
				} else {
					//��ɱ�ɹ����õ��ɹ��������ϸ��¼����������ɱ�ɹ�����Ϣ
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
				}
			} 
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//���б������쳣ת��Ϊ�������쳣
			throw new SeckillException("seckill inner error :"+e.getMessage());
		}
		
	}

}
