package ru.skbkontur.sdk.extern.service.transport.invoker;


public class UserAgentService {

    public static final String SDK = "java-sdk";

    public static final String SDK_VERSION = getSdkVersion();

    public static final String SDK_VERSION_UNDEFINED = "undefined";

    public static final String USER_AGENT_STRING = SDK + "/" + SDK_VERSION;

    private static String getSdkVersion() {
        String implementationVersion = UserAgentService.class.getPackage().getImplementationVersion();
        if (implementationVersion == null) {
            implementationVersion = SDK_VERSION_UNDEFINED;
        }
        return implementationVersion;
    }

}
