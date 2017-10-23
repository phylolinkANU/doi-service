package au.org.ala.doi;

import java.sql.Types;

public class PostgresqlExtensionsDialect extends net.kaleidos.hibernate.PostgresqlExtensionsDialect {

    public PostgresqlExtensionsDialect() {
        super();
        registerColumnType(Types.OTHER, "CITEXT");
    }
}
