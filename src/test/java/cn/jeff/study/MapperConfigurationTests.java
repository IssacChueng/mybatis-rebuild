package cn.jeff.study;

import cn.jeff.study.dao.BeanMapper;
import cn.jeff.study.dao.ObjectMapper;
import cn.jeff.study.model.Bean;
import cn.jeff.study.model.Condition;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class MapperConfigurationTests {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void testConfiguration() throws IOException, SQLException {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.setCacheEnabled(false);
            DataSource dataSource = configuration.getEnvironment().getDataSource();
            runScript(dataSource, "Init.sql");
        }
    }


    @After
    public void release() throws IOException, SQLException {
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        runScript(dataSource, "Release.sql");
    }

    @Test
    public void testMappers() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BeanMapper mapper = sqlSession.getMapper(BeanMapper.class);
        Condition condition = new Condition();
        condition.setName("2");
        /*Bean bean = mapper.selectOneByCondition(condition);
        System.out.println(bean);*/
        MapperConfiguration.Builder builder = new MapperConfiguration.Builder(sqlSessionFactory.getConfiguration());
        FileReader mapperXml = new FileReader(Resources.getResourceAsFile("mapper/BeanAfterMapper.xml"));
        XPathParser parser = new XPathParser(mapperXml);
        XNode mapperNode = parser.evalNode("/mapper");

        mapperXml = new FileReader(Resources.getResourceAsFile("mapper/ObjectAfterMapper.xml"));
        parser = new XPathParser(mapperXml);
        XNode mapperNode2 = parser.evalNode("/mapper");

        builder.addResource(BeanMapper.class.getName().replace(".", "/") + ".xml", mapperNode)
                .addResource(ObjectMapper.class.getName().replace(".", "/") + ".xml", mapperNode2)
                .build().parse();

        Bean bean = mapper.selectOneByCondition(condition);
        System.out.println(bean);

    }


    private void runScript(DataSource dataSource, String sql) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            runScript(scriptRunner, sql);
        }

    }


    private void runScript(ScriptRunner scriptRunner, String resource) throws IOException {
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            scriptRunner.runScript(reader);
        }
    }

}
