/*
 * Copyright 2013 ArcBees Inc.
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
 */

package com.duggan.workflow.server.security.authenticator;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.ServerConstants;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;

public class CurrentUserDtoProvider implements Provider<CurrentUserDto> {
	private final Provider<HttpServletRequest> httpServletRequestProvider;

	@Inject
	CurrentUserDtoProvider(Provider<HttpServletRequest> httpServletRequestProvider) {
		this.httpServletRequestProvider = httpServletRequestProvider;
	}

	@Override
	public CurrentUserDto get() {
		HttpServletRequest request = httpServletRequestProvider.get();
		HTUser userDto = null;
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute(ServerConstants.USER)!=null){
			userDto = (HTUser) session.getAttribute(ServerConstants.USER);
		}
		
		boolean isLoggedIn = userDto != null;

		return new CurrentUserDto(isLoggedIn, userDto);
	}
}
