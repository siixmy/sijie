package com.tcmc.dubbo.test.api;

import com.google.common.collect.Lists;
import com.tcmc.crm.appoint.api.model.ext.FugouOrdersDictDto;
import com.tcmc.crm.appoint.api.model.pojo.FugouOrders;
import com.tcmc.crm.appoint.api.model.pojo.OrderApplyRelation;
import com.tcmc.crm.appoint.api.t.IFugouOrdersDpiService;
import com.tcmc.dispatcher.SafeHospitalId;
import com.tcmc.dubbo.test.AbstractContextTest;
import com.tcmc.crm.constvar.exception.CrmException;
import org.dbunit.Assertion;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sijie
 * @date 2018/3/19
 */
public class IFugouOrdersDpiServiceTest extends AbstractContextTest {
    @Resource
    private IFugouOrdersDpiService fugouOrdersDpiService;

    private static final Long pHID = 6L;
    @Before
    public void init() {

        SafeHospitalId.setCurrentHospitalId(pHID);
    }

    @AfterClass
    public static void cleanup() {

    }
    @Test
    public void selectFugouOrdersByOrderNos() throws Exception{
        backupCustom("crm_fugou_orders");  //备份表
        Set<String> OrderNo=new HashSet<String>();
        OrderNo.add("23543550");OrderNo.add("23543551");
        List<FugouOrders> fugouOrderses = fugouOrdersDpiService.selectFugouOrdersByOrderNos(OrderNo);
        //get expect result from DB
        QueryDataSet queryDataSet = getQueryDataSet();
        queryDataSet.addTable("test", "select * from crm_fugou_orders where order_no in('23543550','23543551')");
        ITable dbTable = queryDataSet.getTable("test");
        Assert.assertEquals(dbTable.getRowCount(), fugouOrderses.size()); //断言
        rollback(); //回滚
    }
 @Test
    public void insertFugouOrders() throws Exception{
        backupCustom("crm_fugou_orders");  //备份表
        clearTable("crm_fugou_orders");  //清空表内数据
        FugouOrders fugouOrders = new FugouOrders();
        fugouOrders.setOrderNo("23543550");
        fugouOrders.setPurchaserName("66");fugouOrders.setPurchaserSex(1);
        fugouOrders.setPurchaserMobile("4454343");fugouOrders.setParentHospitalId(6L);
        fugouOrders.setSubHospitalId(33L);fugouOrders.setDiseaseType(1);
        fugouOrders.setDoctorName("呵呵");fugouOrders.setPurchaserBirth(new Date());
        fugouOrders.setProductName("333");fugouOrders.setProductNum(1);
        fugouOrders.setValidTime(new Date()); fugouOrders.setOrderStatus(1);
        fugouOrders.setCreateTime(new Date()); fugouOrders.setUpdateTime(new Date());
        fugouOrders.setHandleType(1);
       fugouOrdersDpiService.insertFugouOrders(fugouOrders);
     //get actual tableInfo from DB
      IDataSet dbDataSet = getDBDataSet();
       ITable dbTable = dbDataSet.getTable("crm_fugou_orders");
     //get expect Information from xml file
      IDataSet xmlDataSet = getXmlDataSet("dbunitExpectXml/expectCrmFugouOrders.xml");
     // handle null value, replace "[null]" strings with null
     ReplacementDataSet replacementDataSet = createReplacementDataSet(xmlDataSet);
     ITable xmlTable = replacementDataSet.getTable("crm_fugou_orders");
     //exclude some columns which don't want to compare result
     dbTable = DefaultColumnFilter.excludedColumnsTable(dbTable, new String[]{"order_id", "valid_time","create_time","update_time"});
     xmlTable = DefaultColumnFilter.excludedColumnsTable(xmlTable, new String[]{"order_id", "valid_time","create_time","update_time"});
     rollback();  //回滚
     Assertion.assertEquals(xmlTable, dbTable);//断言
    }
    @Test
    public void insertFugouOrders1() throws CrmException{
        FugouOrders fugouOrders = new FugouOrders();
       fugouOrdersDpiService.insertFugouOrders(fugouOrders);
    }
    @Test
    public void insertFugouManyOrders() throws CrmException{  //有问题，错误: <insert id="insertBatch">关系 "crm_fugou_orders" 的 "apply_id" 字段不存在
        FugouOrders fugouOrders = new FugouOrders();
        fugouOrders.setOrderNo("23543552");
        fugouOrders.setPurchaserName("咳咳");fugouOrders.setPurchaserSex(1);
        fugouOrders.setPurchaserMobile("4454343");fugouOrders.setParentHospitalId(6L);
        fugouOrders.setSubHospitalId(33L);fugouOrders.setDiseaseType(1);
        fugouOrders.setDoctorName("呵呵");fugouOrders.setPurchaserBirth(new Date());
        fugouOrders.setProductName("333");fugouOrders.setProductNum(1);
        fugouOrders.setValidTime(new Date()); fugouOrders.setOrderStatus(1);
        fugouOrders.setCreateTime(new Date()); fugouOrders.setUpdateTime(new Date());
        fugouOrders.setHandleType(1);
        FugouOrders fugouOrders1 = new FugouOrders();
        fugouOrders1.setOrderNo("23543550");
        fugouOrders1.setPurchaserName("嘿嘿");fugouOrders1.setPurchaserSex(1);
        fugouOrders1.setPurchaserMobile("4454343");fugouOrders1.setParentHospitalId(6L);
        fugouOrders1.setSubHospitalId(33L);fugouOrders1.setDiseaseType(1);
        fugouOrders1.setDoctorName("呵呵");fugouOrders1.setPurchaserBirth(new Date());
        fugouOrders1.setProductName("333");fugouOrders1.setProductNum(1);
        fugouOrders1.setValidTime(new Date()); fugouOrders1.setOrderStatus(1);
        fugouOrders1.setCreateTime(new Date()); fugouOrders1.setUpdateTime(new Date());
        fugouOrders1.setHandleType(1);
        ArrayList<FugouOrders> objects = Lists.newArrayList();
        objects.add(fugouOrders);objects.add(fugouOrders1);
        fugouOrdersDpiService.insertFugouOrders(objects);

    }
    @Test
    public void updateFugouOrders() throws Exception{
        backupCustom("crm_fugou_orders");  //备份表
        FugouOrders fugouOrders = new FugouOrders();
        fugouOrders.setOrderNo("23543550");
        fugouOrders.setPurchaserName("66");fugouOrders.setPurchaserSex(1);
        fugouOrders.setPurchaserMobile("4454343");fugouOrders.setParentHospitalId(6L);
        fugouOrders.setSubHospitalId(33L);fugouOrders.setDiseaseType(1);
        fugouOrders.setDoctorName("呵呵");fugouOrders.setPurchaserBirth(new Date());
        fugouOrders.setProductName("333");fugouOrders.setProductNum(1);
        fugouOrders.setValidTime(new Date()); fugouOrders.setOrderStatus(1);
        fugouOrders.setCreateTime(new Date()); fugouOrders.setUpdateTime(new Date());
        fugouOrders.setHandleType(1);
        int i = fugouOrdersDpiService.updateFugouOrders(fugouOrders);
        //get actual tableInfo from DB
        IDataSet dbDataSet = getDBDataSet();
        ITable dbTable = dbDataSet.getTable("crm_fugou_orders");
        //get expect Information from xml file
        IDataSet xmlDataSet = getXmlDataSet("dbunitExpectXml/expectCrmFugouOrders.xml");
        // handle null value, replace "[null]" strings with null
        ReplacementDataSet replacementDataSet = createReplacementDataSet(xmlDataSet);
        ITable xmlTable = replacementDataSet.getTable("crm_fugou_orders");
        //exclude some columns which don't want to compare result
        dbTable = DefaultColumnFilter.excludedColumnsTable(dbTable, new String[]{"order_id", "valid_time","create_time","update_time"});
        xmlTable = DefaultColumnFilter.excludedColumnsTable(xmlTable, new String[]{"order_id", "valid_time","create_time","update_time"});
        rollback();  //回滚
        Assertion.assertEquals(xmlTable, dbTable);//断言

    }

