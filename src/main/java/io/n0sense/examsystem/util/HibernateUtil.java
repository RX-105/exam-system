package io.n0sense.examsystem.util;

import io.n0sense.examsystem.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.Properties;

/**
 * Code from: <a href="https://gist.github.com/yusufcakmak/215ede715bab0e1d6489">yusufcakmak</a>
 */
public class HibernateUtil {
    // FIXME:静态变量不能使用@Value、@Autowired等方式获取当前上下文中的配置信息
    //XML based configuration
    private static SessionFactory sessionFactory;
    //Annotation based configuration
    private static SessionFactory sessionAnnotationFactory;
    //Property based configuration
    private static SessionFactory sessionJavaConfigFactory;
    public static String jdbcUrl;
    @Value("${spring.datasource.driver-class-name}")
    private static String driverClassName;
    @Value("${spring.datasource.username}")
    private static String username;
    @Value("${spring.datasource.password}")
    private static String password;

    static {
        InputStream inputStream;
        Properties properties;
        try{
            properties = new Properties();
            inputStream = HibernateUtil.class.getResourceAsStream("application.properties");
            properties.load(inputStream);

            jdbcUrl = properties.getProperty("spring.datasource.url");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            System.out.println("Hibernate Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory buildSessionAnnotationFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate-annotation.cfg.xml");
            System.out.println("Hibernate Annotation Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate Annotation serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory buildSessionJavaConfigFactory() {
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
            configuration.addAnnotatedClass(EnrollmentInfo.class);
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
            System.out.println("Hibernate Java Config serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }

    public static SessionFactory getSessionAnnotationFactory() {
        if (sessionAnnotationFactory == null)
            sessionAnnotationFactory = buildSessionAnnotationFactory();
        return sessionAnnotationFactory;
    }

    public static SessionFactory getSessionJavaConfigFactory() {
        if (sessionJavaConfigFactory == null)
            sessionJavaConfigFactory = buildSessionJavaConfigFactory();
        return sessionJavaConfigFactory;
    }
}
