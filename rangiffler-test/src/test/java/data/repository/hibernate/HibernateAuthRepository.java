package data.repository.hibernate;

import data.DataBase;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import data.repository.AuthRepository;

public class HibernateAuthRepository extends JpaService implements AuthRepository {

    public HibernateAuthRepository() {
        super(EmfContext.INSTANCE.getEmf(DataBase.AUTH).createEntityManager());
    }

    @Override
    public void removeByUsername(String username) {
        String hql = "DELETE AuthEntity WHERE username=:username";
        tx(em -> em.createQuery(hql)
                .setParameter("username", username)
                .executeUpdate()
        );
    }

}
