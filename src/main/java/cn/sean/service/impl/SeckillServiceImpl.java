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

	//日志对象
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	//加入一个混淆字符串（秒杀接口）的salt（盐值字符串），为了避免用户猜出我们的md5值，值任意给，越复杂越好
	private final String salt="hidoifiewhnfienfnvnoiijijojaoi";
	
	//注入service依赖
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
		}//说明查不到该秒杀商品的记录
		
		//若秒杀未开启
		Date startTime=seckill.getStartTime();
		Date endTime=seckill.getEndTime();
		//系统当前时间
		Date nowTime=new Date();
		if (startTime.getTime()>nowTime.getTime()||endTime.getTime()<nowTime.getTime()) {
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		
		//秒杀开启，返回秒杀商品的ID和用来给接口加密的md5
		String md5=getMD5(seckillId);
		return new Exposer(true,md5,seckillId);
	}
	
	private String getMD5(long seckillId) {
		String base=seckillId+'/'+salt;
		String md5=DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	//秒杀是否成功，若成功，则减库存，并增加明细，否则，抛出异常，事务回滚
	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		
		if (md5==null||!md5.equals(getMD5(seckillId))) {
			//秒杀数据被重写
			throw new SeckillException("seckill data rewrite");
		}
		//执行秒杀逻辑：减库存，增加购买明细
		Date nowTime=new Date();
		
		try {
			//减库存
			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if (updateCount <= 0) {
				//没有更新库存记录，说明秒杀结束
				throw new SeckillCloseException("seckill is closed");
			} else {
				//否则更新了库存，说明秒杀成功，增加明细
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				//判断该明细是否被重复插入，即用户是否重复秒杀
				if (updateCount <= 0) {
					throw new RepeatKillException("seckill repeated");
				} else {
					//秒杀成功，得到成功插入的明细记录，并返回秒杀成功的信息
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
			//所有编译期异常转化为运行期异常
			throw new SeckillException("seckill inner error :"+e.getMessage());
		}
		
	}

}
