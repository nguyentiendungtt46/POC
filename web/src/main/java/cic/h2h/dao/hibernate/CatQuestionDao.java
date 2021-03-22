package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.CatQuestion;

@Repository(value = "catQuestionDao")
public class CatQuestionDao extends H2HBaseDao<CatQuestion> {

}
