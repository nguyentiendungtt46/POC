package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.CatCustomer;

@Repository(value = "catCustomerDao")
public class CatCustomerDAO extends H2HBaseDao<CatCustomer> {

}
