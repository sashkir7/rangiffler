package data;

import data.entity.PhotoEntity;
import data.jpa.EmfContext;
import data.jpa.JpaService;
import data.repository.PhotoRepository;

import java.util.UUID;

public class HibernatePhotoRepository extends JpaService implements PhotoRepository {

    public HibernatePhotoRepository() {
        super(EmfContext.INSTANCE.getEmf(DataBase.PHOTO).createEntityManager());
    }

    @Override
    public PhotoEntity findById(UUID id) {
        String hql = "SELECT p FROM PhotoEntity p WHERE p.id=:id";
        return em.createQuery(hql, PhotoEntity.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

}
