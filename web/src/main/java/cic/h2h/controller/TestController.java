package cic.h2h.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.net.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cic.h2h.dao.hibernate.RpSumDao;
import cic.ws.client.WsClient;
import cic.ws.model.TepBaoCaoTinDung;
import common.util.Base64Utils;
import common.util.Formater;
import common.util.ResourceException;
import entity.RpSum;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.form.LoginForm;
import frwk.utils.ApplicationContext;
import intergration.cic.Report;
import vn.org.cic.h2h.ws.endpoint.cicreport.PHTepBaoCao;
import vn.org.cic.h2h.ws.endpoint.cicreport.TepBaoCao;

@Controller
@RequestMapping("/test")
public class TestController extends CommonController<LoginForm, SysUsers> {
	private static Logger lg = LogManager.getLogger(TestController.class);

	@Override
	public String getJsp() {
		return null;
	}

	@Autowired
	private Report report;
	
	@Autowired
	private RpSumDao rpSumDao;

	@RequestMapping(value = "upload", method = { RequestMethod.POST })
	public void sendReport(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		System.out.print("start upload file");
		lg.info("start upload file");
		String _result = "";
		try {
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (appContext == null)
				throw new ResourceException("Can dang nhap truoc");
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user.getUsername() + " g&#7917;i t&#7879;p " + fileName);
			TepBaoCao tep = new TepBaoCao();
			tep.setTenTep(fileName);
			if (!inputFile.isEmpty()) {
				System.out.print("start load file");
				lg.info("start load file");
				String noiDungTep = loadFile(inputFile);
				tep.setNoiDungTep(noiDungTep);
				System.out.print("end load file");
				lg.info("end load file");
			} else
				tep.setNoiDungTep("");
			RpSum rpSum = new RpSum();
			rpSum.setUserReport(user.getUsername());
			//rpSum.setReportCode(reportCode);
			rpSum.setFileName(tep.getTenTep());
			rpSum.setReportDate(Calendar.getInstance().getTime());
			rpSum.setCreatedDate(Calendar.getInstance().getTime());
			rpSum.setCreateBy(user.getUsername());
			//rpSum.setBranch(user.getCompanyId());
			rpSum.setFileContent(Base64.decodeBase64(tep.getNoiDungTep()));
			rpSum.setFileSize(BigDecimal.valueOf(rpSum.getFileContent().length).divide(BigDecimal.valueOf(1024), 5,
					RoundingMode.HALF_UP));
			rpSum.setAttachmentName(tep.getTenTepDinhKem());
			PHTepBaoCao ph = report.tepBaoCao(tep);

			if(ph != null) {
				rpSum.setErrorCode(ph.getTTPhanHoi().getMa());
				rpSum.setErrorDes(ph.getTTPhanHoi().getMoTa());
				_result = "Th&#7921;c hi&#7879;n th&#224;nh c&#244;ng";
			}else
				_result = "L&#7895;i khi call CIC";
			rpSumDao.save(rpSum);
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();

			StringWriter sw = new StringWriter();
			//JAXB.marshal(ph, sw);
			//String xmlString01 = sw.toString();
			//xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
			//lg.info(xmlString01);
			pw.print(_result);
			pw.flush();
			pw.close();

		} catch (ResourceException ex) {
			lg.error("Loi", ex);
			if (!Formater.isNull(ex.getMessage())) {
				rs.setContentType("text/plan;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				if (!Formater.isNull(ex.getParam()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParam()));
				else if (Formater.isNull(ex.getParams()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParams()));
				else
					pw.print(getText(ex.getMessage()));
				pw.flush();
				pw.close();
			}
			throw ex;
		} catch (Exception ex) {
			lg.error(ex);
			throw ex;
		}

	}

