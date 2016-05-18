/**
 * 
 */
package io.github.liuzm.manage.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import io.github.liuzm.manage.web.model.Menu;

/**
 * @author xh-liuzhimin
 *
 */
@Controller
@RequestMapping("/menu/")
public class MenuController {
	
	@RequestMapping("list")
	@ResponseBody
	public List<Menu> listmenu(){
		List<Menu> meuns = new ArrayList<>();
		Menu menu1 = new Menu(0,"服务端管理","/server/detail","");
		Menu menu2 = new Menu(0,"客户端端管理","/client/detail","");
		Menu _menu2 = new Menu(0,"客户端端管理","/client/detail","");
			menu2.setSubmenu(_menu2);
		Menu menu3 = new Menu(0,"日志管理","/server/detail","");
		Menu menu4 = new Menu(0,"监控信息","/server/detail","");
		
		meuns.add(menu1);
		meuns.add(menu2);
		meuns.add(menu3);
		meuns.add(menu4);
		return meuns;
	}
}
