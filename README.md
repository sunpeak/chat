# chat —— 简单安全的聊天

## 特点
* 基于Spring boot开发，轻量级，无需修改配置，安装极其简单
* 登录密码只存在服务器内存中，不持久化保存，且每次启动聊天都可自定义密码
* 聊天内容只在客户端显示，服务器上不保存，无监控更安全

## 效果
![效果图](https://raw.githubusercontent.com/sunpeak/chat/master/chat.png)

## 部署
系统默认采用jar打包和运行，建议jdk 8。
### 打包
    mvn clean install
### 运行
    自定义聊天的用户和登录密码,用户密码对使用";"间隔,用户和密码使用":"间隔
	java -jar chat-*.jar --custom.user=aaa:111;bbb:222;ccc:333

## TODO
* 图片，视频，表情包聊天
