package com.meession.etm.module.crm.service.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRulePageReqVO;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRuleRespVO;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRuleSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmCareRuleDO;
import com.meession.etm.module.crm.dal.mysql.care.CrmCareRuleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CrmCareRuleServiceImpl implements CrmCareRuleService {

    @Resource
    private CrmCareRuleMapper careRuleMapper;
    @Resource
    private CrmCustomerCareService customerCareService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRule(CrmCareRuleSaveReqVO reqVO) {
        CrmCareRuleDO rule = BeanUtils.toBean(reqVO, CrmCareRuleDO.class);
        careRuleMapper.insert(rule);
        return rule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRule(CrmCareRuleSaveReqVO reqVO) {
        CrmCareRuleDO rule = careRuleMapper.selectById(reqVO.getId());
        BeanUtils.copyProperties(reqVO, rule);
        careRuleMapper.updateById(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(Long id) {
        careRuleMapper.deleteById(id);
    }

    @Override
    public CrmCareRuleRespVO getRule(Long id) {
        CrmCareRuleDO rule = careRuleMapper.selectById(id);
        return BeanUtils.toBean(rule, CrmCareRuleRespVO.class);
    }

    @Override
    public PageResult<CrmCareRuleRespVO> getRulePage(CrmCareRulePageReqVO pageReqVO) {
        PageResult<CrmCareRuleDO> page = careRuleMapper.selectPage(pageReqVO);
        List<CrmCareRuleRespVO> vos = BeanUtils.toBean(page.getList(), CrmCareRuleRespVO.class);
        return new PageResult<>(vos, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeRule(Long id) {
        CrmCareRuleDO rule = careRuleMapper.selectById(id);
        if (rule == null) return 0;

        int result;
        if ("BIRTHDAY".equals(rule.getTriggerType())) {
            result = customerCareService.executeBirthdayCare();
        } else {
            result = customerCareService.executeHolidayCare();
        }

        // 更新最近执行时间
        rule.setLastExecTime(LocalDateTime.now());
        careRuleMapper.updateById(rule);

        log.info("[CrmCareRuleServiceImpl][手动执行规则 id={}, name={}, 发送数={}]", id, rule.getName(), result);
        return result;
    }

}
