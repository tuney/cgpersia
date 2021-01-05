package com.tooolshed.projects.cgpersia;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RunCgpersia {

 private WebThreadPoolExecutor webThreadPoolExecutor = new WebThreadPoolExecutor();
 public static final int WEBSITE_PAGES = 300;
 
 public static void main(String[] args) throws Exception {

  RunCgpersia runAppWow = new RunCgpersia();

  URL url1 = new URL("https://cgpersia.com/");

  List<URL> allWebsites = new ArrayList<>();
  allWebsites.add(url1);

  List<URL> allDetailedPages = new ArrayList<>();

  for (URL url : allWebsites) {
   System.out.println("Checking " + url.toString());

   List<URL> allPages = runAppWow.getAllPages(url);

   for (URL page : allPages) {
    OverviewEbookPageWorker overviewEbookPageWorker = new OverviewEbookPageWorker(page, allDetailedPages);
    runAppWow.webThreadPoolExecutor.add(overviewEbookPageWorker);
   }

   runAppWow.webThreadPoolExecutor.shutdown();

   while (!runAppWow.webThreadPoolExecutor.isTerminated()) {
    Thread.sleep(5 * 1000);
    System.out.println("Waiting...");
   }

   allPages = null;

   System.out.println("total single detailed pages found: " + allDetailedPages.size());

   runAppWow.webThreadPoolExecutor.reinitialize();

   List<String> allDownloadLinks = new ArrayList<>();

   int i = allDetailedPages.size();
   // visit now each single page and download
   for (URL singleDetailedPage : allDetailedPages) {
    Runnable runnable = new DetailedEbookPageWorker(singleDetailedPage, allDownloadLinks, i);
    runAppWow.webThreadPoolExecutor.add(runnable);
    i--;
   }

   runAppWow.webThreadPoolExecutor.shutdown();

   while (!runAppWow.webThreadPoolExecutor.isTerminated()) {
    Thread.sleep(5 * 1000);
    System.out.println("Waiting...");
   }

   allDetailedPages = null;

   File wowEbookAllFile = new File("cgpersia_all.txt");
   File wowEbookNewFile = new File("cgpersia_new.txt");

   List<String> wowEbookAllFileContent = new ArrayList<>();

   try {
    BufferedReader allFileReader = new BufferedReader(new FileReader(wowEbookAllFile));
    FileWriter newFileWriter = new FileWriter(wowEbookNewFile);

    String link = null;

    while ((link = allFileReader.readLine()) != null) {
     wowEbookAllFileContent.add(link);
    }

    for (String downloadLink : allDownloadLinks) {
     boolean found = false;

     for (String alreadySavedDownloadLink : wowEbookAllFileContent) {
      if (downloadLink.equals(alreadySavedDownloadLink)) {
       // found
       found = true;
       break;
      }
     }

     if (!found) {
      // save to new
      System.out.println("not found: " + downloadLink);
      newFileWriter.append(downloadLink + "\n");
     }
    }
    newFileWriter.close();

   } catch (FileNotFoundException e) {
    System.err.println("File not found: " + wowEbookAllFile);


    FileWriter allfileWriter = new FileWriter(new File("cgpersia_new.txt"));

    for (String link : allDownloadLinks) {
     allfileWriter.append(link + "\n");
    }
    allfileWriter.close();
   }
  }
 }

 public List<URL> getAllPages(URL url) throws Exception {
  List<URL> urls = new ArrayList<>();
  urls.add(url);

  for (int i = 1; i <= WEBSITE_PAGES; i++) {
   urls.add(new URL(url + "page/" + i + "/"));
  }

  return urls;
 }
}