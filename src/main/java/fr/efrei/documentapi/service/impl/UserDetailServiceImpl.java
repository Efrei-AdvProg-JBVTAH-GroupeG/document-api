package fr.efrei.documentapi.service.impl;

import fr.efrei.documentapi.exception.DocumentException;
import fr.efrei.documentapi.security.user.UserDetailsImpl;
import fr.efrei.documentapi.service.UserDetailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    public UserDetailsImpl getUserFromToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new DocumentException("Fail to retrieve authentication");
        }
        if(authentication.getPrincipal() instanceof UserDetailsImpl user) {
            return user;
        }
        throw new DocumentException("Fail to cast User details implémentation");
    }
}
