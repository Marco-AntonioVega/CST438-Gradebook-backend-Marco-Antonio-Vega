package com.cst438.domain;

import java.util.ArrayList;

/*
 * a transfer object that is a list of assignment details
 */
public class StudentGradesDTO {

	public static class StudentAssignmentDTO {
		public int assignmentId;
		public String assignmentName;
		public String dueDate;
		public String courseTitle;
		public int courseId;
		public String score;

		public StudentAssignmentDTO(int assignmentId, int courseId, String assignmentName, String dueDate,
				String courseTitle, String score) {
			this.assignmentId = assignmentId;
			this.courseId = courseId;
			this.assignmentName = assignmentName;
			this.dueDate = dueDate;
			this.courseTitle = courseTitle;
			this.score = score;
		}

		@Override
		public String toString() {
			return "[assignmentId=" + assignmentId + ", assignmentName=" + assignmentName + ", dueDate="
					+ dueDate + ", courseTitle=" + courseTitle + ", courseId=" + courseId + ", score=" + score + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StudentAssignmentDTO other = (StudentAssignmentDTO) obj;
			if (assignmentId != other.assignmentId)
				return false;
			if (assignmentName == null) {
				if (other.assignmentName != null)
					return false;
			} else if (!assignmentName.equals(other.assignmentName))
				return false;
			if (courseId != other.courseId)
				return false;
			if (courseTitle == null) {
				if (other.courseTitle != null)
					return false;
			} else if (!courseTitle.equals(other.courseTitle))
				return false;
			if (dueDate == null) {
				if (other.dueDate != null)
					return false;
			} else if (!dueDate.equals(other.dueDate))
				return false;
			if (score == null) {
				if (other.score != null)
					return false;
			} else if (!score.equals(other.score))
				return false;
			return true;
		}
	}

	public ArrayList<StudentAssignmentDTO> assignments = new ArrayList<>();

	@Override
	public String toString() {
		return "StudentAssignmentListDTO " + assignments ;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentGradesDTO other = (StudentGradesDTO) obj;
		if (assignments == null) {
			if (other.assignments != null)
				return false;
		} else if (!assignments.equals(other.assignments))
			return false;
		return true;
	}
}