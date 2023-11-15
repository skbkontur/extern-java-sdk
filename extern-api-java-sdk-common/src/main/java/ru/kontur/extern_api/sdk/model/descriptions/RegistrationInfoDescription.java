package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.List;

public class RegistrationInfoDescription {

    private List<ApplicantInfo> applicantInfos;

    private BusinessType businessType;

    private UlInfoDescription ulInfo;

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

    public UlInfoDescription getUlInfo() {
        return ulInfo;
    }

    public void setUlInfo(UlInfoDescription ulInfo) {
        this.ulInfo = ulInfo;
    }
}
