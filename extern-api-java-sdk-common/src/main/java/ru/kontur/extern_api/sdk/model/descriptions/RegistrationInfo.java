package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.List;

public class RegistrationInfo {

    private List<ApplicantInfo> applicantInfos;

    private BusinessType businessType;

    private UlInfo ulInfo;

    private IpInfo ipInfo;

    private ApplicationCode applicationCode;

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

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
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
