package cic.h2h.controller;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.ObjectNotFoundException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.DictMeasureDao;
import cic.h2h.dao.hibernate.TemplatesDao;
import common.util.Formater;
import common.util.ResourceException;
import common.util.Util;
import entity.ControlType;
import entity.DictTemp;
import entity.Templates;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;
import frwk.dao.hibernate.sys.SysUsersDao;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.IdentifierType;
import org.json.JSONArray;
import org.json.JSONObject;

@Controller
@RequestMapping("/dynHtml")
public class InputTemp {
	private static final Logger logger = LogManager.getLogger(InputTemp.class);

	private Templates temp = new Templates();

	private static final String DATA_TYPE_STATIC = "static";
	private static final String DATA_TYPE_DYNAMIC = "dynamic";
	private static final String HTML_HEADER_TEXT = "<div class=\"%s\">%s</div>";
	private static final String HTML_START_ROW = "<div class=\"Row %s\">\n";
	private static final String HTML_END_ROW = "</div>\n";
	private static final String HTML_SPACE = "<div class=\"col-md-2 col-lg-2 col-sm-12 col-xs-12\"></div>\n";
	private static final String HTML_SCRIPT = "<script language=\"javascript\" type=\"text/javascript\"> $(document).ready(function () {  });</script>";

	public Object getModel() {
		return temp;
	}

	@Autowired
	private TemplatesDao templatesDao;
	private Map<String, GroupControl> lstControlGroup = null;

