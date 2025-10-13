package com.yuguanzhang.lumi.course.respository;

import com.yuguanzhang.lumi.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c " + "WHERE c.channelUser.channel.channelId IN :channelIds " + "AND c.startDate <= :endDate " + "AND c.endDate >= :startDate " + "ORDER BY c.startDate ASC")
    List<Course> findCoursesInDateRange(@Param("channelIds") List<Long> channelIds,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

}
