package com.gtg.controller;


import java.util.List;

import com.gtg.model.Test;
import com.jfinal.core.Controller;

public class HelloController extends Controller{
	
	public void index(){
		List<Test> list = Test.dao.find("select * from test");
		System.out.println(list);
		renderText("Hello JFinal");
	}
}
