package com.korit.library.web.api;

import com.korit.library.security.PrincipalDetails;
import com.korit.library.service.RentalService;
import com.korit.library.web.dto.CMRespDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"도서 대여 API"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RentalApi {

//    어노테이션 RequiredArgsConstructor달고 final 달면 Autowired임
//    Autowired는 생성 할때마다 달아줘야함
//    RequiredArgsConstructor는 final만 달면 됨 개취차이임
    private final RentalService rentalService;
    /*
         rental 요청을 할 때 / {bookId} 이 책을 빌리겠다.
         대여 요청 -> 대여 요청 날린 사용자의 대여 가능 여부 확인 필요
         -> 가능함(대여 가능 횟수 3권 미만일 때) -> 대여 정보 추가 rental_mst(대여코드) 만들어야함, rental_dtl 만들어야함
         -> 불가능함(대여 가능 횟수 0) -> 예외 처리
    */

    @PostMapping("/rental/{bookId}")
    public ResponseEntity<CMRespDto<?>> rental(@PathVariable int bookId,
                                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        rentalService.rentalOne(principalDetails.getUser().getUserId(),bookId);
        return ResponseEntity
                .ok()
                .body(new CMRespDto<>(HttpStatus.OK.value(), "Successfully", null));
    }
}
