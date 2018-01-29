package au.org.ala.doi;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayType extends net.kaleidos.hibernate.usertype.ArrayType {

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        final Object values = super.nullSafeGet(rs, names, session, owner);
//        return Arrays.asList(values);

        List<String> result = Arrays.stream((String[])values).collect(Collectors.toList());
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        List<String> list = (List) value;
        String[] array = list.toArray(new String[list.size()]);

        super.nullSafeSet(st, array, index, session);
    }
}
