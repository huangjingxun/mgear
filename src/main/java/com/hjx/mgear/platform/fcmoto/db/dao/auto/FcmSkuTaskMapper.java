package com.hjx.mgear.platform.fcmoto.db.dao.auto;

import com.hjx.mgear.platform.fcmoto.db.entity.auto.FcmSku;
import com.hjx.mgear.platform.fcmoto.db.entity.auto.FcmSkuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface FcmSkuTaskMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    long countByExample(FcmSkuExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int deleteByExample(FcmSkuExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer skuId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int insert(FcmSku record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int insertSelective(FcmSku record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    List<FcmSku> selectByExampleWithRowbounds(FcmSkuExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    List<FcmSku> selectByExample(FcmSkuExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    FcmSku selectByPrimaryKey(Integer skuId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") FcmSku record, @Param("example") FcmSkuExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int updateByExample(@Param("record") FcmSku record, @Param("example") FcmSkuExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FcmSku record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_sku
     * @mbg.generated
     */
    int updateByPrimaryKey(FcmSku record);
}