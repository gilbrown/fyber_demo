package com.fyber.demo.controller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;

import io.micrometer.core.instrument.util.StringUtils;
@RestController
public class Controller {
	@Autowired
	private ConsulClient consulClient;
	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}
	@GetMapping("getkey")
	public String getFoos(@RequestParam String key) {
		try {

			Response<GetValue> orgResponse = consulClient.getKVValue(key);
			GetValue getValue = orgResponse.getValue();
			if (getValue != null && StringUtils.isNotBlank(getValue.getValue())) {
				String value = new String(Base64.decodeBase64(getValue.getValue()));
				return value;
			}	    
			return "Cannot Find Key: ["+key+"]";	    			   
		} catch (Exception error) {
			return error.getMessage();
		}
	}

	@GetMapping("setkey")
	public String getFoos(@RequestParam String key,@RequestParam String value) {
		try {	    	
			consulClient.setKVValue(key,value);
			return "Success";
		} catch (Exception error) {
			return "Fail";
		}
	}

}
