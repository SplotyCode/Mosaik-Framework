package me.david.davidlib.info;

import lombok.Getter;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.storage.Document;

public class ApplicationInfo {

   @Getter private static final String buildDate, buildNumber, version;

   public static String getApplicationInfo() {
       return "DavidLib " + version + " Built on " + buildDate + " (Git: #" + buildNumber + ")";
   }

   static {
       Document document = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).parseResourceFile("/versioninformation.kv");
       buildNumber = document.getNode("number").name();
       buildDate = document.getNode("date").name();
       version = document.getNode("version").name();
   }

}
