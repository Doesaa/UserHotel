package bt.edu.gcit.usermicroservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import bt.edu.gcit.usermicroservice.entity.Room;
import java.util.List;

@Repository
public class RoomDAOImpl implements RoomDAO {
    private final EntityManager entityManager;

    public RoomDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Room findById(Long id) {
        return entityManager.find(Room.class, id);
    }

    @Override
    public List<Room> findAll() {
        TypedQuery<Room> query = entityManager.createQuery(
            "SELECT r FROM Room r", Room.class);
        return query.getResultList();
    }

    @Override
    public List<Room> findByHotelId(Long hotelId) {
        TypedQuery<Room> query = entityManager.createQuery(
            "SELECT r FROM Room r WHERE r.hotel.id = :hotelId", Room.class);
        query.setParameter("hotelId", hotelId);
        return query.getResultList();
    }

    @Override
    public List<Room> findAvailableRooms(Long hotelId) {
        TypedQuery<Room> query = entityManager.createQuery(
            "SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.available = true", Room.class);
        query.setParameter("hotelId", hotelId);
        return query.getResultList();
    }

    @Override
    public void save(Room room) {
        entityManager.persist(room);
    }

    @Override
    public void update(Room room) {
        entityManager.merge(room);
    }

    @Override
    public void delete(Long id) {
        Room room = entityManager.find(Room.class, id);
        if (room != null) {
            entityManager.remove(room);
        }
    }
}
