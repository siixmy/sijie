package com.tcmc.dubbo.ng;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * DB路由及机房关系配置
 *
 * @author tantao
 * @date 2018/3/6
 */
public class DBRouteConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DBRouteConfigurer.class);

    private final static String hIdDbRelationFormat = "(\\[\\d+,\\d+\\];)+";

    /**
     * 子机房编号与持有医院关联
     */
    private String dbRouteDbIndex2HID;

    private static Set<Long> allRouteHIDs;

    public DBRouteConfigurer(String dbRouteDbIndex2HID) {

        this.dbRouteDbIndex2HID = dbRouteDbIndex2HID;
    }

    /**
     * 获取所有子机房路由的医院ID集合
     *
     * @return
     */
    public static Set<Long> allRouteHospitalIds() {
        return allRouteHIDs;
    }

    public void init() {

        generateAllRouteHospitalIds(dbRouteDbIndex2HID);
    }

    /**
     * 构造所有子机房路由的医院ID集合
     *
     * @param hIdDbRelationStr
     */
    private void generateAllRouteHospitalIds(String hIdDbRelationStr) {
        Map<Integer, Long> hdRelationMapTp = new HashMap<>();

        if (StringUtils.isBlank(hIdDbRelationStr)) {
            throw new IllegalArgumentException("子机房编号与持有医院关联不允许为空");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("子机房编号与持有医院关联: " + hIdDbRelationStr);
        }

        Pattern pattern = Pattern.compile(hIdDbRelationFormat);
        if (!pattern.matcher(hIdDbRelationStr).matches()) {
            String errorMsg = "子机房编号与持有医院关联:" + hIdDbRelationStr + " 格式非法, 正确格式为:\"[dbId,pHId];[dbId,pHId];\"";
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        String[] hdrArray = hIdDbRelationStr.split(";");
        for (String hdr : hdrArray) {
            if (logger.isDebugEnabled()) {
                logger.debug("parser pHId和dbId对应关系:" + hdr);
            }
            String[] hdrItems = hdr.replace("[", "").replace("]", "").split(",");
            if (hdrItems.length != 2) {
                String errorMsg = "pHId和dbId对应关系, 参数非法:" + hdr;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("parsed dbId:" + hdrItems[0]);
                logger.debug("parsed phId:" + hdrItems[1]);
            }

            Integer dbId = Integer.parseInt(hdrItems[0]);
            Long phId = Long.parseLong(hdrItems[1]);
            hdRelationMapTp.put(dbId, phId);
        }


        allRouteHIDs = Sets.newHashSet(hdRelationMapTp.values());
    }

}
