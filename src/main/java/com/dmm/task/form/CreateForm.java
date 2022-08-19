package com.dmm.task.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateForm {

	private String title;

	private LocalDate date;

	private String text;

}
