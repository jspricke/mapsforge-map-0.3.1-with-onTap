/*
 * Copyright 2010, 2011, 2012 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.mapsforge.core.model.CoordinatesUtil;
import org.mapsforge.core.model.Tile;

public class MercatorProjectionTest {
	private static final int ZOOM_LEVEL_MAX = 30;
	private static final int ZOOM_LEVEL_MIN = 0;

	@Test
	public void getMapSizeTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			long factor = Math.round(Math.pow(2, zoomLevel));
			Assert.assertEquals(Tile.TILE_SIZE * factor, MercatorProjection.getMapSize(zoomLevel));
		}

		try {
			MercatorProjection.getMapSize((byte) -1);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void latitudeToPixelYTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			double pixelY = MercatorProjection.latitudeToPixelY(MercatorProjection.LATITUDE_MAX, zoomLevel);
			Assert.assertEquals(0, pixelY, 0);

			long mapSize = MercatorProjection.getMapSize(zoomLevel);
			pixelY = MercatorProjection.latitudeToPixelY(0, zoomLevel);
			Assert.assertEquals(mapSize / 2, pixelY, 0);

			pixelY = MercatorProjection.latitudeToPixelY(MercatorProjection.LATITUDE_MIN, zoomLevel);
			Assert.assertEquals(mapSize, pixelY, 0);
		}
	}

	@Test
	public void longitudeToPixelXTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			double pixelX = MercatorProjection.longitudeToPixelX(CoordinatesUtil.LONGITUDE_MIN, zoomLevel);
			Assert.assertEquals(0, pixelX, 0);

			long mapSize = MercatorProjection.getMapSize(zoomLevel);
			pixelX = MercatorProjection.longitudeToPixelX(0, zoomLevel);
			Assert.assertEquals(mapSize / 2, pixelX, 0);

			pixelX = MercatorProjection.longitudeToPixelX(CoordinatesUtil.LONGITUDE_MAX, zoomLevel);
			Assert.assertEquals(mapSize, pixelX, 0);
		}
	}

	@Test
	public void pixelXToLongitudeTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			double longitude = MercatorProjection.pixelXToLongitude(0, zoomLevel);
			Assert.assertEquals(CoordinatesUtil.LONGITUDE_MIN, longitude, 0);

			long mapSize = MercatorProjection.getMapSize(zoomLevel);
			longitude = MercatorProjection.pixelXToLongitude(mapSize / 2, zoomLevel);
			Assert.assertEquals(0, longitude, 0);

			longitude = MercatorProjection.pixelXToLongitude(mapSize, zoomLevel);
			Assert.assertEquals(CoordinatesUtil.LONGITUDE_MAX, longitude, 0);
		}
	}

	@Test
	public void pixelXToTileXTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			Assert.assertEquals(0, MercatorProjection.pixelXToTileX(0, zoomLevel));
		}
	}

	@Test
	public void pixelYToLatitudeTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			double latitude = MercatorProjection.pixelYToLatitude(0, zoomLevel);
			Assert.assertEquals(MercatorProjection.LATITUDE_MAX, latitude, 0);

			long mapSize = MercatorProjection.getMapSize(zoomLevel);
			latitude = MercatorProjection.pixelYToLatitude(mapSize / 2, zoomLevel);
			Assert.assertEquals(0, latitude, 0);

			latitude = MercatorProjection.pixelYToLatitude(mapSize, zoomLevel);
			Assert.assertEquals(MercatorProjection.LATITUDE_MIN, latitude, 0);
		}
	}

	@Test
	public void pixelYToTileYTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			Assert.assertEquals(0, MercatorProjection.pixelYToTileY(0, zoomLevel));
		}
	}

	@Test
	public void tileXToLongitudeTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			double longitude = MercatorProjection.tileXToLongitude(0, zoomLevel);
			Assert.assertEquals(CoordinatesUtil.LONGITUDE_MIN, longitude, 0);

			long tileX = MercatorProjection.getMapSize(zoomLevel) / Tile.TILE_SIZE;
			longitude = MercatorProjection.tileXToLongitude(tileX, zoomLevel);
			Assert.assertEquals(CoordinatesUtil.LONGITUDE_MAX, longitude, 0);
		}
	}

	@Test
	public void tileYToLatitudeTest() {
		for (byte zoomLevel = ZOOM_LEVEL_MIN; zoomLevel <= ZOOM_LEVEL_MAX; ++zoomLevel) {
			double latitude = MercatorProjection.tileYToLatitude(0, zoomLevel);
			Assert.assertEquals(MercatorProjection.LATITUDE_MAX, latitude, 0);

			long tileY = MercatorProjection.getMapSize(zoomLevel) / Tile.TILE_SIZE;
			latitude = MercatorProjection.tileYToLatitude(tileY, zoomLevel);
			Assert.assertEquals(MercatorProjection.LATITUDE_MIN, latitude, 0);
		}
	}
}
