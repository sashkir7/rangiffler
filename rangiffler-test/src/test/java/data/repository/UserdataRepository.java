package data.repository;

import data.entity.UserEntity;

public interface UserdataRepository extends Repository {

    UserEntity findByUsername(String username);

}
