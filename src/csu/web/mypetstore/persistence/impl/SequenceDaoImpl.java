package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.Sequence;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.SequenceDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SequenceDaoImpl implements SequenceDao {
    private static final String GetSequence = "SELECT name, nextid FROM SEQUENCE WHERE NAME = ?";
    private static final String UpdateSequence = "UPDATE SEQUENCE SET NEXTID = ? WHERE NAME = ?";
    @Override
    public Sequence getSequence(Sequence sequence) {
        Sequence sequence1 = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GetSequence);
            preparedStatement.setString(1,sequence.getName());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                sequence1 = new Sequence();
                sequence1.setName(rs.getString(1));
                sequence1.setNextId(rs.getInt(2));
            }
            DBUtil.closeAll(connection,preparedStatement,rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sequence1;
    }

    @Override
    public void updateSequence(Sequence sequence) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UpdateSequence);
            preparedStatement.setInt(1,sequence.getNextId());
            preparedStatement.setString(2,sequence.getName());
            preparedStatement.executeUpdate();
            DBUtil.closeAll(connection,preparedStatement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}