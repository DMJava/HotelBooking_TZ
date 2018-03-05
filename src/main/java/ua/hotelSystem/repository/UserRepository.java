package ua.hotelSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.hotelSystem.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
}
