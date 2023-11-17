package yiu.aisl.yiuservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Token;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.*;
import yiu.aisl.yiuservice.repository.TokenRepository;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.config.jwt.TokenProvider;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
//    private final TokenService tokenService;

    private final JavaMailSender javaMailSender;
    private static final String senderMail = "bmh2038@naver.com";
    private static int number;
    private static String authNum;

    private final long exp = 1000L * 60 * 60 * 24 * 14; // 14일

    // <API> 회원가입
    @Transactional
    public Boolean join(UserJoinRequestDto request) throws Exception {
        if (userRepository.findByStudentId(request.getStudentId()).isPresent()) {
            throw new Exception("이미 존재하는 학번입니다.");
        }

        try {
            User user = User.builder()
                    .studentId(request.getStudentId())
                    .nickname(request.getNickname())
                    .pwd(passwordEncoder.encode(request.getPwd()))
                    .build();
            userRepository.save(user);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    // <API> 닉네임 중복 확인
    @Transactional
    public Boolean checkNickname(CheckNicknameRequestDTO request) {
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            return false;
        }
        return true;
    }

    // <API> 로그인
    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        User user = userRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> new BadCredentialsException("존재하지 않는 계정"));

        if(!passwordEncoder.matches(request.getPwd(), user.getPwd())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }

//        user.setRefreshToken(createRefreshToken(user));
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        return UserLoginResponseDto.builder()
                .studentId(user.getStudentId())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .build();
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .studentId(user.getStudentId())
                        .refreshToken(UUID.randomUUID().toString())
                        .expiration(120) // 2분ㅋ
                        .build()
        );
//        System.out.println("token" + token.getRefreshToken());
        return token.getRefreshToken();
    }

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    @Transactional
    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }

    public static void createNumber() {
        // (int) Math.random() * (최댓값-최소값+1) + 최소값
        number = (int)(Math.random() * (90000)) + 100000;
    }

    public MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderMail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "<h1>";
            body += "<h3>" + "감사합니다" + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    // 인증번호 8자리 무작위 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<8; i++) {
            // 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다.
            int idx = random.nextInt(3);

            // 랜덤하게 idx를 받았으면, 그 값을 switchcase를 통해 또 꼬아버립니다.
            // 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                    // a(97) ~ z(122)
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    // A(65) ~ Z(90)
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0 ~ 9
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        // 코드를 생성합니다.
        createCode();
        String setFrom = "bmh2038@naver.com";	// 보내는 사람
        String toEmail = email;		// 받는 사람(값 받아옵니다.)
        String title = "회원가입 인증번호 테스트";		// 메일 제목

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
        message.setSubject(title);		// 제목 설정

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 용인대학교 AI Service LAB 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authNum + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);		// 보내는 사람 설정
        // 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }

    //실제 메일 전송
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);
        //실제 메일 전송
        javaMailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }




//    public UserResponse login(UserRequest request) throws Exception {
//        User user = userRepository.findByAccount(request.getAccount()).orElseThrow(() ->
//                new BadCredentialsException("잘못된 계정정보입니다."));
//        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("잘못된 계정정보입니다.");
//        }
//
//        user.setRefreshToken(createRefreshToken(user));
//
//        return UserResponse.builder()
//                .id(user.getId())
//                .account(user.getAccount())
//                .email(user.getEmail())
//                .name(user.getName())
//                .roles(user.getRoles())
//                .token(TokenDto.builder()
//                        .access_token(jwtProvider.createToken(user.getAccount(), user.getRoles()))
//                        .refresh_token(user.getRefreshToken())
//                        .build())
//                .build();
//    }
//
//    public UserResponse getUser(String account) throws Exception {
//        User user = userRepository.findByAccount(account)
//                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
//        return new UserResponse(user);
//    }
//
//
}
