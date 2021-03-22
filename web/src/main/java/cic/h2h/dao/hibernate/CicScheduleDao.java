package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.CicSchedule;

@Repository(value = "cicScheduleDao")
public class CicScheduleDao extends H2HBaseDao<CicSchedule>{

}
