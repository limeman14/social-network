package com.skillbox.socialnetwork.main.service.impl;

import com.skillbox.socialnetwork.main.dto.AbstractResponse;
import com.skillbox.socialnetwork.main.dto.post.PostResponseFactory;
import com.skillbox.socialnetwork.main.dto.profile.SearchPersonDto;
import com.skillbox.socialnetwork.main.dto.profile.WallDto;
import com.skillbox.socialnetwork.main.dto.profile.WallResponseFactory;
import com.skillbox.socialnetwork.main.dto.request.AddPostRequestDto;
import com.skillbox.socialnetwork.main.dto.request.UpdateUserDto;
import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import com.skillbox.socialnetwork.main.dto.users.PersonResponseFactory;
import com.skillbox.socialnetwork.main.model.Person;
import com.skillbox.socialnetwork.main.model.Post;
import com.skillbox.socialnetwork.main.model.Post2tag;
import com.skillbox.socialnetwork.main.model.Tag;
import com.skillbox.socialnetwork.main.repository.PersonRepository;
import com.skillbox.socialnetwork.main.repository.PostRepository;
import com.skillbox.socialnetwork.main.repository.TagRepository;
import com.skillbox.socialnetwork.main.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {


    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;


    @Autowired
    public ProfileServiceImpl(PersonRepository personRepository, PostRepository postRepository, TagRepository tagRepository) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public AbstractResponse getMyProfile(Person person) {
        log.info("IN getMyProfile person: {} found", person);
        return PersonResponseFactory.getPerson(person);
    }

    @Override
    public AbstractResponse editMyProfile(Person person, UpdateUserDto request) {
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setBirthDate(request.getBirthDate());
        person.setPhone(request.getPhone());
        person.setPhoto(request.getPhotoId()); // Здесь надо изменить механизм присвоения картинок.
        person.setAbout(request.getAbout());
        person.setTown(null);// здесь нужно определить функциональность на счёт города

        log.info("IN editMyProfile person: {} edited with request: {} and saved to a database", person, request);
        return PersonResponseFactory.getPerson(personRepository.save(person));
    }

    @Override
    public BaseResponseDto deleteMyProfile(Person person) {
        personRepository.delete(person);
        log.info("IN deleteMyProfile user: {} deleted successfully", person);
        return new BaseResponseDto();
    }

    @Override
    public AbstractResponse getUserById(int id) {
        Optional<Person> person = personRepository.findById(id);
        AbstractResponse result = person.map(PersonResponseFactory::getPerson).orElse(null);
        log.info("IN getUserById user with id: {}"+(result!=null ? "  found." : "NOT FOUND"), id);
        return result;
    }

    @Override
    public WallDto getWallPosts(int id, int offset, int limit) {
        Optional<Person> person = personRepository.findById(id);
        WallDto result = person.map(value -> WallResponseFactory.getWall(value.getPosts(), offset, limit)).orElse(null);
        log.info("IN getWallPosts posts" + (result!=null ? ": ("+result.getData().size()+") have found" : "not found") );
        return result;
    }

    @Override
    public AbstractResponse addPost(int id, long publishDate, AddPostRequestDto request) {
        Optional<Person> person = personRepository.findById(id);
        if(person.isPresent()){
            Post post = new Post();
            post.setAuthor(person.get());
            if (publishDate == 0) {
                post.setTime(new Date());
            } else {
                post.setTime(new Date(publishDate));
            }
            post.setTitle(request.getTitle());
            post.setPostText(request.getText());
            post.setIsBlocked(false);
            //collections initialized for exluding nullPointer in PostResponseFactoryMethods
            post.setBlockHistories(new ArrayList<>());
            post.setComments(new ArrayList<>());
            post.setFiles(new ArrayList<>());
            post.setLikes(new ArrayList<>());
            //tags
            List<Post2tag> tags = new ArrayList<>();
            request.getTags().forEach(tag -> {
                Post2tag ttp = new Post2tag();
                ttp.setPost(post);
                if(!tagRepository.existsByTagIgnoreCase(tag)){
                    Tag t = new Tag();
                    t.setTag(tag);
                    tagRepository.save(t);
                }
                ttp.setTag(tagRepository.findFirstByTagIgnoreCase(tag));
                tags.add(ttp);
            });
            post.setTags(tags);
            postRepository.save(post);
            Post result = postRepository.save(post);
            log.info("IN addPost post: {} added with tags: {} successfully", result, tags);
            return PostResponseFactory.getPost(result);
        }
        return null;
    }

    @Override
    public SearchPersonDto searchPeople(String name, String surname, Integer ageFrom, Integer ageTo, String country,
                                        String city, Integer offset, Integer limit) {
        // превращаю из локалдейт в дату ибо spring jpa не может в query воспринимать LocalDate и принимает только Date
        Date dateTo = Date.from(LocalDate.now().minusYears(ageFrom).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());//плюс день для верхней даты и минус день
        Date dateFrom = Date.from(LocalDate.now().minusYears(ageTo).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());//для нижней т.к. between строгое сравнение.(<>)
        List<Person> result = personRepository.search(name, surname, dateFrom, dateTo, country, city);

        log.info("IN searchPeople by parameters: name {}, surname {}, ageFrom {}, ageTo {}, country {}, city {} found {} result",
                name, surname, ageFrom, ageTo, country, city, result.size());
        return PersonResponseFactory.formatPeopleSearchResultSet(result, offset, limit);
    }

}
