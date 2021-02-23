package com.xiaowei.service;

import com.xiaowei.controller.ShoppingCartController;
import com.xiaowei.entity.PageResponse;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    public PageResponse<ShoppingCartController.ShoppingCartData> getShoppingCartOfUser(Long userId, int pageNum, int pageSize) {
        return null;
    }
}
