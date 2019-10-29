package cn.jeff.study;

import cn.jeff.study.dao.ObjectMapper;
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
            runScript(dataSource, "Init.sql");

        }
    }

    @After
    public void release() throws IOException, SQLException {
        System.out.println("release tables");
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        runScript(dataSource, "Release.sql");
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

        MapperReplacer mapperReplacer = new MapperReplacer();
        FileReader mapperXml = new FileReader(new File("/Users/jeff/git/mybatis-dynamic-statement/src/test/resources/mapper/ObjectAfterMapper.xml"));
        XPathParser parser = new XPathParser(mapperXml);
        XNode mapperNode = parser.evalNode("/mapper");
        mapperReplacer.replaceMapperByFile(mapperNode, sqlSessionFactory.getConfiguration(), ObjectMapper.class.getName().replace(".", "/") + ".xml", false);
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectOne());
    }

    @Test
    public void testSelectInclude() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MapperReplacer mapperReplacer = new MapperReplacer();
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectWithInclude());
        System.out.println(Instant.now());
        FileReader mapperXml = new FileReader(Resources.getResourceAsFile("mapper/ObjectAfterMapper.xml"));
        System.out.println(Instant.now());
        XPathParser parser = new XPathParser(mapperXml);
        XNode mapperNode = parser.evalNode("/mapper");
        mapperReplacer.replaceMapperByFile(mapperNode, sqlSessionFactory.getConfiguration(), ObjectMapper.class.getName().replace(".", "/") + ".xml", false);
//        mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectWithInclude());
    }

    @Test
    public void testSelectObj() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MapperReplacer mapperReplacer = new MapperReplacer();
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        FileReader mapperXml = new FileReader(Resources.getResourceAsFile("mapper/ObjectAfterMapper.xml"));
        XPathParser parser = new XPathParser(mapperXml);
        XNode mapperNode = parser.evalNode("/mapper");
        mapperReplacer.replaceMapperByFile(mapperNode, sqlSessionFactory.getConfiguration(), ObjectMapper.class.getName().replace(".", "/") + ".xml", false);
//        mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectObj());
    }

    @Test
    public void testSelectCondition() throws IOException {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        MapperReplacer mapperReplacer = new MapperReplacer();
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        Condition condition = new Condition();
        condition.setId("2");
        condition.setName("2");
        System.out.println(mapper.selectObjCondition(condition));
        FileReader mapperXml = new FileReader(Resources.getResourceAsFile("mapper/ObjectAfterMapper.xml"));
        XPathParser parser = new XPathParser(mapperXml);
        XNode mapperNode = parser.evalNode("/mapper");
        mapperReplacer.replaceMapperByFile(mapperNode, sqlSessionFactory.getConfiguration(), ObjectMapper.class.getName().replace(".", "/") + ".xml", true);
//        mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        condition.setId("2");
        condition.setName("2");
        System.out.println(mapper.selectObjCondition(condition));
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
