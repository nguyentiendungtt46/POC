package cic.ws.client;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cic.utils.FtpInf;
import cic.utils.M1FtpContext;
import cic.utils.QnAFtpContext;
import entity.frwk.SysParam;
import frwk.dao.hibernate.sys.SysParamDao;
import cic.utils.ExchgFtpContext;
import cic.utils.FtpContext;

@Configuration
@ComponentScan({ "cic.*" })
public class FTPConfig {
	@Value("${H2H_CORE_FTP_ADD}")
	private String H2H_CORE_FTP_ADD;
	@Value("${H2H_CORE_FTP_AUTH}")
	private String H2H_CORE_FTP_AUTH;
	@Value("${FTP_RP_HOST}")
	private String FTP_RP_HOST;
	@Value("${FTP_RP_AUTH}")
	private String FTP_RP_AUTH;
	// FTP QNA
	@Value("${FTP_QNA_HOST}")
	private String FTP_QNA_HOST;
	@Value("${FTP_QNA_AUTH}")
	private String FTP_QNA_AUTH;
	// Thong tin cac thu muc goc FTP QNA
	@Value("${FTP_QNA_FL_IN_FLD}")
	/**
	 * Thu muc chua file cau hoi
	 */
	private String FTP_QNA_FL_IN_FLD;
	@Value("${FTP_QNA_FL_OUT_FLD}")
	/**
	 * Thu muc chua file tra loi
	 */
	private String FTP_QNA_FL_OUT_FLD;
	@Value("${FTP_QNA_FX_OUT_FLD}")
	/**
	 * Thu muc chua file tra loi dinh ky
	 */
	private String FTP_QNA_FX_OUT_FLD;
	
	
	
	@Value("${FTP_EXCHG_IN_FLD}")
	/**
	 * Thu muc chua file phi cau truc TCTD
	 */
	private String FTP_QNA_EXCHG_IN_FLD;
	@Value("${FTP_EXCHG_OUT_FLD}")
	/**
	 * Thu muc chua file phi cau truc CIC
	 */
	private String FTP_QNA_EXCHG_OUT_FLD;
	@Value("${FTP_RP_IN_FLD}")
	/**
	 * Thu muc FTP chua file tctd gui bao cao
	 */
	private String FTP_RP_IN_FLD;
	
	@Value("${FTP_RP_M1_FLD}")
	/**
	 * Thu muc FTP chua file tctd gui bao cao tren M1
	 */
	private String FTP_RP_M1_FLD;
	
	@Autowired
	SysParamDao sysParamDao;

	
	@Bean
	public M1FtpContext m1FtpContext() {
		return createM1FtpContext();
	}
	
	
	@Bean 
	public FtpContext ftpContext() {
		return createFtpContext();
	}
	@Bean
	public QnAFtpContext qnaFtpContext() {
		return createQNAFtpContext();
	}
	@Bean
	public ExchgFtpContext exchgFtpContext() {
		return createExchgFtpContext();
	}
	
