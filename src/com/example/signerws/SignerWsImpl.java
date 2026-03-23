package com.example.signerws;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.jws.WebService;

@WebService(endpointInterface = "com.example.signerws.ISignerWs")
public class SignerWsImpl implements ISignerWs {

    private final SignerService _signer;

    public SignerWsImpl()  throws Exception {
        
        Properties props = new Properties();
        try (InputStream is = new FileInputStream("config.properties")) {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("config.properties yüklenemedi", e);
        }
        String licencePath = props.getProperty("LicencePath");
        String configPath = props.getProperty("ConfigPath");
        String certPath = props.getProperty("CertPath");
        String certPass = props.getProperty("CertPass");
        _signer = new SignerService(licencePath, configPath, certPath, certPass);
    }
    
    @Override
    public byte[] signDocument(byte[] bytes) throws Exception {

        return _signer.xadesBes(bytes);
    }

    @Override
    public byte[] signReport(byte[] bytes) throws Exception {

        return _signer.xadesA(bytes);
    }
}