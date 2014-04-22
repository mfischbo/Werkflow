package de.artignition.werkflow.io;

import java.io.Serializable;

public interface ResponsiveObjectMessageDelegate {
	
	Serializable handleMessage(Serializable object);
}
