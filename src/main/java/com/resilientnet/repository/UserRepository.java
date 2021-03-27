package com.resilientnet.repository;
import com.resilientnet.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends  MongoRepository<User, String>{
    User findBySubject(String subject);
}
