package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.PartnerService;
@Repository(value = "partnerServiceDao")
public class PartnerServiceDao extends H2HBaseDao<PartnerService> {

}
