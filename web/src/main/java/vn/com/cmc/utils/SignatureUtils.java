//package vn.com.cmc.utils;
//
//import java.io.StringReader;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathFactory;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.xml.sax.InputSource;
//
//import vn.com.cmc.service.SysUsersServiceImpl;
//
//public class SignatureUtils {
//	private static final Logger logger = LogManager.getLogger(SignatureUtils.class);
//
//	public static boolean verifySign(String _input) throws Exception {
//		try {
//			javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
//			dbf.setIgnoringElementContentWhitespace(true);
//			dbf.setNamespaceAware(true);
//			// dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
//			// javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
//
//			// dbf.setValidating(false);
//			// dbf.setFeature("http://xml.org/sax/features/namespaces", false);
//			// dbf.setFeature("http://xml.org/sax/features/validation", false);
//			// dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
//			// false);
//			// dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
//			// false);
//			//
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			// javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
//			// db.setEntityResolver(null);
//			// db.setEntityResolver(new EntityResolver() {
//			// // Dummi resolver - alvays do nothing
//			// public InputSource resolveEntity(String publicId, String systemId) throws
//			// SAXException, IOException {
//			// return new InputSource(new StringReader(""));
//			// }
//			// });
//
//			// db.setErrorHandler(new
//			// org.apache.xml.security.utils.IgnoreAllErrorHandler());
//			InputSource is = new InputSource(new StringReader(_input));
//			org.w3c.dom.Document doc = db.parse(is);
//			String publicKey = null;
//			try {
//				publicKey = doc.getElementsByTagName("X509Certificate").item(0).getTextContent().replace("\n", "")
//						.replace("\r", "");
//			} catch (Exception ex) {
//				try {
//					publicKey = _input.split("<ds:X509Certificate>")[1].split("</ds:X509Certificate>")[0]
//							.replace("\n", "").replace("\r", "");
//				} catch (Exception ex2) {
//					logger.error("Loi", ex2);
//				}
//
//			}
//			logger.info("publicKey: " + publicKey);
//			logger.info("SystemCache.getPublicKey(alias): " + SystemCache.getPublicKey(alias));
//
//			try {
//				XPathFactory xpf = XPathFactory.newInstance();
//				XPath xpath = xpf.newXPath();
//				xpath.setNamespaceContext(new DSNamespaceContext());
//				String expression = "//ds:Signature[1]";
//				Element sigElement = (Element) xpath.evaluate(expression, doc, XPathConstants.NODE);
//				XMLSignature signature = new XMLSignature(sigElement, "");
//				signature.addResourceResolver(new OfflineResolver());
//				KeyInfo ki = signature.getKeyInfo();
//				if (ki.containsX509Data()) {
//					System.out.println("Could find a X509Data element in the KeyInfo");
//				}
//				X509Certificate cert = signature.getKeyInfo().getX509Certificate();
//				if (cert != null)
//					return signature.checkSignatureValue(cert);
//				else {
//					System.out.println("Did not find a Certificate");
//					PublicKey pk = signature.getKeyInfo().getPublicKey();
//					if (pk != null)
//						return signature.checkSignatureValue(pk);
//				}
//				return true;
//			} catch (Exception ex) {
//				logger.error(AdapterLog4jFactory.LOG_EROR, ex);
//				return false;
//			}
//		} catch (Exception ex) {
//			logger.error("verifySign: " + ex.toString());
//			return false;
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param data
//	 * @param fileName
//	 * @return
//	 * @throws Exception
//	 */
//	public static String sign(String data, String fileName) throws Exception {
//		ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "");
//		String keystoreType = "JKS";
//
//		String keystoreFile = ExchangeContext.DEPLOY_PARTNER
//				? ExchangeContext.SIGN_KEY_FOLDER + ExchangeContext.ADAPTER_PRIVATE_KEY_FILE_INF + ".jks"
//				: "";
//		String keystorePass = ExchangeContext.DEPLOY_PARTNER ? ExchangeContext.ADAPTER_PRIVATE_KEY_PASS : "";
//		String privateKeyAlias = ExchangeContext.DEPLOY_PARTNER ? ExchangeContext.ADAPTER_PRIVATE_KEY_FILE_INF : "";
//		String privateKeyPass = ExchangeContext.DEPLOY_PARTNER ? ExchangeContext.ADAPTER_PRIVATE_KEY_PASS : "";
//		String certificateAlias = ExchangeContext.DEPLOY_PARTNER ? ExchangeContext.ADAPTER_PRIVATE_KEY_FILE_INF : "";
//
//		KeyStore ks = KeyStore.getInstance(keystoreType);
//		FileInputStream fis = new FileInputStream(keystoreFile);
//
//		// load keystore file
//		ks.load(fis, keystorePass.toCharArray());
//		// get the private key for signing.
//		PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias, privateKeyPass.toCharArray());
//		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
//		// XML Signature needs to be namespace aware
//		// javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
//
//		// dbf.setFeature("http://xml.org/sax/features/namespaces", false);
//		// dbf.setFeature("http://xml.org/sax/features/validation", false);
//		// dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
//		// false);
//		// dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
//		// false);
//		dbf.setNamespaceAware(true);
//		dbf.setIgnoringElementContentWhitespace(true);
//		dbf.setValidating(false);
//
//		DocumentBuilder db = dbf.newDocumentBuilder();
//
//		// Disable loading of external Entityes
//		// db.setEntityResolver(null);
//		db.setEntityResolver(new EntityResolver() {
//			// Dummi resolver - always do nothing
//
//			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//				return new InputSource(new StringReader(""));
//			}
//		});
//
//		org.w3c.dom.Document doc = db.newDocument();
//		Element root = doc.createElement("DOCUMENT");
//		doc.appendChild(root);
//
//		Element msg = doc.createElement("DATA");
//		/* 161004 danh dau du lieu nam trong CDATA */
//		msg.appendChild(doc.createCDATASection(data));
//		// msg.appendChild(doc.createTextNode(data));
//
//		root.appendChild(msg);
//
//		XMLSignature sig = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_RSA);
//		root.appendChild(sig.getElement());
//		sig.getSignedInfo().addResourceResolver(new OfflineResolver());
//		{
//			// create the transforms object for the Document/Reference
//			Transforms transforms = new Transforms(doc);
//
//			// First we have to strip away the signature element (it's not part of the
//			// signature calculations). The enveloped transform can be used for this.
//			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
//			// Part of the signature element needs to be canonicalized. It is a kind
//			// of normalizing algorithm for XML. For more information please take a
//			// Add the above Document/Reference
//			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
//		}
//		{
//			// Add in the KeyInfo for the certificate that we used the private key of
//			X509Certificate cert = (X509Certificate) ks.getCertificate(certificateAlias);
//			sig.addKeyInfo(cert);
//			sig.addKeyInfo(cert.getPublicKey());
//			sig.sign(privateKey);
//		}
//		// Bo sung the filetype
//
//		// Serialize DOM
//		OutputFormat format = new OutputFormat(doc);
//		// as a String
//		StringWriter stringOut = new StringWriter();
//		XMLSerializer serial = new XMLSerializer(stringOut, format);
//		serial.serialize(doc);
//		// Display the XML
//		return stringOut.toString();
//	}
//
//}
