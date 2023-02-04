package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import event.Event;
import event.EventService;
import event.EventType;
import event.SearchOption;

@Controller

@RequestMapping("/event")
public class EventController {

	private EventService eventService = new EventService();
	
	@ModelAttribute("recEventList")  //request.setAttribute("recEventList", return object)
	public List<Event> recommend(){
		return eventService.getRecommendedEventService();
	}

	@RequestMapping("list")
	public String list(Model model, SearchOption option) {
		List<Event> eventList = eventService.getOpenEventsList(option);
	
		model.addAttribute("eventList", eventList); //request.setAttribute
		model.addAttribute("eventTypes", EventType.values());
		return "/event/list";
	}
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat("yyyyMMdd"), true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	
}
