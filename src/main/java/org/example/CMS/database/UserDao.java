package org.example.CMS.database;

import org.example.CMS.dto.UserEntity;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.Optional;

public interface UserDao {

    @SqlQuery("SELECT * FROM auth_user WHERE username = :username")
    @RegisterBeanMapper(UserEntity.class)
    Optional<UserEntity> findByUsername(String userName);

    @SqlQuery("SELECT * FROM auth_user WHERE username = :username AND password = :password")
    @RegisterBeanMapper(UserEntity.class)
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);
}
