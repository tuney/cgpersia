package com.tooolshed.projects.cgpersia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

public class WebRequestService {
 public static Document request(URL website) throws Exception {
  return Jsoup.parse(website, 50 * 1000);
 }
}
