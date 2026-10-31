
package com.campuscare.repo;
import com.campuscare.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ComplaintRepository extends JpaRepository<Complaint,Long>{
 List<Complaint> findByReporterIdOrderByCreatedAtDesc(Long id);
 List<Complaint> findByAssignedStaffIdOrderByCreatedAtDesc(Long id);
 List<Complaint> findAllByOrderByCreatedAtDesc();
 long countByStatus(String status); long countByPriority(String priority);
}
