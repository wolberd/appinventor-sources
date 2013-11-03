// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt
package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.JsonUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;

/**
 * Persistently store YAIL values on the phone using tags to store and retrieve.
 *
 * @author markf@google.com (Mark Friedman)
 */
@DesignerComponent(version = YaVersion.TINYDB_COMPONENT_VERSION,
    description = "Non-visible component that persistently stores values on the phone.",
    category = ComponentCategory.STORAGE,
    nonVisible = true,
    iconName = "images/tinyDB.png")

@SimpleObject
public class TinyDB extends AndroidNonvisibleComponent implements Component, Deleteable {

  private SharedPreferences sharedPreferences;

  private Context context;  // this was a local in constructor and final not private


  /**
   * Creates a new TinyDB component.
   *
   * @param container the Form that this component is contained in.
   */
  public TinyDB(ComponentContainer container) {
    super(container.$form());
    context = (Context) container.$context();
    sharedPreferences = context.getSharedPreferences("TinyDB1", Context.MODE_PRIVATE);
  }

  /**
   * Store the given value under the given tag.  The storage persists on the
   * phone when the app is restarted.
   *
   * @param tag The tag to use
   * @param valueToStore The value to store. Can be any type of value (e.g.
   * number, text, boolean or list).
   */
  @SimpleFunction
  public void StoreValue(final String tag, final Object valueToStore) {
    final SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
    try {
      sharedPrefsEditor.putString(tag, JsonUtil.getJsonRepresentation(valueToStore));
      sharedPrefsEditor.commit();
    } catch (JSONException e) {
      throw new YailRuntimeError("Value failed to convert to JSON.", "JSON Creation Error.");
    }
  }

  /**
   * Retrieve the value stored under the given tag.
   *
   * @param tag The tag to use
   * @param valueIfTagNotThere The value returned if tag in not in TinyDB
   * @return The value stored under the tag. Can be any type of value (e.g.
   * number, text, boolean or list).
   */
  @SimpleFunction
  public Object GetValue(final String tag, final Object valueIfTagNotThere) {
    try {
      String value = sharedPreferences.getString(tag, "");
      // If there's no entry with tag as a key then return the empty string.
      //    was  return (value.length() == 0) ? "" : JsonUtil.getObjectFromJson(value);
      return (value.length() == 0) ? valueIfTagNotThere : JsonUtil.getObjectFromJson(value);
    } catch (JSONException e) {
      throw new YailRuntimeError("Value failed to convert from JSON.", "JSON Creation Error.");
    }
  }

   /**
   * Get all the tags in the data store
   *
   * @param
   * @return a list of all keys.
   */
  @SimpleFunction
  public Object GetTags() {
    List<String> keyList = new ArrayList<String>();
    Map<String,?> keyValues = sharedPreferences.getAll();
    // here is the simple way to get keys
    keyList.addAll(keyValues.keySet());
    java.util.Collections.sort(keyList);
    return keyList;
  }

  /**
   * Clear the entire data store
   *
   */
  @SimpleFunction
  public void ClearAll() {
    final SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
    sharedPrefsEditor.clear();
    sharedPrefsEditor.commit();
  }

  /**
   * Clear entry with given tag
   *
   * @param tag The tag to remove.
   */
  @SimpleFunction
  public void ClearTag(final String tag) {
    final SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
    sharedPrefsEditor.remove(tag);
    sharedPrefsEditor.commit();
  }

  @Override
  public void onDelete() {
    final SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
    sharedPrefsEditor.clear();
    sharedPrefsEditor.commit();
  }
}
