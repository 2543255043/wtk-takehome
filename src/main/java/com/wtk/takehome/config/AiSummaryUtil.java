package com.wtk.takehome.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.wtk.takehome.config.AiConfig;
import com.wtk.takehome.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import cn.hutool.json.JSONUtil;
@Slf4j
@Component
@RequiredArgsConstructor
public class AiSummaryUtil {
    private final AiConfig aiConfig;


    private static final String XUNFEI_APP_ID = "你的讯飞APPID";
    private static final String XUNFEI_API_SECRET = "你的讯飞API_SECRET";
    private static final String XUNFEI_API_URL = "https://spark-api.xf-yun.com/v3.5/chat/completions";

    /**
     * 生成酒店评论摘要（自动判断Mock/真实AI）
     * @param hotelId 酒店ID
     * @param reviews 该酒店的所有评论
     * @return 一句话摘要
     */
    public String generateHotelReviewSummary(Long hotelId, List<Review> reviews) {
        if (reviews.isEmpty()) {
            return String.format("酒店ID：%d 暂无用户评论", hotelId);
        }

        // 判断是否启用Mock模式
        if (aiConfig.isMockEnabled()) {
            log.info("使用Mock模式生成酒店{}评论摘要", hotelId);
            return generateMockSummary(hotelId, reviews);
        } else {
            log.info("使用真实AI接口生成酒店{}评论摘要", hotelId);
            return generateRealAiSummary(hotelId, reviews);
        }
    }

    /**
     * Mock模式：本地算法生成摘要（无API Key也能运行）
     */
    private String generateMockSummary(Long hotelId, List<Review> reviews) {
        // 1. 计算平均评分
        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // 2. 提取高频关键词（简单版：统计正面/负面词汇）
        String allContent = reviews.stream()
                .map(Review::getContent)
                .collect(Collectors.joining(" "));

        // 正面关键词
        long positiveCount = countKeywords(allContent, "好", "棒", "满意", "干净", "方便", "舒适", "推荐");
        // 负面关键词
        long negativeCount = countKeywords(allContent, "差", "脏", "慢", "贵", "吵", "不满意", "失望");

        // 3. 生成一句话摘要
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("酒店ID：%d 综合评分%.1f分，共%d条评论。",
                hotelId, avgRating, reviews.size()));

        if (positiveCount > negativeCount) {
            summary.append("用户评价以正面为主，多数反馈房间干净、服务周到、位置便利；");
        } else if (negativeCount > positiveCount) {
            summary.append("用户评价存在部分负面反馈，主要集中在隔音差、价格偏高、服务响应慢；");
        } else {
            summary.append("用户评价褒贬不一，整体体验中等；");
        }

        // 补充最新评论的核心观点
        Review latestReview = reviews.get(0); // 已按时间倒序，第一条是最新的
        summary.append(String.format("最新评论（%s）：%s",
                latestReview.getReviewer(), latestReview.getContent().substring(0, Math.min(20, latestReview.getContent().length())) + "..."));

        return summary.toString();
    }

    /**
     * 真实AI接口：预留扩展（对接讯飞/百度/OpenAI等）
     * 有API Key时可实现此方法
     */
    /**
     * 真实AI接口：讯飞星火大模型调用逻辑
     */
    private String generateRealAiSummary(Long hotelId, List<Review> reviews) {
        // 1. 拼接评论内容，构造AI提示词（Prompt）
        String reviewText = reviews.stream()
                .map(r -> String.format("评论人：%s，评分：%d，内容：%s",
                        r.getReviewer(), r.getRating(), r.getContent()))
                .collect(Collectors.joining("\n"));

        // 明确的摘要指令，让AI生成符合要求的一句话摘要
        String prompt = String.format(
                "请用一句话总结以下酒店（ID：%d）的用户评论核心观点，要求包含平均评分、整体评价倾向、关键反馈：\n%s",
                hotelId, reviewText
        );

        // 2. 构造讯飞星火接口请求参数
        JSONObject requestBody = new JSONObject();
        // 基础配置
        requestBody.put("app_id", XUNFEI_APP_ID);
        requestBody.put("temperature", 0.5); // 生成摘要的随机性（0-1，越小越稳定）
        requestBody.put("max_tokens", 200); // 摘要最大长度

        // 消息体（角色：user=用户提问，assistant=AI回答）
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        requestBody.put("messages", new JSONObject[]{message});

        // 3. 生成接口签名（讯飞要求的鉴权逻辑，其他平台可省略）
        String authorization = getXunfeiAuthorization();

        // 4. 调用讯飞星火API
        try (HttpResponse response = HttpRequest.post(XUNFEI_API_URL)
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("X-Appid", XUNFEI_APP_ID)
                .body(requestBody.toString())
                .timeout(10000) // 超时时间10秒
                .execute()) {

            // 5. 解析响应结果
            if (response.isOk()) {
                JSONObject responseJson = JSONUtil.parseObj(response.body());
                // 提取AI生成的摘要内容
                String summary = responseJson.getJSONObject("choices")
                        .getJSONArray("text")
                        .getStr(0)
                        .trim();
                return summary;
            } else {
                log.error("讯飞AI接口调用失败，响应码：{}，响应内容：{}",
                        response.getStatus(), response.body());
                // 降级：返回Mock摘要
                return generateMockSummary(hotelId, reviews);
            }
        } catch (Exception e) {
            log.error("讯飞AI接口调用异常", e);
            // 降级：返回Mock摘要
            return generateMockSummary(hotelId, reviews);
        }
    }

    /**
     * 辅助方法：生成讯飞星火接口的Authorization签名（讯飞专属鉴权）
     */
    private String getXunfeiAuthorization() {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String signatureOrigin = "host: spark-api.xf-yun.com\ndate: " + timestamp + "\nPOST /v3.5/chat/completions HTTP/1.1";

            // 修正：去掉 algorithm: 写法
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(XUNFEI_API_SECRET.getBytes(), "HmacSHA256"));
            byte[] signatureBytes = mac.doFinal(signatureOrigin.getBytes());
            String signature = Base64.getEncoder().encodeToString(signatureBytes);

            Map<String, String> authMap = new HashMap<>();
            authMap.put("api_key", aiConfig.getApiKey());
            authMap.put("algorithm", "hmac-sha256");
            authMap.put("headers", "host date request-line");
            authMap.put("signature", signature);

            // 修正：转义双引号，只需要 \\\"
            return "hmac " + JSONUtil.toJsonStr(authMap).replace("\"", "\\\"");
        } catch (Exception e) {
            log.error("生成讯飞签名失败", e);
            return "";
        }
    }

    /**
     * 辅助方法：统计关键词出现次数
     */
    private long countKeywords(String text, String... keywords) {
        return java.util.Arrays.stream(keywords)
                .mapToLong(keyword -> text.split(keyword).length - 1)
                .sum();
    }
}