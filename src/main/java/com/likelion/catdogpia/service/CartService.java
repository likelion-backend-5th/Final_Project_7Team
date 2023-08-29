package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.cart.CartListDto;
import com.likelion.catdogpia.domain.entity.cart.Cart;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.CartRepository;
import com.likelion.catdogpia.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    // 장바구니 조회
    @Transactional(readOnly = true)
    public Map<String, Object> readAllCart(String loginId, Integer page) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<CartListDto> cartListDtos = cartRepository.findAllByMemberId(pageable, member.getId());

        // 장바구니 상품 전체 금액
        int totalAmount = 0;
        for (CartListDto dto : cartListDtos) {
            totalAmount += dto.getAmount();
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cartList", cartListDtos);
        resultMap.put("totalAmount", totalAmount);

        return resultMap;
    }

    // 장바구니 상품 수량 변경
    @Transactional
    public int updateCount(String loginId, Long cartId, String mp) {
        // (현재 로그인한 회원과 해당 장바구니 상품 저장한 회원 일치 여부 확인)

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        int count = 0;
        if(mp.equals("minus")) {
            count = -1;
        } else if (mp.equals("plus")) {
            count = 1;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        cart.updateCount(count);

        return cart.getProductCnt();
    }

    // 장바구니 상품 삭제
    public void deleteCart(String loginId, List<Long> cartIds) {
        // (현재 로그인한 회원과 해당 장바구니 상품 저장한 회원 일치 여부 확인)

        for(Long cartId : cartIds) {
            Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            cartRepository.delete(cart);
        }

    }
}
