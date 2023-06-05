package com.zq.admin.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zq.admin.modules.system.domain.Dept;
import com.zq.admin.modules.system.domain.Job;
import com.zq.admin.modules.system.domain.Role;
import com.zq.admin.modules.system.domain.User;
import com.zq.admin.modules.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wilmiam
 * @since 2022/10/11 18:54
 */
@RequiredArgsConstructor
@Service
public class UserManager {

    private final DeptService deptService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.profiles.active:dev}")
    private String active;

    public void addUser(String username) {
        if ("dev".equals(active)) {
            return;
        }
        String resultStr = HttpUtil.get("http://147.1.6.23/updatelist/getUserInfo/" + username.split("@")[0]);
        JSONObject userObj = JSONUtil.parseObj(resultStr);
        Integer xb = userObj.getInt("XB", 0);

        Dept dept = deptService.getById(userObj.getLong("orgId"));
        if (dept == null) {
            dept = addDept(userObj.getStr("JGBS"));
        }

        // 默认角色
        Set<Role> roleSet = new HashSet<>();
        Role role = new Role();
        role.setId(3L);
        roleSet.add(role);

        // 默认岗位
        Set<Job> jobSet = new HashSet<>();
        Job job = new Job();
        job.setId(3L);
        jobSet.add(job);

        User user = new User();
        user.setId(userObj.getLong("userId"));
        user.setDept(dept);
        user.setRoles(roleSet);
        user.setJobs(jobSet);
        user.setUsername(userObj.getStr("YOUXIANG"));
        user.setNickName(userObj.getStr("XM"));
        user.setEmail(userObj.getStr("YOUXIANG"));
        user.setPhone(userObj.getStr("SJHM"));
        user.setGender(xb == 1 ? "男" : xb == 2 ? "女" : "未知");
        user.setPassword(passwordEncoder.encode("Gxfy2022"));
        user.setEnabled(userObj.getInt("YX") == 1);
        user.setIsAdmin(false);
        user.setGradingCode(userObj.getStr("FY")); //法院分级码
        user.setUserCode(userObj.getStr("RYBS")); // 人员标识
        user.setIdCard(userObj.getStr("SFZHM")); // 身份证号码
        user.setCreateTime(new Timestamp(userObj.getDate("CJSJ", new Date()).getTime()));
        user.setUpdateTime(new Timestamp(userObj.getDate("GXSJ", new Date()).getTime()));

        userService.create(user);
    }

    private Dept addDept(String orgBs) {
        String resultStr = HttpUtil.get("http://147.1.6.23/updatelist/getOrgInfo/" + orgBs);
        JSONObject orgObj = JSONUtil.parseObj(resultStr);

        //获取部门id（机构id）
        Long deptId = orgObj.getLong("orgId");
        //获取上级部门id
        Long pid = orgObj.getLong("parentOrgId");
        //获取机构标识
        String orgCodes = orgObj.getStr("JGBS");
        //获取法院代码
        String gradingCode = orgObj.getStr("FY");

        Dept dept = new Dept();
        dept.setId(deptId);
        dept.setDeptSort(orgObj.getInt("PXH"));
        dept.setName(orgObj.getStr("MC"));
        dept.setEnabled(orgObj.getInt("YX") == 1);
        dept.setPid(pid);
        dept.setGradingCode(gradingCode); // 法院分级码
        dept.setOrgCode(orgCodes); // 机构标识
        dept.setAbbreviation(orgObj.getStr("JC")); // 机构简称
        dept.setCreateTime(new Timestamp(orgObj.getDate("CJSJ", new Date()).getTime()));
        dept.setUpdateTime(new Timestamp(orgObj.getDate("GXSJ", new Date()).getTime()));

        deptService.create(dept);

        return dept;
    }

    public void updateUser(String username) {
        if ("dev".equals(active)) {
            return;
        }
        String resultStr = HttpUtil.get("http://147.1.6.23/updatelist/getUserInfo/" + username.split("@")[0]);
        JSONObject userObj = JSONUtil.parseObj(resultStr);
        Integer xb = userObj.getInt("XB", 0);

        User user = userRepository.findByUsername(username);
        user.setNickName(userObj.getStr("XM"));
        user.setPhone(userObj.getStr("SJHM"));
        user.setGender(xb == 1 ? "男" : xb == 2 ? "女" : "未知");
        user.setEnabled(userObj.getInt("YX") == 1);
        user.setGradingCode(userObj.getStr("FY"));
        user.setUserCode(userObj.getStr("RYBS"));
        user.setIdCard(userObj.getStr("SFZHM"));
        user.setUpdateTime(new Timestamp(userObj.getDate("GXSJ", new Date()).getTime()));
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode("Gxfy2022"));
        }

        userRepository.save(user);
    }

    /**
     * 添加默认角色
     *
     * @param userId
     */
    public boolean addDefaultRoleAndJob(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Set<Role> roleSet = new HashSet<>();
            Role role = new Role();
            role.setId(3L); // 公共角色ID
            roleSet.add(role);

            Set<Job> jobSet = new HashSet<>();
            Job job = new Job();
            job.setId(3L); // 其他岗位ID
            jobSet.add(job);

            Set<Role> roles = user.getRoles();
            if (CollUtil.isNotEmpty(roles) || !roles.contains(role)) {
                roleSet.addAll(roles);
            }
            Set<Job> jobs = user.getJobs();
            if (CollUtil.isNotEmpty(jobs) || !jobs.contains(job)) {
                jobSet.addAll(jobs);
            }

            user.setRoles(roleSet);
            user.setJobs(jobSet);

            userRepository.save(user);

            return true;
        }

        return false;
    }

}
