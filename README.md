# xp_CAPTCHA(白嫖版)


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

![image](https://user-images.githubusercontent.com/30351807/115872316-3f4b6780-a474-11eb-8f25-a2de13274510.png)



运行 server.py 


![image](https://user-images.githubusercontent.com/30351807/115872339-470b0c00-a474-11eb-8339-109b82f464eb.png)


等待显示出 Starting server, listen at: 0.0.0.0:8899 访问 http://127.0.0.1:8899/ 显示下面界面即为正常。



![image](https://user-images.githubusercontent.com/30351807/115872365-4ffbdd80-a474-11eb-8be6-cd4150242d66.png)


linux 下安装可能会需要


![image](https://user-images.githubusercontent.com/30351807/115872401-58ecaf00-a474-11eb-9a1a-e933173585a7.png)


安装即可
```
yum install libglvnd-glx-1.0.1-0.8.git5baa1e5.el7.x86_64
```


### 验证码识别率


![image](https://user-images.githubusercontent.com/30351807/115872436-61dd8080-a474-11eb-8971-02c7144ff91e.png)



![image](https://user-images.githubusercontent.com/30351807/115872450-673acb00-a474-11eb-8865-a12383727615.png)



![image](https://user-images.githubusercontent.com/30351807/115872463-6ace5200-a474-11eb-9a8e-a93de9ba0f47.png)



![image](https://user-images.githubusercontent.com/30351807/115872476-702b9c80-a474-11eb-9d48-cdf2e02348d0.png)



![image](https://user-images.githubusercontent.com/30351807/115872496-73bf2380-a474-11eb-9d92-147c69e28452.png)



### 使用方法

把图片base64编码后POST发送至接口http://localhost:8899/base64 的base64参数即可，返回结果为识别的后的结果。


![image](https://user-images.githubusercontent.com/30351807/115872517-791c6e00-a474-11eb-89ad-307efa56d7f1.png)



![image](https://user-images.githubusercontent.com/30351807/115872532-7d488b80-a474-11eb-9886-74519894d224.png)



### burp联动识别验证码爆破

如果 server.py 在服务器上跑的话，xp_CAPTCHA.py需要修改对应的IP。


![image](https://user-images.githubusercontent.com/30351807/115872564-85083000-a474-11eb-85b1-98523a93a60e.png)


修改完后导入burp


![image](https://user-images.githubusercontent.com/30351807/115872601-918c8880-a474-11eb-9b43-6aa958a12172.png)



![image](https://user-images.githubusercontent.com/30351807/115872621-994c2d00-a474-11eb-8072-bbe22b2c8033.png)


Attack type处选择 Pitchfork,在http头部位置插入xiapao:验证码的URL地址


![image](https://user-images.githubusercontent.com/30351807/115872650-a10bd180-a474-11eb-9a34-b30a974d145d.png)


此处导入字典


![image](https://user-images.githubusercontent.com/30351807/115872672-a79a4900-a474-11eb-818f-a3e0c47cfb21.png)


选择验证码识别


![image](https://user-images.githubusercontent.com/30351807/115872696-ad902a00-a474-11eb-814e-305faaa20756.png)



![image](https://user-images.githubusercontent.com/30351807/115872713-b1bc4780-a474-11eb-8e03-df7eab39885f.png)


然后把线程设置为1


![image](https://user-images.githubusercontent.com/30351807/115872728-b5e86500-a474-11eb-8d4f-32344006ee36.png)



# blog
http://www.nmd5.com


