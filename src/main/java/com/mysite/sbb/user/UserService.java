package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    //회원 생성 메소드
    public SiteUser create(String username,String email,String password){
        SiteUser user=new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String name){
        Optional<SiteUser> siteUser=this.userRepository.findByusername(name);
        if(siteUser.isPresent()){
            return siteUser.get();
        }else{
            throw new DataNotFoundException("siteUser not found");
        }
    }


}
