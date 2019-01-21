package io.github.splotycode.mosaik.automatisation.crawler.site;

import io.github.splotycode.mosaik.automatisation.crawler.Crawler;
import lombok.Getter;
import io.github.splotycode.mosaik.util.io.IOUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexingSite implements Site {

    @Getter private URL url;
    @Getter private Crawler crawler;

    private String content;

    private String getContent0() {
        try {
            return IOUtil.loadText(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Pattern pattern = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");

    public Set<String> getLinks() {
        Set<String> links = new HashSet<>();
        Matcher matcher = pattern.matcher(getContent());
        while (matcher.find()) {
            links.add(matcher.group());
        }
        return links;
    }


    public String getContent() {
        if (content == null) {
            content = getContent0();
        }
        return content;
    }

    public IndexingSite(URL url, Crawler crawler) {
        this.url = url;
        this.crawler = crawler;
    }
}
