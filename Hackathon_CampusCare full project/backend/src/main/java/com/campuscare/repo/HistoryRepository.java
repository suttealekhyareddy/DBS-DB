
package com.campuscare.repo;
import com.campuscare.model.ComplaintHistory; import org.springframework.data.jpa.repository.JpaRepository;
public interface HistoryRepository extends JpaRepository<ComplaintHistory,Long>{}