	/**
	 * Gen noi dung HTML theo template va data <br>
	 * Tra ve noi dung Html cua template
	 * 
	 * @param temp     Template
	 * @param profile  Profile
	 * @param recordId recordId
	 * @return Noi dung HTML
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getDynHtml(Templates temp, Object profile, String recordId) throws Exception {
		lstControlGroup = new HashMap<String, GroupControl>();
		// Khong ton tai chi tieu
		if (Formater.isNull(temp.getLstDictTemps()) && !(temp.getMultiInstance() == null || temp.getMultiInstance())) {
			DivAction o = DivAction.make(temp, profile, recordId);
			return o == null ? "" : o.getHtml();
		}

		// Chia thanh nhieu group
		for (DictTemp dt : temp.getLstDictTemps()) {
			if (lstControlGroup == null)
				lstControlGroup = new HashMap<String, GroupControl>();
			// Sua lai groupName, xac dinh group class
			dt.sparateClass();
			// Add item vao group
			if (!lstControlGroup.containsKey(dt.getRealGroupName()))
				lstControlGroup.put(dt.getRealGroupName(), new GroupControl(dt));
			lstControlGroup.get(dt.getRealGroupName()).addItem(dt);
		}

		String htmlContent = "";

		// Grid danh sach mau bieu (doi voi truong hop mau bieu xuat hien nhieu lan
		if (temp.getMultiInstance() != null && temp.getMultiInstance()) {
			// Sap xep danh sach
			ArrayList os = (ArrayList<Object>) Util.getValueByPath(profile, temp.getPath());
			if (!Formater.isNull(os)) {
				Collections.sort(os, new Comparator<Object>() {
					@Override
					public int compare(final Object before, Object afer) {
						try {
							Method getCreateDt = before.getClass().getMethod("getCreateDt");
							Date beforeCreateDate = (Date) getCreateDt.invoke(before);
							Date aferCreateDate = (Date) getCreateDt.invoke(afer);
							if (beforeCreateDate == null) {
								if (aferCreateDate != null)
									return 1;
								else
									return 0;
							}
							if (aferCreateDate == null)
								return -1;
							int iResult = (-1) * beforeCreateDate.compareTo(aferCreateDate);
							return iResult;
						} catch (Exception ex) {
							return 0;
						}

					}
				});
			}

			// gen table
			htmlContent += tblHistory(profile, temp, recordId);

			if (Formater.isNull(temp.getPath()))
				throw new ResourceException("multi instance without path");
			// Xac dinh class
			String[] arrPath = temp.getPath().split("(\\[0\\]\\.)|\\.");
			Class modelClss = profile.getClass();
			for (int i = 1; i < arrPath.length; i++) {
				Field field = modelClss.getDeclaredField(arrPath[i]);
				Type type = field.getGenericType();
				if (type instanceof ParameterizedType) {
					ParameterizedType pType = (ParameterizedType) type;
					modelClss = (Class) pType.getActualTypeArguments()[0];
				} else {
					modelClss = field.getType();
				}
			}
			ClassMetadata m = templatesDao.getClassMetadata(modelClss);

			// Xac dinh recordId
			if (Formater.isNull(recordId) && !"-1".equals(recordId)) {
				if (!Formater.isNull(os)) {
					Object first = os.iterator().next();
					recordId = (String) m.getIdentifier(first, (SessionImplementor) templatesDao.getCurrentSession());
				}
			}

			// create control hidden tren client
			htmlContent += String.format("<input type='hidden' id = 'recordId' name='%s' value = '%s'/>",
					new Object[] { temp.getPath() + "[0]." + m.getIdentifierPropertyName(),
							(Formater.isNull(recordId) ? "" : recordId) });

			// Xoa bo cac phan tu khong dung tren giao dien (giao dien chi hien thi 1 phan
			// tu recordId, cac phan tu khac remove di)
			if (!Formater.isNull(recordId) && !"-1".equals(recordId)) {
				ArrayList<Object> lst = (ArrayList<Object>) Util.getValueByPath(profile, temp.getPath());
				for (Object o : lst) {
					Serializable oId = m.getIdentifier(o, (SessionImplementor) templatesDao.getCurrentSession());
					if (recordId.equals(oId.toString())) {
						ArrayList<Object> tmp = new ArrayList<Object>();
						tmp.add(o);
						Util.updatePropertyByPath(profile, temp.getPath(), tmp);
						break;
					}
				}
			} else {
				ArrayList<Object> lst = (ArrayList<Object>) Util.getValueByPath(profile, temp.getPath());
				String[] arrObjPath = temp.getPath().split("(\\[\\d*].)|\\.");
				Class listItemClass = profile.getClass();
				for (int i = 1; i < arrObjPath.length; i++) {
					Field f = listItemClass.getDeclaredField(arrObjPath[i]);
					if (!ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
						listItemClass = f.getType();
					} else {
						ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
						Type[] fieldType = t1.getActualTypeArguments();
						listItemClass = (Class) fieldType[0];
					}
				}
				Constructor c = listItemClass.getConstructor(null);
				Object o = c.newInstance(null);
				if (Formater.isNull(lst)) {
					lst.add(o);
				} else {
					lst.set(0, o);
				}
				Util.updatePropertyByPath(profile, temp.getPath(), lst.get(0));
			}

		}

		// Ve doc lap tung group
		if (lstControlGroup != null) {
			List<String> keys = new ArrayList(lstControlGroup.keySet());
			Collections.sort(keys, new Comparator<String>() {
				@Override
				public int compare(final String before, String afer) {
					try {
						if (Formater.isNull(before)) {
							if (!Formater.isNull(afer))
								return -1;
							else
								return 0;
						}
						if (Formater.isNull(afer))
							return 1;
						String sBeforIdx = before.substring(0, before.indexOf(". "));
						String sAferIdx = afer.substring(0, afer.indexOf(". "));
						return Double.valueOf(sBeforIdx).compareTo(Double.valueOf(sAferIdx));
					} catch (Exception ex) {
						try {
							return before.compareTo(afer);
						} catch (Exception e) {
							return 0;
						}
					}
				}
			});
			for (String s : keys)
				htmlContent += lstControlGroup.get(s).draw(profile, temp);
		}

		// Bieu mau nhap nhanh thi khong co divaction
		// Nhap nhanh cua hdtd
		/*
		 * if (temp.getType() != null && temp.getType() == 2) { if (temp.getFastInput()
		 * != null && temp.getFastInput()) return htmlContent + readyScript(); } else {
		 * // Kiem tra nhap nhanh cua hdtd Product prod = ((ProductType)
		 * profile).getProduct(); for (ProdTemp o : prod.getProdTemps()) { if
		 * (o.getFastInput() != null && o.getFastInput() &&
		 * o.getTemplates().getId().equals(temp.getId())) return htmlContent +
		 * readyScript(); } }
		 */

