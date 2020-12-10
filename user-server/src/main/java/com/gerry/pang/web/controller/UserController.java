package com.gerry.pang.web.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerry.pang.dto.UserDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@PostMapping
	public UserDTO create(@Validated @RequestBody UserDTO user) {

		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());

		user.setId("1");
		return user;
	}
	
	@PutMapping("/{id:\\d+}")
	public UserDTO update(@Validated @RequestBody UserDTO user, BindingResult errors) {

		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());

		user.setId("1");
		return user;
	}

	@DeleteMapping("/{id:\\d+}")
	public void delete(@PathVariable String id) {
		System.out.println(id);
	}

//	@GetMapping
//	@ApiOperation(value = "用户查询服务")
//	public List<UserDTO> query(UserQueryCondition condition,
//			@PageableDefault(page = 2, size = 17, sort = "username,asc") Pageable pageable) {
//
//		System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));
//
//		System.out.println(pageable.getPageSize());
//		System.out.println(pageable.getPageNumber());
//		System.out.println(pageable.getSort());
//
//		List<UserDTO> users = new ArrayList<>();
//		users.add(new UserDTO());
//		users.add(new UserDTO());
//		users.add(new UserDTO());
//		return users;
//	}

	@GetMapping("/{id:\\d+}")
	public UserDTO getInfo(@PathVariable String id) {
//		throw new RuntimeException("user not exist");
		System.out.println("进入getInfo服务");
		UserDTO user = new UserDTO();
		user.setUsername("tom");
		return user;
	}
}
