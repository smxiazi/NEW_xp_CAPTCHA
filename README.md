# NEW_xp_CAPTCHA


### 说明
xp_CAPTCHA (白嫖版)
* 验证码识别
* burp插件

### 安装

需要python3 小于3.7的版本

安装 `muggle_ocr` 模块（大概400M左右）
```
python3 -m pip install -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com muggle-ocr
```


[attachimg]118576[/attachimg]


运行 server.py 


[attachimg]118577[/attachimg]


等待显示出 Starting server, listen at: 0.0.0.0:8899 访问 http://127.0.0.1:8899/ 显示下面界面即为正常。



[attachimg]118578[/attachimg]


linux 下安装可能会需要


[attachimg]118584[/attachimg]


安装即可
```
yum install libglvnd-glx-1.0.1-0.8.git5baa1e5.el7.x86_64
```


### 验证码识别率


[attachimg]118579[/attachimg]



[attachimg]118580[/attachimg]



[attachimg]118581[/attachimg]



[attachimg]118582[/attachimg]



[attachimg]118583[/attachimg]



### 使用方法

把图片base64编码后POST发送至接口http://localhost:8899/base64 的base64参数即可，返回结果为识别的后的结果。


[attachimg]118585[/attachimg]



[attachimg]118586[/attachimg]



### burp联动识别验证码爆破

如果 server.py 在服务器上跑的话，xp_CAPTCHA.py需要修改对应的IP。


[attachimg]118588[/attachimg]


修改完后导入burp


[attachimg]118589[/attachimg]



[attachimg]118587[/attachimg]


Attack type处选择 Pitchfork,在http头部位置插入xiapao:验证码的URL地址


[attachimg]118590[/attachimg]


此处导入字典


[attachimg]118591[/attachimg]


选择验证码识别


[attachimg]118592[/attachimg]



[attachimg]118593[/attachimg]


然后把线程设置为1


[attachimg]118594[/attachimg]


效果如下：


[attachimg]118596[/attachimg]

### 试用

我自己搭建的（后面会关掉）
http://81.71.75.78:8899/

建议本地自己搭建来使用，或者自己搭建在自己的服务器上使用

[attach]118597[/attach]
