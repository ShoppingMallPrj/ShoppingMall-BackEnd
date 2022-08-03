package com.example.hello.Controller;

import com.example.hello.Annotation.Auth;
import com.example.hello.Annotation.User;
import com.example.hello.Annotation.UserDetails;
import com.example.hello.Dto.Request.Inquiry.InquiryAnswerDto;
import com.example.hello.Dto.Response.Inquiry.InquiryDetailDto;
import com.example.hello.Dto.Response.Inquiry.InquiryDto;
import com.example.hello.Service.InquiryService;
import com.example.hello.Types.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Inquiry", description = "문의사항 관련 api")
@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    @Operation(summary = "문의사항 전부 가져오기", description = "모든 문의사항을 가져온다. 관리자 권한 필요")
    //@Auth(userRole = UserRole.ADMIN)
    @GetMapping("/list")
    public Page<InquiryDto> readAll(
            @SortDefault(sort = "inquiryId", direction = Sort.Direction.DESC)
            Pageable pageable){


        return inquiryService.readAll(pageable);
    }

    @Operation(summary = "문의사항 가져오기", description = "문의사항을 하나 가져온다.")
    //@Auth(userRole = UserRole.NONE)
    @GetMapping("/{id}")
    public InquiryDetailDto read(
            @Parameter(hidden = true)
            @User UserDetails userDetails,
            @Parameter(name = "id", description = "문의사항의 고유 id", in = ParameterIn.PATH)
            @PathVariable("id") int inquiryId
//            @Parameter(name= "", description = "비밀번호", in = ParameterIn.QUERY)
//            @RequestParam("pw") String pw
    ){

        //admin이면 무조건 통과
        //아니면 비밀번호 통해서 들어가야 함.
        InquiryDetailDto inquiryDetailDto = inquiryService.read(inquiryId);
//
//        if(!inquiryDetailOutDto.isSecret() || userDetails.getUserRole() == UserRole.ADMIN) return inquiryDetailOutDto;
//
//        if(!pw.equals(inquiryDetailOutDto.getInquiryPw())) throw new NoAuthException(NO_AUTHORIZATION_ERROR);
//        if(userDetails.getUserId() != inquiryDetailOutDto.getUserId()) throw new NoAuthException(NO_AUTHORIZATION_ERROR);

        return inquiryDetailDto;
    }

    @Operation(summary = "문의사항 등록", description = "문의사항을 등록한다. 유저 권한 필요")
    @Auth(userRole = UserRole.USER)
    @PostMapping("/create")
    public ResponseEntity<Object> create(
            @Parameter(hidden = true)
            @User UserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "문의사항 정보 dto", required = true)
            @Valid @RequestBody com.example.hello.Dto.Request.Inquiry.InquiryDto inquiryDto){

        inquiryService.create(userDetails.getUserId(), inquiryDto);

        return new ResponseEntity<>(
                "",
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "문의사항 답변/수정", description = "id로 문의사항에 답변/수정을 한다. 관리자 권한 필요")
    @Auth(userRole = UserRole.ADMIN)
    @PostMapping("/{id}")
    public void answer(
            @Parameter(name = "answer", description = "답변 내용", in = ParameterIn.DEFAULT)
            @RequestBody InquiryAnswerDto answer,
            @Parameter(name = "id", description = "문의사항의 고유 id", in = ParameterIn.PATH)
            @PathVariable int id
    ) {
        inquiryService.answerInquiry(answer, id);
    }
}
