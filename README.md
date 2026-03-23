# 后端开发 Take-Home 测试

> **时间**: 约 2 小时 | **提交**: Fork 后提交 GitHub 链接

---

## 题目

构建一个酒店评论 API 服务，包含一个 AI 摘要功能。

项目已配好 **H2 内存数据库**和**种子数据**（5 家酒店、40 条评论），`mvn spring-boot:run` 启动即可用。

---

## 启动

```bash
./mvnw spring-boot:run
```

H2 控制台: `http://localhost:8080/h2-console`（JDBC URL: `jdbc:h2:mem:takehome`，用户名 `sa`，无密码）

---

## 已提供

- `pom.xml` — Spring Boot 3.2.5 + JPA + H2 + Lombok
- `application.yml` — 数据库配置（JPA 自动建表 + 自动加载 data.sql）
- `data.sql` — 种子数据

其他的全部由你来写。

---

## 要求

### 数据模型

请根据 `data.sql` 中的数据结构自行设计 Entity。

### API 至少实现：

- 酒店的查询
- 评论的查询、新增
- **一个 AI 端点**：对评论内容生成一句话摘要

### AI 集成：

- 必须支持 **mock 模式**（没有 API Key 也能正常运行）

---

## 提交时请在这里补充

### 如何启动
IDEA 直接启动
1.将项目导入 IDEA，等待 Maven 自动下载依赖；
2.找到启动类 com.wtk.takehome.TakehomeApplication.java；
3.右键 → Run 'TakehomeApplication'，控制台输出 Started TakehomeApplication in X seconds 即启动成功。
### AI 部分的设计思路
1.必须支持 Mock 模式（无 API Key 也能运行）；
2.可无缝切换到真实 AI 接口；
3.摘要逻辑贴合业务场景（酒店评论）；


### 如果有更多时间会怎么改进
增加摘要风格与多语言支持，支持按维度筛选总结；加入评论与 AI 摘要缓存，异步调用提升接口响应；抽象 AI 服务接口，支持动态切换厂商并优化提示词；完善异常处理与统一返回格式。同时接入日志监控，补充单元测试与接口文档，提升系统稳定性与可维护性，让摘要更精准、接口更健壮

---

允许使用 AI 辅助编码，面试时会聊代码细节。
