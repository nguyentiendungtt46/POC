package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.CatService;

@Repository(value = "catServiceDao")
public class CatServiceDao extends H2HBaseDao<CatService> {
	private static final Logger lg = LogManager.getLogger(CatServiceDao.class);

}
