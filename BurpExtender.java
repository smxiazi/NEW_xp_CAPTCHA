package burp;
import java.nio.charset.StandardCharsets;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;


public class BurpExtender implements IBurpExtender, ITab, IHttpListener
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private JSplitPane splitPane;
    public PrintWriter stdout;
    int switchs = 1; //开关 0关 1开
    int clicks_Repeater=0;//64是监听 0是关闭
    int clicks_Intruder=0;//32是监听 0是关闭
    int clicks_Proxy=0;//4是监听 0是关闭
    int xiapao_count = 0;//用于判断是第几个验证码
    String XiaPao_api_HOST = "127.0.0.1";
    int XiaPao_api_Port = 8899;
    JLabel jl_1;
    Map<Integer, List<String>> yzm_set_map = new HashMap<>();// 创建一个HashMap对象,用来存储用户填写的url信息等
    String captcha_url_1;//验证码url
    String captcha_url_2;
    String captcha_url_3;
    String captcha_url_4;
    String captcha_url_5;
    String captcha_modular_1;//验证码模式
    JTextArea jta;//存放日志输入
    JTextField jps_txtfield_1;//高级模式re正则内容
    JComboBox jb_1;//高级模式下 数据来源
    Boolean re_switch = false;//高级模式 启动/关闭
    String xp_version = "4.3";


    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        //输出
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stdout.println("hello xp_CAPTCHA!");
        this.stdout.println("你好 欢迎使用 瞎跑!");
        this.stdout.println("version:"+xp_version);

        // keep a reference to our callbacks object
        this.callbacks = callbacks;

        // obtain an extension helpers object
        helpers = callbacks.getHelpers();

        // set our extension name
        callbacks.setExtensionName("xp_CAPTCHA V"+xp_version);

        // create our UI
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {

                // main split pane
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                JSplitPane splitPanes = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                JSplitPane splitPanes2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

                //左边框的内容
                JPanel jp=new JPanel();
                JLabel jl_0=new JLabel("    瞎跑接口HOST:Port：");
                JTextField txtfield_0=new JTextField("127.0.0.1:8899",1);
                JLabel jl_00=new JLabel("");
                jp.setLayout(new GridLayout(16, 1));
                JComboBox jb_set = new  JComboBox();    //创建JComboBox
                jb_set.addItem("验证码编号：1");
                jb_set.addItem("验证码编号：2");
                jb_set.addItem("验证码编号：3");
                jb_set.addItem("验证码编号：4");
                jb_set.addItem("验证码编号：5");
                JLabel jl_01=new JLabel("");
                jl_1=new JLabel("    验证码编号：1  关键字为：@xiapao@1@    请在下列框中填写对应的验证码URL地址");
                JTextField txtfield_1=new JTextField(1);
                JLabel jl_2=new JLabel("");

                ButtonGroup group_1=new ButtonGroup();
                JRadioButton rb_url_1_1=new JRadioButton("普通模式（GET请求）",true);
                JRadioButton rb_url_1_2=new JRadioButton("复杂模式（自定义请求体，URL地址也要填写）");
                group_1.add(rb_url_1_1);
                group_1.add(rb_url_1_2);

                JComboBox jb_set_case_sensitive = new  JComboBox();    //创建JComboBox
                jb_set_case_sensitive.addItem("纯整数0-9");
                jb_set_case_sensitive.addItem("纯小写英文a-z");
                jb_set_case_sensitive.addItem("纯大写英文A-Z");
                jb_set_case_sensitive.addItem("小写英文a-z + 大写英文A-Z");
                jb_set_case_sensitive.addItem("小写英文a-z + 整数0-9");
                jb_set_case_sensitive.addItem("大写英文A-Z + 整数0-9");
                jb_set_case_sensitive.addItem("小写英文a-z + 大写英文A-Z + 整数0-9");
                jb_set_case_sensitive.addItem("默认字符库 - 小写英文a-z - 大写英文A-Z - 整数0-9");
                jb_set_case_sensitive.addItem("不识别验证码，配合高级模块使用（适合验证码直接在响应包中）");
                jb_set_case_sensitive.setSelectedIndex(6);


                JLabel jl_url_1=new JLabel("验证码编号1：url未设置");
                JLabel jl_url_2=new JLabel("验证码编号2：url未设置");
                JLabel jl_url_3=new JLabel("验证码编号3：url未设置");
                JLabel jl_url_4=new JLabel("验证码编号4：url未设置");
                JLabel jl_url_5=new JLabel("验证码编号5：url未设置");




                //左边
                jp.add(jl_0);
                jp.add(txtfield_0);
                jp.add(jl_00);

                jp.add(jb_set);
                jp.add(jl_01);

                jp.add(jl_1);
                jp.add(txtfield_1);
                jp.add(rb_url_1_1);
                jp.add(rb_url_1_2);
                jp.add(jb_set_case_sensitive);
                jp.add(jl_2);

                jp.add(jl_url_1);
                jp.add(jl_url_2);
                jp.add(jl_url_3);
                jp.add(jl_url_4);
                jp.add(jl_url_5);

                //左边框 下面

                JSplitPane splitPanes_zx = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

                //左边框 下面 - 左
                JPanel jps_xz1 =new JPanel();
                jps_xz1.setLayout(new GridLayout(1, 1)); //六行一列
                String dome_ReQuest = "POST /captcha HTTP/1.1\n" +
                        "Host: www.baidu.com\n" +
                        "Cookie: baidu=1111;\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:127.0) Gecko/20100101 Firefox/127.0\n" +
                        "Accept: text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01\n" +
                        "Sec-Fetch-Site: same-origin\n" +
                        "Te: trailers\n" +
                        "Connection: close\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Content-Length: 0\n" +
                        "\n" +
                        "aa=111";
                JTextArea jta_1=new JTextArea(dome_ReQuest);

                //jta_1.setLineWrap(true);//自动换行
                jta_1.setEditable(false);//不可编辑
                jta_1.setBackground(Color.LIGHT_GRAY);    //设置背景色
                JScrollPane jsp_zx_1=new JScrollPane(jta_1);    //将文本域放入滚动窗口
                jps_xz1.add(jsp_zx_1);


                //左边框 下面 - 右
                JPanel jps_3=new JPanel();
                jps_3.setLayout(new GridLayout(15, 1)); //六行一列
                JLabel jps_2_0=new JLabel("【高级模式 验证码 1 设置】修改记得点击保存设置");    //创建一个标签
                JLabel jps_2_1=new JLabel("数据来源：");    //创建一个标签
                jb_1 = new  JComboBox();    //创建JComboBox
                jb_1.addItem("验证码响应体");    //向下拉列表中添加一项
                jb_1.addItem("验证码响应头");


                JLabel jps_2_3=new JLabel("正则：");    //创建一个标签
                jps_txtfield_1=new JTextField("\"uuid\":\"(.*?)\"",1);

                JLabel jps_2_2=new JLabel("修改的数据包（关键字：@xiapao@x@）：");    //创建一个标签
                JComboBox jb_2 = new  JComboBox();    //创建JComboBox
                jb_2.addItem("要爆破的请求包");    //向下拉列表中添加一项
                JButton jps_2_bt_1 =new JButton("开启");
                JLabel jps_2_4=new JLabel("");//用来提示用的
                jps_2_4.setForeground(Color.red);

                //高级模式开启按钮
                jps_2_bt_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jps_2_4.setText("请点击 保存配置 才生效");
                        if(re_switch == false){
                            re_switch = true;
                            jps_2_bt_1.setText("关闭");
                            jps_txtfield_1.setEditable(false);
                            jps_txtfield_1.setBackground(Color.LIGHT_GRAY);//设置背景色
                            jb_1.setEditable(true);
                            jb_2.setEditable(true);
                        }else {
                            re_switch = false;
                            jps_2_bt_1.setText("开启");
                            jps_txtfield_1.setEditable(true);
                            jps_txtfield_1.setBackground(Color.WHITE);//设置背景色
                            jb_1.setEditable(false);
                            jb_2.setEditable(false);

                        }
                    }
                });

                //高级模式 数据来源下拉框
                jb_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(jb_1.getSelectedIndex()==1){
                            jps_txtfield_1.setText("Set-Cookie|SESSION=(.*?);");
                            jta.insert("验证码响应头正则格式为：响应头参数名|响应头对应的参数值的正则。如：Set-Cookie|SESSION=(.*?);\n\n",0);
                        }else {
                            jps_txtfield_1.setText("\"uuid\":\"(.*?)\"");
                        }
                    }
                });

                //验证码选择下拉框事件
                jb_set.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 当用户从下拉列表中选择一个项目时，会触发这个事件
                        // 获取选中的项目
                        String selectedItem = (String) jb_set.getSelectedItem();
                        //选择的第几个
                        int selectindex = jb_set.getSelectedIndex()+1;
                        // 显示选中的项目
                        jl_1.setText("    "+selectedItem+"  关键字为：@xiapao@"+String.valueOf(selectindex)+"@    请在下列框中填写对应的验证码URL地址");
                        jps_2_0.setText("【高级模式 验证码 "+String.valueOf(selectindex)+" 设置】修改记得点击保存设置");    //创建一个标签

                        //判断key存不存在
                        if (!yzm_set_map.containsKey(selectindex)) {
                            txtfield_1.setText("");
                            jb_set_case_sensitive.setSelectedIndex(6);
                            rb_url_1_1.setSelected(true);
                            jl_2.setText("");
                            jta_1.setText(dome_ReQuest);
                            jb_1.setSelectedIndex(0);
                            jps_txtfield_1.setText("\"uuid\":\"(.*?)\"");
                            jps_txtfield_1.setEditable(true);
                            jps_txtfield_1.setBackground(Color.WHITE);//设置背景色
                            jps_2_bt_1.setText("开启");
                            jl_0.setText("    瞎跑接口HOST:Port：");
                            jb_1.setEditable(false);
                            jb_2.setEditable(false);

                        }else{//存在
                            jl_2.setText("");
                            List<String> yzm_set_data = yzm_set_map.get(selectindex);
                            txtfield_1.setText(yzm_set_data.get(0));
                            if(yzm_set_data.get(1).contains("1")){
                                rb_url_1_1.setSelected(true);
                            }else {
                                rb_url_1_2.setSelected(true);
                            }
                            jb_set_case_sensitive.setSelectedIndex(Integer.valueOf(yzm_set_data.get(2)));
                            jta_1.setText(yzm_set_data.get(3));
                            jb_1.setSelectedIndex(Integer.valueOf(yzm_set_data.get(4)));
                            jps_txtfield_1.setText(yzm_set_data.get(5));
                            if (yzm_set_data.get(6).equals("true")){
                                jps_2_bt_1.setText("关闭");
                            }else{
                                jps_2_bt_1.setText("开启");
                            }
                            if(re_switch == false){
                                jps_txtfield_1.setEditable(true);
                                jps_txtfield_1.setBackground(Color.WHITE);//设置背景色
                                jb_1.setEditable(false);
                                jb_2.setEditable(false);
                            }else {
                                jps_txtfield_1.setEditable(false);
                                jps_txtfield_1.setBackground(Color.LIGHT_GRAY);//设置背景色
                                jb_1.setEditable(true);
                                jb_2.setEditable(true);
                            }
                        }
                        
                    }
                });



                //模式选择 - 单选按钮事件监听
                rb_url_1_1.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // 当单选按钮被选中时执行的操作
                            jta_1.setEditable(false);//不可编辑
                            jta_1.setBackground(Color.LIGHT_GRAY);    //设置背景色
                        }
                    }
                });
                rb_url_1_2.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // 当单选按钮被选中时执行的操作
                            jta_1.setEditable(true);//不可编辑
                            jta_1.setBackground(Color.WHITE);    //设置背景色
                        }
                    }
                });

                splitPanes_zx.setLeftComponent(jps_xz1);//左边
                splitPanes_zx.setRightComponent(jps_3);//右边



                //右边框上面的内容1
                JPanel jps=new JPanel();
                jps.setLayout(new GridLayout(14, 1)); //六行一列
                JLabel jls=new JLabel("插件名：瞎跑 author：算命縖子");    //创建一个标签
                JLabel jls_1=new JLabel("blog:www.nmd5.com");
                JLabel jls_2=new JLabel("版本：xp_CAPTCHA V"+xp_version);
                JLabel jls_3=new JLabel("感谢名单：小白(Assassins)");
                JCheckBox chkbox1=new JCheckBox("启动插件", true);//创建指定文本和状态的复选框
                JCheckBox chkbox2=new JCheckBox("监控Intruder");//创建指定文本的复选框
                JCheckBox chkbox3=new JCheckBox("监控Repeater");
                JCheckBox chkbox4=new JCheckBox("监控Proxy");
                JButton btn1=new JButton("保存配置");
                JLabel jls_5=new JLabel("修改任何配置都记得点击保存");


                JLabel jls_4=new JLabel("说明：在验证码处填写对应关键字即可");
                JLabel jls_6=new JLabel("备注：验证码返回包为json格式也支持");
                JLabel jls_7=new JLabel("注意：爆破时，线程记得设置成1");
                jls_7.setForeground(Color.red);
                JButton btn2=new JButton("清空日志");







                //右边框下面的内容
                JPanel jps_2=new JPanel();
                jps_2.setLayout(new GridLayout(1, 1));
                jta=new JTextArea(18,16);
                jta.setLineWrap(true);//自动换行
                jta.setEditable(false);//不可编辑
                JScrollPane jsp=new JScrollPane(jta);    //将文本域放入滚动窗口


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

                chkbox4.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(chkbox3.isSelected()) {
                            stdout.println("启动 监控Proxy");
                            clicks_Proxy = 4;
                        }else {
                            stdout.println("关闭 监控Proxy");
                            clicks_Proxy = 0;
                        }
                    }
                });

                //保存配置按钮
                btn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        XiaPao_api_HOST = txtfield_0.getText().split(":")[0];
                        XiaPao_api_Port = Integer.parseInt(txtfield_0.getText().split(":")[1]);
                        captcha_url_1 = txtfield_1.getText();
                        jps_2_4.setText("");
                        stdout.println(XiaPao_api_HOST);
                        stdout.println(captcha_url_1);
                        stdout.println(captcha_url_2);
                        stdout.println(captcha_url_3);
                        stdout.println(captcha_url_4);
                        stdout.println(captcha_url_5);

                        if(captcha_url_1.length()==0){
                            jta.insert("error：url为空，保存失败\n\n",0);
                        }else {
                            String select_ms="";
                            if(rb_url_1_1.isSelected()){
                                captcha_modular_1 = "1";
                                select_ms = rb_url_1_1.getText().split("（")[0];
                            }else {
                                captcha_modular_1 = "2";
                                select_ms = rb_url_1_2.getText().split("（")[0];
                            }
                            jl_00.setText(XiaPao_api_HOST+":"+XiaPao_api_Port);
                            jl_00.setForeground(Color.red);

                            String selectedItem = (String) jb_set.getSelectedItem();//选择编号的名字
                            String selectedItem2 = (String) jb_set_case_sensitive.getSelectedItem();//验证码输出模式
                            jl_2.setText("======》"+selectedItem+"    验证码模式："+select_ms+"    验证码输出模式："+selectedItem2+"    URL："+captcha_url_1);
                            jl_2.setForeground(Color.red);



                            //添加数据到map里面
                            //url、模式（1普通、2复杂）、验证码输出模式、复杂模式的验证码请求包、高级模式-数据来源（响应头/体）、高级模式-正则、高级模式-按钮（启动true/关闭false）
                            int selectindex = jb_set.getSelectedIndex()+1;//当前的验证码编号
                            int selectindex2 = jb_set_case_sensitive.getSelectedIndex();//当前的验证码输出模式
                            int selectindex3 = jb_1.getSelectedIndex(); //高级设置中的数据来源（响应头/体）

                            List<String> newList = new ArrayList<>();
                            newList.add(captcha_url_1);//验证码url地址
                            newList.add(captcha_modular_1);//模式名字
                            newList.add(String.valueOf(selectindex2));//当前的验证码输出模式
                            newList.add(jta_1.getText());//复杂模式的验证码请求包
                            newList.add(String.valueOf(selectindex3));//高级模式-数据来源（响应头/体）
                            newList.add(jps_txtfield_1.getText());//高级模式-正则
                            newList.add(String.valueOf(re_switch));//高级模式-按钮（启动true/关闭false）
                            yzm_set_map.put(selectindex, newList);


                            //标签更新
                            if(selectindex ==1 ){
                                jl_url_1.setText("验证码编号1："+select_ms+"、"+selectedItem2+"    URL:"+captcha_url_1);
                            }else if (selectindex ==2){
                                jl_url_2.setText("验证码编号2："+select_ms+"、"+selectedItem2+"    URL:"+captcha_url_1);
                            }else if (selectindex ==3){
                                jl_url_3.setText("验证码编号3："+select_ms+"、"+selectedItem2+"    URL:"+captcha_url_1);
                            }else if (selectindex ==4){
                                jl_url_4.setText("验证码编号4："+select_ms+"、"+selectedItem2+"    URL:"+captcha_url_1);
                            }else if (selectindex ==5){
                                jl_url_5.setText("验证码编号5："+select_ms+"、"+selectedItem2+"    URL:"+captcha_url_1);
                            }







                        }



                    }
                });

                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jta.setText("");
                    }
                });

                //右边上 设置
                jps.add(jls);
                jps.add(jls_1);
                jps.add(jls_2);
                jps.add(jls_3);
                jps.add(chkbox1);
                jps.add(chkbox2);
                jps.add(chkbox3);
                jps.add(chkbox4);
                jps.add(btn1);
                jps.add(jls_5);

                jps.add(jls_4);
                jps.add(jls_6);
                jps.add(jls_7);
                jps.add(btn2);

                //右边下
                jps_2.add(jsp);


                //右边上 高级设置
                jps_3.add(jps_2_0);
                jps_3.add(jps_2_1);
                jps_3.add(jb_1);
                jps_3.add(jps_2_3);
                jps_3.add(jps_txtfield_1);
                jps_3.add(jps_2_2);
                jps_3.add(jb_2);
                jps_3.add(jps_2_bt_1);
                jps_3.add(jps_2_4);





                //右边
                splitPanes.setLeftComponent(jps);//上面
                splitPanes.setRightComponent(jps_2);//下面

                //左边
                splitPanes2.setLeftComponent(jp);
                splitPanes2.setRightComponent(splitPanes_zx);

                //整体分布
                splitPane.setLeftComponent(splitPanes2);//添加在左面
                splitPane.setRightComponent(splitPanes);//添加在右面
                splitPane.setDividerLocation(1000);//设置分割的大小

                // customize our UI components
                callbacks.customizeUiComponent(splitPane);
                callbacks.customizeUiComponent(jps);
                callbacks.customizeUiComponent(jp);
                callbacks.customizeUiComponent(jps_2);
                callbacks.customizeUiComponent(jps_3);

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
            if(toolFlag == clicks_Repeater || toolFlag == clicks_Intruder || toolFlag == clicks_Proxy ){//监听Repeater、Intruder、Proxy
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
        //map里面的内容：url、模式（1普通、2复杂）、验证码输出模式、复杂模式的验证码请求包、高级模式-数据来源（响应头/体）、高级模式-正则、高级模式-按钮（启动true/关闭false）
        List<String> yzm_set_map_data;

        if(yzm_set_map.containsKey(xiapao_count)){
            yzm_set_map_data = yzm_set_map.get(xiapao_count);
            captcha_url = yzm_set_map_data.get(0);
            captcha_type = yzm_set_map_data.get(1);
        }else {
            if(xiapao_count == 1 || xiapao_count == 2 || xiapao_count == 3 || xiapao_count ==4 || xiapao_count==5){
                jta.insert("验证码"+xiapao_count+"：还未填写对应的验证码地址哟！！\n\n",0);//验证码结果输出到插件界面
            }else{
                jta.insert("验证码"+xiapao_count+"：没该验证码编号！\n\n",0);//验证码结果输出到插件界面
            }
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
        }else {//如果请求包中没有cookie
            captcha_cookie_base64 = helpers.base64Encode("null=null;");
        }

        //对验证码接口发起请求
        String Xiapao_body;
        List<String> Xiaopao_head = new ArrayList();
        Xiaopao_head.add("POST /imgurl HTTP/1.1");
        Xiaopao_head.add("Host: "+XiaPao_api_HOST);

        //map里面的内容：url、模式（1普通、2复杂）、验证码输出模式、复杂模式的验证码请求包、高级模式-数据来源（响应头1/体0）、高级模式-正则、高级模式-按钮（启动true/关闭false）
        Xiapao_body = "xp_url="+captcha_url_base64+//url
                "&xp_type="+yzm_set_map_data.get(1)+//模式（1普通、2复杂）
                "&xp_cookie="+captcha_cookie_base64+//cookie
                "&xp_set_ranges="+yzm_set_map_data.get(2)+//验证码输出模式
                "&xp_complex_request="+helpers.base64Encode(yzm_set_map_data.get(3))+//复杂模式的验证码请求包
                "&xp_rf="+yzm_set_map_data.get(4)+//高级模式-数据来源（响应头1/体0）
                "&xp_re="+helpers.base64Encode(yzm_set_map_data.get(5))+//高级模式-正则
                "&xp_is_re_run="+yzm_set_map_data.get(6);//高级模式-按钮（启动true/关闭false）

        IHttpService NEW_HttpService=helpers.buildHttpService(XiaPao_api_HOST,XiaPao_api_Port,"http");
        byte[] bodyByte = Xiapao_body.getBytes();
        byte[] new_Requests = helpers.buildHttpMessage(Xiaopao_head, bodyByte); //关键方法
        IHttpRequestResponse requestResponse = callbacks.makeHttpRequest(NEW_HttpService, new_Requests);//发送请求
        try{
            IResponseInfo requestinfo = helpers.analyzeResponse(requestResponse.getResponse());
            if (requestinfo.getStatusCode()!=200){
                jta.insert("error：瞎跑接口好像出问题了？接口响应码:"+String.valueOf(requestinfo.getStatusCode())+"\n\n",0);//验证码结果输出到插件界面
                return;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            jta.insert("error：瞎跑接口无应答，是不是配置错地址了 或者 开启代理了导致无法访问127.0.0.1？\n\n",0);//验证码结果输出到插件界面
            return;
        }

        String re_rp_data="";//正则匹配的结果
        String[] temp_1 = helpers.bytesToString(requestResponse.getResponse()).split("\\n");
        String captcha_data_ok = temp_1[temp_1.length-1].replace("\r","");//验证码识别结果
        BurpExtender.this.stdout.println(captcha_data_ok);
        if(yzm_set_map_data.get(6).equals("true")) {
            String[] captcha_data_ok_all = captcha_data_ok.split("\\|");
            captcha_data_ok = captcha_data_ok_all[0];
            re_rp_data = captcha_data_ok_all[1];//正则匹配的结果

            jta.insert("高级模式-验证码" + xiapao_count + "：" + captcha_data_ok + "\n正则结果为："+re_rp_data+"\n\n", 0);//验证码结果输出到插件界面
        }else {
            jta.insert("验证码" + xiapao_count + "：" + captcha_data_ok + "\n", 0);//验证码结果输出到插件界面
        }

        //修改正文
        IRequestInfo analyIRequestInfo = helpers.analyzeRequest(baseRequestResponse);
        int bodyOffset = analyIRequestInfo.getBodyOffset();//通过上面的analyIRequestInfo得到请求数据包体（body）的起始偏移
        String body_temp_1 = request.substring(bodyOffset).replaceAll("@xiapao@\\d@",captcha_data_ok);//修改正文
        if(yzm_set_map_data.get(6).equals("true")){
            body_temp_1 = body_temp_1.replaceAll("@xiapao@x@",re_rp_data);//修改正文

            //修改头部
            for(int i=0;i<headers.size();i++){
                if (headers.get(i).indexOf("@xiapao@x@") != -1){
                    String captcha_data = headers.get(i).replaceAll("@xiapao@x@",re_rp_data);//正则替换
                    headers.set(i,captcha_data);
                }
            }

        }
        byte[] body = body_temp_1.getBytes(StandardCharsets.UTF_8);//通过起始偏移点得到请求数据包体（body）的内容,然后替换

        //修改头部
        for(int i=0;i<headers.size();i++){
            //BurpExtender.this.stdout.println(headers.get(i));
            if (headers.get(i).indexOf("@xiapao@") != -1){
                String captcha_data = headers.get(i).replaceAll("@xiapao@\\d@",captcha_data_ok);//正则替换
                headers.set(i,captcha_data);
            }
        }


        byte[] newRequest = helpers.buildHttpMessage(headers,body);
        baseRequestResponse.setRequest(newRequest);//设置最终新的请求包
    }



}
