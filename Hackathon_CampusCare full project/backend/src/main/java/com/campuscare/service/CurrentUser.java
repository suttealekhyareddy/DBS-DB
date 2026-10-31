
package com.campuscare.service;
import com.campuscare.model.User; import com.campuscare.repo.UserRepository; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Component;
@Component
public class CurrentUser {
 private final UserRepository users; public CurrentUser(UserRepository u){users=u;}
 public User get(){String email=(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();return users.findByEmail(email).orElseThrow();}
 public boolean admin(){return get().getRole()==User.Role.ADMIN;}
}
