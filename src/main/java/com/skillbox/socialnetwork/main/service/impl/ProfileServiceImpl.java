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
import com.skillbox.socialnetwork.main.model.*;
import com.skillbox.socialnetwork.main.model.enumerated.FriendshipCode;
import com.skillbox.socialnetwork.main.repository.*;
import com.skillbox.socialnetwork.main.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {


    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final FriendshipStatusRepo friendshipStatusRepo;
    private final FriendshipRepository friendshipRepository;
    private final P2TRepository p2TRepository;


    @Autowired
    public ProfileServiceImpl(PersonRepository personRepository, PostRepository postRepository, TagRepository tagRepository, FriendshipStatusRepo friendshipStatusRepo, FriendshipRepository friendshipRepository, P2TRepository p2TRepository) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.friendshipStatusRepo = friendshipStatusRepo;
        this.friendshipRepository = friendshipRepository;
        this.p2TRepository = p2TRepository;
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
    public AbstractResponse getUserById(int id, Person authorizedUser) {
        Optional<Person> profile = personRepository.findById(id);
        AbstractResponse result = null;
        if(profile.isPresent()){
            //эта строчка проверяет, заблокировал ли текущий авторизованный юзер юзера, которого ищем по id
            profile.get().setIsBlocked(friendshipRepository.isBlocked(authorizedUser, profile.get()));
            result = PersonResponseFactory.getPerson(profile.get());
        }
        log.info("IN getUserById user with id: {}"+(result!=null ? "  found." : "NOT FOUND"), id);
        return result;
    }

    @Override
    public WallDto getWallPosts(int id, int offset, int limit) {
        Optional<Person> person = personRepository.findById(id);
        WallDto result = null;
        if(person.isPresent()) {
            List<Post> posts = person.get().getPosts();
            posts.sort(Comparator.comparing(Post::getTime).reversed());//сортирую по дате чтобы на стенке выводились сначала новые
            result = WallResponseFactory.getWall(posts, offset, limit);
        }
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
            //collections initialized to avoid nullPointer in PostResponseFactoryMethods
            post.setLikes(new ArrayList<>());
            post.setComments(new ArrayList<>());
            post.setFiles(new ArrayList<>());
            post.setBlockHistories(new ArrayList<>());

            Post savedPost = postRepository.save(post);
            //tags
            List<Post2tag> tags = new ArrayList<>();
            request.getTags().forEach(tag -> {
                Post2tag ttp = new Post2tag();
                ttp.setPost(savedPost);
                if(!tagRepository.existsByTagIgnoreCase(tag)){
                    Tag t = new Tag();
                    t.setTag(tag);
                    tagRepository.save(t);
                }
                ttp.setTag(tagRepository.findFirstByTagIgnoreCase(tag));
                tags.add(ttp);
            });
            savedPost.setTags(p2TRepository.saveAll(tags));
            Post result = postRepository.save(savedPost);
            log.info("IN addPost post: {} added with tags: {} successfully", result, tags);
            return PostResponseFactory.getPost(result);
        }
        return null;
    }

    @Override
    public SearchPersonDto searchPeople(String name, String surname, Integer ageFrom, Integer ageTo, String country,
                                        String city, Integer offset, Integer limit, Person authorizedUser) {
        // превращаю из локалдейт в дату ибо spring jpa не может в query воспринимать LocalDate и принимает только Date
        Date dateTo = Date.from(LocalDate.now().minusYears(ageFrom).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());//плюс день для верхней даты и минус день
        Date dateFrom = Date.from(LocalDate.now().minusYears(ageTo).minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());//для нижней т.к. between строгое сравнение.(<>)
        List<Person> result = personRepository.search(name, surname, dateFrom, dateTo, country, city);

        log.info("IN searchPeople by parameters: name {}, surname {}, ageFrom {}, ageTo {}, country {}, city {} found {} result",
                name, surname, ageFrom, ageTo, country, city, result.size());
        return PersonResponseFactory.formatPeopleSearchResultSet(result, offset, limit);
    }

    @Override
    public BaseResponseDto blockUser(int idOfABlockedUser, Person authorizedUser) {
        Optional<Person> profileToBlock = personRepository.findById(idOfABlockedUser);
        if(profileToBlock.isPresent()){
            FriendshipStatus status = new FriendshipStatus();
            status.setCode(FriendshipCode.BLOCKED);
            status.setName("name");//Для чего поле ??
            status.setTime(new Date());

            Friendship relation = new Friendship();
            relation.setSrcPerson(authorizedUser);
            relation.setDstPerson(profileToBlock.get());
            relation.setStatus(friendshipStatusRepo.save(status));

            friendshipRepository.save(relation);
            log.info("IN blockUser user: {} blocked user {}", authorizedUser.getEmail(), profileToBlock.get().getEmail());
            return new BaseResponseDto();
        }
        log.info("IN blockUser user with id: {} not found", idOfABlockedUser);
        return null;
    }

    @Override
    public BaseResponseDto unblockUser(int id, Person authorizedUser) {
        Optional<Person> profileToBlock = personRepository.findById(id);
        if(profileToBlock.isPresent()){
            friendshipRepository.delete(friendshipRepository.findRelation(authorizedUser, profileToBlock.get(), FriendshipCode.BLOCKED));
            log.info("IN unblockUser user: {} unblocked user {}", authorizedUser.getEmail(), profileToBlock.get().getEmail());
            return new BaseResponseDto();
        }
        log.info("IN unblockUser user with id: {} not found", id);
        return null;
    }

}
