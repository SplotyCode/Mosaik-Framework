package me.david.davidlib.util.info;

import lombok.Getter;
import me.david.davidlib.util.core.application.Application;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;
import me.david.davidlib.util.core.storage.Document;

public class ApplicationInfo {

   @Getter private static final String buildDate, buildNumber, version;

   public static String getApplicationInfo() {
       return "DavidLib " + version + " Built on " + buildDate + " (Git: #" + buildNumber + ")";
   }

   private static String implementingName = null;

   public static String getImplementingName() {
       if (implementingName != null) return implementingName;

       implementingName = System.getProperty("davidlib.appname");
       if (implementingName == null) {
           Application app = LinkBase.getApplicationManager().getApplications().iterator().next();
           if (app == null) return null;
           implementingName = app.getName();
           return implementingName;
       }
       return implementingName;
   }

   static {
       Document document = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).parseResourceFile("/versioninformation.kv");
       buildNumber = document.getNode("number").name();
       buildDate = document.getNode("date").name();
       version = document.getNode("version").name();
   }

}
