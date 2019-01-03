package me.david.davidlib.runtimeapi.application;

import lombok.Getter;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;
import me.david.davidlib.runtimeapi.storage.Document;

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