	@RequestMapping(value = "uploadK31K32", method = { RequestMethod.POST })
	public void sendReportK31K32(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("fileNameK31") String fileNameK31, @RequestParam("inputFileK31") MultipartFile inputFileK31,
			@RequestParam("fileNameK32") String fileNameK32, @RequestParam("inputFileK32") MultipartFile inputFileK32)
			throws Exception {
		System.out.print("start upload file K31 K32");
		lg.info("start upload file K31 K32");
		try {
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (appContext == null) {
				throw new ResourceException("Can dang nhap truoc");

			}
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user.getUsername() + " g&#7917;i t&#7879;p k31" + fileNameK31);
			lg.info(user.getUsername() + " g&#7917;i t&#7879;p k32" + fileNameK32);
			TepBaoCaoTinDung tep = new TepBaoCaoTinDung();

			tep.setNguoiYC(user.getUsername());
			tep.setTenTepK31(fileNameK31);
			tep.setTenTepK32(fileNameK32);
			if (!inputFileK31.isEmpty() && !inputFileK32.isEmpty()) {
				System.out.print("start load file");
				lg.info("start load file");
				String noiDungTepK31 = loadFile(inputFileK31);
				tep.setNoiDungTepK31(noiDungTepK31);
				String noiDungTepK32 = loadFile(inputFileK32);
				tep.setNoiDungTepK32(noiDungTepK32);
				System.out.print("end load file");
				lg.info("end load file");
			} else
				tep.setNoiDungTepK31("");
			tep.setNoiDungTepK32("");
			PHTepBaoCao ph = null;//wsClient.sendReportK31K32(tep);

			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();

			StringWriter sw = new StringWriter();
			JAXB.marshal(ph, sw);
			String xmlString01 = sw.toString();
			xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
			lg.info(xmlString01);
			pw.print(xmlString01);
			pw.flush();
			pw.close();

		} catch (ResourceException ex) {
			lg.error("Loi K31 K32", ex);
			if (!Formater.isNull(ex.getMessage())) {
				rs.setContentType("text/plan;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				if (!Formater.isNull(ex.getParam()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParam()));
				else if (Formater.isNull(ex.getParams()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParams()));
				else
					pw.print(getText(ex.getMessage()));
				pw.flush();
				pw.close();
			}
			throw ex;
		} catch (Exception ex) {
			lg.error(ex);
			throw ex;
		}

	}

	@RequestMapping(value = "uploadAtt", method = { RequestMethod.POST })
	public void sendReport(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName,
			@RequestParam("attInputFile") MultipartFile attInputFile, @RequestParam("attFileName") String attFileName)
			throws Exception {

		try {
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (appContext == null) {
				throw new ResourceException("Can dang nhap truoc");

			}
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user.getUsername() + " g&#7917;i t&#7879;p " + fileName);
			TepBaoCao tep = new TepBaoCao();
			tep.setTenTep(fileName);
			lg.info("BEGIN upload file" + fileName);
			tep.setNoiDungTep(loadFile(inputFile));
			if (!attFileName.equals("undefined")) {
				tep.setTenTepDinhKem(attFileName);
				tep.setNoiDungTepDinhKem(loadFile(attInputFile));
			}
			lg.info("End upload file" + fileName);
			lg.info("BEGIN send report file " + fileName);
			PHTepBaoCao ph = report.tepBaoCao(tep);
			lg.info("End send report file " + fileName);
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			StringWriter sw = new StringWriter();
			JAXB.marshal(ph, sw);
			String xmlString01 = sw.toString();
			xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
			lg.info(xmlString01);
			pw.print(xmlString01);
			pw.flush();
			pw.close();

		} catch (ResourceException ex) {
			lg.error("Loi", ex);
			if (!Formater.isNull(ex.getMessage())) {
				rs.setContentType("text/plan;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				if (!Formater.isNull(ex.getParam()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParam()));
				else if (Formater.isNull(ex.getParams()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParams()));
				else
					pw.print(getText(ex.getMessage()));
				pw.flush();
				pw.close();
			}
			throw ex;
		} catch (Exception ex) {
			lg.error(ex);
			throw ex;
		}

	}

	private String loadFile(MultipartFile inputFile) throws IOException {
		InputStream inputStream = new BufferedInputStream(inputFile.getInputStream());
		return Base64Utils.encodeFile(inputStream);
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {

	}
}
