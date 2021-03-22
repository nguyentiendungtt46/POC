package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.PocFile;

@Repository(value = "pocFileDao")
public class PocFileDao extends H2HBaseDao<PocFile>{

}
