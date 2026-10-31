
package com.campuscare.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="feedback")
public class Feedback {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) private Complaint complaint;
 @ManyToOne(optional=false) private User user;
 private int rating; @Column(columnDefinition="TEXT") private String comments;
 private LocalDateTime createdAt=LocalDateTime.now();
 protected Feedback(){}
 public Feedback(Complaint c,User u,int r,String text){complaint=c;user=u;rating=r;comments=text;}
}
