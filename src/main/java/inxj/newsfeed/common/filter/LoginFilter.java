package inxj.newsfeed.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import inxj.newsfeed.common.dto.ResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class LoginFilter extends OncePerRequestFilter {

    // 로그인 필터를 적용하지 않을 요청
    private static final List<AntPathRequestMatcher> EXCLUDE_URLS = List.of(
            new AntPathRequestMatcher("/", "GET"),
            new AntPathRequestMatcher("/login", "POST"),
            new AntPathRequestMatcher("/users", "POST"), // 회원가입
            new AntPathRequestMatcher("/users/*/password", "POST") // 비밀번호 찾기
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // EXCLUDE_URLS 의 링크는 로그인 필터를 실행하지 않음
        for (AntPathRequestMatcher matcher : EXCLUDE_URLS) {
            if (matcher.matches(request)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = request.getSession(false); // 세션이 없으면 null

        // TODO: 세션에 어떤 이름으로 로그인 유저 속성을 담는지 확인 필요
        if (session == null || session.getAttribute("loginUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");

            ResponseDto<Void> failResponse = ResponseDto.fail("로그인이 필요합니다.");
            String json = new ObjectMapper().writeValueAsString(failResponse); // Jackson 의 ObjectMapper 사용
            response.getWriter().write(json);

            return;
        }

        filterChain.doFilter(request, response);
    }

}
