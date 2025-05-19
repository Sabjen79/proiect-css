package fii.css.database.persistence.repositories;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AbstractRepositoryTest {

    @Mock
    private Database mockDatabase;
    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    private TestRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // setup database mock chain
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // mock initial data load
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getObject("id")).thenReturn("1", "2");
        when(mockResultSet.getObject("name")).thenReturn("Test1", "Test2");

        // use MockedStatic for Database.getInstance()
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            repository = new TestRepository();
        }
    }

    @Test
    void getById_ShouldReturnEntity_WhenExists() {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            TestEntity entity = repository.getById("1");
            assertNotNull(entity);
            assertEquals("1", entity.getId());
            assertEquals("Test1", entity.getName());
        }
    }

    @Test
    void getById_ShouldReturnNull_WhenNotExists() {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            TestEntity entity = repository.getById("nonexistent");
            assertNull(entity);
        }
    }

    @Test
    void getByName_ShouldReturnEntity_WhenExists() {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            TestEntity entity = repository.getByName("Test1");
            assertNotNull(entity);
            assertEquals("1", entity.getId());
            assertEquals("Test1", entity.getName());
        }
    }

    @Test
    void getAll_ShouldReturnCopyOfEntities() {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            List<TestEntity> entities = repository.getAll();
            assertEquals(2, entities.size());
            assertThrows(UnsupportedOperationException.class, () -> entities.add(new TestEntity()));
        }
    }

    @Test
    void persist_ShouldAddNewEntity() throws Exception {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockStatement.execute(anyString())).thenReturn(true);
            TestEntity newEntity = repository.newEntity();
            newEntity.setName("NewTest");

            repository.persist(newEntity);

            verify(mockStatement).execute(contains("INSERT INTO test_table"));
            assertTrue(repository.getAll().stream().anyMatch(e -> e.getName().equals("NewTest")));
        }
    }

    @Test
    void merge_ShouldUpdateExistingEntity() throws Exception {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockStatement.execute(anyString())).thenReturn(true);
            TestEntity entity = repository.getById("1");
            entity.setName("UpdatedName");

            repository.merge(entity);

            verify(mockStatement).execute(contains("UPDATE test_table"));
            assertEquals("UpdatedName", repository.getById("1").getName());
        }
    }

    @Test
    void delete_ShouldRemoveEntity() throws Exception {
        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockStatement.execute(anyString())).thenReturn(true);
            TestEntity entity = repository.getById("1");

            repository.delete(entity);

            verify(mockStatement).execute(contains("DELETE FROM test_table"));
            assertNull(repository.getById("1"));
        }
    }

    @Table("test_table")
    public static class TestEntity extends DatabaseEntity {
        @Id
        @Column("id")
        protected String id;

        @Column("name")
        protected String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public DatabaseEntity clone() {
            TestEntity clone = new TestEntity();
            clone.id = this.id;
            clone.name = this.name;
            return clone;
        }
    }


    private static class TestRepository extends AbstractRepository<TestEntity> {
        public TestRepository() {
            super(TestEntity.class);
        }
    }
}
