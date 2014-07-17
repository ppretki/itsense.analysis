package pattern.processing;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

public class MVELMain
{

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        Map varsMap = new HashMap();
        varsMap.put("var1", "dupa");
        varsMap.put("var2", "dupa");
        VariableResolverFactory factory = new MapVariableResolverFactory(varsMap);
        System.out.println(MVEL.eval("var1 == var2", factory));
    }

}
