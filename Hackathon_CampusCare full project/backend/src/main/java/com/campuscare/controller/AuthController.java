
package com.campuscare.controller;
import com.campuscare.model.User; import com.campuscare.repo.UserRepository; import com.campuscare.security.JwtService;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.http.*; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public record Register(@NotBlank String fullName,@Email String email,String universityId,String phone,String department,String yearOfStudy,String sectionName,String designation,@Size(min=8) String password,String role){}
 public record Login(@Email String email,@NotBlank String password){}
 @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody Register r){
  if(users.existsByEmail(r.email()))return ResponseEntity.badRequest().body(Map.of("message","Email already registered"));
  if(r.universityId()!=null&&!r.universityId().isBlank()&&users.existsByUniversityId(r.universityId()))return ResponseEntity.badRequest().body(Map.of("message","University ID already registered"));
  User.Role role;try{role=User.Role.valueOf(r.role()==null?"STUDENT":r.role().toUpperCase());}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("message","Invalid role"));}
  if(role==User.Role.ADMIN||role==User.Role.STAFF)return ResponseEntity.status(403).body(Map.of("message","Admin/staff accounts must be provisioned by an administrator"));
  User u=new User();u.setFullName(r.fullName());u.setEmail(r.email().toLowerCase());u.setUniversityId(r.universityId());u.setPhone(r.phone());u.setDepartment(r.department());u.setYearOfStudy(r.yearOfStudy());u.setSectionName(r.sectionName());u.setDesignation(r.designation());u.setPasswordHash(encoder.encode(r.password()));u.setRole(role);u.setApproved(role==User.Role.STUDENT);users.save(u);
  return ResponseEntity.ok(Map.of("message",role==User.Role.FACULTY?"Registration submitted; await admin approval":"Registration successful; please log in","role",role.name()));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody Login l){
  User u=users.findByEmail(l.email().toLowerCase()).orElse(null);
  if(u==null||!encoder.matches(l.password(),u.getPasswordHash()))return ResponseEntity.status(401).body(Map.of("message","Invalid email or password"));
  if(!u.isEnabled())return ResponseEntity.status(403).body(Map.of("message","Account disabled"));
  if(!u.isApproved())return ResponseEntity.status(403).body(Map.of("message","Account awaiting admin approval"));
  return ResponseEntity.ok(Map.of("token",jwt.generate(u.getEmail(),u.getRole().name()),"user",safe(u)));
 }
 @GetMapping("/me") public Map<String,Object> me(org.springframework.security.core.Authentication a){User u=users.findByEmail(a.getName()).orElseThrow();return safe(u);}
 private Map<String,Object> safe(User u){return Map.of("id",u.getId(),"fullName",u.getFullName(),"email",u.getEmail(),"role",u.getRole().name(),"department",u.getDepartment()==null?"":u.getDepartment());}
}
