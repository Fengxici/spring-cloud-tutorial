package timing.springcloud.tutorial.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RefreshScope
public class ClientController {
	@Value("${service.url}")
	private String serviceUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
    private DiscoveryClient client;
	@Value("${consumer.hello}")
	private String hello;

	@RequestMapping("/consumer")
	public String from() {
		return this.hello;
	}
	
	@GetMapping("/index")
	public ModelAndView index() {
		test();
		ModelAndView modelAndView = new ModelAndView("/index.html");
		return modelAndView;
	}
	
	@GetMapping("/service")
	public Map<String,String> service() {
		Map<String,String> map = new HashMap<>();
		map.put("mykey", "mydata");
		return map;
	}
	private void test() {
		List<ServiceInstance> instances = client.getInstances("provider-service");
        for (int i = 0; i < instances.size(); i++) {
        	System.out.println("/hello,host:" + instances.get(i).getHost() + ",service_id:" + instances.get(i).getServiceId());
        }
        System.out.println(serviceUrl);
		String ret = restTemplate.getForObject(serviceUrl+"/service", String.class);
		System.out.println("response  = " + ret);

		ret = restTemplate.getForObject(serviceUrl+"/service?para=test", String.class);
		System.out.println("response1  = " + ret);
	}
}
