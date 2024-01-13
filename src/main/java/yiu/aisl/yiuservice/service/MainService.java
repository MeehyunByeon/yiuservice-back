package yiu.aisl.yiuservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.dto.*;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.*;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final DeliveryRepository deliveryRepository;
    private final Comment_DeliveryRepository comment_deliveryRepository;
    private final TaxiRepository taxiRepository;
    private final Comment_TaxiRepository comment_taxiRepository;
//    private final TokenService tokenService;

    private final JavaMailSender javaMailSender;
    private static final String senderMail = "bmh2038@naver.com";
    private static int number;
    private static String authNum;

//    private final long exp = 1000L * 60 * 60 * 24 * 14; // 14일
    private long exp_refreshToken = Duration.ofDays(14).toMillis(); // 만료시간 2주

    // 메인 데이터 조회 [all]
    @Transactional
    public Map<String, List<?>> getList() throws Exception {

        try {
            // Delivery
            List<Delivery> listDeliveryActive = deliveryRepository.findByStateOrderByCreatedAtDesc(PostState.ACTIVE);
            List<Delivery> listDeliveryDeleted = deliveryRepository.findByStateOrderByCreatedAtDesc(PostState.DELETED);
            List<Delivery> listDeliveryFinished = deliveryRepository.findByStateOrderByCreatedAtDesc(PostState.FINISHED);

            // 현재 시간과 비교하여 due가 이미 지났다면 상태를 모두 FINISHED로 변경
            LocalDateTime currentTime = LocalDateTime.now();
            listDeliveryFinished.addAll(listDeliveryActive.stream()
                    .filter(delivery -> delivery.getDue().isBefore(currentTime))
                    .map(delivery -> {
                        delivery.setState(PostState.FINISHED);

                        // 마감된 배달 모집 글에 따른 신청글 => state가 ApplyState.WAITING인 글의 state를 FINISHED로 변경
                        List<Comment_Delivery> waitingComments = comment_deliveryRepository.findByDeliveryAndState(delivery, ApplyState.WAITING);
                        waitingComments.forEach(comment -> comment.setState(ApplyState.FINISHED));
                        comment_deliveryRepository.saveAll(waitingComments);
                        return delivery;
                    })
                    .collect(Collectors.toList()));

            List<DeliveryResponse> deliveryGetListDTO = new ArrayList<>();
            deliveryGetListDTO.addAll(listDeliveryActive.stream().map(DeliveryResponse::GetDeliveryDTO).collect(Collectors.toList()));
            deliveryGetListDTO.addAll(listDeliveryDeleted.stream().map(DeliveryResponse::GetDeliveryDTO).collect(Collectors.toList()));
            deliveryGetListDTO.addAll(listDeliveryFinished.stream().map(DeliveryResponse::GetDeliveryDTO).collect(Collectors.toList()));

            // Taxi
            List<Taxi> listTaxiActive = taxiRepository.findByStateOrderByCreatedAtDesc(PostState.ACTIVE);
            List<Taxi> listTaxiDeleted = taxiRepository.findByStateOrderByCreatedAtDesc(PostState.DELETED);
            List<Taxi> listTaxiFinished = taxiRepository.findByStateOrderByCreatedAtDesc(PostState.FINISHED);

            // 현재 시간과 비교하여 due가 이미 지났다면 상태를 모두 FINISHED로 변경
//            LocalDateTime currentTime = LocalDateTime.now();
            listTaxiFinished.addAll(listTaxiActive.stream()
                    .filter(taxi -> taxi.getDue().isBefore(currentTime))
                    .map(taxi -> {
                        taxi.setState(PostState.FINISHED);

                        // 마감된 택시 모집 글에 따른 신청글 => state가 ApplyState.WAITING인 글의 state를 FINISHED로 변경
                        List<Comment_Taxi> waitingComments = comment_taxiRepository.findByTaxiAndState(taxi, ApplyState.WAITING);
                        waitingComments.forEach(comment -> comment.setState(ApplyState.FINISHED));
                        comment_taxiRepository.saveAll(waitingComments);

                        return taxi;
                    })
                    .collect(Collectors.toList()));

            List<TaxiResponse> taxiGetListDTO = new ArrayList<>();
            taxiGetListDTO.addAll(listTaxiActive.stream().map(TaxiResponse::GetTaxiDTO).collect(Collectors.toList()));
            taxiGetListDTO.addAll(listTaxiDeleted.stream().map(TaxiResponse::GetTaxiDTO).collect(Collectors.toList()));
            taxiGetListDTO.addAll(listTaxiFinished.stream().map(TaxiResponse::GetTaxiDTO).collect(Collectors.toList()));

            // result
            Map<String, List<?>> response = new HashMap<>();
            response.put("delivery", deliveryGetListDTO);
            response.put("taxi", taxiGetListDTO);

            return response;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // <API> 회원가입
    @Transactional
    public Boolean join(UserJoinRequestDto request) throws Exception {
        // 400 - 데이터 없음
        if(request.getStudentId() == null || request.getNickname() == null || request.getPwd() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 409 - 학번 or 닉네임 이미 존재
        if (userRepository.findByStudentId(request.getStudentId()).isPresent() || userRepository.findByNickname(request.getNickname()).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE);

        // 데이터 저장
        try {
            User user = User.builder()
                    .studentId(request.getStudentId())
                    .nickname(request.getNickname())
                    .pwd(passwordEncoder.encode(request.getPwd()))
                    .build();
            userRepository.save(user);
        }
        catch (Exception e) {
            throw new Exception("서버 오류");
        }
        return true;
    }

    // <API> 닉네임 중복 확인
    @Transactional
    public Boolean checkNickname(CheckNicknameRequestDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getNickname() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 409 - 닉네임 존재 => 중복
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE);
        }

        return true;
    }

    // <API> 로그인
    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto request) throws Exception {
        // 400 - 데이터 없음
        if(request.getStudentId() == null || request.getPwd() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 401 - 유저 존재 확인
        User user = userRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));

        // 401 - 비밀번호 일치 확인
        if(!passwordEncoder.matches(request.getPwd(), user.getPwd())) {
            throw new CustomException(ErrorCode.VALID_NOT_PWD);
        }


        try {
            // 리프레시 토큰 생성
            user.setRefreshToken(createRefreshToken(user));
            // String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
            UserLoginResponseDto response = UserLoginResponseDto.builder()
                    .studentId(user.getStudentId())
                    .nickname(user.getNickname())
                    .token(TokenDto.builder()
                            .accessToken(tokenProvider.createToken(user))
                            .refreshToken(user.getRefreshToken())
                            .build())
                    .build();
            return response;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // <API> 비밀번호 재설정
    public Boolean changePwd(ChangePwdRequestDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getStudentId() == null || request.getPwd() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 401 - 유저 존재 확인
        User user = userRepository.findByStudentId(request.getStudentId()).orElseThrow(()
                -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));

        user.setPwd(passwordEncoder.encode(request.getPwd()));
        userRepository.save(user);
        try {
            user.setPwd(passwordEncoder.encode(request.getPwd()));
            userRepository.save(user);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.VALID_NOT_STUDENT_ID));
    }

    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .studentId(user.getStudentId())
                        .refreshToken(UUID.randomUUID().toString())
                        .expiration(exp_refreshToken)
                        .build()
        );
