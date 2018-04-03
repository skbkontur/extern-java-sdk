package ru.skbkontur.sdk.extern.service.transport.invoker;


public class UserAgentService {

    public static final String USER_AGENT_STRING = getUserAgentString();

    private static String getUserAgentString() {
        Package aPackage = UserAgentService.class.getPackage();
        String version = aPackage.getImplementationVersion();
        String title = aPackage.getImplementationTitle();
        return title + "/" + version;
    }


}
