package com.dmm.task.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EditForm {

	private String title;

	private LocalDate date;

	private String text;

	private Boolean done;

}
