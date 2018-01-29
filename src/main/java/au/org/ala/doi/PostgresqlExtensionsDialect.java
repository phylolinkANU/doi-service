package au.org.ala.doi;

import net.kaleidos.hibernate.usertype.ArrayType;

import java.sql.Types;

public class PostgresqlExtensionsDialect extends net.kaleidos.hibernate.PostgresqlExtensionsDialect {

    public PostgresqlExtensionsDialect() {
        super();
        registerColumnType(Types.OTHER, "CITEXT");
        registerColumnType(ArrayType.STRING_ARRAY, "_text");
    }
}
