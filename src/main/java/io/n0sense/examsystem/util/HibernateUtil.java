package io.n0sense.examsystem.util;

import io.n0sense.examsystem.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Code from: <a href="https://gist.github.com/yusufcakmak/215ede715bab0e1d6489">yusufcakmak</a>
 */
@Component
@lombok.extern.java.Log
public class HibernateUtil {
    //XML based configuration
    private SessionFactory sessionFactory;
    //Annotation based configuration
    private SessionFactory sessionAnnotationFactory;
    //Property based configuration
    private SessionFactory sessionJavaConfigFactory;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    private SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            log.info("Hibernate Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            log.info("Hibernate serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.warning("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private SessionFactory buildSessionAnnotationFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate-annotation.cfg.xml");
            log.info("Hibernate Annotation Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            log.info("Hibernate Annotation serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            log.warning("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private SessionFactory buildSessionJavaConfigFactory() {
        try {
            Configuration configuration = new Configuration();

            //Create Properties, can be read from property files too
            Properties props = new Properties();
            props.put("hibernate.connection.driver_class", driverClassName);
            props.put("hibernate.connection.url", jdbcUrl);
            props.put("hibernate.connection.username", username);
            props.put("hibernate.connection.password", password);
            props.put("hibernate.current_session_context_class", "thread");

            configuration.setProperties(props);

            // we can set mapping file or class with annotation
            // addClass(Employee1.class) will look for resource
            // com/journaldev/hibernate/model/Employee1.hbm.xml (not good)
            configuration.addAnnotatedClass(Admin.class);
            configuration.addAnnotatedClass(Config.class);
            configuration.addAnnotatedClass(Exam.class);
            configuration.addAnnotatedClass(Log.class);
            configuration.addAnnotatedClass(Major.class);
            configuration.addAnnotatedClass(Registry.class);
            configuration.addAnnotatedClass(School.class);
            configuration.addAnnotatedClass(Score.class);
            configuration.addAnnotatedClass(Stage.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Visits.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            log.info("Hibernate Java Config serviceRegistry created");

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            log.warning("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }

    public SessionFactory getSessionAnnotationFactory() {
        if (sessionAnnotationFactory == null)
            sessionAnnotationFactory = buildSessionAnnotationFactory();
        return sessionAnnotationFactory;
    }

    public SessionFactory getSessionJavaConfigFactory() {
        if (sessionJavaConfigFactory == null)
            sessionJavaConfigFactory = buildSessionJavaConfigFactory();
        return sessionJavaConfigFactory;
    }
}
