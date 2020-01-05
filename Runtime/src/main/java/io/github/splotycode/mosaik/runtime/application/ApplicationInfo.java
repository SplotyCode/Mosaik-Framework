package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.domparsing.dom.Document;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import lombok.Getter;

public class ApplicationInfo {

   @Getter private static String buildDate, buildNumber, version;
   private static boolean versionInfoLoaded = false;

   public static String getApplicationInfo() {
       if (!versionInfoLoaded) loadInfo();
       versionInfoLoaded = true;
       return "Mosaik-Framework " + version + " Built on " + buildDate + " (Git: #" + buildNumber + ")";
   }

    private static void loadInfo() {
        Document document = LinkBase.getInstance().getLink(Links.PARSING_MANAGER).parseResourceFile("/mosaikversion.kv");
        buildNumber = document.getFirstTextFromNode("number");
        buildDate = document.getFirstTextFromNode("date");
        version = document.getFirstTextFromNode("version");
    }

    private static String implementingName = null;
   private static AlmostBoolean wasRunning = AlmostBoolean.MAYBE;

   public static String getImplementingName() {
       boolean running = LinkBase.getInstance().getLink(Links.STARTUP_MANAGER).running();
       boolean runningChanged = wasRunning.isMaybe() || wasRunning.decide(false) != running;
       wasRunning = AlmostBoolean.fromBoolean(running);
       if (implementingName != null && !runningChanged) return implementingName;

       implementingName = getProvidedAppName();
       if (implementingName == null) {
           if (running) {
               Application app = CollectionUtil.getAny(LinkBase.getApplicationManager().getApplications());
               if (app == null) return null;
               implementingName = app.getName();
           } else implementingName = "Mosaik";
       }
       return implementingName;
   }

   private static String getProvidedAppName() {
        String name = System.getenv("mosaik-appname");
        if (name == null) {
            name = System.getenv("mosaik.appname");
            if (name == null) {
                name = System.getProperty("mosaik-appname");
            }
        }
        return name;
   }


}
