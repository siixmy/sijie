package com.tcmc.dubbo.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * testNG单元测试基类
 */
@ContextConfiguration(locations = {"classpath:dubbo-config.xml"})
public abstract class AbstractContextNGTest extends AbstractTestNGSpringContextTests {

}
