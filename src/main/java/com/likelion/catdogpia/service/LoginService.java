package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.domain.entity.user.Role;
import com.likelion.catdogpia.jwt.repository.RefreshTokenRepository;
import com.likelion.catdogpia.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager manager;
    private final JavaMailSender javaMailSender;
    private final RefreshTokenRepository refreshTokenRepository;

    //로그인
    public String login(String loginId, String password) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        //회원정보 없음
        if (optionalMember.isEmpty()) {
            log.info("회원정보 없음 아이디 또는 비밀번호가 일치하지 않습니다.");
            return "fail";
        }

        //비밀번호 불일치
        UserDetails userDetails = manager.loadUserByUsername(loginId);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.info("비밀번호 불일치 아이디 또는 비밀번호가 일치하지 않습니다.");
            return "fail";
        }

        //토큰 발급 &  이동
        if(optionalMember.get().getRole().equals(Role.ADMIN)) {
            log.info("관리자 로그인 성공");
            return "admin";
        } else {
            log.info("회원 로그인 성공");
            return "user";
        }
    }

    //아이디 찾기
    public String findId(String name, String email) {
        Optional<Member> optionalMember = memberRepository.findByNameAndEmail(name, email);

        if(optionalMember.isEmpty())
            return "입력하신 회원 정보로 가입된 아이디가 존재하지 않습니다.";

        Member member = optionalMember.get();
        String loginId = member.getLoginId();
        return "입력하신 회원 정보로 가입된 아이디는 " + loginId + " 입니다.";
    }

    //비밀번호 찾기 (계정 확인 메시지)
    public String findPassword(String loginId, String name, String email) {
        Optional<Member> optionalMember = memberRepository.findByLoginIdAndNameAndEmail(loginId, name, email);

        if(optionalMember.isEmpty())
            return "입력하신 회원 정보로 가입된 계정이 존재하지 않습니다.";

        return "입력하신 이메일로 임시 비밀번호를 발급합니다.";
    }

    //비밀번호 찾기 (임시 비밀번호 생성 및 DB 저장)
    private static char getRandomChar(String characters, SecureRandom random) {
        int randomIndex = random.nextInt(characters.length());
        return characters.charAt(randomIndex);
    }

    public String createPassword(String email) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder();

        passwordBuilder.append(getRandomChar(characters, random));
        passwordBuilder.append(getRandomChar(digits, random));
        passwordBuilder.append(getRandomChar(specialCharacters, random));

        String allCharacters = characters + digits + specialCharacters;
        for (int i = passwordBuilder.length(); i < 8; i++) {
            passwordBuilder.append(getRandomChar(allCharacters, random));
        }

        char[] tempPassword = passwordBuilder.toString().toCharArray();
        for (int i = 0; i < tempPassword.length; i++) {
            int randomIndex = random.nextInt(tempPassword.length);
            char temp = tempPassword[i];
            tempPassword[i] = tempPassword[randomIndex];
            tempPassword[randomIndex] = temp;
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if(optionalMember.isEmpty())
            return "입력하신 회원 정보로 가입된 계정이 존재하지 않습니다.";

        Member member = optionalMember.get();
        member.setTempPassword(passwordEncoder.encode(new String(tempPassword)));
        memberRepository.save(member);
        return new String(tempPassword);
    }

    //메일생성
    public MimeMessage createPasswordMail(String email) {
        String password = createPassword(email);
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom("thecatdogpia@gmail.com");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[CatDogPia] 임시 비밀번호 발급");
            String body = "";
            body += "<h2>" + "CatDogPia 임시 비밀번호" + "</h2>";
            body += "<h3>" + "회원님께서 요청하신 임시 비밀번호가 발급되었습니다." + "</h3>";
            body += "<h3>" + "아래의 임시비밀번호를 사용하여 캣독피아에 로그인 후 새로운 비밀번호로 변경하시기 바랍니다." + "</h3>";
            body += "<h1> " + password + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    //임시 비밀번호 발급 메일 전송
    public String sendTempPassword(String email) {
        MimeMessage message = createPasswordMail(email);
        javaMailSender.send(message);

        return "임시 비밀번호 발급 메일 전송 완료";
    }

    //로그아웃
    @Transactional
    public void logout(String loginId) {
        refreshTokenRepository.deleteByLoginId(loginId);
    }
}