package com.example.mall.Repository;

import com.example.mall.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserId(int userId);

    UserEntity findByUserEmail(String email);

    //이메일과 비밀번호로 유저를 찾는다.
    UserEntity findByUserEmailAndUserPw(String email, String pw);

    //이미 존재하는 이메일 인지 확인
    boolean existsByUserEmail(String email);

    //유저 아이디로 삭제
    void deleteByUserId(int userId);

    //유저 이메일로 삭제
    void deleteByUserEmail(String email);


}
