package com.xiaowei.service;

import com.xiaowei.dao.GoodsMapper;
import com.xiaowei.dao.ShopMapper;
import com.xiaowei.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GoodsService {
    private GoodsMapper goodsMapper;
    private ShopMapper shopMapper;

    @Autowired
    public GoodsService(GoodsMapper goodsMapper, ShopMapper shopMapper) {
        this.goodsMapper = goodsMapper;
        this.shopMapper = shopMapper;
    }

    public Goods createGoods(Goods goods) {
        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            long id = goodsMapper.insert(goods);
            goods.setId(id);
            return goods;
        } else {
            throw HttpException.forbidden("无权访问");
        }
    }

    public Goods deleteGoodsById(Long goodsId) {
        //todo
        Shop shop = shopMapper.selectByPrimaryKey(goodsId);
        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
            if (goods == null) {
                throw HttpException.notFound("商品未找到");
            }
            goods.setStatus(DataStatus.DELETED.getName());
            goodsMapper.updateByPrimaryKey(goods);
            return goods;
        } else {
            throw HttpException.forbidden("无权访问");
        }
    }

    public Goods updateGoods(Goods goods) {
        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            GoodsExample goodsExample = new GoodsExample();
            goodsExample.createCriteria().andIdEqualTo(goods.getId());
            int affectedRows = goodsMapper.updateByExample(goods, goodsExample);
            if (affectedRows == 0) {
                throw HttpException.notFound("未找到");
            }
            return goods;
        } else {
            throw HttpException.forbidden("无权访问");
        }
    }

    public PageResponse<Goods> getGoods(Integer pageNum, Integer pageSize, Integer shopId) {
        int totalNumber = countGoods(shopId);
        int totalPage = totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1;
        GoodsExample page = new GoodsExample();
        page.setLimit(pageSize);
        page.setOffset((pageNum - 1) * pageSize);

        List<Goods> pagedGoods = goodsMapper.selectByExample(page);
        return PageResponse.pageData(pageNum, pageSize, totalPage, pagedGoods);
    }

    private int countGoods(Integer shopId) {
        GoodsExample goodsExample = new GoodsExample();
        goodsExample.createCriteria().andStatusEqualTo(DataStatus.OK.getName());
        if (shopId != null) {
            goodsExample.createCriteria().andShopIdEqualTo(shopId.longValue());
        }
        return (int) goodsMapper.countByExample(goodsExample);
    }
}
