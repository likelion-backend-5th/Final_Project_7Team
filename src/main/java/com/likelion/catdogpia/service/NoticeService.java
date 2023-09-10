package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.notice.NoticeDto;
import com.likelion.catdogpia.domain.entity.notice.Notice;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.repository.NoticeRepository;
import com.likelion.catdogpia.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final QueryRepository queryRepository;

    // 목록 조회
    public Page<NoticeDto> findNoticeList(Pageable pageable, String filter, String keyword) {
        return queryRepository.findByNoticeList(pageable, filter, keyword);
    }

    // 상세 조회
    public NoticeDto findNotice(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId).orElseThrow(IllegalArgumentException::new);
        return NoticeDto.fromEntity(findNotice);
    }

    // 등록
    @Transactional
    public void noticeCreate(String loginId, NoticeDto noticeDto) {
        // 작성자 가져오기
        Member writer = memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);
        // 관리자가 아니면 등록하지 못하도록
        if(!writer.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException("관리자가 아닙니다");
        }
        // 게시글 저장
        noticeRepository.save(Notice.toEntity(noticeDto, writer));
    }

    // 수정
    @Transactional
    public void noticeModify(String loginId, Long noticeId, NoticeDto noticeDto) {
        // 수정할 게시글 가져오기
        Notice findNotice = noticeRepository.findById(noticeId).orElseThrow(IllegalArgumentException::new);
        // 작성자 가져오기
        Member writer = memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);
        // 관리자가 아니면 수정하지 못하도록
        if(!writer.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException("관리자가 아닙니다");
        }
        // 게시글 수정
        findNotice.changeNotice(noticeDto, writer);
    }

    // 삭제
    @Transactional
    public void deleteNoticeList(List<Long> deleteList) {
        // id가 있으면 삭제
        for (Long deleteId : deleteList) {
            if(noticeRepository.existsById(deleteId)) {
                noticeRepository.deleteById(deleteId);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
