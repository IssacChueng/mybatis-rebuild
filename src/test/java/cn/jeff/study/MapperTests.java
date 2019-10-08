package cn.jeff.study;

import cn.jeff.study.dao.ObjectMapper;
import org.apache.commons.io.FileUtils;
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
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author swzhang
 * @date 2019/10/08
 */
public class MapperTests {
    private SqlSessionFactory sqlSessionFactory;
    @Before
    public void testConfiguration() throws IOException, SQLException {
        try(Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
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
        String mapperXml = FileUtils.readFileToString(new File("/Users/jeff/git/mybatis-dynamic-statement/src/test/resources/mapper/ObjectAfterMapper.xml"), "utf-8");
        mapperChanger.changeMapperByFile(mapperXml, sqlSessionFactory.getConfiguration(), ObjectMapper.class);
        ObjectMapper mapper = sqlSession.getMapper(ObjectMapper.class);
        Assert.assertNotNull(mapper);
        System.out.println(mapper.selectOne());
    }

    private void runScript(DataSource dataSource) throws SQLException, IOException {
        try(Connection connection = dataSource.getConnection()) {
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
