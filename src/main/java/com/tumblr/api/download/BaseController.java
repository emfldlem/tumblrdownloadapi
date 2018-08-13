package com.tumblr.api.download;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class BaseController {

    @GetMapping("/url")
    public String Urltest() throws SAXException, ParserConfigurationException {
        //String url = "https://cuteanimalspls.tumblr.com/api/read?type=photo";
        String url = "https://artmunny.tumblr.com/api/read?type=photo";
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();

            NodeList tumblelogList = doc.getElementsByTagName("tumblelog");
            Element idelement = (Element) tumblelogList.item(0);
            String id = idelement.getAttribute("name");
            String Folder = "C:/imgdata/" + id+"/";
            File mkFolder = new File(Folder);
            if(!mkFolder.exists()) {
                mkFolder.mkdir();
            }

            NodeList postList = doc.getElementsByTagName("posts");
            Element postelement = (Element) postList.item(0);
            int total = Integer.parseInt(postelement.getAttribute("total"));

            int count = 1;
            String path = "C:/imgdata/" + id+"/"+id+"_"+count+".jpg";

            File outputFile = new File(path);
            BufferedImage bi = null;


            for (int i = 0; i < total; i = i + 20) {
                url = url + "&start=" + i;
                doc = dBuilder.parse(url);

                NodeList nList = doc.getElementsByTagName("post");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);


                    //System.out.println(nNode.getChildNodes());

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nNode;

                        //System.out.println(element.getTextContent());
                        String imgUrl = getTagValue("photo-url", element);

                        System.out.println("######################################");
                        System.out.println(getTagValue("photo-url", element));
                        System.out.println("###########################111111111111111###########");

                        URL downUrl = new URL(imgUrl);
                        bi = ImageIO.read(downUrl);

                        ImageIO.write(bi, "jpg", outputFile);
                        count++;
                        path = "C:/imgdata/" + id+"/"+id+"_"+count+".jpg";
                        outputFile = new File(path);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "index";
    }

    @GetMapping("/video")
    public String video() {
        String url = "https://artmunny.tumblr.com/api/read?type=video";
        //String url = "https://cuteanimalspls.tumblr.com/api/read?type=video";
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            doc.getDocumentElement().normalize();

            NodeList tumblelogList = doc.getElementsByTagName("tumblelog");
            Element idelement = (Element) tumblelogList.item(0);
            String id = idelement.getAttribute("name");
            String Folder = "C:/imgdata/" + id+"/";
            File mkFolder = new File(Folder);
            if(!mkFolder.exists()) {
                mkFolder.mkdir();
            }

            NodeList postList = doc.getElementsByTagName("posts");
            Element postelement = (Element) postList.item(0);
            int total = Integer.parseInt(postelement.getAttribute("total"));

            int count = 1;
            String path = "C:/imgdata/" + id+"/"+id+"_"+count+".jpg";

            File outputFile = new File(path);
            BufferedImage bi = null;


            for (int i = 0; i < total; i = i + 5000) {
                url = url + "&start=" + i;
                doc = dBuilder.parse(url);

                NodeList nList = doc.getElementsByTagName("post");

                for (int temp = 0; temp <nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);


                    //System.out.println(nNode.getChildNodes());

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nNode;

                        //System.out.println(element.getTextContent());
                        String imgUrl = getTagValue("video-player", element);

                        System.out.println("#########################################");
                        System.out.println(imgUrl);
                        System.out.println("#########################################");

                        String[] sub1 = imgUrl.split("<source src=\"");
                        String sub2 = sub1[1].replace("\" type=\"video/mp4\">\n" +
                                "</video>", "");
                        getVideo(sub2,id,id+"_"+count);
                        count++;

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }


    public void getVideo(String vurl, String id, String fileName) {
        try {
            URL url = new URL(vurl);
            File filePath = new File("C:\\Download\\"+id);
            File fileDir = filePath.getParentFile();

            filePath.mkdirs();

            FileOutputStream fos = new FileOutputStream("C:\\Download\\"+id+"\\"+fileName+".mp4");
            InputStream is = url.openStream();

            byte[] buf = new byte[1024];

            double len = 0;

            double tmp = 0;

            double percent = 0;

            while ((len = is.read(buf)) > 0) {
                tmp += len / 1024 / 1024;
                percent = tmp / 1229 * 100;

                System.out.printf("%.2f", tmp);

                System.out.print("MB");
                System.out.println("");

                fos.write(buf, 0, (int) len);

            }

            fos.close();

            is.close();

            Runtime rt = Runtime.getRuntime();

            System.out.println("다운로드 완료\r\n폴더를 띄워드립니다.");

            //rt.exec("explorer.exe C:\\Download\\");


            /*BufferedInputStream bis = new BufferedInputStream(url.openStream());
            FileOutputStream fis = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bis.read(buffer, 0, 1024)) != -1) {
                fis.write(buffer, 0, count);
            }
            fis.close();
            bis.close();*/
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }

}
