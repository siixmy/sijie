package com.tcmc.dubbo.test.api;

import com.tcmc.crm.vip.api.model.pojo.VipDeposit;
import com.tcmc.crm.vip.api.r.IRVipDepositService;
import com.tcmc.dispatcher.SafeHospitalId;
import com.tcmc.dubbo.ng.DBRouteConfigurer;
import com.tcmc.dubbo.test.AbstractContextNGTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Set;

/**
 * class_desc
 *
 * @author tantao
 * @date 2018/3/14
 */
public class IRVipDepositServiceTest extends AbstractContextNGTest {

    @Resource
    private IRVipDepositService rVipDepositService;

    @BeforeClass
    public void setUp() {

    }

    @Test
    public void queryInitialVipDeposit() {

        //获取所有机房路由与医院Id的映射
        Set<Long> routeHospitalIds = DBRouteConfigurer.allRouteHospitalIds();

        for (Long v : routeHospitalIds) {
            Long routeHosp = SafeHospitalId.getCurrentHospitalId();
            try {

                //遍历所有机房
                SafeHospitalId.setCurrentHospitalId(v);
                VipDeposit vipDeposit = rVipDepositService.sumChargeByParentHospitalId(v);

                Assert.assertNotNull(vipDeposit);

            } finally {
                SafeHospitalId.setCurrentHospitalId(routeHosp);
            }
        }
    }

    @AfterClass
    public void cleanup() {

    }
}
