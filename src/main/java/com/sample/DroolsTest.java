package com.sample;

import javax.swing.JOptionPane;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;


public class DroolsTest {
    public static final void main(String[] args) {
        try{
        	KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");
        	KieBase kbase = kSession.getKieBase();
        	QueryResults results;
            do{
            	kSession.fireAllRules();
            	results = kSession.getQueryResults("Questions without answers");
            	for (QueryResultsRow result : results){
            		Object obj = result.get("question");
            		FactType questionType = kbase.getFactType("com.sample", "Question");
            		String question = (String)questionType.get(obj, "question");
            	    int res = JOptionPane.showConfirmDialog(null, question);
            	    switch (res) {
            	    	case JOptionPane.YES_OPTION:
            	    		questionType.set(obj, "answer", "Yes");
            	    		break;
            	        case JOptionPane.NO_OPTION:
            	        	questionType.set(obj, "answer", "No");
            	        	break;
            	    }
            	    FactHandle handle = kSession.getFactHandle(obj);
            	    kSession.update(handle, obj);
            	}
            	results = kSession.getQueryResults("Get answer");
           }
           while (results.size() == 0);
           for (QueryResultsRow result : results){
        		Object obj = result.get("answer");
        		FactType questionType = kbase.getFactType("com.sample", "FinalAnswer");
        		String finalAnswer = (String)questionType.get(obj, "answer");
        		JOptionPane.showMessageDialog(null, finalAnswer);
           }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