		DivAction divAction = DivAction.make(temp, profile, recordId);
		htmlContent += divAction == null ? "" : divAction.getHtml();
		if (divAction.isAddNew()) {
			String resetInputGrid = "";
			resetInputGrid += "<script language = 'javascript' type = 'text/javascript'>\n";
			resetInputGrid += getArrGridId() + ";\n";
			resetInputGrid += "$('#addTemplate').on('click', function(){\n";
			resetInputGrid += "for(var i =0; i< arrGridId.length; i++)\n";
			resetInputGrid += "     arrGridId[i].deleteAllRow();\n";

			resetInputGrid += "});\n";
			resetInputGrid += "</script>\n";
			htmlContent += resetInputGrid;
		}
		return htmlContent + readyScript();

	}

	@Autowired
	private HttpSession session;

	public void initData() throws Exception {
		// temp.setSid(Long.valueOf(ServletActionContext.getRequest().getParameter("sid")));

	}

	public void setTemp(Templates temp) {
		this.temp = temp;
	}

	public Templates getTemp() {
		return this.temp;
	}

	private JSONArray getListDataSelect(String selectData) {
		JSONArray jsonArray = new JSONArray();
		try {
			JSONObject jsonObject = new JSONObject(selectData);
			if (DATA_TYPE_STATIC.equals(jsonObject.get("dataType"))) {
				jsonArray = (JSONArray) jsonObject.get("data");
			} else if (DATA_TYPE_DYNAMIC.equals(jsonObject.get("dataType"))) {
				jsonArray = dictMeasureDao.getListOptions(jsonObject.get("data").toString());
			}
		} catch (Exception ex) {
			logger.error("Loi", ex);
		}

		return jsonArray;
	}

	@Autowired
	private DictMeasureDao dictMeasureDao;

	private String getHeaderClass(String headerText, Byte groupLevel) {
		if (groupLevel != null) {
			// byte bLevel = Byte.valueOf((byte)(groupLevel.byteValue() % 10));
			return "header" + groupLevel;
		}
		if (Formater.isNull(headerText))
			return "header1";
		if (!Character.isDigit(headerText.charAt(0)))
			return "header1";
		String[] arrTemp = headerText.split(" ");
		if (arrTemp.length <= 1)
			return "header1";
		int iLevel = StringUtils.countMatches(arrTemp[0], ".");
		return "header" + iLevel;

	}

	@Autowired
	private SysUsersDao sysUsersDao;

	private String tblHistory(Object profile, Templates temp, String recordId) throws Exception {
		String html = "<div id = \"divTableHis\" class=\"table-responsive\">";
		html += "<table id=\"tableTableHisId\" style=\"table-layout: fixed; margin: 0px;\" class=\"table\">";
		html += " <thead>\n" + "                <tr>\n" + "                    <th>S\u1ED1 th\u1EE9 t\u1EF1</th>\n"
				+ "                    <th>Id</th>\n" + "                    <th>Ng\u01B0\u1EDDi l\u1EADp</th>\n"
				+ "                    <th>Ng\u00E0y l\u1EADp</th>\n"
				+ "                    <th>Ng\u00E0y upload</th>\n"
				+ "                    <th>Ng\u01B0\u1EDDi upload</th>\n"
				+ "                    <th>Tr\u1EA1ng th\u00E1i</th>\n"
				+ "                </tr>                                            \n" + "            </thead>";
		html += "<tbody>";
		if (profile != null) {
			String rowTemp = "<tr onclick=\"loadTemplate($('#templateId').val(), '%s')\" style=\"%s\">\n"
					+ "                        <td>%s</td>\n"
					+ "                        <td><a href=\"#\" onclick=\"loadTemplate($('#templateId').val(), %s, $(this))\">%s</a></td>\n"
					+ "                        <td>%s</td>\n" + "                        <td>%s</td>\n"
					+ "                        <td>%s</td>\n" + "                        <td>%s</td>\n"
					+ "                        <td>%s</td>\n" + "                    </tr>";

			ArrayList<Object> os = (ArrayList<Object>) Util.getValueByPath(profile, temp.getPath());
			Method getCreateId = null;
			Method getCreateUser = null;
			if (!Formater.isNull(os)) {
				Iterator<?> iter = os.iterator();
				int iIndex = 0;
				while (iter.hasNext()) {
					Object o = iter.next();
					Method getId = o.getClass().getMethod("getId");
					String id = getId.invoke(o) != null ? getId.invoke(o).toString() : "";
					// focus vao dong dau tien neu recordId null
					if (Formater.isNull(recordId))
						recordId = id;
					Method getCreateDtStr = o.getClass().getMethod("getCreateDtStr");
					String createDate = getCreateDtStr.invoke(o) != null ? getCreateDtStr.invoke(o).toString() : "";
					if (iIndex == 0) {
						try {
							getCreateId = o.getClass().getMethod("getCreateId");
						} catch (NoSuchMethodException nsme) {
							getCreateUser = o.getClass().getMethod("getCreateUser");
						}
					}
					String createId = null;
					if (getCreateId != null) {
						createId = getCreateId.invoke(o) != null ? getCreateId.invoke(o).toString() : "";
						if (!Formater.isNull(createId)) {
							SysUsers su = (SysUsers) sysUsersDao.getObject(SysUsers.class, createId);
							if (su != null)
								createId = su.getUsername();
						}
					} else {
						SysUsers su = (SysUsers) getCreateUser.invoke(o);
						try {
							createId = su == null ? "" : su.getUsername();
						} catch (ObjectNotFoundException e) {
							createId = "";
						}
					}

					/*
					 * ProfileTemp profileTemp = ProfileTempImpl.get(profile, temp, id); String
					 * sStatus = ""; String tmpCreateDate = ""; String tmpCreateId = ""; if
					 * (profileTemp != null) { sStatus = profileTemp.getStatusName(); tmpCreateDate
					 * = Formater.date2str(profileTemp.getUploadDt()); if (profileTemp.getUploadId()
					 * != null) { SysUsers uploader = (SysUsers)
					 * sysUsersDao.getObject(SysUsers.class, profileTemp.getUploadId()); tmpCreateId
					 * = uploader.getUsername(); }
					 * 
					 * } if (id.equals(recordId)) { html += String.format(rowTemp, new Object[] {
					 * id, "background-color:aliceblue", String.valueOf(++iIndex), id, id, createId,
					 * createDate, tmpCreateDate, tmpCreateId, sStatus }); } else { html +=
					 * String.format(rowTemp, new Object[] { id, "", String.valueOf(++iIndex), id,
					 * id, createId, createDate, tmpCreateDate, tmpCreateId, sStatus }); }
					 */

				}
			}

		}
		html += "</tbody>";
		html += "</table>";
		html += "</div>";
		return html;
	}

	private String getArrGridId() {
		String scriptArrGridId = "var arrGridId=[";
		boolean isFirst = true;
		for (String s : this.lstControlGroup.keySet()) {
			if (!Formater.isNull(this.lstControlGroup.get(s).getLstGrid())) {
				for (Grid g : this.lstControlGroup.get(s).getLstGrid().values()) {
					if (g.getLstColumn() != null && g.getLstColumn().size() > 0)
						if (isFirst) {
							scriptArrGridId += g.tblObject;
							isFirst = false;
						} else
							scriptArrGridId += "," + g.tblObject;

				}
			}

		}

		return scriptArrGridId += "]";
	}

	private String readyScript() {
		String dyFormScript = "<script language=\"javascript\" type=\"text/javascript\">";
		dyFormScript += "$(document).ready(function(){\n" + "        dynFormReadyScript();\n" + "   });";
		dyFormScript += "</script>";
		return dyFormScript;

	}

	private class Grid {
		private String title;
		private String tblId;
		private String tblObject;
		ArrayList<DictTemp> lstColumn = new ArrayList<DictTemp>();
		private String modelPath;
		private String grdClass;

		private Grid(DictTemp dictTemp) {
			this.title = dictTemp.getGroupName();
			String path = dictTemp.getDictMeasure().getPath();
			// bo [0]
			modelPath = path.substring(0, path.lastIndexOf(".") - 3);
			tblId = modelPath.replaceAll("\\.", "_").replaceAll("\\[0\\]", "0") + "_" + dictTemp.getGroupClass();
			tblObject = "tbl" + this.tblId;
			this.grdClass = dictTemp.getGroupClass();

		}

		public void addColumn(DictTemp column) {
			lstColumn.add(column);
		}

		public ArrayList<DictTemp> getLstColumn() {
			return lstColumn;
		}

		private String displayHtml(Object profile) throws Exception {
			String html = "<div class='Row grid " + (Formater.isNull(grdClass) ? "" : grdClass) + "'>";
			html += "<div class=\"table-responsive\">";
			html += "<table id=\"" + this.tblId + "\" style=\"table-layout: fixed; margin: 0px;\" class=\"table\">";
			html += "<thead><tr>";
			for (DictTemp dt : this.getLstColumn()) {
				String columDesc = Formater.isNull(dt.getColumnDesc()) ? dt.getDictMeasure().getColDesc()
						: dt.getColumnDesc();
				if (dt.getMandatory() != null && dt.getMandatory())
					columDesc += "<font color=\"red\">*</font>";
				html += String.format(" <th>%s</th>", columDesc);
			}

			html += "</tr></thead>";

			html += "<tbody>";

			// Data
			if (profile != null) {
				List<?> o = (List<?>) (profile != null ? profile.getClass()
						.getMethod("getValue", new Class[] { String.class }).invoke(profile, modelPath) : null);
				ArrayList<Object> lstData = new ArrayList<Object>();
				if (o != null)
					lstData.addAll(o);

				if (!Formater.isNull(lstData)) {
					Collections.sort(lstData, new Comparator<Object>() {
						@Override
						public int compare(final Object before, Object afer) {
							try {
								Method getCreateDt = null;
								try {
									getCreateDt = before.getClass().getMethod("getCreateDt");
								} catch (NoSuchMethodException ex) {
									return 0;
								}
								Date beforeCreateDate = (Date) getCreateDt.invoke(before);
								Date aferCreateDate = (Date) getCreateDt.invoke(afer);
								if (beforeCreateDate == null) {
									if (aferCreateDate != null)
										return 1;
									else
										return 0;
								}
								if (aferCreateDate == null)
									return -1;
								int iResult = (-1) * beforeCreateDate.compareTo(aferCreateDate);
								return iResult;
							} catch (Exception ex) {
								return 0;
							}

						}
					});
					ClassMetadata m = null;
					IdentifierType t = null;
					String sIdentifyName = null;
					for (Object dataObj : lstData) {
						if (m == null) {
							m = templatesDao.getClassMetadata(dataObj.getClass());
							t = (IdentifierType) m.getIdentifierType();
							sIdentifyName = m.getIdentifierPropertyName();
						}
						html += "<tr>";
						boolean firstColum = true;
						for (DictTemp column : this.getLstColumn()) {
							html += "<td>";
							String id = m.getIdentifier(dataObj,
									(SessionImplementor) templatesDao.getCurrentSession()) == null ? ""
											: m.getIdentifier(dataObj,
													(SessionImplementor) templatesDao.getCurrentSession()).toString();
							if (firstColum) {
								String temp = String.format(
										"<input type= 'hidden' class='" + column.getCtrType().getValue()
												+ "' onchange='updateSameControl($(this))' name = '%s' value = '%s'/>",
										new Object[] { this.modelPath + "[]." + sIdentifyName, id });
								html += temp;

								firstColum = false;
							}
							html += column.createInput(dataObj, id);
							html += "</td>";
						}
						html += "</tr>";
					}
				}
			}
			html += "</tbody>\n";
			html += "</table>\n";
			String scrptInitTbl = buildScript(profile);
			html += scrptInitTbl;
			logger.info(html);
			html += "</div>";
			html += "</div>";
			return html;
		}

		private String buildScript(Object profile) throws Exception {
			String sScript = "<script type=\"text/javascript\" >\n";
			sScript += "var " + tblObject + ";\n";
			sScript += "$(document).ready(function(){\n";
			sScript += String.format(
					tblObject + " = new TFOnline.DataTable({\n" + "            id : '%s'\n"
							+ "            ,jQueryUI : true\n" + "            ,rowTemp : %s\n"
							+ "            ,hasCheck : true\n" + "            ,afterAddRow : function(){\n"
							+ "                    initControl();\n" + "                }\n" + "             ",
					new Object[] { this.tblId, getRowTemp(profile) });
			sScript += "});\n";
			sScript += "});\n";
			sScript += "\n</script>";
			return sScript;
		}

		private String rowTemp = null;

		private String getRowTemp(Object profile) throws Exception {
			if (rowTemp != null)
				return rowTemp;
			rowTemp = createRowTemp(profile);
			return rowTemp;
		}

		private static final String FIELD_HIDDEN = "<input type='hidden' name = '%s'/>";

		private String createRowTemp1() throws Exception {
			String rowTemp = "[";
			for (DictTemp column : this.getLstColumn()) {
				if ("[".equals(rowTemp))
					rowTemp += "\"" + column.createInput(null, null) + "\"";
				else
					rowTemp += "," + "\"" + column.createInput(null, null) + "\"";
			}
			rowTemp += "]";
			return rowTemp;
		}

		private String createRowTemp(Object profile) throws Exception {
			String rowTemp = "[";
			for (DictTemp column : this.getLstColumn()) {
				column.prepare(profile);
				if ("[".equals(rowTemp)) {
					String[] arrObjPath = column.getDictMeasure().getPath().split("(\\[\\d*].)|\\.");
					Class listItemClass = profile.getClass();
					for (int i = 1; i < arrObjPath.length - 1; i++) {
						Field f = listItemClass.getDeclaredField(arrObjPath[i]);
						if (!ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
							listItemClass = f.getType();
						} else {
							ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
							Type[] fieldType = t1.getActualTypeArguments();
							listItemClass = (Class) fieldType[0];
						}
					}
					String idFieldName = templatesDao.getIdentifierPropertyName(listItemClass);
					String temp = String.format(
							"<input type= 'hidden' class='" + column.getCtrType().getValue()
									+ "' onchange='updateSameControl($(this))' name = '%s' value = '%s'/>",
							new Object[] { this.modelPath + "[]." + idFieldName, "" });
					rowTemp += "\"" + temp + "";
					rowTemp += column.createInput(null, null) + "\"";
				} else {
					rowTemp += "," + "\"" + column.createInput(null, null) + "\"";
				}

			}
			rowTemp += "]";
			return rowTemp;
		}

		public String getTblObject() {
			return tblObject;
		}
	}

	private class GroupControl {
		private String headerText;
		private String headerClass;

		/**
		 * Vi tri se ve
		 */
		private int drawPosition;
		private Byte grpLevel;

		/**
		 * class chung cho moi item. null neu cac item co class khac nhau
		 */
		private String groupClass;
		private ArrayList<DictTemp> dictTemps;

		private String getHeaderHtml() {
			return String.format(HTML_HEADER_TEXT,
					new Object[] { headerClass
							+ (Formater.isNull(this.groupClass) ? " groupHeader" : " groupHeader " + this.groupClass),
							headerText });
		}

		private void addItem(DictTemp dt) {
			this.dictTemps.add(dt);
		}

		private GroupControl(DictTemp dt) {
			dictTemps = new ArrayList<DictTemp>();
			this.grpLevel = dt.getGrpLevel();
			this.headerText = dt.getRealGroupName();
			this.headerClass = getHeaderClass(this.headerText, this.grpLevel);
			this.drawPosition = 1;

		}

		private String draw(DictTemp dt, Object profile) throws Exception {
			dt.prepare(profile);
			String html = "";
			if (dt.getDisplayInOneLine() != null && dt.getDisplayInOneLine()) {
				if (drawPosition == 2) {
					html += HTML_END_ROW;
					drawPosition = 1;
				}
				html += dt.draw(profile);

			} else {
				if (drawPosition == 1) {
					html = String.format(HTML_START_ROW, dt.getGroupClass()) + dt.draw(profile);
					drawPosition = 2;
				}

				else {
					html = HTML_SPACE + dt.draw(profile) + HTML_END_ROW;
					drawPosition = 1;
				}
			}
			return html;
		}

		private Map<String, Grid> lstGrid = null;

		private String draw(Object profile, Templates temp) throws Exception {
			// Xac dinh radio panel, checkbox panel
			for (DictTemp dt : this.dictTemps) {
				String realControlType = dt.getCtrType() == null ? dt.getDictMeasure().getDataSource()
						: dt.getCtrType().getValue();
				dt.setRealControlType(realControlType);
				if (dt.getDictType() != null && ControlType.RADIO.equals(dt.getRealControlType())
						|| ControlType.CHECKBOX.equals(dt.getRealControlType())
						|| ControlType.CHK_GRP.equals(dt.getRealControlType())) {
					if (dt.getDictType() != null && !Formater.isNull(dt.getDictType().getSysDictParams())) {
						for (SysDictParam param : dt.getDictType().getSysDictParams()) {
							if (Boolean.TRUE.equals(dt.getParentCtrl()))
								break;
							// Phai xac minh panel tren toan temp
							for (DictTemp child : temp.getLstDictTemps()) {
								if (child.getId().equals(dt.getId()))
									continue;
								if (Formater.isNull(child.getGroupClass()))
									continue;
								if (child.getGroupClass().equals(param.getCode())
										|| child.getGroupClass().indexOf(" " + param.getCode()) >= 0
										|| child.getGroupClass().indexOf(param.getCode() + " ") >= 0) {
									dt.setParentCtrl(Boolean.TRUE);
									break;
								}
							}
						}
					}

				}

			}

			// Xac dinh groupClass
			boolean initGroupClass = false;
			for (DictTemp dt : this.dictTemps) {
				String realControlType = dt.getCtrType() == null ? dt.getDictMeasure().getDataSource()
						: dt.getCtrType().getValue();
				dt.setRealControlType(realControlType);
				if (!initGroupClass) {
					groupClass = dt.getGroupClass();
					initGroupClass = true;
				} else {
					// Group khong co class do cac item co group clss khac nhau
					if (Formater.isNull(this.groupClass)) {
						if (!Formater.isNull(dt.getGroupClass()))
							break;
					} else {
						if (!this.groupClass.equals(dt.getGroupClass())) {
							this.groupClass = null;
							break;
						}
					}

				}

			}
			String html = getHeaderHtml();
			Collections.sort(this.dictTemps, new Comparator<DictTemp>() {
				@Override
				public int compare(final DictTemp before, DictTemp afer) {
					return before.compareTo(afer);

				}
			});
			boolean firstItem = true;
			for (DictTemp dt : this.dictTemps) {

				String controlType = dt.getCtrType() == null ? dt.getDictMeasure().getDataSource()
						: dt.getCtrType().getValue();

				// Grid column
				if (!Formater.isNull(controlType) && controlType.startsWith("gridColumn_")) {
					if (this.lstGrid == null)
						this.lstGrid = new HashMap<String, Grid>();
					// Grid g = this.lstGrid.get(dt.getRealGroupName());
					Grid g = this.lstGrid.get(dt.getGroupName());
					if (g == null) {
						g = new Grid(dt);
						this.lstGrid.put(dt.getGroupName(), g);
					}
					g.addColumn(dt);
					continue;
				}
				if (firstItem) {
					this.groupClass = dt.getGroupClass();
					firstItem = false;
				} else {
					// Thay doi goupclass can xuong dong
					if ((Formater.isNull(this.groupClass) && !Formater.isNull(dt.getGroupClass()))
							|| (!Formater.isNull(this.groupClass) && Formater.isNull(dt.getGroupClass()))
							|| (!Formater.isNull(this.groupClass) && !this.groupClass.equals(dt.getGroupClass()))
							|| (!Formater.isNull(dt.getGroupClass()) && !dt.getGroupClass().equals(this.groupClass))) {
						this.groupClass = dt.getGroupClass();
						if (drawPosition == 2) {
							html += HTML_END_ROW;
							drawPosition = 1;
						}

					}

				}

				// Draw item, cap nhat drawPosition
				html += this.draw(dt, profile);

				// Ve phan tu cuoi cung cua nhom o vi tri 1, can them end Row
				if (dt.equals(this.dictTemps.get(this.dictTemps.size() - 1))) {
					if (drawPosition == 2)
						html += HTML_END_ROW;
				}

			}

			// Ve grid thuoc nhom
			if (this.lstGrid != null && this.lstGrid.size() > 0) {
				for (Grid g : this.lstGrid.values()) {
					if (g.getLstColumn() != null && g.getLstColumn().size() > 0)
						html += g.displayHtml(profile);

				}

			}
			return html;
		}

		public void setLstGrid(Map<String, InputTemp.Grid> lstGrid) {
			this.lstGrid = lstGrid;
		}

		public Map<String, InputTemp.Grid> getLstGrid() {
			return lstGrid;
		}
	}
}
