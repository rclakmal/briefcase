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
import java.security.Security;
import java.util.prefs.Preferences;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.opendatakit.briefcase.buildconfig.BuildConfig;
import org.opendatakit.briefcase.model.ProxyConnection.ProxyType;

/**
 * This class is used to manage the applications preferences. It achieves this task, by using the standard
 * {@link java.util.prefs.Preferences Preferences API}.
 */
public class BriefcasePreferences {
  
  public static final String VERSION = BuildConfig.VERSION;
  public static final String USERNAME = "username";
  public static final String TOKEN = "token";
  public static final String AGGREGATE_1_0_URL = "url_1_0";
  public static final String AGGREGATE_0_9_X_URL = "url_0_9_X";
  
  private static final String BRIEFCASE_DIR_PROPERTY = "briefcaseDir";
  private static final String BRIEFCASE_PROXY_TYPE_PROPERTY = "briefcaseProxy";
  private static final String BRIEFCASE_PROXY_HOST_PROPERTY = "briefcaseProxyHost";
  private static final String BRIEFCASE_PROXY_PORT_PROPERTY = "briefcaseProxyPort";
  
  static {
    // load the security provider
    Security.addProvider(new BouncyCastleProvider());
  }

  private final Preferences preferences;
  
  private BriefcasePreferences(Class<?> node, PreferenceScope scope) {
    this.preferences = scope.preferenceFactory(node);
  }
  
  /**
   * Factory that creates a class scoped <tt>BriefcasePreferences</tt> object. 
   * @param node the managing class
   */
  public static BriefcasePreferences forClass(Class<?> node) {
    return new BriefcasePreferences(node, PreferenceScope.CLASS_NAME);
  }
  
  /**
   * Returns the value associated with the specified key in this preference
   * node. Returns the specified default if there is no value associated with
   * the key, or the backing store is inaccessible.
   * 
   * @param key
   *          key whose associated value is to be returned.
   * @param defaultValue
   *          the value to be returned in the event that this preference node
   *          has no value associated with key.
   * @return the value associated with key, or defaultValue if no value is associated
   *         with key, or the backing store is inaccessible.
   */
  public String get(String key, String defaultValue) {
    return preferences.get(key, defaultValue);
  }
  
  /**
   * Associates the specified value with the specified key in this preference
   * node.
   * 
   * @param key
   *          key with which the specified value is to be associated.
   * @param value
   *          value to be associated with the specified key.
   */
  public void put(String key, String value) {
    preferences.put(key, value);
  }
  
  /**
   * Removes the value associated with the specified key in this preference
   * node, if any.
   * 
   * @param key
   *          key whose mapping is to be removed from the preference node.
   */
  public void remove(String key) {
    preferences.remove(key);
  }

  public static void setBriefcaseDirectoryProperty(String value) {
    if (value == null) {
      Preference.APPLICATION_SCOPED.remove(BRIEFCASE_DIR_PROPERTY);
    } else {
      Preference.APPLICATION_SCOPED.put(BriefcasePreferences.BRIEFCASE_DIR_PROPERTY, value);
    }
  }

  public static String getBriefcaseDirectoryIfSet() {
    return Preference.APPLICATION_SCOPED.get(BriefcasePreferences.BRIEFCASE_DIR_PROPERTY, null);
  }

  public static String getBriefcaseDirectoryProperty() {
    return Preference.APPLICATION_SCOPED.get(BriefcasePreferences.BRIEFCASE_DIR_PROPERTY,
        System.getProperty("user.home"));
  }
  
  public static void setBriefcaseProxyProperty(ProxyConnection value) {
    if (value == null) {
	  Preference.APPLICATION_SCOPED.remove(BRIEFCASE_PROXY_TYPE_PROPERTY);
    } else {
	  Preference.APPLICATION_SCOPED.put(BriefcasePreferences.BRIEFCASE_PROXY_TYPE_PROPERTY, value.getProxyType().toString());
	  Preference.APPLICATION_SCOPED.put(BriefcasePreferences.BRIEFCASE_PROXY_HOST_PROPERTY, value.getHost());
	  Preference.APPLICATION_SCOPED.put(BriefcasePreferences.BRIEFCASE_PROXY_PORT_PROPERTY, value.getPort().toString());
    }
  }
  
  /**
   * Enum that implements the strategies, to create differently scoped preferences.
   */
  private enum PreferenceScope {
    APPLICATION {
      @Override
      public Preferences preferenceFactory(Class<?> node) {
        return Preferences.userNodeForPackage(node);
      }
    },
    CLASS_NAME {
      @Override
      public Preferences preferenceFactory(Class<?> node) {
        return Preferences.userRoot().node(node.getName());
      }
    };
    public abstract Preferences preferenceFactory(Class<?> node);
  }
  
  /**
   * Through this static nested class, we implement the Initialization-on-demand
   * holder idiom. It enables a safe, highly concurrent lazy initialization for
   * singletons. For more information on this idiom, please refer to <a href=
   * "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
   * Initialization-on-demand holder idiom</a>.
   */
  private static class Preference {
    private static final BriefcasePreferences APPLICATION_SCOPED =
        new BriefcasePreferences(BriefcasePreferences.class, PreferenceScope.APPLICATION);
  }
 
  public static ProxyConnection getBriefCaseProxyConnection() {
	  String proxyType = Preference.APPLICATION_SCOPED.get(BriefcasePreferences.BRIEFCASE_PROXY_TYPE_PROPERTY,
			  null);
	  if(proxyType != null){
		  String host = Preference.APPLICATION_SCOPED.get(
				  BriefcasePreferences.BRIEFCASE_PROXY_HOST_PROPERTY,"");
		  Integer port = Integer.parseInt(Preference.APPLICATION_SCOPED.get(
				  BriefcasePreferences.BRIEFCASE_PROXY_PORT_PROPERTY,"0"));
		  return new ProxyConnection(host, port, ProxyType.valueOf(proxyType));
	  }
	  return null;
  }
}