    @Test
    public void insertDiseaseType() throws CrmException{
        ArrayList<FugouOrdersDictDto> objects = Lists.newArrayList();
        FugouOrdersDictDto dictDto = new FugouOrdersDictDto();
        FugouOrdersDictDto dictDto1 = new FugouOrdersDictDto();
       dictDto.setDictKey("666");dictDto.setDictType("201");/*
        dictDto.setDictValue("333");dictDto.setParentHospitalId(6l);*/
        dictDto1.setDictKey("666");dictDto1.setDictType("201");/*
        dictDto1.setDictValue("333");dictDto1.setParentHospitalId(6l);*/
        objects.add(dictDto);objects.add(dictDto1);
        fugouOrdersDpiService.insertDiseaseType(objects);
    }
    @Test
    public void insertOrderApplyRelation() throws CrmException{//有问题，当订单和申请单关系已经存在时，更新失败。
        // 当OrderNo存在相同时，而ParentHospitalId不同时，插入会报错的：错误: 重复键违反唯一约束"only_apply_order_no
        OrderApplyRelation oar = new OrderApplyRelation();
        oar.setParentHospitalId(7l);oar.setApplyId(7l);
        oar.setOrderNo("2543555");
        fugouOrdersDpiService.insertOrderApplyRelation(oar);
    }
    @Test
    public void selectRelationByOrderNo() throws CrmException{
        OrderApplyRelation orderApplyRelation = fugouOrdersDpiService.selectRelationByOrderNo("2543555", 7l);
        if(orderApplyRelation==null){
            System.out.println("没搜到！");
        }else{
            System.out.println("搜到了！");
        }
    }

}
