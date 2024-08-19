# xp_CAPTCHA V4.3(瞎跑-白嫖版)

* 若要准确率更高，请用 https://github.com/smxiazi/xp_CAPTCHA （这个调用别人商业成熟的验证码识别接口）

## 注意
* 默认使用jdk1.8编译
* 在最新版的burp2.x中jdk为1x,会导致插件不可用,,使用jdk8编译到不行，请下载jdk16版本试试,若还不行，请自行下载源码使用当前电脑的jdk1x进行编译,谢谢。
* 爆破时，记得把线程设置为1。

**********
### 更新4.3 2024-8-19
* 新增支持验证码指定输出格式，如：纯整数0-9、纯小写英文a-z、小写英文a-z + 大写英文A-Z + 整数0-9等
* 新增支持自定义数据包，如发送POST数据包等
* 新增支持高级模式5个验证码均可同时用（之前只能一个验证码使用高级模式）
* 新增支持监控Proxy的流量
* 弃用 muggle-ocr 模块，只是使用ddddocr，模块安装更简单方便
* 使用方法和之前一样，拉到下面查看使用方法
* 注意，请更新ddddocr模块到最新版本
```
未安装ddddocr模块
python3 -m pip install ddddocr
已经安装过的请更新模块
python3 -m pip install -U ddddocr
```

<img width="1720" alt="image" src="https://github.com/user-attachments/assets/719d66f3-6469-4985-b612-832ac43e9170">


<img width="723" alt="image" src="https://github.com/user-attachments/assets/e47a2884-3127-4cd2-ae27-375a7f6c4530">

<img width="1042" alt="image" src="https://github.com/user-attachments/assets/8c52f888-f238-4ab5-a579-3c5009d0dffb">




**********

### 更新4.2 2022-9-5

* 新增支持从验证码返回包中获取内容填充到请求包中（该功能可以支持uuid验证码格式以及其他类似模式）
* 如果下载我打包好的环境的，请替换掉对应的server.py脚本，本次更新有涉及到该脚本的更新。

<img width="1404" alt="image" src="https://user-images.githubusercontent.com/30351807/188346185-917da8a5-62a1-4095-97ce-ff9896b558ea.png">


*********

### 说明
xp_CAPTCHA (白嫖版)
* 验证码识别
* burp插件
* 支持验证码返回包为json格式

*********
### 使用
* 用python3 启动server.py服务
* 把对应的关键字写在填写验证码处即可

<img width="1255" alt="image" src="https://user-images.githubusercontent.com/30351807/175557148-b6b7fa0f-5f7a-49fd-9a2a-4c51eda7e0bf.png">

<img width="1250" alt="image" src="https://user-images.githubusercontent.com/30351807/175553590-9702d872-26a3-4b87-8b3c-0b6a6831e4ab.png">

<img width="1204" alt="image" src="https://user-images.githubusercontent.com/30351807/175553779-103f90f5-2283-4022-ad32-87eee8443086.png">

<img width="1214" alt="image" src="https://user-images.githubusercontent.com/30351807/175557360-8a4b976c-6e73-42f0-9053-d087c3bbc442.png">

<img width="665" alt="image" src="https://user-images.githubusercontent.com/30351807/175557566-66e46ff9-4bcc-40de-82ad-fed3ff8bc37d.png">

### 高级设置使用

* 验证码响应体模式

每次获取验证码，他返回包为json格式，其中包含了一个字段为uuid，每次登录时都会要求账户密码验证码和uuid一起请求。
<img width="1397" alt="image" src="https://user-images.githubusercontent.com/30351807/188490525-c69cabf9-68aa-4c75-874a-1fb22ea37ebc.png">

正常使用正则即可
<img width="1418" alt="image" src="https://user-images.githubusercontent.com/30351807/188490926-fe351131-f1f9-4a25-8612-3dc5d7b5180d.png">

<img width="1392" alt="image" src="https://user-images.githubusercontent.com/30351807/188491180-c6da6d0b-fbc5-4094-b35c-7d118755b7fd.png">




* 验证码响应头模式

该站每次请求获取验证码，都会重新设置cookie。
<img width="1410" alt="image" src="https://user-images.githubusercontent.com/30351807/188486933-e086f07a-2aaa-49e5-832a-8d83d4abcb63.png">

响应头部的正则参数语法：
响应头对应的参数名|对应参数值的正则匹配。如我这个站是从 `Set-Cookie`里面获取`kaptchaId`的值，那么正则为`Set-Cookie|kaptchaId=(.*?);`
<img width="1435" alt="image" src="https://user-images.githubusercontent.com/30351807/188487200-468ff302-977b-4ea5-96f8-2fdca126261d.png">

<img width="1413" alt="image" src="https://user-images.githubusercontent.com/30351807/188487344-91b40601-d704-484f-a148-e8241647d15e.png">


### Star

[![Stargazers over time](https://starchart.cc/smxiazi/NEW_xp_CAPTCHA.svg)](https://starchart.cc/smxiazi/NEW_xp_CAPTCHA)

**********

### 更新4.1 2022-6-24

* 大改版，强烈安装新版本
