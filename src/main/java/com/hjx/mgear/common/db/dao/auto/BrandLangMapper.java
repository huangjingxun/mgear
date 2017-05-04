package com.hjx.mgear.common.db.dao.auto;

import com.hjx.mgear.common.db.entity.auto.BrandLang;
import com.hjx.mgear.common.db.entity.auto.BrandLangExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BrandLangMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    long countByExample(BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int deleteByExample(BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int insert(BrandLang record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int insertSelective(BrandLang record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    List<BrandLang> selectByExampleWithBLOBsWithRowbounds(BrandLangExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    List<BrandLang> selectByExampleWithBLOBs(BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    List<BrandLang> selectByExampleWithRowbounds(BrandLangExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    List<BrandLang> selectByExample(BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    BrandLang selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BrandLang record, @Param("example") BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") BrandLang record, @Param("example") BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int updateByExample(@Param("record") BrandLang record, @Param("example") BrandLangExample example);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BrandLang record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(BrandLang record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table brand_lang
     * @mbg.generated
     */
    int updateByPrimaryKey(BrandLang record);
}