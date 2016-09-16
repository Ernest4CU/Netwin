package com.example.ernest.netwin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dalong.marqueeview.MarqueeView;
import com.dwin.dwinapi.SerialPort;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends Activity {


    public Handler updataViewHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://更新广告
                    xmlDom4j.setTextByName("advertisement",msg.obj+"");
                    //xmlDom4j.resetParameter();
                    netwinData.setAdvertisement(xmlDom4j.getAdvertisement());
                    adsTextView.setText(netwinData.getAdvertisement());
                    break;
                case 2://更新LOGO
                    imgLogo.setImageBitmap(getImgBitmap(netwinData.getStrLogoPicPath() + "/logo_pic.jpg"));//依然是图片地址的问题
                    break;
                default:break;
            }
        }
    };
    String[] adsVidwoList;
    String AdsText;
    ImageView arrowFlag;
    TextView curFloor;
    TextView curdate;
    ImageView dayOfWeek;
    MarqueeView adsTextView;
    SerialPort serialPort;
    VideoView adsShow;
    ImageView imgLogo;
    NetwinData netwinData = new NetwinData();//创建数据存储对象
    XmlDom4j xmlDom4j = new XmlDom4j();
    int intCurVideo=0;//记录当前播放的视频索引
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNetwinSystem();//初始化Netwin系统数据
        initView();//初始化界面



        openSerialPort();//开启串口

        //开启AP
        WifiHostBiz wifiHostBiz = new WifiHostBiz(this);
        if (!wifiHostBiz.isWifiApEnabled()) {
            wifiHostBiz.setWifiApEnabled(true);
        }


        new TimeThread().start();//开启日期更新进程，每秒更新一次
        new downThread().start();//开启网络服务器来监听上传任务

        setVideoAdd();//添加视频播放目录
        adsShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(++intCurVideo==3)
                    intCurVideo=0;
                videoDisplay(adsVidwoList[intCurVideo]);
            }
        });
        videoDisplay(adsVidwoList[0]);
    }

    private void initNetwinSystem() {
        //初始化Netwin结构目录
        creatNetwinPath();
        //初始化xml
        System.out.println("广告内容："+xmlDom4j.getAdvertisement());
        netwinData.setAdvertisement(xmlDom4j.getAdvertisement());
    }

    private void creatNetwinPath() {
        createPath(netwinData.getStrRootPath());//创建根目录
        createPath(netwinData.getStrVideoPath());//创建视频素材目录
        createPath(netwinData.getStrPicPath());//创建图片素材目录
        createPath(netwinData.getStrLogoPicPath());//创建LOGO素材目录
        createPath(netwinData.getStrWeekPicPath());//创建week素材目录
        createPath(netwinData.getStrNumPicPath());//创建num素材目录
        createPath(netwinData.getStrLetterPicPath());//创建num素材目录
    }

    private static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }
    private void initView()
    {
        arrowFlag = (ImageView) findViewById(R.id.arrowFlag);
        curFloor = (TextView) findViewById(R.id.curFloor);
        curdate = (TextView) findViewById(R.id.curdate);
        dayOfWeek = (ImageView) findViewById(R.id.dayOfWeek);
        adsTextView = (MarqueeView) findViewById(R.id.AdsTextView);
        adsShow = (VideoView) findViewById(R.id.adsShow);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLogo.setImageBitmap(getImgBitmap(netwinData.getStrLogoPicPath() + "/logo_pic.jpg"));//此处图片资源地址有问题

        adsTextView.setText(netwinData.getAdvertisement());//设置广告
        adsTextView.startScroll();
        closeBar();//关闭导航栏
    }

    private Bitmap getImgBitmap(String parth) {
        Bitmap imgBitmap=null;
        File file = new File(parth);
        if (file.exists()) {
            imgBitmap = BitmapFactory.decodeFile(parth);
        }
        return imgBitmap;
    }
    private void openSerialPort(){
        serialPort = new SerialPort("S0", 9600, 8, 1,110);
        new ReceiveThread().start();
    }

    //读SD中的文件
    public String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);

            res = EncodingUtils.getString(buffer, "gb2312");

            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    class downThread extends Thread{
        @Override
        public void run() {
            //服务器中信息
            try {
                ServerSocket serverSocket = new ServerSocket(8888);
                Socket socket = null;
                int count=0;
                System.out.println("***服务器即将启动，等待客户端的连接***");
                while (true) {
                    socket = serverSocket.accept();
                    DownloadThread downloadThread = new DownloadThread(socket,netwinData.getStrRootPath(),updataViewHandler,netwinData);
                    downloadThread.start();
                    count++;//用来记录客户量
                    InetAddress address = socket.getInetAddress();
                    System.out.println("客户端IP = " + address.getHostAddress());
                    System.out.println("客户量 = " + count);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setVideoAdd(){
        adsVidwoList = new String[3];
        adsVidwoList[0]="/mnt/sdcard1/Netwin/Video/NetwinVideo0.mp4";
        adsVidwoList[1]="/mnt/sdcard1/Netwin/Video/NetwinVideo1.mp4";
        adsVidwoList[2]="/mnt/sdcard1/Netwin/Video/NetwinVideo2.mp4";
    }

    private void videoDisplay(String str){
//        Uri videoUri = Uri.parse("/mnt/sdcard1/bell2.mp4");
        Uri videoUri = Uri.parse(str);
        adsShow.setVideoURI(videoUri);
        adsShow.start();
    }



    /**
     * 接收数据线程
     */
    class ReceiveThread extends Thread {
        public void run() {
            String type = "HEX".trim();
            while (serialPort.isOpen) {
                String data = serialPort.receiveData(type);
                if (data != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = data;
//                    System.out.println(data + "<<<<<<<<==========data");
                    myHandler.sendMessage(msg);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int strParseToInt(String str) {
        return Integer.parseInt(str);
    }

    private android.os.Handler myHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    String strData= (String) msg.obj;
                    String[] strStateData=strData.split(" ");
//                    System.out.println("处理后数据长度："+strStateData.length);
//                    for (String str : strStateData
//                            ) {
//                        System.out.println("处理后的数据："+str);
//                    }


                    if ((strStateData[5].equals("BB")) && (strStateData[0].equals("AA"))) {
//                        System.out.println("校验成功");
//                        System.out.println("十位："+strParseToInt(strStateData[1]));
//                        System.out.println("个位："+strParseToInt(strStateData[2]));
//                        System.out.println("和："+strParseToInt(strStateData[1])+strParseToInt(strStateData[2]));
                        curFloor.setText(""+strParseToInt(strStateData[1])+strParseToInt(strStateData[2]));
                        switch (strParseToInt(strStateData[3])){
//                            case 0:arrowFlag.setImageResource(R.drawable.up);break;
//                            case 1:arrowFlag.setImageResource(R.drawable.down);break;
                            case 0:arrowFlag.setImageBitmap(netwinData.getUp_pic());break;
                            case 1:arrowFlag.setImageBitmap(netwinData.getDown_pic());break;
                            default:break;
                        }
                    }

//                    Date date = new Date();
//                    System.out.println("进入handler");
//                    String test = (String) msg.obj;
//                    char[] demo = test.toCharArray();
//                    if(demo.length>2){
//                        curFloor.setText(new String(demo,0,2));
//                        switch (demo[2]){
//                            case 'U':arrowFlag.setImageResource(R.drawable.up);break;
//                            case 'D':arrowFlag.setImageResource(R.drawable.down);break;
//                            default:break;
//                        }
//                    }
                    break;
                default:
                    break;
            }
        };
    };

    private void closeBar(){
        try{
            //需要root 权限
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79";

            if(vr.SDK_INT >= vc.ICE_CREAM_SANDWICH){
                ProcID = "42"; //ICS AND NEWER
            }

            //需要root 权限
            Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","service call activity "+ ProcID +" s16 com.android.systemui"}); //WAS 79
            proc.waitFor();

        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showBar(){
        try{
            Process proc = Runtime.getRuntime().exec(new String[]{
                    "am","startservice","-n","com.android.systemui/.SystemUIService"});
            proc.waitFor();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler1.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private int getNumFromWeek(String week) {
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期日"};
        for(int i=0;i<weekDays.length;i++){
            if (week.equals(weekDays[i])) {
                return i;
            }
        }

        return -1;
    }
    //在主线程里面处理消息并更新UI界面
    private android.os.Handler mHandler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long time=System.currentTimeMillis();
                    Date date=new Date(time);
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    curdate.setText(format.format(date)); //更新时间

                    format=new SimpleDateFormat("EEEE");
//                    dayOfWeek.setText(format.format(date));//更新星期

//                    dayOfWeek.setImageBitmap(""+getNumFromWeek(format.format(date)+""));//更新星期
                    dayOfWeek.setImageBitmap(netwinData.bmWeek.get(getNumFromWeek(format.format(date))));//更新星期
                    break;
                default:
                    break;
            }
        }
    };
}
