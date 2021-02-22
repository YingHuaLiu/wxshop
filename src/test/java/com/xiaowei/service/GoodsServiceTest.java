package com.xiaowei.service;

import com.xiaowei.dao.GoodsMapper;
import com.xiaowei.dao.ShopMapper;
import com.xiaowei.entity.*;
import org.apache.shiro.util.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodsServiceTest {
    @Mock
    private GoodsMapper goodsMapper;
    @Mock
    private ShopMapper shopMapper;

    @InjectMocks
    private GoodsService goodsService;

    @Mock
    private Shop shop;
    @Mock
    private Goods goods;

    @BeforeEach
    public void initUserContext() {
        User user = new User();
        user.setId(1L);
        UserContext.setCurrentUser(user);

        lenient().when(shopMapper.selectByPrimaryKey(anyLong())).thenReturn(shop);
    }

    @AfterEach
    public void clearUserContext() {
        UserContext.setCurrentUser(null);
    }

    @Test
    public void createGoodsSuccessedIfUserIsOwner() {
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.insert(goods)).thenReturn(123);

        Assertions.assertEquals(goods,goodsService.createGoods(goods));

        verify(goods).setId(123L);
    }

    @Test
    public void createGoodsFailedIfUserIsNotOwner() {
        when(shop.getOwnerUserId()).thenReturn(2L);

        Assertions.assertThrows(GoodsService.NotAuthorizedForShopException.class,()->{
            goodsService.createGoods(goods);
        });
    }

    @Test
    public void deleteGoodsThrowExceptionIfGoodsNotFound() {
        long goodsToBeDeleted = 123;
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(null);

        Assertions.assertThrows(GoodsService.ResourceNotFoundException.class,()->{
            goodsService.deleteGoodsById(goodsToBeDeleted);
        });
    }

    @Test
    public void deleteGoodsThrowExceptionIfUserIsNotOwner() {
        when(shop.getOwnerUserId()).thenReturn(2L);

        Assertions.assertThrows(GoodsService.NotAuthorizedForShopException.class,()->{
            goodsService.deleteGoodsById(anyLong());
        });
    }

    @Test
    public void deleteGoodsSuccessed() {
        long goodsToBeDeleted = 123;
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.selectByPrimaryKey(goodsToBeDeleted)).thenReturn(goods);

        goodsService.deleteGoodsById(goodsToBeDeleted);
        verify(goods).setStatus(DataStatus.DELETED.getName());
    }

    @Test
    public void getGoodsSuccessedWithNullShopId() {
        int pageNum=5;
        int pageSize=10;
        List<Goods> mockData=mock(List.class);

        when(goodsMapper.countByExample(any())).thenReturn(55L);
        when(goodsMapper.selectByExample(any())).thenReturn(mockData);
        PageResponse<Goods> result = goodsService.getGoods(pageNum, pageSize, null);

        Assertions.assertEquals(6,result.getTotalPage());
        Assertions.assertEquals(5,result.getPageNum());
        Assertions.assertEquals(10,result.getPageSize());
        Assertions.assertEquals(mockData,result.getData());
    }

    @Test
    public void updateGoodsSuccessed() {
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.updateByExample(any(),any())).thenReturn(1);

        Assertions.assertEquals(goods,goodsService.updateGoods(goods));
    }
}