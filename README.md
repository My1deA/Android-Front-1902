# 类似微博主页APP-手机端(Android-Front-1902/Projectzzz)


#### 介绍
> 手机端APP 实现图片/视频上传 定位 查看图片/播放视频 等等功能<br> 
[App配置各依赖环境详情可以参考这篇博客链接](https://blog.csdn.net/Process_ing/article/details/101626325)<br>
[后端项目Github链接](https://github.com/My1deA/Android-Back-1902)

* App主页
<img src='https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xOTExMzA1NC0zOTAxMzI2YmNlMWMwM2JjLmpwZw?x-oss-process=image/format,png' width='250' alt='App主页'>

 * 定位
 <img src='https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xOTExMzA1NC0wYWRiYzI3ZTJhM2I1Yjc3LmpwZw?x-oss-process=image/format,png' width='250' alt='定位'>
 
 * 上传图片/视频
  <img src='https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8xOTExMzA1NC03YjRkY2Y2NmY4OTM5YzY0LmpwZw?x-oss-process=image/format,png' width='250' alt='上传图片/视频'>


#### 软件架构
 后端开发环境：Eclipse + Mysql + Jsp<br>
 App端开发环境：Android Studio<br>

#### 安装教程

1. 将App端项目 download/clone 到 Android Studio中
2. 将后端项目 download/clone 到 Eclipse 中
3. 参考博客配置Mysql表 
3. 若使用局域网测试需要修改一下类中的上传url
```
LoginServletActivity 
UploadActivity
UploadVidioActivity
RegisterServletActivity
AppHomeFragment
AppFindFragment3
private final static String Url="http://本机IP地址:8080/MyWebTest/queryServlet";

```
4. 将App项目安装到手机上
5. 关闭win10防火墙 并开启win10热点 并使用手机wifi连接


#### 使用说明

1. 电脑运行Eclipse
2. 手机运行App

#### 参与贡献

1. 设计App界面
2. 参与设计后端架构
3. 实现前后端数据交互
4. 整合Badui地图定位API
5. 参与讨论 以及 整合 主页九宫格图片 图片查看/视频播放 模板

