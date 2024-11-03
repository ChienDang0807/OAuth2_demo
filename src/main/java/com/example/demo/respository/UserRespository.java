package com.example.demo.respository;

import com.example.demo.dto.response.PageResponse;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRespository extends JpaRepository<User,Integer> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    // nếu thuộc tính nativeQuery=true thì phải query trên sql : SELECT * FROM tbl_user
    // nếu false query trên jpa : select * from User u

    //Join
   // @Query(value = "select * from User u inner join Address a on u.id = a.userId where a.city = :city")
   // List<User> getAllUser(String city);
    


    // --Distinct--
    //@Query(value = " select distinct from User u where u.firstName=:firstName anh u.lastName=:lastName")
//    List<User> findByDistinctByFirstNameAndLastName(String firstName,String lastName);

    //--Is,Equals--
//    @Query(value = "select * from User u where u.firstName=:name ")
//    List<User> findByFirstNameIs(String name);
//
//    List<User> findByFirstNameEquals(String name);
//
//    List<User> findByFirstname(String name);

    //--Between --
   // @Query(value = "select * from User u where u.createdAt between ?1 and ?2")
//    List<User> findByCreatedAtBetween(Date startDate, Date endDate);

    //--Less than --
//    @Query(value = "select * from User u where u.age < :age")
//    List<User> findByAgeLessThan(int age);
//    List<User> findByAgeLessThanEquals(int age);
//    List<User> findByAgeGreaterThan(int age);
//    List<User> findByAgeGreaterThanEquals(int age);

    //Before va After
//    @Query(value = "select * from User u where u.createdAt < :date")
//    List<User> findByCreatedAtBefore(Date date);

    //IsNull, Null
//    @Query(value = "select * from User u where u.age is null ")
//    List<User> findByAgeIsNull();

    //NotNull, IsNotNull
//    @Query(value = "select * from User u where u.age is not null")
//    List<User> findByAgeNotNull();

    //Like,Not like
//    @Query(value = "select * from User u where u.firstName like %:lastName%")
//    List<User> findByLastNameLike(String lastName);

    //StartingWith
//    @Query(value = "select * form User u where u.lastName not like :lastName% ")
//    List<User> findByLastNameStartingWith(String lastName);

    //EndingWith
//    @Query(value = "select * form User u where u.lastName not like %:lastName ")
//    List<User> findByLastNameEndingWith(String lastName);

    //Containing
//    @Query(value = "select * from User u where u,lastName like %:name% ")
//    List<User> findByLastNameContaining(String name);

    //Not
//    @Query(value = " select * from User u where u.lastName <> :name")
//    List<User> findByLastNameNot(String name);

    //In, NotIn
//    @Query(value = "select * from User u where u.age in (18,25,36) ")
//    List<User> findByAgeIn(Collection<Integer> ages);

    // True/ False
//    @Query(value = "select * from User u where u.activated = true")
//    List<User> findByActivatedTrue();
//    List<User> findByActivatedFalse();

    //IgnoseCase
//    @Query(value = "select * from User u where LOWER(u.lastName) = LOWER(:name) ")
//    List<User> findByFirstNameIgnoreCase(String name);


    //Order by
  //  List<User> findByFirstNameOrderByCreatedAtDesc(String name);

    // All IgnoreCase
  //  List<User> findByFirstNameAndLastNameAllIgnoreCase(String firstName, String lastName);


}
