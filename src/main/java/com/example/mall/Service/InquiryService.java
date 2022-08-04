package com.example.mall.Service;

import com.example.mall.Dto.Request.Inquiry.InquiryAnswerDto;
import com.example.mall.Dto.Response.Inquiry.InquiryDetailDto;
import com.example.mall.Dto.Response.Inquiry.InquiryDto;
import com.example.mall.Entity.InquiryEntity;
import com.example.mall.Repository.InquiryRepository;
import com.example.mall.Repository.UserRepository;
import com.example.mall.Util.ModelMapperBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class InquiryService {

    @Autowired
    InquiryRepository inquiryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapperBean modelMapperBean;

    public Page<InquiryDto> readAll(Pageable pageable){

        Page<InquiryEntity> inquiryEntityPage = inquiryRepository.findAll(pageable);
        Page<InquiryDto> inquiryDtos = InquiryDto.from(inquiryEntityPage, modelMapperBean.modelMapper());
        return inquiryDtos;
    }

    //특정 유저가 가진 문의사항을 전부 가져온다.
    public Set<InquiryDto> readUserInquiry(int userId){

        Set<InquiryEntity> inquiryEntitySet  = inquiryRepository.readAllByUserId(userId);
        Set<InquiryDto> inquiryDtos = InquiryDto.from(inquiryEntitySet, modelMapperBean.modelMapper());

        return inquiryDtos;
    }

    //문의사항을 하나 가져온다.
    public InquiryDetailDto read(int inquiryId){

        InquiryEntity inquiryEntity =  inquiryRepository.readByInquiryId(inquiryId);
        return InquiryDetailDto.from(inquiryEntity, modelMapperBean.modelMapper());
    }

    @Transactional
    public InquiryEntity create(int userId, com.example.mall.Dto.Request.Inquiry.InquiryDto inquiryDto){

        InquiryEntity inquiryEntity = new InquiryEntity();

        inquiryEntity.setUserEntity(userRepository.findByUserId(userId));
        inquiryEntity.setInquiryTitle(inquiryDto.getInquiryTitle());
        inquiryEntity.setInquiryContent(inquiryDto.getInquiryContent());
        //inquiryEntity.setSecret(inquiryEntity.isSecret());
        //inquiryEntity.setInquiryPw(inquiryInDto.getInquiryPw());
        inquiryEntity.setAnswered(false);

        System.out.println(inquiryDto.getInquiryTitle());
        return inquiryRepository.save(inquiryEntity);
    }

    //문의사항에 답변을 단다.
    @Transactional
    public void answerInquiry(InquiryAnswerDto inquiryAnswerDto, int inquiryId){

        InquiryEntity inquiryEntity = inquiryRepository.readByInquiryId(inquiryId);
        inquiryEntity.setAnswered(true);
        inquiryEntity.setInquiryAnswer(inquiryAnswerDto.getAnswer());
    }
}
