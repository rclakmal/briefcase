/*
 * Copyright (C) 2011 University of Washington.
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

package org.opendatakit.briefcase.model;

/**
 * This class models a proxy connection.
 * 
 * @author rclakmal@gmail.com
 *
 */
public class ProxyConnection {
	private String host;
	private Integer port;
	private ProxyType proxyType;

	public enum ProxyType {
		NO_PROXY, HTTP, HTTPS
	}

	public ProxyConnection(String host, Integer port, ProxyType proxyType) {
		super();
		this.host = host;
		this.port = port;
		this.proxyType = proxyType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ProxyType getProxyType() {
		return proxyType;
	}

	public void setProxyType(ProxyType proxyType) {
		this.proxyType = proxyType;
	}

}