#!/usr/bin/env python
# -*- conding:utf-8 -*-
from http.server import HTTPServer, BaseHTTPRequestHandler
import muggle_ocr
import re,time,base64,os

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
        data = '<title>xp_CAPTCHA</title><body style="text-align:center"><h1>验证码识别：xp_CAPTCHA</h1><a href="http://www.nmd5.com">author:算命縖子</a><p><TABLE style="BORDER-RIGHT: #ff6600 2px dotted; BORDER-TOP: #ff6600 2px dotted; BORDER-LEFT: #ff6600 2px dotted; BORDER-BOTTOM: #ff6600 2px dotted; BORDER-COLLAPSE: collapse" borderColor=#ff6600 height=40 cellPadding=1 align=center border=2><tr align=center><td>验证码</td><td>识别结果</td><td>时间</td></tr>%s</body>'%(content)
        self.send_response(200)
        self.send_header('Content-type', 'text/html; charset=UTF-8')
        self.end_headers()
        self.wfile.write(data.encode())

    def do_POST(self):
        #print(self.headers)
        #print(self.command)
        text = ''
        try:
            if self.path != '/base64':
                self.send_error(404, "Page not Found!")
                return

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

            with open('temp/log.txt', 'w') as f:
                f.write('<tr align=center><td><img src="data:image/png;base64,%s"/></td><td>%s</td><td>%s</td></tr>\n'%(base64_img.group(1),text,time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(int(img_name))))+ data)

            #删除掉图片文件，以防占用太大的内存
            os.remove("temp/%s.png"%img_name)
        except:
            text= '0000'
            print('\n识别失败！\n')
        
        if text =='':
            text= '0000'
            print('\n识别失败！\n')

        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.end_headers()
        self.wfile.write(text.encode('utf-8'))

if __name__ == '__main__':
    os.makedirs('temp', exist_ok=True)
    with open('temp/log.txt', 'w') as f:
        pass
    server = HTTPServer(host, Resquest)
    print("Starting server, listen at: %s:%s" % host)
    server.serve_forever()
