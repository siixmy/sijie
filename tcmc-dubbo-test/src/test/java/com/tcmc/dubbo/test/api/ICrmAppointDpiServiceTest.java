package com.tcmc.dubbo.test.api;

import com.google.common.collect.Lists;
import com.tcmc.crm.appoint.api.t.ICrmAppointDpiService;
import com.tcmc.crm.appoint.api.t.model.QueueFreePojo;
import com.tcmc.crm.appoint.api.t.model.QueueValidResult;
import com.tcmc.crm.constvar.exception.CrmException;
import com.tcmc.dispatcher.SafeHospitalId;
import com.tcmc.dubbo.test.AbstractContextTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * class_desc
 *
 * @author tantao
 * @date 2018/3/14
 */
public class ICrmAppointDpiServiceTest extends AbstractContextTest {

    @Resource
    private ICrmAppointDpiService crmAppointDpiService;

    private static final Long pHID = 72L;

    @Before
    public void init() {

        SafeHospitalId.setCurrentHospitalId(pHID);
    }

    @AfterClass
    public static void cleanup() {
    }

    @Test
    public void queryPlatAppointQueueByAppointKey() throws CrmException {

        String result = crmAppointDpiService.queryPlatAppointQueueByAppointKey(174226218202422302L, pHID);
//        Assert.notNull(result.equals("0571"));
    }

    @Test
    public void queryValidAppointQueue() throws CrmException {

        List<QueueFreePojo> queueFreePojos = Lists.newArrayList(new QueueFreePojo(1025L, Lists.newArrayList(2), null));
        List<QueueValidResult> queueValidResults = crmAppointDpiService.queryValidAppointQueue(73L, 271L, queueFreePojos);
//        Assert.notNull(queueValidResults);
    }


}
