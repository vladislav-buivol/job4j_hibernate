package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.math.BigDecimal;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Candidate candidate1 = Candidate.of("Can1", "No", new BigDecimal(10000));
            Candidate candidate2 = Candidate.of("Can2", "Senior", new BigDecimal(2000));
            Candidate candidate3 = Candidate.of("Can3", "5 years", new BigDecimal(30000));

            session.save(candidate1);
            session.save(candidate2);
            session.save(candidate3);

            select(session, candidate3);
            update(session, candidate3);
            select(session, candidate3);
            delete(session);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void update(Session session, Candidate candidate3) {
        candidate3.setName("Updated");
        session.createQuery("update Candidate c set "
                + "c.name=:cN, "
                + "c.salary=:cS "
                + "where id>:cId")
                .setParameter("cN", "Updated")
                .setParameter("cId", 2)
                .setParameter("cS", new BigDecimal(70000)).executeUpdate();
    }

    private static void delete(Session session) {
        session.createQuery("delete from Candidate where id>:cId")
                .setParameter("cId", 2)
                .executeUpdate();
    }

    private static void select(Session session, Candidate candidate3) {
        Query selectQ = session.createQuery("from Candidate c "
                + "where c.id >: cId "
                + " and c.name =:cName")
                .setParameter("cId", 0)
                .setParameter("cName", candidate3.getName());
        System.out.println(selectQ.getResultList());

    }
}