package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.entity.user.CustomMemberDetails;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class MemberService implements UserDetailsManager {
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private static int number;
    @Autowired
    public MemberService(MemberRepository memberRepository, JavaMailSender javaMailSender) {
        this.memberRepository = memberRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        if  (optionalMember.isEmpty())
            throw new UsernameNotFoundException(loginId);
        return CustomMemberDetails.fromEntity(optionalMember.get());
    }

    //회원가입
    @Override
    public void createUser(UserDetails user) {
        log.info("try create user: {}", user.getUsername());
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //이미 존재하는 아이디

        try {
            log.info("회원가입 완료");
            memberRepository.save(((CustomMemberDetails) user).newMember());
        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomMemberDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //회원정보 수정
    @Override
    public void updateUser(UserDetails user) {

    }

    //회원 탈퇴
    @Override
    public void deleteUser(String username) {

    }

    //비밀번호 변경
    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    //아이디 중복확인
    @Override
    public boolean userExists(String loginId) {
        return this.memberRepository.existsByLoginId(loginId);
    }

    //닉네임 중복확인
    public boolean nicknameExists(String nickname) {
        return this.memberRepository.existsByNickname(nickname);
    }

    //이메일 중복확인
    public boolean emailExists(String email) {
        return this.memberRepository.existsByEmail(email);
    }

    //이메일 인증
    public static void createNumber() {
        number = (int)(Math.random()*(90000)) + 10000;
    }

    public MimeMessage createMail(String email) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom("thecatdogpia@gmail.com");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[CatDogPia] 회원가입 이메일 인증");
            String body = "";
            body += "<h2>" + "CatDogPia 이메일 인증" + "</h2>";
            body += "<h3>" + "캣독피아에 등록한 이메일 주소가 올바른지 확인하기 위한 인증번호입니다." + "</h3>";
            body += "<h3>" + "아래의 인증번호를 복사하여 이메일 인증을 완료해 주세요." + "</h3>";
            body += "<h1> " + number + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public int sendMail(String email) {
        MimeMessage message = createMail(email);
        javaMailSender.send(message);

        return number;
    }
}
