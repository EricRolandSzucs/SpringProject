package edu.bbte.idde.seim1964.spring.controller;

import edu.bbte.idde.seim1964.spring.model.Announcement;
import edu.bbte.idde.seim1964.spring.model.dto.incoming.AnnouncementCreationDto;
import edu.bbte.idde.seim1964.spring.model.dto.outgoing.AnnouncementResponseDto;
import edu.bbte.idde.seim1964.spring.model.AnnouncementMapper;
import edu.bbte.idde.seim1964.spring.dao.AnnouncementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementDao announcementDao;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @GetMapping
    public Collection<AnnouncementResponseDto> findAll(@RequestParam(required = false) String title) {
        if (title == null) {
            return announcementMapper.modelsToDtos(announcementDao.findAll());
        }
        Collection<Announcement> announcements = announcementDao.findByTitle(title);
        return announcementMapper.modelsToDtos(announcements);
    }

    @GetMapping("/{id}")
    public AnnouncementResponseDto findById(@PathVariable("id") Long id) {
        Optional<Announcement> result = announcementDao.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Error: Announcement not found.");
        }

        return announcementMapper.modelToDto(result.get());
    }

    @PostMapping
    public AnnouncementResponseDto create(@RequestBody @Valid AnnouncementCreationDto dto) {
        Announcement announcement = announcementDao.saveAndFlush(announcementMapper.creationDtoToModel(dto));
        return announcementMapper.modelToDto(announcement);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        announcementDao.deleteById(id);
    }

    @PutMapping("/{id}")
    public AnnouncementResponseDto update(@RequestBody @Valid AnnouncementCreationDto dto,
                                          @PathVariable("id") Long id) {
        Announcement newAnnouncement = announcementMapper.creationDtoToModel(dto);
        newAnnouncement.setId(id);
        Long carId = announcementDao.findCarId(id);
        newAnnouncement.getCar().setId(carId);
        Announcement result = announcementDao.saveAndFlush(newAnnouncement);
        if (result == null) {
            throw new NotFoundException("Error: Announcement to be updated not found.");
        }
        return announcementMapper.modelToDto(result);
    }
}
