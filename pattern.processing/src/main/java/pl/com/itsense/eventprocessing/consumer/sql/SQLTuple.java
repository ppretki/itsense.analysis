package pl.com.itsense.eventprocessing.consumer.sql;

import java.util.HashMap;
import java.util.LinkedHashMap;

import pl.com.itsense.eventprocessing.provider.rexpression.RExpressionEvent;
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
        sb.append("INSERT INTO ").append(table.getName()).append(" (");
        final StringBuffer tables = new StringBuffer();
        final StringBuffer values = new StringBuffer();
        for (final SQLTable table : references.keySet())
        {
            final SQLTuple tuple = references.get(table);
            if (tables.length() > 0)
            {
                tables.append(",");
                values.append(",");
            }
            tables.append(table.getName());
            values.append(tuple.id);
        }
        sb.append(tables).append(" ) VALUES ( ").append(values).append(");");
        return sb.toString();
    }

}
