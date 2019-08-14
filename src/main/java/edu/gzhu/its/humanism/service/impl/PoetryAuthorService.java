package edu.gzhu.its.humanism.service.impl;

import org.springframework.stereotype.Service;

import edu.gzhu.its.base.dao.impl.BaseDAOImpl;
import edu.gzhu.its.humanism.entity.PoetryAuthor;
import edu.gzhu.its.humanism.service.IPoetryAuthorService;

@Service("poetryAuthorService")
public class PoetryAuthorService  extends BaseDAOImpl<PoetryAuthor, Integer> implements IPoetryAuthorService{

}
