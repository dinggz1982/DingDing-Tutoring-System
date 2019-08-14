package edu.gzhu.its.qa.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.gzhu.its.base.dao.impl.BaseDAOImpl;
import edu.gzhu.its.qa.entity.Entity;
import edu.gzhu.its.qa.service.IEntityService;

/**
 * 意图槽
 * @author dingguozhu
 *
 */
@Service("entityService")
public class EntityService  extends BaseDAOImpl<Entity, Integer> implements IEntityService{
	private final static Logger logger = LoggerFactory.getLogger(EntityService.class);


}
