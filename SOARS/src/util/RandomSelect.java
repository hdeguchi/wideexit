package util;

import role.Role;
import env.Environment;

/**
 * The RandomSelect class represents random select resolver.
 * @author H. Tanuma / SOARS project
 */
public class RandomSelect extends Resolver implements Askable<Role> {

	private static final long serialVersionUID = 2898593906810358124L;
	int n = 0;
	Role caller;
	Object message;

	public boolean ask(Role caller, Object message) throws Exception {
		if (++n == 1) {
			requestResolve();
		}
		else if (Environment.getCurrent().getRandom().nextInt(n) != 1) {
			return false;
		}
		this.caller = caller;
		this.message = message;
		return true;
	}
	public void resolve() throws Exception {
		n = 0;
		caller.ask(caller, message);
	}
}
