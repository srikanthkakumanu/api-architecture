package com.apidemo.rest.repos;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import com.apidemo.rest.model.ToDo;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * ToDo Data (DB) Repository aka DAO implementation
 */
@Repository
public class ToDoJdbcRepository implements CommonRepository<ToDo> {

    private final NamedParameterJdbcTemplate template;

    public ToDoJdbcRepository(NamedParameterJdbcTemplate template) { this.template = template; }

    private RowMapper<ToDo> todoRowMapper = (ResultSet rs, int rowNum) -> {
        ToDo todo = new ToDo();
        todo.setId(rs.getString("id"));
        todo.setDescription(rs.getString("description"));
        todo.setCreated(rs.getTimestamp("created").toLocalDateTime());
        todo.setModified(rs.getTimestamp("modified").toLocalDateTime());
        todo.setCompleted(rs.getBoolean("completed"));
        return todo;
    };

    /**
     * Saves ToDo
     * @param todo todo model
     */
    @Override
    public ToDo save(ToDo todo) {
        ToDo result = findById(todo.getId());
        if(result != null) {
            result.setDescription(todo.getDescription());
            result.setCompleted(todo.isCompleted());
            result.setModified(LocalDateTime.now());
            return upsert(result, ToDoQueryHelper.SQL_UPDATE);
        }
        return upsert(todo, ToDoQueryHelper.SQL_INSERT);
    }


    /**
     * Utility method for create or update a todo
     * @param todo
     * @param sql
     * @return
     */
    private ToDo upsert(ToDo todo, String sql) {
        Map<String, Object> params = new HashMap<>();

        params.put("id", todo.getId());
        params.put("description",todo.getDescription());
        params.put("created",java.sql.Timestamp.valueOf(todo.getCreated()));
        params.put("modified",java.sql.Timestamp.valueOf(todo.getModified()));
        params.put("completed",todo.isCompleted());
        
        this.template.update(sql, params);
        
        return findById(todo.getId());
    }

    /**
     * Saves collection of ToDo's
     * @param todos collection of todos
     */
    @Override
    public Iterable<ToDo> save(Collection<ToDo> models) {
        models.forEach(this::save); // saves each todo of models collection
        return findAll();
    }

    /**
     * Deletes a ToDo
     * @param todo a todo model
     */
    @Override
    public void delete(ToDo todo) {
        Map<String, String> params = Collections.singletonMap("id", todo.getId());
        this.template.update(ToDoQueryHelper.SQL_DELETE, params);
    }

    /**
     * Finds a Todo by Id and returns
     * @param id An id of ToDo
     */
    @Override
    public ToDo findById(String id) {
        Map<String, String> params = Collections.singletonMap("id", id);
        try {
            return this.template.queryForObject(ToDoQueryHelper.SQL_QUERY_FIND_BY_ID, params, todoRowMapper);
        }
        catch(EmptyResultDataAccessException erde) { return null; }
    }

    /**
     * Finds and returns all ToDo's after comparision
     */
    @Override
    public Iterable<ToDo> findAll() {
        return this.template.query(ToDoQueryHelper.SQL_QUERY_FIND_ALL, todoRowMapper);
    }

    // util class for SQL queries
    private static class ToDoQueryHelper {
        public static final String SQL_INSERT = "insert into todo (id, description, created, modified, completed)" +
                                                " values (:id, :description, :created, :modified, :completed)";

        public static final String SQL_QUERY_FIND_ALL = "select id, description, created, modified, completed from todo";

        public static final String SQL_QUERY_FIND_BY_ID = SQL_QUERY_FIND_ALL + " where id = :id";

        public static final String SQL_UPDATE = "update todo set description = :description, modified = :modified, " +
                                                "completed = :completed where id = :id";
        
        public static final String SQL_DELETE = "delete from todo where id = :id";
    }
}
