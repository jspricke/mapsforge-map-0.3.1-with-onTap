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
package org.mapsforge.android.maps.overlay;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * A {@code Circle} consists of a center {@link GeoPoint} and a non-negative radius in meters.
 * <p>
 * A {@code Circle} holds two {@link Paint} objects to allow for different outline and filling. These paints define
 * drawing parameters such as color, stroke width, pattern and transparency. {@link Paint#setAntiAlias Anti-aliasing}
 * should be enabled to minimize visual distortions and to improve the overall drawing quality.
 */
public class Circle implements OverlayItem {
	private static void checkRadius(float radius) {
		if (radius < 0) {
			throw new IllegalArgumentException("radius must not be negative: " + radius);
		}
	}

	private static double metersToPixels(double latitude, float meters, byte zoom) {
		double groundResolution = MercatorProjection.calculateGroundResolution(latitude, zoom);
		return meters / groundResolution;
	}

	private GeoPoint geoPoint;
	private Paint paintFill;
	private Paint paintStroke;
	private float radius;

	/**
	 * @param geoPoint
	 *            the initial center point of this circle (may be null).
	 * @param radius
	 *            the initial non-negative radius of this circle in meters.
	 * @param paintFill
	 *            the initial {@code Paint} used to fill this circle (may be null).
	 * @param paintStroke
	 *            the initial {@code Paint} used to stroke this circle (may be null).
	 * @throws IllegalArgumentException
	 *             if the given {@code radius} is negative.
	 */
	public Circle(GeoPoint geoPoint, float radius, Paint paintFill, Paint paintStroke) {
		checkRadius(radius);
		this.geoPoint = geoPoint;
		this.radius = radius;
		this.paintFill = paintFill;
		this.paintStroke = paintStroke;
	}

	@Override
	public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
		if (this.geoPoint == null || (this.paintStroke == null && this.paintFill == null)) {
			return false;
		}

		double latitude = this.geoPoint.latitude;
		double longitude = this.geoPoint.longitude;
		float pixelX = (float) (MercatorProjection.longitudeToPixelX(longitude, zoomLevel) - canvasPosition.x);
		float pixelY = (float) (MercatorProjection.latitudeToPixelY(latitude, zoomLevel) - canvasPosition.y);
		float radiusInPixel = (float) metersToPixels(latitude, this.radius, zoomLevel);

		if (this.paintStroke != null) {
			canvas.drawCircle(pixelX, pixelY, radiusInPixel, this.paintStroke);
		}
		if (this.paintFill != null) {
			canvas.drawCircle(pixelX, pixelY, radiusInPixel, this.paintFill);
		}
		return true;
	}

	/**
	 * @return the center point of this circle (may be null).
	 */
	public synchronized GeoPoint getGeoPoint() {
		return this.geoPoint;
	}

	/**
	 * @return the {@code Paint} used to fill this circle (may be null).
	 */
	public synchronized Paint getPaintFill() {
		return this.paintFill;
	}

	/**
	 * @return the {@code Paint} used to stroke this circle (may be null).
	 */
	public synchronized Paint getPaintStroke() {
		return this.paintStroke;
	}

	/**
	 * @return the non-negative radius of this circle in meters.
	 */
	public synchronized float getRadius() {
		return this.radius;
	}

	/**
	 * @param geoPoint
	 *            the new center point of this circle (may be null).
	 */
	public synchronized void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	/**
	 * @param paintFill
	 *            the new {@code Paint} used to fill this circle (may be null).
	 */
	public synchronized void setPaintFill(Paint paintFill) {
		this.paintFill = paintFill;
	}

	/**
	 * @param paintStroke
	 *            the new {@code Paint} used to stroke this circle (may be null).
	 */
	public synchronized void setPaintStroke(Paint paintStroke) {
		this.paintStroke = paintStroke;
	}

	/**
	 * @param radius
	 *            the new non-negative radius of this circle in meters.
	 * @throws IllegalArgumentException
	 *             if the given {@code radius} is negative.
	 */
	public synchronized void setRadius(float radius) {
		checkRadius(radius);
		this.radius = radius;
	}
}
