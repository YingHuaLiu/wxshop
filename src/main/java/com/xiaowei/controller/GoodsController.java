package com.xiaowei.controller;

import com.xiaowei.entity.Goods;
import com.xiaowei.entity.HttpException;
import com.xiaowei.entity.PageResponse;
import com.xiaowei.entity.Response;
import com.xiaowei.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class GoodsController {
    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/goods")
    @ResponseBody
    public Response<Goods> createGoods(@RequestBody Goods goods, HttpServletResponse response) {
        clean(goods);
        response.setStatus(HttpServletResponse.SC_CREATED);
        try {
            return Response.of(goodsService.createGoods(goods));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(null, e.getMessage());
        }
    }

    @GetMapping("/goods")
    @ResponseBody
    public PageResponse<Goods> getGoods(@RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize")Integer pageSize,
                                 @RequestParam(value="shopId",required = false)Integer shopId) {
        return goodsService.getGoods(pageNum,pageSize,shopId);
    }

    @DeleteMapping("/goods/{id}")
    @ResponseBody
    public Response<Goods> deleteGoods(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return Response.of(goodsService.deleteGoodsById(id));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(null, e.getMessage());
        }
    }

    @PutMapping("/api/v1/update")
    @ResponseBody
    public Response<Goods> updateGoods(Goods goods, HttpServletResponse response) {
        try {
            return Response.of(goodsService.updateGoods(goods));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(null, e.getMessage());
        }
    }


    private void clean(Goods goods) {
        goods.setId(null);
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
    }
}
