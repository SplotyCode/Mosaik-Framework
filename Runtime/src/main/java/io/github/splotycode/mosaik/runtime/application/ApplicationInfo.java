package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import lombok.Getter;

public class ApplicationInfo {

   @Getter private static final String buildDate, buildNumber, version;

   public static String getApplicationInfo() {
       return "Mosaik-Framework " + version + " Built on " + buildDate + " (Git: #" + buildNumber + ")";
   }

   private static String implementingName = null;

   public static String getImplementingName() {
       if (implementingName != null && !LinkBase.getInstance().getLink(Links.STARTUP_MANAGER).running()) return implementingName;

       implementingName = System.getenv("mosaik.appname");
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
       buildNumber = document.getFirstTextFromNode("number");
       buildDate = document.getFirstTextFromNode("date");
       version = document.getFirstTextFromNode("version");
   }

}
