package pl.com.itsense.eventprocessing.consumer.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import pl.com.itsense.eventprocessing.provider.rexpression.RExpression;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionEvent;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionGroup;

/**
 * 
 * @author P.Pretki
 *
 */
public final class SQLTable
{
    /** */
    private int recordCount = 0;
    /** */
    private final RExpression expression;
    /** */
    private final Connection connection;
    /** */
    private LinkedList<SQLTable> references = new LinkedList<SQLTable>();
    /** */
    private final LinkedList<SQLTuple> tuples = new LinkedList<SQLTuple>();
    /** */
    private final HashMap<SQLTable, SQLTuple> waiting = new HashMap<SQLTable, SQLTuple>(); 
    /** */
    public SQLTable(final RExpression expression, final Connection connection)
    {
        this.expression = expression;
        this.connection = connection;
    }
    /**
     * 
     */
    public void init(final Collection<SQLTable> tables)
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
     */
    public SQLTuple insert(final RExpressionEvent event)
    {
        final SQLTuple tuple = new SQLTuple(this, recordCount++, event);
        tuples.add(tuple);
        for (final SQLTable table : references)
        {
            if (waiting.get(table) == null)
            {
                waiting.put(table, tuple);
            }
        }
        return tuple;
    }
    /**
     * 
     * @return
     */
    private String getCreateTableStatement(final Collection<SQLTable> tables)
    {
        final StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ").append(expression.getId()).append(" (");
        sb.append(" id BIGINT NOT NULL,");
        for(final RExpressionGroup group : expression.getGroups())
        {
            sb.append(group.getName()).append(" ").append(group.getType()).append(",");
        }
        for (final SQLTable table : tables)
        {
            references.add(table);
            waiting.put(table, null);
            sb.append(table.expression.getId()).append(" BIGINT ").append(",");
        }
        sb.append(" PRIMARY KEY ( id ));");
        return sb.toString();
    }
    /**
     * 
     * @param tuple
     */
    public void setCrossReferences(final SQLTuple tuple)
    {
       final SQLTuple t = waiting.get(tuple.getTable());
       if (t != null && (tuple != t))
       {
           t.setReference(tuple);
           if (isComplete(t))
           {
               save(t);
               waiting.put(tuple.getTable(), next(t));
           }
       }
    }
    
    private boolean isComplete(final SQLTuple tuple)
    {
        boolean complete = true;
        for (final SQLTable table : references)
        {
            if (tuple.getReference(table) == null)
            {
                complete = false;
                break;
            }
        }
        return complete;
    }
    /**
     * 
     * @return
     */
    private SQLTuple next(final SQLTuple tuple)
    {
        return tuples.get(tuple.getId() + 1);
    }
    
    private void save(final SQLTuple tuple)
    {
        try
        {
            final Statement stmt = connection.createStatement();
            stmt.executeUpdate(tuple.insertInto());
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
    public String getName()
    {
        return expression.getId();
    }
    /**
     * 
     * @return
     */
    public RExpression getExpression()
    {
        return expression;
    }
    
}
