package com.xiaowei.service;

import com.xiaowei.dao.ShopMapper;
import com.xiaowei.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ShopService {
    private ShopMapper shopMapper;

    @Autowired
    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public PageResponse<Shop> getShopByUserId(Long userId, int pageNum, int pageSize) {
        ShopExample countByStatus = new ShopExample();
        countByStatus.createCriteria().andStatusEqualTo(DataStatus.OK.getName());
        int totalNumber = (int) shopMapper.countByExample(countByStatus);
        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;

        ShopExample pageCondition = new ShopExample();
        pageCondition.createCriteria().andStatusEqualTo(DataStatus.OK.getName());
        pageCondition.setLimit(pageSize);
        pageCondition.setOffset((pageNum - 1) * pageSize);

        List<Shop> pagedShops = shopMapper.selectByExample(pageCondition);

        return PageResponse.pageData(pageNum, pageSize, totalPage, pagedShops);
    }

    public Shop createShop(Shop shop, Long creatorId) {
        shop.setId(creatorId);
        shop.setStatus(DataStatus.OK.getName());
        shop.setCreatedAt(new Date());
        shop.setUpdatedAt(new Date());
        long shopId = shopMapper.insert(shop);
        shop.setId(shopId);

        return shop;
    }

    public Shop updateShop(Shop shop, Long userId) {
        Shop shopInDataBase = shopMapper.selectByPrimaryKey(shop.getId());
        if (shopInDataBase == null) {
            throw HttpException.notFound("店铺未找到！");
        }
        if (!Objects.equals(shopInDataBase.getOwnerUserId(), userId)) {
            throw HttpException.forbidden("无权访问！");
        }
        shop.setUpdatedAt(new Date());
        shopMapper.updateByPrimaryKey(shop);
        return shop;
    }

    public Shop deleteShop(Long shopId, Long userId) {
        Shop shopInDataBase = shopMapper.selectByPrimaryKey(shopId);
        if (shopInDataBase == null) {
            throw HttpException.notFound("店铺未找到！");
        }
        if (!Objects.equals(shopInDataBase.getOwnerUserId(), userId)) {
            throw HttpException.forbidden("无权访问！");
        }
        shopInDataBase.setStatus(DataStatus.DELETED.getName());
        shopInDataBase.setUpdatedAt(new Date());
        shopMapper.updateByPrimaryKey(shopInDataBase);
        return shopInDataBase;
    }


}
