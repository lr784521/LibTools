# 基础工具``SDK``

[TOC]

### 1.目录说明

```java
├── app  测试工程
|   |
├── lib_tools  工具SDK工程
|	|── listener  工具回调
|	|── util  所有工具类
|   |   |── bank  银行卡    
|   |   |── bitmap  常见Bitmap转换与Bitmap图片
|   |   |── database  本地数据存储
|   |   |── encryption  加解密
|   |   |── event  EventBus数据发送
|   |   |── fileUtils  文件操作
|   |   |── language  国际化
|   |   |── number  数据处理
|   |   |── qr  二维码
|   |   |── screen  屏幕
|   |   |── time  时间
|   |   |── toast  Toast提示
|   |   |── AppGlobalUtils  反射全局Application
|   |   |── AppManager  Activity管理
|   |   |── AppUtils  应用信息获取
|   |   |── CacheUtil  缓存操作
|   |   |── ClipboardUtils  剪切板
|   |   |── CountDownTimerUtils  验证码倒计时
|   |   |── DoubleClickExitDetector  双击退出
|   |   |── EditTextUtil  输入框工具
|   |   |── GetDeviceId  设备唯一ID
|   |   |── GsonUtil  Json转换
|   |   |── IdcardValidator  身份证
|   |   |── LiveDataBus  跨组件通讯
|   |   |── MapUtil  Map数据操作
|   |   |── NoticeMessageUtils  通知栏
|   |   |── PermissionsUtil  动态权限请求
|   |   |── PhotoUtil  设备功能调用
|   |   |── SendMessage  短信发送
|   |   |── SoftKeyboardUtil  软键盘
|   |   |── StringUtil  常见字符串处理
|   |   |── TextUtil  TextView操作
|   |   |── ThreadUtils  线程操作
```

#### 2.使用方式

###### 2.1 在项目跟``build.gradle``添加如下配置

```java
allprojects {
    repositories {
        ...
        maven {
            allowInsecureProtocol = true
            url 'http://nexus.ops.hyszsz.com/repository/android-plugin-public/'
            credentials {
                username "lurui"
                password "lr123456"
            }
        }
    }
}
```

###### 2.2 在``app``工程``build.gradle``添加依赖

```
dependencies {
	...
    //远程仓库使用方式
    implementation 'com.hysz.lib_tools:lib_tools:版本号-SNAPSHOT/RELEASE'
}
```

#### 3.版本说明

​	发布版(RELEASE)：暂未发布

​	快照版(SNAPSHOT)：0.0.2

