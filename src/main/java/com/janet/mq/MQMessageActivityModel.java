package com.janet.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2021/6/23
 * @Author Janet
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQMessageActivityModel implements Serializable {
    private String schemaName; //数据库名称

    private String tableName; //canal监控表的名字

    private Long activityId; //活动Id

    private String confirmOrder; //审批批次号
}
