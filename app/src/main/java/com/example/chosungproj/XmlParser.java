package com.example.chosungproj;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlParser extends AsyncTask<String, Void, Document> {
    URL url;
    static String[][] definition_list;
    static String[] word_list;
    Document doc;
    @Override
    protected Document doInBackground(String... strings) {
        try {
            url = new URL("https://stdict.korean.go.kr/api/search.do?certkey_no=2672&key=86F3B8C49B9C4187A83294E47AA9058F&type_search=search&q=%EB%B0%A5");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            String s = "";
            NodeList nodelist = doc.getElementsByTagName("item");

            Log.v("nodelist", String.valueOf(nodelist.getLength()));
            for(int i=0;i<nodelist.getLength();i++){
                Node node = nodelist.item(i);
                Element fstElmnt = (Element)node;

                NodeList sense = fstElmnt.getElementsByTagName("definition");
                s =  sense.item(0).getChildNodes().item(0).getNodeValue();
                Log.v("parse", s);
            }

            //super.onPostExecute(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    @Override
    protected void onPostExecute(Document doc) {

    }
}
