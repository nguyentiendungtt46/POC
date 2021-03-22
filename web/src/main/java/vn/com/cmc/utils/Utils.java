package vn.com.cmc.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Utils {
	public static byte[] convertToBytes(Object object) throws IOException {
		String xmlString = null;
		try {
			xmlString = convertObjToXML(object, object.getClass());
		} catch (JAXBException e) {
			xmlString = "";
		}
		return xmlString.getBytes();
	}

	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getClientIpAddrBk(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * Checks if is collection empty.
	 *
	 * @param collection the collection
	 * @return true, if is collection empty
	 */
	private static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is object empty.
	 *
	 * @param object the object
	 * @return true, if is object empty
	 */
	public static boolean isObjectEmpty(Object object) {
		if (object == null)
			return true;
		else if (object instanceof String) {
			if (((String) object).trim().length() == 0) {
				return true;
			}
		} else if (object instanceof Collection) {
			return isCollectionEmpty((Collection<?>) object);
		}
		return false;
	}

	public static boolean isNullObject(Object obj1) {
		if (obj1 == null) {
			return true;
		}
		if (obj1 instanceof String) {
			return isNullOrEmpty(obj1.toString());
		}
		return false;
	}

	/**
	 * check null or empty Su dung ma nguon cua thu vien StringUtils trong apache
	 * common lang
	 *
	 * @param cs String
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNullOrEmpty(final Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isNullOrEmpty(final Object[] collection) {
		return collection == null || collection.length == 0;
	}

	public static boolean isNullOrEmpty(final Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	private static final double MEGABYTE = 1024 * 1024;

	public static double bytesToMeg(double bytes) {
		return bytes / MEGABYTE;
	}

	public static <T> String convertObjToXML(Object object, Class<T> objClass) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(objClass);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(object, sw);
		String xmlString = sw.toString();

		return xmlString;
	}

//	public static Sort buildSortNonConfig(PaginationCriteria pagination) {
//		Sort sort = null;
//		String field = "";
//		for (Map.Entry entry : pagination.getSortBy().getSortBys().entrySet()) {
//			field = entry.getKey().toString();
//			if (entry.getValue().toString().equals("ASC")) {
//				sort = Sort.by(Sort.Direction.ASC, field);
//			} else if (entry.getValue().toString().equals("DESC")) {
//				sort = Sort.by(Sort.Direction.DESC, field);
//			}
//		}
//		return sort;
//	}
//
//	public static Sort buildSortConfig(PaginationCriteria pagination, JsonObject config) {
//		Sort sort = null;
//		String field = "";
//		for (Map.Entry entry : pagination.getSortBy().getSortBys().entrySet()) {
//			field = config.get(entry.getKey().toString()).getAsString();
//			if (entry.getValue().toString().equals("ASC")) {
//				sort = Sort.by(Sort.Direction.ASC, field);
//			} else if (entry.getValue().toString().equals("DESC")) {
//				sort = Sort.by(Sort.Direction.DESC, field);
//			}
//		}
//		return sort;
//	}
//
//	public static String builderDataTable(DataTableRequest dataTableInRQ, Sort sort, PaginationCriteria pagination,
//			Page list) {
//		// DataTableRequest dataTableInRQ = new DataTableRequest(request);
//		String total = "";
//		List listInfo = new ArrayList<>();
//		if (list != null) {
//			total = String.valueOf(list.getTotalElements());
//			listInfo = list.getContent();
//		} else {
//			total = String.valueOf(0);
//		}
//		DataTableResults dataTableResult = new DataTableResults();
//		dataTableResult.setDraw(dataTableInRQ.getDraw());
//		dataTableResult.setListOfDataObjects(listInfo);
//		if (!Utils.isObjectEmpty(listInfo)) {
//			dataTableResult.setRecordsTotal(total);
//			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
//				dataTableResult.setRecordsFiltered(total);
//			} else {
//				dataTableResult.setRecordsFiltered(Integer.toString(listInfo.size()));
//			}
//		}
//
//		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
//		// System.out.println("listInfo: " + gson.toJson(dataTableResult));
//		return gson.toJson(dataTableResult);
//	}

	private static final Logger logger = LogManager.getLogger(Utils.class);

	public static String convertExceptionToString(Throwable th) {
		try {
			if (th == null)
				return null;
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			th.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	// Java method to create SHA-25 checksum
	public static String getSHA256Hash(String data) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			return bytesToHex(hash); // make it printable
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
//	            ex.printStackTrace();
		}
		return result;
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}
	
	public static String nodeListToString(NodeList nodes) throws TransformerException {
		DOMSource source = new DOMSource();
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//	    transformer.setOutputProperty(OutputKeys.INDENT, "no");

		for (int i = 0; i < nodes.getLength(); ++i) {
			// System.out.println("nodes.getLength(): " + nodes.getLength());
			source.setNode(nodes.item(i));
			transformer.transform(source, result);
			// System.out.println("writer.toString(): " + writer.toString());
		}

		return writer.toString();
	}
}
