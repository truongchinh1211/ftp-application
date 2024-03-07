/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.HibernateConfig;
import java.util.List;
import model.File;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author lamanhhai
 */
public class FileDao {
    
    private ShareFilesDao shareFilesDao = new ShareFilesDao();

    public File getFileById(int id) {
        Transaction transaction = null;
        File file = null;
        Session session = null;
        try {
            session = HibernateConfig.getSessionFactory().openSession();

            // start the transaction
            transaction = session.beginTransaction();

            // get user object by id
            file = session.get(File.class, id);

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return file;
    }

    public File getFileByPath(String path) {
        Transaction transaction = null;
        File file = null;
        Session session = null;
        try {
            session = HibernateConfig.getSessionFactory().openSession();

            // start the transaction
            transaction = session.beginTransaction();

            // get file by path
            String hql = "FROM File WHERE path = :path";            
            Query<File> query = session.createQuery(hql, File.class);
            query.setParameter("path", path);
            file = query.getSingleResult();
            
            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return file;
    }

    @SuppressWarnings("unchecked")
    public List<File> getAllFiles() {
        Transaction transaction = null;
        List<File> files = null;
        Session session = null;
        try {
            session = HibernateConfig.getSessionFactory().openSession();

            // start the transaction
            transaction = session.beginTransaction();

            // get all users
            files = session.createQuery("from File").list();

            // commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return files;
    }

    public boolean save(File file) {
        Transaction transaction = null;
        Session session = null;
        boolean isInsert = false;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            // start the transaction
            transaction = session.beginTransaction();

            // save user object
            session.save(file);

            // commit the transaction d
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

    public boolean update(File file) {
        Transaction transaction = null;
        Session session = null;
        boolean isUpdate = false;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            // start the transaction
            transaction = session.beginTransaction();

            // save or update user object
            session.saveOrUpdate(file);

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
        File file = null;
        Session session = null;
        boolean isDelete = false;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            // start the transaction
            transaction = session.beginTransaction();

            file = session.get(File.class, id);

            // save or update user object
            session.delete(file);

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
}
