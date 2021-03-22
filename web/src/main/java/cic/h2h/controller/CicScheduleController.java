package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CicScheduleDao;
import cic.h2h.form.CicScheduleForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.CicSchedule;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/cicSchedule")
public class CicScheduleController extends CatalogController<CicScheduleForm, CicSchedule> {

	@Autowired
	CicScheduleDao cicScheduleDao;

	@Override
	public BaseDao<CicSchedule> createSearchDAO(HttpServletRequest request, CicScheduleForm form) throws Exception {

		CicScheduleDao dao = new CicScheduleDao();
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, CicSchedule e, CicScheduleForm modelForm) throws Exception {
		ja.put((Formater.isNull(e.getSvCode()) ? "" : (e.getSvCode() + " - ")) + e.getService());
		ja.put((Formater.isNull(e.getPrCode()) ? "" : (e.getPrCode() + " - "))
				+ (Formater.isNull(e.getProduct()) ? "" : e.getProduct()));
		ja.put(FormatNumber.num2Str(e.getMaxSizeApi()));
		ja.put(e.getFrequency());
		ja.put(FormatNumber.num2Str(e.getTimeToLive()));
		if (e.getStatus() != null && e.getStatus()) {
			ja.put("X");
		} else
			ja.put("");
	}

	@Override
	public BaseDao<CicSchedule> getDao() {

		return cicScheduleDao;
	}

	@Override
	public String getJsp() {

		return "cic_schedule/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CicScheduleForm form)
			throws Exception {

	}

}
