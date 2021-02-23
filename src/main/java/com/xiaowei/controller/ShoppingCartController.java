package com.xiaowei.controller;

import com.xiaowei.entity.PageResponse;
import com.xiaowei.entity.Shop;
import com.xiaowei.entity.ShoppingCartGoods;
import com.xiaowei.service.ShoppingCartService;
import com.xiaowei.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/shoppingCart")
    public PageResponse<ShoppingCartData> getShoppingCart(@RequestParam("pageNum") int pageNum,
                                                          @RequestParam("pageSize") int pageSize) {
        return shoppingCartService.getShoppingCartOfUser(UserContext.getCurrentUser().getId(),
                pageNum, pageSize);
    }

    public static class ShoppingCartData {
        Shop shop;
        List<ShoppingCartGoods> goods;

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }

        public List<ShoppingCartGoods> getGoods() {
            return goods;
        }

        public void setGoods(List<ShoppingCartGoods> goods) {
            this.goods = goods;
        }
    }

//    @PostMapping("/shoppingCart")
//    public void addToShoppingCart(@RequestBody) {
//
//    }

    public static class AddToShoppingCartRequest {
        List<AddToShoppingCartItem> goods;

        public List<AddToShoppingCartItem> getGoods() {
            return goods;
        }

        public void setGoods(List<AddToShoppingCartItem> goods) {
            this.goods = goods;
        }
    }

    public static class AddToShoppingCartItem {
        long id;
        int number;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
