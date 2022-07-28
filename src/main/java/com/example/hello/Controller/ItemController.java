package com.example.hello.Controller;

import com.example.hello.Annotation.Auth;
import com.example.hello.Annotation.User;
import com.example.hello.Annotation.UserDetails;
import com.example.hello.Dto.In.Item.*;
import com.example.hello.Dto.Out.Item.ItemListOutDto;
import com.example.hello.Dto.Out.Item.ItemOutDto;
import com.example.hello.Dto.Out.Item.ItemReviewOutDto;
import com.example.hello.Service.ItemService;
import com.example.hello.Types.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "Item", description = "상품 관련 api")
@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    //관리자 권한 필요
    // 상품 등록 (POST)
    @Operation(summary = "상품 업로드", description = "상품을 업로드 한다. 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @PostMapping(value = "/create")
    public ResponseEntity<Object> itemUpload(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "상품 업로드시 받아오는 dto")
            @Valid ItemUploadDto itemUploadDto) throws IOException, ParseException {

        itemService.createItem(itemUploadDto);

        return new ResponseEntity<>(
                "",
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "상품 목록.", description = "모든 상품 목록을 보여준다.")
    @GetMapping("/list")
    public Page<ItemListOutDto> itemList(@PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ItemListOutDto> list = null;

        list = itemService.readAllByPage(pageable);

        return list;
    }

    @Operation(summary = "성별 아이템 목록.", description = "각 성별에 해당하는 상품을 리스트로 나타낸다.")
    @GetMapping("/list/gender")
    public Page<ItemListOutDto> itemGender(@RequestParam(value = "gender") String gender,
                                           @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ItemListOutDto> list = null;

        list = itemService.findAllByGender(gender, pageable);

        return list;
    }

    @Operation(summary = "성별 아이템 + 카테고리", description = "각 성별에 해당하는 상품 + 해당 카테고리를 리스트로 나타낸다.")
    @GetMapping("/list/cate")
    public Page<ItemListOutDto> itemCategoryAndGender(@RequestParam(value = "category") String category,
                                                      @RequestParam(value = "gender") String gender,
                                                      @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ItemListOutDto> list = null;

        list = itemService.findByItemCategoryAndGender1(category, gender, pageable);

        return list;
    }

    @Operation(summary = "상품 검색.", description = "검색한 상품 목록을 보여준다.")
    @GetMapping("/list/search")
    public Page<ItemListOutDto> itemSearch(@RequestParam(value = "keyword") String keyword,
                                           @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ItemListOutDto> list = null;

        list = itemService.findItemWithSearch(keyword, pageable);

        return list;
    }

    // 상품 상세 정보
    @Operation(summary = "상품 상세정보", description = " itemId를 기준으로 상품의 상세정보를 받아온다.")
    @GetMapping("/{id}")
    public ItemOutDto itemDetail(@PathVariable("id") int id) {

        ItemOutDto itemOutDto = itemService.itemDetail(id);
        return itemOutDto;
    }

    @Operation(summary = "상품 수정", description = "상품을 수정 한다. 관리자 권한 필요")
    @Auth(userRole = UserRole.ADMIN)
    @PutMapping("/{id}")
    public void updateItem(@PathVariable("id") int id, ItemUploadDto itemUploadDto) {
        itemService.itemUpdate(id, itemUploadDto);
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제 한다. 관리자 권한 필요")
    @Auth(userRole = UserRole.ADMIN)
    @DeleteMapping("/{id}")
    public void itemDelete(@PathVariable("id") Integer id) {
        itemService.itemDelete(id);
    }

    @Operation(summary = "상품에 댓글 추가", description = "상품에 의견을 단다, 유저 권한 필요")
//    @Auth(userRole = UserRole.USER)
    @PostMapping("/review/create")
    public ResponseEntity<Object> createReview(
            @Parameter(hidden = true)
            @User UserDetails userDetails,
            @RequestBody ItemReviewInDto itemReviewInDto) {

        itemService.createReview(userDetails.getUserId(), itemReviewInDto);

        return new ResponseEntity<>(
                "",
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "상품의 댓글 삭제", description = "상품의 댓글을 삭제 한다. 관리자, 해당 댓글 작성자 권한 필요")
//    @Auth(userRole = UserRole.USER)
    @DeleteMapping("{id}/review/{reviewid}")
    public void deleteReview(
            @Parameter(hidden = true)
            @User UserDetails userDetails,
            @PathVariable("id") int itemId,
            @PathVariable("reviewid") int reviewId) {

//        itemService.deleteReview(userDetails.getUserId());
        itemService.deleteReview(reviewId);
    }

    @Operation(summary = "상품의 옵션 추가", description = "상품의 옵션을 추가, 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @PostMapping("{id}/option")
    public void addItemOption(@PathVariable int id, ItemOptionInDto itemOptionInDto) {
        itemService.addItemOption(id, itemOptionInDto);
    }

    @Operation(summary = "상품의 옵션 수정", description = "상품의 옵션을 수정, 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @PutMapping("{id}/option/{optionid}")
    public void updateItemOption(@PathVariable("id") int id,
                                 @PathVariable("optionid") int optionId,
                                 ItemOptionInDto itemOptionInDto) {
        itemService.updateItemOption(optionId, itemOptionInDto);
    }

    @Operation(summary = "상품의 옵션 삭제", description = "상품의 옵션을 삭제, 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @DeleteMapping("{id}/option/{optionid}")
    public void deleteItemOption(@PathVariable("id") int id,
                                 @PathVariable("optionid") int optionId) {
        itemService.deleteItemOption(optionId);
    }

    @Operation(summary = "상품의 프로필 이미지 변경", description = "상품의 프로필 이미지를 변경한다. 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @PutMapping("{id}/update-profile")
    public void updateItemProfile(@PathVariable("id") int id, MultipartFile profile) {
        try {
            itemService.updateItemProfile(id, profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "상품의 이미지 추가", description = "상품의 이미지를 추가한다. 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @PostMapping("{id}/image")
    public void addItemImage(@PathVariable("id") int id, MultipartFile file) {
        try {
            itemService.addItemImage(id, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "상품의 이미지 삭제", description = "상품의 이미지를 삭제한다. 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @DeleteMapping("{id}/image/{imageid}")
    public void deleteItemImage(@PathVariable("id") int id, @PathVariable("imageid") int imageId) {
        try {
            itemService.deleteItemImage(imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
