package vn.com.cmc.signature;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestDigitalSignature {
	private static final Log logger = LogFactory.getLog(TestDigitalSignature.class);
	public static void testDigitalSignature() {
    	String xmlFilePath = "xml" + File.separator + "employeesalary.xml";
        String signedXmlFilePath = "xml" + File.separator + "digitallysignedEmpSal.xml";
        String privateKeyFilePath = "keys" + File.separator + "privatekey.key";
        String publicKeyFilePath = "keys" + File.separator + "publickey.key";
        XmlDigitalSignatureGenerator xmlSig = new XmlDigitalSignatureGenerator();
        xmlSig.generateXMLDigitalSignature(xmlFilePath, signedXmlFilePath, privateKeyFilePath, publicKeyFilePath);
    }
    
    public static void testAllInOne() {
    	 //Generate the keys
        String keysDirPath = "keys";
        KryptoUtil util = new KryptoUtil();
        util.storeKeyPairs(keysDirPath);
        System.out.println("Private and Public Keys generated successfully ...");
        //Sign the XML Dcouments
        String xmlFilePath = "xml" + File.separator + "employeesalary.xml";
        String signedXmlFilePath = "xml" + File.separator + "digitallysignedEmpSal.xml";
        String privateKeyFilePath = "keys" + File.separator + "privatekey.key";
        String publicKeyFilePath = "keys" + File.separator + "publickey.key";
        XmlDigitalSignatureGenerator xmlSig = new XmlDigitalSignatureGenerator();
        xmlSig.generateXMLDigitalSignature(xmlFilePath, signedXmlFilePath, privateKeyFilePath, publicKeyFilePath);
        //Verify the signed XML Document
        try {
            boolean validFlag = XmlDigitalSignatureVerifier.
                    isXmlDigitalSignatureValid(signedXmlFilePath, publicKeyFilePath);
            System.out.println("Validity of XML Digital Signature : " + validFlag);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    
    /**
     * Method used to validate an actual signed XML document
     */
    public static void testSignedXMLDoc() {
        String signedXmlFilePath = "xml" + File.separator + "digitallysignedEmpSal.xml";
        String publicKeyFilePath = "keys" + File.separator + "publickey.key";
        try {
            boolean validFlag = XmlDigitalSignatureVerifier.
                    isXmlDigitalSignatureValid(signedXmlFilePath, publicKeyFilePath);
            System.out.println("Validity of XML Digital Signature : " + validFlag);
        } catch (Exception ex) {
        	logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Method used to validate a tampered signed XML document
     */
    public static void testSignedTamperedXMLDoc() {
        String signedXmlFilePath = "xml" + File.separator + "digitallytamperdEmpSal.xml";
        String publicKeyFilePath = "keys" + File.separator + "publickey.key";
        try {
            boolean validFlag = XmlDigitalSignatureVerifier.
                    isXmlDigitalSignatureValid(signedXmlFilePath, publicKeyFilePath);
            System.out.println("Validity of XML Digital Signature : " + validFlag);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    public static void testGenerateKeys() {
    	 String keysDirPath = "keys";
         KryptoUtil util = new KryptoUtil();
         util.storeKeyPairs(keysDirPath);
         System.out.println("Private and Public Keys generated successfully ...");
    }
}
