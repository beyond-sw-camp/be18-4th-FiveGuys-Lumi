package com.yuguanzhang.lumi.assignment.repository;

import com.yuguanzhang.lumi.assignment.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    boolean existsBySubmission_SubmissionId(Long submissionId);

    Optional<Evaluation> findBySubmission_SubmissionId(Long submissionId);
}
