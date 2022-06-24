package burp;
import java.nio.charset.StandardCharsets;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.*;
import java.awt.event.ItemListener;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;


public class BurpExtender implements IBurpExtender, ITab, IHttpListener
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private JSplitPane splitPane;
    public PrintWriter stdout;
    int switchs = 1; //开关 0关 1开
    int clicks_Repeater=0;//64是监听 0是关闭
    int clicks_Intruder=0;//32是监听 0是关闭
    int xiapao_count = 0;//用于判断是第几个验证码
    String XiaPao_api_HOST = "127.0.0.1";
    int XiaPao_api_Port = 8899;
    String captcha_url_1;//验证码url
    String captcha_url_2;
    String captcha_url_3;
    String captcha_url_4;
    String captcha_url_5;
    String captcha_modular_1;//验证码模式
    String captcha_modular_2;
    String captcha_modular_3;
    String captcha_modular_4;
    String captcha_modular_5;
    JTextArea jta;//存放日志输入


    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        //输出
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stdout.println("hello xp_CAPTCHA!");
        this.stdout.println("你好 欢迎使用 瞎跑!");
        this.stdout.println("version:4.1");

        // keep a reference to our callbacks object
        this.callbacks = callbacks;

        // obtain an extension helpers object
        helpers = callbacks.getHelpers();

        // set our extension name
        callbacks.setExtensionName("xp_CAPTCHA V4.1");

        // create our UI
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {

                // main split pane
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                JSplitPane splitPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

                //左边框的内容
                JPanel jp=new JPanel();
                JLabel jl_0=new JLabel("    瞎跑接口HOST:Port：");
                JTextField txtfield_0=new JTextField("127.0.0.1:8899",1);
                JLabel jl_00=new JLabel("");
                jp.setLayout(new GridLayout(28, 1));
                JLabel jl_1=new JLabel("    验证码编号：1  关键字为：@xiapao@1@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_1=new JTextField(1);
                JLabel jl_2=new JLabel("");
                JLabel jl_3=new JLabel("    验证码编号：2  关键字为：@xiapao@2@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_2=new JTextField(1);
                JLabel jl_4=new JLabel("");
                JLabel jl_5=new JLabel("    验证码编号：3  关键字为：@xiapao@3@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_3=new JTextField(1);
                JLabel jl_6=new JLabel("");
                JLabel jl_7=new JLabel("    验证码编号：4  关键字为：@xiapao@4@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_4=new JTextField(1);
                JLabel jl_8=new JLabel("");
                JLabel jl_9=new JLabel("    验证码编号：5  关键字为：@xiapao@5@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_5=new JTextField(1);
                JLabel jl_10=new JLabel("");

                ButtonGroup group_1=new ButtonGroup();
                JRadioButton rb_url_1_1=new JRadioButton("验证码识别一（muggle_ocr）",true);
                JRadioButton rb_url_1_2=new JRadioButton("验证码识别二（ddddocr）");
                group_1.add(rb_url_1_1);
                group_1.add(rb_url_1_2);

                ButtonGroup group_2=new ButtonGroup();
                JRadioButton rb_url_2_1=new JRadioButton("验证码识别一（muggle_ocr）",true);
                JRadioButton rb_url_2_2=new JRadioButton("验证码识别二（ddddocr）");
                group_2.add(rb_url_2_1);
                group_2.add(rb_url_2_2);

                ButtonGroup group_3=new ButtonGroup();
                JRadioButton rb_url_3_1=new JRadioButton("验证码识别一（muggle_ocr）",true);
                JRadioButton rb_url_3_2=new JRadioButton("验证码识别二（ddddocr）");
                group_3.add(rb_url_3_1);
                group_3.add(rb_url_3_2);

                ButtonGroup group_4=new ButtonGroup();
                JRadioButton rb_url_4_1=new JRadioButton("验证码识别一（muggle_ocr）",true);
                JRadioButton rb_url_4_2=new JRadioButton("验证码识别二（ddddocr）");
                group_4.add(rb_url_4_1);
                group_4.add(rb_url_4_2);

                ButtonGroup group_5=new ButtonGroup();
                JRadioButton rb_url_5_1=new JRadioButton("验证码识别一（muggle_ocr）",true);
                JRadioButton rb_url_5_2=new JRadioButton("验证码识别二（ddddocr）");
                group_5.add(rb_url_5_1);
                group_5.add(rb_url_5_2);

                //添加到面板上
                jp.add(jl_0);
                jp.add(txtfield_0);
                jp.add(jl_00);

                jp.add(jl_1);
                jp.add(txtfield_1);
                jp.add(rb_url_1_1);
                jp.add(rb_url_1_2);
                jp.add(jl_2);

                jp.add(jl_3);
                jp.add(txtfield_2);
                jp.add(rb_url_2_1);
                jp.add(rb_url_2_2);
                jp.add(jl_4);

                jp.add(jl_5);
                jp.add(txtfield_3);
                jp.add(rb_url_3_1);
                jp.add(rb_url_3_2);
                jp.add(jl_6);

                jp.add(jl_7);
                jp.add(txtfield_4);
                jp.add(rb_url_4_1);
                jp.add(rb_url_4_2);
                jp.add(jl_8);

                jp.add(jl_9);
                jp.add(txtfield_5);
                jp.add(rb_url_5_1);
                jp.add(rb_url_5_2);
                jp.add(jl_10);

                //右边框上面的内容
                JPanel jps=new JPanel();
                jps.setLayout(new GridLayout(9, 1)); //六行一列
                JLabel jls=new JLabel("插件名：瞎跑 author：算命縖子");    //创建一个标签
                JLabel jls_1=new JLabel("blog:www.nmd5.com");
                JLabel jls_2=new JLabel("版本：xp_CAPTCHA V4.1");
                JLabel jls_3=new JLabel("感谢名单：小白(Assassins)");
                JCheckBox chkbox1=new JCheckBox("启动插件", true);//创建指定文本和状态的复选框
                JCheckBox chkbox2=new JCheckBox("监控Intruder");//创建指定文本的复选框
                JCheckBox chkbox3=new JCheckBox("监控Repeater");
                JButton btn1=new JButton("保存配置");
                JLabel jls_5=new JLabel("修改任何配置都记得点击保存");


                //右边框下面的内容
                JPanel jps_2=new JPanel();
                jta=new JTextArea(18,16);
                jta.setLineWrap(true);//自动换行
                jta.setEditable(false);//不可编辑
                JScrollPane jsp=new JScrollPane(jta);    //将文本域放入滚动窗口
                JButton btn2=new JButton("清空日志");
                JLabel jls_4=new JLabel("说明：在验证码处填写对应关键字即可");
                JLabel jls_6=new JLabel("备注：验证码返回包为json格式也支持");
                JLabel jls_7=new JLabel("注意：爆破时，线程记得设置成1！！！");
                jls_7.setForeground(Color.red);

                //添加复选框监听事件
                chkbox1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(chkbox1.isSelected()){
                            stdout.println("插件 瞎跑 启动");
                            switchs = 1;
                        }else {
                            stdout.println("插件 瞎跑 关闭");
                            switchs = 0;
                        }

                    }
                });
                chkbox2.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (chkbox2.isSelected()){
                            stdout.println("启动 监控Intruder");
                            clicks_Intruder = 32;
                        }else {
                            stdout.println("关闭 监控Intruder");
                            clicks_Intruder = 0;
                        }
                    }
                });
                chkbox3.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(chkbox3.isSelected()) {
                            stdout.println("启动 监控Repeater");
                            clicks_Repeater = 64;
                        }else {
                            stdout.println("关闭 监控Repeater");
                            clicks_Repeater = 0;
                        }
                    }
                });

                //加载按钮
                btn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        XiaPao_api_HOST = txtfield_0.getText().split(":")[0];
                        XiaPao_api_Port = Integer.parseInt(txtfield_0.getText().split(":")[1]);
                        captcha_url_1 = txtfield_1.getText();
                        captcha_url_2 = txtfield_2.getText();
                        captcha_url_3 = txtfield_3.getText();
                        captcha_url_4 = txtfield_4.getText();
                        captcha_url_5 = txtfield_5.getText();
                        stdout.println(XiaPao_api_HOST);
                        stdout.println(captcha_url_1);
                        stdout.println(captcha_url_2);
                        stdout.println(captcha_url_3);
                        stdout.println(captcha_url_4);
                        stdout.println(captcha_url_5);

                        if(rb_url_1_1.isSelected()){
                            captcha_modular_1 = "1";
                        }else {
                            captcha_modular_1 = "2";
                        }

                        if(rb_url_2_1.isSelected()){
                            captcha_modular_2 = "1";
                        }else {
                            captcha_modular_2 = "2";
                        }

                        if(rb_url_3_1.isSelected()){
                            captcha_modular_3 = "1";
                        }else {
                            captcha_modular_3 = "2";
                        }

                        if(rb_url_4_1.isSelected()){
                            captcha_modular_4 = "1";
                        }else {
                            captcha_modular_4 = "2";
                        }

                        if(rb_url_5_1.isSelected()){
                            captcha_modular_5 = "1";
                        }else {
                            captcha_modular_5 = "2";
                        }

                        jl_00.setText(XiaPao_api_HOST+":"+XiaPao_api_Port);
                        jl_00.setForeground(Color.red);
                        if(captcha_url_1.length()!=0){
                            jl_2.setText("======》验证码1    验证码模式："+captcha_modular_1+"    URL："+captcha_url_1);
                            jl_2.setForeground(Color.red);
                        }
                        if (captcha_url_2.length()!=0){
                            jl_4.setText("======》验证码2    验证码模式："+captcha_modular_2+"    URL："+captcha_url_2);
                            jl_4.setForeground(Color.red);
                        }
                        if (captcha_url_3.length()!=0){
                            jl_6.setText("======》验证码3    验证码模式："+captcha_modular_3+"    URL："+captcha_url_3);
                            jl_6.setForeground(Color.red);
                        }
                        if (captcha_url_4.length()!=0){
                            jl_8.setText("======》验证码4    验证码模式："+captcha_modular_4+"    URL："+captcha_url_4);
                            jl_8.setForeground(Color.red);
                        }
                        if (captcha_url_5.length()!=0){
                            jl_10.setText("======》验证码5    验证码模式："+captcha_modular_5+"    URL："+captcha_url_5);
                            jl_10.setForeground(Color.red);
                        }
                    }
                });

                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jta.setText("");
                    }
                });

                jps.add(jls);
                jps.add(jls_1);
                jps.add(jls_2);
                jps.add(jls_3);
                jps.add(chkbox1);
                jps.add(chkbox2);
                jps.add(chkbox3);
                jps.add(btn1);
                jps.add(jls_5);

                jps_2.add(jsp);
                jps_2.add(btn2);
                jps_2.add(jls_4);
                jps_2.add(jls_6);
                jps_2.add(jls_7);


                //右边
                splitPanes.setLeftComponent(jps);//上面
                splitPanes.setRightComponent(jps_2);//下面

                //整体分布
                splitPane.setLeftComponent(jp);//添加在左面
                splitPane.setRightComponent(splitPanes);//添加在右面
                splitPane.setDividerLocation(1000);//设置分割的大小

                // customize our UI components
                callbacks.customizeUiComponent(splitPane);
                callbacks.customizeUiComponent(jps);
                callbacks.customizeUiComponent(jp);
                callbacks.customizeUiComponent(jps_2);

                // add the custom tab to Burp's UI
                callbacks.addSuiteTab(BurpExtender.this);

                // register ourselves as an HTTP listener
                callbacks.registerHttpListener(BurpExtender.this);

            }
        });
    }

    @Override
    public String getTabCaption()
    {
        return "xia Pao";
    }

    @Override
    public Component getUiComponent()
    {
        return splitPane;
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {

        if(switchs == 1){//插件开关
            if(toolFlag == clicks_Repeater || toolFlag == clicks_Intruder){//监听Repeater、Intruder
                if(messageIsRequest){//请求包
                    String Request_data = helpers.bytesToString(messageInfo.getRequest());

                    if(Request_data.indexOf("@xiapao@") != -1){//判断请求包中是否带有特征

                        Pattern re_p = Pattern.compile("@xiapao@(\\d)@");
                        Matcher re_m = re_p.matcher(Request_data);
                        if (re_m.find()){
                            xiapao_count = Integer.parseInt(re_m.group(1));//验证码url编号
                            //stdout.println(xiapao_count);
                        }
                        //stdout.println(helpers.bytesToString(messageInfo.getRequest()));
                        checkVul(messageInfo,xiapao_count);
                    }
                }

            }
        }

    }


    private void checkVul(IHttpRequestResponse baseRequestResponse, int xiapao_count){
        String captcha_url;//验证码url
        String captcha_type;//验证码类型
        if (xiapao_count == 1){
            captcha_url = captcha_url_1;
            captcha_type =captcha_modular_1;
        }else if (xiapao_count == 2){
            captcha_url = captcha_url_2;
            captcha_type =captcha_modular_2;
        }else if (xiapao_count == 3){
            captcha_url = captcha_url_3;
            captcha_type =captcha_modular_3;
        }else if (xiapao_count == 4){
            captcha_url = captcha_url_4;
            captcha_type =captcha_modular_4;
        }else if (xiapao_count == 5){
            captcha_url = captcha_url_5;
            captcha_type =captcha_modular_5;
        }else {
            jta.insert("验证码"+xiapao_count+"：没该验证码编号！\n\n",0);//验证码结果输出到插件界面
            return;
        }

        
        
        List<String> headers = helpers.analyzeRequest(baseRequestResponse).getHeaders();
        String request = helpers.bytesToString(baseRequestResponse.getRequest());
        
        String cookies = "";
        for(String cookie:headers){//获取cookie
            if(cookie.indexOf("Cookie")!=-1){
                cookies = cookie;
            }
        }


        String captcha_cookie_base64 = "";
        String captcha_url_base64 = helpers.base64Encode(captcha_url);//验证码url base64编码

        if(captcha_url.length()<=1){
            jta.insert("验证码"+xiapao_count+"：还未填写对应的验证码地址哟！\n\n",0);//验证码结果输出到插件界面
            return;
        }

        if (cookies.length()>=1){//判断数据包中是否有cookie
            captcha_cookie_base64 = helpers.base64Encode(cookies);//Cookie base64编码
        }else {
            jta.insert("验证码"+xiapao_count+"：该数据包中没找到cookie，需要有cookie的数据包才支持验证码识别！\n\n",0);//验证码结果输出到插件界面
            return;
        }
        
        //对验证码接口发起请求
        List<String> Xiaopao_head = new ArrayList();;
        Xiaopao_head.add("POST /imgurl HTTP/1.1");
        Xiaopao_head.add("Host: "+XiaPao_api_HOST);
        String Xiapao_body="url="+captcha_url_base64+"&cookie="+captcha_cookie_base64+"&type="+captcha_type;

        IHttpService NEW_HttpService=helpers.buildHttpService(XiaPao_api_HOST,XiaPao_api_Port,"http");
        byte[] bodyByte = Xiapao_body.getBytes();
        byte[] new_Requests = helpers.buildHttpMessage(Xiaopao_head, bodyByte); //关键方法
        IHttpRequestResponse requestResponse = callbacks.makeHttpRequest(NEW_HttpService, new_Requests);//发送请求
        try{
            IResponseInfo requestinfo = helpers.analyzeResponse(requestResponse.getResponse());
            if (requestinfo.getStatusCode()!=200){
                jta.insert("error：瞎跑接口好像出问题了？\n\n",0);//验证码结果输出到插件界面
                return;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            jta.insert("error：瞎跑接口无应答，是不是配置错地址了？\n\n",0);//验证码结果输出到插件界面
            return;
        }

        String[] temp_1 = helpers.bytesToString(requestResponse.getResponse()).split("\\n");
        String captcha_data_ok = temp_1[temp_1.length-1].replace("\r","");//验证码识别结果
        BurpExtender.this.stdout.println(captcha_data_ok);
        jta.insert("验证码"+xiapao_count+"："+captcha_data_ok+"\n",0);//验证码结果输出到插件界面

        IRequestInfo analyIRequestInfo = helpers.analyzeRequest(baseRequestResponse);
        int bodyOffset = analyIRequestInfo.getBodyOffset();//通过上面的analyIRequestInfo得到请求数据包体（body）的起始偏移
        byte[] body = request.substring(bodyOffset).replaceAll("@xiapao@\\d@",captcha_data_ok).getBytes(StandardCharsets.UTF_8);//通过起始偏移点得到请求数据包体（body）的内容,然后替换

        //把验证码替换上，修改请求包
        for(int i=0;i<=headers.size()-1;i++){
            //BurpExtender.this.stdout.println(headers.get(i));
            if (headers.get(i).indexOf("@xiapao@") != -1){
                String captcha_data = headers.get(i).replaceAll("@xiapao@\\d@",captcha_data_ok);//正则替换
                //BurpExtender.this.stdout.println(captcha_data);
                headers.set(i,captcha_data);
            }
        }

        byte[] newRequest = helpers.buildHttpMessage(headers,body);
        baseRequestResponse.setRequest(newRequest);//设置最终新的请求包
    }



}
