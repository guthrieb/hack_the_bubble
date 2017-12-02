import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MoveEngine extends Thread {
    private long timePassed = 0;
    private long curTime = 0;
    private long lastTime = 0;
    private double timeFraction = 0.0;
    private ArrayList<Accel> constForces = new ArrayList<Accel>();

    public void run() {
        Spawn player = new Spawn(0, -1);
        Main.objects.add(player);
        Main.objects.add(Spawn.generateArrow(player.getX() + 500, player.getY() + 500, 0, 0));
        curTime = System.currentTimeMillis();
        initializeConstForces();

        while (Main.isRunning) {
//		    if(Main.objects.size() == 0){
//                Spawn newArrow = Spawn.generateArrow(10, 980, 1000, -800);
//                Main.objects.add(newArrow);
//            }

            updatePlayerV();

            updateTime();
            applyConstForces();
            sumForces();
            moveEnts();

            try {
                sleep(1);
            }
            catch (InterruptedException e) {
            }
        }
    }


    private void updatePlayerV() {
        Spawn player;
        try {
            player = Main.objects.get(0);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Dragon hit an object or smth...");
            System.err.println("At the time of writing this message, there is a manual test.\n" +
                    "An arrow is added to the game at the same place where the player is added.\n" +
                    "This may cause the dragon to be removed from the list.");

            return;
        }

        if (Keys.leftDown) {
            player.rotation.rotateCoordinates(0.003);
        }

        if (Keys.rightDown) {
            player.rotation.rotateCoordinates(-0.003);
        }

        if (Keys.upDown) {

            double ax = Main.UPA * (Math.cos(player.rotation.angle()));
            double ay = Main.UPA * (Math.sin(player.rotation.angle()));

            player.addAccel(new Accel(ax, ay));
        }

    }

    private void updateTime() {
        lastTime = curTime;
        curTime = System.currentTimeMillis();
        timePassed = (curTime - lastTime);
        timeFraction = (timePassed / 1000.0);
    }


    private void initializeConstForces() {
        constForces.add(new Accel(0.0, Main.GRAVITY));
    }

    private synchronized void applyConstForces() {
        double xAccel = 0, yAccel = 0;
        // Find the total acceleration of all const forces.
        for (int i = 0; i < constForces.size(); i++) {
            xAccel += constForces.get(i).ax();
            yAccel += constForces.get(i).ay();
        }
        // Apply the sum acceleration to each entity.
        for (int i = 0; i < Main.objects.size(); i++) {
            Spawn s = Main.objects.get(i);
            s.addAccel(new Accel(xAccel, yAccel));
        }
    }

    private synchronized void sumForces() {
        for (int i = 0; i < Main.objects.size(); i++) {
            Spawn s = Main.objects.get(i);
            // Get the sum of all accelerations acting on object.
            Accel theAccel = s.sumAccel();
            // Apply the resulting change in velocity.
            double vx = s.vx() + (theAccel.ax() * timeFraction);
            double vy = s.vy() + (theAccel.ay() * timeFraction);
            s.updateVelocity(vx, vy);
        }
    }

    private synchronized void moveEnts() {
        for (int i = 0; i < Main.objects.size(); i++) {
            Spawn s = Main.objects.get(i);
            // Get the initial x and y coords.
            double oldX = s.getX(), oldY = s.getY();
            // Calculate the new x and y coords.
            double newX = oldX + (s.vx() * timeFraction);
            double newY = oldY + (s.vy() * timeFraction);
            s.updatePos(newX, newY);
            checkWallCollisions(s);
        }
        checkCollisions();
    }

    private synchronized void checkCollisions() {
        for (int i = 0; i < Main.objects.size() - 1; i++) {
            Spawn s = Main.objects.get(i);
            Point2D sCenter = s.getCenter();
            for (int j = i + 1; j < Main.objects.size(); j++) {
                Spawn t = Main.objects.get(j);
                if (t == null) break;
                Point2D tCenter = t.getCenter();
                double distBetween = sCenter.distance(tCenter);
                double bigR = s.getRadius() > t.getRadius() ? s.getRadius() : t
                        .getRadius();
                if (distBetween < (bigR * 2)) {
                    collide(s, t, distBetween);
                    updatehp(s, t);
                }
            }
        }
        ArrayList<Spawn> remove = new ArrayList<>();
        for (Spawn s : Main.objects) {
            if (s.hp <= 0) {
                remove.add(s);
            }
        }
        Main.objects.removeAll(remove);
    }

    private synchronized void updatehp(Spawn one, Spawn two) {
        if (one.type == TYPE.DRAGON || two.type == TYPE.DRAGON) {
            one.hp -= 2;
            two.hp -= 2;
        }
        else {
            one.hp -= 1;
            two.hp -= 1;
        }
    }

    private synchronized void collide(Spawn s, Spawn t, double distBetween) {
        // Get the relative x and y dist between them.
        double relX = s.getX() - t.getX();
        double relY = s.getY() - t.getY();
        // Take the arctan to find the collision angle.
        double collisionAngle = Math.atan2(relY, relX);
        // if (collisionAngle < 0) collisionAngle += 2 * Math.PI;
        // Rotate the coordinate systems for each object's velocity to align
        // with the collision angle. We do this by supplying the collision angle
        // to the vector's rotateCoordinates method.
        Vector2D sVel = s.velVector(), tVel = t.velVector();
        sVel.rotateCoordinates(collisionAngle);
        tVel.rotateCoordinates(collisionAngle);
        // In the collision coordinate system, the contact normals lie on the
        // x-axis. Only the velocity values along this axis are affected. We can
        // now apply a simple 1D momentum equation where the new x-velocity of
        // the first object equals a negative times the x-velocity of the
        // second.
        double swap = sVel.x;
        sVel.x = tVel.x;
        tVel.x = swap;
        // Now we need to get the vectors back into normal coordinate space.
        sVel.restoreCoordinates();
        tVel.restoreCoordinates();
        // Give each object its new velocity.
        s.updateVelocity(sVel.x * Main.BOUNCE, sVel.y * Main.BOUNCE);
        t.updateVelocity(tVel.x * Main.BOUNCE, tVel.y * Main.BOUNCE);
        // Back them up in the opposite angle so they are not overlapping.
        double minDist = s.getRadius() + t.getRadius();
        double overlap = minDist - distBetween;
        double toMove = overlap / 2;
        double newX = s.getX() + (toMove * Math.cos(collisionAngle));
        double newY = s.getY() + (toMove * Math.sin(collisionAngle));
        s.updatePos(newX, newY);
        newX = t.getX() - (toMove * Math.cos(collisionAngle));
        newY = t.getY() - (toMove * Math.sin(collisionAngle));
        t.updatePos(newX, newY);
    }

    private synchronized void checkWallCollisions(Spawn s) {
        int maxY = Main.screenHeight - s.dimY();
        int maxX = Main.screenWidth - s.dimX();

        if (s.getY() > maxY) {
		    if(s.type == TYPE.ARROW || s.type == TYPE.DRAGON || s.type == TYPE.CANNONBALL){
		        Main.objects.remove(s);
            }else{
                s.updatePos(s.getX(), maxY);
                s.updateVelocity(s.vx(), (s.vy() * -Main.BOUNCE));
            }
        }
		if (s.getX() > maxX) {
            if(s.type == TYPE.ARROW || s.type == TYPE.CANNONBALL){
                Main.objects.remove(s);
            }
            else {
                s.updatePos(maxX, s.getY());
                s.updateVelocity((s.vx() * -Main.BOUNCE), s.vy());
            }
        }
		if (s.getX() < 1) {
            if(s.type == TYPE.ARROW|| s.type == TYPE.CANNONBALL){
                Main.objects.remove(s);
            }
            else {
                s.updatePos(1, s.getY());
                s.updateVelocity((s.vx() * -Main.BOUNCE), s.vy());
            }
        }
    }
}

