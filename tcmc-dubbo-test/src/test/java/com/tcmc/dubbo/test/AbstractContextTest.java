package com.tcmc.dubbo.test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
/**
 * dbunit单元测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dubbo-config.xml"})
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractContextTest {
    @Autowired
    private DataSource dataSource;

    private static IDatabaseConnection conn;

    private File tempFile;

    public static final String ROOT_URL = System.getProperty("user.dir") + "/src/test/resources/";

    @Before
    public void setup() throws Exception {
        //get DataBaseSourceConnection
        conn = new DatabaseConnection(DataSourceUtils.getConnection(dataSource));

        //config database as MySql
        DatabaseConfig dbConfig = conn.getConfig();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,  new MySqlDataTypeFactory());

    }

    @After
    public void teardown() throws Exception {
        if (conn != null) {
            conn.close();
        }

    }

    /**
     *
     * @Title: getXmlDataSet
     * @param name
     * @return
     * @throws DataSetException
     * @throws IOException
     */
    protected IDataSet getXmlDataSet(String name) throws DataSetException, IOException {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        return builder.build(new FileInputStream(new File(ROOT_URL + name)));
    }

    /**
     * Get DB DataSet
     *
     * @Title: getDBDataSet
     * @return
     * @throws SQLException
     */
    protected IDataSet getDBDataSet() throws SQLException {
        return conn.createDataSet();
    }

    /**
     * Get Query DataSet
     *
     * @Title: getQueryDataSet
     * @return
     * @throws SQLException
     */
    protected QueryDataSet getQueryDataSet() throws SQLException {
        return new QueryDataSet(conn);
    }

    /**
     * Get Excel DataSet
     *
     * @Title: getXlsDataSet
     * @param name
     * @return
     * @throws SQLException
     * @throws DataSetException
     * @throws IOException
     */
    protected XlsDataSet getXlsDataSet(String name) throws SQLException, DataSetException,
            IOException {
        InputStream is = new FileInputStream(new File(ROOT_URL + name));

        return new XlsDataSet(is);
    }

    /**
     * backup the whole DB
     *
     * @Title: backupAll
     * @throws Exception
     */
    protected void backupAll() throws Exception {
        // create DataSet from database.
        IDataSet ds = conn.createDataSet();

        // create temp file
        tempFile = File.createTempFile("temp", "xml");

        // write the content of database to temp file
        FlatXmlDataSet.write(ds, new FileWriter(tempFile), "UTF-8");
    }

    /**
     * back specified DB table
     *
     * @Title: backupCustom
     * @param tableName
     * @throws Exception
     */
    protected void backupCustom(String... tableName) throws Exception {
        // back up specific files
        QueryDataSet qds = new QueryDataSet(conn);
        for (String str : tableName) {

            qds.addTable(str);
        }
        tempFile = File.createTempFile("temp", "xml");
        FlatXmlDataSet.write(qds, new FileWriter(tempFile), "UTF-8");

    }

    /**
     * rollback database
     *
     * @Title: rollback
     * @throws Exception
     */
    protected void rollback() throws Exception {

        // get the temp file
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet ds =builder.build(new FileInputStream(tempFile));

        // recover database
        DatabaseOperation.CLEAN_INSERT.execute(conn, ds);
    }


    /**
     * Clear data of table
     *
     * @param tableName
     * @throws Exception
     */
    protected void clearTable(String tableName) throws Exception {
        DefaultDataSet dataset = new DefaultDataSet();
        dataset.addTable(new DefaultTable(tableName));
        DatabaseOperation.DELETE_ALL.execute(conn, dataset);
    }

    /**
     * verify Table is Empty
     *
     * @param tableName
     * @throws DataSetException
     * @throws SQLException
     */
    protected void verifyTableEmpty(String tableName) throws DataSetException, SQLException {
        Assert.assertEquals(0, conn.createDataSet().getTable(tableName).getRowCount());
    }

    /**
     * verify Table is not Empty
     *
     * @Title: verifyTableNotEmpty
     * @param tableName
     * @throws DataSetException
     * @throws SQLException
     */
    protected void verifyTableNotEmpty(String tableName) throws DataSetException, SQLException {
        Assert.assertNotEquals(0, conn.createDataSet().getTable(tableName).getRowCount());
    }

    /**
     *
     * @Title: createReplacementDataSet
     * @param dataSet
     * @return
     */
    protected ReplacementDataSet createReplacementDataSet(IDataSet dataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);

        // Configure the replacement dataset to replace '[NULL]' strings with null.
        replacementDataSet.addReplacementObject("[null]", null);

        return replacementDataSet;
    }
}
