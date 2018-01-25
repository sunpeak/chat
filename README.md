# chat —— 小伙伴们的悄悄话

## 特点
* 基于Spring boot开发，轻量级，无需修改配置，安装极其简单
* 登录密码只存在服务器内存中，不持久化保存，且每次启动聊天都可自定义密码
* 聊天内容只在客户端显示，服务器上不保存，无监控更安全
* 系统可防固定会话攻击,单用户有且仅有一人在线(必须logout后才可再登录)

## 效果
![效果图](https://raw.githubusercontent.com/sunpeak/chat/master/chat.png)

## 部署
系统默认采用jar运行和打包，运行环境建议jdk 8。

### 运行
    https://raw.githubusercontent.com/sunpeak/chat/master/target/chat-0.0.1-SNAPSHOT.jar,可以直接下载运行
    自定义用户和登录密码,用户密码对之间使用";"分隔,用户和密码使用":"分隔
	java -jar chat-0.0.1-SNAPSHOT.jar --"custom.user=aaa:111;bbb:222;ccc:333"

### 打包
    如需要二次开发,则打包命令
    mvn clean install

## TODO
* 图片，视频，表情包聊天
