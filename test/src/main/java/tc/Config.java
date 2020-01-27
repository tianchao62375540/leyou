package tc;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


/**
 * @Auther: tianchao
 * @Date: 2020/1/7 21:59
 * @Description:
 */
@Configuration
public class Config {



    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://120.78.129.112/lubansql");
        dataSource.setUsername("root");
        dataSource.setPassword("Tianchao!!2019");
        return dataSource;
    }

    public SqlSessionFactory sqlSessionFactory(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return null;
    }
}
