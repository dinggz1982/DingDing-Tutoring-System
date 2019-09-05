package edu.gzhu.its.corpus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.gzhu.its.corpus.entity.UserComment;
import edu.gzhu.its.corpus.entity.UserRemark;
import edu.gzhu.its.corpus.service.IUserCommentService;
import edu.gzhu.its.corpus.service.IUserRemarkService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateUserCommentTest {

	@Autowired
	private IUserCommentService userCommentService;
	@Autowired
	private IUserRemarkService userRemarkService;

	@Test
	public void update() {

		/*
		 * List<UserRemark> remarks = this.userRemarkService.find(
		 * " where userComment_id=1");
		 * 
		 * if(remarks!=null){ Map<Integer, UserRemark> map = new
		 * HashMap<Integer, UserRemark>(); int i=0; for (Iterator iterator =
		 * remarks.iterator(); iterator.hasNext();) { UserRemark userRemark =
		 * (UserRemark) iterator.next(); map.put(i, userRemark); i++; }
		 * 
		 * 
		 * }
		 */

		List<UserComment> comments = null;
		try {
			comments = this.userCommentService.findAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Iterator iterator2 = comments.iterator(); iterator2.hasNext();) {
			UserComment comment = (UserComment) iterator2.next();

			List<UserRemark> remarks = this.userRemarkService.find(" where userComment_id=" + comment.getId());
			System.out.println(remarks.size());
			Set<UserRemark> userRemarks = new HashSet<UserRemark>();
			for (Iterator iterator = remarks.iterator(); iterator.hasNext();) {
				UserRemark userRemark = (UserRemark) iterator.next();
				// System.out.println(userRemark.getId()+","+userRemark.getContentRelated()+","+userRemark.getEmotionRelated()+","+userRemark.getOtherRelated()+","+userRemark.isEffectiveComment());
				if (!userRemarks.contains(userRemark)) {
					userRemarks.add(userRemark);
				} else {
					// 有相同的标注
					System.out.println(userRemark.getId());
					// 更新标注信息
					comment.setContentRelated(userRemark.getContentRelated());
					comment.setEmotionRelated(userRemark.getEmotionRelated());
					comment.setOtherRelated(userRemark.getOtherRelated());
					comment.setEffectiveComment(userRemark.isEffectiveComment());
					comment.setAnnotationed(true);
					this.userCommentService.update(comment);
				}
			}
			System.out.println(userRemarks.size());
		}
	}

}
