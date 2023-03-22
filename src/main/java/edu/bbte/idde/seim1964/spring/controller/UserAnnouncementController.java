package edu.bbte.idde.seim1964.spring.controller;

import edu.bbte.idde.seim1964.spring.dao.UserDao;
import edu.bbte.idde.seim1964.spring.model.AnnouncementMapper;
import edu.bbte.idde.seim1964.spring.model.User;
import edu.bbte.idde.seim1964.spring.model.dto.incoming.AnnouncementCreationDto;
import edu.bbte.idde.seim1964.spring.model.dto.outgoing.AnnouncementResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/users/announcements")
@Slf4j
public class UserAnnouncementController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnnouncementMapper announcementMapper;

    // LIST ANNOUNCEMENTS THAT BELONG TO A PARTICULAR USER
    @GetMapping("/{id}")
    public Collection<AnnouncementResponseDto> findAnnouncementsById(@PathVariable("id") Long id) {
        Optional<User> result = userDao.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Error: User not found.");
        }

        return announcementMapper.modelsToDtos(result.get().getAnnouncements());
    }

    // DELETE AN ANNOUNCEMENT THROUGH THE USER
    @DeleteMapping("/{idUser}/{idAnnouncement}")
    public void deleteAnnouncements(@PathVariable("idUser") Long idUser,
                                    @PathVariable("idAnnouncement") Long idAnnouncement) {
        Optional<User> result = userDao.findById(idUser);
        if (result.isEmpty()) {
            throw new NotFoundException("Error: User not found.");
        }
        result.get().deleteAnnouncements(idAnnouncement);
        userDao.saveAndFlush(result.get());
    }

    // CREATE AN ANNOUNCEMENT BY A USER
    @PutMapping("/{id}")
    public AnnouncementResponseDto updateAnnouncements(@RequestBody @Valid AnnouncementCreationDto dto,
                                                       @PathVariable("id") Long id) {
        Optional<User> result = userDao.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Error: User not found.");
        }

        result.get().setAnnouncements(announcementMapper.creationDtoToModel(dto));
        userDao.saveAndFlush(result.get());
        return announcementMapper.modelToDto(announcementMapper.creationDtoToModel(dto));
    }
}
