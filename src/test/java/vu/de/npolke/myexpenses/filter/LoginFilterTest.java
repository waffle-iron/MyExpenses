package vu.de.npolke.myexpenses.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import vu.de.npolke.myexpenses.model.Account;

/**
 * Copyright 2015 Niklas Polke
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Niklas Polke
 */
public class LoginFilterTest {

	private LoginFilter filter = new LoginFilter();

	@Test
	public void redirectWhenNotLoggedIn() {
		assertTrue(filter.redirectToLoginPage("/myexpenses/listexpenses", "/myexpenses", "GET", null));
		assertTrue(filter.redirectToLoginPage("/myexpenses/listexpenses.jsp", "/myexpenses", "GET", null));
	}

	@Test
	public void noRedirectWhen_LoggedIn() {
		assertFalse(filter.redirectToLoginPage("/myexpenses/index.jsp", "/myexpenses", "GET", null));
	}

	@Test
	public void noRedirectWhen_LoginRequest() {
		assertFalse(filter.redirectToLoginPage("/myexpenses/login", "/myexpenses", "POST", null));
	}

	@Test
	public void noRedirectWhen_LoginPage() {
		assertFalse(filter.redirectToLoginPage("/myexpenses/listexpenses", "/myexpenses", "GET", new Account()));
		assertFalse(filter.redirectToLoginPage("/myexpenses/listexpenses.jsp", "/myexpenses", "GET", new Account()));
	}

	@Test
	public void noRedirectWhen_ResourceRequest() {
		assertFalse(filter.redirectToLoginPage("/myexpenses/img/sign-add_24.png", "/myexpenses", "GET", null));
		assertFalse(filter.redirectToLoginPage("/myexpenses/css/styles.css", "/myexpenses", "GET", null));
		assertFalse(filter.redirectToLoginPage("/myexpenses/js/chartist.min.js", "/myexpenses", "GET", null));
	}

	@Test
	public void noRedirectWhen_RegisterPage() {
		assertFalse(filter.redirectToLoginPage("/myexpenses/register.jsp", "/myexpenses", "GET", null));
	}

	@Test
	public void noRedirectWhen_RegisterRequest() {
		assertFalse(filter.redirectToLoginPage("/myexpenses/register", "/myexpenses", "POST", null));
	}
}