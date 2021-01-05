package com.tooolshed.projects.cgpersia;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OverviewEbookPageWorker implements Runnable {

 private URL url;
 private List<URL> allDetailedPages;

 public OverviewEbookPageWorker(URL url, List<URL> allDetailedPages) {
  this.url = url;
  this.allDetailedPages = allDetailedPages;
 }

 public void add(List<URL> detailedPages) {
  synchronized (allDetailedPages) {
   allDetailedPages.addAll(detailedPages);
  }
 }

 @Override
 public void run() {
  try {
   List<URL> singlePages = parseOverviewPage(url);
   add(singlePages);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 public List<URL> parseOverviewPage(URL page) throws Exception {
  List<URL> singlePages = new ArrayList<>();

  Document document = WebRequestService.request(page);

  Elements postTitleElements = document.getElementsByClass("more-link");

  System.out.println("parsing overview page: " + url);
  System.out.println("found detailed pages: " + postTitleElements.size());

  for (Element postTitleElement : postTitleElements) {
   String linkToSinglePage = postTitleElement.getElementsByTag("a").attr("href");
   singlePages.add(new URL(linkToSinglePage));

   System.out.println("link: " + linkToSinglePage);
  }

  System.out.println("\n");

  return singlePages;
 }
}
