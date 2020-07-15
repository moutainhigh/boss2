package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class SuperBankUserJob extends ScheduleJob {

    @Resource
    private SuperBankService superBankService;

    private static final Logger log = LoggerFactory.getLogger(SuperBankUserJob.class);

    @Override
    protected void runTask(String runNo) {
        log.info("======== 超级银行家用户开户 start");
        superBankService.createMerAccount();
        log.info("======== 超级银行家用户开户 end");
    }
}
