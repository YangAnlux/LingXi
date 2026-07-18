// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] CRM BPMN 流程自动部署 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.service.process;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CRM 模块 BPMN 流程定义自动部署器
 * 启动时自动部署 CRM 审批流程，包括：
 * 1. Flowable ProcessDefinition（ACT_RE_PROCDEF）
 * 2. Flowable Model（ACT_RE_MODEL，用于模型编辑器编辑）
 * 3. bpm_process_definition_info（项目扩展元信息）
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@Slf4j
@Component
public class CrmBpmnProcessDeployer implements CommandLineRunner {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SIMPLE_MODEL_JSON = "{\"id\":\"node_start_user_01\",\"type\":10,\"name\":\"发起人\","
            + "\"childNode\":{\"id\":\"node_approve_01\",\"type\":11,\"name\":\"审批人\","
            + "\"childNode\":{\"id\":\"node_end_01\",\"type\":1,\"name\":\"结束\"},"
            + "\"candidateStrategy\":30,\"candidateParam\":\"1\",\"approveType\":1,\"approveMethod\":1}}";

    private final AtomicInteger idCounter = new AtomicInteger(455);

    @Override
    public void run(String... args) {
        deployIfNotExists("crm-contract-audit", "合同审批", "/crm/contract");
        deployIfNotExists("crm-receivable-audit", "回款审批", "/crm/receivable");
        deployIfNotExists("crm-refund-audit", "退款审批", "/crm/refund");
        log.info("[CrmBpmnProcessDeployer] 所有 CRM BPMN 流程定义部署检查完成");
    }

    private void deployIfNotExists(String processKey, String processName, String formPath) {
        // 1. 部署/检查 Flowable ProcessDefinition
        ProcessDefinition definition = ensureProcessDefinition(processKey, processName);
        if (definition == null) return;

        // 2. 确保 Flowable Model 存在（用于模型编辑器查看/编辑）
        String modelId = ensureModel(processKey, processName, formPath);

        // 3. 确保 bpm_process_definition_info 存在
        ensureProcessDefinitionInfo(processKey, definition.getId(), modelId, formPath);
    }

