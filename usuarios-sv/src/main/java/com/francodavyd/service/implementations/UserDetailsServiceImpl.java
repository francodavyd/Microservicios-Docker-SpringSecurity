package com.francodavyd.service.implementations;

import com.francodavyd.dto.AuthCreateUser;
import com.francodavyd.dto.AuthLoginRequestDTO;
import com.francodavyd.dto.AuthResponseDTO;
import com.francodavyd.model.Role;
import com.francodavyd.model.UserSec;
import com.francodavyd.repository.IRoleRepository;
import com.francodavyd.repository.IUserSecRepository;
import com.francodavyd.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserSecRepository repository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRoleRepository repositoryR;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        UserSec userSec = repository.findUserSecByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario " + username + " no fue encontrado"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userSec.getRolesList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));


        userSec.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream()) //acá recorro los permisos de los roles
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isCredentialNotExpired(),
                userSec.isAccountNotLocked(),
                authorityList);
    }

    public AuthResponseDTO loginUser (AuthLoginRequestDTO authLoginRequest){

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate (username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken =jwtUtils.createToken(authentication);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, "Login successfully", accessToken, true);
        System.out.println(accessToken);
        return authResponseDTO;

    }

    public Authentication authenticate (String username, String password) {
        //con esto debo buscar el usuario
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails==null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        // si no es igual
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponseDTO createUser(AuthCreateUser createUser) {
        String username = createUser.username();
        String password = createUser.password();
        List<String> roleListName = createUser.authCreateRoleRequest().roleListName();

        Set<Role> roleSet = repositoryR.findByRoleIn(roleListName).stream().collect(Collectors.toSet());
        if (roleSet.isEmpty()){
            throw new IllegalArgumentException("The roles specified not exist");
        }
        UserSec userSec = UserSec.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .rolesList(roleSet)
                .enabled(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .build();
        UserSec userCreated = repository.save(userSec);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userCreated.getRolesList().forEach(role -> authorityList
                .add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        userCreated.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(),
                userCreated.getPassword(), authorityList);
        String accesToken = jwtUtils.createToken(authentication);
        AuthResponseDTO responseDTO = new AuthResponseDTO(userCreated.getUsername(),
                "Registered" , accesToken, true);
        return responseDTO;
    }
}