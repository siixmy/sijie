package com.tcmc.dubbo.test;

import com.tcmc.dubbo.test.api.ICrmAppointDpiServiceTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于重复测试，不重启
 *
 * @author lilan
 * @version v2.0.0
 * @date 2016/11/4 9:00
 */
public class TestNoRestartRunner {

    public static void main(String[] args) {
        List<Result> res = new ArrayList<Result>();
        for (int i = 0; i < 10; i++) {
            Result result = JUnitCore.runClasses(ICrmAppointDpiServiceTest.class);
            res.add(result);
        }

        int i = 0;
        System.out.println("共执行本用例的次数为：" + res.size());
        for (Result result : res) {
            i = i + 1;
            System.out.println("第" + i + "遍执行结果：");
            System.out.println(result.getRunCount());
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.getTrace());
            }

            System.out.println(result.wasSuccessful());
        }
    }

}