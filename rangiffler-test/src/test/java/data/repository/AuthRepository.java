package data.repository;

public interface AuthRepository extends Repository {

    void removeByUsername(String username);

}
