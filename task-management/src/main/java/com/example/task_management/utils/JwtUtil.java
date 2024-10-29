package com.example.task_management.utils;

import com.example.task_management.entity.User;
import com.example.task_management.enums.UserRole;
import com.example.task_management.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Autowired
    private UserRepository userRepository;
    public String generateToken(UserDetails userDetails){
        return  generateToken(new HashMap<>(), userDetails);
    }

    private  String generateToken(HashMap<String, Objects> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims).
                setSubject(userDetails.getUsername()) //thong tin nguoi dung
                .setIssuedAt(new Date(System.currentTimeMillis())) //thoi gian phat hanh
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *24)) // thoi gian het han
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact(); // phương thức để ký JWT bằng khóa bí mật

    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("219834F8Y0WF9E8RBF0E98RNF938RF38NFF8Y3F84FH389HQEWDF9734HE");
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //kiem tra tinh hop le cua token
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //trich xuat ten nguoi dung
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) { //trích xuất tất cả các claims từ token.
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token){  //Kiểm tra xem token có hết hạn hay chưa.
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) { //trích xuất thời gian hết hạn của token từ các claims.
        return extractClaim(token, Claims::getExpiration);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            System.out.println("User found: " + user.getId());
            Optional<User> optionalUser = userRepository.findById(user.getId());
            return optionalUser.orElse(null);
        }
        System.out.println("No authenticated user found");
        return null;
    }



}
