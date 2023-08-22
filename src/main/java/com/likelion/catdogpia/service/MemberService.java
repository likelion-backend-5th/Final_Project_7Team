package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.entity.user.CustomMemberDetails;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
public class MemberService implements UserDetailsManager {
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private static int number;

    private final PasswordEncoder passwordEncoder;
    public MemberService(MemberRepository memberRepository, JavaMailSender javaMailSender, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
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
}
