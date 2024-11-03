package com.example.demo.respository;

import com.example.demo.dto.response.PageResponse;
import com.example.demo.entity.User;
import com.example.demo.respository.criteria.SearchCriteria;
import com.example.demo.respository.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String LIKE_FORMAT = "%%%s%%";
    private final UserRespository userRespository;

    public SearchRepository(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public PageResponse<?> getAllUsersSortByColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy){
                                                    // cach 1 de lay gia tri tra ve List<UserResponse>
        StringBuilder sqlQuery = new StringBuilder("select new com.example.demo.dto.response.UserResponse(u.id, u.username, u.firstName, u.lastName, u.dob) from User u where 1=1");
       //tim kiem
        if(StringUtils.hasLength(search)){
            sqlQuery.append(" AND (lower(u.firstName) like lower(:firstName)"); // ngon ngu truy van cua jpa
            sqlQuery.append(" OR lower(u.lastName) like lower(:lastName))"); // dua tren thuoc tinh k phai columns
        }
        // sap xep
        if (StringUtils.hasLength(sortBy)){
            // firstName:asc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)?");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String sortField = matcher.group(1);
                String direction = matcher.group(3);
                if (direction == null) {
                    direction = "asc"; // Default ascending order
                }
                sqlQuery.append(String.format(" ORDER BY u.%s %s", sortField, direction));
            }
        }
        //get list users
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        //fill gia tri
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter("firstName",String.format(LIKE_FORMAT, search) ); // ten truong va gia tri cho truong
            selectQuery.setParameter("lastName",String.format(LIKE_FORMAT, search) );
        }
        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);
        List users = selectQuery.getResultList();
        // cach 2 de lay gia tri tra ve List<UserResponse>
        //users.stream().map(userMapper::toUserResponse).collect(Collectors.toList())


        //query so record
        StringBuilder sqlQueryCount = new StringBuilder("SELECT COUNT(*) FROM User u WHERE 1=1");
        if(StringUtils.hasLength(search)){
            sqlQueryCount.append(" and lower(u.firstName) like lower(?1)"); // ngon ngu truy van cua jpa
            sqlQueryCount.append(" or lower(u.lastName) like lower(?2)"); // dua tren thuoc tinh k phai columns
        }

        Query selectCountQuery = entityManager.createQuery(sqlQueryCount.toString());

        if(StringUtils.hasLength(search)){
            selectCountQuery.setParameter(1,String.format(LIKE_FORMAT, search) ); // ten truong va gia tri cho truong
            selectCountQuery.setParameter(2,String.format(LIKE_FORMAT, search) );
        }

        Long totalElements = (Long) selectCountQuery.getSingleResult();
       // System.out.println(totalElements);

        Page<?> page = new PageImpl<Object>(users, PageRequest.of(pageNo,pageSize), totalElements);

        return  PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(page)
                .build();
    }

    public  PageResponse<?> advanceSearchUser (int pageNo, int pageSize, String sortBy, String... search){
        //firstName:T, lastName:T

        List<SearchCriteria> criteriaList = new ArrayList<>();
        //1. lay ra danh sach user
        if (search != null){
            for (String s : search){
                //firstName:value
                Pattern pattern = Pattern.compile("(\\w+?)([:><])(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                  criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3) ));
                }
            }
        }

        //2. lay ra so luong ban ghi
        List<User> users = getUsers(pageNo,pageSize,criteriaList,sortBy);

        Long totalElements = getTotalElements(criteriaList);

        return  PageResponse.builder()
                .pageNo(pageNo) // lay tu offset = vi tri ban ghi trong danh sach
                .pageSize(pageSize)
                .totalPage(totalElements.intValue()) // total elements
                .items(users)
                .build();
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // xu li cac dieu kien tim kiem
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder,predicate,root);

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);

        //sort
        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("desc")){
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                }else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }


       return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }

    private Long getTotalElements(List<SearchCriteria> criteriaList ){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        // xu li cac dieu kien tim kiem
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer searchComsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder,predicate,root);

        criteriaList.forEach(searchComsumer);
        predicate = searchComsumer.getPredicate();
        //
        query.select(criteriaBuilder.count(root));

        query.where(predicate);

        return  entityManager.createQuery(query).getSingleResult();
    }


}
