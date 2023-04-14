package data.repository;

import data.entity.PhotoEntity;

import java.util.UUID;

public interface PhotoRepository extends Repository {

    PhotoEntity findById(UUID id);

}
