package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.dao.UserEntityDao.SqlProvider;
import cn.eeepay.framework.db.pagination.Page;

public interface UserDao {

	@SelectProvider(type=SqlProvider.class, method="selectUserByCondition")
	@ResultType(UserInfo.class)
	List<UserInfo> selectUserByCondition(@Param("user") UserInfo user, Page<UserInfo> page);

	@Select("select id,real_name,dept_id from boss_shiro_user where user_name=#{userName} and status=1")
	@ResultType(UserInfo.class)
	UserInfo getUserInfoByUserName(@Param("userName")String userName);


	@Select("select id,user_name,real_name from boss_shiro_user where dept_id=#{deptId}")
	@ResultType(List.class)
	List<UserInfo> getUsersByDeptNo(@Param("deptId")Integer deptId);
	
	@Insert("insert into boss_shiro_user(user_name,password,real_name,tel_no,email,status,create_operator,dept_id) "
			+ " values(#{user.userName},#{user.password},#{user.realName},#{user.telNo},#{user.email},#{user.status},#{user.createOperator},#{user.deptId})")
	int saveUser(@Param("user")UserInfo user);
	
	@Select("select 1 from boss_shiro_user where user_name=#{userName} limit 1")
	@ResultType(Integer.class)
	Integer checkNameUnique(@Param("userName")String userName);

	@Update("update boss_shiro_user set real_name=#{user.realName},tel_no=#{user.telNo},email=#{user.email},status=#{user.status},dept_id=#{user.deptId} "
			+ " where id=#{user.id}")
	int updateUser(@Param("user")UserInfo user);
	
	@Update("update boss_shiro_user set tel_no=#{user.newTelNo} "
			+ " where tel_no=#{telNo}")
	int updateUsertelNo(@Param("telNo")String telNo,@Param("newTelNo")String newTelNo);

	@Update("update boss_shiro_user set password=#{user.password},update_pwd_time=#{user.updatePwdTime} where id=#{user.id}")
	int restPwd(@Param("user")UserInfo user);
	
	@Update("update user_info set password=#{user.password},update_pwd_time=#{user.updatePwdTime} where mobilephone=#{user.telNo} and team_id=#{user.teamId}")
	int merchantRestPwd(@Param("user")UserInfo user);
	
	@Select("select * from user_info where mobilephone=#{telNo} and team_id=#{teamId}")
	@ResultType(UserInfo.class)
	UserInfo selectInfoByTelNo(@Param("telNo")String telNo,@Param("teamId")String teamId);
	
	@Update("update user_info set password=#{pwd},mobilephone=#{newTelNo},pay_pwd='',update_pwd_time=now() where "
			+ "mobilephone=#{telNo} and team_id=#{teamId}")
	int updateInfoByMp(@Param("telNo")String telNo,@Param("newTelNo")String newTelNo,@Param("teamId")String teamId,@Param("pwd")String pwd);
	
	@Select("select * from boss_shiro_user")
	@ResultType(UserInfo.class)
	List<UserInfo> findUserBox();
	
	@Select("select * from boss_shiro_user where id=#{id}")
	@ResultType(UserInfo.class)
	UserInfo selectInfo(@Param("id")Integer id);
	
	@Delete("DELETE FROM boss_user_role WHERE user_id=#{userId}")
	int delUserRoles(Integer userId);
	@Delete("DELETE FROM boss_user_right WHERE user_id=#{userId}")
	int delUserRights(Integer userId);
	@Delete("DELETE FROM boss_shiro_user WHERE id=#{userId}")
	int delUser(Integer userId);

	@Delete("DELETE FROM user_info WHERE id=#{id}")
	int delPerMer(Integer id);
	
	@Select("SELECT r.id,r.role_code,r.role_name,r.role_remark FROM boss_user_role ur,boss_shiro_role r WHERE r.id=ur.role_id AND ur.user_id=#{id}")
	@ResultType(RoleInfo.class)
	List<RoleInfo> getRolesByUser(@Param("id")Integer id);
	
	@Select("select r.* from boss_shiro_right r , boss_user_right ur where r.id=ur.right_id and ur.user_id=#{id}")
	@ResultType(RightInfo.class)
	List<RightInfo> getRightsByUser(@Param("id")Integer id);
	
	@Select("select id,user_name,real_name,status from boss_shiro_user")
	@ResultType(UserInfo.class)
	List<UserInfo> getAllUsers();
	
	@Select("select m.id,m.menu_name,m.menu_url,m.menu_level,m.menu_type from boss_sys_menu m,boss_user_menu um where um.user_id=#{id} and m.id=um.menu_id")
	@ResultType(MenuInfo.class)
	List<MenuInfo> getMenuByUser(Integer id);

	@UpdateProvider(type=SqlProvider.class, method="updateStatusBatch")
	int updateStatusBatch(@Param("list")List<UserEntityInfo> userEntityInfoList, @Param("status")String status);
	
