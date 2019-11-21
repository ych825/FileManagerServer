package com.ym.dao.impl;

import com.ym.dao.FileDao;
import com.ym.domain.FileDO;
import com.ym.utils.derbyConectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileDaoImpl implements FileDao {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
    Connection connection = derbyConectUtil.GetConnection();
    FileDO file=new FileDO();
    @Override
    public FileDO get(String id) {

        String sql = "SELECT * FROM file_info WHERE id = ?";
        //return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(FileDO.class));
        try{

            PreparedStatement st=connection.prepareStatement(sql);
            st.setString(1,id);
            ResultSet rs=st.executeQuery();
            while(rs.next()){

                file.setName(rs.getString("name"));
                file.setId(rs.getString("id"));
                file.setType(rs.getString("type"));
                file.setSize(rs.getLong("size"));
                file.setUrl(rs.getString("url"));
                file.setContent(rs.getString("content"));
                file.setCreateDate(rs.getDate("create_date"));
             }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("失败");
        }finally {
            return  file;
        }
    }

    @Override
    public List<FileDO> list(Integer currentPage, Integer linesize) {
        //mysql分页查询
        //String sql = "SELECT * FROM file_info LIMIT ?,?";
        //derby分页查询
        String sql ="SELECT * FROM file_info  " +
                "OFFSET ? ROWS " +
                "FETCH NEXT ? ROWS ONLY";
        //SqlRowSet result = jdbcTemplate.queryForRowSet(sql, currentPage, linesize);
        List<FileDO> list = new ArrayList<>();
        try{
            PreparedStatement st=connection.prepareStatement(sql);
            st.setInt(1,currentPage);
            st.setInt(2,linesize);
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                FileDO file=new FileDO();
                file.setName(rs.getString("name"));
                file.setId(rs.getString("id"));
                file.setType(rs.getString("type"));
                file.setSize(rs.getLong("size"));
                file.setUrl(rs.getString("url"));
                file.setContent(rs.getString("content"));
                file.setCreateDate(rs.getDate("create_date"));
                list.add(file);
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("失败");
        }finally {
            return  list;
        }

    }

    @Override
    public Long count() {
        Long count=0L;
        String sql = "SELECT COUNT(*) FROM file_info";
        try{

            PreparedStatement st=connection.prepareStatement(sql);

            ResultSet rs=st.executeQuery();
            while(rs.next()){
                count=rs.getLong(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("失败");
        }finally {
            return  count;
        }
    }

    @Override
    public int save(FileDO file) {
        int flag=0;
        String sql = "INSERT INTO file_info (id, name, type, size, url, content, create_date) VALUES(?,?,?,?,?,?,?)";
        try{

            PreparedStatement st=connection.prepareStatement(sql);
            st.setString(1,file.getId());
            st.setString(2,file.getName());
            st.setString(3,file.getType());
            st.setLong(4,file.getSize());
            st.setString(5,file.getUrl());
            st.setString(6,file.getContent());
            st.setDate(7,new java.sql.Date(file.getCreateDate().getTime()));
            flag=st.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("失败");
        }finally {
            return  flag;
        }
    }

    @Override
    public int remove(String id) {
        int flag=0;
        String sql = "DELETE FROM file_info WHERE id = ?";
        try{

            PreparedStatement st=connection.prepareStatement(sql);
            st.setString(1,id);
            flag=st.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("失败");
        }finally {
            return  flag;
        }
    }
}
