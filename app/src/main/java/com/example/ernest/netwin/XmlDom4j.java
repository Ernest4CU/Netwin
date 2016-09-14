package com.example.ernest.netwin;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 58255 on 2016/9/12.
 */
public class XmlDom4j {
    private String advertisement="";
    private String confPath="/mnt/sdcard1/Netwin/conf.xml";
    public XmlDom4j() {
        initXml();
        resetParameter();//读取参数
    }

    public void resetParameter() {
        try {
            SAXReader reader = new SAXReader();
            FileInputStream fin = new FileInputStream(confPath);

            System.out.println("读取文件成功！");
            Document doc = reader.read(fin);
            Element root = doc.getRootElement();
            this.advertisement = getTextByName(root, "advertisement");//获得广告词
            System.out.println("广告：" + this.advertisement);
            //需要测试如果名称不存在会发生什么
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private String getTextByName(Element root, String name) {
        Element element = root.element(name);
        return element.getText();
    }

    public void setTextByName(String name,String text) {
        try {
            SAXReader reader = new SAXReader();
            FileInputStream fin = new FileInputStream(confPath);
            System.out.println("读取文件成功！");
            Document doc = reader.read(fin);
            Element root = doc.getRootElement();
            System.out.println(root.attributeValue("name"));
            Element element = root.element(name);
            element.setText(text);

            File file = new File(confPath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            XMLWriter out = new XMLWriter(new FileWriter(file));
            out.write(doc);
            out.flush();
            out.close();
            System.out.println("更新成功");
            resetParameter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void setText(Element root,String name,String nameStr,String text) {
        Element college = root.addElement(name);
        college.addAttribute("name", nameStr);
        college.setText(text);
    }
    public void initXml() {
        File file = new File(confPath);
        if (file.exists()) {
            //file.delete();
            System.out.println("配置文件存在，无需新建");
            return;
        }
        System.out.println("配置文件不存在，初始化配置文件");
        try {
            // 创建一个xml文档
            Document doc = DocumentHelper.createDocument();
            Element university = doc.addElement("Netwin");
            university.addAttribute("name", "Netwin");
            // 注释
            university.addComment("存储迪文显示屏的信息");
            setText(university,"rootPath","rootPath","/mnt/sdcard1/Netwin");
            setText(university,"picPath","picPath","/Pic");
            setText(university,"videoPath","videoPath","/Video");
            setText(university,"advertisement","advertisement","xml广告测试");


            file.createNewFile();
            System.out.println("配置文件初始化成功");
            XMLWriter out = new XMLWriter(new FileWriter(file));
            out.write(doc);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getAdvertisement() {
        return advertisement;
    }
}