package com.example.mall.IpBan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IpBanRepository extends JpaRepository<IpBan,Integer > {

    boolean existsByIp(String ip);
}
