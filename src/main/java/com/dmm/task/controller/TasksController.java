package com.dmm.task.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.CreateForm;
import com.dmm.task.form.EditForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class TasksController {
	
	@Autowired
	private TasksRepository repo;
	
	// カレンダータスクの一覧表示
	@GetMapping("/main")
	public String mainTasks(Model model) {
		
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		List<Tasks> taskList = repo.findAll();
		
		List<List<LocalDate>> matrix = new ArrayList<>();
		List<LocalDate> week = new ArrayList<>();

		matrix.add(week);
		
		LocalDate x;
		DayOfWeek y;

		x = LocalDate.now();
		x = LocalDate.of(x.getYear(), x.getMonthValue(), 1);
		y = x.getDayOfWeek();
		
		x = x.minusDays(y.getValue());
		
		// 1週目
		for (int i = 0; i < 7; i++) {
			week.add(x);
			x = x.plusDays(1);
			for(int j = 0; j < taskList.size(); j++) {
                if(taskList.get(j).getDate().isEqual(x)) {
                        tasks.add(x, taskList.get(j));
                }
			}
		}

		// 2週目以降
		week = new ArrayList<>();
		matrix.add(week);
		for (int i = 7; i <= x.lengthOfMonth(); i++) {
			y = x.getDayOfWeek();
			week.add(x);
			if (y.getValue() == 6) {
				week = new ArrayList<>();
				matrix.add(week);
			}
			x = x.plusDays(1);
			for(int j = 0; j < taskList.size(); j++) {
                if(taskList.get(j).getDate().isEqual(x)) {
                        tasks.add(x, taskList.get(j));
                }
			}
		}

		// 最終週
		y = x.getDayOfWeek();
		for (int i = 0; i < 7 - y.getValue(); i++) {
			week.add(x);
			x = x.plusDays(1);
			for(int j = 0; j < taskList.size(); j++) {
                if(taskList.get(j).getDate().isEqual(x)) {
                        tasks.add(x, taskList.get(j));
                }
			}
		}
		
		model.addAttribute("matrix", matrix);
		model.addAttribute("tasks", tasks);
		
		LocalDate today;
		today = LocalDate.now();
		
		model.addAttribute("prev", today.minusMonths(1));
		model.addAttribute("month", today.getYear() + "年" + today.getMonthValue() + "月");
		model.addAttribute("next", today.plusMonths(1));
		
		CreateForm createForm = new CreateForm();
		model.addAttribute("createForm", createForm);

		return "main";
	}
	
	
	// 投稿の新規登録
	@GetMapping("/main/create/{yyyy-MM-dd}")
	public String create(Model model) {
		
		CreateForm createForm = new CreateForm();
        model.addAttribute("CreateForm", createForm);
        
		return "create";
	}
	
	@PostMapping("/main/create")
	public String createPost(CreateForm createForm, @AuthenticationPrincipal AccountUserDetails user, Model model) {

		Tasks task = new Tasks();
		task.setName(user.getName());
		task.setTitle(createForm.getTitle());
		task.setDate(createForm.getDate());
		task.setText(createForm.getText());
		//task.setDone(createForm.isDone());
		task.setDone(false);

		repo.save(task);

		return "redirect:/main";
	}
	
	
	// 投稿の編集
	@GetMapping("/main/edit/{id}")
	public String edit(Model model, @PathVariable Integer id) {
		
		Tasks task = repo.getById(id);
		model.addAttribute("task", task);
        
		return "edit";
	}
	
	@PostMapping("/main/edit/{id}")
	public String editPost(EditForm editForm, @AuthenticationPrincipal AccountUserDetails user, Model model, @PathVariable Integer id) {
		
		Tasks task = new Tasks();
		task.setId(id);
		task.setName(user.getName());
		task.setTitle(editForm.getTitle());
		task.setDate(editForm.getDate());
		task.setText(editForm.getText());
		task.setDone(editForm.isDone());

		repo.save(task);

		return "redirect:/main";
	}
	
	
	// 投稿の削除
	@PostMapping("/main/delete/{id}")
	public String deletePost(@PathVariable Integer id) {
		repo.deleteById(id);
		return "redirect:/main";
	}
	
}
