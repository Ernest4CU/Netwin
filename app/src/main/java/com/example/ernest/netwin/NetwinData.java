package com.example.ernest.netwin;

/**
 * Created by 58255 on 2016/9/13.
 */
public class NetwinData {
    private String strRootPath = "/mnt/sdcard1/Netwin";
    private String strVideoPath = strRootPath+"/Video";
    private String strPicPath = strRootPath+"/Pic";
    private String strPicLogoPath = strPicPath+"/logo_pic.jpg";
    private String strConfPath = strRootPath+"/Conf.xml";
    private String strVideo0Path = strVideoPath+"/NetwinVideo0.mp4";
    private String strVideo1Path = strVideoPath+"/NetwinVideo1.mp4";
    private String strVideo2Path = strVideoPath+"/NetwinVideo2.mp4";
    private String advertisement = "请设置广告！";

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

    public String getStrPicLogoPath() {
        return strPicLogoPath;
    }
}
