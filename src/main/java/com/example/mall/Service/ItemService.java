package com.example.mall.Service;


import com.example.mall.Dto.Request.Item.ItemOptionDto;
import com.example.mall.Dto.Request.Item.ItemUploadDto;
import com.example.mall.Dto.Response.Item.ItemListDto;
import com.example.mall.Dto.Response.Item.ItemDto;
import com.example.mall.Dto.Response.Item.ItemReviewDto;
import com.example.mall.Entity.ItemEntity;
import com.example.mall.Entity.ItemImageEntity;
import com.example.mall.Entity.ItemOptionEntity;
import com.example.mall.Entity.ItemReviewEntity;
import com.example.mall.Repository.ItemImageRepository;
import com.example.mall.Repository.ItemOptionRepository;
import com.example.mall.Repository.ItemRepository;
import com.example.mall.Repository.ItemReviewRepository;
import com.example.mall.Util.ModelMapperBean;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemReviewRepository itemReviewRepository;

    @Autowired
    ItemOptionRepository itemOptionRepository;

    @Autowired
    ItemImageRepository itemImageRepository;

    @Autowired
    ServiceS3 serviceS3;

    @Autowired
    ModelMapperBean modelMapperBean;

    //상품을 생성한다.
    @Transactional
    public void createItem(ItemUploadDto itemUploadDto) throws IOException, ParseException {

        System.out.println(itemUploadDto);

        //아이템 엔티티 생성
        ItemEntity itemEntity = new ItemEntity();

        itemEntity.setItemName(itemUploadDto.getItemName());
        itemEntity.setCategory(itemUploadDto.getItemCategory());
        itemEntity.setGender(itemUploadDto.getGender());
        itemEntity.setItemDescription(itemUploadDto.getItemDescription());
        itemEntity.setItemPrice(itemUploadDto.getItemPrice());
        itemEntity.setDeleted(false);
        itemEntity.setItemProfile("");

        itemRepository.save(itemEntity);

        //프로필 이미지 업로드
        String profileSrc = serviceS3.upload(itemUploadDto.getProfile());
        itemEntity.setItemProfile(profileSrc);

//        //아이템의 이미지들을 생성
//        Set<ItemImageEntity> itemImageEntities = new HashSet<>();
//
//        for (MultipartFile file : itemUploadDto.getFile()) {
//
//            ItemImageEntity itemImageEntity = new ItemImageEntity();
//            itemImageEntity.setItemId(itemEntity.getItemId());
//            String src = serviceS3.upload(file);
//
//            itemImageEntity.setImageSrc(src);
//            itemImageEntities.add(itemImageEntity);
//        }
//
//        itemEntity.setImage(itemImageEntities);
//        itemImageRepository.saveAll(itemImageEntities);

        //상품의 옵션들 처리
        Set<ItemOptionEntity> itemOptionEntities = new HashSet<>();
        JSONParser jsonParser = new JSONParser();
        System.out.println("option : " + Arrays.toString(itemUploadDto.getOption()));
        System.out.println("length : " + itemUploadDto.getOption().length);

        //배열을 하나만 보냈을 경우 예외처리
       if(!itemUploadDto.getOption()[0].endsWith("}")){
            itemUploadDto.setOption(new String[]{itemUploadDto.getOption()[0]+ ", " + itemUploadDto.getOption()[1]});
        }

        for (String option : itemUploadDto.getOption()) {

            ItemOptionEntity itemOptionEntity = new ItemOptionEntity();

            System.out.println("option : " + itemUploadDto.getOption()[0]);

            JSONObject jsonObj = (JSONObject) jsonParser.parse(option);
            ItemOptionDto itemOptionDto = modelMapperBean.modelMapper().map(jsonObj, ItemOptionDto.class);

            itemOptionEntity.setItemId(itemEntity.getItemId());
            itemOptionEntity.setOptionContent(itemOptionDto.getOptionContent());
            itemOptionEntity.setOptionStock(itemOptionDto.getOptionStock());

            itemOptionEntities.add(itemOptionEntity);
        }

        itemEntity.setOption(itemOptionEntities);
        itemOptionRepository.saveAll(itemOptionEntities);

    }    // 상품 등록

    //모든 상품을 가져온다.
