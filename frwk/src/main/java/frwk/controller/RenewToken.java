package frwk.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
@RequestMapping("/RenewToken")
public class RenewToken {

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.reset();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = new JSONObject();
		long time = Calendar.getInstance().getTimeInMillis();
		String newTokenId = StringEscapeUtils.escapeHtml(UUID.randomUUID().toString()) + "---" + time;
		String tokenIdKey = request.getParameter("tokenIdKey");
		request.getSession().setAttribute(tokenIdKey, newTokenId);
		jsonObject.put("newTokenId", newTokenId);
		out.print(jsonObject);
		out.flush();
		out.close();
		return null;

	}

}
