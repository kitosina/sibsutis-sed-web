package sibsutis.sed.sedsibsutis.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoService {

    public String getEmailAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object userAuth = (auth != null && isAuthenticated())
                ? auth.getPrincipal()
                : null;
        UserDetails userDetails = null;
        if (userAuth == null) {
            throw new RuntimeException("Not auth user");
        }
        if (userAuth instanceof UserDetails) {
            userDetails = (UserDetails) userAuth;
        }
        log.info(userDetails.getUsername());
        return userDetails.getUsername();//Email returned
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

}
