package data;

import data.entity.UserEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import data.repository.UserdataRepository;

public class HibernateUserdataRepository extends JpaService implements UserdataRepository {

    public HibernateUserdataRepository() {
        super(EmfContext.INSTANCE.getEmf(DataBase.USERDATA).createEntityManager());
    }

    @Override
    public UserEntity findByUsername(String username) {
        String hql = "SELECT u FROM UserEntity u WHERE u.username=:username";
        return em.createQuery(hql, UserEntity.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst().orElse(null);
    }

}
