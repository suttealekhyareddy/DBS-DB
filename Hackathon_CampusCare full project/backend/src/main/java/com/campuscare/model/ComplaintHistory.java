
package com.campuscare.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="complaint_history")
public class ComplaintHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Complaint complaint;
 @ManyToOne(optional=false) private User changedBy;
 private String oldStatus; private String newStatus;
 @Column(columnDefinition="TEXT") private String remarks;
 private LocalDateTime changedAt=LocalDateTime.now();
 protected ComplaintHistory(){}
 public ComplaintHistory(Complaint c,User u,String oldS,String newS,String r){complaint=c;changedBy=u;oldStatus=oldS;newStatus=newS;remarks=r;}
}
