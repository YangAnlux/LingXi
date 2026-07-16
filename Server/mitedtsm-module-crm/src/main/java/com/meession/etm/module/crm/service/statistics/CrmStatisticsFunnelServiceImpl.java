package com.meession.etm.module.crm.service.statistics;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.date.LocalDateTimeUtils;
import com.meession.etm.module.crm.controller.admin.statistics.vo.funnel.*;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO;
import com.meession.etm.module.crm.dal.mysql.statistics.CrmStatisticsFunnelMapper;
import com.meession.etm.module.crm.enums.business.CrmBusinessEndStatusEnum;
import com.meession.etm.module.crm.service.business.CrmBusinessService;
import com.meession.etm.module.system.api.dept.DeptApi;
import com.meession.etm.module.system.api.dept.dto.DeptRespDTO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertList;

/**
 * CRM 销售漏斗分析 Service 实现类
 *
 * @author HUIHUI
 */
@Service
public class CrmStatisticsFunnelServiceImpl implements CrmStatisticsFunnelService {

    @Resource
    private CrmStatisticsFunnelMapper funnelMapper;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private CrmBusinessService businessService;
    @Resource
    private DeptApi deptApi;

    @Override
    public CrmStatisticFunnelSummaryRespVO getFunnelSummary(CrmStatisticsFunnelStatReqVO reqVO) {
        List<Long> userIds = getUserIds(reqVO);
        if (CollUtil.isEmpty(userIds)) {
            return null;
        }
        reqVO.setUserIds(userIds);

        Long customerCount = funnelMapper.selectCustomerCountByDate(reqVO);
        Long businessCount = funnelMapper.selectBusinessCountByDateAndEndStatus(reqVO, null);
        Long businessWinCount = funnelMapper.selectBusinessCountByDateAndEndStatus(reqVO, CrmBusinessEndStatusEnum.WIN.getStatus());
        return new CrmStatisticFunnelSummaryRespVO(customerCount, businessCount, businessWinCount);
    }

    @Override
    public List<CrmStatisticsBusinessSummaryByEndStatusRespVO> getBusinessSummaryByEndStatus(CrmStatisticsFunnelStatReqVO reqVO) {
        reqVO.setUserIds(getUserIds(reqVO));
        if (CollUtil.isEmpty(reqVO.getUserIds())) {
            return Collections.emptyList();
        }
        return funnelMapper.selectBusinessSummaryListGroupByEndStatus(reqVO);
    }

    @Override
    public List<CrmStatisticsBusinessSummaryByDateRespVO> getBusinessSummaryByDate(CrmStatisticsFunnelStatReqVO reqVO) {
        reqVO.setUserIds(getUserIds(reqVO));
        if (CollUtil.isEmpty(reqVO.getUserIds())) {
            return Collections.emptyList();
        }

        List<CrmStatisticsBusinessSummaryByDateRespVO> businessSummaryList = funnelMapper.selectBusinessSummaryGroupByDate(reqVO);
        List<LocalDateTime[]> timeRanges = LocalDateTimeUtils.getDateRangeList(reqVO.getTimes()[0], reqVO.getTimes()[1], reqVO.getInterval());
        return convertList(timeRanges, times -> {
            Long businessCreateCount = businessSummaryList.stream()
                    .filter(vo -> LocalDateTimeUtils.isBetween(times[0], times[1], vo.getTime()))
                    .mapToLong(CrmStatisticsBusinessSummaryByDateRespVO::getBusinessCreateCount).sum();
            BigDecimal businessDealCount = businessSummaryList.stream()
                    .filter(vo -> LocalDateTimeUtils.isBetween(times[0], times[1], vo.getTime()))
                    .map(CrmStatisticsBusinessSummaryByDateRespVO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new CrmStatisticsBusinessSummaryByDateRespVO()
                    .setTime(LocalDateTimeUtils.formatDateRange(times[0], times[1], reqVO.getInterval()))
                    .setBusinessCreateCount(businessCreateCount).setTotalPrice(businessDealCount);
        });
    }

    @Override
    public List<CrmStatisticsBusinessInversionRateSummaryByDateRespVO> getBusinessInversionRateSummaryByDate(CrmStatisticsFunnelStatReqVO reqVO) {
        reqVO.setUserIds(getUserIds(reqVO));
        if (CollUtil.isEmpty(reqVO.getUserIds())) {
            return Collections.emptyList();
        }

        List<CrmStatisticsBusinessInversionRateSummaryByDateRespVO> businessSummaryList = funnelMapper.selectBusinessInversionRateSummaryByDate(reqVO);
        List<LocalDateTime[]> timeRanges = LocalDateTimeUtils.getDateRangeList(reqVO.getTimes()[0], reqVO.getTimes()[1], reqVO.getInterval());
        return convertList(timeRanges, times -> {
            Long businessCount = businessSummaryList.stream()
                    .filter(vo -> LocalDateTimeUtils.isBetween(times[0], times[1], vo.getTime()))
                    .mapToLong(CrmStatisticsBusinessInversionRateSummaryByDateRespVO::getBusinessCount).sum();
            Long businessWinCount = businessSummaryList.stream()
                    .filter(vo -> LocalDateTimeUtils.isBetween(times[0], times[1], vo.getTime()))
                    .mapToLong(CrmStatisticsBusinessInversionRateSummaryByDateRespVO::getBusinessWinCount).sum();
            return new CrmStatisticsBusinessInversionRateSummaryByDateRespVO()
                    .setTime(LocalDateTimeUtils.formatDateRange(times[0], times[1], reqVO.getInterval()))
                    .setBusinessCount(businessCount).setBusinessWinCount(businessWinCount);
        });
    }

    @Override
    public List<CrmStatisticsBusinessSummaryByStatusRespVO> getBusinessSummaryByStatus(CrmStatisticsFunnelStatReqVO reqVO) {
        reqVO.setUserIds(getUserIds(reqVO));
        if (CollUtil.isEmpty(reqVO.getUserIds())) {
            return Collections.emptyList();
        }
        return funnelMapper.selectBusinessSummaryGroupByStatus(reqVO);
    }

    @Override
    public PageResult<CrmBusinessDO> getBusinessPageByDate(CrmStatisticsFunnelReqVO pageVO) {
        // 1. 获得用户编号数组
        pageVO.setUserIds(getUserIds(pageVO));
        if (CollUtil.isEmpty(pageVO.getUserIds())) {
            return PageResult.empty();
        }
        // 2. 执行查询
        return businessService.getBusinessPageByDate(pageVO);
    }

    private List<Long> getUserIds(CrmStatisticsFunnelStatReqVO reqVO) {
        if (ObjUtil.isNotNull(reqVO.getUserId())) {
            return ListUtil.of(reqVO.getUserId());
        }
        List<Long> deptIds = convertList(deptApi.getChildDeptList(reqVO.getDeptId()), DeptRespDTO::getId);
        deptIds.add(reqVO.getDeptId());
        return convertList(adminUserApi.getUserListByDeptIds(deptIds), AdminUserRespDTO::getId);
    }

    private List<Long> getUserIds(CrmStatisticsFunnelReqVO reqVO) {
        if (ObjUtil.isNotNull(reqVO.getUserId())) {
            return ListUtil.of(reqVO.getUserId());
        }
        List<Long> deptIds = convertList(deptApi.getChildDeptList(reqVO.getDeptId()), DeptRespDTO::getId);
        deptIds.add(reqVO.getDeptId());
        return convertList(adminUserApi.getUserListByDeptIds(deptIds), AdminUserRespDTO::getId);
    }

}
