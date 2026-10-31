
package com.campuscare.controller;
import com.campuscare.model.*; import com.campuscare.repo.*; import com.campuscare.service.CurrentUser;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/admin")
public class AdminController {
 private final UserRepository users;private final ComplaintRepository complaints;private final CurrentUser current;
 public AdminController(UserRepository u,ComplaintRepository c,CurrentUser cu){users=u;complaints=c;current=cu;}
 @GetMapping("/analytics") public Map<String,Object> analytics(){
  if(!current.admin())throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN);
  return Map.of("total",complaints.count(),"submitted",complaints.countByStatus("SUBMITTED"),"assigned",complaints.countByStatus("ASSIGNED"),"inProgress",complaints.countByStatus("IN_PROGRESS"),"resolved",complaints.countByStatus("RESOLVED"),"closed",complaints.countByStatus("CLOSED"),"critical",complaints.countByPriority("CRITICAL"),"users",users.count());
 }
 @GetMapping("/users") public List<User> allUsers(){if(!current.admin())throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN);return users.findAll();}
 @PatchMapping("/users/{id}/approve") public Map<String,Object> approve(@PathVariable Long id){if(!current.admin())throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN);User u=users.findById(id).orElseThrow();u.setApproved(true);users.save(u);return Map.of("message","User approved");}
 @GetMapping("/escalate-overdue") public Map<String,Object> overdue(){if(!current.admin())throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN);long count=complaints.findAll().stream().filter(c->c.getSlaDueAt()!=null&&c.getSlaDueAt().isBefore(LocalDateTime.now())&&!List.of("RESOLVED","CLOSED").contains(c.getStatus())).count();return Map.of("overdueCount",count,"message","Overdue complaints identified for escalation");}
 @GetMapping(value="/complaints/export",produces="text/csv") public ResponseEntity<String> export(){if(!current.admin())return ResponseEntity.status(403).build();StringBuilder b=new StringBuilder("Code,Title,Category,Priority,Status,Department,Created At\n");for(Complaint c:complaints.findAllByOrderByCreatedAtDesc())b.append(c.getComplaintCode()).append(",\"").append(c.getTitle().replace("\"","\"\"")).append("\",").append(c.getCategory()).append(",").append(c.getPriority()).append(",").append(c.getStatus()).append(",").append(c.getDepartment()).append(",").append(c.getCreatedAt()).append("\n");return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=campuscare-report.csv").body(b.toString());}
}
