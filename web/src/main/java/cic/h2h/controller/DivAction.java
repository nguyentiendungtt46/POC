package cic.h2h.controller;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.TemplatesDao;
import common.util.Formater;
import entity.Document;
import entity.Templates;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.form.ModelForm;
@Controller
@RequestMapping("/divAction")
public class DivAction extends CommonController {
	@Autowired
	private TemplatesDao templatesDao;
	public static DivAction make(Templates temp, Object profile, String recordId) throws Exception {
		
		DivAction rsdiv = new DivAction();
		return rsdiv;

	}

	private void addAction(ActButton actBtn) {
		if (this.lstActBtn.contains(actBtn))
			return;
		this.lstActBtn.add(actBtn);
	}

	public static boolean haveRightApprove(Object profile) throws Exception {
		throw new Exception("Chua thuc hien");

	}

	private enum ActButton {
		ADD, SAVE, SAVEDISPLAYNONE, CANCELADDNEW, TOAPPROVE, FINISH, UNFINISH, APPROVE, REDO, UPLOAD, APPROVEUPLOAD,
		UNAPPROVEUPLOAD, CANCELAPPROVEUPLOAD, CANCELAPPROVE, DEL, PREVIEW, DOWNLOAD;
	}

	private List<ActButton> lstActBtn = new ArrayList<ActButton>();

	private String html = null;
	private boolean addNew = false;
	private static final String add = "<input type=\"button\" id=\"addTemplate\" onclick=\"addNewRecord()\" value=\"&#xf067; TH&#xCA;M M&#x1EDA;I\" class=\"btn blue\"/>";
	private static final String save = "<input type=\"button\" onclick=\"save()\" value=\"&#xf0c7; L&#x1AF;U\" id=\"btnSave\" class=\"btn blue\"> ";
	private static final String saveDisplayNone = "<input type=\"button\" style=\"display:none\" onclick=\"save()\" value=\"&#xf0c7; L&#x1AF;U\" id=\"btnSave\" class=\"btn blue\"> ";
	private static final String cancelAddNew = "<input type=\"button\" style=\"display:none\" onclick=\"cancelAddNew()\" value=\"&#xf112; B&#x1ECE; QUA\" id=\"btnCancelAddNew\" class=\"btn blue\"> ";
	private static final String toApprove = "<input type=\"button\" onclick=\"toVerifyTemplateInf()\" value=\"CHUY\u1EC2N PH\u00CA DUY\u1EC6T\" id=\"btnToApprove\" class=\"btn blue\"> ";
	private static final String finish = "<input type=\"button\" onclick=\"toVerifyTemplateInf()\" value=\"HO\u00C0N TH\u00C0NH\" id=\"btnFinish\" class=\"btn blue\"> ";
	private static final String unfinish = "<input type=\"button\" onclick=\"unfinish()\" value=\"CH\u1EC8NH S\u1EECA\" id=\"btnUnfinish\" class=\"btn blue\"> ";
	private static final String approve = "<input type=\"button\" onclick=\"approveTemplateInf()\" value=\"DUY&#x1EC6;T\" id=\"btnApprove\" class=\"btn blue\">";
	private static final String redo = "<input type=\"button\" onclick=\"cancelApprove()\" value=\"Th\u1EF1c hi\u1EC7n l\u1EA1i\" id=\"btlRedo\" class=\"btn blue\">";
	private static final String upload = "<input type=\"button\" onclick=\"uploadFile()\" value=\"UPLOAD\" id=\"btlUploadFile\" class=\"btn blue\">";
	private static final String approveUpload = "<input type=\"button\" onclick=\"approveUploadFile()\" value=\"DUY\u1EC6T FILE\" id=\"btlApproveUploadFile\" class=\"btn blue\">";
	private static final String unApproveUpload = "<input type=\"button\" onclick=\"unApproveUploadFile()\" value=\"KH\u00D4NG DUY\u1EC6T FILE\" id=\"btlUnApproveUpload\" class=\"btn blue\">";
	private static final String cancelApproveUpload = "<input type=\"button\" onclick=\"unApproveUploadFile()\" value=\"KH\u00D4NG DUY\u1EC6T FILE\" id=\"btlCancelApproveUpload\" class=\"btn blue\">";
	private static final String cancelApprove = "<input type=\"button\" onclick=\"cancelApprove()\" value=\"T\u1EEB ch\u1ED1i\" id=\"btlCancelApprove\" class=\"btn blue\">";
	private static final String del = "<input type=\"button\" onclick=\"delTemplateInf()\" value=\"&#xf2d3; X&#xD3;A\" id=\"btnDel\" class=\"btn blue\">";
	private static final String preview = "<input type=\"button\" onclick=\"preview()\" value=\"PREVIEW\" id=\"btnPreviewTemplate\" class=\"btn blue\">";
	private static final String download = "<input type=\"button\" onclick=\"download()\" value=\"DOWNLOAD\" id=\"btnDownloadTemplate\" class=\"btn blue\">";

	private static boolean role(SysUsers su, String string) {
		return true;
	}

	public String getHtml() {
		if (html != null)
			return html;
		html = "<div align=\"center\" class=\"HeaderText\">&#8203;</div>\n";
		html += "<div align=\"center\" class=\"divaction tabTemplate\">\n";
		for (ActButton actBtn : this.lstActBtn) {
			switch (actBtn) {
			case ADD:
				html += add;
				break;
			case APPROVE:
				html += approve;
				break;
			case APPROVEUPLOAD:
				html += approveUpload;
				break;
			case CANCELADDNEW:
				html += cancelAddNew;
				break;
			case CANCELAPPROVE:
				html += cancelApprove;
				break;
			case CANCELAPPROVEUPLOAD:
				html += cancelApproveUpload;
				break;
			case DEL:
				html += del;
				break;
			case DOWNLOAD:
				html += download;
				break;
			case FINISH:
				html += finish;
				break;
			case PREVIEW:
				html += preview;
				break;
			case REDO:
				html += redo;
				break;
			case SAVE:
				html += save;
				break;
			case SAVEDISPLAYNONE:
				html += saveDisplayNone;
				break;
			case TOAPPROVE:
				html += toApprove;
				break;
			case UNAPPROVEUPLOAD:
				html += unApproveUpload;
				break;
			case UNFINISH:
				html += unfinish;
				break;
			case UPLOAD:
				html += upload;
				break;
			}
		}
		html += "</div>";
		return html;
	}

	public void setAddNew(boolean addNew) {
		this.addNew = addNew;
	}

	public boolean isAddNew() {
		return addNew;
	}

	private static final Logger logger = LogManager.getLogger(DivAction.class);

	

	public static final Short TMP_STS_DRAFT = 0;
	public static final Short TMP_STS_PDNG_APPROVE = 1;
	public static final Short TMP_STS_APPROVE = 2;
	public static final Short TMP_STS_REJECT = 3;

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ModelForm form)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
