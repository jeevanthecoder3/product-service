package com.greenry.productservice.Service;

import com.greenry.productservice.Dao.UserRepository;
import com.greenry.productservice.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean checkValidity(String userName){
        LocalDateTime loggedIntime=null;
            User user = this.userRepository.findUserByUserName(userName);
        System.out.println("Found User : "+user.toString());
        if(user==null){
            return false;
        }else {
            loggedIntime=user.getLoggedInTime();
            System.out.println(loggedIntime);
            System.out.println(Duration.between(loggedIntime,LocalDateTime.now()).getSeconds());
            System.out.println(user.isLoggedIn()+" "+(Duration.between(loggedIntime,LocalDateTime.now()).getSeconds() < (5*60*60)));
            if(user.isLoggedIn()==true && Duration.between(loggedIntime,LocalDateTime.now()).getSeconds() < (5*60*60)){
                return true;
            }else{
                user.setLoggedIn(false);
                user.setLoggedInTime(null);
                this.userRepository.save(user);
                return false;
            }
        }
    }

}
