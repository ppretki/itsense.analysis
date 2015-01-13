package pl.com.itsense.eventprocessing.consumer.sql;

import java.util.LinkedHashMap;

import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionEvent;
import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionGroup;
/**
 * 
 */
public final class SQLTuple
{
    /** */
    private final int id;
    /** */
    private final RExpressionEvent event;
    /** */
    private final SQLTable table;
    /** */
    private final LinkedHashMap<SQLTable, SQLTuple> references = new LinkedHashMap<SQLTable, SQLTuple>();
    /** */
    public SQLTuple(final SQLTable table, final int id, final RExpressionEvent event)
    {
        this.table = table;
        this.id = id;
        this.event = event;
    }
    /**
     * 
     * @return
     */
    public SQLTable getTable()
    {
        return table;
    }
    
    /**
     * 
     * @return
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * 
     * @param expression
     * @return
     */
    public void setReference(final SQLTuple tuple)
    {
        if (tuple != null)
        {
            references.put(tuple.table, tuple);
        }
    }
    /**
     * 
     * @return
     */
    public SQLTuple getReference(final SQLTable table)
    {
        return references.get(table);
    }

    /**
     * 
     */
    public String insertInto()
    {
        final StringBuffer sb = new StringBuffer();
        final StringBuffer columns = new StringBuffer("ID");
        final StringBuffer values = new StringBuffer(String.valueOf(id));

        for (final RExpressionGroup group : table.getExpression().getGroups())
        {
            columns.append(",").append(group.getName());
            values.append(",").append(event.getGroup(group));
        }
        
        for (final SQLTable table : references.keySet())
        {
            final SQLTuple tuple = references.get(table);
            columns.append(",").append(table.getName());
            values.append(",").append(tuple.id);
        }
        
        sb.append("INSERT INTO ").append(table.getName()).append(" (").append(columns.toString()).append(") VALUES (").append(values.toString()).append(");");
        return sb.toString();
    }

}
