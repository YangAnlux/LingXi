package com.meession.etm.module.crm.controller.admin.statistics.vo.funnel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CrmStatisticsBusinessSummaryByStatusRespVO {

    private Long statusTypeId;

    private String statusName;

    private BigDecimal percent;

    private Integer sort;

    private Long businessCount;

    private BigDecimal totalPrice;

}