package ru.skbkontur.sdk.extern.service.transport.invoker;


public class UserAgentService {

    public static String detectUserAgent() {
        return getLanguage() + "/" + getVersion();
    }

    private static String getVersion() {
        return System.getProperty("java.version");
    }

    private static String getLanguage() {
        return "java";
    }

}
