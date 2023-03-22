package edu.bbte.idde.seim1964.spring.controller;

import edu.bbte.idde.seim1964.spring.dao.UserDao;
import edu.bbte.idde.seim1964.spring.model.User;
import edu.bbte.idde.seim1964.spring.model.UserMapper;
import edu.bbte.idde.seim1964.spring.model.dto.incoming.UserCreationDto;
import edu.bbte.idde.seim1964.spring.model.dto.outgoing.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public Collection<UserResponseDto> findAll() {
        return userMapper.modelsToDtos(userDao.findAll());
    }

    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable("id") Long id, @RequestParam(required = false) String when) {
        Optional<User> result = userDao.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Error: User not found.");
        }

        if(when == null) {
            throw new WrongPathException("Error: Wrong Path, you need a when request parameter to the path");
        }

        if(!when.equals("off") && !when.equals("on")) {
            throw new ValueNotSupportedException("Error: Value not supported");
        }

        if(Objects.equals(when, "off")) {
            return userMapper.modelToDto(result.get());
        }

        UserResponseDto rdto = userMapper.modelToDto(result.get());
        rdto.setRequestDate(Instant.now());

        return rdto;
    }

    @PostMapping
    public UserResponseDto create(@RequestBody @Valid UserCreationDto dto) {
        User user = userDao.saveAndFlush(userMapper.creationDtoToModel(dto));
        return userMapper.modelToDto(user);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@RequestBody @Valid UserCreationDto dto,
                                  @PathVariable("id") Long id) {
        User newUser = userMapper.creationDtoToModel(dto);
        newUser.setId(id);
        User result = userDao.saveAndFlush(newUser);
        if (result == null) {
            throw new NotFoundException("Error: User to be updated not found.");
        }

        return userMapper.modelToDto(result);
    }

}

