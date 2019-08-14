package edu.gzhu.its.qa.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.gzhu.its.base.dao.impl.BaseDAOImpl;
import edu.gzhu.its.qa.entity.Intent;
import edu.gzhu.its.qa.service.IIntentService;

/**
 * 意图理解
 * @author dingguozhu
 *
 */
@Service("intenService")
public class IntentService  extends BaseDAOImpl<Intent, Integer> implements IIntentService{
	private final static Logger logger = LoggerFactory.getLogger(IntentService.class);
}
