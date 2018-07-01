package cn.sean.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sean.domain.Seckill;
import cn.sean.dto.Exposer;
import cn.sean.dto.SeckillExecution;
import cn.sean.exception.RepeatKillException;
import cn.sean.exception.SeckillCloseException;

/**
 * 
 * @author WangChao
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
					   "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> list=seckillService.getSeckillList();
        logger.info("list={}", list);
	}

	@Test
	public void testGetById() throws Exception {
		long id=1000;
        Seckill seckill=seckillService.getById(id);
        logger.info("seckill={}",seckill);
	}

	//���ɲ��Դ��������߼���ע����ظ�ִ��
	@Test
	public void testSeckillLogic() throws Exception {
		long id=1000;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long phone=13476191876L;
            String md5=exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //��ɱδ����
            logger.warn("exposer={}", exposer);
        }
	}

}
