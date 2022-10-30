package com.cst438.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends CrudRepository <Course, Integer> {
	@Query("select c from Course c where c.course_id = :course_id and c.instructor = :instructor")
	List<Course> verifyCourseInstructor(@Param("course_id") int course_id, @Param("instructor") String instructor);
	
	@Query("select c from Course c where c.instructor = :instructor")
	List<Course> findByEmail(@Param("instructor") String instructor);
}