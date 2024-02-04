package fr.efrei.documentapi.service;

import fr.efrei.documentapi.security.user.UserDetailsImpl;

public interface UserDetailService {

    public UserDetailsImpl getUserFromToken();
}
