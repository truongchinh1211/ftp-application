/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.HibernateConfig;
import java.util.List;
import model.Directory;
import model.File;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author lamanhhai
 */
public class DirectoryDao {

    public Directory getDirectoryById(int id) {
        Transaction transaction = null;
        Directory directory = null;
        Session session = null;
        try {
            session = HibernateConfig.getSessionFactory().openSession();

            // start the transaction
            transaction = session.beginTransaction();

            // get user object by id
            directory = session.get(Directory.class, id);

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return directory;
    }

    @SuppressWarnings("unchecked")
    public List<Directory> getAllDirectories() {
        Transaction transaction = null;
        List<Directory> directories = null;
        Session session = null;
        try {
            session = HibernateConfig.getSessionFactory().openSession();

            // start the transaction
            transaction = session.beginTransaction();

            // get all users
            directories = session.createQuery("from Directory").list();

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return directories;
    }

    public boolean save(Directory directory) {
        Transaction transaction = null;
        Session session = null;
        boolean isInsert = false;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            // start the transaction
            transaction = session.beginTransaction();

            // save user object
            session.save(directory);

            // commit the transaction
            transaction.commit();

            isInsert = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return isInsert;
    }

    public boolean update(Directory directory) {
        Transaction transaction = null;
        Session session = null;
        boolean isUpdate = false;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            // start the transaction
            transaction = session.beginTransaction();

            // save or update user object
            session.saveOrUpdate(directory);

            // commit the transaction
            transaction.commit();

            isUpdate = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return isUpdate;
    }

    public boolean remove(int id) {
        Transaction transaction = null;
        Directory directory = null;
        Session session = null;
        boolean isDelete = false;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            // start the transaction
            transaction = session.beginTransaction();

            directory = session.get(Directory.class, id);

            // save or update user object
            session.delete(directory);

            // commit the transaction
            transaction.commit();

            isDelete = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return isDelete;
    }

    public Directory getDirectoryByPath(String path) {
        Transaction transaction = null;
        Directory directory = null;
        Session session = null;
        try {
            session = HibernateConfig.getSessionFactory().openSession();

            // start the transaction
            transaction = session.beginTransaction();

            // get file by path
            String hql = "FROM Directory WHERE path = :path";
            Query<Directory> query = session.createQuery(hql, Directory.class);
            query.setParameter("path", path);
            directory = query.getSingleResult();

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return directory;
    }
}
