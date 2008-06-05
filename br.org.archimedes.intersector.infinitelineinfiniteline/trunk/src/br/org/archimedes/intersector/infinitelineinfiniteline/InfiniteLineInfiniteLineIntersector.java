package br.org.archimedes.intersector.infinitelineinfiniteline;

import java.util.Collection;
import java.util.LinkedList;

import br.org.archimedes.Constant;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.infiniteline.InfiniteLine;
import br.org.archimedes.intersections.interfaces.Intersector;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Point;

public class InfiniteLineInfiniteLineIntersector implements Intersector{
	
	public Collection<Point> getIntersections(Element element, Element otherElement) throws NullArgumentException {
		InfiniteLine infiniteLine = (InfiniteLine) element;
		InfiniteLine otherInfiniteLine = (InfiniteLine) otherElement;
		
		if (element == null || otherElement == null)
			throw new NullArgumentException();
		
		Collection<Point> intersectionPoints = new LinkedList<Point>();
		
		if (isParallelTo(infiniteLine, otherInfiniteLine)) {
            return intersectionPoints;
        }

        // The first line will be represented by a1x + b1y + c1 = 0
        // The second line will be represented by a2x + b2y + c2 = 0
        double a1, b1, c1;
        double a2, b2, c2;

        Point initialPoint = infiniteLine.getInitialPoint();
        Point endingPoint = infiniteLine.getEndingPoint();

        Point secondInitialPoint = otherInfiniteLine.getInitialPoint();
        Point secondEndingPoint = otherInfiniteLine.getEndingPoint();
        
        a1 = initialPoint.getY() - endingPoint.getY();
        b1 = endingPoint.getX() - initialPoint.getX();
        c1 = (initialPoint.getX() - endingPoint.getX()) * initialPoint.getY()
                + (endingPoint.getY() - initialPoint.getY())
                * initialPoint.getX();

        a2 = secondInitialPoint.getY() - secondEndingPoint.getY();
        b2 = secondEndingPoint.getX() - secondInitialPoint.getX();
        c2 = (secondInitialPoint.getX() - secondEndingPoint.getX())
                * secondInitialPoint.getY()
                + (secondEndingPoint.getY() - secondInitialPoint.getY())
                * secondInitialPoint.getX();

        if ( !((Math.abs(a1 * b2 - b1 * a2) <= Constant.EPSILON) && (Math
                .abs(c1 - c2) <= Constant.EPSILON))) {

            double yIntersection = 0.0;
            double xIntersection = 0.0;

            if (Math.abs(a1) <= Constant.EPSILON) {
                yIntersection = -(c1 / b1);
                xIntersection = -(b2 * yIntersection + c2) / a2;
            }
            else if (Math.abs(a2) <= Constant.EPSILON) {
                yIntersection = -(c2 / b2);
                xIntersection = -(b1 * yIntersection + c1) / a1;
            }
            else if (Math.abs(b1) <= Constant.EPSILON) {
                xIntersection = -(c1 / a1);
                yIntersection = -(a2 * xIntersection + c2) / b2;
            }
            else if (Math.abs(b2) <= Constant.EPSILON) {
                xIntersection = -(c2 / a2);
                yIntersection = -(a1 * xIntersection + c1) / b1;
            }
            else {
                yIntersection = ((c1 * a2) - (c2 * a1))
                        / ((b2 * a1) - (b1 * a2));
                xIntersection = -(c1 / a1) - (b1 / a1) * yIntersection;
            }
            Point intersection = new Point(xIntersection, yIntersection);
            if (infiniteLine.contains(intersection) && otherInfiniteLine.contains(intersection))
            	intersectionPoints.add(intersection);
        }

        return intersectionPoints;
	}
	
    public boolean isParallelTo (Element element1, Element element2) {

        boolean isParallel = false;

        InfiniteLine infiniteLine = ((InfiniteLine)element1);
        InfiniteLine otherInfiniteLine = (InfiniteLine)element2;

        if (infiniteLine != null && otherInfiniteLine != null) {
            double deltay1 = infiniteLine.getInitialPoint().getY()
                    - infiniteLine.getEndingPoint().getY();
            double deltax1 = infiniteLine.getInitialPoint().getX()
                    - infiniteLine.getEndingPoint().getX();
            double deltay2 = otherInfiniteLine.getInitialPoint().getY()
                    - otherInfiniteLine.getEndingPoint().getY();
            double deltax2 = otherInfiniteLine.getInitialPoint().getX()
                    - otherInfiniteLine.getEndingPoint().getX();

            if (Math.abs(deltax1) <= Constant.EPSILON
                    && Math.abs(deltax2) <= Constant.EPSILON) {
                isParallel = true;
            }
            else {
                double m1 = deltay1 / deltax1;
                double m2 = deltay2 / deltax2;
                if (Math.abs(m1 - m2) <= Constant.EPSILON) {
                    isParallel = true;
                }
            }

        }

        return isParallel;
    }

}
