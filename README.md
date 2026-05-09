# 🎬pilipala-backend
基于 SpringBoot 的微服务在线视频平台后端系统
支持视频上传、转码处理、全文搜索、互动系统与后台管理

---

## 📖 项目简介
Video Platform Backend 是在线视频分享系统的后端服务，采用单体架构设计，实现了视频管理、用户互动、搜索推荐以及后台管理等完整业务流程。
系统结合 Redis 提升性能，结合 Elasticsearch 实现全文检索，并通过异步处理优化视频上传与转码流程。

---

## 🧩 核心功能模块

### 🎥 视频资源模块

- 视频分片上传
- 文件合并
- 视频转码
- HLS 切片生成
- 视频信息管理

技术亮点：

- 使用 Redis 实现异步任务处理
- 减少上传过程阻塞
- 提升系统吞吐能力

---

### 💬 互动模块

- 评论功能
- 点赞功能
- 投币功能
- 收藏功能
- 弹幕功能

互动数据存储于 MySQL，热点数据使用 Redis 缓存优化。

---

### 🔎 搜索模块

使用 Elasticsearch 实现：

- 视频标题全文搜索
- IK 分词优化中文搜索
- 相关度排序

结合 Redis 实现：

- 搜索热词统计
- 热门搜索推荐

---

### 🛠 后台管理模块

- 分类管理
- 视频审核
- 用户管理
- 系统参数配置

---

## 🛠 技术栈

### 后端核心
- **Spring Boot 2.x** - 主框架
- **MyBatis + XML Mapper** - ORM 持久层框架
- **Maven** - 项目构建工具

### 数据存储
- **MySQL 8.0+** - 关系型数据库
- **Redis** - 分布式缓存、Session 管理、消息队列
- **Elasticsearch 7.x/8.x** - 全文搜索引擎
- **HikariCP** - 高性能数据库连接池

### 视频/多媒体
- **FFmpeg** - 视频转码、HLS 切片、封面截图
- **HLS (M3U8 + TS)** - HTTP Live Streaming 流媒体协议
- **H.264 + AAC** - 标准化编码格式

### 安全认证
- **BCrypt** - 用户密码哈希加密
- **MD5** - 管理员密码加密
- **UUID Token + Redis Session** - 自定义分布式会话方案
- **Google Kaptcha** - 图形验证码生成

### 其他技术
- **Spring AOP** - 权限拦截、日志记录、消息通知
- **@Scheduled** - 定时任务（数据统计归档）
- **JSR-303 Validation** - 参数校验
- **Spring Data Redis (Jedis)** - Redis 操作封装

---

## ⚙ 运行环境

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 1.8+ | Java 运行环境 |
| Maven | 3.6+ | 项目构建工具 |
| MySQL | 8.0+ | 数据库（需支持 CTE 递归查询） |
| Redis | 5.0+ | 缓存服务 |
| Elasticsearch | 7.x 或 8.x | 搜索引擎 |
| FFmpeg | 4.0+ | 视频转码工具（需配置环境变量） |

---

## 🚀 快速开始

### 启动顺序

1. **启动 MySQL** - 导入数据库脚本
2. **启动 Redis** - 默认端口 6379
3. **启动 Elasticsearch** - 默认端口 9200
4. **启动转码服务**（如有独立进程）
5. **启动 pilipala-admin** - 后台管理（端口 7070）
6. **启动 pilipala-web** - 用户前台（端口 7071）

---

## ⚙ 配置说明

### 1) 修改数据库连接
在 `application.yml` 中修改：
```yaml
spring:
  datasource:
    url: jdbc:mysql://你的IP:3306/pilipala?useSSL=false&serverTimezone=UTC
    username: 你的用户名
    password: 你的密码
```

### 2) 修改 Redis 连接
```yaml
spring:
  redis:
    host: 你的Redis IP
    password: 你的密码
```

### 3) 修改 Elasticsearch 连接
```yaml
es:
  host: 你的ES IP
  port: 9200
```

### 4) 修改文件存储路径
```yaml
project:
  folder: "你的存储路径"
```
确保该目录存在且有读写权限，建议预留 **50GB+ 磁盘空间**。

---

## 🔐 默认管理员账号

| 账号 | 密码 |
|------|------|
| admin | admin123 |

**⚠️ 首次部署后请立即修改密码！**

---

## 📝 注意事项

1. **FFmpeg 必须安装并配置环境变量**，否则视频转码功能无法使用
2. **Elasticsearch 需要至少 2GB 内存**，建议单独部署
3. **Redis 不要设置密码验证** 或在配置文件中正确配置密码
4. **视频上传大小限制**：
   - 用户端默认 100MB（可通过 `spring.servlet.multipart.max-file-size` 调整）
   - 管理端默认 10MB
5. **定时任务**：每日凌晨自动执行数据统计归档，确保应用持续运行

---

# 🔐 安全设计

- Token 身份认证
- 登录拦截器
- 接口权限控制
- 文件类型校验
- 参数统一校验
- 后台审核机制

---

# 📈 项目亮点

- 实现视频分片上传与断点续传
- 使用 Redis 优化高频数据读写
- 构建 Elasticsearch 全文检索系统
- 使用异步处理解决视频转码耗时问题
- 构建完整创作者数据统计体系
- 设计统一异常处理与统一返回结构