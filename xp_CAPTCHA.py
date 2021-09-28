#!/usr/bin/env python
#coding:gbk
from __future__ import print_function
from burp import IBurpExtender
from burp import IIntruderPayloadGeneratorFactory
from burp import IIntruderPayloadGenerator
import base64
import json
import re
import urllib2
import ssl

host = ('127.0.0.1', 8899)

class BurpExtender(IBurpExtender, IIntruderPayloadGeneratorFactory):
    def registerExtenderCallbacks(self, callbacks):
        #注册payload生成器
        callbacks.registerIntruderPayloadGeneratorFactory(self)
        #插件里面显示的名字
        callbacks.setExtensionName("xp_CAPTCHA")
        print('xp_CAPTCHA  中文名:瞎跑验证码\nblog：http://www.nmd5.com/\nT00ls：https://www.t00ls.net/ \nThe loner安全团队 author:算命縖子\n\n用法：\n在head头部添加xiapao:验证码的URL\n\n如：\n\nPOST /login HTTP/1.1\nHost: www.baidu.com\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0\nAccept: text/plain, */*; q=0.01\nAccept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\nContent-Type: application/x-www-form-urlencoded; charset=UTF-8\nX-Requested-With: XMLHttpRequest\nxiapao:http://www.baidu.com/get-validate-code\nContent-Length: 84\nConnection: close\nCookie: JSESSIONID=24D59677C5EDF0ED7AFAB8566DC366F0\n\nusername=admin&password=admin&vcode=8888\n\n')

    def getGeneratorName(self):
        return "xp_CAPTCHA"

    def createNewInstance(self, attack):
        return xp_CAPTCHA(attack)

class xp_CAPTCHA(IIntruderPayloadGenerator):
    def __init__(self, attack):
        tem = "".join(chr(abs(x)) for x in attack.getRequestTemplate()) #request内容
        cookie = re.findall("Cookie: (.+?)\r\n", tem)[0] #获取cookie
        xp_CAPTCHA = re.findall("xiapao:(.+?)\r\n", tem)[0]
        ssl._create_default_https_context = ssl._create_unverified_context #忽略证书，防止证书报错
        print xp_CAPTCHA+'\n'
        print 'cookie:' + cookie+'\n'
        self.xp_CAPTCHA = xp_CAPTCHA
        self.cookie = cookie
        self.max = 1 #payload最大使用次数
        self.num = 0 #标记payload的使用次数
        self.attack = attack

    def hasMorePayloads(self):
        #如果payload使用到了最大次数reset就清0
        if self.num == self.max:
            return False  # 当达到最大次数的时候就调用reset
        else:
            return True

    def getNextPayload(self, payload):  # 这个函数请看下文解释
        xp_CAPTCHA_url = self.xp_CAPTCHA #验证码url

        print xp_CAPTCHA_url
        headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36","Cookie":self.cookie}
        request = urllib2.Request(xp_CAPTCHA_url,headers=headers)
        CAPTCHA = urllib2.urlopen(request) #获取图片
        CAPTCHA_base64 = base64.b64encode(CAPTCHA.read()) #把图片base64编码

        request = urllib2.Request('http://%s:%s/base64'%host, 'base64='+CAPTCHA_base64)
        response = urllib2.urlopen(request).read()
        print(response)
        return response

    def reset(self):
        self.num = 0  # 清零
        return
