package me.david.automatisation.crawler.site;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;

@AllArgsConstructor
@Getter
public class DefaultSite implements Site {

    private URL url;

}
