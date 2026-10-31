
package com.campuscare.controller;
import com.campuscare.model.*; import com.campuscare.repo.*; import com.campuscare.service.CurrentUser;
import org.springframework.beans.factory.annotation.Value; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.client.RestTemplate;
import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/complaints")
public class ComplaintController {
 private final ComplaintRepository complaints; private final UserRepository users; private final HistoryRepository history; private final FeedbackRepository feedback; private final CurrentUser current;
 @Value("${app.ai.url}") private String aiUrl;
 public ComplaintController(ComplaintRepository c,UserRepository u,HistoryRepository h,FeedbackRepository f,CurrentUser cu){complaints=c;users=u;history=h;feedback=f;current=cu;}
 public record Create(String title,String description,String category,String priority,String building,String floorName,String roomNumber,String locationCode){}
 public record StatusUpdate(String status,String remarks){}
 public record Assignment(Long staffId,String department){}
 public record FeedbackInput(int rating,String comments){}
 @GetMapping public List<Complaint> list(){
  User u=current.get(); if(u.getRole()==User.Role.ADMIN)return complaints.findAllByOrderByCreatedAtDesc();
  if(u.getRole()==User.Role.STAFF)return complaints.findByAssignedStaffIdOrderByCreatedAtDesc(u.getId());
  return complaints.findByReporterIdOrderByCreatedAtDesc(u.getId());
 }
 @PostMapping public ResponseEntity<?> create(@RequestBody Create r){
  if(r.title()==null||r.title().isBlank()||r.description()==null||r.description().isBlank())return ResponseEntity.badRequest().body(Map.of("message","Title and description are required"));
  Map<String,String> prediction=predict(r.description());
  Complaint c=new Complaint();c.setComplaintCode("CMP"+System.currentTimeMillis());c.setTitle(r.title());c.setDescription(r.description());
  c.setCategory(r.category()==null||r.category().equals("AUTO")?prediction.get("category"):r.category());
  c.setPriority(r.priority()==null||r.priority().equals("AUTO")?prediction.get("priority"):r.priority());
  c.setDepartment(prediction.get("department"));c.setBuilding(r.building());c.setFloorName(r.floorName());c.setRoomNumber(r.roomNumber());c.setLocationCode(r.locationCode());c.setReporter(current.get());c.setSlaDueAt(LocalDateTime.now().plusHours(c.getPriority().equals("CRITICAL")?4:c.getPriority().equals("HIGH")?24:72));
  return ResponseEntity.ok(complaints.save(c));
 }
 @GetMapping("/{id}") public ResponseEntity<?> get(@PathVariable Long id){return complaints.findById(id).filter(c->current.admin()||c.getReporter().getId().equals(current.get().getId())||(c.getAssignedStaff()!=null&&c.getAssignedStaff().getId().equals(current.get().getId()))).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PatchMapping("/{id}/assign") public ResponseEntity<?> assign(@PathVariable Long id,@RequestBody Assignment a){
  if(!current.admin())return ResponseEntity.status(403).body(Map.of("message","Admin only"));
  Complaint c=complaints.findById(id).orElseThrow();if(a.staffId()!=null)c.setAssignedStaff(users.findById(a.staffId()).orElseThrow());if(a.department()!=null)c.setDepartment(a.department());
  String old=c.getStatus();c.setStatus("ASSIGNED");history.save(new ComplaintHistory(c,current.get(),old,"ASSIGNED","Assigned by admin"));return ResponseEntity.ok(complaints.save(c));
 }
 @PatchMapping("/{id}/status") public ResponseEntity<?> status(@PathVariable Long id,@RequestBody StatusUpdate s){
  Complaint c=complaints.findById(id).orElseThrow();User actor=current.get();
  boolean allowed=actor.getRole()==User.Role.ADMIN||(actor.getRole()==User.Role.STAFF&&c.getAssignedStaff()!=null&&c.getAssignedStaff().getId().equals(actor.getId()));
  if(!allowed)return ResponseEntity.status(403).body(Map.of("message","Only assigned staff or admin can update status"));
  String old=c.getStatus();c.setStatus(s.status().toUpperCase());if("RESOLVED".equalsIgnoreCase(s.status()))c.setResolutionRemarks(s.remarks());
  history.save(new ComplaintHistory(c,actor,old,c.getStatus(),s.remarks()));return ResponseEntity.ok(complaints.save(c));
 }
 @PostMapping("/{id}/feedback") public ResponseEntity<?> giveFeedback(@PathVariable Long id,@RequestBody FeedbackInput f){
  Complaint c=complaints.findById(id).orElseThrow();if(!c.getReporter().getId().equals(current.get().getId()))return ResponseEntity.status(403).build();
  if(f.rating()<1||f.rating()>5)return ResponseEntity.badRequest().body(Map.of("message","Rating must be 1 to 5"));
  feedback.save(new Feedback(c,current.get(),f.rating(),f.comments()));c.setStatus("CLOSED");return ResponseEntity.ok(complaints.save(c));
 }
 private Map<String,String> predict(String text){
  try{return new RestTemplate().postForObject(aiUrl,Map.of("description",text),Map.class);}
  catch(Exception ex){String t=text.toLowerCase();String cat="OTHER",dept="ADMINISTRATION",p="MEDIUM";
   if(t.contains("wifi")||t.contains("internet")||t.contains("network")){cat="NETWORK";dept="IT SUPPORT";}
   else if(t.contains("water")||t.contains("tap")||t.contains("leak")){cat="PLUMBING";dept="MAINTENANCE";}
   else if(t.contains("electric")||t.contains("wire")||t.contains("power")){cat="ELECTRICAL";dept="ELECTRICAL";}
   else if(t.contains("projector")||t.contains("lab")){cat="LAB_EQUIPMENT";dept="LAB SUPPORT";}
   if(t.contains("fire")||t.contains("shock")||t.contains("danger")||t.contains("smoke"))p="CRITICAL";else if(t.contains("not working")||t.contains("leak"))p="HIGH";
   return Map.of("category",cat,"priority",p,"department",dept);
  }
 }
}
