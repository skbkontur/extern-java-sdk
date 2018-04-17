/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.Certificate;


/**
 * @author alexs
 */
public class CertificateDto {

    public Certificate fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateDto dto) {

        if (dto == null) return null;

        Certificate cert = new Certificate();

        cert.setContent(dto.getContent());
        cert.setFio(dto.getFio());
        cert.setInn(dto.getInn());
        cert.setIsCloud(dto.getIsCloud());
        cert.setIsQualified(dto.getIsQualified());
        cert.setIsValid(dto.getIsValid());
        cert.setKpp(dto.getKpp());

        return cert;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateDto toDto(Certificate cert) {

        if (cert == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateDto dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateDto();

        dto.setContent(cert.getContent());
        dto.setFio(cert.getFio());
        dto.setInn(cert.getInn());
        dto.setIsCloud(cert.getIsCloud());
        dto.setIsQualified(cert.getIsQualified());
        dto.setIsValid(cert.getIsValid());
        dto.setKpp(cert.getKpp());

        return dto;
    }
}
