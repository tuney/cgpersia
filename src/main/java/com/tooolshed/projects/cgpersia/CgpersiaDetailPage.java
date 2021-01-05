package com.tooolshed.projects.cgpersia;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CgpersiaDetailPage {
	private URL url;
	private List<String> downloadLinks = new ArrayList<>();

	private final String NITROFLARE_HTTP = "http://nitroflare";
	private final String NITROFLARE_HTTPS = "https://nitroflare";

	private final String RG_HTTP = "http://rg.to";
	private final String RG_HTTPS = "https://rg.to";

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public List<String> getDownloadLinks() {
		return downloadLinks;
	}

	public void parse() throws Exception {

		Document detailEbookPage = WebRequestService.request(url);
		Elements entryContainDownloadLinks = detailEbookPage.getElementsByClass("entry-content");

		for (Element entryContainDownloadLink : entryContainDownloadLinks) {
			Elements preFields = entryContainDownloadLink.getElementsByTag("pre");

			for (Element preField : preFields) {
				Stream<String> lineStream = preField.text().lines();
				lineStream.forEach(line -> {
					if (line.startsWith(NITROFLARE_HTTP) || line.startsWith(NITROFLARE_HTTPS)
							|| line.startsWith(RG_HTTP) || line.startsWith(RG_HTTPS)) {
						System.out.println("ADDING: " + line);
						downloadLinks.add(line);
					}
				});
			}
		}
	}
}