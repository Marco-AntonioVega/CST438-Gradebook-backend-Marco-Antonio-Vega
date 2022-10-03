package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.cst438.controllers.GradeBookController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.GradebookParamsObject;
import com.cst438.services.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

//@SpringBootTest
@ContextConfiguration(classes = { GradeBookController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
class Cst438GradebookApplicationTests {
	static final String URL = "http://localhost:8081";
	public static final int TEST_COURSE_ID = 40442;
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME = "test";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int TEST_YEAR = 2021;
	public static final String TEST_SEMESTER = "Fall";
	public static final String TEST_ASSIGNMENT_NAME = "HW 1";
	public static final int TEST_ASSIGNMENT_ID = 1;
	public static final String TEST_DUE_DATE = "2022-09-15";
	
	@MockBean
	AssignmentRepository assignmentRepository;

	@MockBean
	AssignmentGradeRepository assignmentGradeRepository;

	@MockBean
	CourseRepository courseRepository; // must have this to keep Spring test happy

	@MockBean
	RegistrationService registrationService; // must have this to keep Spring test happy

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void addAssignment() throws Exception {
		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("");
		ag.setStudentEnrollment(enrollment);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.save(any())).willReturn(assignment);
		given(courseRepository.save(any())).willReturn(course);
//		given(assignmentGradeRepository.save(any())).willReturn(ag);
		given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));
		
		// end of mock data
		
		GradebookParamsObject params = new GradebookParamsObject();
		params.setName(TEST_ASSIGNMENT_NAME);
		params.setDueDate(java.sql.Date.valueOf(TEST_DUE_DATE));

		// then do an http post request for adding new assignment
		response = mvc.perform(MockMvcRequestBuilders.post("/gradebook/newAssignment/" + TEST_COURSE_ID)
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(params)).contentType(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		// verify response is okay
		assertEquals(200, response.getStatus());

		// verify that a save was called on assignment repository
		verify(assignmentRepository, times(1)).save(any());
		
//		verify(assignmentGradeRepository, times(1)).save(any());
		
		//verify that a save was called on course repository
		verify(courseRepository, times(1)).save(any());
	}
	
	@Test
	public void updateAssignmentName() throws Exception {
		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("");
		ag.setStudentEnrollment(enrollment);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.findById(TEST_ASSIGNMENT_ID)).willReturn(Optional.of(assignment));
		given(assignmentRepository.save(any())).willReturn(assignment);

		// end of mock data
		
		GradebookParamsObject params = new GradebookParamsObject();
		params.setName(TEST_ASSIGNMENT_NAME);

		// then do an http post request for changing assignment name
		response = mvc.perform(MockMvcRequestBuilders.post("/gradebook/updateAssignment/" + TEST_ASSIGNMENT_ID)
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(params)).contentType(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		// verify response is okay
		assertEquals(200, response.getStatus());

		// verify that a save was called on assignment repository
		verify(assignmentRepository, times(1)).save(any());
		
		//verify that assignment name was changed
		assertEquals("HW 1", assignmentRepository.findById(TEST_ASSIGNMENT_ID).orElse(null).getName());
	}
	
	@Test
	public void deleteAssignment() throws Exception {
		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("");
		ag.setStudentEnrollment(enrollment);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.findById(TEST_ASSIGNMENT_ID)).willReturn(Optional.of(assignment));
		given(assignmentGradeRepository.findByAssignmentId(TEST_ASSIGNMENT_ID)).willReturn(ag);
		given(courseRepository.save(any())).willReturn(course);
		// end of mock data
		
		// then do an http post request for deleting assignment
		response = mvc.perform(MockMvcRequestBuilders.post("/gradebook/deleteAssignment/" + TEST_ASSIGNMENT_ID)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		// verify response is okay
		assertEquals(200, response.getStatus());

		// verify that assignment repository has no records after delete
		assertEquals(0, assignmentRepository.count());
		
		//verify that a delete was called on assignment repository
		verify(assignmentRepository, times(1)).deleteById(TEST_ASSIGNMENT_ID);
		
		// verify that a save was called on course repository
		verify(courseRepository, times(1)).save(any());
	}
	
	@Test
	public void shouldFailDeleteAssignment() throws Exception {
		MockHttpServletResponse response;

		// mock database data

		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);
		course.setSemester(TEST_SEMESTER);
		course.setYear(TEST_YEAR);
		course.setInstructor(TEST_INSTRUCTOR_EMAIL);
		course.setEnrollments(new java.util.ArrayList<Enrollment>());
		course.setAssignments(new java.util.ArrayList<Assignment>());

		Enrollment enrollment = new Enrollment();
		enrollment.setCourse(course);
		course.getEnrollments().add(enrollment);
		enrollment.setId(TEST_COURSE_ID);
		enrollment.setStudentEmail(TEST_STUDENT_EMAIL);
		enrollment.setStudentName(TEST_STUDENT_NAME);

		Assignment assignment = new Assignment();
		assignment.setCourse(course);
		course.getAssignments().add(assignment);
		// set dueDate to 1 week before now.
		assignment.setDueDate(new java.sql.Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		assignment.setId(1);
		assignment.setName("Assignment 1");
		assignment.setNeedsGrading(1);

		AssignmentGrade ag = new AssignmentGrade();
		ag.setAssignment(assignment);
		ag.setId(1);
		ag.setScore("A+");
		ag.setStudentEnrollment(enrollment);

		// given -- stubs for database repositories that return test data
		given(assignmentRepository.findById(TEST_ASSIGNMENT_ID)).willReturn(Optional.of(assignment));
		given(assignmentGradeRepository.findByAssignmentId(TEST_ASSIGNMENT_ID)).willReturn(ag);
		given(courseRepository.save(any())).willReturn(course);
		// end of mock data

		// then do an http post request for deleting assignment
		response = mvc.perform(MockMvcRequestBuilders.post("/gradebook/deleteAssignment/" + TEST_ASSIGNMENT_ID)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		// verify response is okay
		assertEquals(200, response.getStatus());

		// verify that no delete was called on repository
		verify(assignmentRepository, times(0)).delete(any());
		
		// verify that no save was called on course repository
		verify(courseRepository, times(0)).save(any());
	}

	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}