package cn.jeff.study;

import cn.jeff.study.dao.ObjectMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

/**
 * @author swzhang
 * @date 2019/10/08
 */
public class MapperTests {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void testConfiguration() throws IOException, SQLException {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            DataSource dataSource = configuration.getEnvironment().getDataSource();
            runScript(dataSource);

        }
    }

    @Test
    public void testSelectBefore() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectOne());
    }

    @Test
    public void testSelectAfter() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();

        MapperChanger mapperChanger = new MapperChanger();
        FileReader mapperXml = new FileReader(new File("/Users/jeff/git/mybatis-dynamic-statement/src/test/resources/mapper/ObjectAfterMapper.xml"));
        mapperChanger.changeMapperByFile(mapperXml, sqlSessionFactory.getConfiguration(), ObjectMapper.class);
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectOne());
    }

    @Test
    public void testSelectInclude() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MapperChanger mapperChanger = new MapperChanger();
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectWithInclude());
        System.out.println(Instant.now());
        FileReader mapperXml = new FileReader(Resources.getResourceAsFile("mapper/ObjectAfterMapper.xml"));
        System.out.println(Instant.now());
        mapperChanger.changeMapperByFile(mapperXml, sqlSessionFactory.getConfiguration(), ObjectMapper.class);
        mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectWithInclude());
    }

    private void runScript(DataSource dataSource) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            runScript(scriptRunner, "Init.sql");
        }

    }

    private void runScript(ScriptRunner scriptRunner, String resource) throws IOException {
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            scriptRunner.runScript(reader);
        }
    }
}
