package br.org.archimedes.intersector.arcpolyline.tests;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.org.archimedes.Tester;
import br.org.archimedes.arc.Arc;
import br.org.archimedes.exceptions.InvalidArgumentException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.interfaces.Intersector;
import br.org.archimedes.intersector.arcpolyline.ArcPolylineIntersector;
import br.org.archimedes.model.Point;
import br.org.archimedes.polyline.Polyline;

public class ArcPolylineTests extends Tester {
	Arc testArc;
	Intersector intersector;
	
	@Before
	public void setUp() throws NullArgumentException, InvalidArgumentException {
		testArc = new Arc(new Point(0.0, 0.0), new Point(2.0, 2.0), new Point(4.0, 0.0));
		intersector = new ArcPolylineIntersector();
	}
	
	@Test
	public void nullArgumentsShouldRaiseException() throws NullArgumentException,
			InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(0.0, 3.0));
		polyPoints.add(new Point(3.0, 3.0));
		polyPoints.add(new Point(5.0, 0.0));
		Polyline testPoly = new Polyline(polyPoints);
		try {
			intersector.getIntersections(testPoly, null);
			fail("The otherElement is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}
		
		try {
			intersector.getIntersections(null, testPoly);
			fail("The element is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(testArc, null);
			fail("The otherElement is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(null, testArc);
			fail("The element is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(null, null);
			fail("Both elements are null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}
		Assert.assertTrue("Raised all exceptions", true);
	}
	
	@Test
	public void polylineNotIntersectingArcReturnsNoIntersections() throws NullArgumentException, InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(0.0, 3.0));
		polyPoints.add(new Point(3.0, 3.0));
		polyPoints.add(new Point(5.0, 0.0));
		Polyline testPoly = new Polyline(polyPoints);
		
		assertCollectionTheSame(Collections.EMPTY_LIST, intersector.getIntersections(testPoly, testArc));
	}
	
	@Test
	public void polylineIntersectsArcOnceReturnsOneIntersectionPoint() throws NullArgumentException, InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(-1.0, 1.0));
		polyPoints.add(new Point(1.0, -1.0));
		polyPoints.add(new Point(-2.0, -1.0));
		Polyline testPoly = new Polyline(polyPoints);
		
		Collection<Point> expected = new ArrayList<Point>();
		expected.add(new Point(0.0, 0.0));
		
		Collection<Point> real = intersector.getIntersections(testPoly, testArc);
		assertCollectionTheSame(expected, real);
	}
	
	@Test
	public void polylineIntersectsArcThreeTimesReturnsThreeIntersectionPoints() throws NullArgumentException, InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(0.0, 0.0));
		polyPoints.add(new Point(2.0, 2.0));
		polyPoints.add(new Point(4.0, 0.0));
		Polyline testPoly = new Polyline(polyPoints);
		
		Collection<Point> expected = polyPoints;
		
		Collection<Point> real = intersector.getIntersections(testPoly, testArc);
		assertCollectionTheSame(expected, real);
	}

}