    private ProcessDefinition ensureProcessDefinition(String processKey, String processName) {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (definition != null) {
            log.info("[CrmBpmnProcessDeployer] 流程定义已存在: key={}", processKey);
            return definition;
        }
        BpmnModel bpmnModel = buildModelerCompatibleProcess(processKey, processName);
        repositoryService.createDeployment()
                .name(processName)
                .key(processKey)
                .category("CRM")
                .tenantId("1")
                .addBpmnModel(processKey + ".bpmn20.xml", bpmnModel)
                .disableSchemaValidation()
                .deploy();
        definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey).latestVersion().singleResult();
        log.info("[CrmBpmnProcessDeployer] 流程部署成功: key={}, id={}", processKey,
                definition != null ? definition.getId() : "null");
        return definition;
    }

    private String ensureModel(String processKey, String processName, String formPath) {
        // 检查是否已有 Model
        Model model = repositoryService.createModelQuery()
                .modelKey(processKey).singleResult();
        if (model != null) {
            log.info("[CrmBpmnProcessDeployer] Model 已存在: key={}, id={}", processKey, model.getId());
            return model.getId();
        }

        // 创建 Model
        model = repositoryService.newModel();
        model.setKey(processKey);
        model.setName(processName);
        // [MODIFY] 使用现有 bpm_category 分类 code "001" - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
        model.setCategory("001");
        model.setTenantId("1");
        model.setVersion(1);

        // 构建 META_INFO（与 Flowable 模型器格式一致）
        String metaInfo = "{\"icon\":null,\"description\":null,\"type\":20,\"formType\":20,"
                + "\"formId\":null,\"formCustomCreatePath\":\"" + formPath + "\","
                + "\"formCustomViewPath\":\"" + formPath + "\","
                + "\"visible\":true,\"startUserIds\":null,\"startDeptIds\":null,\"managerUserIds\":[1],"
                + "\"sort\":0,\"allowCancelRunningProcess\":null,\"allowWithdrawTask\":null,"
                + "\"processIdRule\":null,\"autoApprovalType\":null,\"titleSetting\":null,"
                + "\"summarySetting\":null,\"processBeforeTriggerSetting\":null,"
                + "\"processAfterTriggerSetting\":null,\"taskBeforeTriggerSetting\":null,"
                + "\"taskAfterTriggerSetting\":null,\"printTemplateSetting\":null}";
        model.setMetaInfo(metaInfo);
        repositoryService.saveModel(model);

        // 保存模型编辑器 BPMN XML（source）- 完整 XML 含 BPMNDiagram 布局坐标
        byte[] bpmnXml = buildFullBpmnXml(processKey, processName);
        repositoryService.addModelEditorSource(model.getId(), bpmnXml);

        // 保存简单模型 JSON（source-extra）
        repositoryService.addModelEditorSourceExtra(model.getId(),
                SIMPLE_MODEL_JSON.getBytes(StandardCharsets.UTF_8));

        log.info("[CrmBpmnProcessDeployer] Model 创建成功: key={}, id={}", processKey, model.getId());
        return model.getId();
    }

    private void ensureProcessDefinitionInfo(String processKey, String definitionId,
                                              String modelId, String formPath) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM bpm_process_definition_info WHERE process_definition_id = ?",
                Integer.class, definitionId);
        if (count != null && count > 0) {
            // [MODIFY] 更新 model_id 为正确的 ACT_RE_MODEL ID - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
            jdbcTemplate.update("UPDATE bpm_process_definition_info SET model_id = ?, category = 'CRM' "
                    + "WHERE process_definition_id = ?", modelId, definitionId);
            log.info("[CrmBpmnProcessDeployer] bpm_process_definition_info 已存在: key={}", processKey);
            return;
        }
        jdbcTemplate.update("INSERT INTO bpm_process_definition_info "
                + "(id, process_definition_id, model_id, model_type, category, form_type, "
                + " form_custom_create_path, form_custom_view_path, simple_model, sort, visible, "
                + " manager_user_ids, creator, create_time, updater, update_time, deleted, tenant_id) "
                + "VALUES (?, ?, ?, 20, 'CRM', 20, ?, ?, ?, 0, '', 1, '1', NOW(), '1', NOW(), '', 1)",
                idCounter.incrementAndGet(), definitionId, modelId,
                formPath, formPath, SIMPLE_MODEL_JSON);
        log.info("[CrmBpmnProcessDeployer] bpm_process_definition_info 创建成功: key={}", processKey);
    }

    /**
     * 构建完整的 BPMN XML（含 BPMNDiagram 布局坐标，与 Flowable 模型器输出格式完全一致）
     */
    private byte[] buildFullBpmnXml(String processKey, String processName) {
        String sf1Uuid = UUID.randomUUID().toString();
        String sf2Uuid = UUID.randomUUID().toString();
        String sf3Uuid = UUID.randomUUID().toString();
        return ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                + " xmlns:flowable=\"http://flowable.org/bpmn\""
                + " xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\""
                + " xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\""
                + " xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\""
                + " typeLanguage=\"http://www.w3.org/2001/XMLSchema\""
                + " expressionLanguage=\"http://www.w3.org/1999/XPath\""
                + " targetNamespace=\"http://www.flowable.org/test\">\n"
                + "  <process id=\"" + processKey + "\" name=\"" + processName + "\" isExecutable=\"true\">\n"
                + "    <startEvent id=\"StartEvent\" name=\"开始\"></startEvent>\n"
                + "    <userTask id=\"node_start_user_01\" name=\"发起人\">\n"
                + "      <extensionElements>\n"
                + "        <flowable:approveType xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[1]]></flowable:approveType>\n"
                + "        <flowable:candidateStrategy xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[36]]></flowable:candidateStrategy>\n"
                + "        <flowable:assignStartUserHandlerType xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[2]]></flowable:assignStartUserHandlerType>\n"
                + "      </extensionElements>\n"
                + "    </userTask>\n"
                + "    <userTask id=\"node_approve_01\" name=\"审批人\">\n"
                + "      <extensionElements>\n"
                + "        <flowable:approveType xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[1]]></flowable:approveType>\n"
                + "        <flowable:candidateStrategy xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[30]]></flowable:candidateStrategy>\n"
                + "        <flowable:candidateParam xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[1]]></flowable:candidateParam>\n"
                + "        <flowable:approveMethod xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[1]]></flowable:approveMethod>\n"
                + "        <flowable:signEnable xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[false]]></flowable:signEnable>\n"
                + "        <flowable:reasonRequire xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[false]]></flowable:reasonRequire>\n"
                + "        <flowable:nodeType xmlns:flowable=\"http://flowable.org/bpmn\"><![CDATA[11]]></flowable:nodeType>\n"
                + "      </extensionElements>\n"
                + "    </userTask>\n"
                + "    <endEvent id=\"node_end_01\" name=\"结束\"></endEvent>\n"
                + "    <sequenceFlow id=\"" + sf1Uuid + "\" sourceRef=\"StartEvent\" targetRef=\"node_start_user_01\"></sequenceFlow>\n"
                + "    <sequenceFlow id=\"" + sf2Uuid + "\" sourceRef=\"node_start_user_01\" targetRef=\"node_approve_01\"></sequenceFlow>\n"
                + "    <sequenceFlow id=\"" + sf3Uuid + "\" sourceRef=\"node_approve_01\" targetRef=\"node_end_01\"></sequenceFlow>\n"
                + "  </process>\n"
                + "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_" + processKey + "\">\n"
                + "    <bpmndi:BPMNPlane bpmnElement=\"" + processKey + "\" id=\"BPMNPlane_" + processKey + "\">\n"
                + "      <bpmndi:BPMNShape bpmnElement=\"StartEvent\" id=\"BPMNShape_StartEvent\">\n"
                + "        <omgdc:Bounds height=\"30.0\" width=\"30.0\" x=\"0.0\" y=\"15.0\"></omgdc:Bounds>\n"
                + "      </bpmndi:BPMNShape>\n"
                + "      <bpmndi:BPMNShape bpmnElement=\"node_start_user_01\" id=\"BPMNShape_node_start_user_01\">\n"
                + "        <omgdc:Bounds height=\"60.0\" width=\"100.0\" x=\"80.0\" y=\"0.0\"></omgdc:Bounds>\n"
                + "      </bpmndi:BPMNShape>\n"
                + "      <bpmndi:BPMNShape bpmnElement=\"node_approve_01\" id=\"BPMNShape_node_approve_01\">\n"
                + "        <omgdc:Bounds height=\"60.0\" width=\"100.0\" x=\"230.0\" y=\"0.0\"></omgdc:Bounds>\n"
                + "      </bpmndi:BPMNShape>\n"
                + "      <bpmndi:BPMNShape bpmnElement=\"node_end_01\" id=\"BPMNShape_node_end_01\">\n"
                + "        <omgdc:Bounds height=\"30.0\" width=\"30.0\" x=\"380.0\" y=\"15.0\"></omgdc:Bounds>\n"
                + "      </bpmndi:BPMNShape>\n"
                + "      <bpmndi:BPMNEdge bpmnElement=\"" + sf1Uuid + "\" id=\"BPMNEdge_" + sf1Uuid + "\">\n"
                + "        <omgdi:waypoint x=\"30.0\" y=\"30.0\"></omgdi:waypoint>\n"
                + "        <omgdi:waypoint x=\"80.0\" y=\"30.0\"></omgdi:waypoint>\n"
                + "      </bpmndi:BPMNEdge>\n"
                + "      <bpmndi:BPMNEdge bpmnElement=\"" + sf2Uuid + "\" id=\"BPMNEdge_" + sf2Uuid + "\">\n"
                + "        <omgdi:waypoint x=\"180.0\" y=\"30.0\"></omgdi:waypoint>\n"
                + "        <omgdi:waypoint x=\"230.0\" y=\"30.0\"></omgdi:waypoint>\n"
                + "      </bpmndi:BPMNEdge>\n"
                + "      <bpmndi:BPMNEdge bpmnElement=\"" + sf3Uuid + "\" id=\"BPMNEdge_" + sf3Uuid + "\">\n"
                + "        <omgdi:waypoint x=\"330.0\" y=\"30.0\"></omgdi:waypoint>\n"
                + "        <omgdi:waypoint x=\"380.0\" y=\"30.0\"></omgdi:waypoint>\n"
                + "      </bpmndi:BPMNEdge>\n"
                + "    </bpmndi:BPMNPlane>\n"
                + "  </bpmndi:BPMNDiagram>\n"
                + "</definitions>").getBytes(StandardCharsets.UTF_8);
    }

    private BpmnModel buildModelerCompatibleProcess(String processKey, String processName) {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId(processKey);
        process.setName(processName);
        process.setExecutable(true);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("StartEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask startUserTask = new UserTask();
        startUserTask.setId("node_start_user_01");
        startUserTask.setName("发起人");
        addFlowableExtension(startUserTask, "approveType", "1");
        addFlowableExtension(startUserTask, "candidateStrategy", "36");
        addFlowableExtension(startUserTask, "assignStartUserHandlerType", "2");
        process.addFlowElement(startUserTask);

        UserTask approveTask = new UserTask();
        approveTask.setId("node_approve_01");
        approveTask.setName("审批");
        addFlowableExtension(approveTask, "approveType", "1");
        addFlowableExtension(approveTask, "candidateStrategy", "30");
        addFlowableExtension(approveTask, "candidateParam", "1");
        addFlowableExtension(approveTask, "approveMethod", "1");
        addFlowableExtension(approveTask, "signEnable", "false");
        addFlowableExtension(approveTask, "reasonRequire", "false");
        addFlowableExtension(approveTask, "nodeType", "11");
        process.addFlowElement(approveTask);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("node_end_01");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);

        process.addFlowElement(new SequenceFlow("StartEvent", "node_start_user_01"));
        process.addFlowElement(new SequenceFlow("node_start_user_01", "node_approve_01"));
        process.addFlowElement(new SequenceFlow("node_approve_01", "node_end_01"));

        model.addProcess(process);
        return model;
    }

    private void addFlowableExtension(UserTask task, String name, String value) {
        ExtensionElement element = new ExtensionElement();
        element.setName(name);
        element.setNamespace("http://flowable.org/bpmn");
        element.setNamespacePrefix("flowable");
        element.setElementText(value);
        task.addExtensionElement(element);
    }
}
// [ADD END] CRM BPMN 流程自动部署 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
