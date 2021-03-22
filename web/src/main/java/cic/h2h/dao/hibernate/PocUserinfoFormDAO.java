package cic.h2h.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.IdentifierType;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import common.util.Util;
import entity.AppFlow;
import entity.DictTemp;
import entity.PocFile;
import entity.PocInterViewRs;
import entity.PocUserinfoForm;
import entity.Templates;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

@Repository(value = "pocUserinfoFormDAO")
public class PocUserinfoFormDAO extends H2HBaseDao<PocUserinfoForm> {

	private static Logger log = LogManager.getLogger(PocUserinfoFormDAO.class);
	

	public PocUserinfoForm getByIdCard(String idCard) {
		return (PocUserinfoForm) getCurrentSession().createCriteria(PocUserinfoForm.class)
				.add(Restrictions.eq("cardId", idCard)).uniqueResult();

	}

	public Boolean checkDuplicateCardId(String cardId) {
		if (getByIdCard(cardId) == null) {
			return true;
		} else
			return false;
	}

	public boolean existItem(AppFlow appFlow) {
		Criteria c = getCurrentSession().createCriteria(PocUserinfoForm.class)
				.add(Restrictions.eq("status", appFlow.getStatus()));
		if (appFlow.getSubStatus() != null)
			c.add(Restrictions.eq("subStatus", appFlow.getSubStatus()));
		c.setMaxResults(1);
		if (c.list().size() >= 1)
			return Boolean.TRUE;
		return Boolean.FALSE;
	}

