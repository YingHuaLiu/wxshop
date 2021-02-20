package com.xiaowei.service;

import com.xiaowei.dao.GoodsDao;
import com.xiaowei.dao.ShopDao;
import com.xiaowei.entity.Goods;
import com.xiaowei.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GoodsService {
    private GoodsDao goodsDao;
    private ShopDao shopDao;

    @Autowired
    public GoodsService(GoodsDao goodsDao, ShopDao shopDao) {
        this.goodsDao = goodsDao;
        this.shopDao = shopDao;
    }

    public Goods createGoods(Goods goods) {
        Shop shop = shopDao.findShopById(goods.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            return goodsDao.insertGoods(goods);
        }else{
            throw new NotAuthorizedForShopException("无权访问");
        }
    }

    public Goods deleteGoodsById(Long id) {
        Shop shop = shopDao.findShopById(id);
        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            return goodsDao.deleteGoodsById(id);
        }else{
            throw new NotAuthorizedForShopException("无权访问");
        }
    }

    public static class NotAuthorizedForShopException extends RuntimeException {
        public NotAuthorizedForShopException(String message) {
            super(message);
        }
    }
}
