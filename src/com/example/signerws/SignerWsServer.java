package com.example.signerws;

import javax.xml.ws.Endpoint;

public class SignerWsServer {

    public static void main(String[] args) throws Exception {

        String url = "http://0.0.0.0:8080/signerws";

        Endpoint.publish(url, new SignerWsImpl());

        System.out.println("SignerWs started at " + url);
    }
}