package com.example.ernest.netwin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 58255 on 2016/9/13.
 */
public class NetwinData {
    private String strRootPath = "/mnt/sdcard1/Netwin";
    private String strVideoPath = strRootPath+"/Video";
    private String strPicPath = strRootPath+"/Pic";
    private String strDirPicPath = strPicPath + "/Dir";
    private String strLogoPicPath = strPicPath+"/Logo";
    private String strWeekPicPath = strPicPath+"/Week";
    private String strNumPicPath = strPicPath+"/Num";
    private String strLetterPicPath = strPicPath+"/Letter";
    private String strConfPath = strRootPath+"/Conf.xml";
    private String strVideo0Path = strVideoPath+"/NetwinVideo0.mp4";
    private String strVideo1Path = strVideoPath+"/NetwinVideo1.mp4";
    private String strVideo2Path = strVideoPath+"/NetwinVideo2.mp4";
    private String advertisement = "请设置广告！";

//    private String StrPicLogoPath = strLogoPicPath + "/logo_pic.jpg";

    List<Bitmap> bmWeek = new ArrayList<>();
    private String strLogo = strLogoPicPath+"/logo_pic.png";//文件地址
    private String strMON = strWeekPicPath + "/mon_pic.png";
    private String strTUE = strWeekPicPath + "/tue_pic.png";
    private String strWED = strWeekPicPath + "/wed_pic.png";
    private String strTHU = strWeekPicPath + "/thu_pic.png";
    private String strFRI = strWeekPicPath + "/fri_pic.png";
    private String strSAT = strWeekPicPath + "/sat_pic.png";
    private String strSUN = strWeekPicPath + "/sun_pic.png";



    private Bitmap up_pic;
    private Bitmap down_pic;
    private Bitmap mon_pic;


    public NetwinData() {
        up_pic = decodeSampleFromSD( strDirPicPath+ "/up_pic.png",160,170);
        down_pic = decodeSampleFromSD( strDirPicPath+ "/down_pic.png",160,170);
        System.out.println("箭头添加成功");
        bmWeek.add(decodeSampleFromSD(strMON,160,60));
        bmWeek.add(decodeSampleFromSD(strTUE,160,60));
        bmWeek.add(decodeSampleFromSD(strWED,160,60));
        bmWeek.add(decodeSampleFromSD(strTHU,160,60));
        bmWeek.add(decodeSampleFromSD(strFRI,160,60));
        bmWeek.add(decodeSampleFromSD(strSAT,160,60));
        bmWeek.add(decodeSampleFromSD(strSUN,160,60));
        System.out.println("添加星期图片成功");
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,  int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高           // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;

    }
    public Bitmap decodeSampleFromSD(String path,int sdwidth,int sdheight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;

        BitmapFactory.decodeFile(path, options);
        options.inSampleSize=calculateInSampleSize(options, sdwidth, sdheight);
        options.inJustDecodeBounds=false;
        options.inDither=false;
        options.inPreferredConfig=Bitmap.Config.ARGB_8888;


        return BitmapFactory.decodeFile(path, options);
    }

    private Bitmap getImgBitmap(String Path) {
        Bitmap imgBitmap=null;
        File file = new File(Path);
        if (file.exists()) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
            imgBitmap = BitmapFactory.decodeFile(Path);
        }
        return imgBitmap;
    }


    public String getStrRootPath() {
        return strRootPath;
    }

    public String getStrVideoPath() {
        return strVideoPath;
    }

    public String getStrPicPath() {
        return strPicPath;
    }

    public String getStrConfPath() {
        return strConfPath;
    }

    public String getStrVideo0Path() {
        return strVideo0Path;
    }

    public String getStrVideo1Path() {
        return strVideo1Path;
    }

    public String getStrVideo2Path() {
        return strVideo2Path;
    }

    public String getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(String advertisement) {

        this.advertisement = advertisement;
    }

    public String getStrLogoPicPath() {
        return strLogoPicPath;
    }

    public String getStrNumPicPath() {
        return strNumPicPath;
    }

    public String getStrLetterPicPath() {
        return strLetterPicPath;
    }

    public String getStrWeekPicPath() {
        return strWeekPicPath;
    }


    public Bitmap getUp_pic() {
        return up_pic;
    }

    public Bitmap getDown_pic() {
        return down_pic;
    }

    public Bitmap getMon_pic() {
        return mon_pic;
    }
}