//    public Page<ItemListOutDto> readAll(Pageable pageable) {
//
//        Page<ItemEntity> itemEntityPage = itemRepository.findAllByPage(pageable);
//        Page<ItemListOutDto> itemListDtos = ItemListOutDto.from(itemEntityPage, modelMapperBean.modelMapper());
//
//        return itemListDtos;
//    }

    // 모든 상품을 리스트에 나타낸다.
    public Page<ItemListDto> readAllByPage(Pageable pageable) {

        Page<ItemEntity> itemEntityPage = itemRepository.findAllByPage(pageable);
        Page<ItemListDto> itemListDtos = ItemListDto.from(itemEntityPage, modelMapperBean.modelMapper());

        return itemListDtos;
    }

    // 각 성별의 해당하는 상품을 리스트에 나타낸다.
    public Page<ItemListDto> findAllByGender(String gender, Pageable pageable) {

        Page<ItemEntity> itemEntityPage = itemRepository.findAllByGender(gender, pageable);
        Page<ItemListDto> itemListDtos = ItemListDto.from(itemEntityPage, modelMapperBean.modelMapper());

        return itemListDtos;
    }

    // 각 성별 + 카테고리를 리스트에 나타낸다.
    public Page<ItemListDto> findByItemCategoryAndGender1(String category, String gender, Pageable pageable) {

        Page<ItemEntity> itemEntityPage = itemRepository.findByCategoryAndGender1(category, gender, pageable);
        Page<ItemListDto> itemListDtos = ItemListDto.from(itemEntityPage, modelMapperBean.modelMapper());

        return itemListDtos;
    }

    // 검색한 상품을 리스트에 나타낸다.
    public Page<ItemListDto> findItemWithSearch(String keyword, Pageable pageable) {

        Page<ItemEntity> itemEntityPage = itemRepository.findItemWithSearch(keyword, pageable);
        Page<ItemListDto> itemListDtos = ItemListDto.from(itemEntityPage, modelMapperBean.modelMapper());

        return itemListDtos;
    }

    //상품 하나를 읽어온다.
    public ItemDto itemDetail(int id) {

        //타겟 id의 상품을 하나 읽어온다.
        ItemEntity itemEntity = itemRepository.findById(id).get();
        ItemDto itemDto = ItemDto.from(itemEntity, modelMapperBean.modelMapper());

        //타겟 아이디의 연관상품 (같은 성별, 같은 카테고리)의 아이템 엔티티를 레포지토리에서 추가로 읽어온다
        Set<ItemEntity> relatedItems = itemRepository.findByCategoryAndGender(itemEntity.getCategory(), itemEntity.getGender());

        //연관상품 엔티티들을 ListOutDto로 변환
        Set<ItemListDto> itemListDtos = ItemListDto.from(relatedItems, modelMapperBean.modelMapper());

        //itemDto 에 연관상품 dto를 추가한다.
        itemDto.setRelated(itemListDtos);

        // 해당 상품 id의 아이템 엔티티를 레포지토리에서 읽어온다.
        Set<ItemReviewEntity> itemReviews = itemReviewRepository.findByItemId(itemEntity.getItemId());

        // 리뷰 엔티티들을 ReviewOutDto로 변환
        Set<ItemReviewDto> itemReviewDtos = ItemReviewDto.from(itemReviews, modelMapperBean.modelMapper());

        // itemOutDto에 리뷰 Dto를 추가
        itemDto.setReviews(itemReviewDtos);

        //완성된 dto를 리턴한다.
        return itemDto;
    }

    //검색 조건을 이용해 상품의 목록을 받아온다.
    public Page<ItemListDto> searchItems(String keyword, Pageable pageable) {

        Page<ItemEntity> itemEntityPage = itemRepository.findItemWithSearch(keyword, pageable);
        Page<ItemListDto> itemListDtos = ItemListDto.from(itemEntityPage, modelMapperBean.modelMapper());

        return itemListDtos;
    }

    // 상품 수정
    @Transactional
    public void itemUpdate(int id, ItemUploadDto itemUploadDto) {
        ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영
        itemEntity.update(itemUploadDto.getItemName(), itemUploadDto.getItemCategory(), itemUploadDto.getGender(),  itemUploadDto.getItemDescription(), itemUploadDto.getItemPrice());
    }

    // 상품 삭제
    @Transactional
    public void itemDelete(int id) {

        //실제로 삭제하지는 않고, delete 항목만 추가한다.
        ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        itemEntity.setDeleted(true);
    }

    // 리뷰 생성
    @Transactional
    public void createReview(int userId, com.example.mall.Dto.Request.Item.ItemReviewDto itemReviewDto) {

        ItemReviewEntity itemReviewEntity = new ItemReviewEntity();

        itemReviewEntity.setUserId(userId);
        itemReviewEntity.setItemId(itemReviewDto.getItemId());
        itemReviewEntity.setReviewContent(itemReviewDto.getReviewContent());
        itemReviewEntity.setReviewStar(itemReviewDto.getReviewStar());

        itemReviewRepository.save(itemReviewEntity);

        ItemEntity itemEntity = itemRepository.getById(itemReviewDto.getItemId());
        itemEntity.getReview().add(itemReviewEntity);

    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(int reviewId) {
        itemReviewRepository.deleteByReviewId(reviewId);
    }

    //상품의 옵션을 추가한다.
    @Transactional
    public void addItemOption(int itemId, ItemOptionDto itemOptionDto) {

        ItemOptionEntity itemOptionEntity = new ItemOptionEntity();

        itemOptionEntity.setItemId(itemId);
        itemOptionEntity.setOptionContent(itemOptionDto.getOptionContent());
        itemOptionEntity.setOptionStock(itemOptionDto.getOptionStock());

        itemOptionRepository.save(itemOptionEntity);
    }

    //상품의 옵션을 수정한다.
    @Transactional
    public void updateItemOption(int itemOptionId, ItemOptionDto itemOptionDto) {

        ItemOptionEntity itemOptionEntity = itemOptionRepository.getById(itemOptionId);
        itemOptionEntity.setOptionContent(itemOptionDto.getOptionContent());
        itemOptionEntity.setOptionStock(itemOptionEntity.getOptionStock());
    }

    //상품의 옵션을 삭제한다.
    @Transactional
    public void deleteItemOption(int itemOptionId) {
        itemOptionRepository.getById(itemOptionId).setDeleted(true);
    }

    //상품의 이미지를 추가한다.
    @Transactional
    public void addItemImage(int itemId, MultipartFile file) throws IOException {

        String src = serviceS3.upload(file);
        ItemImageEntity itemImageEntity = new ItemImageEntity();

        itemImageEntity.setImageSrc(src);
        itemImageEntity.setItemId(itemId);
        itemImageEntity.setProfile(false);

        itemImageRepository.save(itemImageEntity);
    }

    //상품의 이미지를 삭제한다.
    @Transactional
    public void deleteItemImage(int itemImageId) throws IOException {

        ItemImageEntity itemImageEntity = itemImageRepository.getById(itemImageId);

        //s3에서 삭제
        serviceS3.delete(itemImageEntity.getImageSrc());
        //DB에서 삭제
        itemImageRepository.delete(itemImageEntity);
    }

    //상품의 프로필 이미지를 갱신한다.
    @Transactional
    public void updateItemProfile(int itemId, MultipartFile file) throws IOException {

        ItemEntity itemEntity = itemRepository.getById(itemId);

        String src = serviceS3.upload(file);
        //기존 상품 이미지 삭제
        System.out.println(itemEntity.getItemProfile());
        if(!(itemEntity.getItemProfile() == null || itemEntity.getItemProfile().equals(""))){
            serviceS3.delete(itemEntity.getItemProfile());
        }
        //이미지 src 갱신
        itemEntity.setItemProfile(src);
    }
}
