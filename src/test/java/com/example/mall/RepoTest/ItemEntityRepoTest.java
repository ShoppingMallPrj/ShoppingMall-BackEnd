package com.example.mall.RepoTest;


import com.example.mall.Repository.ItemRepository;
import com.example.mall.Service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemEntityRepoTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    //테스트 값
    String  itemName     = "testItem";
    String  itemDes      = "tesDes";
    float   itemStars    = 2.5f;
    int   itemPrice    = 3000;
    String  itemBrand    = "testBrand";
    String  itemCategory = "testCategory";
    String  itemImage    = "testSrc";
    int     itemStock    = 10;


//    ItemEntity create() {
//
//        ItemUploadDto itemUploadDto = new ItemUploadDto();
//
//        itemUploadDto.setItemName(itemName);
//        itemUploadDto.setItemBrand(itemBrand);
//        itemUploadDto.setItemStock(itemStock);
//        itemUploadDto.setItemCategory(itemCategory);
//        itemUploadDto.setItemPrice(itemPrice);
//        itemUploadDto.setItemDescription(itemDes);
//
//        ItemEntity itemEntity = ItemEntity.from(itemUploadDto);
//
//        return itemEntity;
//    }
//    //테스트용 아이템을 생성
//    @Test
//    @DisplayName("아이템 저장 테스트")
//    public void saveItemTest(){
//
//        ItemEntity itemEntity = create();
//        ItemEntity result = itemRepository.save(itemEntity);
//
//        assertThat(result.getItemName()).isEqualTo(itemName);
//        assertThat(result.getItemBrand()).isEqualTo(itemBrand);
//        assertThat(result.getItemCategory()).isEqualTo(itemCategory);
//        assertThat(result.getItemPrice()).isEqualTo(itemPrice);
//        assertThat(result.getItemDescription()).isEqualTo(itemDes);
//
//        System.out.println(result);
//        itemRepository.delete(result);
//    }
//
//    @AfterEach
//    public void deleteTestItem(){
//
//        itemRepository.deleteByItemName(itemName);
//    }

    @Test
    @DisplayName("findPriceByIdTest")
    public void findPriceByIdTest(){

//        ItemEntity itemEntity = itemRepository.getByItemName(itemName);
//        System.out.println(itemEntity);
//        float price = itemRepository.getItemPriceByItemId(itemEntity.getItemId());
//
//
//        assertThat(price).isEqualTo(itemPrice);
    }
}
