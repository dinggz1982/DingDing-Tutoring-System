package edu.gzhu.its;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.gzhu.its.corpus.entity.UserComment;
import edu.gzhu.its.corpus.entity.UserRemark;
import edu.gzhu.its.corpus.service.IUserCommentService;
import edu.gzhu.its.corpus.service.IUserRemarkService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateUserRemakTest {
	@Resource
	private IUserCommentService userCommentService;
	@Resource
	private IUserRemarkService userRemarkService;
	
	@Test
	@Transactional
	@Rollback(false)
	public void test() throws SQLException{
		List<UserComment> comments = this.userCommentService.findAll();
		for (Iterator iterator = comments.iterator(); iterator.hasNext();) {
			UserComment userComment = (UserComment) iterator.next();
			//还未确认标注
			if(!userComment.isAnnotationed()){
				//找出所有用户的标注
				Set<UserComment> userComments = new HashSet<UserComment>();
				List<UserRemark> remarks = this.userRemarkService.find(" userComment_id="+userComment.getId());
				for (Iterator iterator2 = remarks.iterator(); iterator2.hasNext();) {
					UserRemark userRemark = (UserRemark) iterator2.next();
					if(userComments.contains(userRemark.getUserComment())){
						//说明已经有相同的标注
						
					}
				}
				
				
			}
		}
	}
}
