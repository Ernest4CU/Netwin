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

    private String strLogo = strLogoPicPath+"/logo_pic.jpg";//文件地址
    private String strMon = strWeekPicPath + "/mon_pic.jpg";
    private String strTUE = strWeekPicPath + "/tue_pic.jpg";
    private String strWED = strWeekPicPath + "/wed_pic.jpg";
    private String strTHU = strWeekPicPath + "/thu_pic.jpg";
    private String strFRI = strWeekPicPath + "/fri_pic.jpg";
    private String strSAT = strWeekPicPath + "/sat_pic.jpg";
    private String strSUN = strWeekPicPath + "/sun_pic.jpg";

    private String strDirPicPath = strPicPath + "/Dir";

    private Bitmap up_pic;
    private Bitmap down_pic;

    public NetwinData() {
        up_pic = getImgBitmap( strDirPicPath+ "/up_pic.jpg");
        down_pic = getImgBitmap( strDirPicPath+ "/down_pic.jpg");
    }

    private Bitmap getImgBitmap(String parth) {
        Bitmap imgBitmap=null;
        File file = new File(parth);
        if (file.exists()) {
            imgBitmap = BitmapFactory.decodeFile(parth);
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
}
