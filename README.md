# chat —— 小伙伴们的悄悄话

## 特点
* 基于Spring boot开发，轻量级，无需修改配置，安装极其简单
* 使用stomp协议通信,减轻服务端轮询压力
* 登录密码只存在服务器内存中，不持久化保存，且每次启动聊天都可自定义密码
* 聊天内容只在客户端显示，服务器上不保存，无监控更安全
* 系统可防固定会话攻击,单用户有且仅有一人在线(必须Logout后才可再登录)
* 全程https私密通信,防止中间人劫持,可自定义证书
* 支持PC,手机端视频聊天(chrome,uc测试通过),通过每秒12帧的速度截取视频中的图并以base64格式上传

## 效果
![pc端效果](https://raw.githubusercontent.com/sunpeak/chat/master/chat.png)
![手机端效果](https://raw.githubusercontent.com/sunpeak/chat/master/chat1.png)

## 部署
系统默认采用jar运行和打包，运行环境建议jdk 8。

### 运行
    https://raw.githubusercontent.com/sunpeak/chat/master/target/chat-1.0.0.RELEASE.jar,可以直接下载运行
    自定义用户和登录密码,用户密码对之间使用";"分隔,用户和密码使用":"分隔
	java -jar chat-1.0.0.RELEASE.jar --custom.user=aaa:111,bbb:222,ccc:333

### 生成证书
    keytool -genkey -alias chat -storetype PKCS12 -keyalg RSA -keysize 2048  -keystore keystore.p12 -validity 3650
    覆盖项目resource下同名证书,配置在application.perproties文件中

### 打包
    如需要二次开发,则打包命令
    mvn clean install

### 使用
    浏览器打开https://ip:8443

## TODO
* 音频聊天
* 视频缩放
