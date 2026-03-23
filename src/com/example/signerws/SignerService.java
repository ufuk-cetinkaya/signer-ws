package com.example.signerws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import tr.gov.tubitak.uekae.esya.api.signature.util.PfxSigner;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

public class SignerService {

    private final Context context;
    private final PfxSigner pfxSigner;

    public SignerService(String licencePath, String signConfig, String certPath, String certPass) throws Exception {
        try (FileInputStream licence = new FileInputStream(licencePath)) {
            LicenseUtil.setLicenseXml(licence);
        }
        context = new Context();
        context.setConfig(new Config(signConfig));
        pfxSigner = new PfxSigner(SignatureAlg.ECDSA_SHA384, certPath, certPass.toCharArray());
    }

    public byte[] xadesBes(byte[] bytes) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.parse(new ByteArrayInputStream(bytes));
        context.setDocument(xml);
        Node node = xml.getElementsByTagNameNS(
            "urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2",
            "ExtensionContent"
        ).item(0);
        while (node.hasChildNodes()) {
            node.removeChild(node.getFirstChild());
        }
        XMLSignature signature = new XMLSignature(context, false);
        node.appendChild(signature.getElement());
        Transforms transforms = new Transforms(context);
        Transform transform = new Transform(context, TransformType.ENVELOPED.getUrl());
        transforms.addTransform(transform);
        signature.addDocument("", "text/xml", transforms, DigestMethod.SHA_384, false);
        signature.setSignatureMethod(SignatureMethod.ECDSA_SHA384);
        signature.getQualifyingProperties().getSignedSignatureProperties().setSigningTime(getTime());
        ECertificate cert = pfxSigner.getSignersCertificate();
        signature.addKeyInfo(cert);
        signature.sign(pfxSigner);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(xml), new StreamResult(baos));
        return baos.toByteArray();
    }

    public byte[] xadesA(byte[] bytes) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.parse(new ByteArrayInputStream(bytes));
        context.setDocument(xml);
        XMLSignature signature = new XMLSignature(context, false);
        Node node = xml.getDocumentElement().getFirstChild();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        node.appendChild(signature.getElement());
        Transforms transforms = new Transforms(context);
        Transform transform = new Transform(context, TransformType.ENVELOPED.getUrl());
        transforms.addTransform(transform);
        signature.addDocument("", "text/xml", transforms, DigestMethod.SHA_384, false);
        signature.setSignatureMethod(SignatureMethod.ECDSA_SHA384);
        signature.getQualifyingProperties().getSignedSignatureProperties().setSigningTime(getTime());
        ECertificate cert = pfxSigner.getSignersCertificate();
        signature.addKeyInfo(cert);
        signature.sign(pfxSigner);
        try {
            signature.upgrade(SignatureType.ES_A);
        } catch (Exception e) {
            System.out.println("Signature upgrade başarısız: " + e.getMessage());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(xml), new StreamResult(baos));
        return baos.toByteArray();
    }

     private static XMLGregorianCalendar getTime() {
        Calendar cal = new GregorianCalendar();
        cal.get(Calendar.SECOND);
        return XmlUtil.createDate(cal);
    }
}