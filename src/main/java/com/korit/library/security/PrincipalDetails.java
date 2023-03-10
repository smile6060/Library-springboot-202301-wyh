package com.korit.library.security;

import com.korit.library.entity.UserMst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@AllArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

    @Getter
    private final UserMst user;
    private Map<String, Object> response;

    // 권한을 리스트로 관리하는 부분
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

//        List<RoleDtlDto> roleDtlDtoList = user.getRoleDtlDto();
//
//        for(int i = 0;  i < roleDtlDtoList.size(); i++) {
//            // 0 = ROLE_USER, 1 = ROLE_ADMIN
//            RoleDtlDto roleDtlDto = roleDtlDtoList.get(i);
//            RoleMstDto roleMstDto = roleDtlDto.getRoleMstDto();
//            String roleName = roleMstDto.getRoleName();
//
//            GrantedAuthority role = new GrantedAuthority() {
//                @Override
//                public String getAuthority() {
//                    return roleName;
//                }
//            };
//
//            authorities.add(role);
////            System.out.println(roleName == role.getAuthority());
//        }
//        위에 내용들을 밑에 람다로 간추려짐.

        user.getRoleDtl().forEach(dtl -> {
            authorities.add(() -> dtl.getRoleMst().getRoleName());
        });
//        authorities 안에 role user role admin  권한들이 들어감.
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /*
     계정 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
    계정 잠김 여부(휴먼계정)
    */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
    비밀번호 만료 여부(ex-비밀번호 5번 틀렸을 때)
    */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
    사용자 활성화 여부 (여기에 휴먼계정해도 됨)
    */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return response;
    }
}
