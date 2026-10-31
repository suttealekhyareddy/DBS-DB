
package com.campuscare.model;
import jakarta.persistence.*; import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String fullName;
 @Column(nullable=false, unique=true) private String email;
 @Column(unique=true) private String universityId;
 private String phone; private String department; private String yearOfStudy; private String sectionName; private String designation;
 @JsonIgnore @Column(nullable=false) private String passwordHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role;
 private boolean approved=false; private boolean enabled=true;
 private LocalDateTime createdAt=LocalDateTime.now();
 public enum Role { STUDENT, FACULTY, STAFF, ADMIN }
 public Long getId(){return id;} public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getUniversityId(){return universityId;} public void setUniversityId(String v){universityId=v;}
 public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
 public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
 public String getYearOfStudy(){return yearOfStudy;} public void setYearOfStudy(String v){yearOfStudy=v;}
 public String getSectionName(){return sectionName;} public void setSectionName(String v){sectionName=v;}
 public String getDesignation(){return designation;} public void setDesignation(String v){designation=v;}
 public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;}
 public boolean isApproved(){return approved;} public void setApproved(boolean v){approved=v;}
 public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
