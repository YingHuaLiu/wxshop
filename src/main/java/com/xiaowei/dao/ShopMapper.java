package com.xiaowei.dao;

import com.xiaowei.entity.Shop;
import com.xiaowei.entity.ShopExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    long countByExample(ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int deleteByExample(ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int insert(Shop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int insertSelective(Shop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    List<Shop> selectByExample(ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    Shop selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int updateByExampleSelective(@Param("record") Shop record, @Param("example") ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int updateByExample(@Param("record") Shop record, @Param("example") ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int updateByPrimaryKeySelective(Shop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop
     *
     * @mbg.generated Mon Feb 22 14:54:25 CST 2021
     */
    int updateByPrimaryKey(Shop record);
}