package vn.com.cmc.signature;

import java.io.File;
import java.security.PublicKey;
import java.util.Iterator;

import javax.xml.bind.JAXB;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SoapDigitalSignatureVerifier {
	
	public static Boolean isSoapDigitalSignatureValid(Object request, MessageContext messageContext, Class<?> c, String publickeyPath) throws Exception {
		Boolean validFlag = false;
		SaajSoapMessage soapRequest = (SaajSoapMessage) messageContext.getRequest();
		SoapHeader soapHeader = soapRequest.getSoapHeader();

		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		File file = new File("abc.xml");
		JAXB.marshal(request, file);
		doc = dbf.newDocumentBuilder().parse(file);
		
		NodeList bodyNodeList = doc.getElementsByTagName(c.getSimpleName());
		if (bodyNodeList.getLength() == 0) {
            throw new Exception("No XML Body Found, document is discarded");
        }
		Iterator<SoapHeaderElement> itr = soapHeader.examineAllHeaderElements();
//		PublicKey publicKey = new KryptoUtil().getStoredPublicKey("src/main/resources/publickey.key");
//		String publicKeyFilePath = "src/main/webapp/signature/publickey.key";
		PublicKey publicKey = new KryptoUtil().getStoredPublicKey(publickeyPath);
		while (itr.hasNext()) {
			SoapHeaderElement ele = itr.next();
			DOMSource bodyDomSource = (DOMSource) ele.getSource();
			Node bodyNode = bodyDomSource.getNode();
			Node importedNode = doc.importNode(bodyNode, true);
			bodyNodeList.item(0).appendChild(importedNode);

//			TransformerFactory tf = TransformerFactory.newInstance();
//			Transformer t = tf.newTransformer();
//			StreamResult result = new StreamResult(System.out);
//			t.transform(new DOMSource(doc), result);

			NodeList signNodeList = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
			if (signNodeList.getLength() == 0) {
	            throw new Exception("No XML Digital Signature Found, document is discarded");
	        }
			DOMValidateContext valContext = new DOMValidateContext(publicKey, signNodeList.item(0));
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
			XMLSignature signature = fac.unmarshalXMLSignature(valContext);
			validFlag = signature.validate(valContext);
			System.out.println(validFlag);
		}
		return validFlag;
	}
}
