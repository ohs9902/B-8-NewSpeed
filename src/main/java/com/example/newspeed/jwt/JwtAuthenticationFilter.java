package com.example.newspeed.jwt;

import com.example.newspeed.dto.LoginRequestDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Getter
@Slf4j(topic = "로그인 및 JWT생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if("application/json".equals(request.getContentType())){
            try{
                //요청받은 json을 객체 형태로 변환
                LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(),LoginRequestDto.class);
                log.info("Received login request: " + loginRequestDto.getUserId() + "     " + loginRequestDto.getPassword());
                Optional<User> optionalUser = userRepository.findByUserId(loginRequestDto.getUserId());
                if(optionalUser.isPresent()){
                    User user = optionalUser.get();
                    if("탈퇴".equals(user.getStatus())){
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("탈퇴한 계정입니다.");
                    }else if (user.getPassword().equals(loginRequestDto.getPassword())){
                        response.getWriter().write("비밀번호가 틀렸습니다.");
                    }
                }else{
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("존재하지 않는 회원입니다.");
                }
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(),loginRequestDto.getPassword());

                //추가적인 요청정보를 authRequest에 설정
                setDetails(request,authRequest);
                //athentication manager를 통해 인증 시도
                return this.getAuthenticationManager().authenticate(authRequest);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        //요청 json 형식이 아닐경우 부모클래스의 기본 동작 수행
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT생성");
        String userId = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String accessToken = jwtUtil.generateToken(userId, jwtUtil.ACCESS_TOKEN_EXPIRATION , "access");
        String refreshToken = jwtUtil.generateToken(userId , jwtUtil.REFRESH_TOKEN_EXPIRATION,"refresh");

        //헤더에 전달해야함
        jwtUtil.addJwtToHeader(response,accessToken,jwtUtil.AUTHORIZATION_HEADER );

        //엑세스 토큰은 헤더에 전달 성공,refresh 토큰은 Entity에 저장 하면 됨

        //쿠키 전달방식
//        jwtUtil.addJwtToCookie(response, accessToken,jwtUtil.ACCESS_TOKEN_HEADER);
//        jwtUtil.addJwtToCookie(response, refreshToken,jwtUtil.REFRESH_TOKEN_HEADER);

        log.info("accesstoken : "+accessToken);
        log.info("refreshToken : "+refreshToken);


    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패!!");
        response.setStatus(401); //인증실패 401코드 전달
    }

}