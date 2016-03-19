package to.kit.drink.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import to.kit.drink.dto.ListRequest;
import to.kit.sas.control.Controller;

/**
 * 一覧系コントローラー.
 * @author Hidetaka Sasai
 */
public class AccountController implements Controller<Object> {
	/**
	 * ログインユーザーを取得.
	 * @param form フォーム
	 * @return 言語一覧
	 */
	public Object getUser(ListRequest form) {
		Map<String, Object> map = new HashMap<>();
		UserService service = UserServiceFactory.getUserService();

		if (service != null && service.isUserLoggedIn()) {
			User user = service.getCurrentUser();
			String userid = user.getUserId();

			System.out.println("userid:" + userid);
			map.put("user", user);
		} else {
			System.out.println("userid?");
			map.put("user", null);
		}
		return map;
	}

	@Override
	public Object execute(Object form) {
		return null;
	}
}
