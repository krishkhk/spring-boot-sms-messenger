package com.sms.provider.scheduler.controller;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sms.provider.scheduler.dailynotifier.NotificationService;
import com.sms.provider.scheduler.model.MessageLog;
import com.sms.provider.scheduler.model.MessageTemplate;
import com.sms.provider.scheduler.repository.MessageLogRepository;
import com.sms.provider.scheduler.repository.MessageTemplateRepository;

@Controller
public class NotificationController {
	
	private final NotificationService notificationService;
	private final MessageLogRepository logRepository;
    private final MessageTemplateRepository templateRepository;
	
	@Value("${recipient.phone-number}")
    private String defaultRecipient;
	
	public NotificationController(NotificationService notificationService,MessageLogRepository logRepository,MessageTemplateRepository templateRepository) {
        this.notificationService = notificationService;
		this.logRepository = logRepository;
		this.templateRepository = templateRepository;
    }
	
	 /**
     * Displays the main form to send messages.
     */
    @GetMapping("/")
    public String showForm(Model model,Authentication authentication,
    		@RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "logPage", defaultValue = "0") int logPageNum,
            @RequestParam(value = "logSize", defaultValue = "4") int logSize) {
    	
    	if(authentication!=null) {
    		model.addAttribute("loggedInUsername",authentication.getName());
    	}
       
    	model.addAttribute("recipient", defaultRecipient);
        model.addAttribute("templates", templateRepository.findAll());
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageTemplate> templatePage;
        
        if(searchName !=null && !searchName.trim().isEmpty()) {
        	templatePage = templateRepository.findByTemplateNameContainingIgnoreCase(searchName, pageable);
        }else {
        	templatePage = templateRepository.findAll(pageable);
        }
        
        model.addAttribute("templatePage", templatePage);
        model.addAttribute("searchName", searchName);
        model.addAttribute("allTemplates",templateRepository.findAll());
        
        //Add message log
        Pageable logPageable = PageRequest.of(logPageNum, logSize);
        Page<MessageLog>logPage = logRepository.findAllByOrderByTimestampDesc(logPageable);
        model.addAttribute("logPage", logPage);
        return "index";
    }
    
    
    
    
    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("to") String to,
            @RequestParam("smsMessage") String smsMessage,
            @RequestParam("whatsappMessage") String whatsappMessage,
            RedirectAttributes redirectAttributes) {
    	
    	boolean smsSent = false;
    	boolean whatsappSent =false;
    	
    	if(smsMessage !=null && !smsMessage.trim().isEmpty()) {
    		notificationService.sendSMS(to, smsMessage);
    		smsSent=true;
    	}
    	
    	if(whatsappMessage !=null && !whatsappMessage.trim().isEmpty()) {
    		notificationService.sendWhatsAppMessage(to, whatsappMessage);
    		whatsappSent=true;
    	}
    	
    	redirectAttributes.addFlashAttribute("successMessage","Messages sent successfully!");
    	
    	return "redirect:/";
    }
    
	@PostMapping("/add-template")
	public String addTemplate(@RequestParam("templateName") String templateName,
			@RequestParam("templateBody") String templateBody, RedirectAttributes redirectAttributes) {

		MessageTemplate template = new MessageTemplate();
		template.setTemplateName(templateName);
		template.setMessageBody(templateBody);
		templateRepository.save(template);

		redirectAttributes.addFlashAttribute("successMessage", "Template saved successfully!");

		return "redirect:/";

	}
	
	@PostMapping("/delete-template/{id}")
	 public String deleteTemplate(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		 templateRepository.deleteById(id);
		 redirectAttributes.addFlashAttribute("successMessage", "Template deleted successfully!");
		 return "redirect:/";
	 }

}
