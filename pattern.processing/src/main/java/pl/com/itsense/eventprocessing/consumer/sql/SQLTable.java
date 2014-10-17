package pl.com.itsense.eventprocessing.consumer.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import pl.com.itsense.eventprocessing.provider.rexpression.RExpression;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionGroup;

/**
 * 
 * @author P.Pretki
 *
 */
public final class SQLTable
{
    /** */
    private final RExpression expression;
    /** */
    private final Connection connection;
    /** */
    public SQLTable(final RExpression expression, final Connection connection)
    {
        this.expression = expression;
        this.connection = connection;
    }
    /**
     * 
     */
    public void init(final List<SQLTable> tables)
    {
        final String createTable = getCreateTableStatement(tables);
        try
        {
            final Statement stmt = connection.createStatement();
            stmt.executeUpdate(createTable);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     * @return
     */
    private String getCreateTableStatement(final List<SQLTable> tables)
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(expression.getId()).append(" (");
        sb.append(" id INTEGER not NULL, ");
        for(final RExpressionGroup group : expression.getGroups())
        {
            sb.append(group.getName()).append(" ").append(group.getType()).append(",");
        }
        for (final SQLTable table : tables)
        {
            sb.append(table.expression.getId()).append(" INTEGER ").append(",");
        }
        sb.append(" PRIMARY KEY ( id ));");
        return sb.toString();
    }
}