//        System.out.println("token" + token.getRefreshToken());
        return token.getRefreshToken();
    }

    //실제 메일 전송
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        // 400 - 데이터 없음
        if(email == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 409 - 해당 학번의 회원 존재 => 중복
        if (userRepository.findByStudentId(Long.parseLong(email)).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE);

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email+"@yiu.ac.kr");
        //실제 메일 전송
        javaMailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }

    public static void createNumber() {
        // (int) Math.random() * (최댓값-최소값+1) + 최소값
        number = (int)(Math.random() * (90000)) + 100000;
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        // 코드를 생성합니다.
        createCode();
        String setFrom = "yiuaiservicelab@gmail.com";	// 보내는 사람
        String toEmail = email;		// 받는 사람(값 받아옵니다.)
        String title = "YMate 회원가입 인증번호";		// 메일 제목

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
        message.setSubject(title);		// 제목 설정

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 용인대학교 YMate 입니다. </h1>";
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

    // 인증번호 6자리 무작위 생성
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<6; i++)
            key.append(random.nextInt(9));

        authNum = key.toString();
    }

    public Token validRefreshToken(User user, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(user.getStudentId())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_REQUIRED));

        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefreshToken() == null) throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        try {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
            if (token.getExpiration() < 10) {
                token.setExpiration(1000L);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if (!token.getRefreshToken().equals(refreshToken)) {
                // 원래 null
                throw new CustomException(ErrorCode.LOGIN_REQUIRED);
            } else {
                return token;
            }
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    public TokenDto refreshAccessToken(TokenDto token) throws Exception {
        Long studentId = tokenProvider.getStudentId(token.getAccessToken());
        User user = userRepository.findByStudentId(studentId).orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_EXIST));
        Token refreshToken = validRefreshToken(user, token.getRefreshToken());

        try {
            if (refreshToken != null) {
                return TokenDto.builder()
                        .accessToken(tokenProvider.createToken(user))
                        .refreshToken(refreshToken.getRefreshToken())
                        .build();
            } else {
                throw new CustomException(ErrorCode.LOGIN_REQUIRED);
            }
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
