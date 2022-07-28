package com.example.hello.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@Data
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", insertable = false, updatable = false)
    private int itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_category")
    private String category;

    @Column(name = "gender")
    private String gender;

    @Column(name = "item_profile")
    private String itemProfile;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_price")
    private int itemPrice;

    //삭제 여부
    @Column(name = "is_deleted")
    private boolean isDeleted;

    //상품의 옵션들
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Set<ItemOptionEntity> option = new HashSet<>();

    //아이템 이미지들
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Set<ItemImageEntity> image = new HashSet<>();

    //아이템 리뷰들
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Set<ItemReviewEntity> review = new HashSet<>();

    public void update(String itemName, String category, String gender, String itemDescription, int itemPrice) {
        this.itemName = itemName;
        this.category = category;
        this.gender = gender;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
    }

}
