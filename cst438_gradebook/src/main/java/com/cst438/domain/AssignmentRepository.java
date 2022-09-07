package com.cst438.domain;

import java.util.List;

import java.sql.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends CrudRepository <Assignment, Integer> {

	@Query("select a from Assignment a where a.needsGrading=1 and a.dueDate < current_date and a.course.instructor= :email order by a.id")
	List<Assignment> findNeedGradingByEmail(@Param("email") String email);
	
	@Query("insert into Assignment values(null, :due_date, :name, 1, :course_id)")
	void addNewAssignment(@Param("due_date") Date due_date, @Param("name") String name, @Param("course_id") int course_id);
	
	@Query("update Assignment set name= :name where assignment_id= :assignment_id")
	void updateAssignmentName(@Param("name") String name, @Param("assignment_id") int assignment_id);
	
	@Query("delete from Assignment a where assignment_id= : assignment_id and a.assignmentGrades is null")
	void deleteAssignment(@Param("assignment_id") int assignment_id);
}