	public void saveTemplate(PocUserinfoForm profile) throws ResourceException {
		try {
			Session ss = getCurrentSession();
			Templates template = Formater.isNull(profile.getTemplateId()) ? null
					: (Templates) ss.get(Templates.class, profile.getTemplateId());
			if (profile.getId() == null) {
				if (!Formater.isNull(profile.getPocFiles())) {
					for (PocFile item : profile.getPocFiles()) {
						if (item.getUserId() == null || Formater.isNull(item.getUserId().getId())) {
							item.setUserId(profile);
						}
					}
				}

				if (!Formater.isNull(profile.getPocInterViewRs())) {
					for (PocInterViewRs item : profile.getPocInterViewRs()) {
						if (item.getUserId() == null || Formater.isNull(item.getUserId().getId())) {
							item.setUserId(profile);
						}
					}
				}
				super.save(profile);
				return;
			}
			PocUserinfoForm oldInDB = (PocUserinfoForm) ss.get(PocUserinfoForm.class, profile.getId());
			// Cap nhat cac chi tieu khong thuoc ve template model
			Serializable templateModelId = null;
			String objectType = null;
			if (template != null) {
				for (DictTemp dt : template.getLstDictTemps()) {
					String path = dt.getDictMeasure().getPath();
					// chi tieu khong thuoc template model
					if (Formater.isNull(template.getPath()) || !path.contains(template.getPath()))
						Util.updatePropertyByPath(oldInDB, path, Util.getValueByPath(profile, path));
				}
				if (Formater.isNull(template.getPath())) {
					// Xu ly them truong hop co danh sach ben trong
					List<String> process = new ArrayList<String>();
					// Xu ly grid
					for (DictTemp dt : template.getLstDictTemps()) {
						// Khong phai grid
						if (dt.getCtrType() == null || Formater.isNull(dt.getCtrType().getValue())
								|| !dt.getCtrType().getValue().startsWith("gridColumn"))
							continue;
						// dt.getDictMeasure().getPath():
						// profile.lstProfileLnContrs[0].lnContr.lstLnDisbur[0].lstLnBenefis[0].beneficiary
						int lastIndexOfDot = dt.getDictMeasure().getPath().lastIndexOf(".");
						// profile.lstProfileLnContrs[0].lnContr.lstLnDisbur[0].lstLnBenefis[0]
						String firstListItemPath = dt.getDictMeasure().getPath().substring(0, lastIndexOfDot);
						if (!firstListItemPath.endsWith("[0]"))
							continue;
						// bo 4 ky tu [0]. =>
						// profile.lstProfileLnContrs[0].lnContr.lstLnDisbur[0].lstLnBenefis
						String strListPath = firstListItemPath.substring(0, firstListItemPath.length() - 3);
						String tmp[] = strListPath.split("[,]");
						if (tmp.length > 1)
							continue;
						if (process.contains(strListPath))
							continue;
						process.add(strListPath);
						String[] arrObjPath = strListPath.split("(\\[\\d*].)|\\.");
						Class listItemClass = profile.getClass();
						for (int i = 1; i < arrObjPath.length; i++) {
							Field f = listItemClass.getDeclaredField(arrObjPath[i]);
							if (!ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
								listItemClass = f.getType();
							} else {
								ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
								Type[] fieldType = t1.getActualTypeArguments();
								listItemClass = (Class<?>) fieldType[0];
							}
						}
						if (getClassMetadata(listItemClass) == null)
							continue;
						listItemClass = profile.getClass();
						Object dbStartItem = oldInDB;
						Object smtStartItem = profile;
						for (int i = 1; i < arrObjPath.length; i++) {
							Field f = listItemClass.getDeclaredField(arrObjPath[i]);
							Method getter = Util.getGetterMethod(listItemClass, f);
							Method setter = Util.getSetterMethod(listItemClass, f);
							Object dbProcesstItem = getter.invoke(dbStartItem);
							Object smtProcesstItem = getter.invoke(smtStartItem);

							// Trong DB null thi lay gia tri submit
							if (dbProcesstItem == null || (dbProcesstItem.getClass().equals(ArrayList.class)
									&& ((ArrayList<?>) dbProcesstItem).size() == 0)) {
								// Cap nhat danh sach
								setter.invoke(dbStartItem, smtProcesstItem);
								// Cap nhat parent
								Method setParent = null;
								if (smtProcesstItem.getClass().equals(ArrayList.class)) {
									for (Object o : (ArrayList<?>) smtProcesstItem) {
										if (setParent == null)
											setParent = getSetterParent(o.getClass(), smtStartItem.getClass());
										setParent.invoke(o, dbStartItem);
										ss.save(o);
									}
								} else {
									setParent = getSetterParent(smtProcesstItem.getClass(), smtStartItem.getClass());
									setParent.invoke(smtProcesstItem, dbStartItem);
									ss.save(smtProcesstItem);
								}
								break;
							} else {
								if (i == arrObjPath.length - 1) {
									if (!ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
										listItemClass = f.getType();
									} else {
										ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
										Type[] fieldType = t1.getActualTypeArguments();
										listItemClass = (Class) fieldType[0];
									}
									Method getHashSetGeter = getHashSetGeter(smtStartItem.getClass(), listItemClass);
									List<Object> hsOldInDB = (List<Object>) getHashSetGeter.invoke(dbStartItem);

									// Xu ly them/sua/xoa
									ClassMetadata m = getClassMetadata(listItemClass);

									Method setParent = getSetterParent(listItemClass, smtStartItem.getClass());

									// Them moi
									for (Object itemStm : (ArrayList<?>) smtProcesstItem) {
										// Serializable smtItemId = m.getIdentifier(itemStm);
										Serializable smtItemId = m.getIdentifier(itemStm, (SessionImplementor) ss);
										if (smtItemId == null) {
											hsOldInDB.add(itemStm);
											setParent.invoke(itemStm, dbStartItem);
											ss.save(itemStm);
										}
									}
									// Sua xoa
									List<Object> dels = new ArrayList<Object>();
									for (Object dbItem : (List<?>) dbProcesstItem) {
										boolean del = true;
										Serializable dbItemId = m.getIdentifier(dbItem, (SessionImplementor) ss);
										for (Object itemStm : (ArrayList<?>) smtProcesstItem) {
											Serializable itemStmId = m.getIdentifier(itemStm, (SessionImplementor) ss);
											if (dbItemId.equals(itemStmId)) {
												setParent.invoke(itemStm, dbStartItem);
												ss.merge(itemStm);
												del = false;
												break;
											}
										}
										if (del)
											dels.add(dbItem);

									}
									for (Object del : dels) {
										hsOldInDB.remove(del);
										ss.delete(del);
									}

									break;
								}
								dbStartItem = dbProcesstItem;
								if (dbStartItem.getClass().equals(ArrayList.class)
										&& ((ArrayList<?>) dbStartItem).size() >= 0)
									dbStartItem = ((ArrayList<?>) dbStartItem).get(0);

								smtStartItem = getter.invoke(smtStartItem);
								if (smtStartItem.getClass().equals(ArrayList.class)
										&& ((ArrayList<?>) smtStartItem).size() >= 0)
									smtStartItem = ((ArrayList<?>) smtStartItem).get(0);

							}

							if (!ParameterizedTypeImpl.class.equals(f.getGenericType().getClass())) {
								listItemClass = f.getType();
							} else {
								ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
								Type[] fieldType = t1.getActualTypeArguments();
								listItemClass = (Class<?>) fieldType[0];
							}
						}
					}

				} else {
					Object templateModelObject = ((ArrayList<?>) Util.getValueByPath(profile, template.getPath()))
							.get(0);
					ClassMetadata modelClss = getClassMetadata(templateModelObject.getClass());
					templateModelId = modelClss.getIdentifier(templateModelObject, (SessionImplementor) ss);
					objectType = getTblName(modelClss);
					// Them moi
					if (templateModelId == null) {
						ss.save(templateModelObject);
						templateModelId = getClassMetadata(templateModelObject.getClass())
								.getIdentifier(templateModelObject, (SessionImplementor) ss);
					} else {
						ArrayList<?> lstObjectInDB = (ArrayList<?>) Util.getValueByPath(oldInDB, template.getPath());
						ClassMetadata m = null;
						IdentifierType t = null;
						for (Object objectInDB : lstObjectInDB) {
							if (m == null) {
								m = getClassMetadata(objectInDB.getClass());
								t = (IdentifierType) m.getIdentifierType();
							}
							// Xac dinh record id dang edit
							if (!m.getIdentifier(objectInDB, (SessionImplementor) ss)
									.equals(m.getIdentifier(templateModelObject, (SessionImplementor) ss)))
								continue;
							// Update cac chi tieu cua template model cua object update
							Map<String, String> processList = new HashMap<String, String>();
							ArrayList<String> lstField = null;
							for (DictTemp dt : template.getLstDictTemps()) {
								String path = dt.getDictMeasure().getPath();
								// chi tieu thuoc template model
								if (path.contains(template.getPath())) {
									String relatePath = path.substring(template.getPath().length() + 4); // bo 4 ky tu
																											// [0].
									try {
										// Chi tieu primitive
										// profile.lstProfileLnContrs[0].lnContr.lstLnDisbur[0].disburAmtStr =>
										// relatePath: disburAmtStr
										Field f = templateModelObject.getClass().getDeclaredField(relatePath);
										Object newValue = Util.getValueByPath(templateModelObject, relatePath);
										Util.updatePropertyByPath(objectInDB, relatePath, newValue);

									} catch (NoSuchFieldException ex) {
										// Chi tieu object:
										// profile.lstProfileLnContrs[0].lnContr.lstLnDisbur[0].lstLnBenefis[0].benefiBank
										// => relatePath: lstLnBenefis[0].benefiBank
										String lstTempName = relatePath.split("\\[0\\]")[0];
										if (processList.containsKey(lstTempName))
											continue;
										else {
											lstField = new ArrayList<String>();
											String split = lstTempName + "\\[0\\].";
											for (DictTemp dt1 : template.getLstDictTemps()) {
												String path1 = dt1.getDictMeasure().getPath();
												if (path1.contains(template.getPath()) && path1.contains(lstTempName)) {
													String[] arrTmp = path1.split(split);
													if (arrTmp.length > 1)
														lstField.add(arrTmp[1]);

												}

											}
										}

										processList.put(lstTempName, lstTempName);
										Field f = null;
										try {
											f = templateModelObject.getClass().getDeclaredField(lstTempName);
										} catch (NoSuchFieldException ex1) {
											log.info("Khong ton tai field " + lstTempName);
											log.error(ex1);
											continue;
										}
										if (!f.getType().equals(ArrayList.class))
											continue;

										// Kiem tra item co phai modal class
										Class itmType = null;
										ClassMetadata itmClassMetadata = null;

										// Khong phai kieu generic
										if (!ParameterizedTypeImpl.class.equals(f.getGenericType().getClass()))
											continue;

										// Kieu khong co 1 tham so
										ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
										Type[] fieldType = t1.getActualTypeArguments();
										if (fieldType == null || fieldType.length != 1)
											continue;

										// Khon phai hibernat object
										itmType = (Class) fieldType[0];
										itmClassMetadata = getClassMetadata(itmType);
										if (itmClassMetadata == null)
											continue;

										// lay phuong thuc get cua Set<ClassMetadata>
										Method get = null;
										for (Method mt : templateModelObject.getClass().getMethods()) {
											Class setReturnType = mt.getReturnType();
											if (!setReturnType.equals(Set.class))
												continue;
											ParameterizedTypeImpl getGenericReturnType = (ParameterizedTypeImpl) mt
													.getGenericReturnType();
											Type[] fieldType1 = getGenericReturnType.getActualTypeArguments();
											if (fieldType1 == null || fieldType1.length != 1)
												continue;
											if (!fieldType1[0].equals(itmType))
												continue;
											get = mt;
										}
										// Khong ton tai phuong thuc get cua Set<ClassMetadata>
										if (get == null)
											continue;

										// Xu ly xoa
										List<Object> lstDel = new ArrayList<Object>();
										Iterator iterInDB = ((Iterable) get.invoke(objectInDB)).iterator();
										while (iterInDB.hasNext()) {
											boolean del = true;
											Object itemInDb = iterInDB.next();
											Iterator iterSmt = ((Iterable) get.invoke(templateModelObject)).iterator();
											while (iterSmt.hasNext()) {
												Object itemSmt = iterSmt.next();
												if (itmClassMetadata.getIdentifier(itemInDb, (SessionImplementor) ss)
														.equals(itmClassMetadata.getIdentifier(itemSmt,
																(SessionImplementor) ss))) {
													del = false;
													break;
												}

											}
											if (del)
												lstDel.add(itemInDb);
										}
										for (Object o : lstDel) {
											((Set) get.invoke(objectInDB)).remove(o);
											ss.delete(o);
										}

										// Chinh sua, cap nhat
										Method setParent = null;
										for (Method mt : itmType.getMethods()) {
											if (!Void.TYPE.equals(mt.getReturnType()))
												continue;
											Type[] genericParameterTypes = mt.getGenericParameterTypes();
											if (genericParameterTypes == null || genericParameterTypes.length != 1)
												continue;
											if (!genericParameterTypes[0].equals(templateModelObject.getClass()))
												continue;
											setParent = mt;
										}
										// Khong ton tai phuong thuc set parent
										if (setParent == null)
											continue;

										Iterator iterSmt = ((Iterable) get.invoke(templateModelObject)).iterator();
										while (iterSmt.hasNext()) {
											Object itmSmt = iterSmt.next();
											setParent.invoke(itmSmt, objectInDB);
											if (itmClassMetadata.getIdentifier(itmSmt,
													(SessionImplementor) ss) != null) {
												Object itmInDb = ss.load(itmSmt.getClass(), itmClassMetadata
														.getIdentifier(itmSmt, (SessionImplementor) ss));
												for (String s : lstField) {
													try {
														Field f1 = itmSmt.getClass().getDeclaredField(s);
														Object newValue = Util.getValueByPath(itmSmt, s);
														Util.updatePropertyByPath(itmInDb, s, newValue);
													} catch (NoSuchFieldException ex1) {
														log.info("khong ton tai field: " + s);
														log.error(ex);
													}

												}

												ss.merge(itmInDb);
											}

											else
												ss.save(itmSmt);
										}
									} catch (Exception ex) {
										log.error(ex);
									}
								}

							}
							ss.update(objectInDB);
						}
					}
				}
			}

			super.save(oldInDB);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
			throw new ResourceException("Luu khong thanh cong");
		}
	}

}
