/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
 * OkStack is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.system.mapper;

import org.okstar.platform.system.domain.SysDept;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


/**
 * 部门管理 数据层
 */
@Singleton
public class SysDeptMapper {
    @Inject
    EntityManager entityManager;

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept) {
        return entityManager.createQuery("from SysDept").getResultList();
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(Long roleId, boolean deptCheckStrictly){
        return Collections.emptyList();
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(Long deptId){
        return entityManager.find(SysDept.class, deptId);
    }

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(Long deptId){
        return entityManager.createQuery("From SysDept where deptId=:deptId")
                .setParameter("deptId", deptId)
                .getResultList();
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId){
        return 0;
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(Long deptId){
        return 0;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(Long deptId){
        return 0;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(String deptName, Long parentId){
        return null;
    }

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept){
        entityManager.persist(dept);
        return 1;
    }

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept){
         entityManager.merge(dept);
        return 1;
    }

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    public void updateDeptStatusNormal(Long[] deptIds){

    }

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(List<SysDept> depts){
        return 0;
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId){
        entityManager.remove(entityManager.find(SysDept.class,deptId));
        return 1;
    }
}
