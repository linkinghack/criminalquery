package com.linkinghack.criminalquerybase.dao.mapper;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.ARRAY)
public class IntArrayHandler extends BaseTypeHandler<Integer[]> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Integer[] integers, JdbcType jdbcType) throws SQLException {
        preparedStatement.setArray(i,preparedStatement.getConnection().createArrayOf(jdbcType.name(), integers));
    }

    @Override
    public Integer[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return (Integer[]) resultSet.getArray(s).getArray();
    }

    @Override
    public Integer[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return (Integer[]) resultSet.getArray(i).getArray();
    }

    @Override
    public Integer[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return (Integer[]) callableStatement.getArray(i).getArray();
    }
}
