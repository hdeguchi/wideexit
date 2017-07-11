package util;

import env.Agent;
import env.Environment;

/**
 * The SelfResolver class represents self resolver.
 * @author H. Tanuma / SOARS project
 */
public abstract class SelfResolver implements Resolvable, Ownable {

	private static final long serialVersionUID = -777203818794761246L;
	Agent agent = null;

	public static void requestResolve(Resolvable resolvable) {
		Environment.getCurrent().getResolveRequests().addFirst(resolvable);
	}
	public void requestResolve() {
		requestResolve(this);
	}
	public void setOwner(Object owner) {
		if (agent == null || owner == null) {
			agent = (Agent) owner;
		}
	}
	public Agent getAgent() {
		return agent;
	}
}
