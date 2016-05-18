package io.github.liuzm.manage.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.liuzm.distribute.common.model.Node;
import io.github.liuzm.manage.api.ClientService;

@Controller
@RequestMapping("/client/")
public class ClientController {
	
	@Autowired
	private ClientService clientService;
	
	
	@RequestMapping("list")
	@ResponseBody
	public List<Node> listClients(){
		return clientService.getClients();
	}
	
	
	
	
}
