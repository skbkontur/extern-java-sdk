package ru.kontur.extern_api.sdk.model;

import java.util.List;

public class RegistrationInfo {

    private ApplicationCode applicationCode;

    private List<ApplicantInfo> applicantInfos;

    private UlInfo ulInfo;

    private IpInfo ipInfo;

    public ApplicationCode getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(ApplicationCode applicationCode) {
        this.applicationCode = applicationCode;
    }

    public List<ApplicantInfo> getApplicantInfos() {
        return applicantInfos;
    }

    public void setApplicantInfos(List<ApplicantInfo> applicantInfos) {
        this.applicantInfos = applicantInfos;
    }

    public UlInfo getUlInfo() {
        return ulInfo;
    }

    public void setUlInfo(UlInfo ulInfo) {
        this.ulInfo = ulInfo;
    }

    public IpInfo getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(IpInfo ipInfo) {
        this.ipInfo = ipInfo;
    }
}
