package biz.conversity.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
	
	@RequestMapping(value = "/test", method=RequestMethod.GET)
	public long TestGet() {
		System.out.println("Test Get");
		return 1973;
	}
	
	@RequestMapping(value ="/test", method=RequestMethod.POST)
	public String TestPost() {
		System.out.println("Test Post");
		return "nineteen seventy-three";
	}
	
	@RequestMapping(value="/test", method=RequestMethod.DELETE)
	public boolean TestDelete() {
		System.out.println("Test Delete");
		return true;
	}
	
	@RequestMapping(value="/test", method=RequestMethod.PUT)
	public void TestPut() {
		System.out.println("Test Put");
	}
}
