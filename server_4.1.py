#!/usr/bin/env python
# -*- conding:utf-8 -*-
from http.server import HTTPServer, BaseHTTPRequestHandler
import re,time,base64,os,requests

host = ('0.0.0.0', 8899)
count = 50 #保存多少个验证码及结果


class Resquest(BaseHTTPRequestHandler):
    def handler(self):
        print("data:", self.rfile.readline().decode())
        self.wfile.write(self.rfile.readline())

    def do_GET(self):
        print(self.requestline)
        if self.path != '/':
            self.send_error(404, "Page not Found!")
            return
        with open('temp/log.txt', 'r') as f:
            content = f.read()
        data = '<title>xp_CAPTCHA</title><body style="text-align:center"><h1>验证码识别：xp_CAPTCHA</h1><a href="http://www.nmd5.com">author:算命縖子</a><p><TABLE style="BORDER-RIGHT: #ff6600 2px dotted; BORDER-TOP: #ff6600 2px dotted; BORDER-LEFT: #ff6600 2px dotted; BORDER-BOTTOM: #ff6600 2px dotted; BORDER-COLLAPSE: collapse" borderColor=#ff6600 height=40 cellPadding=1 align=center border=2><tr align=center><td>验证码</td><td>识别结果</td><td>时间</td><td>验证码模块</td></tr>%s</body>'%(content)
        self.send_response(200)
        self.send_header('Content-type', 'text/html; charset=UTF-8')
        self.end_headers()
        self.wfile.write(data.encode())

    def do_POST(self):
        #print(self.headers)
        #print(self.command)
        text = ''
        types = '1'
        try:
            if self.path != '/base64' and self.path != '/imgurl':
                self.send_error(404, "Page not Found!")
                return

            if self.path == '/base64':
                img_name = time.time()
                req_datas = self.rfile.read(int(self.headers['content-length']))
                req_datas = req_datas.decode()
                base64_img = re.search('base64=(.*?)$',req_datas)
                #print(base64_img.group(1)) #post base64参数的内容

                with open("temp/%s.png"%img_name, 'wb') as f:
                    f.write(base64.b64decode(base64_img.group(1)))
                    f.close()

                #验证码识别
                sdk = muggle_ocr.SDK(model_type=muggle_ocr.ModelType.Captcha)
                with open(r"temp/%s.png"%img_name, "rb") as f:
                    b = f.read()
                text = sdk.predict(image_bytes=b)
                print('\n'+text+'\n') #识别的结果


            elif self.path == '/imgurl':
                img_name = time.time()
                req_datas = self.rfile.read(int(self.headers['content-length']))
                req_datas = req_datas.decode()
                text_data = re.search('url=(.*?)$', req_datas)
                text_data = text_data.group(1).split("&")

                url = base64.b64decode(text_data[0]).decode("utf-8")
                cookie = base64.b64decode(text_data[1][7:])[7:].decode("utf-8").strip(" ")
                types = text_data[2].strip("type=")

                try:
                    headers = {
                        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36",
                        "Cookie": cookie}
                    print("\n\n"+url)
                    #print(headers)
                    request = requests.get(url,headers=headers,timeout=3)
                    CAPTCHA = request.text# 获取图片
                    print("图片地址响应码：",request.status_code)

                    # 判断验证码数据包是否为json格式
                    if re.findall('"\s*:\s*.?"', CAPTCHA):
                        print("json格式")
                        CAPTCHA = CAPTCHA.split('"')
                        CAPTCHA.sort(key=lambda i: len(i), reverse=True)  # 按照字符串长度排序
                        CAPTCHA = CAPTCHA[0].split(',')
                        CAPTCHA.sort(key=lambda i: len(i), reverse=True)  # 按照字符串长度排序
                        CAPTCHA_base64 = CAPTCHA[0]
                    elif re.findall('data:image/\D*;base64,', CAPTCHA):
                        print("base64格式")
                        CAPTCHA = CAPTCHA.split(',')
                        CAPTCHA.sort(key=lambda i: len(i), reverse=True)  # 按照字符串长度排序
                        CAPTCHA_base64 = CAPTCHA[0]
                    else:
                        print("图片格式")
                        text_img = True

                    if text_img:
                        #图片格式直接保存
                        with open("temp/%s.png" % img_name, 'wb') as f:
                            f.write(request.content)
                            f.close()
                    else:
                        #base64需要解码保存
                        with open("temp/%s.png" % img_name, 'wb') as f:
                            f.write(base64.b64decode(CAPTCHA_base64))
                            f.close()

                except:
                    print("error:获取图片出错！")

            # 验证码识别
            if types == "1":
                sdk = muggle_ocr.SDK(model_type=muggle_ocr.ModelType.Captcha)
                with open(r"temp/%s.png" % img_name, "rb") as f:
                    b = f.read()
                text = sdk.predict(image_bytes=b)
                print('\n' + text + '\n')  # 识别的结果
            elif types == "2":
                with open(r"temp/%s.png" % img_name, "rb") as f:
                    img_bytes = f.read()
                text = ocr.classification(img_bytes)
                print('\n' + text + '\n')  # 识别的结果

            #保存最新count个的验证码及识别结果
            with open('temp/log.txt', 'r') as f:
                data = ""
                counts = 0
                content = f.read()
                pattern = re.compile(r'.*?\n')
                result1 = pattern.findall(content)
                for i in result1:
                    counts += 1
                    if counts >= count: break
                    data = data + i
            with open("temp/%s.png" % img_name, 'rb') as f:
                base64_img = base64.b64encode(f.read()).decode("utf-8")
            with open('temp/log.txt', 'w') as f:
                f.write('<tr align=center><td><img src="data:image/png;base64,%s"/></td><td>%s</td><td>%s</td><td>%s</td></tr>\n'%(base64_img,text,time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(int(img_name))),types)+ data)

            #删除掉图片文件，以防占用太大的内存
            os.remove("temp/%s.png"%img_name)
        except:
            text= '0000'
            print('\nerror:识别失败！\n')
        
        if text =='':
            text= '0000'
            print('\n识别失败！\n')

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(text.encode('utf-8'))

if __name__ == '__main__':
    print('正在加载中请稍后……')
    import muggle_ocr,ddddocr
    os.makedirs('temp', exist_ok=True)
    with open('temp/log.txt', 'w') as f:
        pass
    server = HTTPServer(host, Resquest)
    print("Starting server, listen at: %s:%s" % host)
    print('加载完成！请访问：http://127.0.0.1:%s' % host[1])
    print('github:https://github.com/smxiazi/NEW_xp_CAPTCHA\n\n')
    ocr = ddddocr.DdddOcr()
    server.serve_forever()