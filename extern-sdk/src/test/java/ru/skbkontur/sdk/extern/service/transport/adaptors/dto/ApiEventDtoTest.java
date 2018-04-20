/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.Date;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlexS
 */
public class ApiEventDtoTest {
    private static final String L_HREF = "href";
    private static final String L_REL = "rel";
    private static final String L_NAME = "name";
    private static final String L_TITLE = "title";
    private static final String L_PROFILE = "profile";
    private static final Boolean L_TEMPLATED = true;
    
    private static final String D_TYPE = "docflowType";
    private static final DateTime E_DATE_TIME = new DateTime();
    private static final String E_ID = "id";
    private static final String E_INN = "7810123456";
    private static final String E_KPP = "781001001";
    private static final String E_NEW_STATE = "new:state";
    

    @Test
    public void testFromDto() {
        System.out.println("fromDto");
        ru.skbkontur.sdk.extern.service.transport.swagger.model.ApiEvent dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.ApiEvent();
//<editor-fold defaultstate="collapsed" desc="test">        
        dto.setDocflowLink(buildLinkDto());
        dto.setDocflowType(D_TYPE);
        dto.setEventDateTime(E_DATE_TIME);
        dto.setId(E_ID);
        dto.setInn(E_INN);
        dto.setKpp(E_KPP);
        dto.setNewState(E_NEW_STATE);
//</editor-fold>        
        ApiEventDto instance = new ApiEventDto();
        ru.skbkontur.sdk.extern.model.ApiEvent result = instance.fromDto(dto);
        
        assertEquals(result.getDocflowType(),D_TYPE);
        assertEquals(result.getEventDateTime(), new Date(E_DATE_TIME.getMillis()));
        assertEquals(result.getId(), E_ID);
        assertEquals(result.getInn(), E_INN);
        assertEquals(result.getKpp(), E_KPP);
        assertEquals(result.getNewState(), E_NEW_STATE);
        
        assertEquals(result.getDocflowLink().getHref(), L_HREF);
        assertEquals(result.getDocflowLink().getName(), L_NAME);
        assertEquals(result.getDocflowLink().getProfile(), L_PROFILE);
        assertEquals(result.getDocflowLink().getRel(), L_REL);
        assertEquals(result.getDocflowLink().getTemplated(), L_TEMPLATED);
        assertEquals(result.getDocflowLink().getTitle(), L_TITLE);
    }

    @Test
    public void testToDto() {
        System.out.println("toDto");
        ru.skbkontur.sdk.extern.model.ApiEvent ae = new ru.skbkontur.sdk.extern.model.ApiEvent();
        
        LinkBuilder lb = new LinkBuilder();
        lb.setHref(L_HREF);
        lb.setName(L_NAME);
        lb.setProfile(L_PROFILE);
        lb.setRel(L_REL);
        lb.setTemplated(L_TEMPLATED);
        lb.setTitle(L_TITLE);
        ru.skbkontur.sdk.extern.model.Link docflowLink = lb.buildFromDto();
        
        ae.setDocflowLink(docflowLink);
        ae.setDocflowType(D_TYPE);
        ae.setEventDateTime(new Date(E_DATE_TIME.getMillis()));
        ae.setId(E_ID);
        ae.setInn(E_INN);
        ae.setKpp(E_KPP);
        ae.setNewState(E_NEW_STATE);
        
        ApiEventDto instance = new ApiEventDto();
        ru.skbkontur.sdk.extern.service.transport.swagger.model.ApiEvent result = instance.toDto(ae);
        
        assertEquals(result.getDocflowType(),D_TYPE);
        assertEquals(result.getEventDateTime(), E_DATE_TIME);
        assertEquals(result.getId(), E_ID);
        assertEquals(result.getInn(), E_INN);
        assertEquals(result.getKpp(), E_KPP);
        assertEquals(result.getNewState(), E_NEW_STATE);
        
        assertEquals(result.getDocflowLink().getHref(), L_HREF);
        assertEquals(result.getDocflowLink().getName(), L_NAME);
        assertEquals(result.getDocflowLink().getProfile(), L_PROFILE);
        assertEquals(result.getDocflowLink().getRel(), L_REL);
        assertEquals(result.getDocflowLink().getTemplated(), L_TEMPLATED);
        assertEquals(result.getDocflowLink().getTitle(), L_TITLE);
    }
    
    private static ru.skbkontur.sdk.extern.service.transport.swagger.model.Link buildLinkDto() {
        LinkBuilder lb = new LinkBuilder();
        lb.setHref(L_HREF);
        lb.setName(L_NAME);
        lb.setProfile(L_PROFILE);
        lb.setRel(L_REL);
        lb.setTemplated(L_TEMPLATED);
        lb.setTitle(L_TITLE);
        return lb.buildToDto();
    }

    private static ru.skbkontur.sdk.extern.model.Link buildLinkSDK() {
        LinkBuilder lb = new LinkBuilder();
        lb.setHref(L_HREF);
        lb.setName(L_NAME);
        lb.setProfile(L_PROFILE);
        lb.setRel(L_REL);
        lb.setTemplated(L_TEMPLATED);
        lb.setTitle(L_TITLE);
        return lb.buildFromDto();
    }

    public static ru.skbkontur.sdk.extern.service.transport.swagger.model.ApiEvent buildApiEventDto() {
        ru.skbkontur.sdk.extern.service.transport.swagger.model.ApiEvent dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.ApiEvent();
        
        dto.setDocflowLink(buildLinkDto());
        dto.setDocflowType(D_TYPE);
        dto.setEventDateTime(E_DATE_TIME);
        dto.setId(E_ID);
        dto.setInn(E_INN);
        dto.setKpp(E_KPP);
        dto.setNewState(E_NEW_STATE);
        
        return dto;
    }

    public static ru.skbkontur.sdk.extern.model.ApiEvent buildApiEventSDK() {
        ru.skbkontur.sdk.extern.model.ApiEvent sdk = new ru.skbkontur.sdk.extern.model.ApiEvent();
        
        sdk.setDocflowLink(buildLinkSDK());
        sdk.setDocflowType(D_TYPE);
        sdk.setEventDateTime(new Date(E_DATE_TIME.getMillis()));
        sdk.setId(E_ID);
        sdk.setInn(E_INN);
        sdk.setKpp(E_KPP);
        sdk.setNewState(E_NEW_STATE);
        
        return sdk;
    }
}
