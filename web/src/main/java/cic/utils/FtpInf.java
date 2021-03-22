package cic.utils;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FtpInf {
	private static final Logger logger = LogManager.getLogger(FtpInf.class);
	private String host;
	private int port;
	private String user;
	private String pass;

	public FtpInf(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	FTPClient ftpClient = null;

	public FTPClient start() throws Exception {
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(this.host, this.port);
			ftpClient.login(this.user, this.pass);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (Exception e) {
			end();
			throw e;
		}
		ftpClient.enterLocalPassiveMode();
		return ftpClient;
	}

	public void end() throws IOException {
		try {
			if (ftpClient.isConnected()) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (IOException ex) {
			logger.error(ex);
			throw ex;
		}
	}
}
