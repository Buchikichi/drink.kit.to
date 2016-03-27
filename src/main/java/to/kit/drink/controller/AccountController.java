package to.kit.drink.controller;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import to.kit.sas.control.Controller;
import to.kit.util.SysEnv;

/**
 * アカウント系コントローラー.
 * @author Hidetaka Sasai
 */
public class AccountController implements Controller<Object> {
	@Override
	public Object execute(Object form) {
		if (!SysEnv.isLocal()) {
			UserService service = UserServiceFactory.getUserService();

			return service.getCurrentUser();
		}
		return new User("ぶちきち", "");
	}
}
