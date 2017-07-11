package util;

import env.Environment;

/**
 * The Resolver class represents resolver.
 * @author H. Tanuma / SOARS project
 */
public abstract class Resolver implements Resolvable {

	private static final long serialVersionUID = -702528926497409265L;

	public static void requestResolve(Resolvable resolvable) {
		Environment.getCurrent().getResolveRequests().addLast(resolvable);
	}
	public void requestResolve() {
		requestResolve(this);
	}
}
