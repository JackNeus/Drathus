package drathus.game.com;

public class Vector2 {
	double x, y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2 f(Point p1, Point p2) {
		return new Vector2(p2.x - p1.x, p2.y - p1.y);
	}

	public static void normalize(Vector2 v) {
		double length = Math.sqrt(v.x * v.x + v.y * v.y);
		v.x /= length;
		v.y /= length;
	}

	public static Vector2 getForce(float x, float y, SolidEntity obstacle) {
		double magnitude = obstacle.mass / squareDistanceTo(obstacle, new Point(x, y));
		System.out.println(magnitude);
		Vector2 direction = new Vector2(obstacle.x - x, obstacle.y - y);
		normalize(direction);
		direction.x *= magnitude;
		direction.y *= magnitude;
		return direction;
	}

	public static double squareDistanceTo(SolidEntity obstacle, Point p) {
		Vector2 v = f(new Point(obstacle.x + obstacle.width / 2, obstacle.y + obstacle.height / 2), p);
		return v.x * v.x + v.y + v.y;
	}
}