	private M1FtpContext createM1FtpContext() {
		M1FtpContext m1FtpContext = new M1FtpContext();
		FtpInf ftpInf = createFtpInf(FTP_RP_HOST, FTP_RP_AUTH);
		m1FtpContext.setFtpInf(ftpInf);
		SysParam sysParam = sysParamDao.getSysParamByCode2(FTP_RP_M1_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				m1FtpContext.setFtpM1InfFld(sysParam.getValue());
			}
		}
		return m1FtpContext;
	}
	private QnAFtpContext createQNAFtpContext() {

		QnAFtpContext qnaFtpContext = new QnAFtpContext();
		qnaFtpContext.setFtpInf(createFtpInf(FTP_QNA_HOST, FTP_QNA_AUTH));
		SysParam sysParam = sysParamDao.getSysParamByCode2(FTP_QNA_FL_IN_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				qnaFtpContext.setFtpQnaInFld(sysParam.getValue());
			}
		}

		sysParam = sysParamDao.getSysParamByCode2(FTP_QNA_FL_OUT_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				qnaFtpContext.setFtpQnaOutFld(sysParam.getValue());
			}
		}

		sysParam = sysParamDao.getSysParamByCode2(FTP_QNA_FX_OUT_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				qnaFtpContext.setFtpQnaFxFld(sysParam.getValue());
			}
		}


		return qnaFtpContext;
	}
	private FtpContext createFtpContext() {
		FtpContext qnaFtpContext = new FtpContext();
		qnaFtpContext.setFtpInf(createFtpInf(H2H_CORE_FTP_ADD, H2H_CORE_FTP_AUTH));
		SysParam sysParam = sysParamDao.getSysParamByCode2(FTP_RP_IN_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				qnaFtpContext.setFtpRpInFld(sysParam.getValue());
			}
		}
		return qnaFtpContext;
	}

	public FtpInf createFtpInf(String FTP_RP_HOST, String FTP_RP_AUTH) {
		String host = null, user = null, pass = null;
		int port = 21;
		SysParam ftpHost = sysParamDao.getSysParamByCode2(FTP_RP_HOST);
		if (ftpHost != null) {
			if (ftpHost.getValue() != null && ftpHost.getValue().length() > 0) {
				String[] temp = ftpHost.getValue().trim().split(":");
				host = temp[0];
				if (temp.length >= 2)
					port = Integer.parseInt(temp[1]);
			}

		}
		SysParam ftpAuthen = sysParamDao.getSysParamByCode2(FTP_RP_AUTH);
		if (ftpAuthen != null) {
			if (ftpAuthen.getValue() != null && ftpAuthen.getValue().length() > 0) {
				String[] temp = ftpAuthen.getValue().trim().split("/");
				user = temp[0];
				if (temp.length >= 2)
					pass = temp[1];
			}

		}
		return new FtpInf(host, port, user, pass);
	}
	
	
	@Value("${email_username}")
	private String Email;
	
	@Value("${email_password}")
	private String Password;
	
	@Value("${email_host}")
	private String MAIL_SMTP;
	
	@Value("${email_protocol}")
	private String EMAIL_PROTOCOL;
	@Value("${FTP_EXCHG_IN_FLD}")
	/**
	 * Thu muc chua file phi cau truc TCTD
	 */
	private String FTP_EXCHG_IN_FLD;
	@Value("${FTP_EXCHG_OUT_FLD}")
	/**
	 * Thu muc chua file phi cau truc CIC
	 */
	private String FTP_EXCHG_OUT_FLD;
	@Value("${FTP_EXCHG_ADD}")
	/**
	 * Thu muc chua file phi cau truc CIC
	 */
	private String FTP_EXCHG_ADD;
	@Value("${FTP_EXCHG_AUTH}")
	/**
	 * Thu muc chua file phi cau truc CIC
	 */
	private String FTP_EXCHG_AUTH;
	
	
	
//	@Bean
//	public JavaMailSender getJavaMailSender() {
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		SysParam host = sysParamDao.getSysParamByCode2(MAIL_SMTP);
//		if (host != null) {
//			mailSender.setHost(host.getValue());
//		}
//		
//		mailSender.setPort(587);
//		SysParam email = sysParamDao.getSysParamByCode2(Email);
//		if (email != null) {
//			mailSender.setUsername(email.getValue());
//		}
//		SysParam password = sysParamDao.getSysParamByCode2(Password);
//		if (password != null) {
//			mailSender.setPassword(password.getValue());
//		}
//
//		Properties props = mailSender.getJavaMailProperties();
//		SysParam smtp = sysParamDao.getSysParamByCode2(EMAIL_PROTOCOL);
//		if (smtp != null) {
//			props.put("mail.transport.protocol", smtp.getValue());
//		}
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//
//		return mailSender;
//	}
	private ExchgFtpContext createExchgFtpContext() {

		ExchgFtpContext ftpContext = new ExchgFtpContext();
		ftpContext.setFtpInf(createFtpInf(FTP_EXCHG_ADD, FTP_EXCHG_AUTH));
		SysParam sysParam =  sysParamDao.getSysParamByCode2(FTP_EXCHG_IN_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				ftpContext.setFtpExchgInFld(sysParam.getValue());
			}
		}

		sysParam = sysParamDao.getSysParamByCode2(FTP_EXCHG_OUT_FLD);
		if (sysParam != null) {
			if (sysParam.getValue() != null && sysParam.getValue().length() > 0) {
				ftpContext.setFtpExchgOutFld(sysParam.getValue());
			}
		}

		return ftpContext;
	}
}
