
package com.campuscare.repo;
import com.campuscare.model.Feedback; import org.springframework.data.jpa.repository.JpaRepository;
public interface FeedbackRepository extends JpaRepository<Feedback,Long>{}
