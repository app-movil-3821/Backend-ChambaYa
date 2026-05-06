package com.chambaya.backend.iam.infrastructure.persistence.repositories;

import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.repositories.UserRepository;
import com.chambaya.backend.iam.infrastructure.persistence.documents.UserDocument;
import com.chambaya.backend.iam.infrastructure.persistence.mappers.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoUserRepository implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;

    public MongoUserRepository(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public User save(User user){
        UserDocument document = UserMapper.toDocument(user);
        UserDocument savedDocument = springDataUserRepository.save(document);
        return UserMapper.toDomain(savedDocument);
    }

    @Override
    public Optional<User> findById(String id){
        return springDataUserRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return springDataUserRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll(){
        return springDataUserRepository.findAll()
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email){
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(String id){
        springDataUserRepository.deleteById(id);
    }

}
