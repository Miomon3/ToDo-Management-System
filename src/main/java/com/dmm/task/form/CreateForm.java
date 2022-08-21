package com.dmm.task.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CreateForm {

	private String title;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	private String text;

}
