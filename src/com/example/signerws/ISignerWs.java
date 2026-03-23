package com.example.signerws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ISignerWs {

    @WebMethod
    byte[] signDocument(byte[] bytes) throws Exception;

    @WebMethod
    byte[] signReport(byte[] bytes) throws Exception;
}