package cic.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

public class FTPUtils {
	private static final Logger logger = LogManager.getLogger(FTPUtils.class);

	public static void storeFile(FtpInf ftpInf, InputStream inputStream, String directory, String remoteFile)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			boolean login = ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			logger.info(String.format("Thong tin ket noi FTP %s :: host: %s, port: %s, user: %s, pass: %s",
					new Object[] { login, ftpInf.getUser(), ftpInf.getPass(), ftpInf.getUser(), ftpInf.getPass() }));
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// logger.info("Working directory: " + ftpClient.printWorkingDirectory());
			// ftpClient.changeWorkingDirectory("/");
			boolean result = false;
			boolean bChkExistPath = ftpClient.changeWorkingDirectory(directory);
			// logger.info("Working directory: " + ftpClient.printWorkingDirectory());
			if (!bChkExistPath) {
				makeDirectory(ftpClient, directory);
				result = ftpClient.storeFile(remoteFile, inputStream);

			} else {
				result = ftpClient.storeFile(remoteFile, inputStream);
			}
			logger.info(result);
			if (!result)
				throw new Exception("store file to FTP server fail");

		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error(e);
			}
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
	}

	public static void storeFiles(FtpInf ftpInf, InputStream[] inputStreams, String directory, String[] remoteFileNames)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			boolean bChkExistPath = ftpClient.changeWorkingDirectory(directory);
			if (!bChkExistPath)
				makeDirectory(ftpClient, directory);
			for (int i = 0; i < inputStreams.length; i++) {
				boolean result = ftpClient.storeFile(remoteFileNames[i], inputStreams[i]);
				inputStreams[i].close();
				if (!result) {
					logger.error(
							String.format("store file %s to FTP server fail", new Object[] { remoteFileNames[i] }));

					throw new Exception(
							String.format("store file %s to FTP server fail", new Object[] { remoteFileNames[i] }));
				}
			}

		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
	}

	/**
	 * Tao moi va chuyen den thu muc vua tao
	 * 
	 * @param ftpClient
	 * @param directory
	 * @throws IOException
	 */
	private static void makeDirectory(FTPClient ftpClient, String directory) throws IOException {
		String[] fldPadd = directory.split("/");
		for (String s : fldPadd) {
			boolean executeOk = false;
			executeOk = ftpClient.changeWorkingDirectory(s);
			if (!executeOk) {
				executeOk = ftpClient.makeDirectory(s);
				executeOk = ftpClient.changeWorkingDirectory(s);
			}

		}

	}

	public static void downloadFile(FtpInf ftpInf, String localFilePath, String remoteFilePath) throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
			boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
			outputStream.close();
			if (!success)
				logger.info("Download file not sucess!");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}

	}

	public static void copyFileBeetWeen2Server(FtpInf svSource, String soureFullPathFile, FtpInf svDes,
			String desFullPathFile) throws IOException {
		try {
			FTPClient ftpClientSource = svSource.start();
			InputStream inputStream = ftpClientSource.retrieveFileStream(soureFullPathFile);
			FTPClient ftpClientDes = svDes.start();
			int sparateFileIdx = desFullPathFile.lastIndexOf("/");
			String desDirectory = desFullPathFile.substring(0, sparateFileIdx);
			String desFile = desFullPathFile.substring(sparateFileIdx + 1);
			boolean bChkExistPath = ftpClientDes.changeWorkingDirectory(desDirectory);
			boolean result = false;
			if (!bChkExistPath) {
				makeDirectory(ftpClientDes, desDirectory);
				result = ftpClientDes.storeFile(desFile, inputStream);
			} else {
				result = ftpClientDes.storeFile(desFile, inputStream);
			}
			if (!result) {
				logger.error("Khong chuyen duoc file giua 2 server");
				throw new Exception("Khong chuyen duoc file giua 2 server");
			}
		} catch (Exception e) {
			try {
				svSource.end();
			} finally {
				svDes.end();
			}

		}

	}

	public static boolean existFile(FtpInf ftpInf, String filePath) throws Exception {
		logger.info("nvhuy :: existFile :: " + filePath);
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			logger.info("nvhuy :: connect :: " + ftpInf.getHost() + " port::" + ftpInf.getPort());
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			logger.info("nvhuy :: login :: " + ftpInf.getUser() + "::" + ftpInf.getPass());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			logger.info("nvhuy :: add file :: ");
			FTPFile[] remoteFiles = ftpClient.listFiles(filePath);
			logger.info("nvhuy :: end add file :: " + remoteFiles.length);
			if (remoteFiles == null || remoteFiles.length == 0)
				return false;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
		return true;

	}

	public static void downloadFile(FtpInf ftpInf, OutputStream outputStream, String filePath) throws Exception {

		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean success = ftpClient.retrieveFile(filePath, outputStream);
			outputStream.close();
			if (!success)
				logger.info("Download file not sucess!");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}

	}
	public static byte[] downloadFile2(FtpInf ftpInf, String filePath) throws Exception {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean success = ftpClient.retrieveFile(filePath, s);
			if (!success)
				logger.info("Download file not sucess!");
			s.close();
			return s.toByteArray();
			
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}

	}
	public static FTPFile[] getFileByPath(FtpInf ftpInf, String filePath) throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		FTPFile[] remoteFiles = null;
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			remoteFiles = ftpClient.listFiles(filePath);
			if (remoteFiles == null || remoteFiles.length == 0)
				return null;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
		return remoteFiles;

	}

	public static List<String> chkMultiExistFile(FtpInf ftpInf, Collection<String> lstFilePath) throws Exception {
		List<String> lstRs = new ArrayList<String>();
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			for (String filePath : lstFilePath) {
				FTPFile[] remoteFiles = ftpClient.listFiles(filePath);
				if (remoteFiles != null && remoteFiles.length > 0)
					lstRs.add(filePath);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
		return lstRs;
	}

	public static boolean makeDirectoryByPath(FtpInf ftpInf, String directory) throws IOException {
		FTPClient ftpClient = new FTPClient();
		boolean rs = false;
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// ftpClient.changeWorkingDirectory("/");
			rs = ftpClient.makeDirectory(directory);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}

		return rs;
	}
}
