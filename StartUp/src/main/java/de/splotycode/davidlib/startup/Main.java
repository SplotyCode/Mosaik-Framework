package de.splotycode.davidlib.startup;

import de.splotycode.davidlib.startup.application.ApplicationManager;
import de.splotycode.davidlib.startup.envirementchanger.StartUpInvirementChangerImpl;
import de.splotycode.davidlib.startup.manager.StartUpManager;
import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import de.splotycode.davidlib.startup.starttask.StartTaskExecutor;
import lombok.Getter;
import me.david.davidlib.application.*;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.startup.BootContext;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.utils.ReflectionUtil;
import me.david.davidlib.utils.StringUtil;

@Getter
public class Main {

    @Getter private static Main instance;

    private static BootContext bootData;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("Starting FrameWork!");
        if (ReflectionUtil.getCallerClasses().length >= 4) {
            System.out.println("Framework was not invoked by JVM! It was invoked by: " + ReflectionUtil.getCallerClass().getName());
        }
        System.out.println();

        bootData = new BootContext(args, start);
        instance = new Main();
    }

    private Main() {
        ApplicationManager applicationManager = new ApplicationManager();

        /* Register Links */
        LinkBase.getInstance().registerLink(Links.BOOT_DATA, bootData);
        LinkBase.getInstance().registerLink(Links.APPLICATION_MANAGER, new ApplicationManager());
        LinkBase.getInstance().registerLink(Links.STARTUP_MANAGER, new StartUpManager());

        /* Register StartUp Environment Changer */
        StartUpEnvironmentChanger environmentChanger = new StartUpInvirementChangerImpl();
        LinkBase.getInstance().registerLink(Links.STARTUP_ENVIRONMENT_CHANGER, environmentChanger);

        /* Running Startup Tasks*/
        StartTaskExecutor.getInstance().findAll(false);
        StartTaskExecutor.getInstance().runAll(environmentChanger);

        /* Starting Applications */
        applicationManager.startUp();

        StartUpProcessHandler.getInstance().end();

        System.out.println("Started " + applicationManager.getLoadedApplicationsCount() + " Applications: " + StringUtil.join(applicationManager.getLoadedApplications(), IApplication::getName, ", "));
    }

}
