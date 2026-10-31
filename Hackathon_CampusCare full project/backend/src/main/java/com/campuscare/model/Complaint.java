
package com.campuscare.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="complaints")
public class Complaint {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String complaintCode;
 @Column(nullable=false) private String title;
 @Column(nullable=false,columnDefinition="TEXT") private String description;
 @Column(nullable=false) private String category;
 @Column(nullable=false) private String priority="MEDIUM";
 @Column(nullable=false) private String status="SUBMITTED";
 private String building; private String floorName; private String roomNumber; private String locationCode; private String department;
 @ManyToOne(optional=false) private User reporter;
 @ManyToOne private User assignedStaff;
 @Column(columnDefinition="TEXT") private String resolutionRemarks;
 private LocalDateTime slaDueAt;
 private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt=LocalDateTime.now();
 @PreUpdate public void update(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getComplaintCode(){return complaintCode;} public void setComplaintCode(String v){complaintCode=v;}
 public String getTitle(){return title;} public void setTitle(String v){title=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public String getCategory(){return category;} public void setCategory(String v){category=v;}
 public String getPriority(){return priority;} public void setPriority(String v){priority=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getBuilding(){return building;} public void setBuilding(String v){building=v;}
 public String getFloorName(){return floorName;} public void setFloorName(String v){floorName=v;}
 public String getRoomNumber(){return roomNumber;} public void setRoomNumber(String v){roomNumber=v;}
 public String getLocationCode(){return locationCode;} public void setLocationCode(String v){locationCode=v;}
 public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
 public User getReporter(){return reporter;} public void setReporter(User v){reporter=v;}
 public User getAssignedStaff(){return assignedStaff;} public void setAssignedStaff(User v){assignedStaff=v;}
 public String getResolutionRemarks(){return resolutionRemarks;} public void setResolutionRemarks(String v){resolutionRemarks=v;}
 public LocalDateTime getSlaDueAt(){return slaDueAt;} public void setSlaDueAt(LocalDateTime v){slaDueAt=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
