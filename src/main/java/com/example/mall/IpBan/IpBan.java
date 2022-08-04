package com.example.mall.IpBan;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ip_ban")
@Data
public class IpBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_Id")
    private int ipId;

    private String ip;

}
