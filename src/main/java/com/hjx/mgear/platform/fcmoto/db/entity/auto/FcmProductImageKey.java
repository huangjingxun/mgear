package com.hjx.mgear.platform.fcmoto.db.entity.auto;

import java.io.Serializable;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table fcm_product_image
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class FcmProductImageKey implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fcm_product_image.product_id
     * @mbg.generated
     */
    private Integer productId;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fcm_product_image.image_id
     * @mbg.generated
     */
    private Integer imageId;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table fcm_product_image
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fcm_product_image.product_id
     * @return  the value of fcm_product_image.product_id
     * @mbg.generated
     */
    public Integer getProductId() {

        return productId;
    }

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_product_image
     * @mbg.generated
     */
    public FcmProductImageKey withProductId(Integer productId) {

        this.setProductId(productId);
        return this;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fcm_product_image.product_id
     * @param productId  the value for fcm_product_image.product_id
     * @mbg.generated
     */
    public void setProductId(Integer productId) {

        this.productId = productId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fcm_product_image.image_id
     * @return  the value of fcm_product_image.image_id
     * @mbg.generated
     */
    public Integer getImageId() {

        return imageId;
    }

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_product_image
     * @mbg.generated
     */
    public FcmProductImageKey withImageId(Integer imageId) {

        this.setImageId(imageId);
        return this;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fcm_product_image.image_id
     * @param imageId  the value for fcm_product_image.image_id
     * @mbg.generated
     */
    public void setImageId(Integer imageId) {

        this.imageId = imageId;
    }

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_product_image
     * @mbg.generated
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", productId=").append(productId);
        sb.append(", imageId=").append(imageId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_product_image
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {

        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FcmProductImageKey other = (FcmProductImageKey) that;
        return (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId())) && (this.getImageId() == null ? other.getImageId() == null
                                                                                                                                                             : this.getImageId().equals(other
                                                                                                                                                                                             .getImageId()));
    }

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table fcm_product_image
     * @mbg.generated
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        return result;
    }
}