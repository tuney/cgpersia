package com.tooolshed.projects.cgpersia;

import java.net.URL;
import java.util.List;

public class DetailedEbookPageWorker implements Runnable {

 private URL url;
 private int i;
 private List<String> allDownloadLinks;

 public DetailedEbookPageWorker(URL url, List<String> allDownloadLinks, int i) {
  this.url = url;
  this.allDownloadLinks = allDownloadLinks;
  this.i = i;
 }

 public void add(List<String> downloadLinks) {
  synchronized (allDownloadLinks) {
   allDownloadLinks.addAll(downloadLinks);
  }
 }

 @Override
 public void run() {
  System.out.println(i + " parsing " + url);

  try {
   CgpersiaDetailPage ebookDetailPage = new CgpersiaDetailPage();
   ebookDetailPage.setUrl(url);
   ebookDetailPage.parse();
   
   List<String> downloadLinks = ebookDetailPage.getDownloadLinks();
   add(downloadLinks);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
}
