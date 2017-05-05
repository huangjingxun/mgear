package com.hjx.mgear.platform.fcmoto.db.dao.auto;

import com.hjx.mgear.platform.fcmoto.db.entity.auto.FcmCategory;
import com.hjx.mgear.platform.fcmoto.db.entity.auto.FcmCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface FcmCategoryMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    long countByExample(FcmCategoryExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int deleteByExample(FcmCategoryExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int insert(FcmCategory record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int insertSelective(FcmCategory record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    List<FcmCategory> selectByExampleWithRowbounds(FcmCategoryExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    List<FcmCategory> selectByExample(FcmCategoryExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    FcmCategory selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") FcmCategory record, @Param("example") FcmCategoryExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int updateByExample(@Param("record") FcmCategory record, @Param("example") FcmCategoryExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FcmCategory record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_category
     * @mbg.generated
     */
    int updateByPrimaryKey(FcmCategory record);
}