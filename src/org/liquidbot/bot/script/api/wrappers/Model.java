package org.liquidbot.bot.script.api.wrappers;


import org.liquidbot.bot.Constants;
import org.liquidbot.bot.client.reflection.Reflection;
import org.liquidbot.bot.script.api.methods.data.Calculations;
import org.liquidbot.bot.script.api.methods.data.Game;
import org.liquidbot.bot.script.api.util.Random;


import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

/*
 * Created by Hiasat on 8/2/14
 */
public class Model {

	Object model;
	int orientation;

	int[] orginal_x;
	int[] orginal_z;

	int[] trianglesX;
	int[] trianglesZ;
	int[] trianglesY;
	int[] verticesX;
	int[] verticesZ;
	int[] verticesY;
	int gridX;
	int gridY;
	int z;

	public Model(Object model) {
		if (model != null) {
			this.orientation = 0;
			this.gridY = 0;
			this.gridX = 0;
			this.z = 0;
			this.model = model;
			set((int[]) Reflection.value("Model#getVerticesX()", model), (int[]) Reflection.value("Model#getVerticesY()", model), (int[]) Reflection.value("Model#getVerticesZ()", model), (int[]) Reflection.value("Model#getTrianglesX()", model), (int[]) Reflection.value("Model#getTrianglesY()", model), (int[]) Reflection.value("Model#getTrianglesZ()", model), this.orientation);
		}
	}

	public Model(Model wrapper, int orientation, int x, int y, int z) {
		this.orientation = orientation;
		this.gridY = y;
		this.gridX = x;
		this.z = z;
		set(wrapper.getXVertices(), wrapper.getYVertices(), wrapper.getZVertices(), wrapper.getXTriangles(), wrapper.getYTriangles(), wrapper.getZTriangles(), this.orientation);
	}


	public void set(int[] x, int[] y, int[] z, int[] trianglesX, int[] trianglesY, int[] trianglesZ, int orientation) {
		this.orientation = orientation;
		this.trianglesX = Arrays.copyOf(trianglesX, trianglesX.length);
		this.trianglesY = Arrays.copyOf(trianglesY, trianglesY.length);
		this.trianglesZ = Arrays.copyOf(trianglesZ, trianglesZ.length);
		this.verticesX = Arrays.copyOf(x, x.length);
		this.verticesY = Arrays.copyOf(y, y.length);
		this.verticesZ = Arrays.copyOf(z, z.length);


		if (orientation != 0) {
			orginal_x = new int[verticesX.length];
			orginal_z = new int[verticesZ.length];
			orginal_x = Arrays.copyOfRange(this.verticesX, 0, verticesX.length);
			orginal_z = Arrays.copyOfRange(this.verticesZ, 0, verticesZ.length);
			verticesX = new int[orginal_x.length];
			verticesZ = new int[orginal_z.length];
			int theta = orientation & 0x3fff;
			int sin = Calculations.SINE[theta];
			int cos = Calculations.COSINE[theta];
			for (int i = 0; i < orginal_x.length; ++i) {
				verticesX[i] = (orginal_x[i] * cos + orginal_z[i] * sin >> 15) >> 1;
				verticesZ[i] = (orginal_z[i] * cos - orginal_x[i] * sin >> 15) >> 1;
			}
		}
	}

	public void draw(Graphics2D graphics, Color color) {
		if (Game.isLoggedIn() && isOnScreen()) {
			graphics.setColor(color);
			Polygon[] tangles = getTriangles();
			for (Polygon triangle : tangles) {
				graphics.draw(triangle);
			}

			graphics.setColor(Color.YELLOW);

		}
	}

	public Polygon[] getTriangles() {
		LinkedList<Polygon> polygons = new LinkedList<>();

		int[] indices1 = getXTriangles();
		int[] indices2 = getYTriangles();
		int[] indices3 = getZTriangles();

		int[] xPoints = getXVertices();
		int[] yPoints = getYVertices();
		int[] zPoints = getZVertices();

		int len = indices1.length;


		for (int i = 0; i < len; ++i) {
			Point p1 = Calculations.worldToScreen(gridX + xPoints[indices1[i]], gridY + zPoints[indices1[i]], -yPoints[indices1[i]] + z);
			Point p2 = Calculations.worldToScreen(gridX + xPoints[indices2[i]], gridY + zPoints[indices2[i]], -yPoints[indices2[i]] + z);
			Point p3 = Calculations.worldToScreen(gridX + xPoints[indices3[i]], gridY + zPoints[indices3[i]], -yPoints[indices3[i]] + z);

			if (p1.x >= 0 && p2.x >= 0 && p3.x >= 0) {
				polygons.add(new Polygon(new int[]{p1.x, p2.x, p3.x}, new int[]{p1.y, p2.y, p3.y}, 3));
			}
		}

		return polygons.toArray(new Polygon[polygons.size()]);
	}

	public Point getRandomPoint() {
		Polygon[] triangles = getTriangles();
		if (triangles.length > 0) {
			for (int i = 0; i < 100; i++) {
				Polygon p = triangles[Random.nextInt(0, triangles.length)];
				Point point = new Point(p.xpoints[Random.nextInt(0, p.xpoints.length)], p.ypoints[Random.nextInt(0, p.ypoints.length)]);
				if (Constants.VIEWPORT.contains(point))
					return point;
			}
		}

		return new Point(-1, -1);
	}

	public boolean contains(int x, int y) {
		for (final Polygon polygon : getTriangles()) {
			if (polygon.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}

	public int[] getXTriangles() {
		return trianglesX;
	}

	public int[] getYTriangles() {
		return trianglesY;
	}

	public int[] getZTriangles() {
		return trianglesZ;
	}

	public int[] getXVertices() {
		return verticesX;
	}

	public int[] getYVertices() {
		return verticesY;
	}

	public int[] getZVertices() {
		return verticesZ;
	}

	public boolean isOnScreen() {
		return Constants.VIEWPORT.contains(getRandomPoint());
	}

	public boolean isValid(){
		return model !=null;
	}

}
