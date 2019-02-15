package com.scalesec.vulnado;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class LinkLister {
  public static List<String> getLinks(String url) {
    List<String> result = new ArrayList<String>();
    try {
      Document doc = Jsoup.connect(url).get();
      Elements links = doc.select("a");
      for (Element link : links) {
        result.add(link.absUrl("href"));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
