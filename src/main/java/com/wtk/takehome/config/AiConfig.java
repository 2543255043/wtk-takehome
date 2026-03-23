package com.wtk.takehome.config;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    // 是否启用Mock模式（application.yml中配置，默认true）
    @Value("${ai.mock.enabled:true}")
    private boolean mockEnabled;

    // 真实AI接口的API Key（可选配置）
    @Value("${ai.api.key:}")
    private String apiKey;

    public boolean isMockEnabled() {
        // 兜底逻辑：无API Key时强制启用Mock
        return mockEnabled || StringUtils.isBlank(apiKey);
    }

    public String getApiKey() {
        return apiKey;
    }
}