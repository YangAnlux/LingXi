package com.meession.etm.module.crm.service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板变量替换工具类
 * 同时支持 {varName} 和 ${varName} 两种占位符格式
 * 缺失变量自动替换为默认值"贵宾"
 */
public class VariableResolver {

    /** 默认替换值：当模板变量在数据中缺失时使用 */
    private static final String DEFAULT_VALUE = "贵宾";

    /** 匹配 {varName} 或 ${varName} */
    private static final Pattern VAR_PATTERN = Pattern.compile("\\$?\\{(\\w+)}");

    /**
     * 解析模板中所有变量名
     */
    public static Set<String> extractVariables(String template) {
        Set<String> vars = new LinkedHashSet<>();
        if (template == null || template.isEmpty()) return vars;
        Matcher matcher = VAR_PATTERN.matcher(template);
        while (matcher.find()) {
            vars.add(matcher.group(1));
        }
        return vars;
    }

    /**
     * 检查模板中哪些变量在提供的数据中缺失
     * @return 缺失的变量名列表
     */
    public static List<String> checkMissingVars(String template, Set<String> availableKeys) {
        Set<String> required = extractVariables(template);
        List<String> missing = new ArrayList<>();
        for (String var : required) {
            if (!availableKeys.contains(var)) {
                missing.add(var);
            }
        }
        return missing;
    }

    /**
     * 替换模板变量，缺失变量使用默认值"贵宾"
     */
    public static String resolveOrDefault(String template, Map<String, String> values) {
        return resolve(template, values).getResolvedContent();
    }

    /**
     * 完整解析模板，返回替换后内容和缺失变量列表
     */
    public static ResolveResult resolve(String template, Map<String, String> values) {
        if (template == null || template.isEmpty()) {
            return new ResolveResult(template, Collections.emptyList());
        }
        List<String> missingVars = new ArrayList<>();
        String resolved = template;

        Matcher matcher = VAR_PATTERN.matcher(template);
        // 使用 StringBuffer 收集替换结果
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String varName = matcher.group(1);
            String replacement = values != null && values.containsKey(varName)
                    ? values.get(varName)
                    : null;
            if (replacement == null || replacement.isEmpty()) {
                missingVars.add(varName);
                replacement = DEFAULT_VALUE;
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        return new ResolveResult(sb.toString(), missingVars);
    }

    @Data
    @AllArgsConstructor
    public static class ResolveResult {
        private String resolvedContent;
        private List<String> missingVariables;

        public boolean hasMissing() {
            return missingVariables != null && !missingVariables.isEmpty();
        }
    }

}
