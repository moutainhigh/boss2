package cn.eeepay.framework.service.unTransactionalImpl.job;

import cn.eeepay.framework.service.TimeTaskCollectionService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 出款费率定时任务
 * @author MXG
 * create 2018/11/06
 */
@Component
@Scope("prototype")
public class UpdateOutAccountServiceJob extends ScheduleJob {

    private static final Logger log = LoggerFactory.getLogger(UpdateOutAccountServiceJob.class);

    @Resource
    private TimeTaskCollectionService timeTaskCollectionService;

    @Override
    protected void runTask(String runNo) {
        timeTaskCollectionService.updateOutAccountService();
    }
}