	//待完善商户信息查询
	@SelectProvider(type=SqlProvider.class, method="perfectMerchantQuery")
	@ResultType(UserInfo.class)
	List<UserInfo> perfectMerchantQuery(@Param("user") UserInfo user, Page<UserInfo> page);

	@Select("select * from boss_shiro_user where status=1 and user_name=#{userName}")
	@ResultType(UserInfo.class)
	UserInfo selectUserByUserName(@Param("userName")String userName);
	
	@Select("SELECT u.* FROM boss_shiro_user u, boss_user_right ur, boss_right_menu rm, boss_sys_menu m"
			+" WHERE"
			+" u.id = ur.user_id"
			+" AND rm.right_id = ur.right_id"
			+" AND m.id = rm.menu_id"
			+" AND m.menu_code = #{menuCode}")
	@ResultType(UserInfo.class)	
	List<UserInfo> selectUserByMenuCode(String menuCode);

	@Select("select team_id from user_info where user_id = #{userId}")
	@ResultType(UserInfo.class)
	UserInfo getTeamId(@Param("userId")String userId);

	//查询少量的用户
	@SelectProvider(type=SqlProvider.class, method="getUserlimitSql")
	@ResultType(UserInfo.class)
    List<UserInfo> getUserlimit(@Param("user")UserInfo user);

	@Select("select * from boss_shiro_user where id = #{id}")
	@ResultType(UserInfo.class)
	UserInfo getUserInfoById(@Param("id")Integer id);

	@Select("select real_name from boss_shiro_user where user_name in (${userNames})")
	@ResultType(String.class)
    List<String> getRealNameByUserName(@Param("userNames") String userNames);

    public class SqlProvider{
		public String selectUserByCondition(Map<String, Object> param){
			final UserInfo user = (UserInfo)param.get("user");
			return new SQL(){
				{
					SELECT("*");
					FROM("boss_shiro_user");
					if(user!= null){
						if(StringUtils.isNotBlank(user.getUserName())){
							user.setUserName(user.getUserName()+"%");
							WHERE("user_name like #{user.userName}");
						}
						if(user.getRealName()!=null){
							user.setRealName(user.getRealName()+"%");
							WHERE("real_name like #{user.realName}");
						}
						if(StringUtils.isNotBlank(user.getEmail())){
							user.setEmail(user.getEmail()+"%");
							WHERE("email like #{user.email}");
						}
						if(StringUtils.isNotBlank(user.getTelNo())){
							user.setTelNo(user.getTelNo()+"%");
							WHERE("tel_no like #{user.telNo}");
						}
						if(StringUtil.isNotBlank(user.getDeptId())){
							WHERE("dept_id=#{user.deptId}");
						}
					}
				}
			}.toString();
		}
		
		public String updateStatusBatch(Map<String, Object>param){
			StringBuilder sb = new StringBuilder("update user_info set status=#{status} where user_id in(");
			List<UserEntityInfo> list = (List<UserEntityInfo>) param.get("list");
			MessageFormat message = new MessageFormat("#'{'list[{0}].userId},");
			for(int i=0;i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			sb.append(")");
			System.out.println("批量更新user_info："+ sb.toString());
			return sb.toString();
		}
		
		public String perfectMerchantQuery(Map<String, Object> param){
			final UserInfo user = (UserInfo)param.get("user");
			return new SQL(){
				{
					SELECT("uis.id,uis.mobilephone as telNo,tis.team_name as teamId,uis.create_time");
					FROM("user_info uis");
					LEFT_OUTER_JOIN("team_info tis on tis.team_id=uis.team_id");
					LEFT_OUTER_JOIN("user_entity_info uei on uei.user_id=uis.user_id");
					WHERE("uei.`status`='1'");
					WHERE("uei.entity_id is null");
					if(StringUtils.isNotBlank(user.getTelNo())){
						WHERE("uis.mobilephone = #{user.telNo}");
					}
					if(user.getCreateTime()!=null){
						WHERE("uis.create_time >= #{user.createTime}");
					}
					if(user.getUpdatePwdTime()!=null){
						WHERE("uis.update_pwd_time <= #{user.updatePwdTime}");
					}
					if(StringUtils.isNotBlank(user.getTeamId())){
						WHERE("uis.team_id = #{user.teamId}");
					}
				}
			}.toString();
		}

		public String getUserlimitSql(Map<String, Object> param){
			final UserInfo user = (UserInfo)param.get("user");
			StringBuffer sb=new StringBuffer();
			sb.append(" select id,user_name,real_name,tel_no,status,email ");
			sb.append(" From boss_shiro_user ");
			sb.append(" where status=1 ");
			sb.append("     and tel_no is not null ");
			sb.append("     and tel_no !='' ");
			if(user.getTelNo()!=null&&!"".equals(user.getTelNo())){
				sb.append(" and tel_no like '"+user.getTelNo()+"%'");
			}
			sb.append(" limit 0,100");
			return sb.toString();
		}
	}

}
