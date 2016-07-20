package com.scg.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Approves or Rejects compensation changes
 * 
 * @author craigrainey
 */
public class CompensationManager implements PropertyChangeListener, VetoableChangeListener {
	
	/**	Double to represent maximum pay increase percentage */
	private final double PAY_INCREASE = 1.05;
	
	/**	Logger to log any property and vetos */
	private final Logger log = LoggerFactory.getLogger(CompensationManager.class);
	
	/**	Constructor */
	public CompensationManager() {
	}

	/**	Vetos a pay rate adjustment if the new rate is over 5% of original pay */
	@Override
	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		String propName = evt.getPropertyName();
		
		try {
			if ("payRate".equals(propName)) {
				int oldComp = (Integer) evt.getOldValue();
				int newComp = (Integer) evt.getNewValue();
				if (oldComp * PAY_INCREASE < newComp) {
					log.info("REJECTED: pay rate change from " +evt.getOldValue()+ " to " +evt.getNewValue());
					throw new PropertyVetoException("Count not adjust pay rate because increase is too large ", evt);
				}	
			}
		} catch (ClassCastException c) {
			c.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**	Fires a property change */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if ("payRate".equals(property)) {
			log.info("APPROVED: pay rate changed from " +evt.getOldValue()+ " to " +evt.getNewValue());
		}
	}

}
