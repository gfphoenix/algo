/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.maps;

import com.badlogic.gdx.utils.Disposable;

public class Map implements Disposable {
	private MapLayers layers = new MapLayers();
	private MapProperties properties = new MapProperties();
	
	/**
	 * Creates empty map
	 */
	public Map() {
		
	}
	
	/**
	 * @return the map's layers
	 */
	public MapLayers getLayers() {
		return layers;
	}

	/**
	 * @return the map's properties
	 */
	public MapProperties getProperties() {
		return properties;
	}


	@Override
	public void dispose () {
	}
}
